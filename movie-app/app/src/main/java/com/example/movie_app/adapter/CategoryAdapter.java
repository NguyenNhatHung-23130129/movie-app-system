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
import com.example.movie_app.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int selectedPosition = 0;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void updateList(List<Category> newList) {
        this.categoryList = newList;
        this.selectedPosition = 0;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        boolean isSelected = (position == selectedPosition);
        holder.tvCategoryName.setSelected(isSelected);

        if (isSelected) {
            holder.tvCategoryName.setTextColor(Color.WHITE);
            holder.tvCategoryName.setBackgroundResource(R.drawable.genre_bg_selected);
        } else {
            holder.tvCategoryName.setTextColor(Color.parseColor("#E2E2E8"));
            holder.tvCategoryName.setBackgroundResource(R.drawable.genre_bg_normal);
        }

        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && selectedPosition != currentPos) {
                int previousPosition = selectedPosition;
                selectedPosition = currentPos;

                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);

                if (listener != null) listener.onCategoryClick(categoryList.get(selectedPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = (categoryList != null ? categoryList.size() : 0);
        android.util.Log.d("ADAPTER_DEBUG", "Category list size: " + count);
        return count;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvGenreName);
        }
    }
}