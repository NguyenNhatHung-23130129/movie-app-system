package movie_app_system.demo.controller;

import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.GenreResponse;
import movie_app_system.demo.dto.MovieCategoryResponse;
import movie_app_system.demo.dto.MovieItem;
import movie_app_system.demo.dto.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}