package com.example;

/**
 * User roles for authorization in the application
 */
public enum UserRole {
    ADMIN("Administrator", 3),
    MANAGER("Manager", 2),
    USER("Regular User", 1);

    private final String displayName;
    private final int level;

    UserRole(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasPermission(UserRole requiredRole) {
        return this.level >= requiredRole.level;
    }
}
