package com.modern.tec.films.data.network.interfaces;

import com.modern.tec.films.core.models.ActorsRespond;
import com.modern.tec.films.core.models.FilmsRespond;
import com.modern.tec.films.core.models.GenreRespond;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmApi {
    @GET("trending/{media_type}/{time_window}")
    Call<FilmsRespond> getAllFilms(
            @Path("media_type") String mediaType,
            @Path("time_window") String time,
            @Query("api_key") String key);

    @GET("search/movie")
    Call<FilmsRespond> searchFilm(
            @Query("api_key") String key,
            @Query("query") String filmName,
            @Query("page") int pageNumber);

    @GET("movie/popular")
    Call<FilmsRespond> getPopularFilms(@Query("api_key") String key,
                                       @Query("page") int pageNumber);

    @GET("movie/upcoming")
    Call<FilmsRespond> getComingFilms(@Query("api_key") String key);

    @GET("discover/movie")
    Call<FilmsRespond> getDiscoveredFilms(
            @Query("api_key") String key,
            @Query("sort_by") String sortType,
            @Query("include_adult") boolean isContainAdult,
            @Query("year") String year,
            @Query("page") int pageNumber);


    @GET("movie/{movie_id}/credits")
    Call<ActorsRespond> getFilmActors(@Path("movie_id") int movieId,
                                      @Query("api_key") String key);

    @GET("movie/{movie_id}/recommendations")
    Call<FilmsRespond> getSuggestedFilms(@Path("movie_id") String filmId,
                                         @Query("api_key") String key);

}

