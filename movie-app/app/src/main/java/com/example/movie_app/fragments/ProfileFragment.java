package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_app.R;

public class ProfileFragment extends Fragment {

    private RelativeLayout btnManageSoftware, btnFavoriteMovies, btnNotificationSettings, btnEditProfile, btnLogout;

    private TextView tvUserName, tvUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện fragment_profile.xml vào Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 1. Ánh xạ các thông tin User
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        // 2. Ánh xạ các nút bấm từ CARD 1: CÁC HOẠT ĐỘNG DỊCH VỤ
        btnManageSoftware = view.findViewById(R.id.btnManageSoftware);
        btnFavoriteMovies = view.findViewById(R.id.btnFavoriteMovies);

        // 3. Ánh xạ các nút bấm từ CARD 2: CÀI ĐẶT VÀ TRỢ GIÚP
        btnNotificationSettings = view.findViewById(R.id.btnNotificationSettings);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // 4. Ánh xạ nút bấm từ CARD 3: ĐĂNG XUẤT
        btnLogout = view.findViewById(R.id.btnLogout);

        setupClickListeners();
        loadUserData();

        return view;
    }

    private void setupClickListeners() {
        // CARD 1: CÁC HOẠT ĐỘNG DỊCH VỤ

        // Mục mới: Quản lý phần mềm (Admin)
        btnManageSoftware.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở mục Quản lý phần mềm (Admin)", Toast.LENGTH_SHORT).show();
        });

        // Danh sách của tôi
        btnFavoriteMovies.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở Danh sách của tôi", Toast.LENGTH_SHORT).show();
        });

        // CARD 2: CÀI ĐẶT VÀ TRỢ GIÚP

        // Cài đặt
        btnNotificationSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở Cài đặt hệ thống", Toast.LENGTH_SHORT).show();
        });

        // Trợ giúp & Hỗ trợ
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở mục Trợ giúp & Hỗ trợ", Toast.LENGTH_SHORT).show();
        });

        // CARD 3: ĐĂNG XUẤT
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {

    }
}