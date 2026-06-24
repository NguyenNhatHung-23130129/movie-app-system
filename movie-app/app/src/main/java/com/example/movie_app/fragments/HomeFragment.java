package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;
import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.adapter.CategoryAdapter;
import com.example.movie_app.models.Category;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.utils.RecommendationEngine;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies, rvRecommendedMovies,
            rvGenresContinue, rvGenresNew, rvGenresSeries, rvGenresSingle;
    private TextView btnViewAllNew, btnViewAllSeries, btnViewAllSingle, btnViewAllContinue, tvHeroTitle;
    private ImageView imgHeroPoster;
    private Button btnHeroDetail;

    private MovieAdapter continueWatchingAdapter, newMoviesAdapter, seriesAdapter, singleMoviesAdapter, recommendedAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private MovieViewModel movieViewModel;
    private RecommendationEngine recommendationEngine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        recommendationEngine = new RecommendationEngine(requireContext());

        loadCategories();
        loadDataFromFirebase();
        loadRecommendedMovies();
    }

    private void initViews(View view) {
        rvContinueWatching = view.findViewById(R.id.rvContinueWatching);
        rvNewMovies = view.findViewById(R.id.rvNewMovies);
        rvSeries = view.findViewById(R.id.rvSeries);
        rvSingleMovies = view.findViewById(R.id.rvSingleMovies);
        rvRecommendedMovies = view.findViewById(R.id.rvRecommendedMovies);

        rvGenresContinue = view.findViewById(R.id.rvGenresContinue);
        rvGenresNew = view.findViewById(R.id.rvGenresNew);
        rvGenresSeries = view.findViewById(R.id.rvGenresSeries);
        rvGenresSingle = view.findViewById(R.id.rvGenresSingle);

        btnViewAllContinue = view.findViewById(R.id.btnViewAllContinue);
        btnViewAllNew = view.findViewById(R.id.btnViewAllNew);
        btnViewAllSeries = view.findViewById(R.id.btnViewAllSeries);
        btnViewAllSingle = view.findViewById(R.id.btnViewAllSingle);

        imgHeroPoster = view.findViewById(R.id.imgHeroPoster);
        tvHeroTitle = view.findViewById(R.id.tvHeroTitle);
        btnHeroDetail = view.findViewById(R.id.btnHeroDetail);

        continueWatchingAdapter = setupMovieRecyclerView(rvContinueWatching);
        newMoviesAdapter = setupMovieRecyclerView(rvNewMovies);
        seriesAdapter = setupMovieRecyclerView(rvSeries);
        singleMoviesAdapter = setupMovieRecyclerView(rvSingleMovies);
        recommendedAdapter = setupMovieRecyclerView(rvRecommendedMovies);

        if (btnViewAllContinue != null) btnViewAllContinue.setOnClickListener(v -> navigateToExplore("LATEST"));
        if (btnViewAllNew != null) btnViewAllNew.setOnClickListener(v -> navigateToExplore("NEW"));
        if (btnViewAllSeries != null) btnViewAllSeries.setOnClickListener(v -> navigateToExplore("SERIES"));
        if (btnViewAllSingle != null) btnViewAllSingle.setOnClickListener(v -> navigateToExplore("SINGLE"));
    }

    private MovieAdapter setupMovieRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        MovieAdapter adapter = new MovieAdapter(new ArrayList<>());
        rv.setAdapter(adapter);
        return adapter;
    }

    private void loadRecommendedMovies() {
        String lastWatchedSlug = PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString("LAST_WATCHED_SLUG", null);

        if (lastWatchedSlug != null) {
            List<String> suggestedSlugs = recommendationEngine.getRecommendations(lastWatchedSlug, 6);
            if (!suggestedSlugs.isEmpty()) {
                movieViewModel.getMoviesBySlugs(suggestedSlugs).observe(getViewLifecycleOwner(), movieList -> {
                    if (movieList != null) recommendedAdapter.setMovieList(movieList);
                });
            }
        }
    }

    private void loadCategories() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryList.clear();
                categoryList.addAll(categories);

                setupCategoryRecyclerView(rvGenresContinue, rvContinueWatching, continueWatchingAdapter, null);
                setupCategoryRecyclerView(rvGenresNew, rvNewMovies, newMoviesAdapter, null);
                setupCategoryRecyclerView(rvGenresSeries, rvSeries, seriesAdapter, "series");
                setupCategoryRecyclerView(rvGenresSingle, rvSingleMovies, singleMoviesAdapter, "single");
            }
        });
    }

    private void loadDataFromFirebase() {
        movieViewModel.getMoviesByPath("by_type", "series", "series")
                .observe(getViewLifecycleOwner(), list -> {
                    if (list != null) seriesAdapter.setMovieList(list);
                });

        movieViewModel.getMoviesByPath("by_type", "single", "single")
                .observe(getViewLifecycleOwner(), list -> {
                    if (list != null) singleMoviesAdapter.setMovieList(list);
                });

        movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), movieList -> {
            if (movieList != null && !movieList.isEmpty()) {
                newMoviesAdapter.setMovieList(movieList);
                continueWatchingAdapter.setMovieList(movieList);
                updateHeroSection(movieList.get(0));
            }
        });
    }

    private void updateHeroSection(MovieItem movie) {
        tvHeroTitle.setText(movie.getName());
        imgHeroPoster.post(() -> Glide.with(this).load(movie.getPosterUrl()).into(imgHeroPoster));
        btnHeroDetail.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MovieDetailActivity.class);
            intent.putExtra("movie_slug", movie.getSlug());
            intent.putExtra("movie_image", movie.getPosterUrl());
            startActivity(intent);
        });
    }

    private void setupCategoryRecyclerView(RecyclerView categoryRv, RecyclerView moviesRv, MovieAdapter targetAdapter, String filterType) {
        if (categoryRv == null) return;
        categoryRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Category> categoryCopy = new ArrayList<>(categoryList);

        CategoryAdapter adapter = new CategoryAdapter(requireContext(), categoryCopy, category -> {
            if (category == null) {
                loadDefaultDataForType(targetAdapter, filterType);
            } else {
                movieViewModel.getMoviesByPath("by_category", category.getSlug(), filterType)
                        .observe(getViewLifecycleOwner(), movieList -> {
                            if (movieList != null) {
                                targetAdapter.setMovieList(movieList);
                                if (moviesRv != null) {
                                    moviesRv.scrollToPosition(0);
                                }
                            }
                        });
            }
        });
        categoryRv.setAdapter(adapter);
    }

    private void loadDefaultDataForType(MovieAdapter adapter, String filterType) {
        if ("series".equals(filterType)) {
            movieViewModel.getMoviesByPath("by_type", "series", "series")
                    .observe(getViewLifecycleOwner(), adapter::setMovieList);
        } else if ("single".equals(filterType)) {
            movieViewModel.getMoviesByPath("by_type", "single", "single")
                    .observe(getViewLifecycleOwner(), adapter::setMovieList);
        } else {
            movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), adapter::setMovieList);
        }
    }

    private void navigateToExplore(String movieType) {
        ExploreFragment exploreFragment = new ExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_TYPE", movieType);
        exploreFragment.setArguments(bundle);

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).loadFragment(exploreFragment, R.id.tabExplore);
        }
    }
}