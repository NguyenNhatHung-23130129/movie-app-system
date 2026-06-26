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
import com.example.movie_app.models.Category;
import com.example.movie_app.models.MovieItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminMovieAdapter extends RecyclerView.Adapter<AdminMovieAdapter.AdminMovieViewHolder> {

    private List<MovieItem> movieList = new ArrayList<>();
    private OnMovieActionListener listener;

    public interface OnMovieActionListener {
        void onEdit(MovieItem movie);
        void onDelete(MovieItem movie);
    }

    public void setOnMovieActionListener(OnMovieActionListener listener) {
        this.listener = listener;
    }

    public void setMovieList(List<MovieItem> newList) {
        this.movieList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_card, parent, false);
        return new AdminMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMovieViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class AdminMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster, btnEdit, btnDelete;
        TextView tvTitle, tvCategory, tvStatus, tvRating, tvViews;

        public AdminMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvCategory = itemView.findViewById(R.id.tv_movie_category);
            tvStatus = itemView.findViewById(R.id.tv_movie_status);
            tvRating = itemView.findViewById(R.id.tv_movie_rating);
            tvViews = itemView.findViewById(R.id.tv_movie_views);
            btnEdit = itemView.findViewById(R.id.btn_movie_edit);
            btnDelete = itemView.findViewById(R.id.btn_movie_delete);
        }

        public void bind(MovieItem movie, OnMovieActionListener listener) {
            tvTitle.setText(movie.getName());
            
            String categories = "";
            if (movie.getCategory() != null) {
                categories = movie.getCategory().stream()
                        .map(Category::getName)
                        .collect(Collectors.joining(", "));
            }
            tvCategory.setText(movie.getYear() + " • " + categories);

            // Giả lập dữ liệu cho status, rating, views nếu chưa có trong model
            tvStatus.setText("ĐÃ XUẤT BẢN"); 
            tvRating.setText("8.5");
            tvViews.setText("0 lượt xem");

            Glide.with(itemView.getContext())
                    .load(movie.getFullThumbUrl())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(imgPoster);

            btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(movie);
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(movie);
            });
        }
    }
}
