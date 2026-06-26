package com.example.movie_app.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie_app.activities.AdminDashboardActivity;
import com.example.movie_app.activities.DashboardAnalyticsActivity;
import com.example.movie_app.activities.ModerationManagementActivity;
import com.example.movie_app.activities.MovieManagementActivity;
import com.example.movie_app.activities.SystemSafetyManagementActivity;
import com.example.movie_app.R;

public class AdminNavigationHelper {

    public static void setupAdminBottomNavigation(Activity activity) {
        // Tìm các vùng chứa tab
        View navDashboard = activity.findViewById(R.id.nav_dashboard);
        View navMovies = activity.findViewById(R.id.nav_movies);
        View navModerate = activity.findViewById(R.id.nav_moderate);
        View navSafety = activity.findViewById(R.id.nav_safety);

        // Highlight tab đang đứng
        highlightCurrentTab(activity);

        // Thiết lập sự kiện click cho từng tab với kiểm tra an toàn
        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> navigate(activity, DashboardAnalyticsActivity.class));
        }

        if (navMovies != null) {
            navMovies.setOnClickListener(v -> navigate(activity, MovieManagementActivity.class));
        }

        if (navModerate != null) {
            navModerate.setOnClickListener(v -> navigate(activity, ModerationManagementActivity.class));
        }

        if (navSafety != null) {
            navSafety.setOnClickListener(v -> navigate(activity, SystemSafetyManagementActivity.class));
        }
    }

    private static void navigate(Activity activity, Class<?> targetClass) {
        // Nếu nhấn vào chính tab đang đứng thì không chuyển màn hình
        if (activity.getClass().equals(targetClass)) {
            return;
        }

        try {
            Intent intent = new Intent(activity, targetClass);
            // FLAG_ACTIVITY_REORDER_TO_FRONT giúp chuyển tab mượt mà, không tạo lại Activity
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Lỗi chuyển màn hình: " + targetClass.getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void highlightCurrentTab(Activity activity) {
        int activeColor = Color.parseColor("#E50914"); // Màu đỏ chủ đạo
        int inactiveColor = Color.parseColor("#BDC7D9"); // Màu xám mặc định

        // Reset màu tất cả các tab về mặc định
        resetTabColors(activity, inactiveColor);

        // Đổi màu tab hiện tại
        if (activity instanceof AdminDashboardActivity) {
            applyColorToTab(activity, R.id.nav_dashboard, activeColor);
        } else if (activity instanceof MovieManagementActivity) {
            applyColorToTab(activity, R.id.nav_movies, activeColor);
        } else if (activity instanceof ModerationManagementActivity) {
            applyColorToTab(activity, R.id.nav_moderate, activeColor);
        } else if (activity instanceof SystemSafetyManagementActivity) {
            applyColorToTab(activity, R.id.nav_safety, activeColor);
        }
    }

    private static void resetTabColors(Activity activity, int color) {
        applyColorToTab(activity, R.id.nav_dashboard, color);
        applyColorToTab(activity, R.id.nav_movies, color);
        applyColorToTab(activity, R.id.nav_moderate, color);
        applyColorToTab(activity, R.id.nav_safety, color);
    }

    private static void applyColorToTab(Activity activity, int layoutId, int color) {
        ViewGroup layout = activity.findViewById(layoutId);
        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof ImageView) {
                    ((ImageView) child).setColorFilter(color);
                } else if (child instanceof TextView) {
                    ((TextView) child).setTextColor(color);
                }
            }
        }
    }
}
