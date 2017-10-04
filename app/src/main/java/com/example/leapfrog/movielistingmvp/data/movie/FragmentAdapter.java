package com.example.leapfrog.movielistingmvp.data.movie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


import com.example.leapfrog.movielistingmvp.data.movie.MovieListFragment;

import java.util.List;

import static com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity.NOW_PLAYING;
import static com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity.POPULAR;
import static com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity.TOP_RATED;
import static com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity.UPCOMING;


public class FragmentAdapter extends FragmentStatePagerAdapter {

    private static int NUM_OF_FRAGMENTS = 4;
    List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);

    }


    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        Log.d("frag_position", "fragment_position is: " + position);
        switch (position) {
            case 0:
                fragment = MovieListFragment.newInstance(POPULAR);
                break;
            case 1:
                fragment = MovieListFragment.newInstance(TOP_RATED);
                break;
            case 2:
                fragment = MovieListFragment.newInstance(UPCOMING);
                break;
            case 3:
                fragment = MovieListFragment.newInstance(NOW_PLAYING);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENTS;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String page = null;

        switch (position) {
            case 0:
                page = "POPULAR";
                break;
            case 1:
                page = "TOPRATED";
                break;
            case 2:
                page = "UPCOMING";
                break;
            case 3:
                page = "NOWPLAYING";
                break;
        }
        return page;
    }
}
