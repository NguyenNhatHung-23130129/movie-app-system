package com.example.movie_app.models;

public class Comment {
    private String id;
    private String username;
    private String avatarUrl;
    private String content;
    private String timeAgo;
    public Comment(String id, String username, String avatarUrl, String content, String timeAgo) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.timeAgo = timeAgo;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}