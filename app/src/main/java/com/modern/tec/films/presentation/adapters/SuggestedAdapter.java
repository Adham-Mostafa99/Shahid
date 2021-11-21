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
import com.modern.tec.films.databinding.ItemSuggestedBinding;

import org.jetbrains.annotations.NotNull;


public class SuggestedAdapter extends ListAdapter<Film, SuggestedAdapter.ViewHolder> {

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

    public SuggestedAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SuggestedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(ItemSuggestedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedAdapter.ViewHolder holder, int position) {
        Film currentFilm = getItem(position);

        holder.binding.itemSuggestName.setText(currentFilm.getFilmTitle());


        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();
        if (currentFilm.getFilmPhoto() != null)
            Glide.with(context)
                    .load(photoPath)
                    .placeholder(R.drawable.film_place_holder)
                    .into(holder.binding.itemSuggestImage);
        else
            holder.binding.itemSuggestImage.setImageResource(R.drawable.poster_not_found);


        String vote = String.format("%.1f%n", currentFilm.getFilmVote() / 2);//vote in 1-digit
        holder.binding.itemSuggestRate.setScore(Float.parseFloat(vote));

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
        ItemSuggestedBinding binding;

        public ViewHolder(@NonNull ItemSuggestedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onClick(Film film);
    }
}
