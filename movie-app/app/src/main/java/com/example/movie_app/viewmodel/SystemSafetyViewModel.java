package com.example.movie_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.SafetyDashboardStats;
import com.example.movie_app.models.ReportedContentDto;
import com.example.movie_app.models.SystemLogDto;
import com.example.movie_app.repository.SafetyRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SystemSafetyViewModel extends AndroidViewModel {

    private final SafetyRepository repository;
    private final MutableLiveData<SafetyDashboardStats> dashboardStats = new MutableLiveData<>();
    private final MutableLiveData<List<ReportedContentDto>> reportedList = new MutableLiveData<>();
    private final MutableLiveData<List<SystemLogDto>> systemLogs = new MutableLiveData<>();
    private final MutableLiveData<String> actionStatusMessage = new MutableLiveData<>();

    public SystemSafetyViewModel(@NonNull Application application) {
        super(application);
        repository = new SafetyRepository();
        fetchSafetyData();
    }

    public LiveData<SafetyDashboardStats> getDashboardStats() { return dashboardStats; }
    public LiveData<List<ReportedContentDto>> getReportedList() { return reportedList; }
    public LiveData<List<SystemLogDto>> getSystemLogs() { return systemLogs; }
    public LiveData<String> getActionStatusMessage() { return actionStatusMessage; }

    public void fetchSafetyData() {
        repository.getSafetyDashboardStats().observeForever(dashboardStats::setValue);
        repository.getReportedContents().observeForever(reportedList::setValue);
        repository.getSystemLogs().observeForever(systemLogs::setValue);
    }

    public void changeMaintenanceMode(boolean isEnabled) {
        repository.toggleMaintenanceMode(isEnabled, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    actionStatusMessage.setValue(isEnabled ? "Đã kích hoạt chế độ bảo trì toàn cục" : "Đã tắt chế độ bảo trì");
                    fetchSafetyData();
                } else {
                    actionStatusMessage.setValue("Lỗi khi thay đổi chế độ bảo trì");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                actionStatusMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void handleReport(String reportId, String actionType) {
        repository.processReportAction(reportId, actionType, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if ("IGNORE".equals(actionType)) {
                        actionStatusMessage.setValue("Đã bỏ qua báo cáo này");
                    } else if ("DELETE".equals(actionType)) {
                        actionStatusMessage.setValue("Đã xóa nội dung vi phạm!");
                    }
                    fetchSafetyData();
                } else {
                    actionStatusMessage.setValue("Lỗi khi xử lý báo cáo");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                actionStatusMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}