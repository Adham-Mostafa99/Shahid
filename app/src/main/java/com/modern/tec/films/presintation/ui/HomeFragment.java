package com.modern.tec.films.presintation.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.core.models.Category;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.FragmentHomeBinding;
import com.modern.tec.films.presintation.adapters.CategoryAdapter;
import com.modern.tec.films.presintation.adapters.ComeSoonFilmsAdapter;
import com.modern.tec.films.presintation.adapters.PopularAdapter;
import com.modern.tec.films.presintation.viewmodel.CategoryViewModel;
import com.modern.tec.films.presintation.viewmodel.FilmsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    CategoryViewModel categoryViewModel;
    FilmsViewModel filmsViewModel;

    CategoryAdapter categoryAdapter;
    PopularAdapter popularAdapter;
    ComeSoonFilmsAdapter comeSoonFilmsAdapter;

    MainActivity activity;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initToolbar();
        initViewModels();
        initAdapters();
        initRecyclers();

        searchedListener();
        morePopularFilmsButton();

        getCategories();

        getPopularFilms();

        getComingFilms();


        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (MainActivity) context;
    }

    private void searchedListener() {

        binding.homeContent.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setNavWithCustomFragment(new SearchFragment());
            }
        });
    }


    private void getPopularFilms() {
        filmsViewModel.getPopularFilms().observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                popularAdapter.submitList(films);
            }
        });
    }

    private void getCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.submitList(categories);
            }
        });
    }

    private void getComingFilms() {
        filmsViewModel.getComingFilms().observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                comeSoonFilmsAdapter.submitList(films);
            }
        });
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }


    private void initRecyclers() {
        binding.homeContent.recyclerCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.homeContent.recyclerCategory.setHasFixedSize(true);
        binding.homeContent.recyclerCategory.setAdapter(categoryAdapter);

        binding.homeContent.recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.homeContent.recyclerPopular.setHasFixedSize(true);
        binding.homeContent.recyclerPopular.setAdapter(popularAdapter);

        binding.homeContent.recyclerComingSoon.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.homeContent.recyclerComingSoon.setHasFixedSize(true);
        binding.homeContent.recyclerComingSoon.setAdapter(comeSoonFilmsAdapter);


    }

    private void initAdapters() {
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnItemClick(onCategoryClick());

        popularAdapter = new PopularAdapter();
        popularAdapter.setOnItemClick(onPopularClick());

        comeSoonFilmsAdapter = new ComeSoonFilmsAdapter();
        comeSoonFilmsAdapter.setOnClickItem(onComingClick());


    }


    private void initViewModels() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    private CategoryAdapter.OnItemClick onCategoryClick() {
        return category -> {
            //TODO go to category
        };
    }

    private PopularAdapter.OnItemClick onPopularClick() {
        return film -> {
            //TODO go to film
        };
    }

    private ComeSoonFilmsAdapter.OnClickItem onComingClick() {
        return film -> {
            //TODO go to film
        };
    }

    public void morePopularFilmsButton() {
        binding.homeContent.txtPopularMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to more films
            }
        });
    }

}
