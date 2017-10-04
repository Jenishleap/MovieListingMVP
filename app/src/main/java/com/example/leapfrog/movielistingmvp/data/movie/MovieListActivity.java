package com.example.leapfrog.movielistingmvp.data.movie;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.leapfrog.movielistingmvp.R;


public class MovieListActivity extends AppCompatActivity {

    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";
    public static final String NOW_PLAYING = "now_playing";
    public static final String UPCOMING = "upcoming";

    Toolbar toolbar;
    FragmentAdapter fragmentAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        setTitle(R.string.movie_lists);
        initializeUi();
        setSupportActionBar(toolbar);
        viewPager.setAdapter(fragmentAdapter);
    }

    private void initializeUi() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    
}
