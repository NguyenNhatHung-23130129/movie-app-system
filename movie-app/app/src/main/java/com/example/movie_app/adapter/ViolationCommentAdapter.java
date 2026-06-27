package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.models.ViolationCommentDto;

public class ViolationCommentAdapter extends ListAdapter<ViolationCommentDto, ViolationCommentAdapter.CommentViewHolder> {

    public interface OnCommentActionListener {
        void onShowContext(ViolationCommentDto comment);
        void onIgnore(ViolationCommentDto comment);
        void onDelete(ViolationCommentDto comment);
    }

    private final OnCommentActionListener listener;

    public ViolationCommentAdapter(OnCommentActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<ViolationCommentDto> DIFF_CALLBACK = new DiffUtil.ItemCallback<ViolationCommentDto>() {
        @Override
        public boolean areItemsTheSame(@NonNull ViolationCommentDto oldItem, @NonNull ViolationCommentDto newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ViolationCommentDto oldItem, @NonNull ViolationCommentDto newItem) {
            return oldItem.getContent().equals(newItem.getContent()) &&
                   oldItem.getReason().equals(newItem.getReason());
        }
    };

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_violation_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        ViolationCommentDto comment = getItem(position);
        holder.bind(comment, listener);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvUserName, tvCommentContent, tvViolationReason;
        Button btnIgnore, btnDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            tvViolationReason = itemView.findViewById(R.id.tv_violation_reason);
            btnIgnore = itemView.findViewById(R.id.btn_ignore);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(ViolationCommentDto comment, OnCommentActionListener listener) {
            tvMovieTitle.setText(comment.getMovieTitle());
            tvUserName.setText(comment.getUserName());
            tvCommentContent.setText(comment.getContent());
            tvViolationReason.setText("Lý do: " + comment.getReason());

            itemView.setOnClickListener(v -> listener.onShowContext(comment));
            btnIgnore.setOnClickListener(v -> listener.onIgnore(comment));
            btnDelete.setOnClickListener(v -> listener.onDelete(comment));
        }
    }
}