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

    private int currentTabId = -1;

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
        findViewById(R.id.tabHome).setOnClickListener(v -> loadFragment(new HomeFragment(), R.id.tabHome));
        findViewById(R.id.tabExplore).setOnClickListener(v -> loadFragment(new ExploreFragment(), R.id.tabExplore));
    }

    public void loadFragment(Fragment fragment, int tabId) {
        if (currentTabId == tabId) return;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        currentTabId = tabId;

        View bottomNav = findViewById(R.id.layoutBottom);
        if (bottomNav != null) {
            NavHelper.highlightTab(bottomNav, tabId);
        }
    }
}