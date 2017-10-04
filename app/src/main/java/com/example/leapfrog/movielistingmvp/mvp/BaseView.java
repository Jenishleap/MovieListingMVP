package com.example.leapfrog.movielistingmvp.mvp;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.leapfrog.movielistingmvp.R;

import butterknife.Bind;


/**
 * Abstract base class to be extended by any MVP View such as {@link Fragment} and
 * {@link android.support.v4.app.ActivityCompat}. It contains common attributes and methods to be
 * shared across Presenters.
 */
public abstract class BaseView extends Fragment implements IBaseView {

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    @Override
    public void showToastMessage(String message) {
        try {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void setTrailerProgressBar(boolean show) {

    }
}



/*getContext() method is not overridden in this class from IBaseView interface because
this method is already overrided by Fragment class */
