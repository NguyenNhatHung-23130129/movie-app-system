package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class ModerationDashboardStats {
    @SerializedName("totalUsers")
    private long totalUsers;
    @SerializedName("newViolationsCount")
    private int newViolationsCount;
    @SerializedName("trustScore")
    private int trustScore;
    @SerializedName("bannedToday")
    private int bannedToday;

    public long getTotalUsers() { return totalUsers; }
    public int getNewViolationsCount() { return newViolationsCount; }
    public int getTrustScore() { return trustScore; }
    public int getBannedToday() { return bannedToday; }
}