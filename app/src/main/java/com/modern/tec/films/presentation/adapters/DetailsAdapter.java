package com.modern.tec.films.presentation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.databinding.ItemDetailsFilmBinding;


public class DetailsAdapter extends ListAdapter<String, DetailsAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    public DetailsAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDetailsFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.itemDetailsFilmText.setText(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDetailsFilmBinding binding;

        public ViewHolder(@NonNull ItemDetailsFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
