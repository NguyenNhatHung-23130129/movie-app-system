package com.example.movie_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_app.R;
import com.example.movie_app.models.Movie;
import com.example.movie_app.viewmodel.VideoPlayerViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";

    private StyledPlayerView playerView;
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
    private long savedPosition = 0;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        firebaseAuth = FirebaseAuth.getInstance();


        userId = getUserIdFromFirebase();

        if (userId == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);

        initViews();
        setupObservers();

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie != null) {
            currentMovie = movie;
            setupPlayer(movie);
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

    private void setupObservers() {
        viewModel.getResumeData().observe(this, resumeData -> {
            if (resumeData != null && resumeData.getCurrentPosition() > 0) {
                savedPosition = resumeData.getCurrentPosition();
                currentEpisode = resumeData.getCurrentEpisode();

                Log.d(TAG, "Loaded saved position: " + savedPosition + "ms, episode: " + currentEpisode);
                Toast.makeText(this, "Tiếp tục xem từ tập " + currentEpisode, Toast.LENGTH_SHORT).show();

                if (exoPlayer != null) {
                    exoPlayer.seekTo(savedPosition);
                }
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                playerView.setShutterBackgroundColor(0x66000000);
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPlayer(Movie movie) {
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        tvMovieTitle.setText(movie.getTitle());
        tvCurrentEpisode.setText("Tập " + currentEpisode);

        MediaItem mediaItem = MediaItem.fromUri(movie.getVideoUrl());
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

        viewModel.loadResumeData(movie.getMovieId());

        exoPlayer.addListener(new com.google.android.exoplayer2.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == com.google.android.exoplayer2.Player.STATE_READY) {
                    long duration = exoPlayer.getDuration();
                    tvDuration.setText(formatTime(duration));
                    Log.d(TAG, "Video ready. Duration: " + duration + "ms");
                }
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

            tvCurrentEpisode.setText("Tập " + currentEpisode);
            Log.d(TAG, "Changed to episode " + currentEpisode);
        }
    }

    private void showPlaylist() {
        Toast.makeText(this, "Tính năng danh sách tập sẽ được thêm", Toast.LENGTH_SHORT).show();
    }

    private String formatTime(long timeMs) {
        if (timeMs == com.google.android.exoplayer2.C.TIME_UNSET) {
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

        if (exoPlayer != null) {
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