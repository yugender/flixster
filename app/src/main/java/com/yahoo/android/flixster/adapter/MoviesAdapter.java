package com.yahoo.android.flixster.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.android.flixster.MovieDetailsActivity;
import com.yahoo.android.flixster.MoviesActivity;
import com.yahoo.android.flixster.R;
import com.yahoo.android.flixster.VideoPlayActivity;
import com.yahoo.android.flixster.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by yboini on 11/7/16.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    // View lookup cache
    static class ViewHolder {
        @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
        @Nullable @BindView(R.id.tvOverview) TextView tvOverView;
        @Nullable @BindView(R.id.ivPoster) ImageView ivPoster;
        @Nullable @BindView(R.id.ivPosterPopular) ImageView ivPosterPopular;
        @Nullable @BindView(R.id.imbPlay) ImageButton imbPlay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie , movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        Movie movie = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = getInflatedLayoutForType(viewType, parent);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch(viewType)
        {
            case 0:
                viewHolder.tvTitle.setText(movie.getTitle());
                viewHolder.tvOverView.setText(movie.getOverview());
                String imageUrl = movie.getPosterUrl();
                int orientation = getContext().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageUrl = movie.getBackDropImageUrl();
                }
                Picasso.with(getContext()).load(imageUrl).fit().transform(new RoundedCornersTransformation(5, 0)).placeholder(R.drawable.poster_placeholder).into(viewHolder.ivPoster);

            case 1:
                imageUrl = movie.getBackDropImageUrl();
                if (imageUrl != null) {
                    if (viewHolder.ivPosterPopular != null) {
                        Picasso.with(getContext()).load(imageUrl).fit().transform(new RoundedCornersTransformation(5, 5)).placeholder(R.drawable.poster_placeholder).into(viewHolder.ivPosterPopular);
                    }
                    if (viewHolder.imbPlay != null) {
                        viewHolder.imbPlay.setVisibility(View.VISIBLE);
                    }
                }
        }
        convertView.setOnClickListener(view -> {
            if (movie.getRating() > 5) {
                launchVideoPlayActivity(movie);
            } else {
                launchDetailView(movie);
            }
        });
        return convertView;
    }

    private void launchVideoPlayActivity(Movie movie) {
        Intent i = new Intent(getContext(), VideoPlayActivity.class);
        i.putExtra("id", movie.getId());
        // brings up the second activity
        getContext().startActivity(i);
    }

    private void launchDetailView(Movie movie) {
        Intent i = new Intent(getContext(), MovieDetailsActivity.class);
        i.putExtra("movie", movie);
        // brings up the second activity
        getContext().startActivity(i);
    }


    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        if (movie.getBackDropImageUrl() != null) {
            return getItem(position).getMovieType().ordinal();
        } else {
            return Movie.MovieType.NORMAL.ordinal();
        }
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return Movie.MovieType.values().length;
    }

    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        if (type == Movie.MovieType.POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, parent, false);
        } else {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
    }
}
