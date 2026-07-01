package com.example.movie_app.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.movie_app.entity.FavoriteEntity;
import com.example.movie_app.entity.WatchHistoryEntity;
import com.example.movie_app.models.*;
import com.example.movie_app.repository.HistoryRepository;
import com.example.movie_app.repository.MovieRepository;
import com.example.movie_app.utils.RecommendationEngine;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final HistoryRepository historyRepository;
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();

    public MovieViewModel(@NonNull Application application) {
        super(application);
        this.movieRepository = new MovieRepository(application);
        this.historyRepository = new HistoryRepository(application);
    }

    public LiveData<List<MovieItem>> getMoviesByPath(String path, String slug, String typeFilter) {
        return movieRepository.getMoviesByPath(path, slug, typeFilter);
    }

    public void setCurrentUser(String userId) {
        if (!userId.equals(currentUserId.getValue())) {
            currentUserId.setValue(userId);
        }
    }

    public LiveData<List<Category>> getGenres() { return movieRepository.getGenres(); }
    public LiveData<MovieDetailResponse> getMovieDetail(String slug) { return movieRepository.getMovieDetail(slug); }
    public LiveData<List<MovieItem>> searchMovies(String keyword) { return movieRepository.searchMovies(keyword); }
    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        LiveData<List<MovieItem>> data = movieRepository.getMoviesFromFirebase();
        return data;
    }

    public void addMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        movieRepository.addMovie(movie, listener);
    }
    public void addMovieWithSlug(MovieItem movie, OnCompleteListener<Void> listener) {
        movieRepository.addMovieWithSlug(movie, listener);
    }

    public void updateMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        movieRepository.updateMovie(movie, listener);
    }

    public void deleteMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        movieRepository.deleteMovie(movie, listener);
    }

    public LiveData<List<MovieItem>> getRelatedMovies(String currentSlug, List<Category> categories) {
        MediatorLiveData<List<MovieItem>> result = new MediatorLiveData<>();
        String categorySlug = (categories != null && !categories.isEmpty()) ? categories.get(0).getSlug() : "";

        result.addSource(movieRepository.getMoviesByPath("by_category", categorySlug, ""), movies -> {
            if (movies != null) {
                String seriesPrefix = currentSlug.split("-")[0];
                movies.sort((m1, m2) -> {
                    boolean m1Series = m1.getSlug().contains(seriesPrefix);
                    boolean m2Series = m2.getSlug().contains(seriesPrefix);
                    return m1Series == m2Series ? 0 : (m1Series ? -1 : 1);
                });
                result.setValue(movies);
            }
        });
        return result;
    }

    public LiveData<List<MovieItem>> getPersonalizedRecommendations(Context context) {
        return Transformations.switchMap(currentUserId, userId -> {
            if ("GUEST".equals(userId)) {
                return new MutableLiveData<>(new ArrayList<>());
            }

            return Transformations.switchMap(historyRepository.getHistory(userId), history -> {
                MediatorLiveData<List<MovieItem>> result = new MediatorLiveData<>();

                if (history == null || history.isEmpty()) {
                    result.setValue(new ArrayList<>());
                    return result;
                }

                LinkedHashSet<String> uniqueIds = new LinkedHashSet<>();
                for (int i = history.size() - 1; i >= 0; i--) {
                    uniqueIds.add(history.get(i).movieId);
                    if (uniqueIds.size() >= 10) break;
                }
                List<String> suggestedIds = new RecommendationEngine(context)
                        .getRecommendations(new ArrayList<>(uniqueIds), 8);

                if (suggestedIds.isEmpty()) {
                    result.setValue(new ArrayList<>());
                } else {
                    result.addSource(movieRepository.getMoviesByListSlugs(suggestedIds), result::setValue);
                }
                return result;
            });
        });
    }

    public void saveToHistory(String movieId, String userId, String name, String poster) {
        historyRepository.insertHistory(new WatchHistoryEntity(movieId, userId, name, poster, System.currentTimeMillis(), 0));
    }

    public void refreshData() {
        movieRepository.syncDataFromFirebase();
    }
    public LiveData<Boolean> isFavorite(String userId, String movieId) {
        return movieRepository.isFavorite(userId, movieId);
    }

    public void addToFavorites(String userId, MovieDetailResponse.MovieDetail info, String finalImageUrl) {
        FavoriteEntity fav = new FavoriteEntity(
                userId,
                info.getId(),
                info.getName(),
                finalImageUrl,
                System.currentTimeMillis()
        );
        movieRepository.addFavorite(fav);
    }

    public void removeFromFavorites(String userId, String movieId) {
        movieRepository.removeFavorite(userId, movieId);
    }

    public LiveData<List<WatchHistoryEntity>> getHistory(String userId) {
        return historyRepository.getHistory(userId);
    }

    public LiveData<Integer> getWatchedCount(String userId) {
        return historyRepository.getWatchedCount(userId);
    }

    public LiveData<Long> getTotalWatchTime(String userId) {
        return historyRepository.getTotalWatchTime(userId);
    }

    public LiveData<Integer> getFavoriteCount(String userId) {
        return movieRepository.getFavoriteCount(userId);
    }
}