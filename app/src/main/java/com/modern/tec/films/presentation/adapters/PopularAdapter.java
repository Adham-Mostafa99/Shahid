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
import com.modern.tec.films.databinding.ItemPopularFilmBinding;

import org.jetbrains.annotations.NotNull;


public class PopularAdapter extends ListAdapter<Film, PopularAdapter.ViewHolder> {
    private OnItemClick onItemClick;
    private Context context;

    private static final DiffUtil.ItemCallback<Film> diffCallback = new DiffUtil.ItemCallback<Film>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return oldItem.getFilmId() == newItem.getFilmId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Film oldItem, @NonNull @NotNull Film newItem) {
            return (oldItem.getFilmId() == newItem.getFilmId() &&
                    oldItem.getFilmPhoto().equals(newItem.getFilmPhoto()) &&
                    oldItem.getFilmTitle().equals(newItem.getFilmTitle()) &&
                    oldItem.getFilmVote() == newItem.getFilmVote() &&
                    oldItem.getFilmVoteCount() == newItem.getFilmVoteCount());
        }
    };

    public PopularAdapter() {
        super(diffCallback);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(ItemPopularFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PopularAdapter.ViewHolder holder, int position) {

        Film currentFilm = getItem(position);

        holder.binding.itemPopularName.setText(currentFilm.getFilmTitle());

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();
        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.film_place_holder)
                .into(holder.binding.itemPopularImage);


        holder.binding.itemPopularRating.setScore((float) currentFilm.getFilmVote() / 2);//rating from source is from 10 so divide by 2 to set it from 5


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
        ItemPopularFilmBinding binding;

        public ViewHolder(@NonNull @NotNull ItemPopularFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClick {
        void onClick(Film film);
    }
}
