package com.modern.tec.films.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.modern.tec.films.models.Film;

@Database(entities = {Film.class}, version = 1)
public abstract class FilmDatabase extends RoomDatabase {
    private static FilmDatabase instance;

    public abstract FilmDao filmDao();


    public static synchronized FilmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FilmDatabase.class, "film_database")
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
        }
        return instance;
    }
}
