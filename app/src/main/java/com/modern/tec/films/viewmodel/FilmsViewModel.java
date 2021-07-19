package com.modern.tec.films.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.data.Network;
import com.modern.tec.films.models.Film;
import com.modern.tec.films.repository.FilmRepository;

import java.util.List;

public class FilmsViewModel extends AndroidViewModel {
    FilmRepository filmRepository;
    LiveData<List<Film>> allFilms;

    public FilmsViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        filmRepository = new FilmRepository(application);
        allFilms = filmRepository.getAllFilms();

        if (Network.isNetworkAvailable(application)) {
            filmRepository.getFilmsFromServer();
        }

    }

    public LiveData<List<Film>> getAllFilms() {
        return allFilms;
    }


    public void insert(Film film) {
        filmRepository.insert(film);
    }

    public void insertAllFilms(List<Film> films){
        filmRepository.insertAllFilms(films);
    }

    public void update(Film film) {
        filmRepository.update(film);
    }

    public void delete(Film film) {
        filmRepository.delete(film);
    }

    public void deleteAllFilms() {
        filmRepository.deleteAll();
    }
}
