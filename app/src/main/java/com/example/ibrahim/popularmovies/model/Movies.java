package com.example.ibrahim.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 *
 * Created by ibrahim on 29/04/18.
 */

public class Movies implements Parcelable{

    private static final String TAG = "Movies";

    public long getVotAverage() {
        return votAverage;
    }

    public long getPopularity() {
        return popularity;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public ArrayList<String> getGenre_ids() {
        return genre_ids;
    }

    public long getId() {
        return id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public static Creator<Movies> getCREATOR() {
        return CREATOR;
    }

    private boolean video;
    private long votAverage;
    private String tilte;
    private long popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private ArrayList<String> genre_ids;
    private long id;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;

    public Movies() {
    }

    private Movies(Parcel in) {
        video = in.readByte() != 0;
        votAverage = in.readLong();
        tilte = in.readString();
        popularity = in.readLong();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        genre_ids = in.createStringArrayList();
        id = in.readLong();
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
    }

    private static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            Log.v(TAG,"createbeParcelaple");

            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVotAverage(long votAverage) {
        this.votAverage = votAverage;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setGenre_ids(ArrayList<String> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    @Override
    public int describeContents() {
        Log.v(TAG,"describeParcelaple");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Log.v(TAG,"writeParcelaple");

        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeLong(votAverage);
        parcel.writeString(tilte);
        parcel.writeLong(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeStringList(genre_ids);
        parcel.writeLong(id);
        parcel.writeString(backdrop_path);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }
}
