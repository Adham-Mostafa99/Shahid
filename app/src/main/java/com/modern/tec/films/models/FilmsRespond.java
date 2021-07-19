package com.modern.tec.films.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmsRespond {

    @SerializedName("results")
    private List<Film> result;

    public List<Film> getResult() {
        return result;
    }

}
