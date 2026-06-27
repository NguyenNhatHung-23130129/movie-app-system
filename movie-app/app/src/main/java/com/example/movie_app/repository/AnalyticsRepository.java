package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.AnalyticsDashboardResponse;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsRepository {
    private final ApiService apiService;

    public AnalyticsRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<AnalyticsDashboardResponse> getAnalyticsData(int days) {
        MutableLiveData<AnalyticsDashboardResponse> data = new MutableLiveData<>();
        apiService.getAnalyticsData(days).enqueue(new Callback<AnalyticsDashboardResponse>() {
            @Override
            public void onResponse(Call<AnalyticsDashboardResponse> call, Response<AnalyticsDashboardResponse> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AnalyticsDashboardResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public void exportAnalyticsReport(int days, Callback<Void> callback) {
        apiService.exportAnalyticsReport(days).enqueue(callback);
    }
}