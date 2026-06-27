package com.example.movie_app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
                showAddEditMovieDialog(movie);
            }

            @Override
            public void onDelete(MovieItem movie) {
                showDeleteConfirmDialog(movie);
            }
        });
    }



    private void showAddEditMovieDialog(MovieItem movie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_movie, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        EditText edtName = dialogView.findViewById(R.id.edt_movie_name);
        EditText edtSlug = dialogView.findViewById(R.id.edt_movie_slug);
        EditText edtOriginName = dialogView.findViewById(R.id.edt_movie_origin_name);
        EditText edtYear = dialogView.findViewById(R.id.edt_movie_year);
        EditText edtPoster = dialogView.findViewById(R.id.edt_movie_poster);
        EditText edtThumb = dialogView.findViewById(R.id.edt_movie_thumb);
        EditText edtType = dialogView.findViewById(R.id.edt_movie_type);

        boolean isEdit = (movie != null);
        if (isEdit) {
            tvTitle.setText("Chỉnh sửa phim");
            edtName.setText(movie.getName());
            edtSlug.setText(movie.getSlug());
            edtOriginName.setText(movie.getOriginName());
            edtYear.setText(String.valueOf(movie.getYear()));
            edtPoster.setText(movie.getPosterUrl());
            edtThumb.setText(movie.getThumbUrl());
            edtType.setText(movie.getType());
        } else {
            tvTitle.setText("Thêm phim mới");
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String slug = edtSlug.getText().toString().trim();
            String originName = edtOriginName.getText().toString().trim();
            String yearStr = edtYear.getText().toString().trim();
            String poster = edtPoster.getText().toString().trim();
            String thumb = edtThumb.getText().toString().trim();
            String type = edtType.getText().toString().trim();

            if (name.isEmpty() || slug.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên và slug", Toast.LENGTH_SHORT).show();
                return;
            }

            MovieItem movieToSave = isEdit ? movie : new MovieItem();
            movieToSave.setName(name);
            movieToSave.setSlug(slug);
            movieToSave.setOriginName(originName);
            movieToSave.setYear(yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr));
            movieToSave.setPosterUrl(poster);
            movieToSave.setThumbUrl(thumb);
            movieToSave.setType(type);

            if (isEdit) {
                movieViewModel.updateMovie(movieToSave, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                movieViewModel.addMovie(movieToSave, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Thêm phim thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm phim thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        
        Log.d(TAG, "Bắt đầu đồng bộ dữ liệu từ Firebase...");
        movieViewModel.refreshData(); // Kích hoạt sync
        
        movieViewModel.getMoviesFromFirebase().observe(this, movies -> {
            if (movies != null) {
                Log.d(TAG, "Đã nhận dữ liệu từ Room: " + movies.size() + " phim");
                fullMovieList = movies;
                adapter.setMovieList(movies);
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
        fabAddMovie.setOnClickListener(v-> showAddEditMovieDialog(null));
    }

    private void updateFilterUI(TextView activeBtn) {
        // Reset tất cả các nút về trạng thái inactive
        int inactiveBg = R.drawable.bg_filter_inactive;
        int inactiveTextColor = getResources().getColor(R.color.colorTextSecondary);

        btnFilterAll.setBackgroundResource(inactiveBg);
        btnFilterAll.setTextColor(inactiveTextColor);
        btnFilterPublished.setBackgroundResource(inactiveBg);
        btnFilterPublished.setTextColor(inactiveTextColor);
        btnFilterDraft.setBackgroundResource(inactiveBg);
        btnFilterDraft.setTextColor(inactiveTextColor);

        // Kích hoạt nút đang chọn
        activeBtn.setBackgroundResource(R.drawable.bg_filter_active);
        activeBtn.setTextColor(getResources().getColor(R.color.white));
    }

    private void showDeleteConfirmDialog(MovieItem movie) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phim '" + movie.getName() + "'? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    movieViewModel.deleteMovie(movie, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đã xóa phim", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}