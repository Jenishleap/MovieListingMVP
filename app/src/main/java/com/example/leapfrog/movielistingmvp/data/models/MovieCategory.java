package com.example.leapfrog.movielistingmvp.data.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.leapfrog.movielistingmvp.data.local.DatabaseConfig;

@Entity(tableName = DatabaseConfig.MovieCategoryColumns.TABLENAME, indices = {@Index(value = {DatabaseConfig.MovieCategoryColumns.MOVIE_ID, DatabaseConfig.MovieCategoryColumns.CAT_ID}, unique = true)})
public class MovieCategory {


    /*can't have two rows with same movie_id and cat_id*/


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseConfig.MovieCategoryColumns.ID)
    public int id;


    @ColumnInfo(name = DatabaseConfig.MovieCategoryColumns.MOVIE_ID)
    private int movieId;


    @ColumnInfo(name = DatabaseConfig.MovieCategoryColumns.CAT_ID)
    private int catId;


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }
}
