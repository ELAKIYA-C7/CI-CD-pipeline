package com.example.models;

public class User {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String status;
    private String password;

    public User(String username, String email, String fullName, String phone, String role, String status, String password) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    // Constructor used by AdminPage (default password)
    public User(String username, String email, String fullName, String phone, String role, String status) {
        this(username, email, fullName, phone, role, status, "password123");
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getPassword() { return password; }
    
    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}