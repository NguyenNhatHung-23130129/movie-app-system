package com.example.movie_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Category;
import com.example.movie_app.models.Movie;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private ImageView imgDetailPoster, btnDetailBack;
    private TextView tvDetailTitle, tvDetailSubInfo, tvDetailDescription, tvDetailTime;
    private TextView tvDetailDirector, tvDetailActor, tvDetailCountry, tvDetailStatus;

    private LinearLayout tabDescription, tabComments, tabRelated;
    private TextView tvTabDescription, tvTabComments, tvTabRelated;
    private View viewDescLine, viewCommentLine, viewRelatedLine;
    private LinearLayout layoutTabDescriptionContent, layoutTabCommentsContent, layoutTabRelatedContent;

    private RecyclerView rvRelatedMovies;
    private LinearLayout btnWatchNow;
    private boolean isNested = false;
    private int activeTabId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        activeTabId = getIntent().getIntExtra("ACTIVE_TAB_ID", -1);
        isNested = getIntent().getBooleanExtra("IS_NESTED", false);
        initViews();
        setupTabClickListeners();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
        btnDetailBack.setOnClickListener(v -> goToHome());

        String imageUrlFromIntent = getIntent().getStringExtra("movie_image");
        String movieSlug = getIntent().getStringExtra("movie_slug");
        if (movieSlug != null) {
            movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
            movieViewModel.getMovieDetail(movieSlug).observe(this, response -> {
                if (response != null && response.getMovie() != null) {
                    bindMovieData(response, imageUrlFromIntent);
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin phim!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViews() {
        imgDetailPoster = findViewById(R.id.imgDetailPoster);
        btnDetailBack = findViewById(R.id.btnDetailBack);
        btnWatchNow = findViewById(R.id.btnWatchNow);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubInfo = findViewById(R.id.tvDetailSubInfo);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDirector = findViewById(R.id.tvDetailDirector);
        tvDetailActor = findViewById(R.id.tvDetailActor);
        tvDetailCountry = findViewById(R.id.tvDetailCountry);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);

        tabDescription = findViewById(R.id.tabDescription);
        tabComments = findViewById(R.id.tabComments);
        tabRelated = findViewById(R.id.tabRelated);
        tvTabDescription = findViewById(R.id.tvTabDescription);
        tvTabComments = findViewById(R.id.tvTabComments);
        tvTabRelated = findViewById(R.id.tvTabRelated);
        viewDescLine = findViewById(R.id.viewDescLine);
        viewCommentLine = findViewById(R.id.viewCommentLine);
        viewRelatedLine = findViewById(R.id.viewRelatedLine);
        layoutTabDescriptionContent = findViewById(R.id.layoutTabDescriptionContent);
        layoutTabCommentsContent = findViewById(R.id.layoutTabCommentsContent);
        layoutTabRelatedContent = findViewById(R.id.layoutTabRelatedContent);

        rvRelatedMovies = findViewById(R.id.rvRelatedMovies);
        rvRelatedMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void bindMovieData(MovieDetailResponse response, String imageUrlFromIntent) {
        MovieDetailResponse.MovieDetail info = response.getMovie();
        tvDetailTitle.setText(info.getName());
        tvDetailDescription.setText(info.getContent() != null ? info.getContent() : "Đang cập nhật mô tả...");
        if (info.getCategory() != null && !info.getCategory().isEmpty()) {
            StringBuilder genres = new StringBuilder();
            for (int i = 0; i < info.getCategory().size(); i++) {
                genres.append(info.getCategory().get(i).getName());
                if (i < info.getCategory().size() - 1) genres.append(", ");
            }
            tvDetailSubInfo.setText(info.getYear() + " • " + genres.toString());
        }

        tvDetailTime.setText((info.getTime() != null ? info.getTime() : "N/A") + " • " + (info.getQuality() != null ? info.getQuality() : "HD"));

        boolean isCompleted = "completed".equalsIgnoreCase(info.getStatus());
        tvDetailStatus.setText(isCompleted ? "Trọn bộ" : "Đang chiếu");
        tvDetailStatus.setTextColor(ContextCompat.getColor(this, isCompleted ? android.R.color.holo_green_light : android.R.color.holo_orange_light));

        tvDetailDirector.setText(listToString(info.getDirector()));
        tvDetailActor.setText(listToString(info.getActor()));

        if (info.getCountry() != null && !info.getCountry().isEmpty()) {
            tvDetailCountry.setText(info.getCountry().get(0).getName());
        }

        String finalImageUrl;
        if (imageUrlFromIntent != null && !imageUrlFromIntent.isEmpty()) {
            finalImageUrl = imageUrlFromIntent;
        } else {
            String url = info.getPosterUrl();
            finalImageUrl = (url != null && url.startsWith("http")) ? url : "https://phimimg.com/" + url;
        }

        Glide.with(this)
                .load(finalImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgDetailPoster);

        btnWatchNow.setOnClickListener(v -> {
            movieViewModel.saveToHistory(
                    info.getSlug(),
                    "USER_ID_TEST",
                    info.getName(),
                    finalImageUrl
            );

            Movie movie = new Movie();
            movie.setMovieId(info.getId());
            movie.setTitle(info.getName());
            movie.setDescription(info.getContent());
            movie.setPosterUrl(finalImageUrl);

            ArrayList<String> urls = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();

            if (response.getEpisodes() != null && !response.getEpisodes().isEmpty()) {
                MovieDetailResponse.EpisodeServer server = response.getEpisodes().get(0);
                if (server.getServerData() != null) {
                    for (MovieDetailResponse.EpisodeData ep : server.getServerData()) {
                        String link = ep.getLinkM3u8();
                        // FIX 3: Chỉ thêm URL hợp lệ, bỏ qua URL null/rỗng
                        if (link != null && !link.trim().isEmpty()) {
                            urls.add(link);
                            names.add(ep.getName() != null ? ep.getName() : "Tập " + (urls.size()));
                            Log.d("EP", "Episode = " + ep.getName() + " | URL = " + link);
                        } else {
                            Log.w("EP", "Bỏ qua tập " + ep.getName() + " do URL rỗng/null");
                        }
                    }
                    Log.d("EP", "Total valid episodes = " + urls.size());
                }
            }

            // FIX 3: Kiểm tra có episode hợp lệ không trước khi mở VideoPlayer
            if (urls.isEmpty()) {
                Toast.makeText(this, "Phim này chưa có tập nào để phát!", Toast.LENGTH_SHORT).show();
                return;
            }

            movie.setEpisodeUrls(urls);
            movie.setEpisodeNames(names);
            movie.setEpisodes(urls.size());
            movie.setVideoUrl(urls.get(0));

            Intent intent = new Intent(MovieDetailActivity.this, VideoPlayerActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        });

        loadRelatedMovies(info);
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return "Đang cập nhật";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private void setupTabClickListeners() {
        tabDescription.setOnClickListener(v -> selectTab(1));
        tabComments.setOnClickListener(v -> selectTab(2));
        tabRelated.setOnClickListener(v -> selectTab(3));
    }

    private void selectTab(int tabIndex) {
        int gray = Color.parseColor("#8E8E93");
        int white = Color.parseColor("#FFFFFF");

        tvTabDescription.setTextColor(gray); tvTabComments.setTextColor(gray); tvTabRelated.setTextColor(gray);
        viewDescLine.setVisibility(View.INVISIBLE); viewCommentLine.setVisibility(View.INVISIBLE); viewRelatedLine.setVisibility(View.INVISIBLE);
        layoutTabDescriptionContent.setVisibility(View.GONE); layoutTabCommentsContent.setVisibility(View.GONE); layoutTabRelatedContent.setVisibility(View.GONE);

        if (tabIndex == 1) {
            tvTabDescription.setTextColor(white); viewDescLine.setVisibility(View.VISIBLE); layoutTabDescriptionContent.setVisibility(View.VISIBLE);
        } else if (tabIndex == 2) {
            tvTabComments.setTextColor(white); viewCommentLine.setVisibility(View.VISIBLE); layoutTabCommentsContent.setVisibility(View.VISIBLE);
        } else {
            tvTabRelated.setTextColor(white); viewRelatedLine.setVisibility(View.VISIBLE); layoutTabRelatedContent.setVisibility(View.VISIBLE);
        }
    }

    private void loadRelatedMovies(MovieDetailResponse.MovieDetail info) {
        movieViewModel.getRelatedMovies(info.getSlug(), info.getCategory())
                .observe(this, relatedList -> {
                    if (relatedList != null) {
                        relatedList.removeIf(m -> m.getSlug().equals(info.getSlug()));
                        MovieAdapter relatedAdapter = new MovieAdapter(relatedList);
                        rvRelatedMovies.setAdapter(relatedAdapter);
                    }
                });
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        if (activeTabId != -1) {
            intent.putExtra("TARGET_TAB_ID", activeTabId);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}