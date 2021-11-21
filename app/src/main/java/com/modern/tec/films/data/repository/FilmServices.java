package com.modern.tec.films.data.repository;

import android.app.Application;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.core.models.Actor;
import com.modern.tec.films.core.models.ActorsRespond;
import com.modern.tec.films.data.network.interfaces.FilmApi;
import com.modern.tec.films.data.database.interfaces.FilmDao;
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

    private MutableLiveData<List<Film>> discoveredFilmsLiveData;
    private MutableLiveData<List<Film>> PopularFilmsLiveData;
    private MutableLiveData<List<Film>> searchedLiveData;
    private MutableLiveData<List<Film>> suggestedLiveData;
    private MutableLiveData<List<Film>> categoryLiveData;


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

    }

    public void initRetrofit() {
        filmApi = RetrofitInstance.getInstance(BASE_URL).create(FilmApi.class);
    }

    public LiveData<List<Film>> getPopularFilms(int pageNumber) {
        filmApi.getPopularFilms(KEY, pageNumber).enqueue(new Callback<FilmsRespond>() {
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
                    PopularFilmsLiveData.setValue(filmList);
                }

            }

            @Override
            public void onFailure(Call<FilmsRespond> call, Throwable t) {

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

                        Log.v("TAG", "url:" + response.raw().request().url());


                        if (pageNumber <= response.body().getRespondTotalPageNumber()) {
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


    //***********************************************


//    public void insertAllFilms(List<Film> films) {
//        new InsertAllFilmsAsyncTask(filmDao).execute(films);
//    }
//
//    public void insert(Film film) {
//        new InsertFilmAsyncTask(filmDao).execute(film);
//    }
//
//    public void update(Film film) {
//        new UpdateFilmAsyncTask(filmDao).execute(film);
//    }
//
//    public void delete(Film film) {
//        new DeleteFilmAsyncTask(filmDao).execute(film);
//    }
//
//    public void deleteAll() {
//        new DeleteAllFilmsAsyncTask(filmDao).execute();
//    }
//
//
//    private static class InsertAllFilmsAsyncTask extends AsyncTask<List<Film>, Void, Void> {
//
//        FilmDao filmDao;
//
//        public InsertAllFilmsAsyncTask(FilmDao filmDao) {
//            this.filmDao = filmDao;
//        }
//
//        @SafeVarargs
//        @Override
//        protected final Void doInBackground(List<Film>... lists) {
//            filmDao.insertAll(lists[0]);
//            return null;
//        }
//    }
//
//    private static class InsertFilmAsyncTask extends AsyncTask<Film, Void, Void> {
//
//        FilmDao filmDao;
//
//        public InsertFilmAsyncTask(FilmDao filmDao) {
//            this.filmDao = filmDao;
//        }
//
//        @Override
//        protected Void doInBackground(Film... films) {
//            filmDao.insert(films[0]);
//            return null;
//        }
//    }
//
//    private static class UpdateFilmAsyncTask extends AsyncTask<Film, Void, Void> {
//
//        FilmDao filmDao;
//
//        public UpdateFilmAsyncTask(FilmDao filmDao) {
//            this.filmDao = filmDao;
//        }
//
//        @Override
//        protected Void doInBackground(Film... films) {
//            filmDao.update(films[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteFilmAsyncTask extends AsyncTask<Film, Void, Void> {
//
//        FilmDao filmDao;
//
//        public DeleteFilmAsyncTask(FilmDao filmDao) {
//            this.filmDao = filmDao;
//        }
//
//        @Override
//        protected Void doInBackground(Film... films) {
//            filmDao.delete(films[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteAllFilmsAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        FilmDao filmDao;
//
//        public DeleteAllFilmsAsyncTask(FilmDao filmDao) {
//            this.filmDao = filmDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            filmDao.deleteAllFilms();
//            return null;
//        }
//    }


}
