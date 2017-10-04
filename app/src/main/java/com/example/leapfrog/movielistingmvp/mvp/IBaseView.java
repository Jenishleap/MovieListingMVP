package com.example.leapfrog.movielistingmvp.mvp;


import android.content.Context;

public interface IBaseView {//these are optional methods that view fragment might or might not override


    void showToastMessage(String message);

    void setProgressBar(boolean show);

    void setTrailerProgressBar(boolean show);

    Context getContext();


}
