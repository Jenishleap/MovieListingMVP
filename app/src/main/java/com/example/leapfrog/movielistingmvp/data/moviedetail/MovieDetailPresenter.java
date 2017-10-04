package com.example.leapfrog.movielistingmvp.data.moviedetail;

import com.example.leapfrog.movielistingmvp.data.DataSource;
import com.example.leapfrog.movielistingmvp.data.models.Cast;
import com.example.leapfrog.movielistingmvp.data.models.TrailerResults;
import com.example.leapfrog.movielistingmvp.data.remote.RemoteDataSource;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;
import com.example.leapfrog.movielistingmvp.mvp.BasePresenter;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieDetailPresenter extends BasePresenter<MovieDetailContract.View> implements MovieDetailContract.Presenter {

    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;


    private RemoteDataSource remoteDataSource;//model object

    public MovieDetailPresenter(MovieDetailContract.View view, RemoteDataSource remoteDataSource,
                                ThreadExecutor threadExecutor, MainUiThread mainUiThread) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
    }


    @Override
    public void getCastes(String id) {


        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        remoteDataSource.getCasts(id, RetrofitClient.API_KEY, new DataSource.GetCasteCallback() {
            @Override
            public void handleMovieCaste(List<Cast> casts) {
                if (view != null) {
                    view.showCastes(casts);
                    view.setProgressBar(false);
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Error");
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Network Failure");
                    view.shouldShowPlaceholderText();
                }
            }
        });
    }


    @Override
    public void getTrailerUrl(String id) {

        if (view == null) {
            return;
        }

        view.setTrailerProgressBar(true);

        remoteDataSource.getTrailer(id, RetrofitClient.API_KEY, new DataSource.GetTrailerCallback() {
            @Override
            public void handleTrailerLink(List<TrailerResults> trailerResults) {
                if (view != null) {
                    if (!trailerResults.isEmpty()) {
                        List<String> urls = new ArrayList<>();
                        List<String> keys = new ArrayList<>();
                        for (TrailerResults result : trailerResults) {
                            String key = result.getKey();
                            String url = extractYoutubeId(result.getKey());
                            urls.add(url);
                            keys.add(key);
                        }
                        view.setTrailerUrl(urls, keys);
                        view.setTrailerProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.setTrailerProgressBar(false);
                    view.showToastMessage("Error");
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setTrailerProgressBar(false);
                    view.showToastMessage("Error");
                    view.shouldShowPlaceholderText();
                }
            }
        });

    }


    public String extractYoutubeId(String videoKey) {


        try {
            String youtubeUrlLink = "http://www.youtube.com/watch?v=";

            StringBuilder builder = new StringBuilder(youtubeUrlLink);
            builder.append(videoKey);

            String query = new URL(builder.toString()).getQuery();
            String[] param = query.split("&");
            String id = null;
            for (String row : param) {
                String[] param1 = row.split("=");
                if (param1[0].equals("v")) {
                    id = param1[1];
                }
            }

            String thumbnailUrl = "http://img.youtube.com/vi/" + id + "/0.jpg";
            return thumbnailUrl;

        } catch (MalformedURLException ex) {
            ex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
}
