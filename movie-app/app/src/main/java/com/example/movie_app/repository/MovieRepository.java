package com.example.movie_app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.models.MovieResponse;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;

import java.util.List;

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

    public LiveData<MovieResponse> getSeriesMovies(int page) {
        MutableLiveData<MovieResponse> liveData = new MutableLiveData<>();
        apiService.getSeriesMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) liveData.setValue(response.body());
                else liveData.setValue(null);
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) { liveData.setValue(null); }
        });
        return liveData;
    }

    public LiveData<MovieResponse> getSingleMovies(int page) {
        MutableLiveData<MovieResponse> liveData = new MutableLiveData<>();
        apiService.getSingleMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) liveData.setValue(response.body());
                else liveData.setValue(null);
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) { liveData.setValue(null); }
        });
        return liveData;
    }

    public LiveData<List<Genre>> getGenres() {
        MutableLiveData<List<Genre>> liveData = new MutableLiveData<>();

        apiService.getGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public LiveData<MovieDetailResponse> getMovieDetail(String slug) {
        MutableLiveData<MovieDetailResponse> data = new MutableLiveData<>();

        apiService.getMovieDetail(slug).enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<MovieResponse> getMoviesByCategory(String slug, int page) {
        MutableLiveData<MovieResponse> data = new MutableLiveData<>();
        android.util.Log.d("API_Network", "Đang gọi API category, slug: " + slug); // Log bước gọi

        apiService.getMoviesByCategory(slug, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("API_Network", "Thành công! Nhận được: " + (response.body().getItems() != null ? response.body().getItems().size() : "null"));
                    data.setValue(response.body());
                } else {
                    android.util.Log.e("API_Network", "Server trả về lỗi: " + response.code() + " | URL: " + call.request().url());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                android.util.Log.e("API_Network", "Lỗi kết nối: " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<String> getRawApiResponse(String slug, int page) {
        MutableLiveData<String> rawData = new MutableLiveData<>();

        // Gọi API như bình thường nhưng dùng ResponseBody để lấy dữ liệu thô
        apiService.getMoviesByCategoryRaw(slug, page).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        android.util.Log.d("API_RAW_DATA", json); // In toàn bộ JSON ra Logcat
                        rawData.setValue(json);
                    } catch (Exception e) {
                        rawData.setValue("Lỗi đọc dữ liệu: " + e.getMessage());
                    }
                } else {
                    rawData.setValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                rawData.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
        return rawData;
    }

    public LiveData<MovieResponse> searchMovies(String keyword) {
        MutableLiveData<MovieResponse> data = new MutableLiveData<>();

        apiService.searchMovies(keyword).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}