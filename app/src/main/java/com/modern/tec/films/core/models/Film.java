package com.modern.tec.films.core.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Film implements Serializable {

    @SerializedName("poster_path")
    private String filmPhoto;

    @SerializedName("title")
    private String filmTitle;

    @SerializedName("genre_ids")
    private int[] filmGenreIds;

    private List<String> filmsGenreNames;

    private boolean isFav;

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

    public Film(String filmPhoto, String filmTitle, int[] filmGenreIds, int filmId, String filmOverview, double filmVote, String filmReleaseDate, int filmVoteCount) {
        this.filmPhoto = filmPhoto;
        this.filmTitle = filmTitle;
        this.filmGenreIds = filmGenreIds;
        this.filmId = filmId;
        this.filmOverview = filmOverview;
        this.filmVote = filmVote;
        this.filmReleaseDate = filmReleaseDate;
        this.filmVoteCount = filmVoteCount;



    }

    public void setFilmsGenreNames(List<String> filmsGenreNames) {
        this.filmsGenreNames = filmsGenreNames;
    }

    public List<String> getFilmsGenreNames() {
        return filmsGenreNames;
    }

    public int[] getFilmGenreIds() {
        return filmGenreIds;
    }

    public void setFilmGenreIds(int[] filmGenreIds) {
        this.filmGenreIds = filmGenreIds;
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


    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmPhoto='" + filmPhoto + '\'' +
                ", filmTitle='" + filmTitle + '\'' +
                ", filmGenreIds=" + Arrays.toString(filmGenreIds) +
                ", filmsGenreNames=" + filmsGenreNames +
                ", isFav=" + isFav +
                ", filmId=" + filmId +
                ", filmOverview='" + filmOverview + '\'' +
                ", filmVote=" + filmVote +
                ", filmReleaseDate='" + filmReleaseDate + '\'' +
                ", filmVoteCount=" + filmVoteCount +
                '}';
    }
}
