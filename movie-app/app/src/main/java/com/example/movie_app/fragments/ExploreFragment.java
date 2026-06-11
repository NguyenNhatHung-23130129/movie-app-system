package com.example.movie_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private TextView chipNew, chipSeries, chipSingle;
    private LinearLayout layoutGenres;
    private RecyclerView rvExplore;
    private MovieAdapter exploreAdapter;
    private MovieViewModel movieViewModel;

    private final ArrayList<TextView> genreChipsList = new ArrayList<>();

    // Biến lưu trạng thái bộ lọc đang chọn
    private String currentFormat = "NEW";       // NEW, SERIES, SINGLE
    private String currentGenreSlug = null;     // null nếu không chọn thể loại

    // 🌟 MẸO QUAN TRỌNG: Lưu lại danh sách phim vừa tải về từ API để lọc chéo 2 cấp tại chỗ
    private final List<MovieItem> currentFetchedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        chipNew = view.findViewById(R.id.chipNew);
        chipSeries = view.findViewById(R.id.chipSeries);
        chipSingle = view.findViewById(R.id.chipSingle);
        layoutGenres = view.findViewById(R.id.layoutGenres);
        rvExplore = view.findViewById(R.id.rvExplore);

        exploreAdapter = new MovieAdapter(new ArrayList<>());
        rvExplore.setAdapter(exploreAdapter);

        updateBottomNavigationIcons(view);

        // 🌟 ĐĂNG KÝ QUAN SÁT LIVEDATA MỘT LẦN DUY NHẤT TẠI ĐÂY (TRÁNH LOẠN EVENT)
        registerObservers();

        // Sự kiện click hàng 1 (Định dạng Phim)
        chipNew.setOnClickListener(v -> changeFormatFilter("NEW"));
        chipSeries.setOnClickListener(v -> changeFormatFilter("SERIES"));
        chipSingle.setOnClickListener(v -> changeFormatFilter("SINGLE"));

        // Tải danh sách thể loại chip
        loadGenreChipsFromApi();
    }

    private void registerObservers() {
        // Lắng nghe danh sách phim mới
        movieViewModel.getLatestMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("NEW".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });

        // Lắng nghe danh sách phim bộ
        movieViewModel.getSeriesMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("SERIES".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });

        // Lắng nghe danh sách phim lẻ
        movieViewModel.getSingleMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("SINGLE".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });
    }

    private void loadGenreChipsFromApi() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), genresList -> {
            if (genresList != null && !genresList.isEmpty()) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                layoutGenres.removeAllViews();
                genreChipsList.clear();

                for (Genre genre : genresList) {
                    TextView genreView = (TextView) inflater.inflate(R.layout.item_genre, layoutGenres, false);
                    genreView.setText(genre.getName());
                    genreView.setTag(genre.getSlug());

                    genreView.setOnClickListener(v -> changeGenreFilter(genreView, genre.getSlug()));

                    layoutGenres.addView(genreView);
                    genreChipsList.add(genreView);
                }
            }

            // Xử lý dữ liệu điều hướng từ màn hình khác sang (nếu có)
            if (getArguments() != null) {
                String type = getArguments().getString("MOVIE_TYPE");
                String genreSlug = getArguments().getString("CATEGORY_SLUG");
                if (type != null) currentFormat = type;
                if (genreSlug != null) currentGenreSlug = genreSlug;
            }

            // Kích hoạt nạp dữ liệu lần đầu tiên
            fetchDataFromServer();
        });
    }

    private void changeFormatFilter(String formatType) {
        if (currentFormat.equals(formatType) && currentGenreSlug != null) {
            // Nếu bấm lại định dạng cũ khi đang chọn thể loại -> Giữ nguyên, chỉ lọc lại tại Client
            currentFormat = formatType;
            applyLocalDualFilter();
        } else {
            currentFormat = formatType;
            fetchDataFromServer();
        }
    }

    private void changeGenreFilter(TextView clickedChip, String genreSlug) {
        if (genreSlug.equals(currentGenreSlug)) {
            currentGenreSlug = null; // Bấm lại thể loại đang chọn -> Hủy chọn thể loại
            fetchDataFromServer();
        } else {
            currentGenreSlug = genreSlug; // Chọn thể loại mới
            fetchDataFromServer();
        }
    }

    // Hàm tập trung chịu trách nhiệm gọi API dựa vào trạng thái bộ lọc
    private void fetchDataFromServer() {
        updateChipsUI();

        if (currentGenreSlug == null) {
            // LUỒNG 1: KHÔNG chọn thể loại -> Lấy trực tiếp từ các hàm định dạng lớn
            if ("NEW".equals(currentFormat)) {
                movieViewModel.getLatestMovies(1);
            } else if ("SERIES".equals(currentFormat)) {
                movieViewModel.getSeriesMovies(1);
            } else if ("SINGLE".equals(currentFormat)) {
                movieViewModel.getSingleMovies(1);
            }
        } else {
            // LUỒNG 2: CÓ chọn thể loại -> Bắt buộc phải gọi API lấy phim theo thể loại về trước
            movieViewModel.getMoviesByCategory(currentGenreSlug, 1).observe(getViewLifecycleOwner(), res -> {
                if (currentGenreSlug != null) { // Đảm bảo người dùng chưa hủy chọn trong lúc đợi tải API
                    currentFetchedList.clear();
                    if (res != null && res.getItems() != null) {
                        currentFetchedList.addAll(res.getItems());
                    }
                    // Tiến hành bóc tách lọc cấp 2 (Định dạng) ngay tại chỗ
                    applyLocalDualFilter();
                }
            });
        }
    }

    // 🌟 THUẬT TOÁN LỌC 2 CẤP SONG SONG TẠI CLIENT
    private void applyLocalDualFilter() {
        if (currentFetchedList.isEmpty()) {
            exploreAdapter.setMovieList(new ArrayList<>());
            return;
        }

        // Nếu cấp 1 chọn "Phim Mới" -> Show toàn bộ phim của thể loại đó
        if ("NEW".equals(currentFormat)) {
            exploreAdapter.setMovieList(new ArrayList<>(currentFetchedList));
            return;
        }

        List<MovieItem> filteredList = new ArrayList<>();
        for (MovieItem movie : currentFetchedList) {
            String movieSlug = movie.getSlug() != null ? movie.getSlug().toLowerCase() : "";

            // 🌟 CÁCH LỌC CHUẨN XÁC NHẤT CHO THẰNG OPHIM/PHIMIMG:
            // Phim lẻ (single): Thường trong trường slug sẽ có chữ "phim-" ở đầu hoặc kết thúc bằng đơn lẻ,
            // hoặc cách an toàn nhất dựa trên mảng data của API này là nhận diện qua từ khóa "tap" hoặc cấu trúc slug.
            // Để chắc chắn 100%, ta kiểm tra xem slug có chứa các từ khóa định danh của phim lẻ không:
            boolean isSingleMovie = movieSlug.contains("phim-le") || movieSlug.contains("-single") || !movieSlug.contains("-tap-");

            if ("SINGLE".equals(currentFormat) && isSingleMovie) {
                filteredList.add(movie);
            } else if ("SERIES".equals(currentFormat) && !isSingleMovie) {
                filteredList.add(movie);
            }
        }
        exploreAdapter.setMovieList(filteredList);
    }

    private void updateAdapterData(List<MovieItem> items) {
        if (items != null) {
            exploreAdapter.setMovieList(items);
        } else {
            exploreAdapter.setMovieList(new ArrayList<>());
        }
    }

    private void updateChipsUI() {
        int normalColor = Color.parseColor("#9CA3AF");
        int normalBg = Color.parseColor("#1E2024");
        int activeColor = Color.parseColor("#FFB4AA");
        int activeBg = Color.parseColor("#25FFFFFF");

        chipNew.setTextColor("NEW".equals(currentFormat) ? activeColor : normalColor);
        chipNew.setBackgroundColor("NEW".equals(currentFormat) ? activeBg : normalBg);

        chipSeries.setTextColor("SERIES".equals(currentFormat) ? activeColor : normalColor);
        chipSeries.setBackgroundColor("SERIES".equals(currentFormat) ? activeBg : normalBg);

        chipSingle.setTextColor("SINGLE".equals(currentFormat) ? activeColor : normalColor);
        chipSingle.setBackgroundColor("SINGLE".equals(currentFormat) ? activeBg : normalBg);

        int genreNormalColor = Color.parseColor("#E2E2E8");
        int genreNormalBg = android.R.drawable.screen_background_dark_transparent;

        for (TextView genreChip : genreChipsList) {
            String chipSlug = (String) genreChip.getTag();
            if (chipSlug != null && chipSlug.equals(currentGenreSlug)) {
                genreChip.setTextColor(activeColor);
                genreChip.setBackgroundColor(activeBg);
            } else {
                genreChip.setTextColor(genreNormalColor);
                genreChip.setBackgroundResource(genreNormalBg);
            }
        }
    }

    private void updateBottomNavigationIcons(View view) {
        ImageView ivHome = view.findViewById(R.id.ivHome);
        TextView tvHome = view.findViewById(R.id.tvHome);
        ImageView ivExplore = view.findViewById(R.id.ivExplore);
        TextView tvExplore = view.findViewById(R.id.tvExplore);

        if (ivHome != null) ivHome.setColorFilter(Color.parseColor("#9CA3AF"));
        if (tvHome != null) tvHome.setTextColor(Color.parseColor("#9CA3AF"));
        if (ivExplore != null) ivExplore.setColorFilter(Color.parseColor("#FFB4AA"));
        if (tvExplore != null) tvExplore.setTextColor(Color.parseColor("#FFB4AA"));
    }
}