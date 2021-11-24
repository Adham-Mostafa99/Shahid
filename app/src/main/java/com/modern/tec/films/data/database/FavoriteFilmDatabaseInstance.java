package com.modern.tec.films.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.modern.tec.films.core.converter.CategoryConverter;
import com.modern.tec.films.core.converter.GeneresConverter;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.data.database.interfaces.FavoriteFilmDao;

@Database(entities = {Film.class}, version = 1)
@TypeConverters({CategoryConverter.class, GeneresConverter.class})
public abstract class FavoriteFilmDatabaseInstance extends RoomDatabase {
    private static FavoriteFilmDatabaseInstance instance;

    public abstract FavoriteFilmDao filmDao();


    public static synchronized FavoriteFilmDatabaseInstance getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoriteFilmDatabaseInstance.class, "film_database")
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
        }
        return instance;
    }
}
