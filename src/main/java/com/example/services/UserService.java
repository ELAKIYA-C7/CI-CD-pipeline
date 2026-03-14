package com.example.services;

import com.example.models.User;
import com.example.models.UserProfile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Service class for managing users with file-based persistence.
 * Data is stored in 'users.csv' in the application root.
 */
public class UserService {
    private static final String DATA_FILE = "users.csv";
    private static final String PROFILES_FILE = "user_profiles.csv";
    // Thread-safe in-memory cache
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final Map<String, UserProfile> profiles = new ConcurrentHashMap<>();
    private static final ObservableList<User> observableUsers = FXCollections.observableArrayList();

    static {
        loadUsers();
        loadUserProfiles();
        // Ensure default users exist if the file was empty or didn't exist
        if (users.isEmpty()) {
            addUser(new User("john_doe", "john@example.com", "John Doe", "", "User", "Active", "password123"));
            addUser(new User("admin", "admin@example.com", "System Admin", "+1 (555) 999-9999", "Admin", "Active", "admin123"));
        }
    }

    public static boolean addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        observableUsers.add(user);
        saveUsers();
        return true;
    }

    public static boolean verifyPassword(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public static User getUserByUsername(String username) {
        return users.get(username);
    }

    // Used by AdminPage for data binding
    public static ObservableList<User> getAllUsers() {
        return observableUsers;
    }

    // Used by AdminPage for statistics
    public static int getTotalUsers() {
        return users.size();
    }

    // Used by AdminPage for statistics
    public static int getActiveUsers() {
        return (int) users.values().stream()
                .filter(u -> "Active".equalsIgnoreCase(u.getStatus()))
                .count();
    }

    // Used by AdminPage to update user details
    public static boolean updateUser(User user) {
        if (users.containsKey(user.getUsername())) {
            users.put(user.getUsername(), user);
            for (int i = 0; i < observableUsers.size(); i++) {
                if (observableUsers.get(i).getUsername().equals(user.getUsername())) {
                    observableUsers.set(i, user);
                    break;
                }
            }
            saveUsers();
            return true;
        }
        return false;
    }

    // Used by AdminPage to delete users
    public static boolean removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            observableUsers.removeIf(u -> u.getUsername().equals(username));
            saveUsers();
            return true;
        }
        return false;
    }

    public static boolean updatePassword(String username, String newPassword) {
        User user = users.get(username);
        if (user != null) {
            User updated = new User(
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                newPassword
            );
            users.put(username, updated);
            for (int i = 0; i < observableUsers.size(); i++) {
                if (observableUsers.get(i).getUsername().equals(username)) {
                    observableUsers.set(i, updated);
                    break;
                }
            }
            saveUsers();
            sendPasswordChangeNotification(user.getEmail(), username);
            return true;
        }
        return false;
    }

    // Used by AdminPage to reset user passwords
    public static boolean resetPassword(String username) {
        User user = users.get(username);
        if (user != null) {
            return updatePassword(username, "password123");
        }
        return false;
    }

    public static boolean updatePasswordByEmail(String email, String newPassword) {
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return updatePassword(user.getUsername(), newPassword);
            }
        }
        return false;
    }

    public static UserProfile getUserProfile(String username) {
        return profiles.getOrDefault(username, new UserProfile(username, "", "", ""));
    }

    public static void updateUserProfile(UserProfile profile) {
        if (profile != null && profile.getUsername() != null) {
            profiles.put(profile.getUsername(), profile);
            saveUserProfiles();
        }
    }

    private static void sendPasswordChangeNotification(String email, String username) {
        // NOTE: This requires the 'com.sun.mail:javax.mail' dependency in pom.xml
        final String fromEmail = "your_app_email@gmail.com"; // TODO: Configure sender email
        final String password = "your_app_password";         // TODO: Configure app password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Security Alert - Password Changed");
            message.setText("Hello " + username + ",\n\n"
                    + "Your password has been successfully changed.\n"
                    + "If this wasn't you, please contact support immediately.");

            Transport.send(message);
            System.out.println("📧 [EMAIL SENT] To: " + email);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadUsers() {
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: username|email|fullName|phone|role|status|password
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    User u = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    users.put(u.getUsername(), u);
                    observableUsers.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private static void saveUsers() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DATA_FILE))) {
            for (User u : users.values()) {
                String line = String.join("|",
                    u.getUsername(),
                    u.getEmail(),
                    u.getFullName(),
                    u.getPhone(),
                    u.getRole(),
                    u.getStatus(),
                    u.getPassword()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private static void loadUserProfiles() {
        Path path = Paths.get(PROFILES_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: username|jobTitle|location|bio
                String[] parts = line.split("\\|", -1); // -1 to keep empty trailing strings
                if (parts.length >= 4) {
                    String bio = parts[3].replace("\\n", "\n");
                    UserProfile p = new UserProfile(parts[0], parts[1], parts[2], bio);
                    profiles.put(p.getUsername(), p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading profiles: " + e.getMessage());
        }
    }

    private static void saveUserProfiles() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PROFILES_FILE))) {
            for (UserProfile p : profiles.values()) {
                String line = p.getUsername() + "|" + p.getJobTitle() + "|" + p.getLocation() + "|" + p.getBioSafe();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving profiles: " + e.getMessage());
        }
    }
}