package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.models.Comment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCommentAdapter extends RecyclerView.Adapter<AdminCommentAdapter.AdminCommentViewHolder> {

    private List<Comment> commentList = new ArrayList<>();
    private final OnCommentDeleteListener deleteListener;

    public interface OnCommentDeleteListener {
        void onDelete(Comment comment);
    }

    public AdminCommentAdapter(OnCommentDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void submitList(List<Comment> newList) {
        this.commentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new AdminCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.tvUsername.setText(comment.getUsername() != null ? comment.getUsername() : "Ẩn danh");
        holder.tvContent.setText(comment.getContentComment() != null ? comment.getContentComment() : "");
        holder.ratingBar.setRating((float) comment.getRating());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            holder.tvTime.setText(sdf.format(new Date(comment.getTimestamp())));
        } catch (Exception e) {
            holder.tvTime.setText("Vừa xong");
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(comment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public static class AdminCommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent, tvTime;
        RatingBar ratingBar;
        ImageButton btnDelete;

        public AdminCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvCommentUser);
            tvContent = itemView.findViewById(R.id.tvCommentContent);
            tvTime = itemView.findViewById(R.id.tvCommentTime);
            ratingBar = itemView.findViewById(R.id.rbCommentRating);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);

            if (btnDelete != null) {
                btnDelete.setVisibility(View.VISIBLE);
            }
        }
    }
}