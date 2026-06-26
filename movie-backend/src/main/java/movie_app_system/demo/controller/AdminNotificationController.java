package movie_app_system.demo.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import movie_app_system.demo.dto.NotificationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/notify")
public class AdminNotificationController {

    @PostMapping
    @RequestMapping("/send")
    public ResponseEntity<?> sendNotificationToTopic(@RequestBody NotificationRequest request) {
        try {
            // Thiết lập phần hiển thị trực quan của thông báo
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getMessage())
                    .build();

            // Nếu không truyền topic, mặc định gửi tới nhóm toàn bộ user "all_users"
            String targetTopic = (request.getTopic() != null && !request.getTopic().isEmpty())
                    ? request.getTopic() : "all_users";

            // Xây dựng message gửi qua Firebase
            Message message = Message.builder()
                    .setNotification(notification)
                    .setTopic(targetTopic)
                    .build();

            // Thực hiện lệnh push qua Firebase Admin SDK
            String response = FirebaseMessaging.getInstance().send(message);

            return ResponseEntity.ok("{\"message\": \"Đã gửi thông báo thành công!\", \"fcm_id\": \"" + response + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi thông báo thất bại: " + e.getMessage());
        }
    }
}