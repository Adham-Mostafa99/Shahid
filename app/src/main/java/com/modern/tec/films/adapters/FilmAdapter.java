package com.modern.tec.films.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.models.Film;

import org.jetbrains.annotations.NotNull;

public class FilmAdapter extends ListAdapter<Film, FilmAdapter.ViewHolder> {

    private static final String TAG = "FilmAdapter";
    private Context context;

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

    public FilmAdapter(Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilmAdapter.ViewHolder holder, int position) {

        Film currentFilm = getItem(position);

        holder.filmTitle.setText(currentFilm.getFilmTitle());
        holder.filmOverview.setText(currentFilm.getFilmOverview());
        holder.filmDate.setText(currentFilm.getFilmReleaseDate());
        holder.filmVoteAvg.setText(String.valueOf(currentFilm.getFilmVote()));
        holder.filmVoteCount.setText(String.valueOf(currentFilm.getFilmVoteCount()));

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();

        Glide.with(context)
                .load(photoPath)
                .centerCrop()
                .placeholder(R.drawable.film_place_holder)
                .into(holder.filmPhoto);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView filmPhoto;
        private TextView filmTitle;
        private TextView filmOverview;
        private TextView filmVoteCount;
        private TextView filmVoteAvg;
        private TextView filmDate;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            filmPhoto = itemView.findViewById(R.id.film_item_photo);
            filmTitle = itemView.findViewById(R.id.film_item_title);
            filmOverview = itemView.findViewById(R.id.film_item_over_view);
            filmVoteCount = itemView.findViewById(R.id.film_item_vote_count);
            filmVoteAvg = itemView.findViewById(R.id.film_item_vote_average);
            filmDate = itemView.findViewById(R.id.film_item_release_date);
        }
    }
}
