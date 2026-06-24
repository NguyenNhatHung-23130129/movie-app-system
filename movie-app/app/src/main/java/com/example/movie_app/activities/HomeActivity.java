package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
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