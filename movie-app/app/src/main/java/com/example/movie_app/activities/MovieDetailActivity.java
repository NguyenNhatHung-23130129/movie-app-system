package com.example.movie_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.viewmodel.MovieViewModel;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;

    // View gốc của màn hình
    private ImageView imgDetailPoster, btnDetailBack, btnDetailShare, btnDetailHeart;
    private TextView tvDetailTitle, tvDetailSubInfo, tvDetailDescription, tvDetailTime;
    private TextView tvDetailDirector, tvDetailActor, tvDetailCountry, tvDetailStatus;
    private LinearLayout btnWatchNow;
    private View btnAddToMyList;

    // 🛑 THÊM CÁC BIẾN CHO PHẦN TABS
    private LinearLayout tabDescription, tabComments, tabRelated;
    private TextView tvTabDescription, tvTabComments, tvTabRelated;
    private View viewDescLine, viewCommentLine, viewRelatedLine;
    private LinearLayout layoutTabDescriptionContent, layoutTabCommentsContent, layoutTabRelatedContent;

    private RecyclerView rvRelatedMovies;
    private String currentCategorySlug = ""; // Lưu lại danh mục phim đang xem để chuyển sang màn hình Tìm kiếm/Khám phá

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        initViews();
        setupTabClickListeners(); // Kích hoạt bộ lắng nghe đổi Tab

        btnDetailBack.setOnClickListener(v -> finish());

        String movieSlug = getIntent().getStringExtra("movie_slug");

        if (movieSlug != null) {
            movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
            movieViewModel.getMovieDetail(movieSlug).observe(this, response -> {
                if (response != null && response.getMovie() != null) {
                    bindMovieData(response.getMovie());
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin bộ phim!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Đường dẫn phim không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        // Ánh xạ cũ
        imgDetailPoster = findViewById(R.id.imgDetailPoster);
        btnDetailBack = findViewById(R.id.btnDetailBack);
        btnDetailShare = findViewById(R.id.btnDetailShare);
        btnDetailHeart = findViewById(R.id.btnDetailHeart);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubInfo = findViewById(R.id.tvDetailSubInfo);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDirector = findViewById(R.id.tvDetailDirector);
        tvDetailActor = findViewById(R.id.tvDetailActor);
        tvDetailCountry = findViewById(R.id.tvDetailCountry);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        btnWatchNow = findViewById(R.id.btnWatchNow);
        btnAddToMyList = findViewById(R.id.btnAddToMyList);

        // 🛑 ÁNH XẠ CÁC BIẾN TABS MỚI THÊM
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

    // 🛑 THIẾT LẬP LOGIC CHUYỂN ĐỔI ẨN HIỆN TAB
    private void setupTabClickListeners() {
        tabDescription.setOnClickListener(v -> selectTab(1));
        tabComments.setOnClickListener(v -> selectTab(2));
        tabRelated.setOnClickListener(v -> selectTab(3));
    }

    private void selectTab(int tabIndex) {
        // Reset trạng thái chữ xám và ẩn thanh gạch chân đỏ
        tvTabDescription.setTextColor(Color.parseColor("#8E8E93"));
        tvTabComments.setTextColor(Color.parseColor("#8E8E93"));
        tvTabRelated.setTextColor(Color.parseColor("#8E8E93"));

        viewDescLine.setVisibility(View.INVISIBLE);
        viewCommentLine.setVisibility(View.INVISIBLE);
        viewRelatedLine.setVisibility(View.INVISIBLE);

        layoutTabDescriptionContent.setVisibility(View.GONE);
        layoutTabCommentsContent.setVisibility(View.GONE);
        layoutTabRelatedContent.setVisibility(View.GONE);

        // Kích hoạt tab được lựa chọn
        switch (tabIndex) {
            case 1:
                tvTabDescription.setTextColor(Color.parseColor("#FFFFFF"));
                viewDescLine.setVisibility(View.VISIBLE);
                layoutTabDescriptionContent.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvTabComments.setTextColor(Color.parseColor("#FFFFFF"));
                viewCommentLine.setVisibility(View.VISIBLE);
                layoutTabCommentsContent.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvTabRelated.setTextColor(Color.parseColor("#FFFFFF"));
                viewRelatedLine.setVisibility(View.VISIBLE);
                layoutTabRelatedContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void bindMovieData(MovieDetailResponse.MovieInfo info) {
        tvDetailTitle.setText(info.getName());
        tvDetailDescription.setText(info.getContent());

        // Lấy danh mục thể loại đầu tiên để phục vụ mục "Xem thêm" sau này
        if (info.getCategory() != null && !info.getCategory().isEmpty()) {
            currentCategorySlug = info.getCategory().get(0).getSlug();
        }

        StringBuilder genresBuilder = new StringBuilder();
        if (info.getCategory() != null && !info.getCategory().isEmpty()) {
            for (int i = 0; i < info.getCategory().size(); i++) {
                genresBuilder.append(info.getCategory().get(i).getName());
                if (i < info.getCategory().size() - 1) genresBuilder.append(", ");
            }
        }
        String genresStr = genresBuilder.toString();
        tvDetailSubInfo.setText(info.getYear() + "  •  " + (genresStr.isEmpty() ? "Đang cập nhật" : genresStr));

        String movieTime = info.getTime();
        if (movieTime != null && !movieTime.isEmpty()) {
            tvDetailTime.setText(movieTime + "  •  4K Ultra HD");
        } else {
            tvDetailTime.setText("N/A  •  4K Ultra HD");
        }

        // --- Các dòng hiển thị Đạo diễn, diễn viên, quốc gia giữ nguyên ---
        if (info.getDirector() != null && !info.getDirector().isEmpty() && info.getDirector().get(0) != null) {
            tvDetailDirector.setText(String.valueOf(info.getDirector().get(0)));
        } else {
            tvDetailDirector.setText("Đang cập nhật");
        }
        if (info.getActor() != null && !info.getActor().isEmpty() && info.getActor().get(0) != null) {
            tvDetailActor.setText(String.valueOf(info.getActor().get(0)));
        } else {
            tvDetailActor.setText("Vô danh");
        }
        if (info.getCountry() != null && !info.getCountry().isEmpty()) {
            tvDetailCountry.setText(info.getCountry().get(0).getName());
        } else {
            tvDetailCountry.setText("Không rõ");
        }
        if ("completed".equalsIgnoreCase(info.getMovieStatus())) {
            tvDetailStatus.setText("Trọn bộ");
            tvDetailStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            tvDetailStatus.setText("Đang chiếu");
            tvDetailStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        }

        String fullImageUrl = info.getPosterUrl();
        if (fullImageUrl != null && !fullImageUrl.startsWith("http")) {
            fullImageUrl = "https://phimimg.com/" + fullImageUrl;
        }
        Glide.with(this).load(fullImageUrl)
                .placeholder(android.R.drawable.progress_horizontal)
                .error(android.R.drawable.ic_menu_gallery).into(imgDetailPoster);

        // NẠP DỮ LIỆU PHIM LIÊN QUAN (Sử dụng tạm danh sách giả lập)
        setupRelatedMoviesRecyclerView();
    }

    private void setupRelatedMoviesRecyclerView() {
        // Thêm danh sách phim demo gồm 5 phim thật, phần tử thứ 6 đóng vai trò là nút "Xem thêm"
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("Phim 1");
        mockList.add("Phim 2");
        mockList.add("Phim 3");
        mockList.add("Phim 4");
        mockList.add("Phim 5");
        mockList.add("SEE_MORE_NODE"); // Node đặc biệt để adapter vẽ chữ "Xem thêm" ở cuối dòng

        // Ở đây, bạn gán Adapter phim liên quan của bạn vào (Ví dụ: RelatedMovieAdapter)
        // Trong Adapter đó, khi phát hiện item cuối là "SEE_MORE_NODE" và người dùng click vào,
        // bạn thực hiện gọi ý định quay về màn hình Khám Phá:
        /*
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("action_tab", "explore");
        intent.putExtra("filter_category", currentCategorySlug);
        startActivity(intent);
        finish(); // Tắt chi tiết phim đi để lộ trang Home kèm bộ lọc
        */
    }
}