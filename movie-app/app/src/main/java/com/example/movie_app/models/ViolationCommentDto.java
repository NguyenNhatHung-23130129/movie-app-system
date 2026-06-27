package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class ViolationCommentDto {
    @SerializedName("id")
    private String id;
    @SerializedName("userName")
    private String userName;
    @SerializedName("content")
    private String content;
    @SerializedName("movieTitle")
    private String movieTitle;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("reason")
    private String reason;

    public String getId() { return id; }
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public String getMovieTitle() { return movieTitle; }
    public String getTimestamp() { return timestamp; }
    public String getReason() { return reason; }
}