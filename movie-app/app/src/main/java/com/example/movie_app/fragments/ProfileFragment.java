package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_app.R;

public class ProfileFragment extends Fragment {

    private RelativeLayout btnEditProfile, btnChangePassword, btnFavoriteMovies, btnNotificationSettings, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện XML vào Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ các nút bấm từ XML
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
      //  btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnFavoriteMovies = view.findViewById(R.id.btnFavoriteMovies);
        btnNotificationSettings = view.findViewById(R.id.btnNotificationSettings);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Thiết lập sự kiện Click cho các mục
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        // ID btnEditProfile giờ tương ứng với "Trợ giúp & Hỗ trợ"
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở mục Trợ giúp & Hỗ trợ", Toast.LENGTH_SHORT).show();
        });

        // ID btnChangePassword giờ tương ứng với "Lịch sử giao dịch"
        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở Lịch sử giao dịch", Toast.LENGTH_SHORT).show();
        });

        // ID btnFavoriteMovies giờ tương ứng với "Danh sách của tôi"
        btnFavoriteMovies.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở danh sách của tôi", Toast.LENGTH_SHORT).show();
        });

        // ID btnNotificationSettings giờ tương ứng với "Cài đặt"
        btnNotificationSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở Cài đặt hệ thống", Toast.LENGTH_SHORT).show();
        });

        // Giữ nguyên Đăng xuất
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }
}
