package com.yahoo.android.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.yahoo.android.flixster.util.YouTubePlayerListener;

import static com.yahoo.android.flixster.util.YouTubePlayerListener.API_KEY;

public class VideoPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        int movieId = getIntent().getIntExtra("id", 0);

        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);
        youtubeFragment.initialize(API_KEY, new YouTubePlayerListener(true, movieId));
    }
}
