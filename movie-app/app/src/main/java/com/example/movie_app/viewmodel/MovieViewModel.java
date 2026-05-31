package com.example.movie_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.movie_app.models.MovieResponse;
import com.example.movie_app.repository.MovieRepository;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;

    public MovieViewModel() {
        this.movieRepository = new MovieRepository();
    }

    public LiveData<MovieResponse> getLatestMovies(int page) {
        return movieRepository.getLatestMovies(page);
    }
}