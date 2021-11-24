package com.modern.tec.films.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.data.network.Network;
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
        initPage();

        checkNetworkListener();

        onBackButton();
        onSearchClick();

        onGetSearchFilms();

        getCategoryFilms(categoryName);
    }

    private void checkNetworkListener() {
        Network network = new Network(getApplication());
        network.getIsNetworkAvailable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    binding.activityNetworkText.setVisibility(View.GONE);
                else
                    binding.activityNetworkText.setVisibility(View.VISIBLE);
            }
        });
    }


    private void onGetSearchFilms() {
        filmsViewModel.getSearchedFilmsLiveData().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {

                if (films != null) {
                    filmAdapter.notifyDataSetChanged();
                    filmList.addAll(films);
                    filmAdapter.submitList(filmList);

                    if (filmList.isEmpty())
                        binding.txtNoMovie.setVisibility(View.VISIBLE);
                    else
                        binding.txtNoMovie.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Check your connection, and try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void resetPage() {
        page = 1;
    }

    private void onSearchClick() {

        binding.filmDateEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetPage();
                if (!s.toString().isEmpty()) {
                    resetAdapter();
                    searchFilm(s.toString());
                } else {
                    resetAdapter();
                    filmsViewModel.getCategoryFilms(categoryName, getString(R.string.most_watched_sort), "", page);
                }

            }
        });

    }

    private void searchFilm(String filmName) {
        filmsViewModel.searchFilms(filmName, categoryName, page);
    }

    private void resetAdapter() {
        filmList.clear();
        filmAdapter.submitList(filmList);

    }

    private void setToolbarName(int screen_name) {
        binding.moviesBarName.setText(getString(screen_name));
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
                        if (films != null) {
                            if (films.isEmpty())//if no films then get next page
                                filmsViewModel.getCategoryFilms(categoryName, getString(R.string.top_rated_sort), "", ++page);
                            filmAdapter.notifyDataSetChanged();
                            filmList.addAll(films);
                            filmAdapter.submitList(filmList);

                            if (filmList.size() > 0 && !isSuggestedFilmsShown)
                                getSuggestedFilms();

                        }
                        else {
                            binding.txtNoMovie.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Check your connection, and try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getSuggestedFilms() {// run after get list of films movie
        filmsViewModel.getSuggestedFilms(getRandomId(filmList)).observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                if (!films.isEmpty() && films.size() > 5) {
                    isSuggestedFilmsShown = true;
                    suggestedAdapter.submitList(films);
                }
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
                filmsViewModel.getCategoryFilms(categoryName, getString(R.string.most_watched_sort), "", ++page);
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
