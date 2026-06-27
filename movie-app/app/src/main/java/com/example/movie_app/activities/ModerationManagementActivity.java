package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_app.R;
import com.example.movie_app.utils.AdminNavigationHelper;

public class ModerationManagementActivity extends AppCompatActivity {

    private LinearLayout btnLockUser1, btnUnlockUser2;
    private LinearLayout btnViewMovieContext, btnIgnoreComment, btnDeleteComment;
    private TextView tvTotalUsersCount, tvViolationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_moderation);

        initViews();
        setupClickListeners();
        initDataMocking();

        // Gọi Helper để quản lý Bottom Nav đồng bộ
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        btnLockUser1 = findViewById(R.id.btn_action_lock_user1);
        btnUnlockUser2 = findViewById(R.id.btn_action_unlock_user2);
        btnViewMovieContext = findViewById(R.id.btn_view_movie_context);
        btnIgnoreComment = findViewById(R.id.btn_ignore_comment);
        btnDeleteComment = findViewById(R.id.btn_delete_comment);
        tvTotalUsersCount = findViewById(R.id.tv_total_users_count);
        tvViolationBadge = findViewById(R.id.tv_violation_badge);
    }

    private void setupClickListeners() {
        btnLockUser1.setOnClickListener(v -> Toast.makeText(this, "Đã khóa tài khoản: An Nguyễn", Toast.LENGTH_SHORT).show());
        btnUnlockUser2.setOnClickListener(v -> Toast.makeText(this, "Đã mở khóa: Bảo Minh", Toast.LENGTH_SHORT).show());
        btnViewMovieContext.setOnClickListener(v -> Toast.makeText(this, "Xem ngữ cảnh phim", Toast.LENGTH_SHORT).show());
        btnIgnoreComment.setOnClickListener(v -> Toast.makeText(this, "Đã bỏ qua", Toast.LENGTH_SHORT).show());
        btnDeleteComment.setOnClickListener(v -> Toast.makeText(this, "Đã xóa bình luận", Toast.LENGTH_SHORT).show());
        // ĐÃ XÓA PHẦN GÁN CLICK CHO NAV TẠI ĐÂY ĐỂ TRÁNH XUNG ĐỘT
    }

    private void initDataMocking() {
        tvTotalUsersCount.setText("248 Tổng số");
        tvViolationBadge.setText("12 VI PHẠM MỚI");
    }
}