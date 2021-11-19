package com.modern.tec.films.data.repository.interfaces;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.core.models.Genre;

import java.util.List;

public interface IFilmRepo {
    String KEY = "d8f59d50a0380e7168b45e9d51f40eca";
    String BASE_URL = "https://api.themoviedb.org/3/";


    LiveData<List<Film>> getPopularFilms();

    LiveData<List<Film>> getComingFilms();


    void getDiscoveredFilms(String sortType, String year, int pageNumber);

    void searchFilms(String name, int page);


}
