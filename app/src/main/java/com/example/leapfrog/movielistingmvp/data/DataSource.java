package com.example.leapfrog.movielistingmvp.data;

import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.models.TrailerResults;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public abstract class DataSource implements BaseDataSource {

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;

    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;
    }


    public interface GetMoviesCallback {
        void handleResponse(LinkedHashMap<String, Movie> movies, ArrayList<Integer> movieIds, String category);

        void onFailure(Throwable throwable);

        void onNetworkFailure();
    }


    public interface GetCasteCallback {
        void handleMovieCaste(List<Cast> movieCasts);

        void onFailure(Throwable throwable);

        void onNetworkFailure();
    }

    public interface GetTrailerCallback {

        void handleTrailerLink(List<TrailerResults> trailerResults);

        void onFailure(Throwable throwable);

        void onNetworkFailure();
    }


    public abstract void getMovies(String category, String API_KEY, GetMoviesCallback callback);

    public abstract void getCasts(String movie_id, String API_KEY, GetCasteCallback callback);

    public abstract void getTrailer(String movie_id, String API_KEY,GetTrailerCallback callback);


    public interface DoneStroringMoviesCallback {
        void doneStoringMovies();
    }

    public interface DoneFetchingMovieIds {
        void doneFetchingMovieIds(List<Integer> movieIds);

    }


    public interface DoneFetchingMovieswithIds {
        void doneFetchingMoviesWithIds(List<Movie> movies);
    }


    @Override
    public void storeMovie(List<Movie> movie, String page, DoneStroringMoviesCallback callback) {

    }


    @Override
    public void checkTotalMovieCount() {

    }


    @Override
    public void storeCategories() {

    }


    @Override
    public void fetchMoviesIdsFromDb(int id, DoneFetchingMovieIds callback) {

    }

    @Override
    public void fetchMoviesWithIds(List<Integer> movieIds, DoneFetchingMovieswithIds callback) {

    }
}
