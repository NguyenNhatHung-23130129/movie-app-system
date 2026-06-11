package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;
import com.example.movie_app.adapter.GenreAdapter;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies,
            rvGenresContinue, rvGenresNew, rvGenresSeries, rvGenresSingle;
    private TextView btnViewAllNew, btnViewAllSeries, btnViewAllSingle;

    private MovieAdapter continueWatchingAdapter;
    private MovieAdapter newMoviesAdapter;
    private MovieAdapter seriesAdapter;
    private MovieAdapter singleMoviesAdapter;

    private List<Genre> genreList = new ArrayList<>();
    private MovieViewModel movieViewModel;

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
        rvContinueWatching = view.findViewById(R.id.rvContinueWatching);
        rvNewMovies = view.findViewById(R.id.rvNewMovies);
        rvSeries = view.findViewById(R.id.rvSeries);
        rvSingleMovies = view.findViewById(R.id.rvSingleMovies);
        rvGenresContinue = view.findViewById(R.id.rvGenresContinue);
        rvGenresNew = view.findViewById(R.id.rvGenresNew);
        rvGenresSeries = view.findViewById(R.id.rvGenresSeries);
        rvGenresSingle = view.findViewById(R.id.rvGenresSingle);

        btnViewAllNew = view.findViewById(R.id.btnViewAllNew);
        btnViewAllSeries = view.findViewById(R.id.btnViewAllSeries);
        btnViewAllSingle = view.findViewById(R.id.btnViewAllSingle);

        LinearLayoutManager lm1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm3 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm4 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        rvGenresContinue.setLayoutManager(lm1);
        rvGenresNew.setLayoutManager(lm2);
        rvGenresSeries.setLayoutManager(lm3);
        rvGenresSingle.setLayoutManager(lm4);

        rvContinueWatching.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        continueWatchingAdapter = new MovieAdapter(new ArrayList<>());
        rvContinueWatching.setAdapter(continueWatchingAdapter);

        rvNewMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        newMoviesAdapter = new MovieAdapter(new ArrayList<>());
        rvNewMovies.setAdapter(newMoviesAdapter);

        rvSeries.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        seriesAdapter = new MovieAdapter(new ArrayList<>());
        rvSeries.setAdapter(seriesAdapter);

        rvSingleMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        singleMoviesAdapter = new MovieAdapter(new ArrayList<>());
        rvSingleMovies.setAdapter(singleMoviesAdapter);

        MovieAdapter.OnItemClickListener clickListener = movie -> {
            if (movie != null && movie.getSlug() != null) {
                android.content.Intent intent = new android.content.Intent(requireContext(), com.example.movie_app.activities.MovieDetailActivity.class);

                intent.putExtra("movie_slug", movie.getSlug());
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Không thể mở bộ phim này!", Toast.LENGTH_SHORT).show();
            }
        };

        newMoviesAdapter.setOnItemClickListener(clickListener);
        seriesAdapter.setOnItemClickListener(clickListener);
        singleMoviesAdapter.setOnItemClickListener(clickListener);
        continueWatchingAdapter.setOnItemClickListener(clickListener);

        if (btnViewAllNew != null) btnViewAllNew.setOnClickListener(v -> navigateToExplore("NEW"));
        if (btnViewAllSeries != null) btnViewAllSeries.setOnClickListener(v -> navigateToExplore("SERIES"));
        if (btnViewAllSingle != null) btnViewAllSingle.setOnClickListener(v -> navigateToExplore("SINGLE"));
    }

    private void navigateToExplore(String movieType) {
        ExploreFragment exploreFragment = new ExploreFragment();

        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_TYPE", movieType);
        exploreFragment.setArguments(bundle);

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).loadFragment(exploreFragment);
        }
    }

    private void loadDataFromApi() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                genreList.clear();
                genreList.addAll(genres);
                rvGenresContinue.setAdapter(new GenreAdapter(requireContext(), genreList));
                rvGenresNew.setAdapter(new GenreAdapter(requireContext(), genreList));
                rvGenresSeries.setAdapter(new GenreAdapter(requireContext(), genreList));
                rvGenresSingle.setAdapter(new GenreAdapter(requireContext(), genreList));
            }
        });

        movieViewModel.getLatestMovies(1).observe(getViewLifecycleOwner(), movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                List<MovieItem> items = movieResponse.getItems();
                newMoviesAdapter.setMovieList(items);
                continueWatchingAdapter.setMovieList(items);
            }
        });

        movieViewModel.getSeriesMovies(1).observe(getViewLifecycleOwner(), movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                seriesAdapter.setMovieList(movieResponse.getItems());
            }
        });

        movieViewModel.getSingleMovies(1).observe(getViewLifecycleOwner(), movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                singleMoviesAdapter.setMovieList(movieResponse.getItems());
            }
        });
    }
}