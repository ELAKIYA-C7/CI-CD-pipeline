package com.example.app;

import com.example.SceneManager;
import com.example.models.User;
import com.example.models.UserProfile;
import com.example.services.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import java.io.File;

public class ProfilePage {
    private final String username;
    private final SceneManager sceneManager;
    private TextField emailField;
    private TextField fullNameField;
    private TextField phoneField;
    private TextField jobTitleField; // New option
    private TextField locationField; // New option
    private TextArea bioArea;        // New option
    private ComboBox<String> countryCodeBox;
    private Label statusLabel;
    private Label roleLabel;
    private Label accountStatusLabel;
    private Circle avatarCircle;
    private Label avatarInitials;
    private ContextMenu avatarMenu;
    
    // Fields to track initial state for unsaved changes detection
    private String initialEmail = "";
    private String initialFullName = "";
    private String initialPhoneCode = "";
    private String initialPhoneNumber = "";
    private String initialJobTitle = "";
    private String initialLocation = "";
    private String initialBio = "";

    public ProfilePage(String username, SceneManager sceneManager) {
        this.username = username;
        this.sceneManager = sceneManager;
    }

    public Scene createScene() {
        VBox root = new VBox(0);
        root.setAlignment(Pos.TOP_CENTER);

        // Header
        VBox header = new VBox(10);
        header.getStyleClass().add("profile-header");
        
        Label titleLabel = new Label("My Profile");
        titleLabel.getStyleClass().add("profile-title");
        
        Label subtitleLabel = new Label("Manage your personal information and account settings");
        subtitleLabel.getStyleClass().add("profile-subtitle");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Main Content Area
        HBox content = new HBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        
        // Left Sidebar (Profile Summary)
        VBox sidebar = createSidebar();
        
        // Right Content (Edit Form)
        VBox mainForm = createMainForm();
        HBox.setHgrow(mainForm, Priority.ALWAYS);

        content.getChildren().addAll(sidebar, mainForm);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().addAll(header, scrollPane);

        // Load initial data
        loadUserData();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/profile.css").toExternalForm());
        return scene;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(300);
        sidebar.setMinWidth(300);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(30));
        sidebar.getStyleClass().add("card");

        // Avatar
        StackPane avatarStack = new StackPane();
        avatarCircle = new Circle(60);
        avatarCircle.getStyleClass().add("avatar-circle");
        
        avatarInitials = new Label("U");
        avatarInitials.getStyleClass().add("avatar-initials");
        
        avatarStack.getChildren().addAll(avatarCircle, avatarInitials);

        // Avatar interaction
        avatarStack.setCursor(javafx.scene.Cursor.HAND);
        Tooltip.install(avatarStack, new Tooltip("Click to manage photo"));
        
        avatarStack.setOnMouseEntered(e -> {
            avatarCircle.setOpacity(0.8);
            avatarStack.setScaleX(1.05);
            avatarStack.setScaleY(1.05);
        });
        avatarStack.setOnMouseExited(e -> {
            avatarCircle.setOpacity(1.0);
            avatarStack.setScaleX(1.0);
            avatarStack.setScaleY(1.0);
        });

        avatarMenu = new ContextMenu();
        updateAvatarMenu();

        avatarStack.setOnMouseClicked(e -> avatarMenu.show(avatarStack, e.getScreenX(), e.getScreenY()));

        // Username & Role
        Label userLabel = new Label("@" + username);
        userLabel.getStyleClass().add("user-handle");

        roleLabel = new Label("User");
        roleLabel.getStyleClass().add("role-badge");

        accountStatusLabel = new Label("Active");
        accountStatusLabel.getStyleClass().add("status-badge");

        HBox badges = new HBox(10);
        badges.setAlignment(Pos.CENTER);
        badges.getChildren().addAll(roleLabel, accountStatusLabel);

        Separator sep = new Separator();

        // Quick Stats (Mock data for elegance)
        VBox stats = new VBox(15);
        stats.getChildren().addAll(
            createStatRow("📅 Joined", "Jan 2024"),
            createStatRow("🕒 Last Login", "Today, 10:30 AM"),
            createStatRow("🛡️ Security", "High")
        );

