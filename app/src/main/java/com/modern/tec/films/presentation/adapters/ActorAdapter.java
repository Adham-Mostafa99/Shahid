package com.modern.tec.films.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.databinding.ItemStarsBinding;

public class ActorAdapter extends ListAdapter<Actor, ActorAdapter.ViewHolder> {
    private Context context;
    private static final DiffUtil.ItemCallback<Actor> diffCallback = new DiffUtil.ItemCallback<Actor>() {
        @Override
        public boolean areItemsTheSame(@NonNull Actor oldItem, @NonNull Actor newItem) {
            return oldItem.getActorId() == newItem.getActorId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Actor oldItem, @NonNull Actor newItem) {
            return oldItem.getActorId() == newItem.getActorId() &&
                    oldItem.getActorCharacter().equals(newItem.getActorCharacter()) &&
                    oldItem.getActorName().equals(newItem.getActorName()) &&
                    oldItem.getActorPhoto().equals(newItem.getActorPhoto()) &&
                    oldItem.getActorGender().equals(newItem.getActorGender());
        }
    };

    public ActorAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ActorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(ItemStarsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActorAdapter.ViewHolder holder, int position) {
        Actor currentActor = getItem(position);
        String photoPath = context.getString(R.string.photo_base_url) + currentActor.getActorPhoto();

        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.poster_not_found)
                .into(holder.binding.itemStarsImage);

        holder.binding.itemStarsName.setText(currentActor.getActorName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStarsBinding binding;

        public ViewHolder(@NonNull ItemStarsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
