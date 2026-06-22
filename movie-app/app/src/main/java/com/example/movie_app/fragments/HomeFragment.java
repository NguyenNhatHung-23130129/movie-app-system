package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies,
            rvGenresContinue, rvGenresNew, rvGenresSeries, rvGenresSingle;
    private TextView btnViewAllNew, btnViewAllSeries, btnViewAllSingle, btnViewAllContinue, tvHeroTitle;
    private ImageView imgHeroPoster;
    private Button btnHeroDetail;
    private MovieAdapter activeAdapter;

    private MovieAdapter continueWatchingAdapter, newMoviesAdapter, seriesAdapter, singleMoviesAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private MovieViewModel movieViewModel;
    private MovieItem heroMovie;

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

        loadCategories();
        loadDataFromFirebase();
    }

    private void initViews(View view) {
        rvContinueWatching = view.findViewById(R.id.rvContinueWatching);
        rvNewMovies = view.findViewById(R.id.rvNewMovies);
        rvSeries = view.findViewById(R.id.rvSeries);
        rvSingleMovies = view.findViewById(R.id.rvSingleMovies);
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

    private void loadCategories() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryList.clear();
                categoryList.addAll(categories);

                setupCategoryRecyclerView(rvGenresContinue, continueWatchingAdapter);
                setupCategoryRecyclerView(rvGenresNew, newMoviesAdapter);
                setupCategoryRecyclerView(rvGenresSeries, seriesAdapter);
                setupCategoryRecyclerView(rvGenresSingle, singleMoviesAdapter);
            }
        });
    }

    private void loadDataFromFirebase() {
        movieViewModel.getMoviesFromFirebase().observe(getViewLifecycleOwner(), movieList -> {
            if (movieList != null && !movieList.isEmpty()) {
                List<MovieItem> latestList = new ArrayList<>();
                List<MovieItem> seriesList = new ArrayList<>();
                List<MovieItem> singleList = new ArrayList<>();

                for (MovieItem movie : movieList) {
                    // 1. Luôn thêm vào danh sách phim mới (Latest)
                    latestList.add(movie);

                    // 2. Lọc bằng trường type mới (cách này chuẩn xác và nhanh nhất)
                    // Giả định giá trị trả về từ API/Firebase là "series" và "single"
                    if ("series".equals(movie.getType())) {
                        seriesList.add(movie);
                    } else if ("single".equals(movie.getType())) {
                        singleList.add(movie);
                    }
                }

                newMoviesAdapter.setMovieList(latestList);
                seriesAdapter.setMovieList(seriesList);
                singleMoviesAdapter.setMovieList(singleList);
                continueWatchingAdapter.setMovieList(latestList);

                if (!movieList.isEmpty()) {
                    heroMovie = movieList.get(0);
                    updateHeroSection(heroMovie);
                }
            }
        });
    }

    private void updateHeroSection(MovieItem movie) {
        tvHeroTitle.setText(movie.getName());
        imgHeroPoster.post(() -> Glide.with(this).load(movie.getThumbUrl()).into(imgHeroPoster));
        btnHeroDetail.setOnClickListener(v -> startActivity(new Intent(requireContext(), MovieDetailActivity.class)
                .putExtra("movie_slug", movie.getSlug())));
    }

    private void setupCategoryRecyclerView(RecyclerView rv, MovieAdapter targetAdapter) {
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        CategoryAdapter adapter = new CategoryAdapter(requireContext(), categoryList, category -> {
            movieViewModel.getMoviesByCategory(category.getSlug(), 1)
                    .observe(getViewLifecycleOwner(), movieList -> {
                        if (movieList != null) {
                            targetAdapter.setMovieList(movieList);
                            rv.scrollToPosition(0);
                        }
                    });
        });
        rv.setAdapter(adapter);
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