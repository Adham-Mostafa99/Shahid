package com.modern.tec.films.core.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreRespond {
    @SerializedName("genres")
    private List<Genre> genreRespond;

    public List<Genre> getGenreRespond() {
        return genreRespond;
    }
}
