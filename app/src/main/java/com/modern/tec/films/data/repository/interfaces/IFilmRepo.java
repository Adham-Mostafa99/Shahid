package com.modern.tec.films.data.repository.interfaces;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.core.models.Genre;

import java.util.List;

public interface IFilmRepo {
    String KEY = "d8f59d50a0380e7168b45e9d51f40eca";
    String BASE_URL = "https://api.themoviedb.org/3/";


    LiveData<List<Film>> getPopularFilms(int pageNumber);

    LiveData<List<Film>> getComingFilms();


    void getDiscoveredFilms(String sortType, String year, int pageNumber);

    LiveData<List<Film>> getCategoryFilms(String categoryName, String sortType, String year, int pageNumber);

    void searchFilms(String name, String categoryFilms, int page);


    LiveData<List<Actor>> getFilmActors(int filmId);

    LiveData<List<Film>> getSuggestedFilms(String filmId);


    LiveData<Boolean> insertFilmToFavoriteTable(Film film);

    LiveData<Boolean> insertListFilmsToFavoriteTable(List<Film> filmList);

    LiveData<Boolean> deleteFilmFromTable(Film film);

    LiveData<Boolean> deleteAllFilms();

    LiveData<List<Film>> getAllFilmsFromTable();

    LiveData<Boolean> isFilmExistInTable(int filmId);

}
