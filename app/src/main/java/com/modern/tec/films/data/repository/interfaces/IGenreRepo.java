package com.modern.tec.films.data.repository.interfaces;

import androidx.lifecycle.LiveData;

import com.modern.tec.films.core.models.Genre;

import java.util.List;
import java.util.Map;

public interface IGenreRepo {
    String KEY = "d8f59d50a0380e7168b45e9d51f40eca";
    String BASE_URL = "https://api.themoviedb.org/3/";

    LiveData<List<Genre>> getGenres();

    void storeGenreInInternalMemory(Map<Integer, String> genreMap);

    Map<Integer, String> getGenreFromInternalMemory();

}
