package com.example.movie_app.models;

public class ResumeData {
    private String movieId;
    private String movieTitle;
    private long currentPosition;
    private long duration;
    private int currentEpisode;
    private String lastWatchedTime;
    private String userId;

    public ResumeData() {}

    public ResumeData(String movieId, String movieTitle, long currentPosition,
                      long duration, int currentEpisode, String userId) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.currentPosition = currentPosition;
        this.duration = duration;
        this.currentEpisode = currentEpisode;
        this.userId = userId;
        this.lastWatchedTime = String.valueOf(System.currentTimeMillis());
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public long getCurrentPosition() { return currentPosition; }
    public void setCurrentPosition(long currentPosition) { this.currentPosition = currentPosition; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public int getCurrentEpisode() { return currentEpisode; }
    public void setCurrentEpisode(int currentEpisode) { this.currentEpisode = currentEpisode; }

    public String getLastWatchedTime() { return lastWatchedTime; }
    public void setLastWatchedTime(String lastWatchedTime) { this.lastWatchedTime = lastWatchedTime; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}