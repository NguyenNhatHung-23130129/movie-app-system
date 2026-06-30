package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.ReportedUserAdapter;
import com.example.movie_app.adapter.ViolationCommentAdapter;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.ModerationViewModel;

public class ModerationManagementActivity extends BaseAdminActivity {

    private TextView tvTotalUsersCount, tvViolationBadge;
    private TextView tvTrustScoreValue, tvBannedTodayValue;
    private RecyclerView rvReportedUsers, rvViolationComments;

    private ReportedUserAdapter userAdapter;
    private ViolationCommentAdapter commentAdapter;
    private ModerationViewModel moderationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_moderation);

        initViews();
        setupAdminInfo();
        setupRecyclerViews();
        initViewModel();

        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvTotalUsersCount = findViewById(R.id.tv_total_users_count);
        tvViolationBadge = findViewById(R.id.tv_violation_badge);
        tvTrustScoreValue = findViewById(R.id.tv_trust_score_value);
        tvBannedTodayValue = findViewById(R.id.tv_banned_today_value);

        rvReportedUsers = findViewById(R.id.rv_reported_users);
        rvViolationComments = findViewById(R.id.rv_violation_comments);
    }

    private void setupRecyclerViews() {
        rvReportedUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new ReportedUserAdapter(user -> {
            moderationViewModel.changeUserLockStatus(user);
        });
        rvReportedUsers.setAdapter(userAdapter);

        rvViolationComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new ViolationCommentAdapter(new ViolationCommentAdapter.OnCommentActionListener() {
            @Override
            public void onShowContext(com.example.movie_app.models.ViolationCommentDto comment) {
                Toast.makeText(ModerationManagementActivity.this, "Phim: " + comment.getMovieTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIgnore(com.example.movie_app.models.ViolationCommentDto comment) {
                moderationViewModel.ignoreComment(comment.getId());
            }

            @Override
            public void onDelete(com.example.movie_app.models.ViolationCommentDto comment) {
                moderationViewModel.deleteComment(comment.getId());
            }
        });
        rvViolationComments.setAdapter(commentAdapter);
    }

    private void initViewModel() {
        moderationViewModel = new ViewModelProvider(this).get(ModerationViewModel.class);

        moderationViewModel.getStatsData().observe(this, stats -> {
            if (stats != null) {
                tvTotalUsersCount.setText(stats.getTotalUsers() + " Tổng số");
                tvViolationBadge.setText(stats.getNewViolationsCount() + " VI PHẠM");
                tvTrustScoreValue.setText(stats.getTrustScore() + "%");
                tvBannedTodayValue.setText(String.valueOf(stats.getBannedToday()));
            }
        });

        moderationViewModel.getReportedUsers().observe(this, users -> {
            if (users != null) userAdapter.submitList(users);
        });

        moderationViewModel.getViolationComments().observe(this, comments -> {
            if (comments != null) commentAdapter.submitList(comments);
        });

        moderationViewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
