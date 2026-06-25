package com.example.movie_app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.movie_app.models.MovieItem;
import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<MovieItem>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieItem> movies);

    @Query("DELETE FROM movies")
    void deleteAll();
}