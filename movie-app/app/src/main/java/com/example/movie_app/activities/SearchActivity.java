package com.example.movie_app.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView rvSearchResult;
    private MovieAdapter searchAdapter;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupSearchLogic();
    }

    private void initViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        edtSearch = findViewById(R.id.edtSearch);
        rvSearchResult = findViewById(R.id.rvSearchResult);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        searchAdapter = new MovieAdapter(new ArrayList<>());
        rvSearchResult.setLayoutManager(new GridLayoutManager(this, 3));
        rvSearchResult.setAdapter(searchAdapter);

        edtSearch.requestFocus();
    }

    private void setupSearchLogic() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = edtSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    performSearch(keyword);
                    hideKeyboard();
                } else {
                    Toast.makeText(this, "Vui lòng nhập tên phim", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    private void performSearch(String keyword) {
        Log.d("SEARCH_DEBUG", "Bắt đầu tìm kiếm với từ khóa: " + keyword);
        movieViewModel.searchMovies(keyword).observe(this, movieList -> {
            if (movieList != null) {
                Log.d("SEARCH_DEBUG", "Số lượng phim tìm thấy: " + movieList.size());
                searchAdapter.setMovieList(movieList);
            } else {
                Log.e("SEARCH_DEBUG", "API trả về NULL hoặc lỗi!");
                searchAdapter.setMovieList(new ArrayList<>());
                Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}