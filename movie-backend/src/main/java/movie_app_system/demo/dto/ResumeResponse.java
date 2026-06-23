package com.movie_app_system.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    private String movieId;
    private String movieTitle;
    private long currentPosition;
    private long duration;
    private int currentEpisode;
    private String userId;
    private String lastWatchedTime;
    private String message;
    private boolean success;
}