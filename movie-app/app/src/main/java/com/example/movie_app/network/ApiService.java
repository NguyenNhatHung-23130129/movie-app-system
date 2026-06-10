package com.example.movie_app.network;

import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
}