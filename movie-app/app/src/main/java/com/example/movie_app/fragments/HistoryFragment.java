package com.example.movie_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.adapter.HistoryAdapter;
import com.example.movie_app.database.AppDatabase;
import com.example.movie_app.models.MovieItem;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private LinearLayout tabHistory, tabFavorites;
    private RecyclerView rvMovieList;
    private AppDatabase db;
    private boolean isShowingHistory = true; // Theo dõi tab hiện tại

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);

        db = AppDatabase.getDatabase(requireContext());
        tabHistory = view.findViewById(R.id.tabHistory);
        tabFavorites = view.findViewById(R.id.tabFavorites);
        rvMovieList = view.findViewById(R.id.rvMovieList);

        rvMovieList.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Luôn làm mới dữ liệu khi quay lại tab này (sau khi Login/Logout)
        if (isShowingHistory) loadHistoryData();
        else loadFavoritesData();
    }

    private String getUserId() {
        return requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                .getString("USER_ID", "GUEST");
    }

    private void setupEvents() {
        tabHistory.setOnClickListener(v -> {
            isShowingHistory = true;
            tabHistory.setBackgroundResource(R.drawable.bg_btn_red);
            tabFavorites.setBackgroundResource(R.drawable.bg_btn_dark_square);
            loadHistoryData();
        });

        tabFavorites.setOnClickListener(v -> {
            isShowingHistory = false;
            tabFavorites.setBackgroundResource(R.drawable.bg_btn_red);
            tabHistory.setBackgroundResource(R.drawable.bg_btn_dark_square);
            loadFavoritesData();
        });
    }

    private void loadHistoryData() {
        String userId = getUserId();
        db.historyDao().getHistoryByUserId(userId).removeObservers(getViewLifecycleOwner());

        db.historyDao().getHistoryByUserId(userId).observe(getViewLifecycleOwner(), historyList -> {
            ArrayList<MovieItem> movieList = new ArrayList<>();
            if (historyList != null) {
                for (var history : historyList) {
                    movieList.add(new MovieItem(history.movieId, history.movieName, history.posterUrl));
                }
            }
            rvMovieList.setAdapter(new HistoryAdapter(requireContext(), movieList));
        });
    }

    private void loadFavoritesData() {
        String userId = getUserId();
        db.favoriteDao().getFavoritesByUserId(userId).removeObservers(getViewLifecycleOwner());

        db.favoriteDao().getFavoritesByUserId(userId).observe(getViewLifecycleOwner(), favoriteEntities -> {
            ArrayList<MovieItem> movieList = new ArrayList<>();
            if (favoriteEntities != null) {
                for (var fav : favoriteEntities) {
                    movieList.add(new MovieItem(fav.movieId, fav.movieName, fav.posterUrl));
                }
            }
            rvMovieList.setAdapter(new HistoryAdapter(requireContext(), movieList));
        });
    }
}