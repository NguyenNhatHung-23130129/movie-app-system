package com.example.movie_app.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.movie_app.models.*;
import com.example.movie_app.repository.MovieRepository;

import java.util.ArrayList;
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
        return movieRepository.searchMovies(keyword);
    }

    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        return movieRepository.getMoviesFromFirebase();
    }

    public LiveData<List<MovieItem>> getRelatedMovies(String currentSlug, List<Category> categories) {
        MutableLiveData<List<MovieItem>> relatedLiveData = new MutableLiveData<>();
        String categorySlug = (categories != null && !categories.isEmpty()) ? categories.get(0).getSlug() : "";

        movieRepository.getMoviesByPath("by_category", categorySlug, "").observeForever(movies -> {
            if (movies != null) {
                String seriesPrefix = currentSlug.split("-")[0];

                movies.sort((m1, m2) -> {
                    boolean m1Series = m1.getSlug().contains(seriesPrefix);
                    boolean m2Series = m2.getSlug().contains(seriesPrefix);

                    if (m1Series && !m2Series) return -1;
                    if (!m1Series && m2Series) return 1;
                    return 0;
                });
                relatedLiveData.setValue(movies);
            }
        });
        return relatedLiveData;
    }

    public LiveData<List<MovieItem>> getMoviesBySlugs(List<String> slugs) {
        return movieRepository.getMoviesByListSlugs(slugs);
    }
}