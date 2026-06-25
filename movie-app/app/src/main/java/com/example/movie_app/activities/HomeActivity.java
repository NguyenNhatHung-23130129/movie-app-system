package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.movie_app.R;
import com.example.movie_app.fragments.ExploreFragment;
import com.example.movie_app.fragments.HomeFragment;
import com.example.movie_app.helpers.NavHelper;

public class HomeActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final ExploreFragment exploreFragment = new ExploreFragment();
    private int currentTabId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.home_activity);

        setupWindowInsets();
        setupClickListeners();

        if (savedInstanceState == null) {
            loadFragment(homeFragment, R.id.tabHome);
        }
        initNavigation();
    }

    private void setupWindowInsets() {
        View headerBar = findViewById(R.id.layoutHeader);
        if (headerBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(headerBar, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), 0);
                return insets;
            });
        }

        View bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
                return insets;
            });
        }
    }

    private void setupClickListeners() {
        View headerBar = findViewById(R.id.layoutHeader);
        if (headerBar != null) {
            ImageView btnSearch = headerBar.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        }

        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void initNavigation() {
        findViewById(R.id.tabHome).setOnClickListener(v -> loadFragment(homeFragment, R.id.tabHome));
        findViewById(R.id.tabExplore).setOnClickListener(v -> loadFragment(exploreFragment, R.id.tabExplore));
    }

    public void loadFragment(Fragment fragment, int tabId) {
        if (currentTabId == tabId) return;

        FragmentManager fm = getSupportFragmentManager();
        var transaction = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentById(R.id.fragmentContainer);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (!fragment.isAdded()) {
            transaction.add(R.id.fragmentContainer, fragment).commit();
        } else {
            transaction.show(fragment).commit();
        }

        currentTabId = tabId;
        View bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) NavHelper.highlightTab(bottomNav, tabId);
    }
}