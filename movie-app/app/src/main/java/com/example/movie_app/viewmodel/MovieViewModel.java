package com.example.movie_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.movie_app.models.*;
import com.example.movie_app.repository.MovieRepository;
import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;

    private final MutableLiveData<List<MovieItem>> moviesByCategory = new MutableLiveData<>();

    public MovieViewModel() {
        this.movieRepository = new MovieRepository();
    }


    public LiveData<List<MovieItem>> getLatestMovies(int page) {
        return movieRepository.getLatestMovies(page);
    }

    public LiveData<List<MovieItem>> getMoviesByCategory(String slug, int page) {
        // Trả về LiveData trực tiếp từ repo, không cần observeForever
        return movieRepository.getMoviesByCategory(slug, page);
    }

    public LiveData<List<MovieItem>> getSeriesMovies(int page) {
        return movieRepository.getSeriesMovies(page);
    }

    public LiveData<List<MovieItem>> getSingleMovies(int page) {
        return movieRepository.getSingleMovies(page);
    }

    public LiveData<List<Category>> getGenres() {
        return movieRepository.getGenres();
    }

    public LiveData<MovieDetailResponse> getMovieDetail(String slug) {
        return movieRepository.getMovieDetail(slug);
    }

    public LiveData<List<MovieItem>> searchMovies(String keyword) {
        return movieRepository.searchMovies(keyword);
    }

    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        return movieRepository.getMoviesFromFirebase();
    }
}