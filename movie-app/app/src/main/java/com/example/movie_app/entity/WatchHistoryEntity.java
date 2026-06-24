package com.example.movie_app.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watch_history")
public class WatchHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String movieId;

    public String userId;
    public String movieName;
    public String posterUrl;
    public long lastWatched;
    public int progress;

    public WatchHistoryEntity(@NonNull String movieId, String userId,
                              String movieName, String posterUrl,
                              long lastWatched, int progress) {
        this.movieId = movieId;
        this.userId = userId;
        this.movieName = movieName;
        this.posterUrl = posterUrl;
        this.lastWatched = lastWatched;
        this.progress = progress;
    }
}