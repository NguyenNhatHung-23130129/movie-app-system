package com.example.movie_app.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.movie_app.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmailReg, edtPasswordReg, edtConfirmPassword;
    private AppCompatButton btnRegister;
    private TextView tvLoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // XÁC THỰC TRỰC TIẾP QUA FIREBASE SDK
            com.google.firebase.auth.FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Cập nhật DisplayName (Họ và tên) cho tài khoản vừa tạo
                            var user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                com.google.firebase.auth.UserProfileChangeRequest profileUpdates =
                                        new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                user.updateProfile(profileUpdates);
                            }

                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                            finish(); // Tự động quay về màn hình Login
                        } else {
                            // Hiển thị thông báo lỗi chi tiết từ Firebase (ví dụ: mật khẩu quá ngắn, trùng email...)
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Đăng ký thất bại!";
                            Toast.makeText(RegisterActivity.this, "Lỗi: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}