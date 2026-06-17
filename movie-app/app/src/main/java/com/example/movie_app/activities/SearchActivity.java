package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

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

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        edtSearch = findViewById(R.id.edtSearch);
        rvSearchResult = findViewById(R.id.rvSearchResult);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        searchAdapter = new MovieAdapter(new ArrayList<>());
        rvSearchResult.setLayoutManager(new GridLayoutManager(this, 3));
        rvSearchResult.setAdapter(searchAdapter);

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = edtSearch.getText().toString();
            if (!keyword.isEmpty()) {
                performSearch(keyword);
            }
            return true;
        });

        searchAdapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(SearchActivity.this, MovieDetailActivity.class);
            intent.putExtra("movie_slug", movie.getSlug());
            startActivity(intent);
        });
    }

    private void performSearch(String keyword) {
        Log.d("SEARCH_DEBUG", "Đang gọi tới URL: " + "http://192.168.1.5:8080/api/v1/movies/search?keyword=" + keyword);
        movieViewModel.searchMovies(keyword).observe(this, res -> {
            if (res != null) searchAdapter.setMovieList(res.getItems());
        });
    }
}
