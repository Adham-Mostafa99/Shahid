package com.modern.tec.films.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.ActivityMoviesBinding;
import com.modern.tec.films.presentation.adapters.FilmAdapter;
import com.modern.tec.films.presentation.adapters.SuggestedAdapter;
import com.modern.tec.films.presentation.viewmodel.FilmsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    public static final String TOOLBAR_NAME_INTENT = "toolbar_name";

    ActivityMoviesBinding binding;

    FilmsViewModel filmsViewModel;
    FilmAdapter filmAdapter;
    SuggestedAdapter suggestedAdapter;

    private String categoryName;

    boolean isSuggestedFilmsShown;

    List<Film> filmList;
    int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding());

        categoryName = getIntent().getStringExtra(TOOLBAR_NAME_INTENT);
        setToolbarName(categoryName);

        initViewModel();
        initAdapters();
        initRecycler();

        onBackButton();

        initPage();

        getCategoryFilms(categoryName);
    }

    private View getBinding() {
        binding = ActivityMoviesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void setToolbarName(String screen_name) {
        binding.moviesBarName.setText(screen_name);
    }


    private String getRandomId(List<Film> films) {
        int randomFilmIndex = new Random().nextInt(films.size());


        return String.valueOf(films.get(randomFilmIndex).getFilmId());
    }

    private void initPage() {
        page = 1;
    }

    private void onBackButton() {
        binding.moviesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getCategoryFilms(String categoryName) {
        filmsViewModel.getCategoryFilms(categoryName, getString(R.string.most_watched_sort), "", page)
                .observe(this, new Observer<List<Film>>() {
                    @Override
                    public void onChanged(List<Film> films) {
                        if (films.isEmpty())//if no films then get next page
                            filmsViewModel.getCategoryFilms(categoryName, "", "", ++page);
                        filmList.addAll(films);
                        filmAdapter.submitList(filmList);
                        filmAdapter.notifyDataSetChanged();

                        if (filmList.size() > 0 && !isSuggestedFilmsShown)
                            getSuggestedFilms(films);
                    }
                });
    }

    private void getSuggestedFilms(List<Film> filmsList) {// run after get list of films movie
        filmsViewModel.getSuggestedFilms(getRandomId(filmsList)).observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                Log.v("TAG", "suggest" + films.toString());
                suggestedAdapter.submitList(films);
                if (!films.isEmpty() && films.size() > 5)
                    isSuggestedFilmsShown = true;
            }
        });
    }


    private void initRecycler() {
        binding.moviesRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.moviesRecycler.setAdapter(filmAdapter);

        binding.moviesSuggestedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.moviesSuggestedRecycler.setAdapter(suggestedAdapter);

        filmList = new ArrayList<>();
    }

    private void initAdapters() {
        filmAdapter = new FilmAdapter();
        filmAdapter.setOnItemClick(onItemClick());
        filmAdapter.setLoadMoreData(onLoadMoreData());


        suggestedAdapter = new SuggestedAdapter();
        suggestedAdapter.setOnItemClick(onSuggestItemClick());
    }


    private void initViewModel() {
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    private FilmAdapter.LoadMoreData onLoadMoreData() {
        return new FilmAdapter.LoadMoreData() {
            @Override
            public void onLoadMore() {
                filmsViewModel.getCategoryFilms(categoryName, "", "", ++page);
            }
        };
    }

    private FilmAdapter.OnItemClick onItemClick() {
        return new FilmAdapter.OnItemClick() {
            @Override
            public void onClick(Film film) {
                startActivity(new Intent(getBaseContext(), DetailsActivity.class).putExtra(DetailsActivity.FILM_DETAILS_INTENT, film));
            }
        };
    }

    private SuggestedAdapter.OnItemClick onSuggestItemClick() {
        return new SuggestedAdapter.OnItemClick() {
            @Override
            public void onClick(Film film) {
                startActivity(new Intent(getBaseContext(), DetailsActivity.class).putExtra(DetailsActivity.FILM_DETAILS_INTENT, film));
            }
        };
    }
}
