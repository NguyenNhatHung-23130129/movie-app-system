package com.example.movie_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.movie_app.database.AppDatabase;
import com.example.movie_app.database.ResumeDao;
import com.example.movie_app.models.ResumeData;  // ✅ Import ResumeData thay vì ResumeEntity

public class VideoRepository {
    private static final String TAG = "VideoRepository";
    private ResumeDao resumeDao;

    public VideoRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.resumeDao = database.resumeDao();
    }

    public LiveData<ResumeData> getResumeData(String movieId) {
        Log.d(TAG, "Getting resume data for movie: " + movieId);
        return resumeDao.getResumeByMovieId(movieId);
    }

    public void saveResumeData(String movieId, String movieTitle,
                               long currentPosition, long duration,
                               int currentEpisode) {
        Log.d(TAG, "Saving resume data:");
        Log.d(TAG, "  Movie: " + movieTitle);
        Log.d(TAG, "  Position: " + currentPosition + " ms");
        Log.d(TAG, "  Episode: " + currentEpisode);

        ResumeData resume = new ResumeData(movieId, movieTitle,
                currentPosition, duration, currentEpisode);

        new Thread(() -> {
            resumeDao.insertOrUpdateResume(resume);
            Log.d(TAG, " Saved successfully to Room DB!");
        }).start();
    }

    public void deleteResumeData(String movieId) {
        Log.d(TAG, "Deleting resume for movie: " + movieId);

        new Thread(() -> {
            resumeDao.deleteResumeByMovieId(movieId);
            Log.d(TAG, " Deleted from Room DB!");
        }).start();
    }

    public LiveData<java.util.List<ResumeData>> getAllResumes() {
        return resumeDao.getAllResumes();
    }

    public void saveResumeDataSync(String movieId, String movieTitle,
                                   long currentPosition, long duration,
                                   int currentEpisode) {
        ResumeData resume = new ResumeData(movieId, movieTitle,
                currentPosition, duration, currentEpisode);
        resumeDao.insertOrUpdateResume(resume);
    }
}