package com.modern.tec.films.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.bumptech.glide.Glide;
import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.ActivityDetailsBinding;
import com.modern.tec.films.presentation.adapters.ActorAdapter;
import com.modern.tec.films.presentation.adapters.DetailsAdapter;
import com.modern.tec.films.presentation.viewmodel.FilmsViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static String FILM_DETAILS_INTENT = "film_details";
    private ActivityDetailsBinding binding;
    private Film currentFilm;
    private DetailsAdapter adapter;
    private ActorAdapter actorAdapter;
    private ArrayList<String> categoriesList;
    private FilmsViewModel filmsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding());

        initViewModels();
        getCurrentFilm();

        init();
        initAdapter();
        initRecycler();
        onBackButton();
        onFavButton();

    }

    private void onFavButton() {
        binding.detailsFav.setOnClickListener(v -> {
            //TODO make film fav
        });
    }

    private void initViewModels() {
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    private void onBackButton() {
        binding.detailsBackButton.setOnClickListener(v -> onBackPressed());
    }

    private void initRecycler() {
        binding.detailsRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        binding.detailsRecycler.setAdapter(adapter);
        binding.detailsRecycler.setHasFixedSize(true);
        adapter.submitList(categoriesList);

        binding.detailsStarsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.detailsStarsRecycler.setHasFixedSize(true);
        binding.detailsStarsRecycler.setAdapter(actorAdapter);
    }

    private void initAdapter() {
        adapter = new DetailsAdapter();
        actorAdapter = new ActorAdapter();
    }

    private void getCurrentFilm() {
        currentFilm = (Film) getIntent().getSerializableExtra(FILM_DETAILS_INTENT);
    }

    private void init() {
        categoriesList = new ArrayList<>();

        categoriesList.addAll(currentFilm.getFilmsGenreNames());
        categoriesList.add("2h 3m");//TODO set time
        categoriesList.add(getFilmYear(currentFilm.getFilmReleaseDate()));


        String photoPath = getString(R.string.photo_base_url) + currentFilm.getFilmPhoto();
        Glide
                .with(this)
                .load(photoPath)
                .placeholder(R.drawable.poster_not_found)
                .into(binding.detailsFilmImage);

        binding.detailsFilmName.setText(currentFilm.getFilmTitle());

        String vote = String.format("%.1f%n", currentFilm.getFilmVote() / 2);
        binding.detailsFilmRankDigit.setText(vote);

        binding.detailsFilmRankStar.setScore(Float.parseFloat(vote));


        binding.detailsFilmOverview.setText(currentFilm.getFilmOverview());
        binding.detailsFilmOverview.setMovementMethod(new ScrollingMovementMethod());

        filmsViewModel.getFilmActors(currentFilm.getFilmId()).observe(this, new Observer<List<Actor>>() {
            @Override
            public void onChanged(List<Actor> actors) {
                actorAdapter.submitList(actors);
            }
        });

        if(currentFilm.isFav())//TODO make it livedata
            binding.detailsFav.setImageResource(R.drawable.favourite_button_clicked);
        else
            binding.detailsFav.setImageResource(R.drawable.favourite_button);


    }

    private View getBinding() {
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private String getFilmYear(String releaseDate) {
        return releaseDate.split("-")[0];
    }
}