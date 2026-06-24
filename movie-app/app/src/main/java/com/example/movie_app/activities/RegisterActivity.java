package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_app.R;
import com.example.movie_app.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmailReg, edtPasswordReg, edtConfirmPassword;
    private AppCompatButton btnRegister;
    private TextView tvLoginNow;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        edtName = findViewById(R.id.edtName);
        edtEmailReg = findViewById(R.id.edtEmailReg);
        edtPasswordReg = findViewById(R.id.edtPasswordReg);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginNow = findViewById(R.id.tvLoginNow);

        // Trở về màn hình đăng nhập nếu đã có tài khoản
        tvLoginNow.setOnClickListener(v -> {
            finish(); // Đóng Activity hiện tại
        });

        // Bấm nút Đăng ký
        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmailReg.getText().toString().trim();
            String password = edtPasswordReg.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu xác nhận
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi ViewModel Đăng ký
            authViewModel.performRegister(name, email, password).observe(this, response -> {
                if (response != null) {
                    if (response.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                        finish(); // Đóng màn hình đăng ký, tự động trở về Login
                    } else {
                        Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }
}