package com.modern.tec.films.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "films_table")
public class Film {

    @SerializedName("poster_path")
    private String filmPhoto;

    @SerializedName("title")
    private String filmTitle;

    @PrimaryKey
    @SerializedName("id")
    private int filmId;

    @SerializedName("overview")
    private String filmOverview;

    @SerializedName("vote_average")
    private double filmVote;

    @SerializedName("release_date")
    private String filmReleaseDate;

    @SerializedName("vote_count")
    private int filmVoteCount;

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public Film(String filmPhoto, String filmTitle, String filmOverview, double filmVote, String filmReleaseDate, int filmVoteCount) {
        this.filmPhoto = filmPhoto;
        this.filmTitle = filmTitle;
        this.filmOverview = filmOverview;
        this.filmVote = filmVote;
        this.filmReleaseDate = filmReleaseDate;
        this.filmVoteCount = filmVoteCount;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public int getFilmId() {
        return filmId;
    }

    public String getFilmOverview() {
        return filmOverview;
    }

    public double getFilmVote() {
        return filmVote;
    }

    public String getFilmReleaseDate() {
        return filmReleaseDate;
    }

    public int getFilmVoteCount() {
        return filmVoteCount;
    }

    public String getFilmPhoto() {
        return filmPhoto;
    }



}
