package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.*;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiService apiService;
    private final String FIREBASE_URL = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public MovieRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<List<MovieItem>> getLatestMovies(int page) {
        return handleListResponse(apiService.getLatestMovies(page));
    }

    public LiveData<List<MovieItem>> getSeriesMovies(int page) {
        return handleV1Response(apiService.getSeriesMovies(page));
    }

    public LiveData<List<MovieItem>> getMoviesByCategory(String slug, int page) {
        return handleV1Response(apiService.getMoviesByCategory(slug, page));
    }

    private LiveData<List<MovieItem>> handleListResponse(Call<KKPhimListResponse> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimListResponse>() {
            @Override public void onResponse(Call<KKPhimListResponse> call, Response<KKPhimListResponse> res) {
                if (res.isSuccessful() && res.body() != null) liveData.setValue(res.body().getItems());
            }
            @Override public void onFailure(Call<KKPhimListResponse> call, Throwable t) { liveData.setValue(null); }
        });
        return liveData;
    }

    private LiveData<List<MovieItem>> handleV1Response(Call<KKPhimV1Response> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimV1Response>() {
            @Override public void onResponse(Call<KKPhimV1Response> call, Response<KKPhimV1Response> res) {
                if (res.isSuccessful() && res.body() != null && res.body().getData() != null)
                    liveData.setValue(res.body().getData().getItems());
            }
            @Override public void onFailure(Call<KKPhimV1Response> call, Throwable t) { liveData.setValue(null); }
        });
        return liveData;
    }

    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        List<MovieItem> list = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            list.add(child.getValue(MovieItem.class));
                        }
                        liveData.setValue(list);
                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });
        return liveData;
    }
    // --- CÁC HÀM API BỔ SUNG ---

    public LiveData<List<MovieItem>> getSingleMovies(int page) {
        return handleV1Response(apiService.getSingleMovies(page));
    }

    public LiveData<List<Category>> getGenres() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        apiService.getGenres().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<MovieDetailResponse> getMovieDetail(String slug) {
        MutableLiveData<MovieDetailResponse> liveData = new MutableLiveData<>();
        apiService.getMovieDetail(slug).enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<List<MovieItem>> searchMovies(String keyword) {
        return handleV1Response(apiService.searchMovies(keyword));
    }
}