package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.MovieResponse;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiService apiService;

    public MovieRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<MovieResponse> getLatestMovies(int page) {
        MutableLiveData<MovieResponse> liveData = new MutableLiveData<>();

        apiService.getLatestMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}