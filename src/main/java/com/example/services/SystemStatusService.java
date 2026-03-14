package com.example.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.models.SystemStatus;

/**
 * SystemStatusService - Manages system health and performance metrics
 */
public class SystemStatusService {
    private static final ObservableList<SystemStatus> statusList = FXCollections.observableArrayList();

    static {
        // Initialize with sample status data
        statusList.addAll(
                new SystemStatus("System Status", "Online", "online"),
                new SystemStatus("Database Connection", "Connected", "online"),
                new SystemStatus("API Server", "Running", "online"),
                new SystemStatus("Cache Server", "Running", "online"),
                new SystemStatus("CPU Usage", "45%", "warning"),
                new SystemStatus("Memory Usage", "62%", "warning"),
                new SystemStatus("Disk Usage", "78%", "warning")
        );
    }

    /**
     * Get all system status
     */
    public static ObservableList<SystemStatus> getAllStatus() {
        return statusList;
    }

    /**
     * Get status by name
     */
    public static SystemStatus getStatusByName(String name) {
        for (SystemStatus status : statusList) {
            if (status.getName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }

    /**
     * Update status
     */
    public static boolean updateStatus(String name, String value, String status) {
        for (int i = 0; i < statusList.size(); i++) {
            if (statusList.get(i).getName().equalsIgnoreCase(name)) {
                statusList.get(i).setValue(value);
                statusList.get(i).setStatus(status);
                return true;
            }
        }
        return false;
    }

    /**
     * Check system health
     */
    public static String checkHealth() {
        int onlineCount = 0;
        int warningCount = 0;

        for (SystemStatus status : statusList) {
            if ("online".equals(status.getStatus())) {
                onlineCount++;
            } else if ("warning".equals(status.getStatus())) {
                warningCount++;
            }
        }

        if (warningCount == 0) {
            return "✅ All systems operational";
        } else if (onlineCount >= statusList.size() / 2) {
            return "⚠️ " + warningCount + " warnings detected";
        } else {
            return "❌ Critical issues detected";
        }
    }

    /**
     * Restart services (simulated)
     */
    public static String restartServices() {
        // Simulate restart
        for (SystemStatus status : statusList) {
            if ("online".equals(status.getStatus())) {
                status.setStatus("online");
            }
        }
        return "Services restarted successfully";
    }
}
