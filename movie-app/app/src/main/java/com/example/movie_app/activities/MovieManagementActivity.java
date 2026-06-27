package com.example.movie_app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.AdminMovieAdapter;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieManagementActivity extends AppCompatActivity {
    private static final String TAG = "ADMIN_MOVIE";

    private TextView btnFilterAll, btnFilterPublished, btnFilterDraft;
    private ImageButton fabAddMovie;
    private EditText edtSearchMovie;
    private RecyclerView rcvMovieList;
    
    private MovieViewModel movieViewModel;
    private AdminMovieAdapter adapter;
    private List<MovieItem> fullMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_movie);
        
        initViews();
        setupRecyclerView();
        setupViewModel();
        setupClickListeners();
        setupSearch();
        
        AdminNavigationHelper.setupAdminBottomNavigation(this);
    }

    private void initViews() {
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterPublished = findViewById(R.id.btn_filter_published);
        btnFilterDraft = findViewById(R.id.btn_filter_draft);
        fabAddMovie = findViewById(R.id.fab_rating);
        edtSearchMovie = findViewById(R.id.edt_search_movie);
        rcvMovieList = findViewById(R.id.rcv_movie_list);
    }

    private void setupRecyclerView() {
        adapter = new AdminMovieAdapter();
        rcvMovieList.setLayoutManager(new LinearLayoutManager(this));
        rcvMovieList.setAdapter(adapter);

        adapter.setOnMovieActionListener(new AdminMovieAdapter.OnMovieActionListener() {
            @Override
            public void onEdit(MovieItem movie) {
                Toast.makeText(MovieManagementActivity.this, "Sửa: " + movie.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(MovieItem movie) {
                Toast.makeText(MovieManagementActivity.this, "Xóa: " + movie.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        
        Log.d(TAG, "Bắt đầu đồng bộ dữ liệu từ Firebase...");
        movieViewModel.refreshData(); // Kích hoạt sync
        
        movieViewModel.getMoviesFromFirebase().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                Log.d(TAG, "Đã nhận dữ liệu từ Room: " + movies.size() + " phim");
                fullMovieList = movies;
                adapter.setMovieList(movies);
            } else {
                Log.w(TAG, "Dữ liệu phim trong Room đang trống!");
            }
        });
    }

    private void filterMovies(String query) {
        if (query.isEmpty()) {
            adapter.setMovieList(fullMovieList);
        } else {
            List<MovieItem> filteredList = fullMovieList.stream()
                    .filter(movie -> movie.getName() != null && movie.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            adapter.setMovieList(filteredList);
        }
    }

    private void setupSearch() {
        edtSearchMovie.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        btnFilterAll.setOnClickListener(v-> {
            updateFilterUI(btnFilterAll);
            adapter.setMovieList(fullMovieList);
        });

        btnFilterPublished.setOnClickListener(v-> updateFilterUI(btnFilterPublished));
        btnFilterDraft.setOnClickListener(v-> updateFilterUI(btnFilterDraft));
        fabAddMovie.setOnClickListener(v-> Toast.makeText(this, "Thêm phim mới", Toast.LENGTH_SHORT).show());
    }

    private void updateFilterUI(TextView activeBtn) {
        btnFilterAll.setBackgroundResource(R.drawable.bg_filter_inactive);
        btnFilterPublished.setBackgroundResource(R.drawable.bg_filter_inactive);
        btnFilterDraft.setBackgroundResource(R.drawable.bg_filter_inactive);
        activeBtn.setBackgroundResource(R.drawable.bg_filter_active);
    }
}