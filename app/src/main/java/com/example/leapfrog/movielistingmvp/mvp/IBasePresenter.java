package com.example.leapfrog.movielistingmvp.mvp;


public interface IBasePresenter<ViewT> {

    void onViewActive(ViewT view);

    void onViewInactive();
}
