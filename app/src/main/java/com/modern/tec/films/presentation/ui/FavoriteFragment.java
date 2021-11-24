package com.modern.tec.films.presentation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.FragmentFavoriteBinding;
import com.modern.tec.films.presentation.adapters.FavoriteFilmsAdapter;
import com.modern.tec.films.presentation.viewmodel.FilmsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteFragment extends Fragment {
    FragmentFavoriteBinding binding;
    private FavoriteFilmsAdapter adapter;
    FilmsViewModel filmsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);


        initViewModels();
        initAdapters();
        initRecyclers();


        onBackClick();


        getFilmsFromTable();

        return binding.getRoot();
    }

    private void getFilmsFromTable() {
        filmsViewModel.getAllFilmsFromTable().observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> filmList) {
                adapter.submitList(filmList);
                if(filmList.isEmpty())
                    binding.txtNoMovie.setVisibility(View.VISIBLE);
                else
                    binding.txtNoMovie.setVisibility(View.GONE);
            }
        });
    }

    private void initRecyclers() {
        binding.favRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.favRecycler.setHasFixedSize(true);
        binding.favRecycler.setAdapter(adapter);
    }

    private void initAdapters() {
        adapter = new FavoriteFilmsAdapter();
        adapter.setOnFavButtonClick(onFavButtonClick());
        adapter.setOnItemClick(onItemClick());
    }

    private FavoriteFilmsAdapter.OnItemClick onItemClick() {

        return new FavoriteFilmsAdapter.OnItemClick() {
            @Override
            public void onClick(Film film) {
                startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra(DetailsActivity.FILM_DETAILS_INTENT, film));

            }
        };
    }

    private FavoriteFilmsAdapter.OnFavButtonClick onFavButtonClick() {
        return new FavoriteFilmsAdapter.OnFavButtonClick() {
            @Override
            public void onClick(Film film) {
                filmsViewModel.deleteFilmFromTable(film);
            }
        };
    }

    private void initViewModels() {
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    private MainActivity activity;


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (MainActivity) context;
    }

    private void onBackClick() {
        binding.favBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setNavWithCustomFragment(new HomeFragment());
            }
        });
    }
}
