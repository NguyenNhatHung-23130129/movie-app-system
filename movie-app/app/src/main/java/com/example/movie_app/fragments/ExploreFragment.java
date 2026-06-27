package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button btnPrev, btnNext;
    private TextView txtPageInfo;
    private int currentPage = 1;
    private final int PAGE_SIZE = 18;

    private MovieAdapter exploreAdapter;
    private MovieViewModel movieViewModel;

    private final List<TextView> genreChipsList = new ArrayList<>();
    private final List<MovieItem> allMoviesList = new ArrayList<>();
    private final List<MovieItem> currentFilteredList = new ArrayList<>();

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

        if (getArguments() != null) {
            String type = getArguments().getString("MOVIE_TYPE");
            if (type != null) {
                if (type.equals("LATEST")) currentFormat = "NEW";
                else currentFormat = type;
            }
        }

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

        rvExplore.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        exploreAdapter = new MovieAdapter(new ArrayList<>());
        rvExplore.setAdapter(exploreAdapter);

        edtSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchActivity.class)));
        edtSearch.setFocusable(false);

        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);
        txtPageInfo = view.findViewById(R.id.txtPageInfo);

        rvExplore.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        exploreAdapter = new MovieAdapter(new ArrayList<>());
        rvExplore.setAdapter(exploreAdapter);
    }

    private void setupListeners() {
        chipNew.setOnClickListener(v -> { resetPagination(); updateFormat("NEW"); });
        chipSeries.setOnClickListener(v -> { resetPagination(); updateFormat("SERIES"); });
        chipSingle.setOnClickListener(v -> { resetPagination(); updateFormat("SINGLE"); });

        btnNext.setOnClickListener(v -> {
            if ((currentPage * PAGE_SIZE) < currentFilteredList.size()) {
                currentPage++;
                renderPage();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                renderPage();
            }
        });
    }

    private void renderPage() {
        int totalItems = currentFilteredList.size();
        int fromIndex = (currentPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalItems);

        if (fromIndex < totalItems) {
            List<MovieItem> pageList = currentFilteredList.subList(fromIndex, toIndex);
            exploreAdapter.setMovieList(pageList);
        } else {
            exploreAdapter.setMovieList(new ArrayList<>());
        }

        txtPageInfo.setText("Trang " + currentPage);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled((currentPage * PAGE_SIZE) < totalItems);
    }

    private void resetPagination() {
        currentPage = 1;
    }

    private void loadData() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) renderGenreChips(categories);
        });

        movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                allMoviesList.clear();
                allMoviesList.addAll(movies);
                applyFilters();
            }
        });
    }

    private void applyFilters() {
        if (currentGenreSlug != null) {
            String type = currentFormat.equals("NEW") ? "" : currentFormat.toLowerCase();
            movieViewModel.getMoviesByPath("by_category", currentGenreSlug, type)
                    .observe(getViewLifecycleOwner(), movies -> {
                        currentFilteredList.clear();
                        if (movies != null) currentFilteredList.addAll(movies);
                        renderPage();
                    });
        }
        else {
            currentFilteredList.clear();
            for (MovieItem m : allMoviesList) {
                if (currentFormat.equals("NEW") || currentFormat.toLowerCase().equals(m.getType())) {
                    currentFilteredList.add(m);
                }
            }
            renderPage();
        }
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