package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.movie_app.R;
import com.example.movie_app.viewmodel.MovieViewModel;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.refreshData();
        viewModel.getMoviesFromFirebase().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }, 1500);
            }
        });
    }
}