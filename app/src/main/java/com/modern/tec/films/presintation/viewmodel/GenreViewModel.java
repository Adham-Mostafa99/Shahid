package com.modern.tec.films.presintation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.modern.tec.films.core.models.Genre;
import com.modern.tec.films.data.repository.GenreServices;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class GenreViewModel extends AndroidViewModel {

    GenreServices genreServices;

    public GenreViewModel(@NonNull @NotNull Application application) {
        super(application);
        genreServices = new GenreServices(application);
    }

    public LiveData<List<Genre>> getGenres() {
        return genreServices.getGenres();
    }


  public   void storeGenreInInternalMemory(Map<Integer, String> genreList) {

        genreServices.storeGenreInInternalMemory(genreList);
    }

    Map<Integer, String> getGenreFromInternalMemory() {

        return genreServices.getGenreFromInternalMemory();
    }

}
