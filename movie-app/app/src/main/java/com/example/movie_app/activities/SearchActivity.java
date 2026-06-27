package com.example.movie_app.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.adapter.SearchHistoryAdapter;
import com.example.movie_app.utils.SearchHistoryManager;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView rvSearchResult, rvSuggestions;
    private MovieAdapter searchAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    private MovieViewModel movieViewModel;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private boolean isFromHistoryClick = false;

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
        rvSuggestions = findViewById(R.id.rvSuggestions);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        searchAdapter = new MovieAdapter(new ArrayList<>());
        rvSearchResult.setLayoutManager(new GridLayoutManager(this, 3));
        rvSearchResult.setAdapter(searchAdapter);

        searchHistoryAdapter = new SearchHistoryAdapter(new ArrayList<>(), new SearchHistoryAdapter.OnHistoryClickListener() {
            @Override
            public void onHistoryClick(String query) {
                isFromHistoryClick = true;
                edtSearch.setText(query);
                edtSearch.setSelection(query.length());
                executeSearch(query);
            }

            @Override
            public void onDeleteClick(String query) {
                List<String> currentList = SearchHistoryManager.getHistory(SearchActivity.this);
                currentList.remove(query);
                SearchHistoryManager.saveHistoryList(SearchActivity.this, currentList);
                searchHistoryAdapter.updateList(currentList, true);
            }
        });
        rvSuggestions.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestions.setAdapter(searchHistoryAdapter);

        edtSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showSuggestionsLayer();
        });
        edtSearch.requestFocus();
    }

    private void showSuggestionsLayer() {
        List<String> historyList = SearchHistoryManager.getHistory(this);
        searchHistoryAdapter.updateList(historyList, true);
        rvSuggestions.setVisibility(View.VISIBLE);
        rvSearchResult.setVisibility(View.GONE);
    }

    private void executeSearch(String keyword) {
        SearchHistoryManager.saveSearch(this, keyword);

        rvSuggestions.setVisibility(View.GONE);
        rvSearchResult.setVisibility(View.VISIBLE);

        performSearch(keyword);
        hideKeyboard();
    }

    private void performSearch(String keyword) {
        movieViewModel.searchMovies(keyword).observe(this, movieList -> {
            if (movieList != null && !movieList.isEmpty()) {
                searchAdapter.setMovieList(movieList);
            } else {
                searchAdapter.setMovieList(new ArrayList<>());
                Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchLogic() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = edtSearch.getText().toString().trim();
                if (!keyword.isEmpty()) executeSearch(keyword);
                return true;
            }
            return false;
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacks(searchRunnable);
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (isFromHistoryClick) { isFromHistoryClick = false; return; }

                String query = s.toString().trim();
                if (query.isEmpty()) {
                    showSuggestionsLayer();
                } else {
                    rvSuggestions.setVisibility(View.VISIBLE);
                    rvSearchResult.setVisibility(View.GONE);

                    searchHandler.removeCallbacks(searchRunnable);
                    searchRunnable = () -> {
                        fetchMovieSuggestions(query);
                    };
                    searchHandler.postDelayed(searchRunnable, 600);
                }
            }
        });
    }

    private void fetchMovieSuggestions(String keyword) {
        movieViewModel.searchMovies(keyword).observe(this, movieList -> {
            List<String> suggestionTitles = new ArrayList<>();

            if (movieList != null && !movieList.isEmpty()) {
                for (int i = 0; i < Math.min(movieList.size(), 5); i++) {
                    suggestionTitles.add(movieList.get(i).getName());
                }
            }
            searchHistoryAdapter.updateList(suggestionTitles, false);
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