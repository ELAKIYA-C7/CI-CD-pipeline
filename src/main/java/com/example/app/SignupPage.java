package com.example.app;

import com.example.SceneManager;
import com.example.models.User;
import com.example.services.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SignupPage {
    private final SceneManager sceneManager;
    private final Runnable onLogin;
    private TextField usernameField;
    private TextField emailField;
    private TextField fullNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private CheckBox termsCheckBox;
    private Label statusLabel;

    public SignupPage(SceneManager sceneManager, Runnable onLogin) {
        this.sceneManager = sceneManager;
        this.onLogin = onLogin;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);");

        VBox card = new VBox(15);
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: -fx-card-bg; -fx-background-radius: 10; -fx-effect: -fx-card-effect;");

        Label titleLabel = new Label("Create Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-padding: 10; -fx-background-radius: 5;");

        emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setStyle("-fx-padding: 10; -fx-background-radius: 5;");

        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.setStyle("-fx-padding: 10; -fx-background-radius: 5;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 5;");

        Button signupButton = new Button("Sign Up");
        signupButton.setPrefWidth(200);
        signupButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 5; -fx-cursor: hand;");
        signupButton.setOnAction(e -> handleSignup());

        Button loginLink = new Button("Already have an account? Login");
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #667eea; -fx-cursor: hand;");
        loginLink.setOnAction(e -> onLogin.run());

        statusLabel = new Label();
        statusLabel.setWrapText(true);

        card.getChildren().addAll(
            titleLabel,
            new Label("Username"), usernameField,
            new Label("Email"), emailField,
            new Label("Full Name"), fullNameField,
            new Label("Password"), passwordField,
            new Separator(),
            statusLabel,
            signupButton,
            loginLink
        );

        root.getChildren().add(card);
        return new Scene(root, 1000, 700);
    }

    private VBox createLabeledField(String labelText, Control field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 12px;");
        VBox box = new VBox(5);
        box.getChildren().addAll(label, field);
        return box;
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please fill in all required fields.");
            return;
        }

        if (!password.equals(confirmPass)) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Passwords do not match.");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("You must agree to the Terms & Conditions.");
            return;
        }

        // Phone is explicitly empty string until user adds it in profile
        String phone = ""; 
        String role = "User";
        String status = "Active";

        User newUser = new User(username, email, fullName, phone, role, status, password);

        if (UserService.addUser(newUser)) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Account created successfully! Redirecting to login...");
            // Clear fields
            usernameField.clear(); emailField.clear(); fullNameField.clear(); passwordField.clear();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Username already exists. Please choose another.");
        }
    }
}