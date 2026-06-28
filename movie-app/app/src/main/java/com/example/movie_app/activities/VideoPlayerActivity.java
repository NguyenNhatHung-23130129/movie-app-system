package com.example.movie_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.C;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.example.movie_app.R;
import com.example.movie_app.models.Movie;
import com.example.movie_app.viewmodel.VideoPlayerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private VideoPlayerViewModel viewModel;

    private TextView tvMovieTitle;
    private TextView tvCurrentEpisode;
    private TextView tvDuration;
    private ImageButton btnPrevEpisode;
    private ImageButton btnNextEpisode;
    private ImageButton btnPlaylist;

    private Movie currentMovie;
    private String userId;
    private int currentEpisode = 1;

    // FIX 1: Dùng pendingSeekPosition thay vì seekTo() trực tiếp sau prepare()
    private long pendingSeekPosition = 0;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        firebaseAuth = FirebaseAuth.getInstance();

        userId = getUserIdFromFirebase();
        if (userId == null) {
            Log.d(TAG, "Development Mode - Fake User");
            userId = "TEST_USER";
        }

        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);

        initViews();

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            currentMovie = movie;

            exoPlayer = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(exoPlayer);
            tvMovieTitle.setText(movie.getTitle());
            tvCurrentEpisode.setText("Tập " + currentEpisode);

            // FIX 1: setupPlayerListener phải được gọi TRƯỚC setupObservers
            // để listener sẵn sàng bắt STATE_READY khi video load
            setupPlayerListener();
            setupObservers();
            viewModel.loadResumeData(movie.getMovieId());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin phim!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getUserIdFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Log.d(TAG, "User logged in: " + uid);
            return uid;
        } else {
            Log.e(TAG, "No user logged in");
            return null;
        }
    }

    private void initViews() {
        playerView = findViewById(R.id.player_view);
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvCurrentEpisode = findViewById(R.id.tv_current_episode);
        tvDuration = findViewById(R.id.tv_duration);
        btnPrevEpisode = findViewById(R.id.btn_prev_episode);
        btnNextEpisode = findViewById(R.id.btn_next_episode);
        btnPlaylist = findViewById(R.id.btn_playlist);

        btnPrevEpisode.setOnClickListener(v -> previousEpisode());
        btnNextEpisode.setOnClickListener(v -> nextEpisode());
        btnPlaylist.setOnClickListener(v -> showPlaylist());
    }

    private void loadEpisode(int episodeIndex) {
        if (currentMovie == null) return;
        if (currentMovie.getEpisodeUrls() == null || currentMovie.getEpisodeUrls().isEmpty()) {
            Toast.makeText(this, "Không có danh sách tập phim!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (episodeIndex < 0 || episodeIndex >= currentMovie.getEpisodeUrls().size()) return;

        currentEpisode = episodeIndex + 1;
        String url = currentMovie.getEpisodeUrls().get(episodeIndex);

        if (url == null || url.trim().isEmpty()) {
            Log.e(TAG, "Đường dẫn video tập " + currentEpisode + " bị NULL!");
            Toast.makeText(this, "Tập phim này lỗi đường dẫn, không thể phát!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MediaItem mediaItem = MediaItem.fromUri(url);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            // FIX 1: KHÔNG gọi exoPlayer.play() ở đây
            // play() sẽ được gọi trong listener sau khi STATE_READY
            tvCurrentEpisode.setText("Tập " + currentEpisode);
            Log.d(TAG, "Loading episode " + currentEpisode + " với URL: " + url);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi nạp MediaItem: " + e.getMessage());
            Toast.makeText(this, "Định dạng video không hỗ trợ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupObservers() {
        viewModel.getResumeData().observe(this, resumeData -> {
            if (resumeData != null && resumeData.getCurrentPosition() > 0) {
                // FIX 1: Lưu vị trí cần seek vào biến, KHÔNG seekTo ngay
                pendingSeekPosition = resumeData.getCurrentPosition();
                currentEpisode = resumeData.getCurrentEpisode();

                Log.d(TAG, "Loaded saved position: " + pendingSeekPosition + "ms, episode: " + currentEpisode);
                Toast.makeText(this, "Tiếp tục xem từ tập " + currentEpisode, Toast.LENGTH_SHORT).show();

                if (currentMovie.getEpisodeUrls() != null && !currentMovie.getEpisodeUrls().isEmpty()) {
                    loadEpisode(currentEpisode - 1);
                    // KHÔNG gọi exoPlayer.seekTo() ở đây!
                    // Việc seek sẽ được thực hiện trong setupPlayerListener() khi STATE_READY
                }
            } else {
                pendingSeekPosition = 0;
                if (currentMovie.getEpisodeUrls() != null && !currentMovie.getEpisodeUrls().isEmpty()) {
                    loadEpisode(0);
                } else if (currentMovie.getVideoUrl() != null && !currentMovie.getVideoUrl().isEmpty()) {
                    MediaItem mediaItem = MediaItem.fromUri(currentMovie.getVideoUrl());
                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.prepare();
                } else {
                    Toast.makeText(this, "Không tìm thấy đường dẫn video!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPlayerListener() {
        if (exoPlayer == null) return;
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    long duration = exoPlayer.getDuration();
                    tvDuration.setText(formatTime(duration));
                    Log.d(TAG, "Video ready. Duration: " + duration + "ms");

                    // FIX 1: Seek đến vị trí đã lưu SAU KHI video sẵn sàng
                    if (pendingSeekPosition > 0) {
                        exoPlayer.seekTo(pendingSeekPosition);
                        pendingSeekPosition = 0; // Reset sau khi đã seek
                        Log.d(TAG, "Seeked to saved position successfully.");
                    }

                    // Tự động play sau khi video sẵn sàng (và đã seek nếu cần)
                    if (!exoPlayer.isPlaying()) {
                        exoPlayer.play();
                    }
                }

                if (playbackState == Player.STATE_ENDED) {
                    Log.d(TAG, "Episode ended.");
                }
            }

            @Override
            public void onPlayerError(androidx.media3.common.PlaybackException error) {
                Log.e(TAG, "Player error: " + error.getMessage());
                Toast.makeText(VideoPlayerActivity.this,
                        "Lỗi phát video: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void previousEpisode() {
        if (currentEpisode > 1) {
            currentEpisode--;
            saveAndChangeEpisode();
        } else {
            Toast.makeText(this, "Đây là tập đầu tiên", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextEpisode() {
        if (currentEpisode < (currentMovie != null ? currentMovie.getEpisodes() : 1)) {
            currentEpisode++;
            saveAndChangeEpisode();
        } else {
            Toast.makeText(this, "Đây là tập cuối cùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAndChangeEpisode() {
        if (exoPlayer != null && currentMovie != null) {
            long currentPos = exoPlayer.getCurrentPosition();
            long duration = exoPlayer.getDuration();

            viewModel.saveResumeData(
                    currentMovie.getMovieId(),
                    currentMovie.getTitle(),
                    currentPos,
                    duration,
                    currentEpisode,
                    userId
            );

            // Reset pendingSeek khi chuyển tập chủ động (không cần seek tới vị trí cũ)
            pendingSeekPosition = 0;
            loadEpisode(currentEpisode - 1);
            Log.d(TAG, "Changed to episode " + currentEpisode);
        }
    }

    private void showPlaylist() {
        Toast.makeText(this, "Tính năng danh sách tập sẽ được thêm", Toast.LENGTH_SHORT).show();
    }

    private String formatTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            return "00:00";
        }
        long seconds = timeMs / 1000;
        long minutes = seconds / 60;
        long secondsRemainder = seconds % 60;
        return String.format("%02d:%02d", minutes, secondsRemainder);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null && currentMovie != null) {
            long currentPos = exoPlayer.getCurrentPosition();
            long duration = exoPlayer.getDuration();
            Log.d(TAG, "Saving position on pause: " + currentPos + "ms");

            viewModel.saveResumeData(
                    currentMovie.getMovieId(),
                    currentMovie.getTitle(),
                    currentPos,
                    duration,
                    currentEpisode,
                    userId
            );

            exoPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // FIX 2: Chỉ play nếu player đang ở trạng thái sẵn sàng và chưa play
        if (exoPlayer != null
                && exoPlayer.getPlaybackState() == Player.STATE_READY
                && !exoPlayer.isPlaying()) {
            exoPlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null && currentMovie != null) {
            long currentPos = exoPlayer.getCurrentPosition();
            long duration = exoPlayer.getDuration();

            viewModel.saveResumeData(
                    currentMovie.getMovieId(),
                    currentMovie.getTitle(),
                    currentPos,
                    duration,
                    currentEpisode,
                    userId
            );

            exoPlayer.release();
            exoPlayer = null;
        }
    }
}