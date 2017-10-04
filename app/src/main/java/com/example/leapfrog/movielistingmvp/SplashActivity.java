package com.example.leapfrog.movielistingmvp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity;


public class SplashActivity extends AppCompatActivity {

    public final int DELAY = 3500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MovieListActivity.class));
            }
        }, DELAY);
    }
}