package com.modern.tec.films.data;

import com.modern.tec.films.models.Film;
import com.modern.tec.films.models.FilmsRespond;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FilmApi {
    @GET("3/trending/movie/day?api_key=d8f59d50a0380e7168b45e9d51f40eca")
    Call<FilmsRespond> getAllFilms();
}
