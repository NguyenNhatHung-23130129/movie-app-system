package com.example.movie_app.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;
import com.google.android.material.card.MaterialCardView;

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

        MaterialCardView btnContactEmail = view.findViewById(R.id.btnContactEmail);
        if (btnContactEmail != null) {
            btnContactEmail.setOnClickListener(v -> {
                String emailBody = "Xin chào đội ngũ hỗ trợ,\n\n" +
                        "Tôi đang gặp vấn đề với ứng dụng:\n" +
                        "- Thiết bị: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "\n" +
                        "- Phiên bản Android: " + android.os.Build.VERSION.RELEASE + "\n" +
                        "- Mô tả chi tiết vấn đề:\n[Vui lòng nhập vấn đề của bạn ở đây]";

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:support@movieapp.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hỗ trợ ứng dụng CineDaily");
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);

                try {
                    startActivity(Intent.createChooser(intent, "Gửi email qua..."));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Không tìm thấy ứng dụng gửi email!", Toast.LENGTH_SHORT).show();
                }
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