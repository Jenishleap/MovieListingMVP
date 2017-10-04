package com.example.leapfrog.movielistingmvp.data.moviedetail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.leapfrog.movielistingmvp.R;
import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.movie.MovieListFragment;
import com.example.leapfrog.movielistingmvp.data.remote.RemoteDataSource;
import com.example.leapfrog.movielistingmvp.data.remote.RestApiMethods;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;
import com.example.leapfrog.movielistingmvp.helper.ItemClickSupport;
import com.example.leapfrog.movielistingmvp.helper.NetworkHelper;
import com.example.leapfrog.movielistingmvp.helper.RatingCalculator;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {


    public static final int NUM_OF_STARS = 5;

    String movieId, movieTitle, movieRating, movieReleaseDate, movieOverview, moviePosterPath, movieBackgroundPath;
    String tag = "movie_details";


    ImageView ivMovieBackground, ivMoviePoster;
    TextView tvMovieDescription, tvReleaseDate, tvRating;
    private LinearLayoutManager llManagerHorizontal, llManagerHorizontal1;
    RecyclerView rvMovieCasts, rvYoutubeLinks;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    RatingBar rbMovieRatings;


    MovieCasteAdapter casteAdapter;
    YoutubeThumbnailAdapter youtubeThumbnailAdapter;

    private Bitmap bitmap = null;


    private MovieDetailPresenter movieDetailPresenter;
    private RemoteDataSource remoteDataSource;
    private ProgressBar progressBar, progressBarTrailer;
    private List<String> videoKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity_collapse);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        videoKeys = new ArrayList<>();
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();
        remoteDataSource = RemoteDataSource.getInstance(mainUiThread, threadExecutor, RetrofitClient.getInstance().create(RestApiMethods.class));
        movieDetailPresenter = new MovieDetailPresenter(this, remoteDataSource, threadExecutor, mainUiThread);


        intializeUi();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.movieId = extras.getString(MovieListFragment.MOVIE_ID);
            this.movieTitle = extras.getString(MovieListFragment.MOVIE_TITLE);
            this.movieRating = extras.getString(MovieListFragment.MOVIE_RATING);
            this.movieReleaseDate = extras.getString(MovieListFragment.MOVIE_RELEASE_DATE);
            this.movieOverview = extras.getString(MovieListFragment.MOVIE_OVERVIEW);
            this.moviePosterPath = extras.getString(MovieListFragment.MOVIE_POSTER_PATH);
            this.movieBackgroundPath = extras.getString(MovieListFragment.MOVIE_BACKGROUND_PATH);

        }
        Log.d(tag, "the movie detail: " + movieId);
        collapsingToolbarLayout.setTitle(movieTitle);

        updateUi();

        ItemClickSupport.addTo(rvYoutubeLinks).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        openYoutube(position);
                    }
                });


        fetchMovieCaste(movieId);
        fetchTrailerLink(movieId);
        toolbarTextAppernce();
    }

    private void intializeUi() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        ivMovieBackground = findViewById(R.id.ivMovieBackground);
        ivMoviePoster = findViewById(R.id.ivMoviePoster);
        rbMovieRatings = findViewById(R.id.rbMovieRatings);
        progressBar = findViewById(R.id.progressBar);
        progressBarTrailer = findViewById(R.id.progressBarTrailer);

        tvMovieDescription = findViewById(R.id.tvMovieDescription);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvRating = findViewById(R.id.tvRating);
        rvMovieCasts = findViewById(R.id.rvmovie_casts);
        rvYoutubeLinks = findViewById(R.id.rvYoutubeLinks);
        llManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        llManagerHorizontal1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMovieCasts.setLayoutManager(llManagerHorizontal);
        rvYoutubeLinks.setLayoutManager(llManagerHorizontal1);
        casteAdapter = new MovieCasteAdapter(getApplicationContext());
        youtubeThumbnailAdapter = new YoutubeThumbnailAdapter(getApplicationContext());
        rvMovieCasts.setAdapter(casteAdapter);
        rvYoutubeLinks.setAdapter(youtubeThumbnailAdapter);

    }


    public void fetchMovieCaste(String movieId) {

        movieDetailPresenter.getCastes(movieId);
    }

    public void fetchTrailerLink(String movieId) {
        movieDetailPresenter.getTrailerUrl(movieId);
    }

    private void updateUi() {

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/LeagueGothic-Regular.otf");
        tvReleaseDate.setTypeface(tf);

        tvMovieDescription.setText(movieOverview);
        tvReleaseDate.setText("Release Date: " + movieReleaseDate);
        tvRating.setText(movieRating);


        //set backgroundpath for movie
        Glide.with(getApplicationContext())
                .load(RetrofitClient.IMG_URL + movieBackgroundPath) // Image URL
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.bckgnd_placeholder)
                .error(R.drawable.bckgnd_placeholder)
                .into(new ImageViewTarget<GlideDrawable>(ivMovieBackground) {


                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        setImage(resource);
                    }

                    @Override
                    protected void setResource(GlideDrawable resource) {

                    }

                    private void setImage(GlideDrawable resource) {
                        ivMovieBackground.setImageDrawable(resource.getCurrent());
                    }


                }); // ImageView to display image

        rbMovieRatings.setNumStars(NUM_OF_STARS);//total num of stars is 5
