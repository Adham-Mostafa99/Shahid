package com.modern.tec.films.data.database.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.modern.tec.films.core.models.Film;

import java.util.List;

@Dao
public interface FavoriteFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Film film);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Film> films);

    @Update
    void update(Film film);

    @Delete
    void delete(Film film);

    @Query("DELETE FROM `Favorite Films`")
    void deleteAllFilms();

    @Query("SELECT * FROM `Favorite Films` ORDER BY filmTitle DESC")
    LiveData<List<Film>> getAllFilms();

    @Query("SELECT EXISTS(SELECT * FROM `Favorite Films` WHERE filmId= :filmId)")
    boolean isFilmExistInTable(int filmId);


}
