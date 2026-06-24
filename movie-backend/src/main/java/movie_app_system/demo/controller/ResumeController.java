package movie_app_system.demo.controller;

import com.movie_app_system.demo.dto.ResumeRequest;
import com.movie_app_system.demo.dto.ResumeResponse;
import movie_app_system.demo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResumeController {
    
    private final ResumeService resumeService;

    /**
     * Lưu mốc thời gian xem dở
     * POST /api/v1/resume/save
     * Request body: ResumeRequest (chứa movieId, currentPosition, userId...)
     */
    @PostMapping("/save")
    public ResponseEntity<ResumeResponse> saveResumeData(@RequestBody ResumeRequest request) {
        try {
            log.info("Request to save resume data: {}", request);
            ResumeResponse response = resumeService.saveResumeData(request);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error saving resume data", e);
            ResumeResponse errorResponse = new ResumeResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Failed to save resume data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Lấy mốc thời gian xem dở của một phim
     * GET /api/v1/resume/{movieId}?userId={userId}
     * Query param userId: ID của người dùng
     * Path param movieId: ID của phim
     * Response: ResumeResponse (chứa currentPosition để tua đến)
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<ResumeResponse> getResumeData(
            @PathVariable String movieId,
            @RequestParam String userId) {
        try {
            log.info("Request to get resume data for movie: {} and user: {}", movieId, userId);
            ResumeResponse response = resumeService.getResumeData(userId, movieId);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error fetching resume data", e);
            ResumeResponse errorResponse = new ResumeResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Failed to fetch resume data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Xóa lịch sử xem của một phim
     * DELETE /api/v1/resume/{movieId}?userId={userId}
     * Query param userId: ID của người dùng
     * Path param movieId: ID của phim
     */
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteResumeData(
            @PathVariable String movieId,
            @RequestParam String userId) {
        try {
            log.info("Request to delete resume data for movie: {} and user: {}", movieId, userId);
            boolean deleted = resumeService.deleteResumeData(userId, movieId);
            
            if (deleted) {
                return ResponseEntity.ok("Resume data deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Resume data not found");
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error deleting resume data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete resume data: " + e.getMessage());
        }
    }
}