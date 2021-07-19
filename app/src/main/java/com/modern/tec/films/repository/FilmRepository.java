package com.modern.tec.films.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.data.FilmApi;
import com.modern.tec.films.data.FilmDao;
import com.modern.tec.films.data.FilmDatabase;
import com.modern.tec.films.data.RetrofitInstance;
import com.modern.tec.films.models.Film;
import com.modern.tec.films.models.FilmsRespond;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository {

    private static final String TAG = "FilmRepository";
    private final LiveData<List<Film>> allFilms;
    private final FilmDao filmDao;


    public FilmRepository(Application application) {
        FilmDatabase filmDatabase = FilmDatabase.getInstance(application);
        filmDao = filmDatabase.filmDao();
        allFilms = filmDao.getAllFilms();
    }

    public LiveData<List<Film>> getAllFilms() {
        return allFilms;
    }

    public void getFilmsFromServer() {
        FilmApi filmApi = RetrofitInstance.getInstance().create(FilmApi.class);

        filmApi.getAllFilms().enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                insertAllFilms(response.body().getResult());
            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {
                Log.v(TAG, t.getMessage());
            }
        });
    }


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