//        rbMovieRatings.setStepSize(0.1f);
        rbMovieRatings.setRating(RatingCalculator.calculateRatingNum(movieRating));


        //set posterpath for the movie
        //grab color for stars with respect to posterpath
        Glide.with(this)
                .load(RetrofitClient.IMG_URL + moviePosterPath)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap = resource;
                        setStarColors(bitmap);

                        ivMoviePoster.setImageDrawable(null);//in case default place holder is not working

                        Drawable d = new BitmapDrawable(getResources(), bitmap);

                        ivMoviePoster.setBackgroundDrawable(d);

                    }
                });

    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/HOMINIS.ttf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/HOMINIS.ttf"));

        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.yellowstale)));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }


    private void setStarColors(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {

                int defaultValue = 0x000000;
                int vibrant = palette.getVibrantColor(defaultValue);
                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                int muted = palette.getMutedColor(defaultValue);
                int mutedLight = palette.getLightMutedColor(defaultValue);
                int mutedDark = palette.getDarkMutedColor(defaultValue);


                if (vibrantDark != 0) {
                    tvRating.setTextColor(vibrantDark);
                    if (rbMovieRatings.getProgressDrawable() instanceof LayerDrawable) {
                        LayerDrawable stars = (LayerDrawable) rbMovieRatings.getProgressDrawable();
                        DrawableCompat.setTint(stars.getDrawable(2), vibrantDark);
                    } else {
                        DrawableCompat.setTint(rbMovieRatings.getProgressDrawable(), vibrantDark);
                    }
                } else {
                    tvRating.setTextColor(muted);
                    if (rbMovieRatings.getProgressDrawable() instanceof LayerDrawable) {
                        LayerDrawable stars = (LayerDrawable) rbMovieRatings.getProgressDrawable();
                        DrawableCompat.setTint(stars.getDrawable(2), muted);
                    } else {
                        DrawableCompat.setTint(rbMovieRatings.getProgressDrawable(), muted);
                    }
                }


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showCastes(List<Cast> casts) {
        casteAdapter.update(casts);
    }

    @Override
    public void shouldShowPlaceholderText() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void setProgressBar(boolean show) {

        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


    @Override
    public void setTrailerProgressBar(boolean show) {
        progressBarTrailer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return null;
    }


    @Override
    public void setTrailerUrl(List<String> urls, List<String> videoKeys) {
        this.videoKeys = videoKeys;
        youtubeThumbnailAdapter.update(urls);
    }


    public void openYoutube(int position) {


        if (NetworkHelper.getInstance().isNetworkAvailable(getApplicationContext())) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKeys.get(position)));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoKeys.get(position)));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }

        } else {
            Toast.makeText(this, "No Internet available!!!", Toast.LENGTH_SHORT).show();
        }


    }
}
