package com.example.movie_app.models;

public class Movie {
    private String movieId;
    private String title;
    private String description;
    private String posterUrl;
    private String videoUrl;
    private long duration;
    private int episodes;
    private int currentEpisode;
    private double rating;
    private String quality;

    public Movie() {}

    public Movie(String movieId, String title, String videoUrl, long duration) {
        this.movieId = movieId;
        this.title = title;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.currentEpisode = 1;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public int getEpisodes() { return episodes; }
    public void setEpisodes(int episodes) { this.episodes = episodes; }

    public int getCurrentEpisode() { return currentEpisode; }
    public void setCurrentEpisode(int currentEpisode) { this.currentEpisode = currentEpisode; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
}