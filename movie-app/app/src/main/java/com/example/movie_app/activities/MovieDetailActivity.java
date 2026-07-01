package com.example.movie_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.adapter.CommentAdapter;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Category;
import com.example.movie_app.models.Comment;
import com.example.movie_app.models.Movie;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.viewmodel.MovieViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RatingBar ratingBarInput;
    private EditText edtCommentInput;
    private ImageView btnSendComment;
    private DatabaseReference databaseReference;
    private String currentMovieId = "";

    private FrameLayout btnAddToMyList;
    private ImageView imgAddToMyList;

    private boolean isFavorite = false;
    private String currentUserId = "USER_ID_TEST";
    private String currentUserName = "Người dùng ẩn danh";
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("USER_NAME", "Người dùng ẩn danh");
        currentUserId = prefs.getString("USER_ID", "USER_ID_TEST");
        initFirebaseConfig();
        activeTabId = getIntent().getIntExtra("ACTIVE_TAB_ID", -1);
        isNested = getIntent().getBooleanExtra("IS_NESTED", false);
        initViews();
        setupTabClickListeners();
        setupCommentAction();
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

    private void initFirebaseConfig() {
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Log.e("FIREBASE_INIT", "Lỗi: " + e.getMessage());
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
        btnAddToMyList = findViewById(R.id.btnAddToMyList);
        imgAddToMyList = findViewById(R.id.imgAddToMyList);

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
        ratingBarInput = findViewById(R.id.ratingBarInput);
        edtCommentInput = findViewById(R.id.edtCommentInput);
        btnSendComment = findViewById(R.id.btnSendComment);
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");

        rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setNestedScrollingEnabled(false);
        commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);
        rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }
        });
    }


    private void bindMovieData(MovieDetailResponse response, String imageUrlFromIntent) {
        MovieDetailResponse.MovieDetail info = response.getMovie();
        currentMovieId = info.getId();
        loadComments();
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
                    currentUserId,
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
        checkFavoriteState(info.getSlug());
        btnAddToMyList.setOnClickListener(v -> toggleFavorite(info, finalImageUrl));
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

    private void setupCommentAction() {
        // Lắng nghe sự kiện khi người dùng bấm nút trên bàn phím ảo
        edtCommentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                // Ẩn bàn phím
                hideKeyboard();

                // Bỏ comment dòng dưới đây nếu bạn muốn khi bấm nút trên bàn phím
                // thì app tự động thực hiện lệnh Gửi bình luận luôn:
                // btnSendComment.performClick();

                return true;
            }
        });
        btnSendComment.setOnClickListener(v -> {
            String text = edtCommentInput.getText().toString().trim();
            float stars = ratingBarInput.getRating();

            if (text.isEmpty()) {
                Toast.makeText(this, "Chưa nhập nội dung!", Toast.LENGTH_SHORT).show();
                return;
            }

            String dbUrl = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";
            DatabaseReference newCommentRef = FirebaseDatabase.getInstance(dbUrl)
                    .getReference("comments")
                    .child(currentMovieId)
                    .push();

            Comment newComment = new Comment(
                    newCommentRef.getKey(),
                    currentUserName,
                    text,
                    0,
                    (double) stars
            );

            newCommentRef.setValue(newComment).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    newCommentRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    edtCommentInput.setText("");
                    ratingBarInput.setRating(0);
                    hideKeyboard();

                }
            });
        });
    }

    private void loadComments() {

        if (currentMovieId == null || currentMovieId.isEmpty()) {
            return;
        }
        String dbUrl = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference commentsRef = FirebaseDatabase.getInstance(dbUrl)
                .getReference("comments")
                .child(currentMovieId);

        commentsRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                List<Comment> newList = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    if (comment != null) {
                        newList.add(comment);
                    }
                }

                newList.sort((c1, c2) -> Long.compare(c2.getTimestamp(), c1.getTimestamp()));

                if (commentAdapter != null) {
                    commentAdapter.updateList(newList);
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
            }
        });
    }

    private void checkFavoriteState(String slug) {
        if (slug == null || slug.isEmpty()) return;

        movieViewModel.isFavorite(currentUserId, slug).observe(this, isFav -> {
            this.isFavorite = (isFav != null && isFav);
            updateFavoriteUI();
        });
    }

    private void toggleFavorite(MovieDetailResponse.MovieDetail info, String imageUrl) {
        if (info == null || info.getSlug() == null) return;

        String originalId = info.getId();
        info.setId(info.getSlug());

        if (isFavorite) {
            movieViewModel.removeFromFavorites(currentUserId, info.getSlug());
            Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            movieViewModel.addToFavorites(currentUserId, info, imageUrl);
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        }

        info.setId(originalId);
    }
    private void updateFavoriteUI() {
        if (isFavorite) {
            imgAddToMyList.setImageResource(R.drawable.ic_done);
            imgAddToMyList.setColorFilter(Color.parseColor("#4CAF50"));
        } else {
            imgAddToMyList.setImageResource(R.drawable.ic_add);
            imgAddToMyList.setColorFilter(Color.parseColor("#E2E2E8"));
        }
    }
    private void loadComments(String movieId) {
        // Lọc trong nhánh "reviews", chỉ lấy những bình luận thuộc về movieId hiện tại
        databaseReference.orderByChild("movieId").equalTo(movieId)
                .addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        commentList.clear(); // Xóa list cũ trước khi nạp list mới

                        for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren()) {
                            // Đọc dữ liệu thô từ Firebase một cách an toàn
                            String user = data.child("username").getValue(String.class);
                            String content = data.child("content").getValue(String.class);
                            Float rating = data.child("rating").getValue(Float.class);

                            // Ép vào model Comment để đưa lên giao diện
                            com.example.movie_app.models.Comment comment = new com.example.movie_app.models.Comment(
                                    data.getKey(),
                                    user != null ? user : "Ẩn danh",
                                    content != null ? content : "",
                                    0, // Truyền một giá trị mặc định cho timestamp
                                    rating != null ? (double) rating : 5.0
                            );
                            commentList.add(comment);
                        }
                        // Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại màn hình
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(MovieDetailActivity.this, "Lỗi tải bình luận!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Bỏ focus khỏi ô nhập liệu
        if (edtCommentInput != null) {
            edtCommentInput.clearFocus();
        }
    }
}