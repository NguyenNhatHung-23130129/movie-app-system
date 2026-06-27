package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.AdminUserDto;
import com.example.movie_app.models.ModerationDashboardStats;
import com.example.movie_app.models.ViolationCommentDto;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModerationRepository {
    private final ApiService apiService;

    public ModerationRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<ModerationDashboardStats> getModerationStats() {
        MutableLiveData<ModerationDashboardStats> data = new MutableLiveData<>();
        apiService.getModerationStats().enqueue(new Callback<ModerationDashboardStats>() {
            @Override
            public void onResponse(Call<ModerationDashboardStats> call, Response<ModerationDashboardStats> response) {
                if (response.isSuccessful()) data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ModerationDashboardStats> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<AdminUserDto>> getReportedUsers() {
        MutableLiveData<List<AdminUserDto>> data = new MutableLiveData<>();
        apiService.getReportedUsers().enqueue(new Callback<List<AdminUserDto>>() {
            @Override
            public void onResponse(Call<List<AdminUserDto>> call, Response<List<AdminUserDto>> response) {
                if (response.isSuccessful()) data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<AdminUserDto>> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<ViolationCommentDto>> getViolationComments() {
        MutableLiveData<List<ViolationCommentDto>> data = new MutableLiveData<>();
        apiService.getViolationComments().enqueue(new Callback<List<ViolationCommentDto>>() {
            @Override
            public void onResponse(Call<List<ViolationCommentDto>> call, Response<List<ViolationCommentDto>> response) {
                if (response.isSuccessful()) data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ViolationCommentDto>> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public void toggleUserLock(String userId, boolean isLock, Callback<Void> callback) {
        apiService.toggleUserLock(userId, isLock).enqueue(callback);
    }

    public void deleteComment(String commentId, Callback<Void> callback) {
        apiService.deleteComment(commentId).enqueue(callback);
    }

    public void ignoreComment(String commentId, Callback<Void> callback) {
        apiService.ignoreCommentViolation(commentId).enqueue(callback);
    }
}