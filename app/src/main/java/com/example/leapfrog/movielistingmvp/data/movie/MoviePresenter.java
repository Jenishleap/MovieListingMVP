package com.example.leapfrog.movielistingmvp.data.movie;


import android.util.Log;

import com.example.leapfrog.movielistingmvp.data.DataSource;
import com.example.leapfrog.movielistingmvp.data.local.CategoryEnum;
import com.example.leapfrog.movielistingmvp.data.local.LocalDataSource;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.remote.RemoteDataSource;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;
import com.example.leapfrog.movielistingmvp.helper.CompareLists;
import com.example.leapfrog.movielistingmvp.mvp.BasePresenter;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;





/*extending BasePresenter<view> is necessary in order to assign view to this presenter class*/

public class MoviePresenter extends BasePresenter<MovieContract.View> implements MovieContract.Presenter {

    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;

    private RemoteDataSource remoteDataSource;//model object
    private LocalDataSource localDataSource;//model object


    private ArrayList<Integer> newMovieIds;
    ArrayList<Integer> oldMovieIds = new ArrayList<>();

    private LinkedHashMap<String, Movie> movieWithId;
    ArrayList<Movie> oldMovies;


    public MoviePresenter(MovieContract.View view, RemoteDataSource remoteDataSource, LocalDataSource localDataSource,
                          ThreadExecutor threadExecutor, MainUiThread mainUiThread) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
    }


    @Override
    public void getMovies(final String category) {

        if (!CompareLists.pbShowed) {
            view.showProgressBar();
            CompareLists.pbShowed = true;
        }


        if (view == null) {
            return;
        }


        if (oldMovieIds != null && !oldMovieIds.isEmpty()) {
            view.showMovies(oldMovies, oldMovieIds);
            view.hideProgressBar();
        }


        remoteDataSource.getMovies(category, RetrofitClient.API_KEY, new DataSource.GetMoviesCallback() {
            @Override
            public void handleResponse(LinkedHashMap<String, Movie> movies, ArrayList<Integer> movieIds, String category) {
                if (view != null) {

                    newMovieIds = movieIds;
                    movieWithId = movies;


                    if (oldMovieIds.isEmpty()) {
                        //no retrived movies found, show latest ones
                        view.showMovies(new ArrayList<>(movies.values()), movieIds);
                        view.shouldShowPlaceholderText();
                        view.hideProgressBar();
                    } else {
                        filterLists();
                    }

                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.showToastMessage("No Internet Connection");


                    String curr_frag = MovieListFragment.currentPage;//delete this later

                    localDataSource.fetchMoviesIdsFromDb(CategoryEnum.cat_id_helper.get(category), new DataSource.DoneFetchingMovieIds() {
                        @Override
                        public void doneFetchingMovieIds(final List<Integer> movieIds) {

                            Log.d("movieidfetching", "the list of fetched movie ids:  " + movieIds);


                            localDataSource.fetchMoviesWithIds(movieIds, new DataSource.DoneFetchingMovieswithIds() {
                                @Override
                                public void doneFetchingMoviesWithIds(List<Movie> movies) {

                                    Collections.sort(movieIds);

                                    Log.d("moviefetching", "the list of fetched movies: " + movies);

                                    ArrayList<Movie> movies1 = new ArrayList<>(movies);
                                    ArrayList<Integer> movieIds1 = new ArrayList<>(movieIds);


                                    view.showMovies(movies1, movieIds1);
                                    view.shouldShowPlaceholderText();
                                    view.hideProgressBar();


                                }
                            });


                        }
                    });//pass category to fetch movie list

                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.showToastMessage("Network Failure");
                    view.shouldShowPlaceholderText();
                    view.hideProgressBar();
                }
            }
        });

    }


    public void storeCategories() {
        localDataSource.storeCategories();
    }


    public void passOldMovieIds(ArrayList<Integer> ids, ArrayList<Movie> movies) {


        this.oldMovieIds = ids;
        this.oldMovies = movies;


    }


    public void filterLists() {
        try {


            CompareLists compareLists = new CompareLists();
            if (!compareLists.equalLists(newMovieIds, oldMovieIds)) {
                //lists vary
                //further operation for identifying new ids
                Log.d("different", "lists are different");

                System.out.println("old list: " + oldMovieIds);
                System.out.println("old list: " + newMovieIds);

                //removing stale elements from the old list
                boolean found;

                Iterator<Integer> iterId = oldMovieIds.iterator();
                while (iterId.hasNext()) {
                    found = false;

                    Integer oldId = iterId.next();

                    for (Integer newId : newMovieIds) {

                        if (oldId.equals(newId)) {
                            //means this old id matches with new id
                            //no need to further look on this oldId
                            //break the loop
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        view.deleteMovie(oldMovieIds.indexOf(oldId));//
                        iterId.remove();
                        Log.d("movie deleted", "movie deleted");
                    }
                }

                System.out.println("the filtered out list: " + oldMovieIds);

                //now adding new elements from the new list
                List<Integer> newMovieIdsCopy = new ArrayList<>(newMovieIds);
                newMovieIdsCopy.removeAll(oldMovieIds);//remove same items
                System.out.println("the new ids are: " + newMovieIdsCopy);
                for (Integer newId : newMovieIdsCopy) {
                    oldMovieIds.add(newId);
                    view.addMovie(newId, movieWithId.get(newId));
                }
                System.out.println("the updated list: " + oldMovieIds);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void storeMoviesToDb(List<Movie> movies, String page) {

        localDataSource.storeMovie(movies, page, new DataSource.DoneStroringMoviesCallback() {
            @Override
            public void doneStoringMovies() {
                localDataSource.checkTotalMovieCount();
            }
        });


    }


}
