package com.example.leapfrog.movielistingmvp.data.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.leapfrog.movielistingmvp.data.local.DatabaseConfig;

@Entity(tableName = DatabaseConfig.CategoryColumns.TABLENAME)
public class Category {


    @PrimaryKey
    @ColumnInfo(name = DatabaseConfig.CategoryColumns.ID)
    public int id;

    @ColumnInfo(name = DatabaseConfig.CategoryColumns.CATNAME)
    private String cat_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
