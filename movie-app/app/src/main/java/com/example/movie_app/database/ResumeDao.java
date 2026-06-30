package com.example.movie_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.movie_app.models.ResumeData;

import java.util.List;

@Dao
public interface ResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateResume(ResumeData resume);

    @Query("SELECT * FROM resume_data WHERE movieId = :movieId")
    LiveData<ResumeData> getResumeByMovieId(String movieId);

    @Query("SELECT * FROM resume_data ORDER BY lastWatchedTime DESC")
    LiveData<List<ResumeData>> getAllResumes();

    @Query("DELETE FROM resume_data WHERE movieId = :movieId")
    void deleteResumeByMovieId(String movieId);

    @Query("DELETE FROM resume_data")
    void deleteAllResumes();
}