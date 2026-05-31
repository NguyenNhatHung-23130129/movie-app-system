package com.example.movie_app.models;

public class MovieItem {
    private String id;
    private String name;
    private String originName;
    private String posterUrl;
    private String thumbUrl;
    private int year;

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getThumbUrl() { return thumbUrl; }
    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}