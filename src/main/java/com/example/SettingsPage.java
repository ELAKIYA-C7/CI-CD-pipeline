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
 * Settings page for application and user settings configuration.
 */
public class SettingsPage {
    private SceneManager sceneManager;
    private String username;

    public SettingsPage(String username, SceneManager sceneManager) {
        this.username = username;
        this.sceneManager = sceneManager;
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
        return scene;
    }

    private VBox createHeader() {
        VBox header = new VBox(8);
        header.getStyleClass().add("header-panel");
        
        HBox headerContent = new HBox(20);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(8);
        Label title = new Label("⚙️ Settings");
        title.getStyleClass().add("h1");
        
        Label subtitle = new Label("Customize your experience, security & preferences");
        subtitle.getStyleClass().add("subtitle");
        
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        MenuButton profileMenu = new MenuButton("👤 " + username);
        profileMenu.getStyleClass().addAll("button", "btn-primary");

        MenuItem logoutItem = new MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> sceneManager.showLoginScene());
        profileMenu.getItems().add(logoutItem);

        headerContent.getChildren().addAll(titleBox, spacer, profileMenu);
        header.getChildren().add(headerContent);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(35));

        // Settings Tabs
        TabPane tabPane = createSettingsTabs();
        
        content.getChildren().add(tabPane);
        return content;
    }

    private TabPane createSettingsTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("settings-tab-pane");

        // General Settings Tab
        Tab generalTab = new Tab("⚙️ General", createGeneralSettings());
        
        // Appearance Tab
        Tab appearanceTab = new Tab("🎨 Appearance", createAppearanceSettings());
        
        // Security Tab
        Tab securityTab = new Tab("🔐 Security", createSecuritySettings());
        
        // Notifications Tab
        Tab notificationsTab = new Tab("🔔 Notifications", createNotificationSettings());

        tabPane.getTabs().addAll(generalTab, appearanceTab, securityTab, notificationsTab);
        return tabPane;
    }

    private VBox createGeneralSettings() {
        VBox settings = new VBox(18);
        settings.setPadding(new Insets(30));
        settings.getStyleClass().add("card");

        Label sectionTitle = new Label("🌍 Regional Settings");
        sectionTitle.getStyleClass().add("h2"); // Reuse h2 or similar

        // Language
        HBox languageBox = createSettingField("🌐 Language:", "English", new String[]{"English", "Spanish", "French", "German"});

        // Timezone
        HBox timezoneBox = createSettingField("🕐 Timezone:", "EST", new String[]{"UTC", "EST", "CST", "PST"});

        // Date Format
        HBox dateBox = createSettingField("📅 Date Format:", "MM/DD/YYYY", new String[]{"MM/DD/YYYY", "DD/MM/YYYY", "YYYY-MM-DD"});

        Button saveBtn = new Button("💾 Save Settings");
        saveBtn.getStyleClass().addAll("button", "btn-success");
        saveBtn.setOnAction(e -> showAlert("✓ Success", "General settings saved successfully."));

        settings.getChildren().addAll(sectionTitle, languageBox, timezoneBox, dateBox, saveBtn);
        return settings;
    }

    private VBox createAppearanceSettings() {
        VBox settings = new VBox(18);
        settings.setPadding(new Insets(30));
        settings.getStyleClass().add("card");

        Label sectionTitle = new Label("🎨 Visual Customization");
        sectionTitle.getStyleClass().add("h2");

        // Theme
        HBox themeBox = new HBox(15);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("🎨 Theme:");
        themeLabel.setPrefWidth(150);
        themeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        ComboBox<String> themeCombo = new ComboBox<>();
        themeCombo.getItems().addAll("Light", "Dark", "Auto");
        themeCombo.setValue("Light");
        themeCombo.setPrefWidth(280);
        themeCombo.getStyleClass().add("combo-box");
        themeBox.getChildren().addAll(themeLabel, themeCombo);

        // Font Size
        HBox fontBox = new HBox(15);
        fontBox.setAlignment(Pos.CENTER_LEFT);
        Label fontLabel = new Label("📝 Font Size:");
        fontLabel.setPrefWidth(150);
        fontLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        Spinner<Integer> fontSpinner = new Spinner<>(8, 20, 12, 1);
        fontSpinner.setPrefWidth(280);
        fontSpinner.getStyleClass().add("spinner");
        fontBox.getChildren().addAll(fontLabel, fontSpinner);

        // Compact View
        HBox compactBox = new HBox(15);
        compactBox.setAlignment(Pos.CENTER_LEFT);
        Label compactLabel = new Label("📦 View Mode:");
        compactLabel.setPrefWidth(150);
        compactLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox compactCheck = new CheckBox("Enable compact mode for smaller screens");
        compactCheck.setStyle("-fx-font-size: 12; -fx-padding: 8; -fx-text-fill: -fx-text-primary;");
        compactBox.getChildren().addAll(compactLabel, compactCheck);

        Button applyBtn = new Button("✨ Apply Changes");
        applyBtn.getStyleClass().addAll("button", "btn-primary");
        applyBtn.setOnAction(e -> {
            // Apply theme
            String selectedTheme = themeCombo.getValue();
            if ("Dark".equals(selectedTheme)) {
                sceneManager.setDarkMode(true);
            } else {
                sceneManager.setDarkMode(false);
            }
            
            // Apply font size
            int selectedFontSize = fontSpinner.getValue();
            sceneManager.setFontSize(selectedFontSize);
            
            showAlert("✓ Success", "Appearance settings applied successfully.\nFont Size: " + selectedFontSize + "pt\nTheme: " + selectedTheme);
        });

        settings.getChildren().addAll(sectionTitle, themeBox, fontBox, compactBox, applyBtn);
        return settings;
    }

    private VBox createSecuritySettings() {
        VBox settings = new VBox(18);
        settings.setPadding(new Insets(30));
        settings.getStyleClass().add("card");

        Label sectionTitle = new Label("🔐 Security & Privacy");
        sectionTitle.getStyleClass().add("h2");

        // Two-Factor Authentication
        HBox twoFactorBox = new HBox(15);
        twoFactorBox.setAlignment(Pos.CENTER_LEFT);
        Label twoFactorLabel = new Label("🔐 Two-Factor Auth:");
        twoFactorLabel.setPrefWidth(150);
        twoFactorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox twoFactorCheck = new CheckBox("Enable 2FA for enhanced security");
        twoFactorCheck.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        twoFactorBox.getChildren().addAll(twoFactorLabel, twoFactorCheck);

        // Session Timeout
        HBox timeoutBox = new HBox(15);
        timeoutBox.setAlignment(Pos.CENTER_LEFT);
        Label timeoutLabel = new Label("⏱️ Session Timeout:");
        timeoutLabel.setPrefWidth(150);
        timeoutLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        Spinner<Integer> timeoutSpinner = new Spinner<>(5, 120, 30, 5);
        timeoutSpinner.setPrefWidth(100);
        timeoutSpinner.getStyleClass().add("spinner");
        Label minutesLabel = new Label("minutes");
        minutesLabel.setStyle("-fx-font-size: 12; -fx-text-fill: -fx-text-primary;");
        HBox timeoutHBox = new HBox(10);
        timeoutHBox.setAlignment(Pos.CENTER_LEFT);
        timeoutHBox.getChildren().addAll(timeoutSpinner, minutesLabel);
        timeoutBox.getChildren().addAll(timeoutLabel, timeoutHBox);

        // Login Alerts
        HBox alertBox = new HBox(15);
        alertBox.setAlignment(Pos.CENTER_LEFT);
        Label alertLabel = new Label("🔔 Login Alerts:");
        alertLabel.setPrefWidth(150);
        alertLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox alertCheck = new CheckBox("Notify me on new logins from unknown devices");
        alertCheck.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        alertCheck.setSelected(true);
        alertBox.getChildren().addAll(alertLabel, alertCheck);

        Button changePasswordBtn = new Button("🔑 Change Password");
        changePasswordBtn.getStyleClass().addAll("button", "btn-danger");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog());

        settings.getChildren().addAll(sectionTitle, twoFactorBox, timeoutBox, alertBox, changePasswordBtn);
        return settings;
    }

    private VBox createNotificationSettings() {
        VBox settings = new VBox(18);
        settings.setPadding(new Insets(30));
        settings.getStyleClass().add("card");

        Label sectionTitle = new Label("🔔 Notification Preferences");
        sectionTitle.getStyleClass().add("h2");

        // Email Notifications
        HBox emailBox = new HBox(15);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        Label emailLabel = new Label("📧 Email:");
        emailLabel.setPrefWidth(150);
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox emailCheck = new CheckBox("Enable email notifications for important events");
        emailCheck.setSelected(true);
        emailCheck.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        emailBox.getChildren().addAll(emailLabel, emailCheck);

        // Push Notifications
        HBox pushBox = new HBox(15);
        pushBox.setAlignment(Pos.CENTER_LEFT);
        Label pushLabel = new Label("📲 Push:");
        pushLabel.setPrefWidth(150);
        pushLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox pushCheck = new CheckBox("Enable in-app push notifications");
        pushCheck.setSelected(true);
        pushCheck.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        pushBox.getChildren().addAll(pushLabel, pushCheck);

        // SMS Notifications
        HBox smsBox = new HBox(15);
        smsBox.setAlignment(Pos.CENTER_LEFT);
        Label smsLabel = new Label("💬 SMS:");
        smsLabel.setPrefWidth(150);
        smsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        CheckBox smsCheck = new CheckBox("Enable SMS alerts for critical updates");
        smsCheck.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        smsBox.getChildren().addAll(smsLabel, smsCheck);

        // Notification Frequency
        HBox frequencyBox = createSettingField("⏰ Frequency:", "Daily", new String[]{"Real-time", "Hourly", "Daily", "Weekly"});

        Button applyBtn = new Button("💾 Save Preferences");
        applyBtn.getStyleClass().addAll("button", "btn-primary");
        applyBtn.setOnAction(e -> showAlert("✓ Success", "Notification preferences saved successfully."));

        VBox.setMargin(sectionTitle, new Insets(20, 30, 0, 30));
        VBox.setMargin(emailBox, new Insets(0, 30, 0, 30));
        VBox.setMargin(pushBox, new Insets(0, 30, 0, 30));
        VBox.setMargin(smsBox, new Insets(0, 30, 0, 30));
        VBox.setMargin(frequencyBox, new Insets(0, 30, 0, 30));
        VBox.setMargin(applyBtn, new Insets(0, 30, 20, 30));

        settings.getChildren().addAll(sectionTitle, emailBox, pushBox, smsBox, frequencyBox, applyBtn);
        return settings;
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

        Button settingsBtn = new Button("⚙️ Settings");
        settingsBtn.getStyleClass().addAll("button", "btn-primary");

        Button adminBtn = new Button("👨‍💼 Admin");
        adminBtn.getStyleClass().addAll("button", "btn-icon");
        adminBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-border-color: transparent;");
        adminBtn.setOnAction(e -> sceneManager.showAdminScene());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navbar.getChildren().addAll(dashboardBtn, settingsBtn, adminBtn, spacer);
        return navbar;
    }

    private HBox createSettingField(String label, String defaultValue, String[] options) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        Label fieldLabel = new Label(label);
        fieldLabel.setPrefWidth(150);
        fieldLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(options);
        combo.setValue(defaultValue);
        combo.setPrefWidth(280);
        combo.getStyleClass().add("combo-box");
        
        box.getChildren().addAll(fieldLabel, combo);
        return box;
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔐 Change Password");
        dialog.setHeaderText("Enter your current and new password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

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
