package com.example.movie_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.models.MovieItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<MovieItem> movieList;

    public HistoryAdapter(Context context, List<MovieItem> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);
        holder.tvTitle.setText(movie.getName());

        String posterUrl = movie.getPosterUrl();
        if (posterUrl != null && !posterUrl.startsWith("http")) {
            posterUrl = "https://phimimg.com/" + posterUrl;
        }

        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgPoster);

        // Xử lý sự kiện nhấn vào phim
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            // Truyền dữ liệu sang trang chi tiết
            intent.putExtra("movie_slug", movie.getId());
            intent.putExtra("movie_image", movie.getPosterUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
        }
    }
}