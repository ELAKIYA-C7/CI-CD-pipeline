package com.example.models;

/**
 * SystemStatus model representing system health and performance metrics
 */
public class SystemStatus {
    private String name;
    private String value;
    private String status; // "online", "offline", "warning", "error"

    public SystemStatus(String name, String value, String status) {
        this.name = name;
        this.value = value;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + ": " + value + " (" + status + ")";
    }
}
