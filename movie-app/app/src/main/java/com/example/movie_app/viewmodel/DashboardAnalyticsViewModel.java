package com.example.movie_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.movie_app.models.AnalyticsDashboardResponse;
import com.example.movie_app.repository.AnalyticsRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardAnalyticsViewModel extends AndroidViewModel {

    private final AnalyticsRepository repository;
    private final MutableLiveData<Integer> selectedFilterDays = new MutableLiveData<>(30);
    private final LiveData<AnalyticsDashboardResponse> analyticsData;
    private final MutableLiveData<String> operationMessage = new MutableLiveData<>();

    public DashboardAnalyticsViewModel(@NonNull Application application) {
        super(application);
        repository = new AnalyticsRepository();
        
        // Mỗi khi selectedFilterDays thay đổi, switchMap sẽ gọi repository để lấy dữ liệu mới
        analyticsData = Transformations.switchMap(selectedFilterDays, repository::getAnalyticsData);
    }

    public LiveData<AnalyticsDashboardResponse> getAnalyticsData() { return analyticsData; }
    public LiveData<Integer> getSelectedFilterDays() { return selectedFilterDays; }
    public LiveData<String> getOperationMessage() { return operationMessage; }

    public void setFilterDays(int days) {
        selectedFilterDays.setValue(days);
    }

    public void loadAnalytics() {
        // Trigger reload bằng cách set lại giá trị hiện tại
        Integer current = selectedFilterDays.getValue();
        selectedFilterDays.setValue(current != null ? current : 30);
    }

    public void exportReport() {
        int days = selectedFilterDays.getValue() != null ? selectedFilterDays.getValue() : 30;
        operationMessage.setValue("Đang khởi tạo xuất file báo cáo " + days + " ngày qua...");
        
        repository.exportAnalyticsReport(days, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    operationMessage.postValue("Xuất báo cáo thành công!");
                } else {
                    operationMessage.postValue("Lỗi khi xuất báo cáo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                operationMessage.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}