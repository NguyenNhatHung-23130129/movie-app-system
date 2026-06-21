package com.example.movie_app.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.ResumeData;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {
    private static final String TAG = "VideoRepository";
    private ApiService apiService;

    public VideoRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public MutableLiveData<ResumeData> getResumeData(String movieId) {
        MutableLiveData<ResumeData> resumeLiveData = new MutableLiveData<>();

        apiService.getResumeData(movieId).enqueue(new Callback<ResumeData>() {
            @Override
            public void onResponse(Call<ResumeData> call, Response<ResumeData> response) {
                if (response.isSuccessful()) {
                    ResumeData data = response.body();
                    Log.d(TAG, "Resume data loaded: position=" + (data != null ? data.getCurrentPosition() : 0));
                    resumeLiveData.postValue(data);
                } else {
                    Log.e(TAG, "Failed to load resume data: " + response.code());
                    resumeLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResumeData> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                resumeLiveData.postValue(null);
            }
        });

        return resumeLiveData;
    }

    public MutableLiveData<Boolean> saveResumeData(ResumeData resumeData) {
        MutableLiveData<Boolean> saveLiveData = new MutableLiveData<>();

        apiService.saveResumeData(resumeData).enqueue(new Callback<ResumeData>() {
            @Override
            public void onResponse(Call<ResumeData> call, Response<ResumeData> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resume data saved successfully");
                    saveLiveData.postValue(true);
                } else {
                    Log.e(TAG, "Failed to save resume data: " + response.code());
                    saveLiveData.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResumeData> call, Throwable t) {
                Log.e(TAG, "Network error while saving: " + t.getMessage());
                saveLiveData.postValue(false);
            }
        });

        return saveLiveData;
    }

    public MutableLiveData<Boolean> deleteResumeData(String movieId) {
        MutableLiveData<Boolean> deleteLiveData = new MutableLiveData<>();

        apiService.deleteResumeData(movieId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resume data deleted successfully");
                    deleteLiveData.postValue(true);
                } else {
                    Log.e(TAG, "Failed to delete resume data: " + response.code());
                    deleteLiveData.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error while deleting: " + t.getMessage());
                deleteLiveData.postValue(false);
            }
        });

        return deleteLiveData;
    }
}