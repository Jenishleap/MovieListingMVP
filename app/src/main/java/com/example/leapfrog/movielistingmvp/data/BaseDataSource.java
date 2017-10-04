package com.example.leapfrog.movielistingmvp.data;

import com.example.leapfrog.movielistingmvp.data.models.Movie;

import java.util.List;


public interface BaseDataSource {

    void storeMovie(List<Movie> movie,String page, DataSource.DoneStroringMoviesCallback callback);

    void checkTotalMovieCount();

    void storeCategories();

    void fetchMoviesIdsFromDb(int id, DataSource.DoneFetchingMovieIds callback);

    void fetchMoviesWithIds(List<Integer> movieIds, DataSource.DoneFetchingMovieswithIds callback);

}
