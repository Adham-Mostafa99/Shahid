package com.modern.tec.films.core.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActorsRespond {
    @SerializedName("id")
    int filmId;

    @SerializedName("cast")
    List<Actor> filmActors;

    public int getFilmId() {
        return filmId;
    }

    public List<Actor> getFilmActors() {
        return filmActors;
    }
}
