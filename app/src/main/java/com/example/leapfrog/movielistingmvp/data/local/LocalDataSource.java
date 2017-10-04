package com.example.leapfrog.movielistingmvp.data.local;

import android.util.Log;

import com.example.leapfrog.movielistingmvp.data.DataSource;
import com.example.leapfrog.movielistingmvp.data.models.Category;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.models.MovieCategory;
import com.example.leapfrog.movielistingmvp.data.movie.MovieListActivity;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.List;


public class LocalDataSource extends DataSource {


    private String TAG = "database";
    private AppDatabase appDatabase;


    public LocalDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor, AppDatabase appDatabase) {
        super(mainUiThread, threadExecutor);
        this.appDatabase = appDatabase;
    }


    private static LocalDataSource localDataSource;


    public static synchronized LocalDataSource getInstance(MainUiThread mainUiThread,
                                                           ThreadExecutor threadExecutor, AppDatabase appDatabase) {
        if (localDataSource == null) {

            localDataSource = new LocalDataSource(mainUiThread, threadExecutor, appDatabase);
        }
        return localDataSource;
    }


    @Override
    public void getMovies(String category, String API_KEY, GetMoviesCallback callback) {

    }

    @Override
    public void getCasts(String movie_id, String API_KEY, GetCasteCallback callback) {

    }

    @Override
    public void getTrailer(String movie_id, String API_KEY, GetTrailerCallback callback) {

    }

    @Override
    public void storeMovie(final List<Movie> movies, final String page, final DoneStroringMoviesCallback callback) {

        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for (Movie movie : movies) {
                    appDatabase.tablesDao().insertAll(movie);//this should run in background thread
                    Log.d("dbinsert", "adding movie:  " + movie.getTitle());

                    MovieCategory movieCategory = new MovieCategory();
                    movieCategory.setMovieId(movie.getId());
                    movieCategory.setCatId(CategoryEnum.cat_id_helper.get(page));

                    appDatabase.tablesDao().insertAll(movieCategory);
                    Log.d(TAG, "callback");
                }

                mainUiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.doneStoringMovies();

                    }
                });

            }
        });

    }

    @Override
    public void checkTotalMovieCount() {


        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> movieList = appDatabase.tablesDao().getAllMovies();
                Log.d(TAG, "movie table is done inserting, Rows Count: " + movieList.size());

                List<Category> categoryList = appDatabase.tablesDao().getAllCategory();
                Log.d(TAG, "total categories size: " + categoryList.size());

                List<MovieCategory> movieCategoryList = appDatabase.tablesDao().getAllMovieCats();
                Log.d(TAG, "total moviecategories size: " + movieCategoryList.size());
            }
        });

    }


    public void storeCategories() {

        final String[] cats = {MovieListActivity.TOP_RATED, MovieListActivity.POPULAR, MovieListActivity.NOW_PLAYING, MovieListActivity.UPCOMING};


        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < cats.length; i++) {
                    Category category = new Category();
                    category.setId(i + 1);
                    category.setCat_name(cats[i]);
                    appDatabase.tablesDao().insertAll(category);
                }
            }
        });
    }


    @Override
    public void fetchMoviesIdsFromDb(final int id, final DoneFetchingMovieIds callback) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Integer> movieIds = appDatabase.tablesDao().getMovieIdsFromCatId(id);


                mainUiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.doneFetchingMovieIds(movieIds);
                    }
                });

            }
        });
    }


    @Override
    public void fetchMoviesWithIds(final List<Integer> movieIds, final DoneFetchingMovieswithIds callback) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Movie> movies = appDatabase.tablesDao().findMoviesByIds(movieIds);


                mainUiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.doneFetchingMoviesWithIds(movies);
                    }
                });


            }
        });
    }
}
