package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private List<String> historyList;
    private final OnHistoryClickListener listener;
    private boolean isHistoryMode = true;

    public interface OnHistoryClickListener {
        void onHistoryClick(String query);
        void onDeleteClick(String query);
    }

    public SearchHistoryAdapter(List<String> historyList, OnHistoryClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
    }

    // Cập nhật thêm tham số mode
    public void updateList(List<String> newList, boolean isHistoryMode) {
        this.historyList = newList;
        this.isHistoryMode = isHistoryMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String query = historyList.get(position);
        holder.tvHistory.setText(query);
        holder.btnDelete.setVisibility(isHistoryMode ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onHistoryClick(query));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(query));
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistory;
        ImageView btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistory = itemView.findViewById(R.id.tvHistory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}