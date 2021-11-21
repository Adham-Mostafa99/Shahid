package com.modern.tec.films.core.models;

import com.google.gson.annotations.SerializedName;

public class Actor {
    @SerializedName("id")
    int actorId;
    @SerializedName("character")
    String actorCharacter;
    @SerializedName("name")
    String actorName;
    @SerializedName("profile_path")
    String actorPhoto;
    @SerializedName("gender")
    String actorGender;

    public int getActorId() {
        return actorId;
    }

    public String getActorCharacter() {
        return actorCharacter;
    }

    public String getActorName() {
        return actorName;
    }

    public String getActorPhoto() {
        return actorPhoto;
    }

    public String getActorGender() {
        return actorGender;
    }
}
