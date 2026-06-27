package com.example.movie_app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.movie_app.R;
import com.example.movie_app.utils.AdminNavigationHelper;

public class SystemSafetyManagementActivity extends AppCompatActivity {

    private String reportCommentValue = "";
    private SwitchCompat switchMaintenance;
    private EditText edtReportContent1;
    private TextView btnBadgeUrgent;
    private TextView btnIgnoreReport1;
    private TextView btnDeleteReport1;
    private TextView btnViewAllReports;
    private View btnPanelLockedAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_system_safety);

        initViews();
        setupListeners();

        // Kích hoạt thanh điều hướng Admin
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        switchMaintenance = findViewById(R.id.switch_maintenance);
        edtReportContent1 = findViewById(R.id.edt_report_content_1);
        btnBadgeUrgent = findViewById(R.id.btn_badge_urgent);
        btnIgnoreReport1 = findViewById(R.id.btn_ignore_report_1);
        btnDeleteReport1 = findViewById(R.id.btn_delete_report_1);
        btnViewAllReports = findViewById(R.id.btn_view_all_reports);
        btnPanelLockedAccounts = findViewById(R.id.btn_panel_locked_accounts);
    }

    private void setupListeners() {
        edtReportContent1.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportCommentValue = s.toString();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        switchMaintenance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Xử lý logic bảo trì
        });

        btnBadgeUrgent.setOnClickListener(v -> {});
        btnIgnoreReport1.setOnClickListener(v -> {});
        btnDeleteReport1.setOnClickListener(v -> {});
        btnViewAllReports.setOnClickListener(v -> {});
        btnPanelLockedAccounts.setOnClickListener(v -> {});
    }
}