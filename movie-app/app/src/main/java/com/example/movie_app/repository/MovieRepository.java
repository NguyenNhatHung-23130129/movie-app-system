package com.example.movie_app.repository;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.dao.MovieDao;
import com.example.movie_app.database.AppDatabase;
import com.example.movie_app.models.*;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TAG = "MOVIE_REPO_DEBUG";
    private final ApiService apiService;
    private final MovieDao movieDao;
    private final String FIREBASE_URL = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public MovieRepository(Application application) {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
        AppDatabase db = AppDatabase.getDatabase(application);
        this.movieDao = db.movieDao();
    }

    public LiveData<List<MovieItem>> getMoviesFromFirebase() {
        return movieDao.getAllMovies();
    }

    public void syncDataFromFirebase() {
        FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        new Thread(() -> {
                            List<MovieItem> list = new ArrayList<>();
                            for (DataSnapshot child : snapshot.getChildren()) {
                                MovieItem movie = child.getValue(MovieItem.class);
                                if (movie != null) {
                                    if (movie.getId() == null || movie.getId().isEmpty()) {
                                        movie.setId(child.getKey());
                                    }
                                    list.add(movie);
                                }
                            }

                            if (!list.isEmpty()) {
                                movieDao.deleteAll();
                                movieDao.insertAll(list);
                                Log.d(TAG, "Đã đồng bộ " + list.size() + " phim vào Room.");
                            } else {
                                Log.w(TAG, "Không tìm thấy phim nào trên Firebase.");
                            }
                        }).start();
                    }

                    @Override public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Lỗi sync Firebase: " + error.getMessage());
                    }
                });
    }

    public void addMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies");
        String id = ref.push().getKey();
        if (id != null) {
            movie.setId(id);
            ref.child(id).setValue(movie).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    new Thread(() -> movieDao.insert(movie)).start();
                }
                if (listener != null) listener.onComplete(task);
            });
        }
    }

    public void updateMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        if (movie.getId() == null || movie.getId().isEmpty()) return;
        DatabaseReference ref = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies").child(movie.getId());
        ref.setValue(movie).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new Thread(() -> movieDao.update(movie)).start();
            }
            if (listener != null) listener.onComplete(task);
        });
    }

    public void deleteMovie(MovieItem movie, OnCompleteListener<Void> listener) {
        if (movie.getId() == null || movie.getId().isEmpty()) return;
        DatabaseReference ref = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies").child(movie.getId());
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new Thread(() -> movieDao.delete(movie)).start();
            }
            if (listener != null) listener.onComplete(task);
        });
    }

    public LiveData<List<MovieItem>> getMoviesByPath(String path, String slug, String typeFilter) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        DatabaseReference movieListRef = "by_category".equals(path) ? rootRef.child(path).child(slug).child("movies") : rootRef.child(path).child(slug);
        movieListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                List<String> movieSlugs = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) movieSlugs.add(child.getKey());
                if (movieSlugs.isEmpty()) { liveData.postValue(new ArrayList<>()); return; }
                List<MovieItem> filteredList = new ArrayList<>();
                final int[] count = {0};
                for (String movieSlug : movieSlugs) {
                    rootRef.child("movies").child(movieSlug).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(DataSnapshot movieSnapshot) {
                            MovieItem movie = movieSnapshot.getValue(MovieItem.class);
                            if (movie != null) {
                                if (movie.getId() == null || movie.getId().isEmpty()) movie.setId(movieSnapshot.getKey());
                                if (typeFilter == null || typeFilter.isEmpty() || typeFilter.equalsIgnoreCase(movie.getType())) filteredList.add(movie);
                            }
                            count[0]++;
                            if (count[0] == movieSlugs.size()) liveData.postValue(filteredList);
                        }
                        @Override public void onCancelled(DatabaseError error) { count[0]++; if (count[0] == movieSlugs.size()) liveData.postValue(filteredList); }
                    });
                }
            }
            @Override public void onCancelled(DatabaseError error) { liveData.postValue(null); }
        });
        return liveData;
    }

    public LiveData<MovieDetailResponse> getMovieDetail(String slug) {
        MutableLiveData<MovieDetailResponse> liveData = new MutableLiveData<>();
        apiService.getMovieDetail(slug).enqueue(new Callback<MovieDetailResponse>() {
            @Override public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) { liveData.postValue(response.isSuccessful() ? response.body() : null); }
            @Override public void onFailure(Call<MovieDetailResponse> call, Throwable t) { liveData.postValue(null); }
        });
        return liveData;
    }

    public LiveData<List<MovieItem>> getMoviesByListSlugs(List<String> movieSlugs) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        if (movieSlugs == null || movieSlugs.isEmpty()) { liveData.postValue(new ArrayList<>()); return liveData; }
        List<MovieItem> movieList = new ArrayList<>();
        DatabaseReference moviesRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies");
        final int[] count = {0};
        for (String slug : movieSlugs) {
            moviesRef.child(slug).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MovieItem movie = snapshot.getValue(MovieItem.class);
                    if (movie != null) {
                        if (movie.getId() == null || movie.getId().isEmpty()) movie.setId(snapshot.getKey());
                        movieList.add(movie);
                    }
                    count[0]++;
                    if (count[0] == movieSlugs.size()) liveData.postValue(movieList);
                }
                @Override public void onCancelled(@NonNull DatabaseError error) { count[0]++; if (count[0] == movieSlugs.size()) liveData.postValue(movieList); }
            });
        }
        return liveData;
    }

    public LiveData<List<Category>> getGenres() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        FirebaseDatabase.getInstance(FIREBASE_URL).getReference("by_category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category cat = categorySnapshot.getValue(Category.class);
                    if (cat != null) {
                        if (cat.getSlug() == null) cat.setSlug(categorySnapshot.getKey());
                        list.add(cat);
                    }
                }
                liveData.postValue(list);
            }
            @Override public void onCancelled(DatabaseError error) { liveData.postValue(new ArrayList<>()); }
        });
        return liveData;
    }

    public LiveData<List<MovieItem>> searchMovies(String keyword) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        apiService.searchMovies(keyword).enqueue(new Callback<List<MovieItem>>() {
            @Override public void onResponse(Call<List<MovieItem>> call, Response<List<MovieItem>> res) { liveData.postValue(res.isSuccessful() ? res.body() : null); }
            @Override public void onFailure(Call<List<MovieItem>> call, Throwable t) { liveData.postValue(null); }
        });
        return liveData;
    }
    
    private LiveData<List<MovieItem>> handleListResponse(Call<KKPhimListResponse> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimListResponse>() {
            @Override public void onResponse(Call<KKPhimListResponse> call, Response<KKPhimListResponse> res) { if (res.isSuccessful() && res.body() != null) liveData.postValue(res.body().getItems()); }
            @Override public void onFailure(Call<KKPhimListResponse> call, Throwable t) { liveData.postValue(null); }
        });
        return liveData;
    }

    private LiveData<List<MovieItem>> handleV1Response(Call<KKPhimV1Response> call) {
        MutableLiveData<List<MovieItem>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<KKPhimV1Response>() {
            @Override public void onResponse(Call<KKPhimV1Response> call, Response<KKPhimV1Response> res) { if (res.isSuccessful() && res.body() != null && res.body().getData() != null) liveData.postValue(res.body().getData().getItems()); }
            @Override public void onFailure(Call<KKPhimV1Response> call, Throwable t) { liveData.postValue(null); }
        });
        return liveData;
    }
    
    public LiveData<List<MovieItem>> getLatestMovies(int page) { return handleListResponse(apiService.getLatestMovies(page)); }
    public LiveData<List<MovieItem>> getSeriesMovies(int page) { return handleV1Response(apiService.getSeriesMovies(page)); }
    public LiveData<List<MovieItem>> getSingleMovies(int page) { return handleV1Response(apiService.getSingleMovies(page)); }
}