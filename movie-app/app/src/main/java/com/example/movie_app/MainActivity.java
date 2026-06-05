package com.example.movie_app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvContinueWatching, rvNewMovies, rvSeries, rvSingleMovies, rvGenres;
    private MovieAdapter newMoviesAdapter;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initViews();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getLatestMovies(1).observe(this, movieResponse -> {
            if (movieResponse != null) {
                List<MovieItem> items = movieResponse.getItems();

                if (items != null && !items.isEmpty()) {
                    newMoviesAdapter.setMovieList(items);
                } else {
                    Toast.makeText(MainActivity.this, "Danh sách phim trống!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Lỗi kết nối API KKPhim / Spring Boot!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        rvContinueWatching = findViewById(R.id.rvContinueWatching);
        rvNewMovies = findViewById(R.id.rvNewMovies);
        rvSeries = findViewById(R.id.rvSeries);
        rvSingleMovies = findViewById(R.id.rvSingleMovies);
        rvGenres = findViewById(R.id.rvGenres);

        rvNewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewMovies.setHasFixedSize(true);

        newMoviesAdapter = new MovieAdapter(new ArrayList<>());
        rvNewMovies.setAdapter(newMoviesAdapter);

        newMoviesAdapter.setOnItemClickListener(movie -> {
            Toast.makeText(MainActivity.this, "Đang mở phim: " + movie.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}