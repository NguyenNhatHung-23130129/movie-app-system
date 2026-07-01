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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9002;
    private EditText edtName, edtEmailReg, edtPasswordReg, edtConfirmPassword;
    private AppCompatButton btnRegister, btnGoogleReg;
    private TextView tvLoginNow;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        edtName = findViewById(R.id.edtName);
        edtEmailReg = findViewById(R.id.edtEmailReg);
        edtPasswordReg = findViewById(R.id.edtPasswordReg);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginNow = findViewById(R.id.tvLoginNow);
        btnGoogleReg = findViewById(R.id.btnGoogleReg);

        tvLoginNow.setOnClickListener(v -> finish());
        btnGoogleReg.setOnClickListener(v -> signInWithGoogle());

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

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser fbUser = mAuth.getCurrentUser();
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

    private void signInWithGoogle() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Đăng ký bằng Google thất bại!", Toast.LENGTH_SHORT).show();
                resetGoogleButton();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        btnGoogleReg.setEnabled(false);
        btnGoogleReg.setText("Đang xử lý với Google...");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkGoogleUserInFirestore(user.getUid(), user);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Xác thực Firebase thất bại.", Toast.LENGTH_SHORT).show();
                        resetGoogleButton();
                    }
                });
    }

    private void checkGoogleUserInFirestore(String uid, FirebaseUser fbUser) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                navigateBasedOnRole(user);
                            }
                        } catch (Exception e) {
                            resetGoogleButton();
                        }
                    } else {
                        String name = fbUser.getDisplayName() != null ? fbUser.getDisplayName() : "Google User";
                        String email = fbUser.getEmail();
                        String avatar = fbUser.getPhotoUrl() != null ? fbUser.getPhotoUrl().toString() : "";
                        User newUser = new User(uid, name, email, avatar, 0, Timestamp.now());

                        FirebaseFirestore.getInstance().collection("users").document(uid).set(newUser)
                                .addOnSuccessListener(aVoid -> navigateBasedOnRole(newUser))
                                .addOnFailureListener(e -> resetGoogleButton());
                    }
                })
                .addOnFailureListener(e -> resetGoogleButton());
    }

    private void navigateBasedOnRole(User user) {
        saveUserToPrefs(user);
        Toast.makeText(RegisterActivity.this, "Đăng nhập bằng Google thành công!", Toast.LENGTH_SHORT).show();

        Intent intent;
        if (user.getRole() == 1) {
            intent = new Intent(RegisterActivity.this, DashboardAnalyticsActivity.class);
        } else {
            intent = new Intent(RegisterActivity.this, HomeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveUserToPrefs(User user) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USER_ID", user.getUid());
        editor.putInt("USER_ROLE", (int) user.getRole());
        editor.putString("USER_NAME", user.getFullName());
        editor.putString("USER_AVATAR", user.getAvatarUrl());
        editor.apply();
    }

    private void resetGoogleButton() {
        btnGoogleReg.setEnabled(true);
        btnGoogleReg.setText("Tiếp tục với Google");
    }

    private void saveUserToFirestore(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
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