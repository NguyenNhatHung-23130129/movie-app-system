package com.example.movie_app.models;

public class SafetyDashboardStats {
    private int safetyPercentage;
    private String safetyStatusText;
    private int urgentCount;
    private int totalReportsCount;
    private int lockedAccountsCount;
    private String securityVersion;
    private boolean maintenanceModeActive;

    public int getSafetyPercentage() { return safetyPercentage; }
    public void setSafetyPercentage(int safetyPercentage) { this.safetyPercentage = safetyPercentage; }

    public String getSafetyStatusText() { return safetyStatusText; }
    public void setSafetyStatusText(String safetyStatusText) { this.safetyStatusText = safetyStatusText; }

    public int getUrgentCount() { return urgentCount; }
    public void setUrgentCount(int urgentCount) { this.urgentCount = urgentCount; }

    public int getTotalReportsCount() { return totalReportsCount; }
    public void setTotalReportsCount(int totalReportsCount) { this.totalReportsCount = totalReportsCount; }

    public int getLockedAccountsCount() { return lockedAccountsCount; }
    public void setLockedAccountsCount(int lockedAccountsCount) { this.lockedAccountsCount = lockedAccountsCount; }

    public String getSecurityVersion() { return securityVersion; }
    public void setSecurityVersion(String securityVersion) { this.securityVersion = securityVersion; }

    public boolean isMaintenanceModeActive() { return maintenanceModeActive; }
    public void setMaintenanceModeActive(boolean maintenanceModeActive) { this.maintenanceModeActive = maintenanceModeActive; }
}