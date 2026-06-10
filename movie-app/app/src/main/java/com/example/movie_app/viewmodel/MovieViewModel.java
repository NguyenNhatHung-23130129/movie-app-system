package com.example.movie_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieResponse;
import com.example.movie_app.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;

    public MovieViewModel() {
        this.movieRepository = new MovieRepository();
    }

    public LiveData<MovieResponse> getLatestMovies(int page) {
        return movieRepository.getLatestMovies(page);
    }

    public LiveData<MovieResponse> getSeriesMovies(int page) {
        return movieRepository.getSeriesMovies(page);
    }

    public LiveData<MovieResponse> getSingleMovies(int page) {
        return movieRepository.getSingleMovies(page);
    }

    public LiveData<List<Genre>> getGenres() {
        return movieRepository.getGenres();
    }
}