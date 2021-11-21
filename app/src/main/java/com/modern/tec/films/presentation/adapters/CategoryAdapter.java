package com.modern.tec.films.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.core.models.Category;
import com.modern.tec.films.databinding.ItemCategoriesBinding;

import org.jetbrains.annotations.NotNull;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.ViewHolder> {
    private OnItemClick onItemClick;

    private static final DiffUtil.ItemCallback<Category> diffCallback = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Category oldItem, @NonNull @NotNull Category newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Category oldItem, @NonNull @NotNull Category newItem) {
            return false;
        }
    };

    public CategoryAdapter() {
        super(diffCallback);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Category current = getItem(position);

        holder.binding.itemCategoryImage.setImageResource(current.getCategoryImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null && position != RecyclerView.NO_POSITION) {
                    onItemClick.onItemCLick(current);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;

        public ViewHolder(@NonNull @NotNull ItemCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

 public    interface OnItemClick {
        void onItemCLick(Category category);
    }
}
