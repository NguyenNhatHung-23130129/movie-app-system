package movie_app_system.demo.controller;

import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private KKPhimClient kkPhimClient;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestMovies(@RequestParam(defaultValue = "1") int page) {
        return executeListCall(kkPhimClient.getNewMovies(page), "Không thể lấy dữ liệu phim mới");
    }

    @GetMapping("/series")
    public ResponseEntity<?> getSeriesMovies(@RequestParam(defaultValue = "1") int page) {
        return executeV1Call(kkPhimClient.getSeriesMovies(page), "Không thể lấy dữ liệu phim bộ");
    }

    @GetMapping("/single")
    public ResponseEntity<?> getSingleMovies(@RequestParam(defaultValue = "1") int page) {
        return executeV1Call(kkPhimClient.getSingleMovies(page), "Không thể lấy dữ liệu phim lẻ");
    }

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres() {
        try {
            Response<List<Category>> retrofitResponse = kkPhimClient.getRemoteGenres().execute();
            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                List<Category> customGenres = new ArrayList<>(retrofitResponse.body());
                Category allGenre = new Category("all", "Tất cả", "all");
                customGenres.add(0, allGenre);
                return ResponseEntity.ok(customGenres);
            }
            return ResponseEntity.status(500).body("Không thể lấy dữ liệu thể loại.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{slug}")
    public ResponseEntity<?> getMovieDetail(@PathVariable String slug) {
        try {
            Response<MovieDetailResponse> response = kkPhimClient.getMovieDetail(slug).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ResponseEntity.ok(response.body());
            }
            return ResponseEntity.status(404).body("{\"message\": \"Không tìm thấy phim!\"}");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"message\": \"Lỗi mạng: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/genres/{slug}")
    public ResponseEntity<?> getMoviesByCategory(@PathVariable String slug, @RequestParam(defaultValue = "1") int page) {
        if ("all".equals(slug)) return getLatestMovies(page);
        return executeV1Call(kkPhimClient.getMoviesByCategory(slug, page), "Không thể lấy dữ liệu thể loại");
    }

    private ResponseEntity<?> executeV1Call(Call<KKPhimV1Response> call, String errorMessage) {
        try {
            Response<KKPhimV1Response> response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                return ResponseEntity.ok(response.body().getData().getItems());
            }
            return ResponseEntity.status(500).body(errorMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }

    private ResponseEntity<?> executeListCall(Call<KKPhimListResponse> call, String errorMessage) {
        try {
            Response<KKPhimListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return ResponseEntity.ok(response.body().getItems());
            }
            return ResponseEntity.status(500).body(errorMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }
}