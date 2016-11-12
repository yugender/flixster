package com.yahoo.android.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.yahoo.android.flixster.model.Movie;
import com.yahoo.android.flixster.util.YouTubePlayerListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tvDetailTitle) TextView tvDetailTitle;
    @BindView(R.id.tvDetailReleaseDate) TextView tvDetailReleaseDate;
    @BindView(R.id.rtbRating) RatingBar rtbRating;
    @BindView(R.id.tvDetailOverview) TextView tvDetailOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        tvDetailTitle.setText(movie.getTitle());
        tvDetailOverview.setText(movie.getOverview());
        tvDetailReleaseDate.setText(movie.getReleaseDate());
        rtbRating.setRating(movie.getRating());
        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.vdvDetail);
        youtubeFragment.initialize(YouTubePlayerListener.API_KEY, new YouTubePlayerListener(false, movie.getId()));
    }
}
