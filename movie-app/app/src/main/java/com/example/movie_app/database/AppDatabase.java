package com.example.movie_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.movie_app.dao.HistoryDao;
import com.example.movie_app.dao.MovieDao; // Import DAO mới
import com.example.movie_app.entity.UserEntity;
import com.example.movie_app.entity.WatchHistoryEntity;
import com.example.movie_app.models.MovieItem; // Import Model
import com.example.movie_app.models.ResumeData;

@Database(entities = {UserEntity.class, WatchHistoryEntity.class, MovieItem.class, ResumeData.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract HistoryDao historyDao();
    public abstract MovieDao movieDao();

    public abstract ResumeDao resumeDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}