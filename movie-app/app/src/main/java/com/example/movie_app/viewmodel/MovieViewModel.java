package com.example.movie_app.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.movie_app.models.*;
import com.example.movie_app.repository.MovieRepository;
import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;

    public MovieViewModel() {
        this.movieRepository = new MovieRepository();
    }


    public LiveData<List<MovieItem>> getMoviesByPath(String path, String slug, String typeFilter) {
        return movieRepository.getMoviesByPath(path, slug, typeFilter);
    }

    public LiveData<List<Category>> getGenres() {
        return movieRepository.getGenres();
    }

    public LiveData<MovieDetailResponse> getMovieDetail(String slug) {
        return movieRepository.getMovieDetail(slug);
    }

    public LiveData<List<MovieItem>> searchMovies(String keyword) {
        Log.d("SEARCH_DEBUG", "ViewModel nhận yêu cầu tìm: " + keyword);
        return movieRepository.searchMovies(keyword);
    }

    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        return movieRepository.getMoviesFromFirebase();
    }
}