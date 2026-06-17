package com.example.movie_app.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.movie_app.R;
import com.example.movie_app.fragments.ExploreFragment;
import com.example.movie_app.fragments.HomeFragment;
import com.example.movie_app.helpers.NavHelper;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), R.id.tabHome);
        }

        initNavigation();
    }

    private void initNavigation() {
        findViewById(R.id.tabHome).setOnClickListener(v ->
                loadFragment(new HomeFragment(), R.id.tabHome));

        findViewById(R.id.tabExplore).setOnClickListener(v ->
                loadFragment(new ExploreFragment(), R.id.tabExplore));

        // findViewById(R.id.tabFavorite).setOnClickListener(v -> loadFragment(new FavoriteFragment(), R.id.tabFavorite));
        // findViewById(R.id.tabProfile).setOnClickListener(v -> loadFragment(new ProfileFragment(), R.id.tabProfile));
    }

    public void loadFragment(Fragment fragment, int tabId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        View bottomNav = findViewById(R.id.layoutBottom);

        if (bottomNav != null) {
            NavHelper.highlightTab(bottomNav, tabId);
        }
    }
}