package com.example.leapfrog.movielistingmvp.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.models.Category;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.models.MovieCategory;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface TablesDao {

    /*-----movies------*/

    @Query("SELECT * FROM " + DatabaseConfig.MovieColumns.TABLENAME)
    List<Movie> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Movie... movie);

    @Insert
    void insertAllMovies(ArrayList<Movie> movies);


    @Query("SELECT * FROM " + DatabaseConfig.MovieColumns.TABLENAME + " WHERE id IN (:movieIds)")
    List<Movie> findMoviesByIds(List<Integer> movieIds);



    /*-------category------*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... category);


    /*@Query("SELECT * FROM " + DatabaseConfig.CategoryColumns.TABLENAME)
    List<Category> getAllCategory();*/

    @Query("SELECT * FROM " + DatabaseConfig.CategoryColumns.TABLENAME)
    List<Category> getAllCategory();



    /*-----movie category-------*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MovieCategory... movieCategorie);

    @Query("SELECT * FROM " + DatabaseConfig.MovieCategoryColumns.TABLENAME)
    List<MovieCategory> getAllMovieCats();


    @Query("SELECT " + DatabaseConfig.MovieCategoryColumns.MOVIE_ID + " FROM " + DatabaseConfig.MovieCategoryColumns.TABLENAME + " where " + DatabaseConfig.MovieCategoryColumns.CAT_ID + " = :id")
    List<Integer> getMovieIdsFromCatId(int id);

}
