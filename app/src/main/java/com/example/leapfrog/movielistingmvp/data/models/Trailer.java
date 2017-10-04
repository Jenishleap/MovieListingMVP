package com.example.leapfrog.movielistingmvp.data.models;

import java.util.List;


public class Trailer {
    
    private Integer id;
    private List<TrailerResults> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerResults> getResults() {
        return results;
    }

    public void setResults(List<TrailerResults> results) {
        this.results = results;
    }


}
