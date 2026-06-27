package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.MaintenanceRequest;
import com.example.movie_app.models.ReportedContentDto;
import com.example.movie_app.models.SafetyDashboardStats;
import com.example.movie_app.models.SystemLogDto;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafetyRepository {
    private final ApiService apiService;

    public SafetyRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<SafetyDashboardStats> getSafetyDashboardStats() {
        MutableLiveData<SafetyDashboardStats> data = new MutableLiveData<>();
        apiService.getSafetyDashboardStats().enqueue(new Callback<SafetyDashboardStats>() {
            @Override
            public void onResponse(Call<SafetyDashboardStats> call, Response<SafetyDashboardStats> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SafetyDashboardStats> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<ReportedContentDto>> getReportedContents() {
        MutableLiveData<List<ReportedContentDto>> data = new MutableLiveData<>();
        apiService.getReportedContents().enqueue(new Callback<List<ReportedContentDto>>() {
            @Override
            public void onResponse(Call<List<ReportedContentDto>> call, Response<List<ReportedContentDto>> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ReportedContentDto>> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<SystemLogDto>> getSystemLogs() {
        MutableLiveData<List<SystemLogDto>> data = new MutableLiveData<>();
        apiService.getSystemLogs().enqueue(new Callback<List<SystemLogDto>>() {
            @Override
            public void onResponse(Call<List<SystemLogDto>> call, Response<List<SystemLogDto>> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<SystemLogDto>> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public void toggleMaintenanceMode(boolean isEnabled, Callback<Void> callback) {
        apiService.toggleMaintenanceMode(new MaintenanceRequest(isEnabled)).enqueue(callback);
    }

    public void processReportAction(String reportId, String action, Callback<Void> callback) {
        apiService.processReportAction(reportId, action).enqueue(callback);
    }
}