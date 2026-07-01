package com.example.movie_app.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.movie_app.dao.HistoryDao;
import com.example.movie_app.database.AppDatabase;
import com.example.movie_app.entity.WatchHistoryEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryRepository {
    private final HistoryDao historyDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public HistoryRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        historyDao = db.historyDao();
    }

    public void insertHistory(WatchHistoryEntity history) {
        executor.execute(() -> historyDao.insertHistory(history));
    }

    public LiveData<List<WatchHistoryEntity>> getHistory(String userId) {
        return historyDao.getHistoryByUserId(userId);
    }
}