package com.example.movie_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie_app.R;
import com.example.movie_app.activities.HomeActivity;
import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.models.MovieItem;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> movieList;


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
        holder.tvTitle.setText(movie.getName());

        String imageUrl = (movie.getFullThumbUrl() != null && !movie.getFullThumbUrl().isEmpty())
                ? movie.getFullThumbUrl()
                : movie.getFullPosterUrl();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            if (movie.getSlug() != null) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movie_slug", movie.getSlug());
                intent.putExtra("movie_image", imageUrl);

                if (context instanceof HomeActivity) {
                    int currentTabId = ((HomeActivity) context).getCurrentTabId();
                    intent.putExtra("ACTIVE_TAB_ID", currentTabId);
                    intent.putExtra("IS_NESTED", false);
                } else {
                    intent.putExtra("IS_NESTED", true);
                }

                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

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