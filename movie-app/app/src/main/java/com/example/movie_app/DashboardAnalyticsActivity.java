package com.example.movie_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardAnalyticsActivity extends AppCompatActivity {

    private ScrollView scrollDashboardContainer;
    private ImageView imgToolbarProfile;
    private LinearLayout btnFilterTime;
    private Button btnExportReport;
    private TextView tvCounterTotalUsers, tvPeakHours, tvBtnSeeAll;
    private ProgressBar progressMovie, progressSeries;
    private LinearLayout navDashboard, navMovies, navUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_analytics);

        initViews();

        setupClickListeners();
    }

    private void initViews() {
        scrollDashboardContainer = findViewById(R.id.scroll_dashboard_container);
        imgToolbarProfile = findViewById(R.id.img_toolbar_profile);
        btnFilterTime = findViewById(R.id.btn_filter_time);
        btnExportReport = findViewById(R.id.btn_export_report);
        tvCounterTotalUsers = findViewById(R.id.tv_counter_total_users);
        tvPeakHours = findViewById(R.id.tv_peak_hours);
        tvBtnSeeAll = findViewById(R.id.tv_btn_see_all);
        progressMovie = findViewById(R.id.progress_movie);
        progressSeries = findViewById(R.id.progress_series);
        navDashboard = findViewById(R.id.nav_dashboard);
        navMovies = findViewById(R.id.nav_movies);
        navUsers = findViewById(R.id.nav_users);
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

        navDashboard.setOnClickListener(v -> scrollDashboardContainer.smoothScrollTo(0, 0));

        navMovies.setOnClickListener(v ->
                Toast.makeText(DashboardAnalyticsActivity.this, "Mở phân hệ Movies", Toast.LENGTH_SHORT).show()
        );

        navUsers.setOnClickListener(v ->
                Toast.makeText(DashboardAnalyticsActivity.this, "Mở phân hệ Users", Toast.LENGTH_SHORT).show()
        );
    }
}