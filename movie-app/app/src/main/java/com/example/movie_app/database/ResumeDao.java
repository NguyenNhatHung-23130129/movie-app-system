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

    // LẤY DỮ LIỆU: Lấy resume của 1 phim theo movieId
    @Query("SELECT * FROM resume_data WHERE movieId = :movieId")
    LiveData<ResumeData> getResumeByMovieId(String movieId);

    //  LẤY TOÀN BỘ: Lấy tất cả resume đã lưu
    @Query("SELECT * FROM resume_data ORDER BY lastWatchedTime DESC")
    LiveData<List<ResumeData>> getAllResumes();

    // XÓA: Xóa resume của 1 phim
    @Query("DELETE FROM resume_data WHERE movieId = :movieId")
    void deleteResumeByMovieId(String movieId);

    //  XÓA TOÀN BỘ
    @Query("DELETE FROM resume_data")
    void deleteAllResumes();
}