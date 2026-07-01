package com.example.movie_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.activities.DashboardAnalyticsActivity;
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
        fragmentContainer.removeAllViews();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            View memberView = layoutInflater.inflate(R.layout.fragment_profile, fragmentContainer, false);
            fragmentContainer.addView(memberView);
            initProfileViews(memberView);
        } else {
            View guestView = layoutInflater.inflate(R.layout.fragment_profile_guest, fragmentContainer, false);
            fragmentContainer.addView(guestView);
            initGuestViews(guestView);
        }
    }

    private void initGuestViews(View view) {
        AppCompatButton btnGoToLogin = view.findViewById(R.id.btnGoToLogin);
        if (btnGoToLogin != null) {
            btnGoToLogin.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            });
        }
    }

    private void initProfileViews(View view) {
        TextView tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        ImageView imgAvatar = view.findViewById(R.id.imgAvatar);
        RelativeLayout btnLogout = view.findViewById(R.id.btnLogout);
        RelativeLayout btnManageSoftware = view.findViewById(R.id.btnManageSoftware);
        RelativeLayout btnNotificationSettings = view.findViewById(R.id.btnNotificationSettings);
        RelativeLayout btnEditProfile = view.findViewById(R.id.btnEditProfile);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String name = prefs.getString("USER_NAME", "Thành viên");
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String avatarUrl = prefs.getString("USER_AVATAR", "");
        int role = prefs.getInt("USER_ROLE", 0);

        if (btnManageSoftware != null) {
            if (role == 1) {
                btnManageSoftware.setVisibility(View.VISIBLE);
                btnManageSoftware.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), DashboardAnalyticsActivity.class);
                    startActivity(intent);
                });
            } else {
                btnManageSoftware.setVisibility(View.GONE);
            }
        }


        if (tvUserName != null) tvUserName.setText(name);
        if (tvUserEmail != null) tvUserEmail.setText(email);
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


        if (imgAvatar != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.outline_account_circle_24)
                    .into(imgAvatar);
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                SharedPreferences preffs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                preffs.edit().clear().apply();
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), com.example.movie_app.activities.HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }
}
