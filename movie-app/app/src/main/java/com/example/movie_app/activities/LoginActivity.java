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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private EditText edtEmail, edtPassword;
    private AppCompatButton btnLogin, btnGoogle;
    private TextView tvRegisterNow;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
        btnGoogle = findViewById(R.id.btnGoogle);

        btnGoogle.setOnClickListener(v -> signInWithGoogle());

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

            mGoogleSignInClient.signOut().addOnCompleteListener(taskSignOut -> {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser fbUser = mAuth.getCurrentUser();
                                if (fbUser != null) {
                                    checkUserRoleAndNavigate(fbUser.getUid(), fbUser);
                                }
                            } else {
                                resetLoginButton();
                                handleLoginFailure(task.getException());
                            }
                        });
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
                Toast.makeText(this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
                resetGoogleButton();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        btnGoogle.setEnabled(false);
        btnGoogle.setText("Đang xử lý với Google...");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendTokenToBackend(idToken, user.getUid(), user);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Xác thực Firebase thất bại.", Toast.LENGTH_SHORT).show();
                        resetGoogleButton();
                    }
                });
    }

    private void sendTokenToBackend(String idToken, String uid, FirebaseUser fbUser) {
        checkUserRoleAndNavigate(uid, fbUser);
    }

    private void checkUserRoleAndNavigate(String uid, FirebaseUser fbUser) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                navigateBasedOnRole(user);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Lỗi phân tích dữ liệu: " + e.getMessage());
                            Toast.makeText(this, "Lỗi định dạng dữ liệu: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            resetLoginButton();
                            resetGoogleButton();
                        }
                    } else {
                        String name = fbUser.getDisplayName() != null ? fbUser.getDisplayName() : "Google User";
                        String email = fbUser.getEmail();
                        String avatar = fbUser.getPhotoUrl() != null ? fbUser.getPhotoUrl().toString() : "";
                        User newUser = new User(uid, name, email, avatar, 0, Timestamp.now());

                        FirebaseFirestore.getInstance().collection("users").document(uid).set(newUser)
                                .addOnSuccessListener(aVoid -> navigateBasedOnRole(newUser))
                                .addOnFailureListener(e -> {
                                    Toast.makeText(LoginActivity.this, "Lỗi khởi tạo tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    resetLoginButton();
                                    resetGoogleButton();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi kết nối Firestore: " + e.getMessage());
                    Toast.makeText(this, "Lỗi kết nối CSDL: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    resetLoginButton();
                    resetGoogleButton();
                });
    }

    private void navigateBasedOnRole(User user) {
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

    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Đăng nhập");
    }

    private void resetGoogleButton() {
        btnGoogle.setEnabled(true);
        btnGoogle.setText("Tiếp tục với Google");
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