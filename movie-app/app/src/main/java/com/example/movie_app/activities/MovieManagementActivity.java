package com.example.movie_app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.AdminMovieAdapter;
import com.example.movie_app.models.MovieDetailResponse;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.utils.AdminNavigationHelper;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieManagementActivity extends BaseAdminActivity {
    private TextView btnFilterAll, btnFilterPublished, btnFilterDraft;
    private ImageButton fabAddMovie;
    private EditText edtSearchMovie;
    private RecyclerView rcvMovieList;

    private MovieViewModel movieViewModel;
    private AdminMovieAdapter adapter;
    private List<MovieItem> fullMovieList = new ArrayList<>();
    private String currentFilterStatus = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_movie);

        initViews();
        setupAdminInfo();
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

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.refreshData();
        movieViewModel.getMoviesFromFirebase().observe(this, movies -> {
            if (movies != null) {
                fullMovieList = movies;
                applyFilters();
            }
        });
    }

    private void setupSearch() {
        edtSearchMovie.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        btnFilterAll.setOnClickListener(v -> {
            currentFilterStatus = "ALL";
            updateFilterUI(btnFilterAll);
            applyFilters();
        });
        btnFilterPublished.setOnClickListener(v -> {
            currentFilterStatus = "published";
            updateFilterUI(btnFilterPublished);
            applyFilters();
        });
        btnFilterDraft.setOnClickListener(v -> {
            currentFilterStatus = "draft";
            updateFilterUI(btnFilterDraft);
            applyFilters();
        });
        fabAddMovie.setOnClickListener(v -> showAddEditMovieDialog(null));
    }

    private void applyFilters() {
        String searchQuery = edtSearchMovie.getText().toString().trim().toLowerCase();
        
        List<MovieItem> filteredList = fullMovieList.stream()
                .filter(movie -> {
                    boolean matchesStatus = currentFilterStatus.equals("ALL") ||
                            (movie.getStatus() != null && movie.getStatus().equalsIgnoreCase(currentFilterStatus));
                    
                    boolean matchesQuery = searchQuery.isEmpty() ||
                            (movie.getName() != null && movie.getName().toLowerCase().contains(searchQuery)) ||
                            (movie.getOriginName() != null && movie.getOriginName().toLowerCase().contains(searchQuery));
                    
                    return matchesStatus && matchesQuery;
                })
                .collect(Collectors.toList());
        
        adapter.setMovieList(filteredList);
    }

    private void updateFilterUI(TextView activeBtn) {
        int inactiveBg = R.drawable.bg_filter_inactive;
        int inactiveTextColor = getResources().getColor(R.color.colorTextSecondary);
        
        btnFilterAll.setBackgroundResource(inactiveBg);
        btnFilterAll.setTextColor(inactiveTextColor);
        btnFilterPublished.setBackgroundResource(inactiveBg);
        btnFilterPublished.setTextColor(inactiveTextColor);
        btnFilterDraft.setBackgroundResource(inactiveBg);
        btnFilterDraft.setTextColor(inactiveTextColor);

        activeBtn.setBackgroundResource(R.drawable.bg_filter_active);
        activeBtn.setTextColor(getResources().getColor(R.color.white));
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
        Spinner spnStatus = dialogView.findViewById(R.id.spn_movie_status);
        Button btnFetchApi = dialogView.findViewById(R.id.btn_fetch_api);

        String[] statusOptions = {"Xuất bản", "Bản nháp"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(statusAdapter);

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
            edtSlug.setEnabled(false);
            btnFetchApi.setVisibility(View.GONE);

            if ("draft".equalsIgnoreCase(movie.getStatus())) {
                spnStatus.setSelection(1);
            } else {
                spnStatus.setSelection(0);
            }
        } else {
            tvTitle.setText("Thêm phim mới");
            btnFetchApi.setVisibility(View.VISIBLE);
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String slug = edtSlug.getText().toString().trim();
            if (name.isEmpty() || slug.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên và slug", Toast.LENGTH_SHORT).show();
                return;
            }

            MovieItem movieToSave = isEdit ? movie : new MovieItem();
            movieToSave.setName(name);
            movieToSave.setSlug(slug);
            movieToSave.setOriginName(edtOriginName.getText().toString().trim());
            movieToSave.setYear(edtYear.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtYear.getText().toString()));
            movieToSave.setPosterUrl(edtPoster.getText().toString().trim());
            movieToSave.setThumbUrl(edtThumb.getText().toString().trim());
            movieToSave.setType(edtType.getText().toString().trim());
            
            String selectedStatus = spnStatus.getSelectedItemPosition() == 0 ? "published" : "draft";
            movieToSave.setStatus(selectedStatus);

            if (isEdit) {
                movieViewModel.updateMovie(movieToSave, task -> {
                    if (task.isSuccessful()) Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                });
            } else {
                movieViewModel.addMovieWithSlug(movieToSave, task -> {
                    if (task != null && task.isSuccessful()) Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                });
            }
        });

        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        if (!isEdit) {
            btnFetchApi.setOnClickListener(v -> {
                String slugInput = edtSlug.getText().toString().trim();
                if (slugInput.isEmpty()) return;
                movieViewModel.getMovieDetail(slugInput).observe(this, response -> {
                    if (response != null && response.getMovie() != null) {
                        MovieDetailResponse.MovieDetail detail = response.getMovie();
                        edtName.setText(detail.getName());
                        edtYear.setText(String.valueOf(detail.getYear()));
                        edtType.setText(detail.getType());
                        String img = detail.getPosterUrl();
                        if (img != null && !img.startsWith("http")) img = "https://phimimg.com/" + img;
                        edtPoster.setText(img);
                        edtThumb.setText(img);
                    }
                });
            });
        }
    }

    private void showDeleteConfirmDialog(MovieItem movie) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phim '" + movie.getName() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    movieViewModel.deleteMovie(movie, task -> {
                        if (task.isSuccessful()) Toast.makeText(this, "Đã xóa phim", Toast.LENGTH_SHORT).show();
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
