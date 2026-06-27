package com.example.movie_app.models;

public class MaintenanceRequest {
    private boolean maintenanceMode;

    public MaintenanceRequest(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public boolean isMaintenanceMode() { return maintenanceMode; }
    public void setMaintenanceMode(boolean maintenanceMode) { this.maintenanceMode = maintenanceMode; }
}