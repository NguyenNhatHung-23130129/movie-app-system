package movie_app_system.demo.service;

import com.movie_app_system.demo.dto.ResumeRequest;
import com.movie_app_system.demo.dto.ResumeResponse;
import movie_app_system.demo.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    
    private final ResumeRepository resumeRepository;

    /**
     * Lưu mốc thời gian xem dở
     */
    public ResumeResponse saveResumeData(ResumeRequest request) throws ExecutionException, InterruptedException {
        log.info("Saving resume data for user: {} and movie: {}", request.getUserId(), request.getMovieId());
        
        if (request.getMovieId() == null || request.getUserId() == null) {
            ResumeResponse response = new ResumeResponse();
            response.setSuccess(false);
            response.setMessage("Movie ID and User ID are required");
            return response;
        }
        
        return resumeRepository.saveResumeData(request);
    }

    /**
     * Lấy dữ liệu resume
     */
    public ResumeResponse getResumeData(String userId, String movieId) throws ExecutionException, InterruptedException {
        log.info("Fetching resume data for user: {} and movie: {}", userId, movieId);
        
        if (movieId == null || userId == null) {
            ResumeResponse response = new ResumeResponse();
            response.setSuccess(false);
            response.setMessage("Movie ID and User ID are required");
            return response;
        }
        
        return resumeRepository.getResumeData(userId, movieId);
    }

    /**
     * Xóa dữ liệu resume
     */
    public boolean deleteResumeData(String userId, String movieId) throws ExecutionException, InterruptedException {
        log.info("Deleting resume data for user: {} and movie: {}", userId, movieId);
        
        return resumeRepository.deleteResumeData(userId, movieId);
    }
}