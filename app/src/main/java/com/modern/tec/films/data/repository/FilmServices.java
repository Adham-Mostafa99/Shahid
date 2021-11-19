package com.modern.tec.films.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.data.network.interfaces.FilmApi;
import com.modern.tec.films.data.database.interfaces.FilmDao;
import com.modern.tec.films.data.database.FilmDatabase;
import com.modern.tec.films.data.network.RetrofitInstance;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.core.models.FilmsRespond;
import com.modern.tec.films.data.repository.interfaces.IFilmRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmServices implements IFilmRepo {

    private static final String TAG = "FilmRepository";
    private final FilmDao filmDao;
    private FilmApi filmApi;
    private GenreServices genreServices;

    private MutableLiveData<List<Film>> discoveredFilmsLiveData;
    private MutableLiveData<List<Film>> searchedLiveData;

    public FilmServices(Application application) {
        FilmDatabase filmDatabase = FilmDatabase.getInstance(application);
        filmDao = filmDatabase.filmDao();

        initRoom(application);
        initRetrofit();
        genreServices = new GenreServices(application);

        discoveredFilmsLiveData = new MutableLiveData<>();
        searchedLiveData = new MutableLiveData<>();
    }

    private void initRoom(Application application) {

    }

    public void initRetrofit() {
        filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
    }

    public LiveData<List<Film>> getPopularFilms() {
        MutableLiveData<List<Film>> films = new MutableLiveData<>();
        filmApi.getPopularFilms(KEY).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                films.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

            }
        });
        return films;
    }

    @Override
    public LiveData<List<Film>> getComingFilms() {
        MutableLiveData<List<Film>> films = new MutableLiveData<>();

        filmApi.getComingFilms(KEY).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                films.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

            }
        });
        return films;
    }


    @Override
    public void getDiscoveredFilms(String sortType, String year, int pageNumber) {
        MutableLiveData<List<Film>> films = new MutableLiveData<>();

        filmApi.getDiscoveredFilms(
                KEY,
                sortType,
                false,
                year,
                pageNumber)
                .enqueue(new Callback<FilmsRespond>() {
                    @Override
                    public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                        if (pageNumber <= response.body().getRespondTotalPageNumber()) {
                            List<Film> filmList = response.body().getResult();
                            Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                            for (Film film : filmList) {
                                List<String> genreNames = new ArrayList<>();
                                for (int id : film.getFilmGenreIds()) {
                                    genreNames.add(genresMap.get(id));
                                }

                                film.setFilmsGenreNames(genreNames);
                            }

                            discoveredFilmsLiveData.setValue(filmList);
                        }

                    }

                    @Override
                    public void onFailure(Call<FilmsRespond> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<List<Film>> getDiscoveredFilmsLiveData() {
        return discoveredFilmsLiveData;
    }

    public MutableLiveData<List<Film>> getSearchedLiveData() {
        return searchedLiveData;
    }

    @Override
    public void searchFilms(String name, int page) {


        FilmApi filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
        filmApi.searchFilm(KEY, name, page).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                if (page <= response.body().getRespondTotalPageNumber()) {
                    List<Film> filmList = response.body().getResult();
                    Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                    for (Film film : filmList) {
                        List<String> genreNames = new ArrayList<>();
                        for (int id : film.getFilmGenreIds()) {
                            genreNames.add(genresMap.get(id));
                        }

                        film.setFilmsGenreNames(genreNames);
                    }

                    searchedLiveData.setValue(filmList);
                }

            }


            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

            }
        });

    }


    //***********************************************


    public void insertAllFilms(List<Film> films) {
        new InsertAllFilmsAsyncTask(filmDao).execute(films);
    }

    public void insert(Film film) {
        new InsertFilmAsyncTask(filmDao).execute(film);
    }

    public void update(Film film) {
        new UpdateFilmAsyncTask(filmDao).execute(film);
    }

    public void delete(Film film) {
        new DeleteFilmAsyncTask(filmDao).execute(film);
    }

    public void deleteAll() {
        new DeleteAllFilmsAsyncTask(filmDao).execute();
    }


    private static class InsertAllFilmsAsyncTask extends AsyncTask<List<Film>, Void, Void> {

        FilmDao filmDao;

        public InsertAllFilmsAsyncTask(FilmDao filmDao) {
            this.filmDao = filmDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Film>... lists) {
            filmDao.insertAll(lists[0]);
            return null;
        }
    }

    private static class InsertFilmAsyncTask extends AsyncTask<Film, Void, Void> {

        FilmDao filmDao;

        public InsertFilmAsyncTask(FilmDao filmDao) {
            this.filmDao = filmDao;
        }

        @Override
        protected Void doInBackground(Film... films) {
            filmDao.insert(films[0]);
            return null;
        }
    }

    private static class UpdateFilmAsyncTask extends AsyncTask<Film, Void, Void> {

        FilmDao filmDao;

        public UpdateFilmAsyncTask(FilmDao filmDao) {
            this.filmDao = filmDao;
        }

        @Override
        protected Void doInBackground(Film... films) {
            filmDao.update(films[0]);
            return null;
        }
    }

    private static class DeleteFilmAsyncTask extends AsyncTask<Film, Void, Void> {

        FilmDao filmDao;

        public DeleteFilmAsyncTask(FilmDao filmDao) {
            this.filmDao = filmDao;
        }

        @Override
        protected Void doInBackground(Film... films) {
            filmDao.delete(films[0]);
            return null;
        }
    }

    private static class DeleteAllFilmsAsyncTask extends AsyncTask<Void, Void, Void> {

        FilmDao filmDao;

        public DeleteAllFilmsAsyncTask(FilmDao filmDao) {
            this.filmDao = filmDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            filmDao.deleteAllFilms();
            return null;
        }
    }


}
