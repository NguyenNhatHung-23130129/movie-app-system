package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.ReportedContentAdapter;
import com.example.movie_app.adapter.SystemLogAdapter;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.SystemSafetyViewModel;

public class SystemSafetyManagementActivity extends BaseAdminActivity {

    private TextView tvSafetyPercentage, tvSafetyStatus, btnBadgeUrgent, btnViewAllReports;
    private TextView tvLockedAccountsCount, tvSecurityVersion;
    private SwitchCompat switchMaintenance;

    private RecyclerView rvReportedContents, rvSystemLogs;
    private ReportedContentAdapter reportedAdapter;
    private SystemLogAdapter logAdapter;

    private SystemSafetyViewModel safetyViewModel;
    private boolean isUserInitiatedChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_system_safety);

        initViews();
        setupAdminInfo();
        setupRecyclerViews();
        initViewModel();
        setupInteractions();
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvSafetyPercentage = findViewById(R.id.tv_safety_percentage);
        tvSafetyStatus = findViewById(R.id.tv_safety_status);
        btnBadgeUrgent = findViewById(R.id.btn_badge_urgent);
        btnViewAllReports = findViewById(R.id.btn_view_all_reports);
        tvLockedAccountsCount = findViewById(R.id.tv_locked_accounts_count);
        tvSecurityVersion = findViewById(R.id.tv_security_version);
        switchMaintenance = findViewById(R.id.switch_maintenance);

        rvReportedContents = findViewById(R.id.rv_reported_contents);
        rvSystemLogs = findViewById(R.id.rv_system_logs);
    }

    private void setupRecyclerViews() {
        rvReportedContents.setLayoutManager(new LinearLayoutManager(this));
        reportedAdapter = new ReportedContentAdapter(
                report -> safetyViewModel.handleReport(report.getId(), "IGNORE"),
                report -> safetyViewModel.handleReport(report.getId(), "DELETE")
        );
        rvReportedContents.setAdapter(reportedAdapter);

        rvSystemLogs.setLayoutManager(new LinearLayoutManager(this));
        logAdapter = new SystemLogAdapter();
        rvSystemLogs.setAdapter(logAdapter);
    }

    private void initViewModel() {
        safetyViewModel = new ViewModelProvider(this).get(SystemSafetyViewModel.class);

        safetyViewModel.getDashboardStats().observe(this, stats -> {
            if (stats != null) {
                tvSafetyPercentage.setText(stats.getSafetyPercentage() + "%");
                tvSafetyStatus.setText(stats.getSafetyStatusText());
                btnBadgeUrgent.setText(stats.getUrgentCount() + " Cấp bách");
                btnViewAllReports.setText("Xem tất cả báo cáo (" + stats.getTotalReportsCount() + ")");
                tvLockedAccountsCount.setText(String.valueOf(stats.getLockedAccountsCount()));
                tvSecurityVersion.setText(stats.getSecurityVersion());

                isUserInitiatedChange = false;
                switchMaintenance.setChecked(stats.isMaintenanceModeActive());
                isUserInitiatedChange = true;
            }
        });

        safetyViewModel.getReportedList().observe(this, reports -> {
            if (reports != null) reportedAdapter.submitList(reports);
        });

        safetyViewModel.getSystemLogs().observe(this, logs -> {
            if (logs != null) logAdapter.submitList(logs);
        });

        safetyViewModel.getActionStatusMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInteractions() {
        if (switchMaintenance != null) {
            switchMaintenance.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isUserInitiatedChange) {
                    safetyViewModel.changeMaintenanceMode(isChecked);
                }
            });
        }
    }
}
