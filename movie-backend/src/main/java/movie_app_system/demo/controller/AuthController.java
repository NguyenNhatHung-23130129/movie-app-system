package movie_app_system.demo.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import movie_app_system.demo.dto.GoogleLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private Firestore firestore;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            // 1. Verify idToken nhận được từ Android Client
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();

            // 2. Kiểm tra xem user này đã tồn tại trong Firestore chưa
            DocumentReference userRef = firestore.collection("users").document(uid);
            DocumentSnapshot document = userRef.get().get();

            if (!document.exists()) {
                // 3. Nếu chưa tồn tại -> Tự động đăng ký (Tạo mới bản ghi User)
                Map<String, Object> newUser = new HashMap<>();
                newUser.put("uid", uid);
                newUser.put("fullName", name != null ? name : "Google User");
                newUser.put("email", email);
                newUser.put("avatarUrl", picture != null ? picture : "");
                newUser.put("role", 0); // Mặc định role = 0 (User thường)
                newUser.put("createdAt", com.google.cloud.Timestamp.now());

                userRef.set(newUser).get();
                return ResponseEntity.ok(Map.of("message", "Đăng ký thành viên mới qua Google thành công!", "uid", uid));
            }

            return ResponseEntity.ok(Map.of("message", "Đăng nhập Google thành công!", "uid", uid));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token không hợp lệ hoặc lỗi hệ thống: " + e.getMessage()));
        }
    }
}