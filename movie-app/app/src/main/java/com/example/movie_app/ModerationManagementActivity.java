package com.example.movie_app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ModerationManagementActivity extends AppCompatActivity {

    private LinearLayout btnLockUser1, btnUnlockUser2;
    private LinearLayout btnViewMovieContext, btnIgnoreComment, btnDeleteComment;

    private LinearLayout navDashboard, navMovies, navModerate, navSafety;
    private TextView tvTotalUsersCount, tvViolationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_moderation);

        initViews();

        setupClickListeners();

        initDataMocking();

        setupActiveTab();
    }

    private void initViews() {
        btnLockUser1 = findViewById(R.id.btn_action_lock_user1);
        btnUnlockUser2 = findViewById(R.id.btn_action_unlock_user2);

        btnViewMovieContext = findViewById(R.id.btn_view_movie_context);
        btnIgnoreComment = findViewById(R.id.btn_ignore_comment);
        btnDeleteComment = findViewById(R.id.btn_delete_comment);

        tvTotalUsersCount = findViewById(R.id.tv_total_users_count);
        tvViolationBadge = findViewById(R.id.tv_violation_badge);

        navDashboard = findViewById(R.id.nav_dashboard);
        navMovies = findViewById(R.id.nav_movies);
        navModerate = findViewById(R.id.nav_moderate);
        navSafety = findViewById(R.id.nav_safety);
    }

    private void setupClickListeners() {
        btnLockUser1.setOnClickListener(v ->
                Toast.makeText(this, "Đã thực hiện khóa tài khoản: An Nguyễn", Toast.LENGTH_SHORT).show()
        );

        btnUnlockUser2.setOnClickListener(v ->
                Toast.makeText(this, "Đã mở khóa tài khoản thành công cho: Bảo Minh", Toast.LENGTH_SHORT).show()
        );

        btnViewMovieContext.setOnClickListener(v ->
                Toast.makeText(this, "Di chuyển đến ngữ cảnh bộ phim: Inception", Toast.LENGTH_SHORT).show()
        );

        btnIgnoreComment.setOnClickListener(v ->
                Toast.makeText(this, "Bỏ qua cảnh báo vi phạm này", Toast.LENGTH_SHORT).show()
        );

        btnDeleteComment.setOnClickListener(v ->
                Toast.makeText(this, "Đã xóa bình luận vi phạm khỏi hệ thống!", Toast.LENGTH_SHORT).show()
        );

        // Xử lý chuyển hướng click từ các menu dùng chung
        navDashboard.setOnClickListener(v -> Toast.makeText(this, "Chuyển về Tổng quan Dashboard", Toast.LENGTH_SHORT).show());
        navMovies.setOnClickListener(v -> Toast.makeText(this, "Mở danh sách Phim", Toast.LENGTH_SHORT).show());
        navModerate.setOnClickListener(v -> Toast.makeText(this, "Bạn đang ở Tab Kiểm duyệt", Toast.LENGTH_SHORT).show());
        navSafety.setOnClickListener(v -> Toast.makeText(this, "Mở trung tâm bảo mật An toàn hệ thống", Toast.LENGTH_SHORT).show());
    }

    private void setupActiveTab() {
        // Ánh xạ trực tiếp ID của ImageView và TextView bên trong file include
        ImageView imgModerate = findViewById(R.id.img_nav_moderate);
        TextView tvModerate = findViewById(R.id.tv_nav_moderate);

        if (imgModerate != null && tvModerate != null) {
            // Đổi toàn bộ icon và chữ của phân hệ này sang màu đỏ chủ đạo của app
            imgModerate.setColorFilter(Color.parseColor("#E50914"));
            tvModerate.setTextColor(Color.parseColor("#E50914"));
        }
    }

    private void initDataMocking() {
        tvTotalUsersCount.setText("248 Tổng số");
        tvViolationBadge.setText("12 VI PHẠM MỚI");
    }
}