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
        View navDashboard = activity.findViewById(R.id.nav_dashboard);
        View navMovies = activity.findViewById(R.id.nav_movies);
        View navModerate = activity.findViewById(R.id.nav_moderate);
        View navSafety = activity.findViewById(R.id.nav_safety);

        highlightCurrentTab(activity);

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
        if (activity.getClass().equals(targetClass)) {
            return;
        }

        Intent intent = new Intent(activity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        
        // Không kết thúc activity cũ để giữ stack nếu cần, 
        // hoặc dùng finish() nếu muốn tối ưu bộ nhớ
    }

    private static void highlightCurrentTab(Activity activity) {
        int activeColor = Color.parseColor("#E50914");
        int inactiveColor = Color.parseColor("#BDC7D9");

        resetTabColors(activity, inactiveColor);

        if (activity instanceof DashboardAnalyticsActivity || activity instanceof AdminDashboardActivity) {
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
        View layout = activity.findViewById(layoutId);
        if (layout instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) layout;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                if (child instanceof ImageView) {
                    ((ImageView) child).setColorFilter(color);
                } else if (child instanceof TextView) {
                    ((TextView) child).setTextColor(color);
                }
            }
        }
    }
}
