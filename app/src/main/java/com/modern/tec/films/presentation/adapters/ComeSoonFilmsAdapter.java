package com.modern.tec.films.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.ItemComeSoonFilmBinding;

import org.jetbrains.annotations.NotNull;

public class ComeSoonFilmsAdapter extends ListAdapter<Film, ComeSoonFilmsAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Film> diffCallback = new DiffUtil.ItemCallback<Film>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return oldItem.getFilmId() == newItem.getFilmId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return (oldItem.getFilmId() == newItem.getFilmId() &&
                    oldItem.getFilmPhoto().equals(newItem.getFilmPhoto()) &&
                    oldItem.getFilmTitle().equals(newItem.getFilmTitle()));
        }
    };
    private Context context;
    private OnClickItem onClickItem;

    public ComeSoonFilmsAdapter() {
        super(diffCallback);
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(ItemComeSoonFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComeSoonFilmsAdapter.ViewHolder holder, int position) {

        Film currentFilm = getItem(position);

        holder.binding.itemComeSoonName.setText(currentFilm.getFilmTitle());

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();
        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.film_place_holder)
                .into(holder.binding.itemComeSoonImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItem != null && position != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(currentFilm);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemComeSoonFilmBinding binding;

        public ViewHolder(ItemComeSoonFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickItem {
        void onClick(Film film);
    }
}
