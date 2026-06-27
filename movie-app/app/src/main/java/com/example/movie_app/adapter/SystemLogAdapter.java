package com.example.movie_app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.models.SystemLogDto;

public class SystemLogAdapter extends ListAdapter<SystemLogDto, SystemLogAdapter.ViewHolder> {

    public SystemLogAdapter() {
        super(new DiffUtil.ItemCallback<SystemLogDto>() {
            @Override
            public boolean areItemsTheSame(@NonNull SystemLogDto oldItem, @NonNull SystemLogDto newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull SystemLogDto oldItem, @NonNull SystemLogDto newItem) {
                return oldItem.getDescription().equals(newItem.getDescription()) &&
                       oldItem.getSeverity().equals(newItem.getSeverity());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SystemLogDto log = getItem(position);
        holder.tvEventType.setText(log.getEventType());
        holder.tvTimestamp.setText(log.getTimestamp());
        holder.tvDescription.setText(log.getDescription());

        // Change color based on severity if needed
        if ("HIGH".equalsIgnoreCase(log.getSeverity()) || "CRITICAL".equalsIgnoreCase(log.getSeverity())) {
            holder.tvEventType.setTextColor(Color.parseColor("#FF5252"));
        } else {
            holder.tvEventType.setTextColor(Color.parseColor("#E2E2E8"));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventType, tvTimestamp, tvDescription;

        ViewHolder(View itemView) {
            super(itemView);
            tvEventType = itemView.findViewById(R.id.tv_event_type);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}