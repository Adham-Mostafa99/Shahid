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
import com.modern.tec.films.databinding.ItemFavFilmBinding;

import org.jetbrains.annotations.NotNull;

public class FavoriteFilmsAdapter extends ListAdapter<Film, FavoriteFilmsAdapter.ViewHolder> {
    private Context context;
    private OnFavButtonClick onFavButtonClick;
    private OnItemClick onItemClick;
    private static final DiffUtil.ItemCallback<Film> diffCallback = new DiffUtil.ItemCallback<Film>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return oldItem.getFilmId() == newItem.getFilmId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return (oldItem.getFilmOverview().equals(newItem.getFilmOverview()) &&
                    oldItem.getFilmReleaseDate().equals(newItem.getFilmReleaseDate()) &&
                    oldItem.getFilmTitle().equals(newItem.getFilmTitle()) &&
                    oldItem.getFilmVote() == newItem.getFilmVote() &&
                    oldItem.getFilmVoteCount() == newItem.getFilmVoteCount());
        }
    };

    public FavoriteFilmsAdapter() {
        super(diffCallback);
    }

    public void setOnFavButtonClick(OnFavButtonClick onFavButtonClick) {
        this.onFavButtonClick = onFavButtonClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(ItemFavFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film currentFilm = getItem(position);

        holder.binding.itemFavName.setText(currentFilm.getFilmTitle());

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();

        if (currentFilm.getFilmPhoto() != null)
            Glide.with(context)
                    .load(photoPath)
                    .placeholder(R.drawable.film_place_holder)
                    .into(holder.binding.itemFavImage);
        else
            holder.binding.itemFavImage.setImageResource(R.drawable.poster_not_found);

        holder.binding.itemFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavButtonClick.onClick(currentFilm);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null && position != RecyclerView.NO_POSITION) {
                    onItemClick.onClick(currentFilm);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFavFilmBinding binding;

        public ViewHolder(@NonNull ItemFavFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnFavButtonClick {
        void onClick(Film film);
    }

    public interface OnItemClick {
        void onClick(Film film);
    }

}
