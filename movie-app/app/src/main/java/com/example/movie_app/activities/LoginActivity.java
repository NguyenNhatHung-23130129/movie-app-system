package com.example.movie_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.movie_app.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;
    private TextView tvRegisterNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

            // XÁC THỰC MẬT KHẨU TRỰC TIẾP QUA FIREBASE SDK
            com.google.firebase.auth.FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lấy User ID duy nhất (UID) do Firebase cấp
                            var user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String currentUserId = user.getUid();

                                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                prefs.edit().putString("USER_ID", currentUserId).apply();

                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                // Chuyển sang màn hình chính HomeActivity
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }
                        } else {
                            Exception exception = task.getException();
                            String errorMsg = "Đăng nhập thất bại. Vui lòng thử lại sau!";

                            if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidUserException) {
                                errorMsg = "Tài khoản Email này không tồn tại trong hệ thống!";
                            }
                            else if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                                errorMsg = "Mật khẩu không chính xác. Vui lòng kiểm tra lại!";
                            }
                            else if (exception != null && exception.getMessage() != null) {
                                errorMsg = exception.getMessage();
                            }

                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}