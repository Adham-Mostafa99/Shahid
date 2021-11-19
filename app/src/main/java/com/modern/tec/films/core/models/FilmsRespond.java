package com.modern.tec.films.core.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmsRespond {

    @SerializedName("results")
    private List<Film> result;
    @SerializedName("page")
    private int respondPageNumber;
    @SerializedName("total_pages")
    private int respondTotalPageNumber;

    public List<Film> getResult() {
        return result;
    }

    public int getRespondPageNumber() {
        return respondPageNumber;
    }

    public int getRespondTotalPageNumber() {
        return respondTotalPageNumber;
    }

    @Override
    public String toString() {
        return "FilmsRespond{" +
                "result=" + result +
                ", respondPageNumber=" + respondPageNumber +
                ", respondTotalPageNumber=" + respondTotalPageNumber +
                '}';
    }
}
