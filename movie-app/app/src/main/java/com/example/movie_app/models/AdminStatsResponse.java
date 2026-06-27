package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class AdminStatsResponse {
    @SerializedName("totalUsers")
    private long totalUsers;

    @SerializedName("peakHours")
    private String peakHours;

    @SerializedName("movieProgress")
    private int movieProgress;

    @SerializedName("seriesProgress")
    private int seriesProgress;

    // Getters
    public long getTotalUsers() { return totalUsers; }
    public String getPeakHours() { return peakHours; }
    public int getMovieProgress() { return movieProgress; }
    public int getSeriesProgress() { return seriesProgress; }
}