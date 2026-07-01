package com.example.movie_app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.movie_app.entity.FavoriteEntity;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE userId = :userId AND movieId = :movieId")
    void deleteFavorite(String userId, String movieId);

    @Query("SELECT * FROM favorites WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    LiveData<FavoriteEntity> isFavorite(String userId, String movieId);

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    LiveData<List<FavoriteEntity>> getFavoritesByUserId(String userId);
}