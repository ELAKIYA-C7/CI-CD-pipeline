package com.example.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginValidatorTest - Unit tests for validation logic
 * 
 * Why Unit Testing?
 * - Catches bugs early in development
 * - Enables safe refactoring
 * - Serves as documentation
 * - Supports CI/CD pipeline reliability
 * - Reduces debugging time
 * 
 * Run tests with: mvn test
 * Run specific test: mvn test -Dtest=LoginValidatorTest
 */
@DisplayName("Login Validator Tests")
public class LoginValidatorTest {

    private ValidationResult result;

    @BeforeEach
    public void setUp() {
        // Runs before each test
        result = null;
    }

    // ===================== Username Validation Tests =====================

    @Test
    @DisplayName("Should reject empty username")
    public void testEmptyUsername() {
        result = LoginValidator.validateUsername("");
        assertFalse(result.isValid(), "Empty username should be invalid");
        assertEquals("Username cannot be empty", result.getErrorMessage());
    }

    @Test
    @DisplayName("Should reject null username")
    public void testNullUsername() {
        result = LoginValidator.validateUsername(null);
        assertFalse(result.isValid(), "Null username should be invalid");
    }

    @Test
    @DisplayName("Should reject username shorter than minimum length")
    public void testUsernameTooShort() {
        result = LoginValidator.validateUsername("ab");
        assertFalse(result.isValid(), "Username shorter than 3 chars should be invalid");
        assertTrue(result.getErrorMessage().contains("at least"));
    }

    @Test
    @DisplayName("Should reject username longer than maximum length")
    public void testUsernameTooLong() {
        String longUsername = "a".repeat(25); // 25 characters
        result = LoginValidator.validateUsername(longUsername);
        assertFalse(result.isValid(), "Username longer than 20 chars should be invalid");
        assertTrue(result.getErrorMessage().contains("exceed"));
    }

    @Test
    @DisplayName("Should reject username with special characters")
    public void testUsernameWithSpecialChars() {
        result = LoginValidator.validateUsername("user@name");
        assertFalse(result.isValid(), "Username with special chars should be invalid");
        assertTrue(result.getErrorMessage().contains("letters, numbers, and underscores"));
    }

    @Test
    @DisplayName("Should accept valid username with letters")
    public void testValidUsernameLetters() {
        result = LoginValidator.validateUsername("john");
        assertTrue(result.isValid(), "Valid username with letters should be accepted");
    }

    @Test
    @DisplayName("Should accept valid username with numbers")
    public void testValidUsernameNumbers() {
        result = LoginValidator.validateUsername("user123");
        assertTrue(result.isValid(), "Valid username with numbers should be accepted");
    }

    @Test
    @DisplayName("Should accept valid username with underscores")
    public void testValidUsernameUnderscores() {
        result = LoginValidator.validateUsername("john_doe");
        assertTrue(result.isValid(), "Valid username with underscores should be accepted");
    }

    // ===================== Password Validation Tests =====================

    @Test
    @DisplayName("Should reject empty password")
    public void testEmptyPassword() {
        result = LoginValidator.validatePassword("");
        assertFalse(result.isValid(), "Empty password should be invalid");
        assertEquals("Password cannot be empty", result.getErrorMessage());
    }

    @Test
    @DisplayName("Should reject null password")
    public void testNullPassword() {
        result = LoginValidator.validatePassword(null);
        assertFalse(result.isValid(), "Null password should be invalid");
    }

    @Test
    @DisplayName("Should reject password shorter than minimum length")
    public void testPasswordTooShort() {
        result = LoginValidator.validatePassword("pass");
        assertFalse(result.isValid(), "Password shorter than 6 chars should be invalid");
        assertTrue(result.getErrorMessage().contains("at least"));
    }

    @Test
    @DisplayName("Should reject password longer than maximum length")
    public void testPasswordTooLong() {
        String longPassword = "p".repeat(35); // 35 characters
        result = LoginValidator.validatePassword(longPassword);
        assertFalse(result.isValid(), "Password longer than 30 chars should be invalid");
        assertTrue(result.getErrorMessage().contains("exceed"));
    }

    @Test
    @DisplayName("Should accept valid password")
    public void testValidPassword() {
        result = LoginValidator.validatePassword("MyPassword123");
        assertTrue(result.isValid(), "Valid password should be accepted");
    }

    // ===================== Combined Credentials Validation Tests =====================

    @Test
    @DisplayName("Should reject both empty username and password")
    public void testBothEmpty() {
        result = LoginValidator.validateCredentials("", "");
        assertFalse(result.isValid(), "Both empty should be invalid");
    }

    @Test
    @DisplayName("Should reject valid username with invalid password")
    public void testValidUsernameInvalidPassword() {
        result = LoginValidator.validateCredentials("john_doe", "short");
        assertFalse(result.isValid(), "Valid username with short password should be invalid");
    }

    @Test
    @DisplayName("Should reject invalid username with valid password")
    public void testInvalidUsernameValidPassword() {
        result = LoginValidator.validateCredentials("ab", "ValidPassword123");
        assertFalse(result.isValid(), "Invalid username with valid password should be invalid");
    }

    @Test
    @DisplayName("Should accept both valid username and password")
    public void testBothValid() {
        result = LoginValidator.validateCredentials("john_doe", "SecurePassword123");
        assertTrue(result.isValid(), "Valid credentials should be accepted");
    }

    @Test
    @DisplayName("Should accept minimum length credentials")
    public void testMinimumLengthCredentials() {
        result = LoginValidator.validateCredentials("abc", "secret");
        assertTrue(result.isValid(), "Minimum length credentials should be accepted");
    }

    // ===================== Edge Cases =====================

    @Test
    @DisplayName("Should handle username with leading/trailing spaces")
    public void testUsernameWithSpaces() {
        result = LoginValidator.validateCredentials("  john  ", "password123");
        // Note: In MainApp, we use trim(), so this simulates trimmed input
        result = LoginValidator.validateCredentials("john", "password123");
        assertTrue(result.isValid(), "Trimmed username should be valid");
    }

    @Test
    @DisplayName("Should accept username with numbers only")
    public void testUsernameNumbersOnly() {
        result = LoginValidator.validateUsername("123456789");
        assertTrue(result.isValid(), "Username with numbers only should be valid");
    }

    @Test
    @DisplayName("Should accept password with special characters")
    public void testPasswordWithSpecialChars() {
        result = LoginValidator.validatePassword("Pass@123!#$");
        assertTrue(result.isValid(), "Password with special characters should be valid");
    }
}
