package com.example.leapfrog.movielistingmvp.data.remote;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = "5ba4b7b03f5316d2e5db0391f9190a76";
    public static final String IMG_URL = "https://image.tmdb.org/t/p/w640";
    private static Retrofit retrofit = null;


    //make singlton instance of retrofit object
    public static Retrofit getInstance() {


        //interceptor for logging responses
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // set your desired log level
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //interceptor for adding header files
        HttpInterceptor httpInterceptor = new HttpInterceptor();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(httpInterceptor)
                .addInterceptor(interceptor)
                .build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofit;

    }
}

//in retrofit3 and okhttp3, interceptors works by queue,
// so add logginginterceptors at end after other interceptors
