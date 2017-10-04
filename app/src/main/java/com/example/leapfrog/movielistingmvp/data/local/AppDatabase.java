package com.example.leapfrog.movielistingmvp.data.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.models.Category;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.models.MovieCategory;


@Database(entities = {Movie.class, Category.class, MovieCategory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {




    public abstract TablesDao tablesDao();


    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DatabaseConfig.DB_NAME).build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
