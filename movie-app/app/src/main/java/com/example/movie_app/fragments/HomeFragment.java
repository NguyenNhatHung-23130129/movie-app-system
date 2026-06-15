package com.example.movie_app.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.movie_app.adapter.GenreAdapter;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.helpers.MovieFilterHelper;
import com.example.movie_app.models.Genre;
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

    private MovieAdapter continueWatchingAdapter, newMoviesAdapter, seriesAdapter, singleMoviesAdapter;
    private List<Genre> genreList = new ArrayList<>();
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
        loadDataFromApi();
    }

    private void initViews(View view) {
        // Ánh xạ RecyclerViews
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
        adapter.setOnItemClickListener(movie -> {
            if (movie != null && movie.getSlug() != null) {
                startActivity(new Intent(requireContext(), MovieDetailActivity.class).putExtra("movie_slug", movie.getSlug()));
            }
        });
        rv.setAdapter(adapter);
        return adapter;
    }

    private void loadDataFromApi() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null && !genres.isEmpty()) {
                genreList.clear();
                genreList.addAll(genres);

                setupGenreRecyclerView(rvGenresContinue, continueWatchingAdapter, "LATEST");
                setupGenreRecyclerView(rvGenresNew, newMoviesAdapter, "NEW");
                setupGenreRecyclerView(rvGenresSeries, seriesAdapter, "SERIES");
                setupGenreRecyclerView(rvGenresSingle, singleMoviesAdapter, "SINGLE");
            }
        });

        movieViewModel.getLatestMovies(1).observe(getViewLifecycleOwner(), res -> {
            if (res != null && res.getItems() != null && !res.getItems().isEmpty()) {
                newMoviesAdapter.setMovieList(res.getItems());
                continueWatchingAdapter.setMovieList(res.getItems());

                heroMovie = res.getItems().get(0);
                updateHeroSection(heroMovie);
            }
        });

        movieViewModel.getSeriesMovies(1).observe(getViewLifecycleOwner(), res -> {
            if (res != null) seriesAdapter.setMovieList(res.getItems());
        });
        movieViewModel.getSingleMovies(1).observe(getViewLifecycleOwner(), res -> {
            if (res != null) singleMoviesAdapter.setMovieList(res.getItems());
        });
    }

    private void updateHeroSection(MovieItem movie) {
        tvHeroTitle.setText(movie.getName());
        Glide.with(this).load(movie.getPosterUrl()).into(imgHeroPoster);
        btnHeroDetail.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MovieDetailActivity.class).putExtra("movie_slug", movie.getSlug()));
        });
    }

    private void setupGenreRecyclerView(RecyclerView rv, MovieAdapter targetAdapter, String type) {
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        GenreAdapter adapter = new GenreAdapter(requireContext(), genreList, genre -> {
            movieViewModel.getMoviesByCategory(genre.getSlug(), 1).observe(getViewLifecycleOwner(), res -> {
                if (res != null && res.getItems() != null) {
                    targetAdapter.setMovieList(MovieFilterHelper.filterMovies(res.getItems(), type));
                }
            });
        });

        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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