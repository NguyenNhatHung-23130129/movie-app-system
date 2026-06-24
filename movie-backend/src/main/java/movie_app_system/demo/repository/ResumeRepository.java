package movie_app_system.demo.repository;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.movie_app_system.demo.dto.ResumeRequest;
import com.movie_app_system.demo.dto.ResumeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class ResumeRepository {
    
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "user_resume_data";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Lưu hoặc cập nhật mốc thời gian xem dở vào Firestore
     * Document ID: {userId}_{movieId}
     */
    public ResumeResponse saveResumeData(ResumeRequest request) throws ExecutionException, InterruptedException {
        try {
            String docId = request.getUserId() + "_" + request.getMovieId();
            
            // Dữ liệu sẽ lưu vào Firestore
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("movieId", request.getMovieId());
            data.put("movieTitle", request.getMovieTitle());
            data.put("currentPosition", request.getCurrentPosition());
            data.put("duration", request.getDuration());
            data.put("currentEpisode", request.getCurrentEpisode());
            data.put("userId", request.getUserId());
            data.put("lastWatchedTime", LocalDateTime.now().format(formatter));
            
            // Lưu vào Collection "user_resume_data"
            firestore.collection(COLLECTION_NAME).document(docId).set(data).get();
            
            return new ResumeResponse(
                request.getMovieId(),
                request.getMovieTitle(),
                request.getCurrentPosition(),
                request.getDuration(),
                request.getCurrentEpisode(),
                request.getUserId(),
                LocalDateTime.now().format(formatter),
                "Resume data saved successfully",
                true
            );
        } catch (Exception e) {
            return new ResumeResponse(
                request.getMovieId(),
                request.getMovieTitle(),
                0,
                0,
                0,
                request.getUserId(),
                null,
                "Error saving resume data: " + e.getMessage(),
                false
            );
        }
    }

    /**
     * Lấy dữ liệu resume từ Firestore
     * Document ID: {userId}_{movieId}
     */
    public ResumeResponse getResumeData(String userId, String movieId) throws ExecutionException, InterruptedException {
        try {
            String docId = userId + "_" + movieId;
            DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(docId).get().get();
            
            if (document.exists()) {
                return new ResumeResponse(
                    document.getString("movieId"),
                    document.getString("movieTitle"),
                    document.getLong("currentPosition") != null ? document.getLong("currentPosition") : 0,
                    document.getLong("duration") != null ? document.getLong("duration") : 0,
                    document.getLong("currentEpisode") != null? document.getLong("currentEpisode").intValue() : 1,
                    document.getString("userId"),
                    document.getString("lastWatchedTime"),
                    "Resume data loaded successfully",
                    true
                );
            } else {
                return new ResumeResponse(
                    movieId,
                    null,
                    0,
                    0,
                    1,
                    userId,
                    null,
                    "No resume data found",
                    false
                );
            }
        } catch (Exception e) {
            return new ResumeResponse(
                movieId,
                null,
                0,
                0,
                1,
                userId,
                null,
                "Error loading resume data: " + e.getMessage(),
                false
            );
        }
    }

    /**
     * Xóa dữ liệu resume khỏi Firestore
     */
    public boolean deleteResumeData(String userId, String movieId) throws ExecutionException, InterruptedException {
        try {
            String docId = userId + "_" + movieId;
            firestore.collection(COLLECTION_NAME).document(docId).delete().get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}