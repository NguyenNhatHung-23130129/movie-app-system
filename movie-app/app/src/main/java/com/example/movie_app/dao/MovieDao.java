package com.example.movie_app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_app.models.MovieItem;
import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<MovieItem>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieItem> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieItem movie);

    @Update
    void update(MovieItem movie);

    @Delete
    void delete(MovieItem movie);

    @Query("DELETE FROM movies")
    void deleteAll();

    @Query("DELETE FROM movies WHERE id = :movieId")
    void deleteById(String movieId);
}