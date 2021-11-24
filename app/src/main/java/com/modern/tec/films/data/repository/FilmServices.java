package com.modern.tec.films.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.core.models.ActorsRespond;
import com.modern.tec.films.data.database.FavoriteFilmDatabaseInstance;
import com.modern.tec.films.data.database.interfaces.FavoriteFilmDao;
import com.modern.tec.films.data.network.interfaces.FilmApi;
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
    //    private final FilmDao filmDao;
    private FilmApi filmApi;
    private GenreServices genreServices;


    private FavoriteFilmDao filmDao;

    private final MutableLiveData<List<Film>> discoveredFilmsLiveData;
    private final MutableLiveData<List<Film>> PopularFilmsLiveData;
    private final MutableLiveData<List<Film>> searchedLiveData;
    private final MutableLiveData<List<Film>> suggestedLiveData;
    private final MutableLiveData<List<Film>> categoryLiveData;


    public FilmServices(Application application) {
//        FilmDatabase filmDatabase = FilmDatabase.getInstance(application);
//        filmDao = filmDatabase.filmDao();

        initRoom(application);
        initRetrofit();
        genreServices = new GenreServices(application);

        discoveredFilmsLiveData = new MutableLiveData<>();
        searchedLiveData = new MutableLiveData<>();
        PopularFilmsLiveData = new MutableLiveData<>();
        suggestedLiveData = new MutableLiveData<>();
        categoryLiveData = new MutableLiveData<>();

    }

    private void initRoom(Application application) {

        FavoriteFilmDatabaseInstance favoriteFilmDatabaseInstance = FavoriteFilmDatabaseInstance.getInstance(application);
        this.filmDao = favoriteFilmDatabaseInstance.filmDao();

    }

    public void initRetrofit() {
        filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
    }

    public LiveData<List<Film>> getPopularFilms(int pageNumber) {
        filmApi.getPopularFilms(KEY, pageNumber).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                if (response.isSuccessful()) {

                    List<Film> filmList = response.body().getResult();
                    Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                    for (Film film : filmList) {
                        List<String> genreNames = new ArrayList<>();
                        for (int id : film.getFilmGenreIds()) {
                            genreNames.add(genresMap.get(id));
                        }

                        film.setFilmsGenreNames(genreNames);
                    }
                    PopularFilmsLiveData.setValue(filmList);
                }

            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

                PopularFilmsLiveData.setValue(null);

            }
        });
        return PopularFilmsLiveData;
    }

    public MutableLiveData<List<Film>> getPopularFilmsLiveData() {
        return PopularFilmsLiveData;
    }

    @Override
    public LiveData<List<Film>> getComingFilms() {
        MutableLiveData<List<Film>> films = new MutableLiveData<>();

        filmApi.getComingFilms(KEY).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {

                List<Film> filmList = response.body().getResult();
                Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                for (Film film : filmList) {
                    List<String> genreNames = new ArrayList<>();
                    for (int id : film.getFilmGenreIds()) {
                        genreNames.add(genresMap.get(id));
                    }

                    film.setFilmsGenreNames(genreNames);
                }
                films.setValue(filmList);
            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {
                films.setValue(null);
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
                        if (response.isSuccessful()) {
                            Log.v("TAG", "url:" + response.raw().request().url());

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
                        discoveredFilmsLiveData.setValue(null);
                    }
                });
    }

    @Override
    public LiveData<List<Film>> getCategoryFilms(String categoryName, String sortType, String year, int pageNumber) {
        filmApi.getDiscoveredFilms(
                KEY,
                sortType,
                false,
                year,
                pageNumber)
                .enqueue(new Callback<FilmsRespond>() {
                    @Override
                    public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {

                        if (response.isSuccessful()) {
                            List<Film> filmList = response.body().getResult();
                            List<Film> categoryFilmList = new ArrayList<>();
                            Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                            for (Film film : filmList) {
                                List<String> genreNames = new ArrayList<>();
                                for (int id : film.getFilmGenreIds()) {
                                    genreNames.add(genresMap.get(id));
                                }

                                film.setFilmsGenreNames(genreNames);
                                if (film.getFilmsGenreNames().contains(categoryName)) {
                                    categoryFilmList.add(film);
                                }
                            }

                            categoryLiveData.setValue(categoryFilmList);
                        }

                    }

                    @Override
                    public void onFailure(Call<FilmsRespond> call, Throwable t) {
                        categoryLiveData.setValue(null);
                    }
                });
        return categoryLiveData;
    }

    public MutableLiveData<List<Film>> getDiscoveredFilmsLiveData() {
        return discoveredFilmsLiveData;
    }

    public MutableLiveData<List<Film>> getSearchedLiveData() {
        return searchedLiveData;
    }

    @Override
    public void searchFilms(String name, String categoryFilms, int page) {
        FilmApi filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
        filmApi.searchFilm(KEY, name, page).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                if (response.isSuccessful()) {
                    List<Film> filmList = response.body().getResult();
                    List<Film> categoryFilmList = new ArrayList<>();
                    Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                    for (Film film : filmList) {
                        List<String> genreNames = new ArrayList<>();
                        for (int id : film.getFilmGenreIds()) {
                            genreNames.add(genresMap.get(id));
                        }

                        film.setFilmsGenreNames(genreNames);

                        if (categoryFilms != null)
                            if (film.getFilmsGenreNames().contains(categoryFilms)) {
                                categoryFilmList.add(film);
                            }

                    }

                    if (categoryFilms != null)
                        searchedLiveData.setValue(categoryFilmList);
                    else
                        searchedLiveData.setValue(filmList);
                }

            }


            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {
                searchedLiveData.setValue(null);
            }
        });

    }

    @Override
    public LiveData<List<Actor>> getFilmActors(int filmId) {
        MutableLiveData<List<Actor>> actorsLiveData = new MutableLiveData<>();
        FilmApi filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
        filmApi.getFilmActors(filmId, KEY).enqueue(new Callback<ActorsRespond>() {
            @Override
            public void onResponse(Call<ActorsRespond> call, Response<ActorsRespond> response) {
                List<Actor> actorList = response.body().getFilmActors();
                actorsLiveData.setValue(actorList);
            }

            @Override
            public void onFailure(Call<ActorsRespond> call, Throwable t) {
//TODO handle no actors
            }
        });
        return actorsLiveData;
    }

    @Override
    public LiveData<List<Film>> getSuggestedFilms(String filmId) {

        FilmApi filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
        filmApi.getSuggestedFilms(filmId, KEY).enqueue(new Callback<FilmsRespond>() {
            @Override
            public void onResponse(Call<FilmsRespond> call, Response<FilmsRespond> response) {
                List<Film> filmList = response.body().getResult();
                Map<Integer, String> genresMap = genreServices.getGenreFromInternalMemory();

                for (Film film : filmList) {
                    List<String> genreNames = new ArrayList<>();
                    for (int id : film.getFilmGenreIds()) {
                        genreNames.add(genresMap.get(id));
                    }

                    film.setFilmsGenreNames(genreNames);
                }

                suggestedLiveData.setValue(filmList);
            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

            }
        });

        return suggestedLiveData;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    /*Room*/
    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public LiveData<Boolean> insertFilmToFavoriteTable(Film film) {
        MutableLiveData<Boolean> insertFilmToFavoriteTable = new MutableLiveData<>();
        new InsertFilm(filmDao, insertFilmToFavoriteTable).execute(film);
        return insertFilmToFavoriteTable;
    }

    @Override
    public LiveData<Boolean> insertListFilmsToFavoriteTable(List<Film> filmList) {

        MutableLiveData<Boolean> insertListFilmsToFavoriteTable = new MutableLiveData<>();
        new InsertListFilms(filmDao, insertListFilmsToFavoriteTable).execute(filmList);
        return insertListFilmsToFavoriteTable;
    }

    @Override
    public LiveData<Boolean> deleteFilmFromTable(Film film) {
        MutableLiveData<Boolean> deleteFilmFromTable = new MutableLiveData<>();
        new DeleteFilm(filmDao, deleteFilmFromTable).execute(film);
        return deleteFilmFromTable;
    }

    @Override
    public LiveData<Boolean> deleteAllFilms() {
        MutableLiveData<Boolean> deleteAllFilms = new MutableLiveData<>();
        new DeleteAllFilm(filmDao, deleteAllFilms).execute();
        return deleteAllFilms;
    }

    @Override
    public LiveData<List<Film>> getAllFilmsFromTable() {
        return filmDao.getAllFilms();
    }

    @Override
    public LiveData<Boolean> isFilmExistInTable(int filmId) {
        MutableLiveData<Boolean> isFilmExistInTable = new MutableLiveData<>();
        new CheckIsFilmExistInTable(filmDao, isFilmExistInTable).execute(filmId);
        return isFilmExistInTable;
    }

    private class InsertFilm extends AsyncTask<Film, Void, Boolean> {
        FavoriteFilmDao filmDao;
        MutableLiveData<Boolean> insertFilmToFavoriteTable;

        public InsertFilm(FavoriteFilmDao filmDao, MutableLiveData<Boolean> insertFilmToFavoriteTable) {
            this.filmDao = filmDao;
            this.insertFilmToFavoriteTable = insertFilmToFavoriteTable;
        }

        @Override
        protected Boolean doInBackground(Film... films) {
            filmDao.insert(films[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            insertFilmToFavoriteTable.setValue(aBoolean);
        }
    }


    private class InsertListFilms extends AsyncTask<List<Film>, Void, Boolean> {
        FavoriteFilmDao filmDao;
        MutableLiveData<Boolean> insertListFilmsToFavoriteTable;

        public InsertListFilms(FavoriteFilmDao filmDao, MutableLiveData<Boolean> insertListFilmsToFavoriteTable) {
            this.filmDao = filmDao;
            this.insertListFilmsToFavoriteTable = insertListFilmsToFavoriteTable;
        }


        @Override
        protected Boolean doInBackground(List<Film>... lists) {
            filmDao.insertAll(lists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            insertListFilmsToFavoriteTable.setValue(aBoolean);
        }
    }

    private class DeleteFilm extends AsyncTask<Film, Void, Boolean> {
        FavoriteFilmDao filmDao;
        MutableLiveData<Boolean> deleteFilmFromTable;

        public DeleteFilm(FavoriteFilmDao filmDao, MutableLiveData<Boolean> deleteFilmFromTable) {
            this.filmDao = filmDao;
            this.deleteFilmFromTable = deleteFilmFromTable;
        }

        @Override
        protected Boolean doInBackground(Film... films) {
            filmDao.delete(films[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            deleteFilmFromTable.setValue(aBoolean);
        }
    }

    private class DeleteAllFilm extends AsyncTask<Void, Void, Boolean> {
        FavoriteFilmDao filmDao;
        MutableLiveData<Boolean> deleteAllFilms;

        public DeleteAllFilm(FavoriteFilmDao filmDao, MutableLiveData<Boolean> deleteAllFilms) {
            this.filmDao = filmDao;
            this.deleteAllFilms = deleteAllFilms;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            filmDao.deleteAllFilms();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            deleteAllFilms.setValue(aBoolean);
        }
    }


    private class CheckIsFilmExistInTable extends AsyncTask<Integer, Void, Boolean> {
        FavoriteFilmDao filmDao;
        MutableLiveData<Boolean> isFilmExistInTable;

        public CheckIsFilmExistInTable(FavoriteFilmDao filmDao, MutableLiveData<Boolean> isFilmExistInTable) {
            this.filmDao = filmDao;
            this.isFilmExistInTable = isFilmExistInTable;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            return filmDao.isFilmExistInTable(integers[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isFilmExistInTable.setValue(aBoolean);
        }
    }

}
