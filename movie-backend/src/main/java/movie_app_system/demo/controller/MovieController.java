package movie_app_system.demo.controller;

import com.google.gson.Gson;
import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/series")
    public ResponseEntity<?> getSeriesMovies(@RequestParam(defaultValue = "1") int page) {
        try {
            Response<MovieCategoryResponse> retrofitResponse = kkPhimClient.getSeriesMovies(page).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                java.util.List<MovieItem> items = retrofitResponse.body().getData().getItems();
                java.util.Map<String, Object> cleanResponse = new java.util.HashMap<>();
                cleanResponse.put("items", items);

                return ResponseEntity.ok(cleanResponse);
            } else {
                return ResponseEntity.status(500).body("Không thể lấy dữ liệu phim bộ từ KKPhim.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối mạng: " + e.getMessage());
        }
    }

    @GetMapping("/single")
    public ResponseEntity<?> getSingleMovies(@RequestParam(defaultValue = "1") int page) {
        try {
            Response<MovieCategoryResponse> retrofitResponse = kkPhimClient.getSingleMovies(page).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                java.util.List<MovieItem> items = retrofitResponse.body().getData().getItems();

                java.util.Map<String, Object> cleanResponse = new java.util.HashMap<>();
                cleanResponse.put("items", items);

                return ResponseEntity.ok(cleanResponse);
            } else {
                return ResponseEntity.status(500).body("Không thể lấy dữ liệu phim lẻ từ KKPhim.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối mạng: " + e.getMessage());
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres() {
        try {
            Response<List<GenreResponse>> retrofitResponse = kkPhimClient.getRemoteGenres().execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                List<GenreResponse> originalGenres = retrofitResponse.body();
                List<GenreResponse> customGenres = new ArrayList<>();

                GenreResponse allGenre = new GenreResponse();
                allGenre.setId("all");
                allGenre.setName("Tất cả");
                allGenre.setSlug("all");
                customGenres.add(allGenre);

                customGenres.addAll(originalGenres);

                return ResponseEntity.ok(customGenres);
            } else {
                return ResponseEntity.status(500).body("Không thể lấy dữ liệu thể loại từ KKPhim.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối mạng khi lấy thể loại: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{slug}")
    public ResponseEntity<?> getMovieDetail(@PathVariable String slug) {
        try {
            Response<MovieDetailResponse> retrofitResponse = kkPhimClient.getMovieDetail(slug).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                MovieDetailResponse movieDetail = retrofitResponse.body();

                if (!movieDetail.isStatus() || movieDetail.getMovie() == null) {
                    return ResponseEntity.status(404).body("{\"message\": \"Không tìm thấy bộ phim này trên hệ thống!\"}");
                }

                return ResponseEntity.ok(movieDetail);
            } else {
                return ResponseEntity.status(404).body("{\"message\": \"Không tìm thấy thông tin phim.\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"message\": \"Lỗi kết nối mạng: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/genres/{slug}")
    public ResponseEntity<?> getMoviesByCategory(@PathVariable String slug, @RequestParam(defaultValue = "1") int page) {
        if ("all".equals(slug)) {
            return getLatestMovies(page);
        }
        try {
            Response<MovieCategoryResponse> retrofitResponse = kkPhimClient.getMoviesByCategory(slug, page).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                java.util.List<MovieItem> items = retrofitResponse.body().getData().getItems();
                java.util.Map<String, Object> cleanResponse = new java.util.HashMap<>();
                cleanResponse.put("items", items);
                return ResponseEntity.ok(cleanResponse);
            } else {
                return ResponseEntity.status(500).body("Không thể lấy dữ liệu từ KKPhim.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam("keyword") String keyword) {
        try {
            Response<MovieCategoryResponse> retrofitResponse = kkPhimClient.searchMovies(keyword).execute();

            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                java.util.List<MovieItem> items = retrofitResponse.body().getData().getItems();
                java.util.Map<String, Object> cleanResponse = new java.util.HashMap<>();
                cleanResponse.put("items", items);
                return ResponseEntity.ok(cleanResponse);
            } else {
                return ResponseEntity.status(500).body("Không tìm thấy kết quả.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi kết nối: " + e.getMessage());
        }
    }
}