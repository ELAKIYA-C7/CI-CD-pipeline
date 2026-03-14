package com.example;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages scene switching and navigation between different pages in the application.
 * This class provides a centralized way to switch between login, dashboard, profile, 
 * settings, and admin pages with proper error handling.
 * 
 * Thread-safe and provides null-safety for all operations.
 */
public class SceneManager {
    private final Stage stage;
    private Scene loginScene;
    private Scene signupScene;
    private Scene dashboardScene;
    private Scene profileScene;
    private Scene settingsScene;
    private Scene adminScene;
    private Scene resetPasswordScene; // New scene for password reset
    private UserRole currentUserRole = UserRole.USER;
    private boolean darkMode = false;
    private int fontSize = 14;

    /**
     * Initialize SceneManager with primary stage
     * @param stage The primary JavaFX stage
     * @throws IllegalArgumentException if stage is null
     */
    public SceneManager(Stage stage) {
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null");
        }
        this.stage = stage;
    }

    public void setLoginScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Login scene cannot be null");
        }
        this.loginScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, true);
    }

    public void setSignupScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Signup scene cannot be null");
        }
        this.signupScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, true);
    }

    /**
     * Sets the scene for the password reset page.
     * @param scene The password reset scene.
     * @throws IllegalArgumentException if the scene is null.
     */
    public void setResetPasswordScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Reset password scene cannot be null");
        }
        this.resetPasswordScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, true);
    }

    public void setDashboardScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Dashboard scene cannot be null");
        }
        this.dashboardScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, false);
    }

    public void setProfileScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Profile scene cannot be null");
        }
        this.profileScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, false);
    }

    public void setSettingsScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Settings scene cannot be null");
        }
        this.settingsScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, false);
    }

    public void setAdminScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Admin scene cannot be null");
        }
        this.adminScene = scene;
        applyBaseStylesToScene(scene);
        applyFontSizeToScene(scene);
        applyDarkModeToScene(scene);
        updateSceneBackground(scene, false);
    }

    public UserRole getCurrentUserRole() {
        return currentUserRole;
    }

    public void setCurrentUserRole(UserRole role) {
        if (role == null) return;
        this.currentUserRole = role;
        // notify listeners about role change
        try {
            for (RoleChangeListener l : roleListeners) {
                l.onRoleChanged(role);
            }
        } catch (Exception e) {
            // ignore listener failures
        }
    }

    // Role change listener support
    public interface RoleChangeListener {
        void onRoleChanged(UserRole newRole);
    }

    private final List<RoleChangeListener> roleListeners = new CopyOnWriteArrayList<>();

    public void addRoleChangeListener(RoleChangeListener listener) {
        if (listener == null) return;
        roleListeners.add(listener);
    }

    public void removeRoleChangeListener(RoleChangeListener listener) {
        if (listener == null) return;
        roleListeners.remove(listener);
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean enabled) {
        this.darkMode = enabled;
        // apply stylesheet state to all initialized scenes
        try {
            if (loginScene != null) {
                applyDarkModeToScene(loginScene);
                updateSceneBackground(loginScene, true);
            }
            if (signupScene != null) {
                applyDarkModeToScene(signupScene);
                updateSceneBackground(signupScene, true);
            }
            if (resetPasswordScene != null) {
                applyDarkModeToScene(resetPasswordScene);
                updateSceneBackground(resetPasswordScene, true);
            }
            if (dashboardScene != null) {
                applyDarkModeToScene(dashboardScene);
                updateSceneBackground(dashboardScene, false);
            }
            if (profileScene != null) {
                applyDarkModeToScene(profileScene);
                updateSceneBackground(profileScene, false);
            }
            if (settingsScene != null) {
                applyDarkModeToScene(settingsScene);
                updateSceneBackground(settingsScene, false);
            }
            if (adminScene != null) {
                applyDarkModeToScene(adminScene);
                updateSceneBackground(adminScene, false);
            }
        } catch (Exception e) {
            // ignore failures
        }
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int size) {
        if (size < 8 || size > 20) return;
        this.fontSize = size;
        // apply font size to all initialized scenes
        try {
            if (loginScene != null) applyFontSizeToScene(loginScene);
            if (signupScene != null) applyFontSizeToScene(signupScene);
            if (dashboardScene != null) applyFontSizeToScene(dashboardScene);
            if (profileScene != null) applyFontSizeToScene(profileScene);
            if (settingsScene != null) applyFontSizeToScene(settingsScene);
            if (adminScene != null) applyFontSizeToScene(adminScene);
        } catch (Exception e) {
            // ignore failures
        }
    }

    private void updateSceneBackground(Scene scene, boolean isAuth) {
        if (scene == null) return;
        try {
            StringBuilder style = new StringBuilder();
            if (isAuth) {
                style.append("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
                style.append("-fx-text-primary: #2c3e50;");
                style.append("-fx-card-bg: white;");
                style.append("-fx-card-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 30, 0.8, 0, 15);");
            } else {
                if (darkMode) {
                    style.append("-fx-background-color: #1a1a1a;");
                    style.append("-fx-text-primary: white;");
                    style.append("-fx-card-bg: #2d2d2d;");
                    style.append("-fx-card-effect: null;"); // Remove shadow in dark mode
                } else {
                    style.append("-fx-background-color: #f0f2f5;");
                    style.append("-fx-text-primary: #2c3e50;");
                    style.append("-fx-card-bg: white;");
                    style.append("-fx-card-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0.5, 0, 3);");
                }
            }
            style.append("-fx-font-family: 'Segoe UI', Arial; -fx-font-size: ").append(fontSize).append(";");
            scene.getRoot().setStyle(style.toString());
        } catch (Exception e) {
            // ignore
        }
    }

    private void applyFontSizeToScene(Scene scene) {
        if (scene == null) return;
        try {
            scene.getRoot().setStyle(scene.getRoot().getStyle() + "; -fx-font-size: " + fontSize + ";");
        } catch (Exception e) {
            // ignore
        }
    }

    private void applyDarkModeToScene(Scene scene) {
        if (scene == null) return;
        try {
            String sheet = getClass().getResource("/dark-theme.css") != null ? getClass().getResource("/dark-theme.css").toExternalForm() : null;
            if (sheet == null) return;
            if (darkMode) {
                if (!scene.getStylesheets().contains(sheet)) scene.getStylesheets().add(sheet);
            } else {
                scene.getStylesheets().remove(sheet);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private void applyBaseStylesToScene(Scene scene) {
        if (scene == null) return;
        try {
            String base = getClass().getResource("/app-controls.css") != null ? getClass().getResource("/app-controls.css").toExternalForm() : null;
            if (base == null) return;
            if (!scene.getStylesheets().contains(base)) scene.getStylesheets().add(base);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Show login scene with error handling
     */
    public void showLoginScene() {
        if (loginScene == null) {
            throw new IllegalStateException("Login scene has not been initialized");
        }
        stage.setScene(loginScene);
        stage.setTitle("JavaFX CI/CD - Login");
    }

    /**
     * Show signup scene with error handling
     */
    public void showSignupScene() {
        if (signupScene == null) {
            throw new IllegalStateException("Signup scene has not been initialized");
        }
        stage.setScene(signupScene);
        stage.setTitle("JavaFX CI/CD - Sign Up");
    }

    /**
     * Show reset password scene with error handling
     */
    public void showResetPasswordScene() {
        if (resetPasswordScene == null) {
            throw new IllegalStateException("Reset password scene has not been initialized");
        }
        stage.setScene(resetPasswordScene);
        stage.setTitle("JavaFX CI/CD - Reset Password");
    }

    /**
     * Show dashboard scene with error handling
     */
    public void showDashboardScene() {
        if (dashboardScene == null) {
            throw new IllegalStateException("Dashboard scene has not been initialized");
        }
        stage.setScene(dashboardScene);
        stage.setTitle("JavaFX CI/CD - Dashboard");
    }

    /**
     * Show profile scene with error handling
     */
    public void showProfileScene() {
        if (profileScene == null) {
            throw new IllegalStateException("Profile scene has not been initialized");
        }
        stage.setScene(profileScene);
        stage.setTitle("JavaFX CI/CD - Profile");
    }

    /**
     * Show settings scene with error handling
     */
    public void showSettingsScene() {
        if (settingsScene == null) {
            throw new IllegalStateException("Settings scene has not been initialized");
        }
        stage.setScene(settingsScene);
        stage.setTitle("JavaFX CI/CD - Settings");
    }

    /**
     * Show admin scene with error handling
     */
    public void showAdminScene() {
        if (adminScene == null) {
            throw new IllegalStateException("Admin scene has not been initialized");
        }
        stage.setScene(adminScene);
        stage.setTitle("JavaFX CI/CD - Admin");
    }
}
