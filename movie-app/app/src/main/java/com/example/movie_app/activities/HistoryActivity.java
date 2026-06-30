package com.example.movie_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter; // Giả sử bạn sử dụng MovieAdapter
import com.example.movie_app.database.AppDatabase;
import com.example.movie_app.entity.FavoriteEntity;
import com.example.movie_app.entity.WatchHistoryEntity;
import com.example.movie_app.models.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout tabHistory, tabFavorites;
    private RecyclerView rvMovieList;
    private ImageView btnBack;

    // Khai báo Adapter và Database
    // private MovieAdapter movieAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history); // Ánh xạ layout history.xml

        initViews();
        setupEvents();


        // Khởi tạo database
        db = AppDatabase.getDatabase(this);

        // Mặc định load tab Lịch sử
        loadHistoryData();
    }

    private void initViews() {
        tabHistory = findViewById(R.id.tabHistory);
        tabFavorites = findViewById(R.id.tabFavorites);
        rvMovieList = findViewById(R.id.rvMovieList);
        btnBack = findViewById(R.id.btnBack);

        rvMovieList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        tabHistory.setOnClickListener(v -> {
            // Đổi màu tab đang được chọn
            tabHistory.setBackgroundResource(R.drawable.bg_btn_red);
            tabFavorites.setBackgroundResource(R.drawable.bg_btn_dark_square);
            loadHistoryData();
        });

        tabFavorites.setOnClickListener(v -> {
            // Đổi màu tab đang được chọn
            tabFavorites.setBackgroundResource(R.drawable.bg_btn_red);
            tabHistory.setBackgroundResource(R.drawable.bg_btn_dark_square);
            loadFavoritesData();
        });
    }

    private void loadHistoryData() {
        // Dùng Thread để truy vấn DB vì HistoryDao trả về List thông thường
        new Thread(() -> {
            // Lưu ý: Đang dùng tạm "USER_ID_TEST" giống như trong MovieDetailActivity
            List<WatchHistoryEntity> historyList = db.historyDao().getHistoryByUserId("USER_ID_TEST");

            runOnUiThread(() -> {
                List<MovieItem> movieList = new ArrayList<>();
                if (historyList != null) {
                    for (WatchHistoryEntity history : historyList) {
                        MovieItem item = new MovieItem();
                        item.setId(history.movieId);
                        item.setName(history.movieName); // Đảm bảo gọi đúng hàm setter của model MovieItem bạn đang dùng
                        item.setPosterUrl(history.posterUrl);
                        // Có thể set thêm thuộc tính slug tại đây nếu MovieAdapter cần dùng để click mở phim
                        movieList.add(item);
                    }
                }

                // Khởi tạo adapter và gán cho RecyclerView
                MovieAdapter movieAdapter = new MovieAdapter(movieList);
                rvMovieList.setAdapter(movieAdapter);
            });
        }).start();
    }

    private void loadFavoritesData() {
        // favoriteDao trả về LiveData nên chúng ta cần observe trực tiếp trên Main Thread
        // Xóa bỏ các observe cũ trước khi gọi mới để tránh việc trùng lặp dữ liệu khi bấm qua lại giữa 2 tab
        db.favoriteDao().getAllFavorites().removeObservers(this);

        db.favoriteDao().getAllFavorites().observe(this, favoriteEntities -> {
            List<MovieItem> movieList = new ArrayList<>();
            if (favoriteEntities != null) {
                for (FavoriteEntity fav : favoriteEntities) {
                    MovieItem item = new MovieItem();
                    item.setId(fav.movieId);
                    item.setName(fav.movieName);
                    item.setPosterUrl(fav.posterUrl);
                    movieList.add(item);
                }
            }

            // Khởi tạo adapter và gán cho RecyclerView
            MovieAdapter movieAdapter = new MovieAdapter(movieList);
            rvMovieList.setAdapter(movieAdapter);
        });
    }
}