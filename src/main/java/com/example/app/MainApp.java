package com.example.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.example.*;
import com.example.services.UserService;
import com.example.models.User;
import java.util.prefs.Preferences;

/**
 * Main JavaFX Application - Professional Multi-Page Portal
 * 
 * Features:
 * - Advanced login with role-based authentication
 * - Dashboard with statistics and real-time activity
 * - User profile management
 * - Settings with theme support (dark/light)
 * - Admin panel with authorization checks
 * - Reports generation
 * 
 * Security:
 * - Role-based access control (User, Manager, Admin)
 * - User session management
 * - Admin authorization for sensitive operations
 */
public class MainApp extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private CheckBox rememberMe;
    private Label statusLabel;
    private SceneManager sceneManager;
    private UserRole currentUserRole;
    private String currentUsername;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Professional Portal - Login");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        
        // Initialize scene manager for navigation
        sceneManager = new SceneManager(primaryStage);

        // Create all scenes
        Scene loginScene = createLoginScene();
        sceneManager.setLoginScene(loginScene);
        
        // Create signup scene
        SignupPage signupPage = new SignupPage(sceneManager, () -> sceneManager.showLoginScene());
        Scene signupScene = signupPage.createScene();
        sceneManager.setSignupScene(signupScene);

        // Create reset password scene
        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(sceneManager);
        Scene resetPasswordScene = resetPasswordPage.createScene();
        sceneManager.setResetPasswordScene(resetPasswordScene);
        
        // Initialize other scenes (will be created lazily when needed)
        
        // Show login scene initially
        sceneManager.showLoginScene();
        primaryStage.show();
    }

    /**
     * Create the login scene with modern gradient design - ENHANCED
     */
    /**
     * Create the login scene with modern structured CSS - PREMIUM
     */
    private Scene createLoginScene() {
        VBox root = new VBox();
        root.getStyleClass().add("login-root");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Card Container
        VBox cardContainer = new VBox(0);
        cardContainer.setMaxWidth(420);
        cardContainer.getStyleClass().add("login-card");

        // Header
        VBox header = new VBox(10);
        header.getStyleClass().add("login-header");
        
        Label titleLabel = new Label("Welcome Back");
        titleLabel.getStyleClass().add("h1");
        
        Label subtitleLabel = new Label("Sign in to your dashboard");
        subtitleLabel.getStyleClass().add("subtitle");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Content
        VBox formContent = new VBox(20);
        formContent.setPadding(new Insets(30, 40, 10, 40));
        formContent.setAlignment(Pos.CENTER_LEFT);

        // Username
        Label userLbl = new Label("Username");
        userLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.getStyleClass().add("text-field");

        // Password
        Label passLbl = new Label("Password");
        passLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.getStyleClass().add("password-field");

        // Remember Me
        rememberMe = new CheckBox("Remember me");
        rememberMe.setStyle("-fx-text-fill: #7f8c8d; -fx-cursor: hand;");

        // Status
        statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-font-size: 13px;");

        // Login Button (Green/Success)
        Button loginButton = new Button("LOGIN");
        loginButton.getStyleClass().addAll("button", "btn-success");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> handleLogin());

        formContent.getChildren().addAll(
            userLbl, usernameField,
            passLbl, passwordField,
            rememberMe,
            statusLabel,
            loginButton
        );

        // Footer
        VBox footer = new VBox(10);
        footer.getStyleClass().add("login-footer");
        
        Hyperlink forgotPass = new Hyperlink("Forgot Password?");
        forgotPass.setStyle("-fx-text-fill: #667eea;");
        
        Hyperlink createAcc = new Hyperlink("Create Account");
        createAcc.setStyle("-fx-text-fill: #667eea; -fx-font-weight: bold;");

        footer.getChildren().addAll(forgotPass, createAcc);

        // Assemble
        cardContainer.getChildren().addAll(header, formContent, footer);
        root.getChildren().add(cardContainer);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/modern-theme.css").toExternalForm());
        return scene;
    }
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            ValidationResult result = LoginValidator.validateCredentials(username, password);

            if (result.isValid()) {
                // Verify password against stored user password
                if (!UserService.verifyPassword(username, password)) {
                    statusLabel.setStyle("-fx-text-fill: #d9534f;");
                    statusLabel.setText("✗ Invalid username or password");
                    return;
                }

                statusLabel.setStyle("-fx-text-fill: #5cb85c;");
                statusLabel.setText("✓ Login successful! Welcome, " + username);
                
                // Handle Remember Me
                Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
                if (rememberMe.isSelected()) {
                    prefs.put("user_" + username, password);
                } else {
                    prefs.remove("user_" + username);
                }

                // Clear fields after successful login
                usernameField.clear();
                passwordField.clear();
                
                // Determine and propagate user role for the session
                try {
                    User found = UserService.getUserByUsername(username);
                    if (found != null) {
                        String roleStr = found.getRole();
                        if ("Admin".equalsIgnoreCase(roleStr)) {
                            sceneManager.setCurrentUserRole(com.example.UserRole.ADMIN);
                        } else if ("Manager".equalsIgnoreCase(roleStr)) {
                            sceneManager.setCurrentUserRole(com.example.UserRole.MANAGER);
                        } else {
                            sceneManager.setCurrentUserRole(com.example.UserRole.USER);
                        }
                    } else {
                        // fallback to regular user
                        sceneManager.setCurrentUserRole(com.example.UserRole.USER);
                    }
                } catch (Exception ex) {
                    // ignore role propagation failures
                }

                // Create dashboard scene and show it
                DashboardPage dashboardPage = new DashboardPage(username, sceneManager);
                Scene dashboardScene = dashboardPage.createScene();
                sceneManager.setDashboardScene(dashboardScene);
                
                // Create other pages
                ProfilePage profilePage = new ProfilePage(username, sceneManager);
                Scene profileScene = profilePage.createScene();
                sceneManager.setProfileScene(profileScene);
                
                SettingsPage settingsPage = new SettingsPage(username, sceneManager);
                Scene settingsScene = settingsPage.createScene();
                sceneManager.setSettingsScene(settingsScene);
                
                AdminPage adminPage = new AdminPage(username, sceneManager);
                Scene adminScene = adminPage.createScene();
                sceneManager.setAdminScene(adminScene);
                
                // Navigate to dashboard
                sceneManager.showDashboardScene();
            } else {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ " + result.getErrorMessage());
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #d9534f;");
            statusLabel.setText("✗ An error occurred: " + e.getMessage());
        }
    }

    /**
     * Entry point of the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
