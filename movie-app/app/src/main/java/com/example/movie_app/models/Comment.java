package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("contentComment")
    private String contentComment;
    @SerializedName("timeAgo")
    private String timeAgo;
    @SerializedName("rating")
    private float rating;

    public Comment(String id, String username, String avatarUrl, String contentComment, String timeAgo, float rating) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.contentComment = contentComment;
        this.timeAgo = timeAgo;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
