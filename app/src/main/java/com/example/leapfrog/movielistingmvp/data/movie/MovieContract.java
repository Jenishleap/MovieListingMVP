package com.example.leapfrog.movielistingmvp.data.movie;


import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.mvp.IBasePresenter;
import com.example.leapfrog.movielistingmvp.mvp.IBaseView;

import java.util.ArrayList;
import java.util.List;

interface MovieContract {


    interface View extends IBaseView {//these are methods that view fragment must override

        void showMovies(ArrayList<Movie> movies, ArrayList<Integer> movieIds);

        void shouldShowPlaceholderText();

        void deleteMovie(int position);

        void addMovie(Integer id, Movie movie);

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends IBasePresenter<View> {

        void getMovies(String category);

        void passOldMovieIds(ArrayList<Integer> ids, ArrayList<Movie> movies);

        void storeCategories();

        void storeMoviesToDb(List<Movie> movies, String page);


    }


}
