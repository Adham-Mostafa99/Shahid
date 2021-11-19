package com.modern.tec.films.presintation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.ItemFilmBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilmAdapter extends ListAdapter<Film, FilmAdapter.ViewHolder> {

    private LoadMoreData loadMoreData;
    private OnItemClick onItemClick;
    private Context context;
    private Film lastFilm;

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

    public FilmAdapter() {
        super(diffCallback);

    }

    @Override
    public void submitList(@Nullable @org.jetbrains.annotations.Nullable List<Film> list) {
        super.submitList(list);
        assert list != null;
        if (!list.isEmpty())
            lastFilm = list.get(getItemCount() - 1);
    }

    public void setLoadMoreData(LoadMoreData loadMoreData) {
        this.loadMoreData = loadMoreData;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(ItemFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilmAdapter.ViewHolder holder, int position) {
        Film currentFilm = getItem(position);

        if (currentFilm.equals(lastFilm)) {
            loadMoreData.onLoadMore();
        }

        holder.binding.itemName.setText(currentFilm.getFilmTitle());

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();

        if (currentFilm.getFilmPhoto() != null)
            Glide.with(context)
                    .load(photoPath)
                    .placeholder(R.drawable.film_place_holder)
                    .into(holder.binding.itemImage);
        else
            holder.binding.itemImage.setImageResource(R.drawable.poster_not_found);

        String vote = String.format("%.1f IMDb", currentFilm.getFilmVote() / 2);//vote in 1-digit
        holder.binding.itemVote.setText(vote);
        Log.v("TAG", "Vote: " + vote);


        if (currentFilm.getFilmsGenreNames().size() > 0) {
            StringBuilder filmGenresBuilder = new StringBuilder();
            for (String name : currentFilm.getFilmsGenreNames()) {
                filmGenresBuilder.append(name).append("|");
            }

            String filmGenres = filmGenresBuilder.deleteCharAt(filmGenresBuilder.length() - 1).toString();
            holder.binding.itemSearchCategory.setText(filmGenres);
        }


        holder.binding.itemDesc.setText(currentFilm.getFilmOverview());

//        StringBuilder filmActorsBuilder = new StringBuilder();
//        for (String name : currentFilm.getFilmsGenreNames()) {
//            filmActorsBuilder.append(name).append("|");
//        }
//        if (filmActorsBuilder.length() > 0) {
//            String filmGenres = filmActorsBuilder.deleteCharAt(filmActorsBuilder.length() - 1).toString();
//            holder.binding.itemSearchCategory.setText(filmGenres);
//        }


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
        ItemFilmBinding binding;

        public ViewHolder(ItemFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClick {
        void onClick(Film film);
    }

    public interface LoadMoreData {
        void onLoadMore();
    }
}
