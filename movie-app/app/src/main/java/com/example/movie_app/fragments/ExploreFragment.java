package com.example.movie_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieAdapter;
import com.example.movie_app.helpers.MovieFilterHelper;
import com.example.movie_app.models.Genre;
import com.example.movie_app.models.MovieItem;
import com.example.movie_app.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
public class ExploreFragment extends BaseFragment {
    private static final String TAG = "ExploreFragment_Debug";
    private TextView chipNew, chipSeries, chipSingle;
    private LinearLayout layoutGenres;
    private RecyclerView rvExplore;
    private EditText edtSearch;
    private MovieAdapter exploreAdapter;
    private MovieViewModel movieViewModel;

    private final ArrayList<TextView> genreChipsList = new ArrayList<>();

    private String currentFormat = "NEW";
    private String currentGenreSlug = null;

    private final List<MovieItem> currentFetchedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        chipNew = view.findViewById(R.id.chipNew);
        chipSeries = view.findViewById(R.id.chipSeries);
        chipSingle = view.findViewById(R.id.chipSingle);
        layoutGenres = view.findViewById(R.id.layoutGenres);
        rvExplore = view.findViewById(R.id.rvExplore);
        edtSearch = view.findViewById(R.id.edtSearch);

        edtSearch.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(getActivity(), com.example.movie_app.activities.SearchActivity.class);
            startActivity(intent);
        });

        edtSearch.setFocusable(false);
        edtSearch.setCursorVisible(false);

        exploreAdapter = new MovieAdapter(new ArrayList<>());
        exploreAdapter.setOnItemClickListener(movie -> {
            Log.d("DEBUG_CLICK", "Đã click vào phim: " + movie.getName());

            android.content.Intent intent = new android.content.Intent(getActivity(), com.example.movie_app.activities.MovieDetailActivity.class);
            intent.putExtra("movie_slug", movie.getSlug());

            startActivity(intent);
        });
        rvExplore.setAdapter(exploreAdapter);

        registerObservers();

        chipNew.setOnClickListener(v -> changeFormatFilter("NEW"));
        chipSeries.setOnClickListener(v -> changeFormatFilter("SERIES"));
        chipSingle.setOnClickListener(v -> changeFormatFilter("SINGLE"));

        loadGenreChipsFromApi();
    }

    private void registerObservers() {
        movieViewModel.getLatestMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("NEW".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });

        movieViewModel.getSeriesMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("SERIES".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });

        movieViewModel.getSingleMovies(1).observe(getViewLifecycleOwner(), res -> {
            if ("SINGLE".equals(currentFormat) && currentGenreSlug == null) {
                updateAdapterData(res != null ? res.getItems() : null);
            }
        });
    }

    private void loadGenreChipsFromApi() {
        movieViewModel.getGenres().observe(getViewLifecycleOwner(), genresList -> {
            if (genresList != null && !genresList.isEmpty()) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                layoutGenres.removeAllViews();
                genreChipsList.clear();

                for (Genre genre : genresList) {
                    TextView genreView = (TextView) inflater.inflate(R.layout.item_genre, layoutGenres, false);
                    genreView.setText(genre.getName());
                    genreView.setTag(genre.getSlug());

                    genreView.setOnClickListener(v -> changeGenreFilter(genreView, genre.getSlug()));

                    layoutGenres.addView(genreView);
                    genreChipsList.add(genreView);
                }
            }

            if (getArguments() != null) {
                String type = getArguments().getString("MOVIE_TYPE");
                String genreSlug = getArguments().getString("CATEGORY_SLUG");
                if (type != null) currentFormat = type;
                if (genreSlug != null) currentGenreSlug = genreSlug;
            }
            updateChipsUI();
            fetchDataFromServer();
        });
    }

    private void changeFormatFilter(String formatType) {
        if (currentFormat.equals(formatType) && currentGenreSlug != null) {
            currentFormat = formatType;
            applyLocalDualFilter();
        } else {
            currentFormat = formatType;
            fetchDataFromServer();
        }
    }

    private void changeGenreFilter(TextView clickedChip, String genreSlug) {
        if (genreSlug.equals(currentGenreSlug)) {
            currentGenreSlug = null;
            fetchDataFromServer();
        } else {
            currentGenreSlug = genreSlug;
            fetchDataFromServer();
        }
    }

    private void fetchDataFromServer() {
        // Thêm vào đầu hàm fetchDataFromServer()
        android.widget.Toast.makeText(getContext(), "Đang gọi fetchData", android.widget.Toast.LENGTH_SHORT).show();
        Log.d(TAG, "fetchDataFromServer: Format=" + currentFormat + ", Genre=" + currentGenreSlug);
        updateChipsUI();

        if (currentGenreSlug == null) {
            Log.d(TAG, "Luồng 1: Không chọn thể loại, đang lấy data theo Format");
            if ("NEW".equals(currentFormat)) {
                movieViewModel.getLatestMovies(1);
            } else if ("SERIES".equals(currentFormat)) {
                movieViewModel.getSeriesMovies(1);
            } else if ("SINGLE".equals(currentFormat)) {
                movieViewModel.getSingleMovies(1);
            }
        } else {
            Log.d(TAG, "Luồng 2: Có chọn thể loại " + currentGenreSlug + ", đang gọi API category");

            movieViewModel.getRawDataForDebugging(currentGenreSlug, 1).observe(getViewLifecycleOwner(), json -> {
                Log.d("API_RAW_DATA", "Dữ liệu JSON nhận được: " + json);
            });

            movieViewModel.getMoviesByCategory(currentGenreSlug, 1).observe(getViewLifecycleOwner(), res -> {
                if (currentGenreSlug != null) {
                    if (res != null && res.getItems() != null) {
                        currentFetchedList.clear();
                        currentFetchedList.addAll(res.getItems());
                        applyLocalDualFilter();
                    }
                }
            });
        }
    }

    private void applyLocalDualFilter() {
        Log.d(TAG, "Đang lọc cấp 2 với format: " + currentFormat + ". Tổng list ban đầu: " + currentFetchedList.size());

        List<MovieItem> filtered = MovieFilterHelper.filterMovies(currentFetchedList, currentFormat);

        Log.d("DEBUG_UI", "Số phim trước lọc: " + currentFetchedList.size());
        Log.d("DEBUG_UI", "Số phim sau lọc: " + filtered.size());
        exploreAdapter.setMovieList(filtered);
    }

    private void updateAdapterData(List<MovieItem> items) {
        if (items != null) {
            exploreAdapter.setMovieList(items);
        } else {
            exploreAdapter.setMovieList(new ArrayList<>());
        }
    }

    private void updateChipsUI() {
        int normalColor = Color.parseColor("#9CA3AF");
        int normalBg = Color.parseColor("#1E2024");
        int activeColor = Color.parseColor("#FFB4AA");
        int activeBg = Color.parseColor("#25FFFFFF");

        chipNew.setTextColor("NEW".equals(currentFormat) ? activeColor : normalColor);
        chipNew.setBackgroundColor("NEW".equals(currentFormat) ? activeBg : normalBg);

        chipSeries.setTextColor("SERIES".equals(currentFormat) ? activeColor : normalColor);
        chipSeries.setBackgroundColor("SERIES".equals(currentFormat) ? activeBg : normalBg);

        chipSingle.setTextColor("SINGLE".equals(currentFormat) ? activeColor : normalColor);
        chipSingle.setBackgroundColor("SINGLE".equals(currentFormat) ? activeBg : normalBg);

        for (TextView genreChip : genreChipsList) {
            String chipSlug = (String) genreChip.getTag();

            if (chipSlug != null && chipSlug.equals(currentGenreSlug)) {
                genreChip.setTextColor(activeColor);
                genreChip.setBackgroundResource(R.drawable.genre_bg_selected);
            } else {
                genreChip.setTextColor(normalColor);
                genreChip.setBackgroundResource(R.drawable.genre_bg_normal);
            }
        }
    }

    private void updateBottomNavigationIcons(View view) {
        ImageView ivHome = view.findViewById(R.id.ivHome);
        TextView tvHome = view.findViewById(R.id.tvHome);
        ImageView ivExplore = view.findViewById(R.id.ivExplore);
        TextView tvExplore = view.findViewById(R.id.tvExplore);

        if (ivHome != null) ivHome.setColorFilter(Color.parseColor("#9CA3AF"));
        if (tvHome != null) tvHome.setTextColor(Color.parseColor("#9CA3AF"));
        if (ivExplore != null) ivExplore.setColorFilter(Color.parseColor("#FFB4AA"));
        if (tvExplore != null) tvExplore.setTextColor(Color.parseColor("#FFB4AA"));
    }
}