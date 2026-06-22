package com.example.movie_app.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.movie_app.models.Movie;
import com.example.movie_app.models.ResumeData;
import com.example.movie_app.repository.VideoRepository;

public class VideoPlayerViewModel extends ViewModel {
    private static final String TAG = "VideoPlayerViewModel";

    private VideoRepository videoRepository;
    private MutableLiveData<Movie> currentMovie;
    private MutableLiveData<ResumeData> resumeData;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> isLoading;

    public VideoPlayerViewModel() {
        this.videoRepository = new VideoRepository();
        this.currentMovie = new MutableLiveData<>();
        this.resumeData = new MutableLiveData<>();
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

        LiveData<ResumeData> data = videoRepository.getResumeData(movieId);
        data.observeForever(resumeData -> {
            this.resumeData.postValue(resumeData);
            isLoading.postValue(false);

            if (resumeData != null && resumeData.getCurrentPosition() > 0) {
                Log.d(TAG, "Resume position found: " + resumeData.getCurrentPosition() + "ms");
            } else {
                Log.d(TAG, "No resume data found, starting from beginning");
            }
        });
    }

    public void saveResumeData(String movieId, String movieTitle, long currentPosition,
                               long duration, int currentEpisode, String userId) {
        Log.d(TAG, "Saving resume data: position=" + currentPosition + ", episode=" + currentEpisode);

        ResumeData data = new ResumeData(movieId, movieTitle, currentPosition, duration, currentEpisode, userId);

        LiveData<Boolean> result = videoRepository.saveResumeData(data);
        result.observeForever(success -> {
            if (success) {
                Log.d(TAG, "Resume data saved successfully");
            } else {
                errorMessage.postValue("Failed to save resume data");
                Log.e(TAG, "Failed to save resume data");
            }
        });
    }

    public void clearResumeData(String movieId) {
        Log.d(TAG, "Clearing resume data for movie: " + movieId);

        LiveData<Boolean> result = videoRepository.deleteResumeData(movieId);
        result.observeForever(success -> {
            if (success) {
                resumeData.postValue(null);
                Log.d(TAG, "Resume data cleared");
            } else {
                errorMessage.postValue("Failed to clear resume data");
            }
        });
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}