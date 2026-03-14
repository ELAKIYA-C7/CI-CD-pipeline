package com.example.app;

/**
 * LoginValidator - Handles validation logic
 * 
 * Purpose:
 * - Separates business logic from UI code
 * - Makes code testable (can unit test validation independently)
 * - Reusable across different UI frameworks
 * - Follows Single Responsibility Principle
 */
public class LoginValidator {

    // Validation constraints
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 30;

    /**
     * Validate username and password
     * @param username User's username
     * @param password User's password
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateCredentials(String username, String password) {
        // Check if username is provided
        if (username == null || username.isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty");
        }

        // Check username length
        if (username.length() < MIN_USERNAME_LENGTH) {
            return new ValidationResult(false, 
                "Username must be at least " + MIN_USERNAME_LENGTH + " characters");
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            return new ValidationResult(false, 
                "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters");
        }

        // Check username format (alphanumeric and underscores only)
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new ValidationResult(false, 
                "Username can only contain letters, numbers, and underscores");
        }

        // Check if password is provided
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }

        // Check password length
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters");
        }

        // All validations passed
        return new ValidationResult(true, "Validation successful");
    }

    /**
     * Validate username only
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty");
        }

        if (username.length() < MIN_USERNAME_LENGTH) {
            return new ValidationResult(false, 
                "Username must be at least " + MIN_USERNAME_LENGTH + " characters");
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            return new ValidationResult(false, 
                "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters");
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new ValidationResult(false, 
                "Username can only contain letters, numbers, and underscores");
        }

        return new ValidationResult(true, "Username valid");
    }

    /**
     * Validate password only
     */
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters");
        }

        return new ValidationResult(true, "Password valid");
    }
}
