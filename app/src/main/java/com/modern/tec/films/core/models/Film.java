package com.modern.tec.films.core.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "films_table")
public class Film {

    @SerializedName("poster_path")
    private String filmPhoto;

    @SerializedName("title")
    private String filmTitle;

    @SerializedName("genre_ids")
    @Ignore
    private int[] filmGenreIds;

    private transient  List<String> filmsGenreNames;

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


    @Override
    public String toString() {
        return "Film{" +
                "filmPhoto='" + filmPhoto + '\'' +
                ", filmTitle='" + filmTitle + '\'' +
                ", filmId=" + filmId +
                ", filmOverview='" + filmOverview + '\'' +
                ", filmVote=" + filmVote +
                ", filmReleaseDate='" + filmReleaseDate + '\'' +
                ", filmVoteCount=" + filmVoteCount +
                '}';
    }
}
