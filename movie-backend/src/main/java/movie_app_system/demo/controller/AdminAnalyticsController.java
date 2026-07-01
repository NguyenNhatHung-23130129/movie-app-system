package movie_app_system.demo.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import movie_app_system.demo.dto.AnalyticsDashboardResponse;
import movie_app_system.demo.dto.ChartDataPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/analytics")
public class AdminAnalyticsController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> getAnalyticsDashboardData(@RequestParam(value = "days", defaultValue = "30") int days) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> usersFuture = db.collection("users").get();
            long realTotalUsers = usersFuture.get().size();

            AnalyticsDashboardResponse response = new AnalyticsDashboardResponse();
            response.setTotalUsersFormatted(String.valueOf(realTotalUsers));
            response.setGrowthRateText("+12.5%");
            response.setUserSubtext("Người dùng đăng ký mới trong " + days + " ngày qua");
            response.setPeakHoursInterval("19:00 - 22:00");

            response.setMovieMinutesFormatted("2.4");
            response.setMoviePercentage(65);
            response.setSeriesMinutesFormatted("1.3");
            response.setSeriesPercentage(35);
            List<ChartDataPoint> points = new ArrayList<>();
            if (days == 7) {
                points.add(new ChartDataPoint("T2", 40, false));
                points.add(new ChartDataPoint("T3", 60, false));
                points.add(new ChartDataPoint("T4", 50, false));
                points.add(new ChartDataPoint("T5", 80, false));
                points.add(new ChartDataPoint("T6", 75, false));
                points.add(new ChartDataPoint("T7", 110, true));
                points.add(new ChartDataPoint("CN", 130, true));
            } else {
                points.add(new ChartDataPoint("Tuần 1", 70, false));
                points.add(new ChartDataPoint("Tuần 2", 95, false));
                points.add(new ChartDataPoint("Tuần 3", 140, true));
                points.add(new ChartDataPoint("Tuần 4", 110, false));
            }
            response.setChartDataPoints(points);
            response.setTrendingMovies(new ArrayList<>());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi kết nối và tính toán dữ liệu Firestore Analytics: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Void> exportAnalyticsReport(@RequestParam("days") int days) {
        return ResponseEntity.ok().build();
    }
}