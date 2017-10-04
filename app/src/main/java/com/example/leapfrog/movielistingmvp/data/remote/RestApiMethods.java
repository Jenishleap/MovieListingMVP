package com.example.leapfrog.movielistingmvp.data.remote;


import com.example.leapfrog.movielistingmvp.data.models.AllMovies;
import com.example.leapfrog.movielistingmvp.data.models.MovieCasts;
import com.example.leapfrog.movielistingmvp.data.models.MovieDetail;
import com.example.leapfrog.movielistingmvp.data.models.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


//synchronous
public interface RestApiMethods {



    @GET("movie/{category}")
    Call<AllMovies> getAllMovies(@Path("category") String category, @Query("api_key") String apiKey);


    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);
    //make sure id is not in string

    @GET("movie/{id}/credits")
    Call<MovieCasts> getMovieCaste(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos?")
    Call<Trailer> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);


}
