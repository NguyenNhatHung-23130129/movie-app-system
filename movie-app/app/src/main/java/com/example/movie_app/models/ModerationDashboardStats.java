package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class ModerationDashboardStats {
    @SerializedName("totalReportedUsers")
    private long totalUsers;

    @SerializedName("totalPendingReports")
    private int newViolationsCount;

    @SerializedName("totalViolationComments")
    private int bannedToday;

    // Backend hiện chưa trả về trustScore, ta gán mặc định để UI hiển thị đẹp
    private int trustScore = 100;

    public long getTotalUsers() { return totalUsers; }
    public int getNewViolationsCount() { return newViolationsCount; }
    public int getTrustScore() { return trustScore; }
    public int getBannedToday() { return bannedToday; }
}
