package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.adapter.AdminCommentAdapter;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.CommentManagementViewModel;

public class CommentManagementActivity extends BaseAdminActivity {

    private TextView tvTotalCommentsCount;
    private RecyclerView rvAdminComments;
    private AdminCommentAdapter commentAdapter;
    private CommentManagementViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comment_management);

        initViews();
        setupAdminInfo();
        setupRecyclerView();
        initViewModel();

        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        tvTotalCommentsCount = findViewById(R.id.tv_total_comments_count);
        rvAdminComments = findViewById(R.id.rv_admin_comments);
    }

    private void setupRecyclerView() {
        rvAdminComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new AdminCommentAdapter(comment -> viewModel.deleteComment(comment));
        rvAdminComments.setAdapter(commentAdapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CommentManagementViewModel.class);

        viewModel.getCommentsLiveData().observe(this, comments -> {
            if (comments != null) {
                commentAdapter.submitList(comments);
                tvTotalCommentsCount.setText(comments.size() + " Bình luận");
            }
        });

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}