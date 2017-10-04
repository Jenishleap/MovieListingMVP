package com.example.leapfrog.movielistingmvp.data.local;

import com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity;
import com.example.leapfrog.movielistingmvp.data.movie.MovieListFragment;

import java.util.HashMap;
import java.util.Map;


public class CategoryEnum {


    public static Map<String, Integer> cat_id_helper = new HashMap<String, Integer>() {
        {
            put(MovieListActivity.TOP_RATED, 1);
            put(MovieListActivity.POPULAR, 2);
            put(MovieListActivity.NOW_PLAYING, 3);
            put(MovieListActivity.UPCOMING, 4);
        }
    };


}
