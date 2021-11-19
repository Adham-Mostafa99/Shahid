package com.modern.tec.films.data.network.interfaces;

import com.modern.tec.films.core.models.GenreRespond;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreApi {
    @GET("genre/movie/list")
    Call<GenreRespond> getGenres(@Query("api_key") String key);
}
