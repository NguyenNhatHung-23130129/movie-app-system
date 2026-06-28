package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;

public class HelpSupportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_help_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateHeaderTitle("Trợ giúp & Hỗ trợ");

        ImageView btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                getParentFragmentManager().popBackStack();
                updateHeaderTitle("Trang cá nhân");
            });
        }
    }

    private void updateHeaderTitle(String title) {
        if (getActivity() != null) {
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
        }
    }
}