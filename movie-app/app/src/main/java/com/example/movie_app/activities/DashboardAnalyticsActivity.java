package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_app.R;
import com.example.movie_app.models.AdminStatsResponse;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import com.example.movie_app.utils.AdminNavigationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardAnalyticsActivity extends AppCompatActivity {

    private ScrollView scrollDashboardContainer;
    private ImageView imgToolbarProfile;
    private LinearLayout btnFilterTime;
    private Button btnExportReport;
    private TextView tvCounterTotalUsers, tvPeakHours, tvBtnSeeAll;
    private ProgressBar progressMovie, progressSeries;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_analytics);

        initViews();
        setupClickListeners();

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Kích hoạt tiến trình lấy số liệu thật từ Server
        fetchStatisticsFromServer();

        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        scrollDashboardContainer = findViewById(R.id.scroll_dashboard_container);
        imgToolbarProfile = findViewById(R.id.img_toolbar_profile);
        btnFilterTime = findViewById(R.id.btn_filter_time);
        btnExportReport = findViewById(R.id.btn_export_report);

        // Ánh xạ chính xác theo ID trong file XML của bạn
        tvCounterTotalUsers = findViewById(R.id.tv_counter_total_users);
        tvPeakHours = findViewById(R.id.tv_peak_hours);
        tvBtnSeeAll = findViewById(R.id.tv_btn_see_all);
        progressMovie = findViewById(R.id.progress_movie);
        progressSeries = findViewById(R.id.progress_series);
    }

    private void fetchStatisticsFromServer() {
        apiService.getAdminStats().enqueue(new Callback<AdminStatsResponse>() {
            @Override
            public void onResponse(Call<AdminStatsResponse> call, Response<AdminStatsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AdminStatsResponse stats = response.body();

                    // 1. Đổ số lượng User thật và định dạng có dấu phẩy phân tách (Ví dụ: 1,024)
                    tvCounterTotalUsers.setText(String.format("%,d", stats.getTotalUsers()));

                    // 2. Cập nhật chuỗi giờ cao điểm động
                    tvPeakHours.setText(stats.getPeakHours());

                    // 3. Tự động co giãn các thanh ProgressBar theo tỷ lệ phần trăm thật của DB
                    progressMovie.setProgress(stats.getMovieProgress());
                    progressSeries.setProgress(stats.getSeriesProgress());

                } else {
                    Toast.makeText(DashboardAnalyticsActivity.this, "Máy chủ trả về dữ liệu trống!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminStatsResponse> call, Throwable t) {
                Toast.makeText(DashboardAnalyticsActivity.this, "Lỗi đồng bộ dữ liệu: Thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnFilterTime.setOnClickListener(v ->
                Toast.makeText(DashboardAnalyticsActivity.this, "Lọc dữ liệu 30 ngày qua", Toast.LENGTH_SHORT).show()
        );

        btnExportReport.setOnClickListener(v ->
                Toast.makeText(DashboardAnalyticsActivity.this, "Đang xử lý xuất file báo cáo...", Toast.LENGTH_SHORT).show()
        );

        tvBtnSeeAll.setOnClickListener(v ->
                Toast.makeText(DashboardAnalyticsActivity.this, "Xem toàn bộ danh sách phim Hot", Toast.LENGTH_SHORT).show()
        );
    }
}