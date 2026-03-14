package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import com.example.services.UserService;
import com.example.app.LoginValidator;
import com.example.app.ValidationResult;

/**
 * Reset Password page for users who forgot their password.
 */
public class ResetPasswordPage {
    private SceneManager sceneManager;

    public ResetPasswordPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
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
        cardContainer.setStyle("-fx-background-color: -fx-card-bg; -fx-border-radius: 20; -fx-effect: -fx-card-effect;");

        // Header section
        VBox header = new VBox(8);
        header.setPadding(new Insets(50, 30, 35, 30));
        header.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -fx-border-radius: 20 20 0 0;");
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("🔐 Reset Password");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Enter your email to reset password");
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #e8e8f0; -fx-font-style: italic;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form content
        VBox formContent = new VBox(20);
        formContent.setPadding(new Insets(35));
        formContent.setStyle("-fx-background-color: -fx-card-bg;");
        formContent.setAlignment(Pos.TOP_CENTER);

        // Email field
        Label emailLabel = new Label("📧 Email Address");
        emailLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your registered email");
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

        // New Password field
        Label passwordLabel = new Label("🔑 New Password");
        passwordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
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

        // Confirm Password field
        Label confirmLabel = new Label("🔑 Confirm Password");
        confirmLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm new password");
        confirmField.setStyle(
            "-fx-padding: 14; " +
            "-fx-font-size: 13; " +
            "-fx-border-color: #dfe6e9; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #2c3e50;"
        );
        confirmField.setPrefHeight(50);

        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #d9534f; -fx-wrap-text: true;");

        // Reset button
        Button resetButton = new Button("🔄 RESET PASSWORD");
        resetButton.setStyle(
            "-fx-padding: 14 30; " +
            "-fx-font-size: 15; " +
            "-fx-font-weight: bold; " +
            "-fx-background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%); " +
            "-fx-text-fill: white; " +
            "-fx-cursor: hand; " +
            "-fx-border-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(243, 156, 18, 0.5), 15, 0.8, 0, 8);"
        );
        resetButton.setPrefHeight(50);
        resetButton.setPrefWidth(Double.MAX_VALUE);
        resetButton.setOnAction(e -> handleReset(emailField, passwordField, confirmField, statusLabel));

        formContent.getChildren().addAll(
            emailLabel, emailField,
            passwordLabel, passwordField,
            confirmLabel, confirmField,
            statusLabel,
            resetButton
        );

        // Footer
        VBox footer = new VBox(10);
        footer.setPadding(new Insets(0, 30, 30, 30));
        footer.setAlignment(Pos.CENTER);

        Separator sep = new Separator();
        sep.setStyle("-fx-border-color: #e8eef5; -fx-border-width: 1 0 0 0;");

        Button backLink = new Button("⬅ Back to Login");
        backLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        backLink.setOnAction(e -> sceneManager.showLoginScene());

        footer.getChildren().addAll(sep, backLink);

        cardContainer.getChildren().addAll(header, formContent, footer);
        root.getChildren().add(cardContainer);

        return new Scene(root, 1000, 700);
    }

    private void handleReset(TextField emailField, PasswordField passwordField, PasswordField confirmField, Label statusLabel) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        if (email.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: #d9534f;");
            statusLabel.setText("✗ Please enter your email address");
            return;
        }

        ValidationResult valResult = LoginValidator.validatePassword(password);
        if (!valResult.isValid()) {
            statusLabel.setStyle("-fx-text-fill: #d9534f;");
            statusLabel.setText("✗ " + valResult.getErrorMessage());
            return;
        }

        if (!password.equals(confirm)) {
            statusLabel.setStyle("-fx-text-fill: #d9534f;");
            statusLabel.setText("✗ Passwords do not match");
            return;
        }

        if (UserService.updatePasswordByEmail(email, password)) {
            statusLabel.setStyle("-fx-text-fill: #5cb85c;");
            statusLabel.setText("✓ Password reset successfully! Redirecting...");
            
            // Clear fields
            emailField.clear();
            passwordField.clear();
            confirmField.clear();
            
            // Redirect to login after delay
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
            statusLabel.setText("✗ Email not found in our records");
        }
    }
}