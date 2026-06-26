package movie_app_system.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalUsers;       // Tổng số tài khoản thật trong collection "users"
    private long totalMovies;      // Tổng số phim (Nếu nhóm lưu cache/Firestore) hoặc số mặc định
    private String peakHours;      // Chuỗi hiển thị giờ cao điểm (Ví dụ: "20:00 - 23:00")
    private int movieProgress;     // Phần trăm thời lượng xem phim lẻ thật
    private int seriesProgress;    // Phần trăm thời lượng xem phim bộ thật
}