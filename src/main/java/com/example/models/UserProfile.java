package com.example.models;

public class UserProfile {
    private String username;
    private String jobTitle;
    private String location;
    private String bio;

    public UserProfile(String username, String jobTitle, String location, String bio) {
        this.username = username;
        this.jobTitle = jobTitle;
        this.location = location;
        this.bio = bio;
    }

    public String getUsername() { return username; }
    public String getJobTitle() { return jobTitle; }
    public String getLocation() { return location; }
    public String getBio() { return bio; }
    
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setLocation(String location) { this.location = location; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getBioSafe() {
        return bio == null ? "" : bio.replace("\n", "\\n");
    }
}