package com.example.leapfrog.movielistingmvp.data.moviedetail;


import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.models.MovieCasts;
import com.example.leapfrog.movielistingmvp.mvp.IBasePresenter;
import com.example.leapfrog.movielistingmvp.mvp.IBaseView;

import java.util.List;

public interface MovieDetailContract {

    interface View extends IBaseView {

        void showCastes(List<Cast> casts);

        void shouldShowPlaceholderText();

        void setTrailerUrl(List<String> urls, List<String> videoKeys);
    }


    interface Presenter extends IBasePresenter<View> {

        void getCastes(String id);

        void getTrailerUrl(String id);

    }

}
