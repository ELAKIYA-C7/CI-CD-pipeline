
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

/**
 * Dashboard page displayed after successful login.
 * Shows user welcome message and provides navigation to other sections.
 */
public class DashboardPage {
    private String username;
    private SceneManager sceneManager;
    private Label welcomeLabel;

    public DashboardPage(String username, SceneManager sceneManager) {
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

        // Navigation Bar
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

        VBox titleBox = new VBox(5);
        Label title = new Label("📊 Dashboard");
        title.getStyleClass().add("h1");
        
        welcomeLabel = new Label("Welcome back, " + username + "! 👋");
        welcomeLabel.getStyleClass().add("subtitle");
        
        titleBox.getChildren().addAll(title, welcomeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Refresh Button
        Button refreshBtn = new Button("↻");
        refreshBtn.getStyleClass().add("btn-icon");
        refreshBtn.setTooltip(new Tooltip("Refresh Data"));
        refreshBtn.setOnAction(e -> refreshData());

        // Profile Menu Button
        MenuButton profileMenu = new MenuButton("👤 " + username);
        profileMenu.getStyleClass().addAll("button", "btn-primary");
        
        MenuItem logoutItem = new MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> sceneManager.showLoginScene());

        profileMenu.getItems().add(logoutItem);

        headerContent.getChildren().addAll(titleBox, spacer, refreshBtn, profileMenu);
        header.getChildren().add(headerContent);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));

        // Statistics Section
        Label statsTitle = new Label("📈 Key Metrics");
        statsTitle.getStyleClass().add("h2");

        HBox stats = new HBox(25);
        stats.setAlignment(Pos.CENTER);
        stats.getChildren().addAll(
                createStatCard("👥 Total Users", "1,234"),
                createStatCard("🟢 Active Sessions", "56"),
                createStatCard("✓ System Status", "Healthy")
        );

        // Recent Activity Section
        Label activityTitle = new Label("📋 Recent Activity");
        activityTitle.getStyleClass().add("h2");

        VBox activityBox = new VBox(15);
        activityBox.getStyleClass().add("card");
        
        Label activityLabel = new Label("System Events Log");
        activityLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
        
        TextArea activityText = new TextArea();
        activityText.setText("🔹 Login successful at 10:30 AM\n" +
                            "🔹 Profile updated at 09:45 AM\n" +
                            "🔹 Settings changed at 08:20 AM\n" +
                            "🔹 Admin report generated at 07:15 AM\n" +
                            "🔹 System backup completed at 02:00 AM");
        activityText.setWrapText(true);
        activityText.setPrefHeight(150);
        activityText.setEditable(false);
        activityText.getStyleClass().add("text-field"); // Use text-field style for consistency
        
        activityBox.getChildren().addAll(
                activityLabel,
                activityText
        );

        content.getChildren().addAll(statsTitle, stats, activityTitle, activityBox);
        return content;
    }

    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(220);
        card.setMinHeight(140);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-card-title");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-card-value");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private HBox createNavBar() {
        HBox navbar = new HBox(15);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 0, -2, 0, 0);");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Button dashboardBtn = new Button("📊 Dashboard");
        dashboardBtn.getStyleClass().addAll("button", "btn-primary"); // Active state style could be added

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

        navbar.getChildren().addAll(dashboardBtn, settingsBtn, adminBtn, spacer);
        return navbar;
    }
    
    private void refreshData() {
        // Simulating refresh
        welcomeLabel.setText("Welcome back, " + username + "! (Refreshed ↻)");
        // In real impl, this would fetch new data
    }
}
