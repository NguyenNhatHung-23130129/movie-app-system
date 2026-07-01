package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;
import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.activities.VideoPlayerActivity;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.adapter.CategoryAdapter;
import com.example.movie_app.models.Category;
import com.example.movie_app.models.Movie;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.utils.RecommendationEngine;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies, rvRecommendedMovies, rvGenresNew, rvGenresSeries, rvGenresSingle;
    private TextView btnViewAllNew, btnViewAllSeries, btnViewAllSingle, btnViewAllContinue, tvHeroTitle;
    private ImageView imgHeroPoster;
    private Button btnHeroDetail;
    private Button btnHeroWatchNow;

    private MovieAdapter continueWatchingAdapter, newMoviesAdapter, seriesAdapter, singleMoviesAdapter, recommendedAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private MovieViewModel movieViewModel;
    private RecommendationEngine recommendationEngine;
    private LinearLayout layoutRecommendedSection;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieItem currentHeroMovie;

    private boolean isFirstLoad = true;

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoad) {
            loadRecommendedMovies();
        }
        isFirstLoad = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(0xFFE50914);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        initViews(view);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        recommendationEngine = new RecommendationEngine(requireContext());

        loadAllData();
    }

    private void loadAllData() {
        loadCategories();
        loadDataFromFirebase();
        loadRecommendedMovies();
    }

    private void refreshData() {
        loadAllData();
        swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
    }

    private void initViews(View view) {
        rvContinueWatching = view.findViewById(R.id.rvContinueWatching);
        rvNewMovies = view.findViewById(R.id.rvNewMovies);
        rvSeries = view.findViewById(R.id.rvSeries);
        rvSingleMovies = view.findViewById(R.id.rvSingleMovies);
        rvRecommendedMovies = view.findViewById(R.id.rvRecommendedMovies);

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
        btnHeroWatchNow = view.findViewById(R.id.btnHeroWatchNow);

        continueWatchingAdapter = setupMovieRecyclerView(rvContinueWatching);
        newMoviesAdapter = setupMovieRecyclerView(rvNewMovies);
        seriesAdapter = setupMovieRecyclerView(rvSeries);
        singleMoviesAdapter = setupMovieRecyclerView(rvSingleMovies);
        recommendedAdapter = setupMovieRecyclerView(rvRecommendedMovies);
        layoutRecommendedSection = view.findViewById(R.id.layoutRecommendedSection);

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
        String currentUserId = "USER_ID_TEST";
        movieViewModel.getPersonalizedRecommendations(currentUserId, requireContext())
                .observe(getViewLifecycleOwner(), movieList -> {
                    if (movieList != null && !movieList.isEmpty()) {
                        recommendedAdapter.setMovieList(movieList);
                        recommendedAdapter.notifyDataSetChanged();
                        layoutRecommendedSection.setVisibility(View.VISIBLE);
                        rvRecommendedMovies.setVisibility(View.VISIBLE);
                    } else {
                        rvRecommendedMovies.setVisibility(View.GONE);
                        layoutRecommendedSection.setVisibility(View.GONE);
                    }
                });
    }

    private void loadCategories() {
        if (movieViewModel == null) return;

        LiveData<List<Category>> genreLiveData = movieViewModel.getGenres();

        if (genreLiveData != null) {
            genreLiveData.observe(getViewLifecycleOwner(), categories -> {
                if (categories != null && !categories.isEmpty()) {
                    categoryList.clear();
                    categoryList.addAll(categories);

                    setupCategoryRecyclerView(rvGenresNew, rvNewMovies, newMoviesAdapter, null);
                    setupCategoryRecyclerView(rvGenresSeries, rvSeries, seriesAdapter, "series");
                    setupCategoryRecyclerView(rvGenresSingle, rvSingleMovies, singleMoviesAdapter, "single");
                }
            });
        } else {
            Log.e("HOME_FRAGMENT", "Repository trả về null cho genres");
        }
    }

    private void loadDataFromFirebase() {
        movieViewModel.getMoviesByPath("by_type", "series", "series").observe(getViewLifecycleOwner(), list -> {
            if (list != null) seriesAdapter.setMovieList(list);
        });

        movieViewModel.getMoviesByPath("by_type", "single", "single").observe(getViewLifecycleOwner(), list -> {
            if (list != null) singleMoviesAdapter.setMovieList(list);
        });

        movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), movieList -> {
            if (movieList == null || movieList.isEmpty()) {
                movieViewModel.refreshData();
            } else {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                newMoviesAdapter.setMovieList(movieList);
                newMoviesAdapter.notifyDataSetChanged();

                continueWatchingAdapter.setMovieList(movieList);
                continueWatchingAdapter.notifyDataSetChanged();

                updateHeroSection(movieList.get(0));
            }
        });
    }

    private void updateHeroSection(MovieItem movie) {
        if (movie == null) return;
        currentHeroMovie = movie;

        tvHeroTitle.setText(movie.getName());
        imgHeroPoster.post(() -> Glide.with(this).load(movie.getPosterUrl()).into(imgHeroPoster));

        btnHeroDetail.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MovieDetailActivity.class);
            intent.putExtra("movie_slug", movie.getSlug());
            intent.putExtra("movie_image", movie.getPosterUrl());
            startActivity(intent);
        });

        if (btnHeroWatchNow != null) {
            btnHeroWatchNow.setOnClickListener(v -> watchHeroMovieNow(movie));
        }
    }

    private void watchHeroMovieNow(MovieItem movie) {
        if (movie == null || movie.getSlug() == null) return;

        btnHeroWatchNow.setEnabled(false);
        btnHeroWatchNow.setText("Đang tải...");

        movieViewModel.getMovieDetail(movie.getSlug()).observe(getViewLifecycleOwner(), response -> {
            btnHeroWatchNow.setEnabled(true);
            btnHeroWatchNow.setText("▶ Xem ngay");

            if (response == null || response.getMovie() == null) {
                Toast.makeText(requireContext(), "Không tìm thấy thông tin phim!", Toast.LENGTH_SHORT).show();
                return;
            }

            MovieDetailResponse.MovieDetail info = response.getMovie();

            ArrayList<String> urls = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();

            if (response.getEpisodes() != null && !response.getEpisodes().isEmpty()) {
                MovieDetailResponse.EpisodeServer server = response.getEpisodes().get(0);
                if (server.getServerData() != null) {
                    for (MovieDetailResponse.EpisodeData ep : server.getServerData()) {
                        String link = ep.getLinkM3u8();
                        if (link != null && !link.trim().isEmpty()) {
                            urls.add(link);
                            names.add(ep.getName() != null ? ep.getName() : "Tập " + urls.size());
                        }
                    }
                }
            }

            if (urls.isEmpty()) {
                Toast.makeText(requireContext(), "Phim này chưa có tập nào để phát!", Toast.LENGTH_SHORT).show();
                return;
            }

            movieViewModel.saveToHistory(
                    info.getSlug(),
                    "USER_ID_TEST",
                    info.getName(),
                    movie.getPosterUrl()
            );

            Movie movieObj = new Movie();
            movieObj.setMovieId(info.getSlug());
            movieObj.setTitle(info.getName());
            movieObj.setDescription(info.getContent());
            movieObj.setPosterUrl(movie.getPosterUrl());
            movieObj.setEpisodeUrls(urls);
            movieObj.setEpisodeNames(names);
            movieObj.setEpisodes(urls.size());
            movieObj.setVideoUrl(urls.get(0));

            Intent intent = new Intent(requireContext(), VideoPlayerActivity.class);
            intent.putExtra("movie", movieObj);
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