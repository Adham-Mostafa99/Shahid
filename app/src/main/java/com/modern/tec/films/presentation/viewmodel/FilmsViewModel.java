package com.modern.tec.films.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.data.repository.FilmServices;

import java.util.List;

public class FilmsViewModel extends AndroidViewModel {
    FilmServices filmServices;
    LiveData<List<Film>> allFilms;

    public FilmsViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        filmServices = new FilmServices(application);
    }

    public LiveData<List<Film>> getAllFilms() {
        return allFilms;
    }

    public void searchFilms(String name, int page) {
        filmServices.searchFilms(name, page);
    }

//    public void insert(Film film) {
//        filmServices.insert(film);
//    }
//
//    public void insertAllFilms(List<Film> films) {
//        filmServices.insertAllFilms(films);
//    }
//
//    public void update(Film film) {
//        filmServices.update(film);
//    }
//
//    public void delete(Film film) {
//        filmServices.delete(film);
//    }
//
//    public void deleteAllFilms() {
//        filmServices.deleteAll();
//    }

    public LiveData<List<Film>> getPopularFilms(int pageNumber) {
        return filmServices.getPopularFilms(pageNumber);
    }

    public MutableLiveData<List<Film>> getPopularFilmsLiveData() {
        return filmServices.getPopularFilmsLiveData();
    }

    public LiveData<List<Film>> getComingFilms() {
        return filmServices.getComingFilms();
    }

    public void getDiscoveredFilms(String sortType, String year, int pageNumber) {
        filmServices.getDiscoveredFilms(sortType, year, pageNumber);

    }

    public LiveData<List<Film>> getCategoryFilms(String categoryName, String sortType, String year, int pageNumber) {
        return filmServices.getCategoryFilms(categoryName, sortType, year, pageNumber);
    }

    public LiveData<List<Actor>> getFilmActors(int filmId) {
        return filmServices.getFilmActors(filmId);
    }

    public LiveData<List<Film>> getDiscoveredFilmsLiveData() {
        return filmServices.getDiscoveredFilmsLiveData();
    }

    public LiveData<List<Film>> getSearchedFilmsLiveData() {
        return filmServices.getSearchedLiveData();
    }


    public LiveData<List<Film>> getSuggestedFilms(String filmId) {
        return filmServices.getSuggestedFilms(filmId);
    }

}
