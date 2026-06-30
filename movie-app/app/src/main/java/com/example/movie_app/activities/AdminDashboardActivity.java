package com.example.movie_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_app.R;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView tvTotalUsers, tvTotalMovies, tvPendingCount, tvStreamingCount;
    private TextView tvUserName, tvUserJoinedDate, tvAdminInitial;
    private TextView tvActivityMessage, tvActivityTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();
        initViews();

        // Gọi các hàm load dữ liệu
        loadStats();
        loadRecentUser();
        loadSystemActivity();

        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalMovies = findViewById(R.id.tvTotalMovies);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvStreamingCount = findViewById(R.id.tvStreamingCount);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserJoinedDate = findViewById(R.id.tvUserJoinedDate);
        tvAdminInitial = findViewById(R.id.tvHeaderInitial);

        tvActivityMessage = findViewById(R.id.tvActivityMessage);
        tvActivityTime = findViewById(R.id.tvActivityTime);
    }

    private void loadStats() {
        // 1. Lấy tổng số User
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                tvTotalUsers.setText(formatCount(count));
            }
        });

        // 2. Lấy tổng số Phim
        db.collection("movies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                tvTotalMovies.setText(String.valueOf(count));
            }
        });

        // 3. Lấy số lượng chờ kiểm duyệt (ví dụ từ collection 'reviews')
        db.collection("reviews")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvPendingCount.setText(String.valueOf(task.getResult().size()));
                    }
                });

        // Mặc định cho Streaming (hoặc lấy từ collection active_sessions nếu có)
        tvStreamingCount.setText("0");
    }

    private void loadRecentUser() {
        db.collection("users")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        var doc = queryDocumentSnapshots.getDocuments().get(0);
                        String name = doc.getString("fullName");
                        tvUserName.setText(name != null ? name : "Unknown User");

                        var timestamp = doc.getTimestamp("createdAt");
                        if (timestamp != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                            tvUserJoinedDate.setText("Joined " + sdf.format(timestamp.toDate()));
                        }
                    }
                });
    }

    private void loadSystemActivity() {
        // Lấy log hoạt động mới nhất (ví dụ: đăng ký mới)
        tvActivityMessage.setText("Hệ thống đang hoạt động ổn định");
        tvActivityTime.setText("VỪA XONG");
    }

    private String formatCount(int count) {
        if (count >= 1000) {
            return String.format(Locale.US, "%.1fK", count / 1000.0);
        }
        return String.valueOf(count);
    }
}
