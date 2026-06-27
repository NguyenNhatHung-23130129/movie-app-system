package com.example.movie_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.R;
import com.example.movie_app.models.ReportedContentDto;

public class ReportedContentAdapter extends ListAdapter<ReportedContentDto, ReportedContentAdapter.ViewHolder> {

    public interface OnReportActionListener {
        void onAction(ReportedContentDto report);
    }

    private final OnReportActionListener onIgnoreListener;
    private final OnReportActionListener onDeleteListener;

    public ReportedContentAdapter(OnReportActionListener onIgnoreListener, OnReportActionListener onDeleteListener) {
        super(new DiffUtil.ItemCallback<ReportedContentDto>() {
            @Override
            public boolean areItemsTheSame(@NonNull ReportedContentDto oldItem, @NonNull ReportedContentDto newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ReportedContentDto oldItem, @NonNull ReportedContentDto newItem) {
                return oldItem.getStatus().equals(newItem.getStatus());
            }
        });
        this.onIgnoreListener = onIgnoreListener;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reported_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportedContentDto report = getItem(position);
        holder.tvTitle.setText(report.getContentTitle());
        holder.tvReason.setText(report.getReason());
        holder.tvReporterInfo.setText("Bởi: " + report.getReporterName() + " | " + report.getTimestamp());

        holder.btnIgnore.setOnClickListener(v -> onIgnoreListener.onAction(report));
        holder.btnDelete.setOnClickListener(v -> onDeleteListener.onAction(report));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvReason, tvReporterInfo, btnIgnore, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_content_title);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvReporterInfo = itemView.findViewById(R.id.tv_reporter_info);
            btnIgnore = itemView.findViewById(R.id.btn_ignore);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}