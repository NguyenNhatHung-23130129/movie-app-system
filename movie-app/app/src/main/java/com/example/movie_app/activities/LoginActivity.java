package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_app.MainActivity;
import com.example.movie_app.R;
import com.example.movie_app.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;
    private TextView tvRegisterNow;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Ánh xạ View từ file XML
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);

        // Sự kiện: Bấm chữ "Đăng ký ngay" chuyển sang màn hình Register
        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Sự kiện: Bấm nút Đăng Nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi ViewModel và Observe (lắng nghe) kết quả
            authViewModel.performLogin(email, password).observe(this, response -> {
                if (response != null) {
                    if (response.isSuccess()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // TODO: Sau này bạn có thể lưu response.getToken() vào SharedPreferences ở đây

                        // Chuyển sang màn hình chính của App (MainActivity)
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Hiển thị thông báo lỗi từ Server
                        Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }
}