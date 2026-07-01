package movie_app_system.demo.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import movie_app_system.demo.dto.AdminUserDto;
import movie_app_system.demo.dto.ModerationDashboardStats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminModerationController {

    @GetMapping("/moderation/stats")
    public ResponseEntity<ModerationDashboardStats> getModerationStats() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> usersFuture = db.collection("users").get();
            int totalUsers = usersFuture.get().size();

            // Đồng bộ hóa các trường số liệu cho Frontend
            ModerationDashboardStats stats = new ModerationDashboardStats();
            stats.setTotalPendingReports(12);
            stats.setTotalReportedUsers(totalUsers);
            stats.setTotalViolationComments(8);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.ok(new ModerationDashboardStats(0, 0, 0));
        }
    }

    @GetMapping("/users/reported")
    public ResponseEntity<List<AdminUserDto>> getReportedUsers() {
        List<AdminUserDto> list = new ArrayList<>();
        // Sử dụng set ID và Name khớp với Frontend DTO
        list.add(new AdminUserDto("user_id_001", "nguyenvanA", "vanya@gmail.com", false, 4));
        list.add(new AdminUserDto("user_id_002", "bad_user99", "spamking@gmail.com", true, 9));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/users/{userId}/toggle-lock")
    public ResponseEntity<Void> toggleUserLock(@PathVariable("userId") String userId, @RequestParam("isLock") boolean isLock) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection("users").document(userId).update("isLocked", isLock);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") String commentId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/ignore")
    public ResponseEntity<Void> ignoreCommentViolation(@PathVariable("commentId") String commentId) {
        return ResponseEntity.ok().build();
    }
}
