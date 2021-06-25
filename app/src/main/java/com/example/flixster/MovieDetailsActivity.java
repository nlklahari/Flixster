package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;


public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivLandPoster;
    ImageButton playButton;

    // trailer components
    public static String url;
    public static final String TAG = "MovieDetailsActivity";

    public MovieDetailsActivity() {
        this.movie = new Movie();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        //ivPosterDetails = (ImageView) findViewById(R.id.ivPosterDetails);
        ivLandPoster = (ImageView) findViewById(R.id.ivLandPoster);
        playButton = (ImageButton) findViewById(R.id.playButton);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
//        Glide.with(this)
//                .load(movie.getPosterPath()).transform(new CenterInside(), new RoundedCorners(24))
//                .placeholder(R.drawable.flicks_movie_placeholder)
//                .into(ivPosterDetails);

        Glide.with(this)
                .load(movie.getBackdropPath()).transform(new CenterInside(), new RoundedCorners(24))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .into(ivLandPoster);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        // make url here for video
        url = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=df25f8d855260071132492ab4a64894c&language=en-US";
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
    }

    /**
     * Fetches data
     */
    public void fetchData() {
        AsyncHttpClient client =  new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                Log.d(TAG, json.toString());
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    String videoId = results.getJSONObject(0).getString("key");

                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("videoId", videoId);
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}