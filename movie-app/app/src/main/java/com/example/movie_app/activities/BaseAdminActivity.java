package com.example.movie_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;

public abstract class BaseAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int role = prefs.getInt("USER_ROLE", 0);

        if (role != 1) {
            Toast.makeText(this, "Bạn không có quyền truy cập vùng quản trị!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    protected void setupAdminInfo() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String adminName = prefs.getString("USER_NAME", "Quản trị viên");
        String avatarUrl = prefs.getString("USER_AVATAR", "");

        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        ImageView imgHeaderAvatar = findViewById(R.id.imgHeaderAvatar);
        TextView tvHeaderInitial = findViewById(R.id.tvHeaderInitial);
        ImageView imgHeaderMenu = findViewById(R.id.imgHeaderMenu);

        if (tvHeaderTitle != null) tvHeaderTitle.setText("Chào, " + adminName);
        
        if (imgHeaderMenu != null) {
            imgHeaderMenu.setImageResource(R.drawable.ic_home);
            imgHeaderMenu.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }

        displayAvatar(imgHeaderAvatar, tvHeaderInitial, avatarUrl, adminName);
    }

    private void displayAvatar(ImageView imageView, TextView initialView, String url, String name) {
        if (imageView == null) return;
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.outline_account_circle_24).circleCrop().into(imageView);
            if (initialView != null) initialView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            if (initialView != null) {
                imageView.setVisibility(View.GONE);
                initialView.setVisibility(View.VISIBLE);
                String initial = name.length() >= 2 ? name.substring(0, 2) : name;
                initialView.setText(initial.toUpperCase());
            }
        }
    }
}
