package com.example.movie_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.Movie;
import com.example.movie_app.models.ResumeData;
import com.example.movie_app.repository.VideoRepository;

public class VideoPlayerViewModel extends AndroidViewModel {
    private static final String TAG = "VideoPlayerViewModel";

    private VideoRepository videoRepository;
    private MutableLiveData<Movie> currentMovie;
    private LiveData<ResumeData> resumeData;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> isLoading;

    public VideoPlayerViewModel(@NonNull Application application) {
        super(application);
        this.videoRepository = new VideoRepository(application);
        this.currentMovie = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>(false);
    }

    public LiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(Movie movie) {
        currentMovie.postValue(movie);
    }

    public LiveData<ResumeData> getResumeData() {
        return resumeData;
    }

    public void loadResumeData(String movieId) {
        isLoading.postValue(true);
        Log.d(TAG, "Loading resume data for movie: " + movieId);

        resumeData = videoRepository.getResumeData(movieId);

        resumeData.observeForever(resume -> {
            if (resume != null) {
                Log.d(TAG, "✅ Resume found:");
                Log.d(TAG, "   Position: " + resume.getCurrentPosition() + " ms");
                Log.d(TAG, "   Episode: " + resume.getCurrentEpisode());
            } else {
                Log.d(TAG, "No resume found - starting from beginning");
            }
            isLoading.postValue(false);
        });
    }


    public void saveResumeData(String movieId, String movieTitle, long currentPosition,
                               long duration, int currentEpisode, String userId) {
        Log.d(TAG, "Saving resume data (Room DB):");
        Log.d(TAG, "  Position: " + currentPosition + " ms");
        Log.d(TAG, "  Episode: " + currentEpisode);

        //  GỌI REPOSITORY ĐỂ LƯU VÀO ROOM
        videoRepository.saveResumeData(movieId, movieTitle, currentPosition,
                duration, currentEpisode, userId);
    }

    public void clearResumeData(String movieId) {
        Log.d(TAG, "Clearing resume data for movie: " + movieId);
        videoRepository.deleteResumeData(movieId);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}