package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // Sử dụng AlertDialog từ thư viện AppCompat
import androidx.fragment.app.Fragment;
import com.example.movie_app.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateHeaderTitle("Cài đặt");

        ImageView btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                getParentFragmentManager().popBackStack();
                updateHeaderTitle("Trang cá nhân");
            });
        }

        View btnChangePassword = view.findViewById(R.id.btnChangePassword);
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        }
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);
        EditText edtOld = dialogView.findViewById(R.id.edtOldPassword);
        EditText edtNew = dialogView.findViewById(R.id.edtNewPassword);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        dialogView.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            String oldPass = edtOld.getText().toString().trim();
            String newPass = edtNew.getText().toString().trim();
            if (!oldPass.isEmpty() && !newPass.isEmpty()) {
                updatePassword(oldPass, newPass, dialog);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
    }

    private void updatePassword(String oldPass, String newPass, AlertDialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);

            user.reauthenticate(credential).addOnSuccessListener(aVoid -> {
                user.updatePassword(newPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateHeaderTitle(String title) {
        if (getActivity() != null) {
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            if (tvTitle != null) tvTitle.setText(title);
        }
    }
}