        sidebar.getChildren().addAll(avatarStack, userLabel, badges, sep, stats);
        return sidebar;
    }

    private HBox createStatRow(String label, String value) {
        HBox row = new HBox();
        Label l = new Label(label);
        l.getStyleClass().add("stat-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label v = new Label(value);
        v.getStyleClass().add("stat-value");
        row.getChildren().addAll(l, spacer, v);
        return row;
    }

    private VBox createMainForm() {
        VBox formContainer = new VBox(25);
        formContainer.setPadding(new Insets(30));
        formContainer.getStyleClass().add("card");

        Label sectionTitle = new Label("📝 Edit Profile Details");
        sectionTitle.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // Username (Read-only)
        Label userLbl = new Label("Username");
        userLbl.getStyleClass().add("field-label");
        TextField userField = new TextField(username);
        userField.setEditable(false);
        userField.getStyleClass().add("profile-input");
        grid.add(userLbl, 0, 0);
        grid.add(userField, 0, 1);

        // Email
        Label emailLbl = new Label("Email Address");
        emailLbl.getStyleClass().add("field-label");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("profile-input");
        grid.add(emailLbl, 1, 0);
        grid.add(emailField, 1, 1);

        // Full Name
        Label nameLbl = new Label("Full Name");
        nameLbl.getStyleClass().add("field-label");
        fullNameField = new TextField();
        fullNameField.setPromptText("Enter your full name");
        fullNameField.getStyleClass().add("profile-input");
        grid.add(nameLbl, 0, 2);
        grid.add(fullNameField, 0, 3);

        // Phone Number with Country Code
        Label phoneLbl = new Label("Phone Number");
        phoneLbl.getStyleClass().add("field-label");
        
        HBox phoneBox = new HBox(10);
        countryCodeBox = new ComboBox<>();
        countryCodeBox.getItems().addAll("+1", "+44", "+91", "+61", "+81", "+49", "+33", "+86");
        countryCodeBox.setPromptText("Code");
        countryCodeBox.setPrefWidth(110);
        countryCodeBox.getStyleClass().add("profile-input");
        
        phoneField = new TextField();
        phoneField.setPromptText("Contact Number");
        phoneField.getStyleClass().add("profile-input");
        HBox.setHgrow(phoneField, Priority.ALWAYS);
        
        phoneBox.getChildren().addAll(countryCodeBox, phoneField);
        grid.add(phoneLbl, 1, 2);
        grid.add(phoneBox, 1, 3);

        // Additional Options (New Fields)
        Label jobLbl = new Label("Job Title");
        jobLbl.getStyleClass().add("field-label");
        jobTitleField = new TextField();
        jobTitleField.setPromptText("e.g. Software Engineer");
        jobTitleField.getStyleClass().add("profile-input");
        grid.add(jobLbl, 0, 4);
        grid.add(jobTitleField, 0, 5);

        Label locLbl = new Label("Location");
        locLbl.getStyleClass().add("field-label");
        locationField = new TextField();
        locationField.setPromptText("e.g. New York, USA");
        locationField.getStyleClass().add("profile-input");
        grid.add(locLbl, 1, 4);
        grid.add(locationField, 1, 5);

        Label bioLbl = new Label("Bio / About Me");
        bioLbl.getStyleClass().add("field-label");
        bioArea = new TextArea();
        bioArea.setPromptText("Tell us a little about yourself...");
        bioArea.setPrefRowCount(3);
        bioArea.setWrapText(true);
        bioArea.getStyleClass().add("profile-input");
        
        grid.add(bioLbl, 0, 6);
        grid.add(bioArea, 0, 7, 2, 1); // Span 2 columns

        // Status Label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 13px;");

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button saveButton = new Button("Save Changes");
        saveButton.getStyleClass().add("save-button");
        saveButton.setOnAction(e -> saveProfile());

        Button backButton = new Button("Back to Dashboard");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> sceneManager.showDashboardScene());

        buttonBox.getChildren().addAll(backButton, saveButton);

        formContainer.getChildren().addAll(sectionTitle, grid, statusLabel, buttonBox);
        return formContainer;
    }

    private void loadUserData() {
        User user = UserService.getUserByUsername(username);
        if (user != null) {
            emailField.setText(user.getEmail());
            fullNameField.setText(user.getFullName());
            
            // Update Sidebar Info
            roleLabel.setText(user.getRole());
            accountStatusLabel.setText(user.getStatus());
            
            // Initials for avatar
            String name = user.getFullName();
            if (name != null && !name.isEmpty()) {
                String initials = name.substring(0, 1).toUpperCase();
                if (name.contains(" ")) initials += name.split(" ")[1].substring(0, 1).toUpperCase();
                avatarInitials.setText(initials);
            }

            String phone = user.getPhone();
            if (phone != null && !phone.isEmpty()) {
                // Try to split country code and number
                boolean codeFound = false;
                for (String code : countryCodeBox.getItems()) {
                    if (phone.startsWith(code)) {
                        countryCodeBox.setValue(code);
                        phoneField.setText(phone.substring(code.length()).trim());
                        codeFound = true;
                        break;
                    }
                }
                if (!codeFound) {
                    countryCodeBox.setValue("+1"); // Default
                    phoneField.setText(phone);
                }
            } else {
                countryCodeBox.setValue("+1"); // Default
                phoneField.setText("");
            }

            // Load extended profile data
            UserProfile profile = UserService.getUserProfile(username);
            jobTitleField.setText(profile.getJobTitle());
            locationField.setText(profile.getLocation());
            bioArea.setText(profile.getBio());
            
            // Capture initial state
            initialEmail = emailField.getText();
            initialFullName = fullNameField.getText();
            initialPhoneCode = countryCodeBox.getValue() != null ? countryCodeBox.getValue() : "+1";
            initialPhoneNumber = phoneField.getText();
            initialJobTitle = jobTitleField.getText();
            initialLocation = locationField.getText();
            initialBio = bioArea.getText();
        }
    }

    private void saveProfile() {
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        String code = countryCodeBox.getValue();
        String number = phoneField.getText();
        String jobTitle = jobTitleField.getText();
        String location = locationField.getText();
        String bio = bioArea.getText();
        
        String fullPhone = "";
        if (code != null && number != null && !number.trim().isEmpty()) {
            fullPhone = code + " " + number.trim();
        }

        User currentUser = UserService.getUserByUsername(username);
        if (currentUser != null) {
            User updatedUser = new User(username, email, fullName, fullPhone, currentUser.getRole(), currentUser.getStatus(), currentUser.getPassword());
            if (UserService.updateUser(updatedUser)) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("✓ Profile updated successfully!");
                
                // Save extended profile data
                UserProfile profile = new UserProfile(username, jobTitle, location, bio);
                UserService.updateUserProfile(profile);
                
                // Update avatar initials if name changed
                if (fullName != null && !fullName.isEmpty()) {
                    String initials = fullName.substring(0, 1).toUpperCase();
                    if (fullName.contains(" ")) initials += fullName.split(" ")[1].substring(0, 1).toUpperCase();
                    avatarInitials.setText(initials);
                }
                
                // Update initial state to match saved state
                initialEmail = email;
                initialFullName = fullName;
                initialPhoneCode = code != null ? code : "";
                initialPhoneNumber = number;
                initialJobTitle = jobTitle;
                initialLocation = location;
                initialBio = bio;
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("✗ Failed to update profile.");
            }
        }
    }

    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                setAvatarFromUrl(selectedFile.toURI().toString());
            } catch (Exception e) {
                statusLabel.setText("✗ Failed to load image");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void setAvatarFromUrl(String url) {
        // Use inline style to override CSS class
        avatarCircle.setStyle("-fx-fill: url('" + url + "');");
        avatarInitials.setVisible(false);
        statusLabel.setText("✓ Avatar updated");
        statusLabel.setStyle("-fx-text-fill: green;");
        updateAvatarMenu();
    }

    private void handleRemovePhoto() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Profile Photo");
        alert.setHeaderText("Are you sure you want to remove your profile photo?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                avatarCircle.setStyle(""); // Clear inline style to revert to CSS
                avatarInitials.setVisible(true);
                statusLabel.setText("✓ Photo removed");
                statusLabel.setStyle("-fx-text-fill: green;");
                updateAvatarMenu();
            }
        });
    }

    private void updateAvatarMenu() {
        avatarMenu.getItems().clear();

        if (avatarInitials.isVisible()) {
            MenuItem addItem = new MenuItem("Add Picture");
            addItem.setOnAction(e -> handleUploadPhoto());
            avatarMenu.getItems().add(addItem);
        } else {
            MenuItem changeItem = new MenuItem("Change Picture");
            changeItem.setOnAction(e -> handleUploadPhoto());

            MenuItem removeItem = new MenuItem("Remove Picture");
            removeItem.setOnAction(e -> handleRemovePhoto());

            avatarMenu.getItems().addAll(changeItem, removeItem);
        }
    }

    private void handleBackNavigation() {
        if (hasUnsavedChanges()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Are you sure you want to discard them and go back?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    sceneManager.showDashboardScene();
                }
            });
        } else {
            sceneManager.showDashboardScene();
        }
    }

    private boolean hasUnsavedChanges() {
        String currentCode = countryCodeBox.getValue();
        if (currentCode == null) currentCode = "";
        
        return !emailField.getText().equals(initialEmail) ||
               !fullNameField.getText().equals(initialFullName) ||
               !currentCode.equals(initialPhoneCode) ||
               !phoneField.getText().equals(initialPhoneNumber) ||
               !jobTitleField.getText().equals(initialJobTitle) ||
               !locationField.getText().equals(initialLocation) ||
               !bioArea.getText().equals(initialBio);
    }
}