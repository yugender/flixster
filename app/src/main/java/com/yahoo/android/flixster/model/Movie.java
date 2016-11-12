package com.yahoo.android.flixster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yboini on 11/7/16.
 */

public class Movie implements Serializable {
    private String title;
    private String overview;
    private String posterUrl;
    private String backDropImageUrl;
    private String releaseDate;
    private double rating;
    private int id;

    public enum MovieType {
        NORMAL, POPULAR
    }

    public Movie() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackDropImageUrl() {
        return backDropImageUrl;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setBackDropImageUrl(String backDropImageUrl) {
        this.backDropImageUrl = backDropImageUrl;
    }

    public int getRating() {
        return (int) Math.round(this.rating);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MovieType getMovieType() {
        if (this.rating > 5) {
            return MovieType.POPULAR;
        } else {
            return MovieType.NORMAL;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<Movie> getMovies(JSONArray moviesJSONArray) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            try {
                JSONObject movieJSON = (JSONObject) moviesJSONArray.get(i);
                Movie movie = new Movie();
                movie.setId(movieJSON.getInt("id"));
                movie.setTitle(movieJSON.getString("title"));
                movie.setOverview(movieJSON.getString("overview"));
                movie.setPosterUrl("https://image.tmdb.org/t/p/w500" + movieJSON.getString("poster_path"));
                movie.setBackDropImageUrl("https://image.tmdb.org/t/p/w780" + movieJSON.getString("backdrop_path"));
                movie.setReleaseDate(movieJSON.getString("release_date"));
                movie.setRating(movieJSON.getDouble("vote_average"));
                movies.add(movie);
            } catch (JSONException e) {

            }
        }
        return movies;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
