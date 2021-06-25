package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one movie and provides access to its components
 */
@Parcel
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;

    public Movie() {
    }

    /**
     * Constructs a new Movie object from the given JSONObject
     * @param jsonObject
     * @throws JSONException
     */
    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
    }

    /**
     * Creates a list containing Movie objects from the given JSONArray
     * @param movieJsonArray
     * @return a list containing Movie objects from the given JSONArray
     * @throws JSONException
     */
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    /**
     * @return path to backdrop
     */
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342//%s", backdropPath);
    }

    /**
     * @return path to poster
     */
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342//%s", posterPath);
    }

    /**
     * @return path to title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return path to overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return path to vote average
     */
    public Double getVoteAverage() {
        return voteAverage;
    }
}
