package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;

import com.bumptech.glide.Glide;
import com.example.movie_app.models.MovieItem;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> movieList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MovieItem movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieAdapter(List<MovieItem> movieList) {
        this.movieList = movieList;
    }

    public void setMovieList(List<MovieItem> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);

        if (movie == null) return;
        android.util.Log.d("GLIDE_URL", "Link ảnh của phim " + movie.getName() + " là: " + movie.getPosterUrl());
        holder.tvTitle.setText(movie.getName());

        String fullImageUrl = movie.getThumbUrl();
        if (fullImageUrl != null && !fullImageUrl.startsWith("http")) {
            fullImageUrl = "https://phimimg.com/" + fullImageUrl;
        }

        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(android.R.drawable.progress_horizontal)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    // ViewHolder chịu trách nhiệm ánh xạ các thành phần UI trong file home_item_movie.xml
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}