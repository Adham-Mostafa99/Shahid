package com.modern.tec.films.data.repository;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.modern.tec.films.core.models.Genre;
import com.modern.tec.films.core.models.GenreRespond;
import com.modern.tec.films.data.network.RetrofitInstance;
import com.modern.tec.films.data.network.interfaces.GenreApi;
import com.modern.tec.films.data.repository.interfaces.IGenreRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreServices implements IGenreRepo {

    GenreApi genreApi;
    Context context;

    public GenreServices(Application application) {
        initRetrofit();
        this.context = application;
    }




    public void initRetrofit() {
        genreApi = RetrofitInstance.getInstance(BASE_URL).create(GenreApi.class);
    }

    @Override
    public LiveData<List<Genre>> getGenres() {
        MutableLiveData<List<Genre>> genre = new MutableLiveData<>();
        genreApi.getGenres(KEY).enqueue(new Callback<GenreRespond>() {
            @Override
            public void onResponse(Call<GenreRespond> call, Response<GenreRespond> response) {
                genre.setValue(response.body().getGenreRespond());
            }

            @Override
            public void onFailure(Call<GenreRespond> call, Throwable t) {

            }
        });
        return genre;
    }

    @Override
    public void storeGenreInInternalMemory(Map<Integer, String> genreMap) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(genreMap);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("GenreName", hashMapString).apply();
    }

    @Override
    public Map<Integer, String> getGenreFromInternalMemory() {
        Gson gson = new Gson();
        String storedHashMapString = PreferenceManager.getDefaultSharedPreferences(context).getString("GenreName", "");
        java.lang.reflect.Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        HashMap<Integer, String> namesHashMap = gson.fromJson(storedHashMapString, type);
        return namesHashMap;
    }

}
