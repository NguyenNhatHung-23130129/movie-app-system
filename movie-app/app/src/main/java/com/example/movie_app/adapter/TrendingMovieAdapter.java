package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.models.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class TrendingMovieAdapter extends RecyclerView.Adapter<TrendingMovieAdapter.ViewHolder> {

    private List<MovieItem> movieList = new ArrayList<>();
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(MovieItem movie);
    }

    public TrendingMovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<MovieItem> newList) {
        this.movieList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvSubInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvSubInfo = itemView.findViewById(R.id.tv_movie_category);
        }

        public void bind(MovieItem movie, OnMovieClickListener listener) {
            tvTitle.setText(movie.getName());
            tvSubInfo.setText(movie.getOriginName());

            String imageUrl = (movie.getFullThumbUrl() != null && !movie.getFullThumbUrl().isEmpty())
                    ? movie.getFullThumbUrl()
                    : movie.getFullPosterUrl();

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgPoster);

            itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        }
    }
}
