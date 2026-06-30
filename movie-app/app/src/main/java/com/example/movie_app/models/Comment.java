package com.example.movie_app.models;

public class Comment {
    private String id;
    private String username;
    private String contentComment;
    private long timestamp;
    private double rating;

    public Comment() {}

    public Comment(String id, String username, String contentComment, long timestamp, double rating) {
        this.id = id;
        this.username = username;
        this.contentComment = contentComment;
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getContentComment() { return contentComment; }
    public long getTimestamp() { return timestamp; }
    public double getRating() { return rating; }
}