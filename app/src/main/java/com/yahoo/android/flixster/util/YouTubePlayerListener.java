package com.yahoo.android.flixster.util;

import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.yahoo.android.flixster.VideoPlayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yboini on 11/12/16.
 */

public class YouTubePlayerListener implements YouTubePlayer.OnInitializedListener {
    public static String VIDEO_API_URL = "http://api.themoviedb.org/3/movie/%1$s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static String API_KEY = "AIzaSyDee4tehie0tYZHQBdPO5OsVs8g4P8ccAw";
    private boolean autoplayVideo;
    int movieId;
    public YouTubePlayerListener(boolean autoplayVideo, int movieId) {
        this.autoplayVideo = autoplayVideo;
        this.movieId = movieId;
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        String url = String.format(VIDEO_API_URL, movieId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String videoSource = getVideoSource(response);
                if (videoSource != null) {
                    if (autoplayVideo) {
                        youTubePlayer.loadVideo(videoSource);
                    } else {
                        youTubePlayer.cueVideo(videoSource);
                    }
                }
            }
        });
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {

    }

    public static String getVideoSource (Response response) {
        String videoSource = null;
        if (response.isSuccessful()) {
            try {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                JSONArray videosArray = jsonResponse.getJSONArray("youtube");
                JSONObject videoObject = null;
                for (int i = 0; i < videosArray.length(); i++) {
                    try {
                        videoObject = videosArray.getJSONObject(i);
                        if (videoObject.get("type").equals("Trailer") || videoObject.get("type").equals("Teaser")) {
                            break;
                        }
                    } catch (JSONException jsonExpection) {
                        videoObject = null;
                    }
                }
                if (videoObject != null) {
                    videoSource = videoObject.getString("source");
                }
            } catch (JSONException jsonException) {
                Log.d("debug", "invalid JSON Reponse from Trailers API ");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            Log.e("error", "Unexpected response code " + response);
        }
        return videoSource;
    }
}
