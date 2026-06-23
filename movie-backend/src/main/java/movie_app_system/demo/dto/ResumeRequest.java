package com.movie_app_system.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequest {
    private String movieId;
    private String movieTitle;
    private long currentPosition;      // Vị trí xem (milliseconds)
    private long duration;             // Tổng thời gian phim (milliseconds)
    private int currentEpisode;        // Tập hiện tại
    private String userId;
}