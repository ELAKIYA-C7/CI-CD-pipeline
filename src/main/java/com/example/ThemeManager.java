package com.example;

/**
 * Theme manager for dark/light mode support
 */
public class ThemeManager {
    public enum Theme {
        LIGHT,
        DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getTheme() {
        return currentTheme;
    }

    public static boolean isDarkMode() {
        return currentTheme == Theme.DARK;
    }

    public static String getBackgroundColor() {
        return isDarkMode() ? "#000000" : "#f5f5f5";
    }

    public static String getPrimaryColor() {
        return isDarkMode() ? "#2c3e50" : "#2c3e50";
    }

    public static String getSecondaryColor() {
        return isDarkMode() ? "#34495e" : "#34495e";
    }

    public static String getTextColor() {
        return isDarkMode() ? "#ecf0f1" : "#333333";
    }

    public static String getCardBackgroundColor() {
        return isDarkMode() ? "#2d2d2d" : "#ffffff";
    }

    public static String getBorderColor() {
        return isDarkMode() ? "#3d3d3d" : "#e0e0e0";
    }

    public static String getHeaderStyle() {
        return "-fx-background: linear-gradient(to right, " +
                (isDarkMode() ? "#1a1a1a, #2d2d2d" : "#2c3e50, #34495e") +
                "); -fx-padding: 25; -fx-border-color: " +
                (isDarkMode() ? "#0d0d0d" : "#1a252f") +
                "; -fx-border-width: 0 0 3 0;";
    }

    public static String getRootStyle() {
        return "-fx-background-color: " + getBackgroundColor() +
                "; -fx-font-family: 'Segoe UI', Arial; -fx-font-size: 12;";
    }

    public static String getCardStyle() {
        return "-fx-background-color: " + getCardBackgroundColor() +
                "; -fx-border-radius: 8; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                (isDarkMode() ? "0.5" : "0.1") +
                "), 5, 0.5, 0, 3);";
    }
}
