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
    private MovieDetailResponse.MovieDetail currentMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        initViews();
        setupTabClickListeners();
        setupWatchNowButton();

        String imageUrlFromIntent = getIntent().getStringExtra("movie_image");
        Log.d("DEBUG_INTENT", "Nhận được từ Intent: " + imageUrlFromIntent);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Log.d("DEBUG_INTENT", "Key tồn tại: " + key);
            }
        }
        btnDetailBack.setOnClickListener(v -> finish());

        String movieSlug = getIntent().getStringExtra("movie_slug");
        Log.d("DEBUG_SLUG", "Slug: " + movieSlug);
        if (movieSlug != null) {
            movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
            movieViewModel.getMovieDetail(movieSlug).observe(this, response -> {
                if (response != null && response.getMovie() != null) {
                    currentMovieDetail = response.getMovie();
                    bindMovieData(response.getMovie(), imageUrlFromIntent);
                } else {
                    Log.e("DEBUG_API", "Response hoặc Movie bị null");
                    Toast.makeText(this, "Không tìm thấy thông tin phim!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViews() {
        imgDetailPoster = findViewById(R.id.imgDetailPoster);
        btnDetailBack = findViewById(R.id.btnDetailBack);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubInfo = findViewById(R.id.tvDetailSubInfo);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDirector = findViewById(R.id.tvDetailDirector);
        tvDetailActor = findViewById(R.id.tvDetailActor);
        tvDetailCountry = findViewById(R.id.tvDetailCountry);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);

        btnWatchNow = findViewById(R.id.btnWatchNow);

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

    private void setupWatchNowButton() {
        btnWatchNow.setOnClickListener(v -> {
            if (currentMovieDetail == null) {
                Toast.makeText(MovieDetailActivity.this, "Thông tin phim chưa tải xong!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("WATCH_NOW", "Click Xem Ngay - Movie: " + currentMovieDetail.getName());

            // ✅ Gọi API để lấy episodes
            if (movieViewModel == null) {
                movieViewModel = new ViewModelProvider(MovieDetailActivity.this).get(MovieViewModel.class);
            }

            movieViewModel.getMovieDetail(currentMovieDetail.getSlug())
                    .observe(MovieDetailActivity.this, response -> {

                        Log.d("DEBUG_VIDEO", "Response = " + response);

                        if(response == null){
                            Log.e("DEBUG_VIDEO","response NULL");
                            return;
                        }

                        if(response.getEpisodes() == null){
                            Log.e("DEBUG_VIDEO","episodes NULL");
                            return;
                        }

                        Log.d(
                                "DEBUG_VIDEO",
                                "episodes size = " + response.getEpisodes().size()
                        );

                        if(response.getEpisodes().isEmpty()){
                            Log.e("DEBUG_VIDEO","episodes EMPTY");
                            return;
                        }

                        MovieDetailResponse.EpisodeServer firstServer =
                                response.getEpisodes().get(0);

                        if(firstServer == null){
                            Toast.makeText(
                                    MovieDetailActivity.this,
                                    "Không tìm thấy server phim",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        if(firstServer.getServerData() == null){
                            Toast.makeText(
                                    MovieDetailActivity.this,
                                    "Không có dữ liệu tập phim",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Log.e("DEBUG_VIDEO",
                                    "serverData NULL");

                            return;
                        }

                        if(firstServer.getServerData().isEmpty()){
                            Toast.makeText(
                                    MovieDetailActivity.this,
                                    "Danh sách tập rỗng",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        MovieDetailResponse.EpisodeData firstEpisode =
                                firstServer.getServerData().get(0);

                        String videoUrl = firstEpisode.getLinkM3u8();

                        Log.d("DEBUG_VIDEO", "videoUrl = " + videoUrl);

                        if(videoUrl == null || videoUrl.isEmpty()){
                            Toast.makeText(
                                    MovieDetailActivity.this,
                                    "Không tìm thấy video phim!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        Movie playMovie = new Movie();

                        playMovie.setMovieId(currentMovieDetail.getSlug());
                        playMovie.setTitle(currentMovieDetail.getName());
                        playMovie.setDescription(currentMovieDetail.getContent());

                        ArrayList<String> episodeUrls = new ArrayList<>();
                        ArrayList<String> episodeNames = new ArrayList<>();

                        for (MovieDetailResponse.EpisodeData ep :
                                firstServer.getServerData()) {

                            episodeUrls.add(ep.getLinkM3u8());
                            episodeNames.add(ep.getName());
                        }

                        playMovie.setEpisodeUrls(episodeUrls);
                        playMovie.setEpisodeNames(episodeNames);

                        playMovie.setVideoUrl(episodeUrls.get(0));

                        playMovie.setEpisodes(episodeUrls.size());

                        playMovie.setCurrentEpisode(1);

                        Intent intent =
                                new Intent(
                                        MovieDetailActivity.this,
                                        VideoPlayerActivity.class
                                );

                        intent.putExtra("movie", playMovie);

                        startActivity(intent);
                    });
        });
    }

    private void bindMovieData(MovieDetailResponse.MovieDetail info, String imageUrlFromIntent) {
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
}