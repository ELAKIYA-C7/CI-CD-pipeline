package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.models.User;
import com.example.services.UserService;
import com.example.app.LoginValidator;
import com.example.app.ValidationResult;

/**
 * Signup page for new user registration with email collection.
 */
public class SignupPage {
    private SceneManager sceneManager;
    private Runnable onSignupSuccess;

    public SignupPage(SceneManager sceneManager, Runnable onSignupSuccess) {
        this.sceneManager = sceneManager;
        this.onSignupSuccess = onSignupSuccess;
    }

    public Scene createScene() {
        VBox root = new VBox();
        root.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));

        // Main card container
        VBox cardContainer = new VBox(0);
        cardContainer.setPrefWidth(480);
        cardContainer.getStyleClass().add("card");
        cardContainer.setStyle("-fx-border-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 30, 0.8, 0, 15);");

        // Header section
        VBox header = new VBox(8);
        header.setPadding(new Insets(50, 30, 35, 30));
        header.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -fx-border-radius: 20 20 0 0;");
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("📝 Create Account");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Join Our Professional Portal");
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #e8e8f0; -fx-font-style: italic;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form content
        VBox formContent = new VBox(20);
        formContent.setPadding(new Insets(35));
        formContent.setStyle("-fx-background-color: white;");
        formContent.setAlignment(Pos.TOP_CENTER);

        // Username field
        Label usernameLabel = new Label("👤 Username");
        usernameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setStyle(
            "-fx-padding: 14; " +
            "-fx-font-size: 13; " +
            "-fx-border-color: #dfe6e9; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #2c3e50;"
        );
        usernameField.setPrefHeight(50);

        // Email field - NEW
        Label emailLabel = new Label("📧 Email");
        emailLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setStyle(
            "-fx-padding: 14; " +
            "-fx-font-size: 13; " +
            "-fx-border-color: #dfe6e9; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #2c3e50;"
        );
        emailField.setPrefHeight(50);

        // Full Name field
        Label nameLabel = new Label("😊 Full Name");
        nameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setStyle(
            "-fx-padding: 14; " +
            "-fx-font-size: 13; " +
            "-fx-border-color: #dfe6e9; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #2c3e50;"
        );
        nameField.setPrefHeight(50);

        // Password field
        Label passwordLabel = new Label("🔑 Password");
        passwordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a strong password");
        passwordField.setStyle(
            "-fx-padding: 14; " +
            "-fx-font-size: 13; " +
            "-fx-border-color: #dfe6e9; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #2c3e50;"
        );
        passwordField.setPrefHeight(50);

        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #d9534f; -fx-wrap-text: true;");

        // Signup button
        Button signupButton = new Button("✓ CREATE ACCOUNT");
        signupButton.setStyle(
            "-fx-padding: 14 30; " +
            "-fx-font-size: 15; " +
            "-fx-font-weight: bold; " +
            "-fx-background: linear-gradient(135deg, #27ae60 0%, #229954 100%); " +
            "-fx-text-fill: white; " +
            "-fx-cursor: hand; " +
            "-fx-border-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(39, 174, 96, 0.5), 15, 0.8, 0, 8);"
        );
        signupButton.setPrefHeight(50);
        signupButton.setPrefWidth(Double.MAX_VALUE);
        signupButton.setOnAction(e -> handleSignup(usernameField, emailField, nameField, passwordField, statusLabel));

        formContent.getChildren().addAll(
            usernameLabel, usernameField,
            emailLabel, emailField,
            nameLabel, nameField,
            passwordLabel, passwordField,
            statusLabel,
            signupButton
        );

        // Footer with link to login
        VBox footer = new VBox(10);
        footer.setPadding(new Insets(0, 30, 30, 30));
        footer.setAlignment(Pos.CENTER);

        Separator sep = new Separator();
        sep.setStyle("-fx-border-color: #e8eef5; -fx-border-width: 1 0 0 0;");

        Label loginPrompt = new Label("Already have an account?");
        loginPrompt.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Button loginLink = new Button("Sign In Here");
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        loginLink.setOnAction(e -> sceneManager.showLoginScene());

        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(loginPrompt, loginLink);

        footer.getChildren().addAll(sep, loginBox);

        cardContainer.getChildren().addAll(header, formContent, footer);
        root.getChildren().add(cardContainer);

        return new Scene(root, 1000, 700);
    }

    private void handleSignup(TextField usernameField, TextField emailField, TextField nameField, PasswordField passwordField, Label statusLabel) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = nameField.getText().trim();
        String password = passwordField.getText();

        try {
            // Validate inputs
            ValidationResult usernameResult = LoginValidator.validateUsername(username);
            if (!usernameResult.isValid()) {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ " + usernameResult.getErrorMessage());
                return;
            }

            ValidationResult passwordResult = LoginValidator.validatePassword(password);
            if (!passwordResult.isValid()) {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ " + passwordResult.getErrorMessage());
                return;
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ Please enter a valid email address");
                return;
            }

            if (fullName.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ Full name cannot be empty");
                return;
            }

            // Create new user
            User newUser = new User(username, email, fullName, "+1 (555) 000-0000", "User", "Active", password);
            
            if (UserService.addUser(newUser)) {
                statusLabel.setStyle("-fx-text-fill: #5cb85c;");
                statusLabel.setText("✓ Account created successfully! Redirecting to login...");
                
                // Clear fields
                usernameField.clear();
                emailField.clear();
                nameField.clear();
                passwordField.clear();
                
                // Redirect to login after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        Platform.runLater(() -> sceneManager.showLoginScene());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                statusLabel.setStyle("-fx-text-fill: #d9534f;");
                statusLabel.setText("✗ Username already exists. Please choose a different username.");
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #d9534f;");
            statusLabel.setText("✗ An error occurred: " + e.getMessage());
        }
    }
}
