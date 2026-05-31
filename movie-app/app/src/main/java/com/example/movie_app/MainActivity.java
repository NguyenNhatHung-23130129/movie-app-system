package com.example.movie_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.movie_app.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getLatestMovies(1).observe(this, movieResponse -> {
            if (movieResponse != null && movieResponse.getItems() != null) {
                int size = movieResponse.getItems().size();
                Log.d("MVVM_TEST", "Đã lấy về thành công " + size + " bộ phim!");
                Toast.makeText(this, "Đã kết nối Backend & lấy " + size + " phim!", Toast.LENGTH_LONG).show();

            } else {
                Log.e("MVVM_TEST", "Không lấy được dữ liệu từ Backend");
                Toast.makeText(this, "Kết nối Backend thất bại hoặc Server chưa bật!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}