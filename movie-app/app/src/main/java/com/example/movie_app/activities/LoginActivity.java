package com.example.movie_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.movie_app.R;
import com.example.movie_app.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;
    private TextView tvRegisterNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);

        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("Đang đăng nhập...");
            
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            if (fbUser != null) {
                                checkUserRoleAndNavigate(fbUser.getUid());
                            }
                        } else {
                            resetLoginButton();
                            handleLoginFailure(task.getException());
                        }
                    });
        });
    }

    private void checkUserRoleAndNavigate(String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                saveUserToPrefs(user);
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                Intent intent;
                                if (user.getRole() == 1) {
                                    intent = new Intent(LoginActivity.this, DashboardAnalyticsActivity.class);
                                } else {
                                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Lỗi phân tích dữ liệu: " + e.getMessage());
                            Toast.makeText(this, "Lỗi định dạng dữ liệu: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            resetLoginButton();
                        }
                    } else {
                        Toast.makeText(this, "Tài khoản không tồn tại trên hệ thống!", Toast.LENGTH_LONG).show();
                        resetLoginButton();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi kết nối Firestore: " + e.getMessage());
                    Toast.makeText(this, "Lỗi kết nối CSDL: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    resetLoginButton();
                });
    }

    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Đăng nhập");
    }

    private void saveUserToPrefs(User user) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USER_ID", user.getUid());
        // Ép kiểu long về int để lưu vào SharedPreferences
        editor.putInt("USER_ROLE", (int) user.getRole());
        editor.putString("USER_NAME", user.getFullName());
        editor.putString("USER_AVATAR", user.getAvatarUrl());
        editor.apply();
    }

    private void handleLoginFailure(Exception exception) {
        String errorMsg = "Đăng nhập thất bại!";
        if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidUserException) {
            errorMsg = "Email chưa được đăng ký!";
        } else if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
            errorMsg = "Mật khẩu không chính xác!";
        } else if (exception != null) {
            errorMsg = exception.getMessage();
        }
        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
    }
}
