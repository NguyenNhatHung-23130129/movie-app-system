package com.example.movie_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.movie_app.R;
import com.example.movie_app.models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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

        tvLoginNow.setOnClickListener(v -> finish());

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

            btnRegister.setEnabled(false);
            btnRegister.setText("Đang xử lý...");

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (fbUser != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                fbUser.updateProfile(profileUpdates);

                                User userModel = new User(
                                        fbUser.getUid(),
                                        name,
                                        email,
                                        "", 
                                        0,
                                        Timestamp.now()
                                );
                                saveUserToFirestore(userModel);
                            }
                        } else {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Đăng ký");
                            handleRegisterError(task.getException());
                        }
                    });
        });
    }

    private void saveUserToFirestore(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    
                    FirebaseAuth.getInstance().signOut();
                    
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Đăng ký");
                    Toast.makeText(RegisterActivity.this, "Lỗi lưu Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void handleRegisterError(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "Email này đã được sử dụng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi: " + (e != null ? e.getMessage() : "Đăng ký thất bại"), Toast.LENGTH_LONG).show();
        }
    }
}
