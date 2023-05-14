package com.lor.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Quote extends Item {
    private String dialog;
    @SerializedName("movie")
    private String movieId;
    @SerializedName("character")
    private String characterId;

    public String getDialog() {
        return dialog;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getCharacterId() {
        return characterId;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "dialog='" + dialog + '\'' +
                ", movieId='" + movieId + '\'' +
                ", characterId='" + characterId + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Quote quote = (Quote) o;
        return Objects.equals(dialog, quote.dialog) && Objects.equals(movieId, quote.movieId) && Objects.equals(characterId, quote.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dialog, movieId, characterId);
    }
}
