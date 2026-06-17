package com.example.movie_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SystemSafetyManagementActivity extends AppCompatActivity {

    private String reportCommentValue = "";

    // Khai báo các view thành phần sau tối ưu
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportCommentValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không xử lý
            }
        });

        // Lắng nghe sự kiện bật/tắt Switch bảo trì toàn cục
        switchMaintenance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("Maintenance Mode: Activated");
                } else {
                    System.out.println("Maintenance Mode: Deactivated");
                }
            }
        });

        // Sự kiện click Badge "Cấp bách"
        btnBadgeUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Badge Urgent Clicked");
            }
        });

        // Sự kiện nút Bỏ qua báo cáo
        btnIgnoreReport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Report 1: Ignored");
            }
        });

        // Sự kiện nút Xóa nội dung vi phạm
        btnDeleteReport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Report 1: Deleted");
            }
        });

        // Sự kiện Xem tất cả báo cáo
        btnViewAllReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("View All Reports Requested");
            }
        });

        // Sự kiện nhấn vào thẻ thống kê Tài khoản bị khóa
        btnPanelLockedAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Locked Accounts Dashboard Panel Opened");
            }
        });
    }
}