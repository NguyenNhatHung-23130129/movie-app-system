package com.example.movie_app.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.movie_app.entity.WatchHistoryEntity;
import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(WatchHistoryEntity history);

    @Query("SELECT * FROM watch_history WHERE userId = :userId ORDER BY lastWatched DESC")
    List<WatchHistoryEntity> getHistoryByUserId(String userId);
}