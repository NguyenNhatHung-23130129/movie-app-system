package com.example.movie_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.AdminUserDto;
import com.example.movie_app.models.ViolationCommentDto;
import com.example.movie_app.models.ModerationDashboardStats;
import com.example.movie_app.repository.ModerationRepository;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModerationViewModel extends AndroidViewModel {

    private final ModerationRepository repository;
    private final MutableLiveData<ModerationDashboardStats> statsData = new MutableLiveData<>();
    private final MutableLiveData<List<AdminUserDto>> reportedUsers = new MutableLiveData<>();
    private final MutableLiveData<List<ViolationCommentDto>> violationComments = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public ModerationViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ModerationRepository();
        loadDashboardData();
    }

    public LiveData<ModerationDashboardStats> getStatsData() { return statsData; }
    public LiveData<List<AdminUserDto>> getReportedUsers() { return reportedUsers; }
    public LiveData<List<ViolationCommentDto>> getViolationComments() { return violationComments; }
    public LiveData<String> getToastMessage() { return toastMessage; }

    public void loadDashboardData() {
        repository.getModerationStats().observeForever(statsData::setValue);
        repository.getReportedUsers().observeForever(reportedUsers::setValue);
        repository.getViolationComments().observeForever(violationComments::setValue);
    }

    public void changeUserLockStatus(AdminUserDto user) {
        boolean targetLockState = !user.isLocked();
        repository.toggleUserLock(user.getId(), targetLockState, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessage.setValue((targetLockState ? "Đã khóa: " : "Đã mở khóa: ") + user.getName());
                    loadDashboardData();
                } else {
                    toastMessage.setValue("Lỗi khi cập nhật trạng thái người dùng");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void deleteComment(String commentId) {
        repository.deleteComment(commentId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessage.setValue("Đã xóa bình luận khỏi hệ thống!");
                    loadDashboardData();
                } else {
                    toastMessage.setValue("Lỗi khi xóa bình luận");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void ignoreComment(String commentId) {
        repository.ignoreComment(commentId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessage.setValue("Bỏ qua cảnh báo vi phạm này");
                    loadDashboardData();
                } else {
                    toastMessage.setValue("Lỗi khi xử lý");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}