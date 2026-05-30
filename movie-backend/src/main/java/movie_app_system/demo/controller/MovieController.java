package movie_app_system.demo.controller;

import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private KKPhimClient kkPhimClient;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestMovies(@RequestParam(defaultValue = "1") int page) {
        try {
            Response<MovieResponse> retrofitResponse = kkPhimClient.getNewMovies(page).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                return ResponseEntity.ok(retrofitResponse.body());
            } else {
                return ResponseEntity.status(500).body("Không thể lấy dữ liệu từ server KKPhim đối tác.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối mạng giữa Backend và KKPhim: " + e.getMessage());
        }
    }
}