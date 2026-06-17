package com.example.movie_app;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MovieManagementActivity extends AppCompatActivity {

    private TextView btnFilterAll;
    private TextView btnFilterPublished;
    private TextView btnFilterDraft;
    private ImageButton fabRating;
    private EditText edtSearchMovie;
    private RecyclerView rcvMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_movie);
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterPublished = findViewById(R.id.btn_filter_published);
        btnFilterDraft = findViewById(R.id.btn_filter_draft);
        fabRating = findViewById(R.id.fab_rating);

        edtSearchMovie = findViewById(R.id.edt_search_movie);
        rcvMovieList = findViewById(R.id.rcv_movie_list);
    }
    private void setupClickListeners() {
        btnFilterAll.setOnClickListener(v-> {
                // TODO: Thực hiện logic lọc tất cả phim tại đây
        });

        btnFilterPublished.setOnClickListener(v-> {
                // TODO: Thực hiện logic lọc phim đã xuất bản tại đây
        });

        btnFilterDraft.setOnClickListener(v-> {

                // TODO: Thực hiện logic lọc phim nháp tại đây

        });

        fabRating.setOnClickListener(v-> {
                // TODO: Mở màn hình hoặc hộp thoại đánh giá phim tại đây
        });
    }
}