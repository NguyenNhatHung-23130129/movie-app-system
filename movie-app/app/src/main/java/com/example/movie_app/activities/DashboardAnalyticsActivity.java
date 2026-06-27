package com.example.movie_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.TrendingMovieAdapter;
import com.example.movie_app.models.ChartDataPoint;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.DashboardAnalyticsViewModel;

import java.util.List;

public class DashboardAnalyticsActivity extends AppCompatActivity {

    private TextView tvFilterTimeLabel, tvGrowthPercentage, tvCounterTotalUsers, tvCounterUserSubtext, tvPeakHours;
    private TextView tvMovieWatchTime, tvSeriesWatchTime;
    private ProgressBar progressMovie, progressSeries;
    private LinearLayout layoutBarChart, layoutChartLabels;
    private Button btnExportReport;

    private RecyclerView rvTrendingMovies;
    private TrendingMovieAdapter trendingAdapter;
    private DashboardAnalyticsViewModel analyticsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_analytics);

        initViews();
        setupRecyclerView();
        initViewModel();
        setupClickListeners();
        
        // Khởi tạo thanh điều hướng Bottom Navigation
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvFilterTimeLabel = findViewById(R.id.tv_filter_time_label);
        tvGrowthPercentage = findViewById(R.id.tv_growth_percentage);
        tvCounterTotalUsers = findViewById(R.id.tv_counter_total_users);
        tvCounterUserSubtext = findViewById(R.id.tv_counter_user_subtext);
        tvPeakHours = findViewById(R.id.tv_peak_hours);
        tvMovieWatchTime = findViewById(R.id.tv_movie_watch_time);
        tvSeriesWatchTime = findViewById(R.id.tv_series_watch_time);

        progressMovie = findViewById(R.id.progress_movie);
        progressSeries = findViewById(R.id.progress_series);
        layoutBarChart = findViewById(R.id.layout_bar_chart);
        layoutChartLabels = findViewById(R.id.layout_chart_labels);
        btnExportReport = findViewById(R.id.btn_export_report);
        rvTrendingMovies = findViewById(R.id.rv_trending_movies);
    }

    private void setupRecyclerView() {
        rvTrendingMovies.setLayoutManager(new LinearLayoutManager(this));
        trendingAdapter = new TrendingMovieAdapter(movie -> {
            Toast.makeText(this, "Thống kê chi tiết: " + movie.getName(), Toast.LENGTH_SHORT).show();
        });
        rvTrendingMovies.setAdapter(trendingAdapter);
    }

    private void initViewModel() {
        analyticsViewModel = new ViewModelProvider(this).get(DashboardAnalyticsViewModel.class);

        // Cập nhật nhãn thời gian khi bộ lọc thay đổi
        analyticsViewModel.getSelectedFilterDays().observe(this, days -> {
            if (tvFilterTimeLabel != null) tvFilterTimeLabel.setText(days + " ngày qua");
        });

        // Quan sát dữ liệu thực từ Server
        analyticsViewModel.getAnalyticsData().observe(this, data -> {
            if (data != null) {
                updateUI(data);
            }
        });

        // Hiển thị thông báo khi thực hiện các tác vụ (như xuất báo cáo)
        analyticsViewModel.getOperationMessage().observe(this, message -> {
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI(com.example.movie_app.models.AnalyticsDashboardResponse data) {
        if (data.getGrowthRateText() != null) {
            tvGrowthPercentage.setText(data.getGrowthRateText() + " so với tháng trước");
        }
        tvCounterTotalUsers.setText(data.getTotalUsersFormatted());
        tvCounterUserSubtext.setText(data.getUserSubtext());
        tvPeakHours.setText(data.getPeakHoursInterval());

        tvMovieWatchTime.setText(data.getMovieMinutesFormatted() + "M phút");
        progressMovie.setProgress(data.getMoviePercentage());
        tvSeriesWatchTime.setText(data.getSeriesMinutesFormatted() + "M phút");
        progressSeries.setProgress(data.getSeriesPercentage());

        populateDynamicChart(data.getChartDataPoints());
        trendingAdapter.submitList(data.getTrendingMovies());
    }

    private void populateDynamicChart(List<ChartDataPoint> points) {
        layoutBarChart.removeAllViews();
        layoutChartLabels.removeAllViews();
        if (points == null) return;

        float density = getResources().getDisplayMetrics().density;
        for (ChartDataPoint point : points) {
            View bar = new View(this);
            int heightPx = (int) (point.getHeightInDp() * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, heightPx, 1f);
            params.setMargins(8, 0, 8, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundResource(point.isHighlight() ? R.drawable.chart_bar_red : R.drawable.chart_bar_gray);
            layoutBarChart.addView(bar);

            TextView label = new TextView(this);
            label.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            label.setGravity(android.view.Gravity.CENTER);
            label.setText(point.getLabelDate());
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            label.setTextColor(Color.parseColor("#8E8E93"));
            layoutChartLabels.addView(label);
        }
    }

    private void setupClickListeners() {
        findViewById(R.id.btn_filter_time).setOnClickListener(v -> {
            int current = analyticsViewModel.getSelectedFilterDays().getValue();
            analyticsViewModel.setFilterDays(current == 30 ? 7 : 30);
        });

        btnExportReport.setOnClickListener(v -> analyticsViewModel.exportReport());
    }
}