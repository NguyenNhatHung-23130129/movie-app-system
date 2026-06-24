package com.example.movie_app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
@Entity(tableName = "resume_data")
public class ResumeData {

    @PrimaryKey
    @NonNull
    private String movieId;

    private String movieTitle;
    private long currentPosition;
    private long duration;
    private int currentEpisode;
    private String lastWatchedTime;

    public ResumeData() {}

    @Ignore
    public ResumeData(@NonNull String movieId, String movieTitle, long currentPosition,
                      long duration, int currentEpisode) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.currentPosition = currentPosition;
        this.duration = duration;
        this.currentEpisode = currentEpisode;
        this.lastWatchedTime = String.valueOf(System.currentTimeMillis());
    }

    // Getters & Setters
    public String getMovieId() { return movieId; }
    public void setMovieId(@NonNull String movieId) { this.movieId = movieId; }

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

    @Override
    public String toString() {
        return "ResumeData{" +
                "movieId='" + movieId + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", currentPosition=" + currentPosition +
                ", currentEpisode=" + currentEpisode +
                '}';
    }
}