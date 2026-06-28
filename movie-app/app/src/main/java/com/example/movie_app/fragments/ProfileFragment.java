package com.example.movie_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.movie_app.R;
import com.example.movie_app.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.layoutInflater = inflater;

        fragmentContainer = new FrameLayout(requireContext());
        fragmentContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        return fragmentContainer;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatusAndInflate();
    }

    private void checkLoginStatusAndInflate() {
        if (fragmentContainer == null) return;

        // Xóa bỏ giao diện cũ đang hiển thị trong thùng chứa
        fragmentContainer.removeAllViews();

        // Kiểm tra Token/User thực tế từ Firebase SDK (Đồng bộ tuyệt đối với LoginActivity)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // ĐÃ ĐĂNG NHẬP -> Nạp giao diện thông tin cá nhân
            View memberView = layoutInflater.inflate(R.layout.fragment_profile, fragmentContainer, false);
            fragmentContainer.addView(memberView);
            initProfileViews(memberView, currentUser);
        } else {
            // CHƯA ĐĂNG NHẬP -> Nạp giao diện khách ẩn danh
            View guestView = layoutInflater.inflate(R.layout.fragment_profile_guest, fragmentContainer, false);
            fragmentContainer.addView(guestView);
            initGuestViews(guestView);
        }
    }

    // --- GIAO DIỆN CHƯA ĐĂNG NHẬP (GUEST) ---
    private void initGuestViews(View view) {
        AppCompatButton btnGoToLogin = view.findViewById(R.id.btnGoToLogin);
        if (btnGoToLogin != null) {
            btnGoToLogin.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    // --- GIAO DIỆN ĐÃ ĐĂNG NHẬP (MEMBER) ---
    private void initProfileViews(View view, FirebaseUser user) {
        TextView tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        RelativeLayout btnLogout = view.findViewById(R.id.btnLogout);
        RelativeLayout btnManageSoftware = view.findViewById(R.id.btnManageSoftware);
        RelativeLayout btnFavoriteMovies = view.findViewById(R.id.btnFavoriteMovies);
        RelativeLayout btnNotificationSettings = view.findViewById(R.id.btnNotificationSettings);
        RelativeLayout btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Hiển thị thông tin thực tế từ Gmail Firebase
        if (tvUserName != null) {
            tvUserName.setText(user.getDisplayName() != null && !user.getDisplayName().isEmpty()
                    ? user.getDisplayName() : "Thành viên MovieFlow");
        }
        if (tvUserEmail != null) {
            tvUserEmail.setText(user.getEmail());
        }

        // Cấu hình các nút chức năng khác
        if (btnManageSoftware != null) {
            btnManageSoftware.setOnClickListener(v -> Toast.makeText(getContext(), "Mở mục Quản lý phần mềm (Admin)", Toast.LENGTH_SHORT).show());
        }
        if (btnFavoriteMovies != null) {
            btnFavoriteMovies.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Danh sách của tôi", Toast.LENGTH_SHORT).show());
        }
        btnNotificationSettings.setOnClickListener(v -> {
            SettingsFragment settingsFragment = new SettingsFragment();

            getParentFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, settingsFragment)
                    .hide(this)
                    .addToBackStack("SETTINGS_FRAGMENT")
                    .commit();
        });

        btnEditProfile.setOnClickListener(v -> {
            HelpSupportFragment helpFragment = new HelpSupportFragment();
            getParentFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, helpFragment)
                    .hide(this)
                    .addToBackStack("HELP_FRAGMENT")
                    .commit();
        });

        // XỬ LÝ ĐĂNG XUẤT
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                prefs.edit().remove("USER_ID").apply();

                FirebaseAuth.getInstance().signOut();

                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

                checkLoginStatusAndInflate();
            });
        }
    }
}