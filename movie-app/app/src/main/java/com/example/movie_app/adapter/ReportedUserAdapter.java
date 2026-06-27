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
import com.example.movie_app.models.AdminUserDto;

public class ReportedUserAdapter extends ListAdapter<AdminUserDto, ReportedUserAdapter.UserViewHolder> {

    public interface OnUserActionListener {
        void onToggleLock(AdminUserDto user);
    }

    private final OnUserActionListener listener;

    public ReportedUserAdapter(OnUserActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<AdminUserDto> DIFF_CALLBACK = new DiffUtil.ItemCallback<AdminUserDto>() {
        @Override
        public boolean areItemsTheSame(@NonNull AdminUserDto oldItem, @NonNull AdminUserDto newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AdminUserDto oldItem, @NonNull AdminUserDto newItem) {
            return oldItem.isLocked() == newItem.isLocked() && 
                   oldItem.getReportCount() == newItem.getReportCount() &&
                   oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reported_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        AdminUserDto user = getItem(position);
        holder.bind(user, listener);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvReportCount;
        Button btnToggleLock;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvReportCount = itemView.findViewById(R.id.tv_report_count);
            btnToggleLock = itemView.findViewById(R.id.btn_toggle_lock);
        }

        public void bind(AdminUserDto user, OnUserActionListener listener) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvReportCount.setText("Báo cáo: " + user.getReportCount());
            
            btnToggleLock.setText(user.isLocked() ? "Mở khóa" : "Khóa");
            btnToggleLock.setOnClickListener(v -> listener.onToggleLock(user));
        }
    }
}