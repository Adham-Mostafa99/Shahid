package com.modern.tec.films.presintation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.ItemPopularFilmBinding;
import com.modern.tec.films.databinding.ItemRankingFilmBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RankingFilmAdapter extends ListAdapter<Film, RankingFilmAdapter.ViewHolder> {

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
            return (oldItem.getFilmId() == newItem.getFilmId() &&
                    oldItem.getFilmPhoto().equals(newItem.getFilmPhoto()) &&
                    oldItem.getFilmTitle().equals(newItem.getFilmTitle()) &&
                    oldItem.getFilmVote() == newItem.getFilmVote() &&
                    oldItem.getFilmVoteCount() == newItem.getFilmVoteCount());
        }
    };

    public RankingFilmAdapter() {
        super(diffCallback);

    }

    @Override
    public void submitList(@Nullable @org.jetbrains.annotations.Nullable List<Film> list) {
        super.submitList(list);
        lastFilm = getItem(getItemCount() - 1);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setLoadMoreData(LoadMoreData loadMoreData) {
        this.loadMoreData = loadMoreData;
    }

    @NonNull
    @NotNull
    @Override
    public RankingFilmAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(ItemRankingFilmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RankingFilmAdapter.ViewHolder holder, int position) {

        Film currentFilm = getItem(position);

        if (currentFilm.equals(lastFilm)) {
            loadMoreData.onLoadMore();
        }

        holder.binding.itemRankingName.setText(currentFilm.getFilmTitle());

        String photoPath = context.getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();
        if (currentFilm.getFilmPhoto() != null)
            Glide.with(context)
                    .load(photoPath)
                    .placeholder(R.drawable.film_place_holder)
                    .into(holder.binding.itemRankingImage);
        else
            holder.binding.itemRankingImage.setImageResource(R.drawable.poster_not_found);

        StringBuilder filmGenresBuilder = new StringBuilder();
        for (String name : currentFilm.getFilmsGenreNames()) {
            filmGenresBuilder.append(name).append("/");
        }
        if (filmGenresBuilder.length() > 0) {
            String filmGenres = filmGenresBuilder.deleteCharAt(filmGenresBuilder.length() - 1).toString();
            holder.binding.itemRankingType.setText(filmGenres);
        }

        String vote = String.format("%.1f%n", currentFilm.getFilmVote() / 2);//vote in 1-digit
        holder.binding.itemRankingValue.setText(vote);


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
        ItemRankingFilmBinding binding;

        public ViewHolder(ItemRankingFilmBinding binding) {
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
