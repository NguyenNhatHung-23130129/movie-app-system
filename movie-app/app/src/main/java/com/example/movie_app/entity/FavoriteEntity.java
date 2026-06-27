package com.example.movie_app.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String userId;

    @NonNull
    public String movieId;

    public String movieName;
    public String posterUrl;
    public long timestamp;

    public FavoriteEntity(@NonNull String userId, @NonNull String movieId, String movieName, String posterUrl, long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterUrl = posterUrl;
        this.timestamp = timestamp;
    }
}