package com.example.movie_app.repository;

import android.util.Log;

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
    private static final String TAG = "MOVIE_REPO_DEBUG";
    private final ApiService apiService;
    private final String FIREBASE_URL = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public MovieRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private LiveData<List<MovieItem>> handleListResponse(Call<KKPhimListResponse> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimListResponse>() {
            @Override public void onResponse(Call<KKPhimListResponse> call, Response<KKPhimListResponse> res) {
                if (res.isSuccessful() && res.body() != null) {
                    Log.d(TAG, "API List Success: " + res.body().getItems().size() + " items loaded.");
                    liveData.setValue(res.body().getItems());
                } else {
                    Log.e(TAG, "API List Error: " + res.code());
                }
            }
            @Override public void onFailure(Call<KKPhimListResponse> call, Throwable t) {
                Log.e(TAG, "API List Failure: " + t.getMessage());
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    private LiveData<List<MovieItem>> handleV1Response(Call<KKPhimV1Response> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimV1Response>() {
            @Override public void onResponse(Call<KKPhimV1Response> call, Response<KKPhimV1Response> res) {
                if (res.isSuccessful() && res.body() != null && res.body().getData() != null) {
                    Log.d(TAG, "API V1 Success: " + res.body().getData().getItems().size() + " items loaded.");
                    liveData.setValue(res.body().getData().getItems());
                } else {
                    Log.e(TAG, "API V1 Error: Empty data or response");
                }
            }
            @Override public void onFailure(Call<KKPhimV1Response> call, Throwable t) {
                Log.e(TAG, "API V1 Failure: " + t.getMessage());
                liveData.setValue(null);
            }
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
                            MovieItem movie = child.getValue(MovieItem.class);
                            if (movie != null) {
                                list.add(movie);
                            }
                        }
                        liveData.setValue(list);
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Firebase Failure: " + error.getMessage());
                    }
                });
        return liveData;
    }

    public LiveData<List<MovieItem>> getLatestMovies(int page) {
        return handleListResponse(apiService.getLatestMovies(page));
    }

    public LiveData<List<MovieItem>> getSeriesMovies(int page) {
        return handleV1Response(apiService.getSeriesMovies(page));
    }

    public LiveData<List<MovieItem>> getMoviesByPath(String path, String slug, String typeFilter) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();

        DatabaseReference movieListRef;
        if ("by_category".equals(path)) {
            movieListRef = rootRef.child(path).child(slug).child("movies");
        } else {
            movieListRef = rootRef.child(path).child(slug);
        }

        movieListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> movieSlugs = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    movieSlugs.add(child.getKey());
                }

                if (movieSlugs.isEmpty()) {
                    liveData.setValue(new ArrayList<>());
                    return;
                }

                List<MovieItem> filteredList = new ArrayList<>();
                final int[] count = {0};

                for (String movieSlug : movieSlugs) {
                    rootRef.child("movies").child(movieSlug)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot movieSnapshot) {
                                    MovieItem movie = movieSnapshot.getValue(MovieItem.class);

                                    // KIỂM TRA LỌC: Chỉ thêm vào danh sách nếu type khớp với typeFilter
                                    if (movie != null) {
                                        if (typeFilter == null || typeFilter.isEmpty() ||
                                                typeFilter.equalsIgnoreCase(movie.getType())) {
                                            filteredList.add(movie);
                                        }
                                    }

                                    count[0]++;
                                    if (count[0] == movieSlugs.size()) {
                                        liveData.setValue(filteredList);
                                    }
                                }
                                @Override public void onCancelled(DatabaseError error) {
                                    count[0]++;
                                    if (count[0] == movieSlugs.size()) liveData.setValue(filteredList);
                                }
                            });
                }
            }
            @Override public void onCancelled(DatabaseError error) { liveData.setValue(null); }
        });
        return liveData;
    }

    public LiveData<List<MovieItem>> getSingleMovies(int page) {
        return handleV1Response(apiService.getSingleMovies(page));
    }

    public LiveData<List<Category>> getGenres() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();

        FirebaseDatabase.getInstance(FIREBASE_URL).getReference("by_category")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Category> list = new ArrayList<>();

                        for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                            String slug = categorySnapshot.getKey();
                            String name = categorySnapshot.child("name").getValue(String.class);

                            if (slug != null && name != null) {
                                Category category = new Category();
                                category.setSlug(slug);
                                category.setName(name);
                                list.add(category);
                            }
                        }
                        liveData.setValue(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Lỗi Firebase: " + error.getMessage());
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