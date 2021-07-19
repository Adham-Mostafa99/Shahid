package com.modern.tec.films.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.modern.tec.films.models.Film;

import java.util.List;

@Dao
public interface FilmDao {

    @Insert
    void insert(Film film);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Film> films);

    @Update
    void update(Film film);

    @Delete
    void delete(Film film);

    @Query("DELETE FROM films_table")
    void deleteAllFilms();

    @Query("SELECT * FROM films_table ORDER BY filmTitle DESC")
    LiveData<List<Film>> getAllFilms();
}
