package com.yahoo.android.flixster;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.yahoo.android.flixster.adapter.MoviesAdapter;
import com.yahoo.android.flixster.databinding.ActivityMoviesBinding;
import com.yahoo.android.flixster.model.Movie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {
    MoviesAdapter moviesAdapter;
    ListView lvMovies;
    private ActivityMoviesBinding binding;

    List<Movie> movies;
    SwipeRefreshLayout swipeContainer;
    OkHttpClient client;
    private static String NOW_PLAYING_API_URL = "http://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        lvMovies = binding.lvMovies;
        movies = new ArrayList<>();
        client = new OkHttpClient();
        moviesAdapter = new MoviesAdapter(this, movies);
        swipeContainer = binding.swipeContainer;
        lvMovies.setAdapter(moviesAdapter);
        lvMovies.setItemsCanFocus(true);
        getMoviesData();
        swipeContainer.setOnRefreshListener(() -> fetchTimelineAsync(0));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void getMoviesData() {
        Request request = new Request.Builder().url(NOW_PLAYING_API_URL).build();
        client.newCall(request).enqueue(new NetworkRequestCallback(false));
    }

    public void fetchTimelineAsync(int page) {
        Request request = new Request.Builder().url(NOW_PLAYING_API_URL).build();
        client.newCall(request).enqueue(new NetworkRequestCallback(true));
    }

    private class NetworkRequestCallback implements Callback {
        private boolean isSwipeRequest;
        NetworkRequestCallback(boolean isSwipeRequest) {
            this.isSwipeRequest = isSwipeRequest;
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }
            try {
                JSONObject moviesResponse = new JSONObject(response.body().string());
                JSONArray moviesJSONArray = moviesResponse.getJSONArray("results");
                movies.addAll(Movie.getMovies(moviesJSONArray));
                runOnUiThread(() -> {
                    moviesAdapter.notifyDataSetChanged();
                    if (isSwipeRequest) {
                        swipeContainer.setRefreshing(false);
                    }
                });
            } catch (JSONException jsonException) {
                Log.d("debug", "invalid JSON Reponse from videos API");
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    }

}
