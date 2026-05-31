package com.example.movie_app.network;

import com.example.movie_app.models.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1/movies/latest")
    Call<MovieResponse> getLatestMovies(@Query("page") int page);
}