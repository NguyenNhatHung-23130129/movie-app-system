package com.example.movie_app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.movie_app.R;
import com.example.movie_app.activities.SearchActivity;

public abstract class BaseFragment extends Fragment {

    protected final int COLOR_ACTIVE = Color.parseColor("#FFB4AA");
    protected final int COLOR_INACTIVE = Color.parseColor("#9CA3AF");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchHeader(view);
    }

    private void setupSearchHeader(View view) {
        View headerBar = view.findViewById(R.id.layoutHeader);
        if (headerBar != null) {
            View btnSearch = headerBar.findViewById(R.id.btnSearch);
            if (btnSearch != null) {
                btnSearch.setOnClickListener(v -> {
                    if (getActivity() != null) {
                        startActivity(new Intent(getActivity(), SearchActivity.class));
                    }
                });
            }
        }
    }

    protected void setupBottomNavigation(View root, int activeTabId) {
        int[] tabIds = {R.id.tabHome, R.id.tabExplore, R.id.tabFavorite, R.id.tabProfile};
        int[] ivIds = {R.id.ivHome, R.id.ivExplore, R.id.ivFavorite, R.id.ivProfile};
        int[] tvIds = {R.id.tvHome, R.id.tvExplore, R.id.tvFavorite, R.id.tvProfile};

        for (int i = 0; i < tabIds.length; i++) {
            View tabView = root.findViewById(tabIds[i]);
            if (tabView == null) continue;

            ImageView icon = root.findViewById(ivIds[i]);
            TextView text = root.findViewById(tvIds[i]);

            boolean isActive = (tabIds[i] == activeTabId);

            if (icon != null) icon.setColorFilter(isActive ? COLOR_ACTIVE : COLOR_INACTIVE);
            if (text != null) text.setTextColor(isActive ? COLOR_ACTIVE : COLOR_INACTIVE);

            tabView.animate()
                    .scaleX(isActive ? 1.1f : 1.0f)
                    .scaleY(isActive ? 1.1f : 1.0f)
                    .setDuration(200)
                    .start();
        }
    }
}