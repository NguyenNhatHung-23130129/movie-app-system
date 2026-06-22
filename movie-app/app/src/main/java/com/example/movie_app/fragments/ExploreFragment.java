package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.activities.SearchActivity;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Category;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends BaseFragment {

    private TextView chipNew, chipSeries, chipSingle;
    private LinearLayout layoutGenres;
    private RecyclerView rvExplore;
    private EditText edtSearch;
    private MovieAdapter exploreAdapter;
    private MovieViewModel movieViewModel;

    private final List<TextView> genreChipsList = new ArrayList<>();
    private final List<MovieItem> allMoviesList = new ArrayList<>();

    private String currentFormat = "NEW";
    private String currentGenreSlug = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        initViews(view);
        setupListeners();
        loadData();
    }

    private void initViews(View view) {
        chipNew = view.findViewById(R.id.chipNew);
        chipSeries = view.findViewById(R.id.chipSeries);
        chipSingle = view.findViewById(R.id.chipSingle);
        layoutGenres = view.findViewById(R.id.layoutGenres);
        rvExplore = view.findViewById(R.id.rvExplore);
        edtSearch = view.findViewById(R.id.edtSearch);

        // Đã sửa thành 3 cột theo ý bạn
        rvExplore.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        exploreAdapter = new MovieAdapter(new ArrayList<>());
        rvExplore.setAdapter(exploreAdapter);

        edtSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchActivity.class)));
        edtSearch.setFocusable(false);
    }

    private void setupListeners() {
        chipNew.setOnClickListener(v -> updateFormat("NEW"));
        chipSeries.setOnClickListener(v -> updateFormat("SERIES"));
        chipSingle.setOnClickListener(v -> updateFormat("SINGLE"));
    }

    private void loadData() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) renderGenreChips(categories);
        });

        movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                Log.d("FILTER_DEBUG", "Đã tải thành công: " + movies.size() + " phim từ Firebase");
                allMoviesList.clear();
                allMoviesList.addAll(movies);
                applyFilters();
            }
        });
    }

    private void applyFilters() {
        List<MovieItem> filteredList = new ArrayList<>();

        for (MovieItem movie : allMoviesList) {
            // Log để debug xem từng phim có dữ liệu gì
            Log.d("FILTER_DEBUG", "Đang kiểm tra phim: " + movie.getName() + " | Cat: " + movie.getCategory());

            if (movie.getCategory() == null) continue;

            // Kiểm tra Format
            boolean matchFormat = true;
            if (currentFormat.equals("SERIES")) {
                matchFormat = movie.getCategory().stream().anyMatch(c ->
                        "phim-bo".equalsIgnoreCase(c.getSlug()) || c.getName().contains("Bộ"));
            } else if (currentFormat.equals("SINGLE")) {
                matchFormat = movie.getCategory().stream().anyMatch(c ->
                        "phim-le".equalsIgnoreCase(c.getSlug()) || c.getName().contains("Lẻ"));
            }

            // Kiểm tra Thể loại (Genre)
            boolean matchGenre = true;
            if (currentGenreSlug != null) {
                // Kiểm tra bằng cả Slug và Name để không bị sót
                matchGenre = movie.getCategory().stream().anyMatch(c ->
                        c.getSlug().equalsIgnoreCase(currentGenreSlug) ||
                                c.getName().equalsIgnoreCase(currentGenreSlug)
                );
            }

            if (matchFormat && matchGenre) {
                filteredList.add(movie);
            }
        }

        Log.d("FILTER_DEBUG", "Kết quả sau lọc: " + filteredList.size() + " phim");
        exploreAdapter.setMovieList(filteredList);
        updateUI();
    }

    private void renderGenreChips(List<Category> categories) {
        layoutGenres.removeAllViews();
        genreChipsList.clear();

        for (Category category : categories) {
            TextView chip = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_genre, layoutGenres, false);
            chip.setText(category.getName());
            chip.setTag(category.getSlug());
            chip.setOnClickListener(v -> {
                currentGenreSlug = category.getSlug().equals(currentGenreSlug) ? null : category.getSlug();
                applyFilters();
            });
            layoutGenres.addView(chip);
            genreChipsList.add(chip);
        }
        updateUI();
    }

    private void updateFormat(String format) {
        this.currentFormat = format;
        applyFilters();
    }

    private void updateUI() {
        chipNew.setBackgroundResource("NEW".equals(currentFormat) ? R.drawable.genre_bg_selected : R.drawable.genre_bg_normal);
        chipSeries.setBackgroundResource("SERIES".equals(currentFormat) ? R.drawable.genre_bg_selected : R.drawable.genre_bg_normal);
        chipSingle.setBackgroundResource("SINGLE".equals(currentFormat) ? R.drawable.genre_bg_selected : R.drawable.genre_bg_normal);

        for (TextView chip : genreChipsList) {
            boolean isSelected = chip.getTag().equals(currentGenreSlug);
            chip.setBackgroundResource(isSelected ? R.drawable.genre_bg_selected : R.drawable.genre_bg_normal);
        }
    }
}