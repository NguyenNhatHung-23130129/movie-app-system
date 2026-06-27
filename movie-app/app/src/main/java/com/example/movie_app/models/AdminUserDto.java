package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class AdminUserDto {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("isLocked")
    private boolean isLocked;
    @SerializedName("reportCount")
    private int reportCount;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isLocked() { return isLocked; }
    public int getReportCount() { return reportCount; }

    public void setLocked(boolean locked) { isLocked = locked; }
}