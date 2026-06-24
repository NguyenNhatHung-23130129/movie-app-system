package movie_app_system.demo.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import movie_app_system.demo.dto.LoginRequest;
import movie_app_system.demo.dto.LoginResponse;
import movie_app_system.demo.dto.RegisterRequest;
import movie_app_system.demo.dto.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // 1. API ĐĂNG KÝ TÀI KHOẢN LÊN FIREBASE
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();
        try {
            // Tạo request gửi lên Firebase Authentication để tạo User mới
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(request.getEmail())
                    .setPassword(request.getPassword())
                    .setDisplayName(request.getName());

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);

            response.setSuccess(true);
            response.setMessage("Đăng ký tài khoản thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Lỗi đăng ký: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 2. API ĐĂNG NHẬP (XÁC THỰC)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(request.getEmail());

            // Nếu tìm thấy user, ta giả lập trả về một Token thành công
            response.setSuccess(true);
            response.setMessage("Đăng nhập thành công!");
            response.setToken("mock-firebase-jwt-token-" + userRecord.getUid());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Tài khoản không tồn tại hoặc sai thông tin!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}