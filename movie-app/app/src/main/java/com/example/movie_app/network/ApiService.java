package com.example.movie_app.network;

import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1/movies/latest")
    Call<MovieResponse> getLatestMovies(@Query("page") int page);

    @GET("api/v1/movies/series")
    Call<MovieResponse> getSeriesMovies(@Query("page") int page);

    @GET("api/v1/movies/single")
    Call<MovieResponse> getSingleMovies(@Query("page") int page);

    @GET("api/v1/movies/genres")
    Call<List<Genre>> getGenres();

    @GET("api/v1/movies/detail/{slug}")
    Call<MovieDetailResponse> getMovieDetail(@Path("slug") String slug);

    @GET("api/v1/movies/genres/{slug}")
    Call<MovieResponse> getMoviesByCategory(@Path("slug") String slug, @Query("page") int page);
}