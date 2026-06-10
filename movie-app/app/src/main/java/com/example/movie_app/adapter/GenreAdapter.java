package com.example.movie_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.models.Genre;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private Context context;
    private List<Genre> genreList;
    private int selectedPosition = 0;

    public GenreAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
        if (!this.genreList.isEmpty()) {
            this.genreList.get(0).setSelected(true);
        }
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.tvGenreName.setText(genre.getName());

        if (genre.isSelected()) {

            holder.tvGenreName.setTextColor(Color.parseColor("#E50914"));
            holder.tvGenreName.setBackgroundResource(R.drawable.genre_bg_normal);
        } else {
            holder.tvGenreName.setTextColor(Color.parseColor("#E2E2E8"));
            holder.tvGenreName.setBackgroundResource(R.drawable.genre_bg_selected);
        }

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            if (selectedPosition != currentPosition) {
                genreList.get(selectedPosition).setSelected(false);
                notifyItemChanged(selectedPosition);

                selectedPosition = currentPosition;
                genreList.get(selectedPosition).setSelected(true);
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList != null ? genreList.size() : 0;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenreName;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView instanceof TextView) {
                tvGenreName = (TextView) itemView;
            } else {
                tvGenreName = itemView.findViewById(R.id.tvGenreName);
            }
        }
    }
}