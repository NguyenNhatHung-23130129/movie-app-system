package com.example.movie_app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.adapter.GenreAdapter;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies, rvGenresContinue, rvGenresNew, rvGenresSeries, rvGenresSingle;

    private MovieAdapter continueWatchingAdapter;
    private MovieAdapter newMoviesAdapter;
    private MovieAdapter seriesAdapter;
    private MovieAdapter singleMoviesAdapter;

    private GenreAdapter genreAdapter;
    private List<Genre> genreList = new ArrayList<>();

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initViews();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        loadDataFromApi();
    }

    private void initViews() {
        rvContinueWatching = findViewById(R.id.rvContinueWatching);
        rvNewMovies = findViewById(R.id.rvNewMovies);
        rvSeries = findViewById(R.id.rvSeries);
        rvSingleMovies = findViewById(R.id.rvSingleMovies);
        rvGenresContinue = findViewById(R.id.rvGenresContinue);
        rvGenresNew = findViewById(R.id.rvGenresNew);
        rvGenresSeries = findViewById(R.id.rvGenresSeries);
        rvGenresSingle = findViewById(R.id.rvGenresSingle);

        LinearLayoutManager lm1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager lm4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvGenresContinue.setLayoutManager(lm1);
        rvGenresNew.setLayoutManager(lm2);
        rvGenresSeries.setLayoutManager(lm3);
        rvGenresSingle.setLayoutManager(lm4);

        rvContinueWatching.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        continueWatchingAdapter = new MovieAdapter(new ArrayList<>());
        rvContinueWatching.setAdapter(continueWatchingAdapter);

        rvNewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newMoviesAdapter = new MovieAdapter(new ArrayList<>());
        rvNewMovies.setAdapter(newMoviesAdapter);

        rvSeries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        seriesAdapter = new MovieAdapter(new ArrayList<>());
        rvSeries.setAdapter(seriesAdapter);

        rvSingleMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        singleMoviesAdapter = new MovieAdapter(new ArrayList<>());
        rvSingleMovies.setAdapter(singleMoviesAdapter);

        MovieAdapter.OnItemClickListener clickListener = movie -> {
            Toast.makeText(MainActivity.this, "Đang mở phim: " + movie.getName(), Toast.LENGTH_SHORT).show();
        };

        newMoviesAdapter.setOnItemClickListener(clickListener);
        seriesAdapter.setOnItemClickListener(clickListener);
        singleMoviesAdapter.setOnItemClickListener(clickListener);
        continueWatchingAdapter.setOnItemClickListener(clickListener);
    }

    private void loadDataFromApi() {

        movieViewModel.getGenres().observe(this, genres -> {
            if (genres != null) {
                android.util.Log.d("API_CHECK", "Thể loại về được: " + genres.size() + " danh mục");
                genreList.clear();
                genreList.addAll(genres);
                rvGenresContinue.setAdapter(new GenreAdapter(this, genreList));
                rvGenresNew.setAdapter(new GenreAdapter(this, genreList));
                rvGenresSeries.setAdapter(new GenreAdapter(this, genreList));
                rvGenresSingle.setAdapter(new GenreAdapter(this, genreList));
            } else {
                android.util.Log.e("API_CHECK", "Danh sách thể loại bị NULL!");
            }
        });

        movieViewModel.getLatestMovies(1).observe(this, movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                List<MovieItem> items = movieResponse.getItems();
                newMoviesAdapter.setMovieList(items);

                continueWatchingAdapter.setMovieList(items);
            }
        });

        movieViewModel.getSeriesMovies(1).observe(this, movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                android.util.Log.d("API_CHECK", "Phim bộ về được: " + movieResponse.getItems().size() + " phim");
                seriesAdapter.setMovieList(movieResponse.getItems());
            } else {
                android.util.Log.e("API_CHECK", "Phim bộ bị NULL hoặc trống rỗng!");
            }
        });

        movieViewModel.getSingleMovies(1).observe(this, movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                android.util.Log.d("API_CHECK", "Phim lẻ về được: " + movieResponse.getItems().size() + " phim");
                singleMoviesAdapter.setMovieList(movieResponse.getItems());
            } else {
                android.util.Log.e("API_CHECK", "Phim lẻ bị NULL hoặc trống rỗng!");
            }
        });
    }
}