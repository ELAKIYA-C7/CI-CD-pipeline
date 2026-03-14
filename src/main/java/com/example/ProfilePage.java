package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import com.example.services.UserService;
import com.example.app.LoginValidator;
import com.example.app.ValidationResult;

/**
 * User profile page displaying user information and allowing profile updates.
 */
public class ProfilePage {
    private String username;
    private SceneManager sceneManager;
    private Label roleValue;

    public ProfilePage(String username, SceneManager sceneManager) {
        this.username = username;
        this.sceneManager = sceneManager;
        // register to receive role change notifications so UI updates immediately
        try {
            if (this.sceneManager != null) {
                this.sceneManager.addRoleChangeListener(newRole -> {
                    // run later on FX thread if label exists
                    try {
                        if (roleValue != null) {
                            roleValue.setText(newRole.getDisplayName());
                        }
                    } catch (Exception ignored) {
                    }
                });
            }
        } catch (Exception ignored) {
        }
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Content
        VBox content = createContent();
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        root.setCenter(scrollPane);

        // Navigation
        HBox navbar = createNavBar();
        root.setBottom(navbar);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/modern-theme.css").toExternalForm());

        // Safely attach a listener to update role when the scene's window becomes available
        scene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
            if (newWindow != null) {
                newWindow.setOnShown(e -> {
                    updateRoleDisplay();
                    root.requestFocus(); // Deselect text fields for cleaner look
                });
            }
        });

        return scene;
    }

    private void updateRoleDisplay() {
        if (roleValue != null && sceneManager != null && sceneManager.getCurrentUserRole() != null) {
            roleValue.setText(sceneManager.getCurrentUserRole().getDisplayName());
        }
    }

    private VBox createHeader() {
        VBox header = new VBox(8);
        header.getStyleClass().add("header-panel");
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("👤 User Profile");
        title.getStyleClass().add("h1");
        
        Label subtitle = new Label("Manage your personal information, settings & security");
        subtitle.getStyleClass().add("subtitle");
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(50));

        // Profile Card
        VBox profileCard = new VBox(18);
        profileCard.getStyleClass().add("card");
        profileCard.setMaxWidth(700);

        // Username - READONLY
        HBox usernameBox = createProfileField("👤 Username", new TextField(username), false);

        // Email - EDITABLE
        TextField emailField = new TextField("john@example.com");
        HBox emailBox = createProfileField("📧 Email", emailField, true);

        // Full Name - EDITABLE
        TextField nameField = new TextField("John Doe");
        HBox nameBox = createProfileField("😊 Full Name", nameField, true);

        // Phone - EDITABLE
        TextField phoneField = new TextField("+1 (555) 123-4567");
        HBox phoneBox = createProfileField("📱 Phone", phoneField, true);

        // Divider
        Separator sep1 = new Separator();

        // Role - READONLY
        HBox roleBox = new HBox(10);
        roleBox.setAlignment(Pos.CENTER_LEFT);
        Label roleLabel = new Label("🎭 Role:");
        roleLabel.setPrefWidth(120);
        roleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        
        roleValue = new Label("User");
        roleValue.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db; -fx-padding: 5 10; -fx-background-color: #ebf5fb; -fx-background-radius: 4;");
        
        roleBox.getChildren().addAll(roleLabel, roleValue);

        // Status - READONLY
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        Label statusLabel = new Label("✓ Status:");
        statusLabel.setPrefWidth(120);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        
        Label statusValue = new Label("● Active");
        statusValue.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60; -fx-padding: 5 10; -fx-background-color: #eafaf1; -fx-background-radius: 4;");
        
        statusBox.getChildren().addAll(statusLabel, statusValue);

        // Divider
        Separator sep2 = new Separator();

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.getStyleClass().addAll("button", "btn-success");
        saveBtn.setOnAction(e -> showAlert("✓ Success", "Your profile has been updated successfully!"));

        Button changePasswordBtn = new Button("🔐 Change Password");
        changePasswordBtn.getStyleClass().addAll("button", "btn-primary");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog());

        Button resetBtn = new Button("↩️ Reset");
        resetBtn.getStyleClass().addAll("button", "btn-icon"); // Using simple style for reset
        resetBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;"); // Override for grey
        resetBtn.setOnAction(e -> {
            emailField.setText("john@example.com");
            nameField.setText("John Doe");
            phoneField.setText("+1 (555) 123-4567");
        });

        buttonBox.getChildren().addAll(saveBtn, changePasswordBtn, resetBtn);

        profileCard.getChildren().addAll(usernameBox, emailBox, nameBox, phoneBox, sep1, roleBox, statusBox, sep2, buttonBox);
        
        VBox cardWrapper = new VBox();
        cardWrapper.setAlignment(Pos.TOP_CENTER);
        cardWrapper.getChildren().add(profileCard);
        
        content.getChildren().add(cardWrapper);

        return content;
    }

    private HBox createProfileField(String label, TextField field, boolean editable) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10, 0, 10, 0));

        Label fieldLabel = new Label(label);
        fieldLabel.setPrefWidth(120);
        fieldLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");

        field.setEditable(editable);
        field.getStyleClass().add("text-field");
        field.setPrefWidth(350);

        box.getChildren().addAll(fieldLabel, field);
        return box;
    }

    private HBox createNavBar() {
        HBox navbar = new HBox(15);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 0, -2, 0, 0);");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Button dashboardBtn = new Button("📊 Dashboard");
        dashboardBtn.getStyleClass().addAll("button", "btn-icon");
        dashboardBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-border-color: transparent;");
        dashboardBtn.setOnAction(e -> sceneManager.showDashboardScene());

        Button profileBtn = new Button("👤 Profile");
        profileBtn.getStyleClass().addAll("button", "btn-primary");

        Button settingsBtn = new Button("⚙️ Settings");
        settingsBtn.getStyleClass().addAll("button", "btn-icon");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-border-color: transparent;");
        settingsBtn.setOnAction(e -> sceneManager.showSettingsScene());

        Button adminBtn = new Button("👨‍💼 Admin");
        adminBtn.getStyleClass().addAll("button", "btn-icon");
        adminBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-border-color: transparent;");
        adminBtn.setOnAction(e -> sceneManager.showAdminScene());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.getStyleClass().addAll("button", "btn-danger");
        logoutBtn.setOnAction(e -> sceneManager.showLoginScene());

        navbar.getChildren().addAll(dashboardBtn, profileBtn, settingsBtn, adminBtn, spacer, logoutBtn);
        return navbar;
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔐 Change Password");
        dialog.setHeaderText("Enter your current and new password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 50, 10, 10));

        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Current Password");
        currentPasswordField.getStyleClass().add("password-field");
        
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.getStyleClass().add("password-field");
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");
        confirmPasswordField.getStyleClass().add("password-field");

        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPasswordField, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Apply theme to dialog? Dialogs are native-ish, but we can try adding stylesheet
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/modern-theme.css").toExternalForm());

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String current = currentPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirm = confirmPasswordField.getText();

            if (!UserService.verifyPassword(username, current)) {
                showAlert("Error", "Incorrect current password.");
                event.consume();
                return;
            }

            ValidationResult valResult = LoginValidator.validatePassword(newPass);
            if (!valResult.isValid()) {
                showAlert("Validation Error", valResult.getErrorMessage());
                event.consume();
                return;
            }

            if (!newPass.equals(confirm)) {
                showAlert("Error", "New passwords do not match.");
                event.consume();
                return;
            }

            if (UserService.updatePassword(username, newPass)) {
                showAlert("Success", "Password updated successfully!");
            } else {
                showAlert("Error", "Failed to update password.");
                event.consume();
            }
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/modern-theme.css").toExternalForm());
        alert.showAndWait();
    }
}
