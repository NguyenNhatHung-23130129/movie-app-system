package com.example.movie_app.helpers;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.R;

public class NavHelper {
    public static void highlightTab(View rootView, int activeTabId) {
        int[] tabIds = {R.id.tabHome, R.id.tabExplore, R.id.tabFavorite, R.id.tabProfile};
        int[] ivIds = {R.id.ivHome, R.id.ivExplore, R.id.ivFavorite, R.id.ivProfile};
        int[] tvIds = {R.id.tvHome, R.id.tvExplore, R.id.tvFavorite, R.id.tvProfile};

        int activeColor = Color.parseColor("#FFB4AA");
        int inactiveColor = Color.parseColor("#9CA3AF");

        for (int i = 0; i < tabIds.length; i++) {
            View tab = rootView.findViewById(tabIds[i]);
            if (tab == null) continue;
            ImageView icon = rootView.findViewById(ivIds[i]);
            TextView text = rootView.findViewById(tvIds[i]);

            boolean isActive = (tabIds[i] == activeTabId);
            icon.setColorFilter(isActive ? activeColor : inactiveColor);
            text.setTextColor(isActive ? activeColor : inactiveColor);

            tab.animate().scaleX(isActive ? 1.1f : 1.0f).scaleY(isActive ? 1.1f : 1.0f).setDuration(200).start();
        }
    }
}
