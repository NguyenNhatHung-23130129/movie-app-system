package com.example.movie_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.movie_app.R;
import com.example.movie_app.fragments.ExploreFragment;
import com.example.movie_app.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        initNavigation();
    }

    private void initNavigation() {
        findViewById(R.id.tabHome).setOnClickListener(v -> {
            loadFragment(new HomeFragment());
        });

        findViewById(R.id.tabExplore).setOnClickListener(v -> {
            loadFragment(new ExploreFragment());
        });
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}