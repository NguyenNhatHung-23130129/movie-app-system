package com.example.movie_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.TrendingMovieAdapter;
import com.example.movie_app.models.ChartDataPoint;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.DashboardAnalyticsViewModel;

import java.util.List;

public class DashboardAnalyticsActivity extends BaseAdminActivity {

    private TextView tvFilterTimeLabel, tvGrowthPercentage, tvCounterTotalUsers, tvPeakHours;
    private TextView tvMovieWatchTime, tvSeriesWatchTime;
    private ProgressBar progressMovie, progressSeries;
    private LinearLayout layoutBarChart, layoutChartLabels;
    private View btnExportReport, btnFilterTime;

    private RecyclerView rvTrendingMovies;
    private TrendingMovieAdapter trendingAdapter;
    private DashboardAnalyticsViewModel analyticsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_analytics);

        initViews();
        setupAdminInfo();
        setupRecyclerView();
        initViewModel();
        setupClickListeners();
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvGrowthPercentage = findViewById(R.id.tv_growth_percentage);
        tvCounterTotalUsers = findViewById(R.id.tv_counter_total_users);
        tvMovieWatchTime = findViewById(R.id.tv_movie_watch_time);
        tvSeriesWatchTime = findViewById(R.id.tv_series_watch_time);

        progressMovie = findViewById(R.id.progress_movie);
        progressSeries = findViewById(R.id.progress_series);
        layoutBarChart = findViewById(R.id.layout_bar_chart);
        layoutChartLabels = findViewById(R.id.layout_chart_labels);

        btnExportReport = findViewById(R.id.btn_export_report_action);
        rvTrendingMovies = findViewById(R.id.rv_trending_movies);

        tvFilterTimeLabel = findViewById(R.id.tv_filter_time_label);
        tvPeakHours = findViewById(R.id.tv_peak_hours);
        btnFilterTime = findViewById(R.id.btn_filter_time);
    }

    private void setupRecyclerView() {
        if (rvTrendingMovies == null) return;
        rvTrendingMovies.setLayoutManager(new LinearLayoutManager(this));
        trendingAdapter = new TrendingMovieAdapter(movie -> {
            Toast.makeText(this, "Quản lý nội dung: " + movie.getName(), Toast.LENGTH_SHORT).show();
        });
        rvTrendingMovies.setAdapter(trendingAdapter);
    }

    private void initViewModel() {
        analyticsViewModel = new ViewModelProvider(this).get(DashboardAnalyticsViewModel.class);

        analyticsViewModel.getSelectedFilterDays().observe(this, days -> {
            if (tvFilterTimeLabel != null) tvFilterTimeLabel.setText(days + " ngày qua");
        });

        analyticsViewModel.getAnalyticsData().observe(this, data -> {
            if (data != null) {
                updateUI(data);
            }
        });

        analyticsViewModel.getOperationMessage().observe(this, message -> {
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI(com.example.movie_app.models.AnalyticsDashboardResponse data) {
        if (tvGrowthPercentage != null && data.getGrowthRateText() != null) {
            tvGrowthPercentage.setText(data.getGrowthRateText());
            tvGrowthPercentage.setTextColor(data.getGrowthRateText().contains("▼") ?
                    Color.parseColor("#EF4444") : Color.parseColor("#22C55E"));
        }

        if (tvCounterTotalUsers != null) tvCounterTotalUsers.setText(data.getTotalUsersFormatted());
        if (tvPeakHours != null) tvPeakHours.setText(data.getPeakHoursInterval());

        if (tvMovieWatchTime != null) tvMovieWatchTime.setText(data.getMovieMinutesFormatted() + "M");
        if (progressMovie != null) progressMovie.setProgress(data.getMoviePercentage());

        if (tvSeriesWatchTime != null) tvSeriesWatchTime.setText(data.getSeriesMinutesFormatted() + "M");
        if (progressSeries != null) progressSeries.setProgress(data.getSeriesPercentage());

        populateDynamicChart(data.getChartDataPoints());
        if (trendingAdapter != null) trendingAdapter.submitList(data.getTrendingMovies());
    }

    private void populateDynamicChart(List<ChartDataPoint> points) {
        if (layoutBarChart == null || layoutChartLabels == null) return;

        layoutBarChart.removeAllViews();
        layoutChartLabels.removeAllViews();
        if (points == null || points.isEmpty()) return;

        float density = getResources().getDisplayMetrics().density;
        for (ChartDataPoint point : points) {
            View bar = new View(this);
            int heightPx = (int) (point.getHeightInDp() * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, heightPx, 1f);
            params.setMargins(10, 0, 10, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundResource(point.isHighlight() ? R.drawable.chart_bar_red : R.drawable.chart_bar_gray);
            layoutBarChart.addView(bar);

            TextView label = new TextView(this);
            label.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            label.setGravity(android.view.Gravity.CENTER);
            label.setText(point.getLabelDate());
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            label.setTextColor(Color.parseColor("#8E8E93"));
            layoutChartLabels.addView(label);
        }
    }

    private void setupClickListeners() {
        if (btnFilterTime != null) {
            btnFilterTime.setOnClickListener(v -> {
                Integer currentVal = analyticsViewModel.getSelectedFilterDays().getValue();
                int current = currentVal != null ? currentVal : 30;
                analyticsViewModel.setFilterDays(current == 30 ? 7 : 30);
            });
        }

        if (btnExportReport != null) {
            btnExportReport.setOnClickListener(v -> analyticsViewModel.exportReport());
        }

        View actionAddMovie = findViewById(R.id.action_add_movie);
        if (actionAddMovie != null) {
            actionAddMovie.setOnClickListener(v -> {
                startActivity(new Intent(this, MovieManagementActivity.class));
            });
        }

        View actionManageUsers = findViewById(R.id.action_manage_users);
        if (actionManageUsers != null) {
            actionManageUsers.setOnClickListener(v -> {
                startActivity(new Intent(this, ModerationManagementActivity.class));
            });
        }

        View actionModeration = findViewById(R.id.action_moderation);
        if (actionModeration != null) {
            actionModeration.setOnClickListener(v -> {
                startActivity(new Intent(this, ModerationManagementActivity.class));
            });
        }

        View actionSafety = findViewById(R.id.action_safety);
        if (actionSafety != null) {
            actionSafety.setOnClickListener(v -> {
                startActivity(new Intent(this, SystemSafetyManagementActivity.class));
            });
        }

        View btnSeeAll = findViewById(R.id.tv_btn_see_all);
        if (btnSeeAll != null) {
            btnSeeAll.setOnClickListener(v -> {
                startActivity(new Intent(this, MovieManagementActivity.class));
            });
        }
    }
}