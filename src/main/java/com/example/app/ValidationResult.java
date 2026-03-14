package com.example.app;

/**
 * ValidationResult - Encapsulates validation outcome
 * 
 * Benefits of this pattern:
 * - Returns both status and error message
 * - Type-safe (no null checks needed)
 * - Can be easily extended with error codes, field information, etc.
 */
public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;

    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
