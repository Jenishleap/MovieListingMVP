package com.example.leapfrog.movielistingmvp.data.remote;

import android.os.Handler;
import android.util.Log;


import com.example.leapfrog.movielistingmvp.data.DataSource;
import com.example.leapfrog.movielistingmvp.data.models.AllMovies;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.models.MovieCasts;
import com.example.leapfrog.movielistingmvp.data.models.Trailer;
import com.example.leapfrog.movielistingmvp.data.models.TrailerResults;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient.API_KEY;


public class RemoteDataSource extends DataSource {


    private static RemoteDataSource remoteDataSource;

    private RestApiMethods client;

    public RemoteDataSource(MainUiThread mainUiThread,
                            ThreadExecutor threadExecutor, RestApiMethods apiService) {
        super(mainUiThread, threadExecutor);
        this.client = apiService;

    }

    public static synchronized RemoteDataSource getInstance(MainUiThread mainUiThread,
                                                            ThreadExecutor threadExecutor,
                                                            RestApiMethods apiService) {
        if (remoteDataSource == null) {

            remoteDataSource = new RemoteDataSource(mainUiThread, threadExecutor, apiService);
        }
        return remoteDataSource;
    }

    @Override
    public void getMovies(final String category, String apiKey, final GetMoviesCallback callback) {


        if (API_KEY.isEmpty()) {

            return;
        }


        Call<AllMovies> call = client.getAllMovies(category, API_KEY);

        call.enqueue(new Callback<AllMovies>() {
            @Override
            public void onResponse(Call<AllMovies> call, Response<AllMovies> response) {

                if (response != null) {

                    LinkedHashMap<String, Movie> my_movies = new LinkedHashMap<>();
                    ArrayList<Integer> movie_ids = new ArrayList<>();
                    for (Movie movie : response.body().getResults()) {
                        movie_ids.add(movie.getId());
                        my_movies.put(String.valueOf(movie.getId()), movie);
                    }
                    Log.d("movie_ids", "the list of movie_ids: " + movie_ids);

                    callback.handleResponse(my_movies, movie_ids, category);
                } else
                    callback.onFailure(new Throwable());
            }

            @Override
            public void onFailure(Call<AllMovies> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                callback.onFailure(t);

            }
        });


    }

    @Override
    public void getCasts(String movieId, String apiKey, final GetCasteCallback callback) {

        RestApiMethods client =
                RetrofitClient.getInstance().create(RestApiMethods.class);

        Call<MovieCasts> call = client.getMovieCaste(Integer.parseInt(movieId), API_KEY);
        call.enqueue(new Callback<MovieCasts>() {
            @Override
            public void onResponse(Call<MovieCasts> call, Response<MovieCasts> response) {


                if (response != null) {
                    MovieCasts movieCasts = response.body();

                    //movieCasts contains list of casts and other informations
                    //extract only cast list from the whole information

                    if (movieCasts.getCast().size() != 0) {
                        if (movieCasts.getCast().size() > 5) {
                            callback.handleMovieCaste(movieCasts.getCast().subList(0, 5));//show top 5 casts only
                        } else {
                            callback.handleMovieCaste(movieCasts.getCast());
                        }
                    } else
                        callback.handleMovieCaste(movieCasts.getCast());//send empty cast list

                } else {
                    //response failure
                    callback.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(Call<MovieCasts> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());


                callback.onFailure(new Throwable());
            }
        });


    }


    @Override
    public void getTrailer(String movieId, String apikey, final GetTrailerCallback callback) {


        RestApiMethods client =
                RetrofitClient.getInstance().create(RestApiMethods.class);

        Call<Trailer> call = client.getMovieTrailer(Integer.parseInt(movieId), API_KEY);
        call.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, final Response<Trailer> response) {


                if (response != null) {
                    Trailer trailer = response.body();

                    if (!trailer.getResults().isEmpty()) {
                        if (trailer.getResults().size() > 2) {
                            callback.handleTrailerLink(trailer.getResults().subList(0, 2));
                        } else
                            callback.handleTrailerLink(trailer.getResults());
                    }


                } else {
                    //response failure
                    callback.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());


                callback.onFailure(new Throwable());
            }
        });

    }
}
