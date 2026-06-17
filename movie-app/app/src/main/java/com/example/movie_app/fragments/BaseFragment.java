package com.example.movie_app.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchHeader(view);
    }

    private void setupSearchHeader(View view) {
        View headerBar = view.findViewById(R.id.layoutHeader);
        if (headerBar != null) {
            ImageView btnSearch = headerBar.findViewById(R.id.btnSearch);
            if (btnSearch != null) {
                btnSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchActivity.class)));
            }
        }
    }

    protected void setupBottomNavigation(View view, int activeTabId) {
        int[] tabIds = {R.id.tabHome, R.id.tabExplore, R.id.tabFavorite, R.id.tabProfile};

        int[] ivIds = {R.id.ivHome, R.id.ivExplore, R.id.ivFavorite, R.id.ivProfile};
        int[] tvIds = {R.id.tvHome, R.id.tvExplore, R.id.tvFavorite, R.id.tvProfile};

        for (int i = 0; i < tabIds.length; i++) {
            ImageView icon = view.findViewById(ivIds[i]);
            TextView text = view.findViewById(tvIds[i]);

            if (tabIds[i] == activeTabId) {
                icon.setColorFilter(COLOR_ACTIVE);
                text.setTextColor(COLOR_ACTIVE);

                view.findViewById(tabIds[i]).animate()
                        .scaleX(1.1f).scaleY(1.1f)
                        .setDuration(200).start();
            } else {
                icon.setColorFilter(COLOR_INACTIVE);
                text.setTextColor(COLOR_INACTIVE);
                view.findViewById(tabIds[i]).animate()
                        .scaleX(1.0f).scaleY(1.0f)
                        .setDuration(200).start();
            }
        }
    }
}