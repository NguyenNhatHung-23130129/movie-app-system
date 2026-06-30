package com.example.movie_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_app.activities.DashboardAnalyticsActivity;
import com.example.movie_app.activities.HomeActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            
            if (currentUser != null) {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int role = prefs.getInt("USER_ROLE", 0);

                if (role == 1) {
                    startActivity(new Intent(MainActivity.this, DashboardAnalyticsActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            } else {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
            finish();
        }, 2000);
    }
}
