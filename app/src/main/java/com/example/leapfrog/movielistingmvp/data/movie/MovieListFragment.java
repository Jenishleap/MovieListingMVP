package com.example.leapfrog.movielistingmvp.data.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leapfrog.movielistingmvp.R;
import com.example.leapfrog.movielistingmvp.data.local.AppDatabase;
import com.example.leapfrog.movielistingmvp.data.local.LocalDataSource;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.moviedetail.MovieDetailActivity;
import com.example.leapfrog.movielistingmvp.data.remote.RemoteDataSource;
import com.example.leapfrog.movielistingmvp.data.remote.RestApiMethods;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;
import com.example.leapfrog.movielistingmvp.helper.ItemClickSupport;
import com.example.leapfrog.movielistingmvp.mvp.BaseView;
import com.example.leapfrog.movielistingmvp.threading.MainUiThread;
import com.example.leapfrog.movielistingmvp.threading.ThreadExecutor;

import java.util.ArrayList;


import butterknife.ButterKnife;


public class MovieListFragment extends BaseView implements MovieContract.View {


    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_RELEASE_DATE = "movie_releasedate";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_POSTER_PATH = "movie_posterpath";
    public static final String MOVIE_BACKGROUND_PATH = "movie_backgrounpath";
    public static final String POPULAR_MOVIES = "popular_movies";
    public static final String TOP_RATED_MOVIES = "toprated_movies";
    public static final String UPCOMING_MOVIES = "upcoming_movies";
    public static final String NOW_PLAYING_MOVIES = "now_playing_movies";
    private static final String CATEGORY = "cats";
    public final String LIFECYCLE = "lifecycle";
    private String category;

    private RecyclerView rvMovieList;
    private MovieAdapter movieAdapter;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvPlaceHolder;


    private ArrayList<Movie> myMovies;
    public ArrayList<Integer> newMovieIds;


    private MovieContract.Presenter presenter;
    public RemoteDataSource remoteDataSource;
    public static ProgressDialog progressDialog = null;


    private LocalDataSource localDataSource;

    public static String currentPage;


    public static MovieListFragment newInstance(String cat) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, cat);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMovies = new ArrayList<>();
        newMovieIds = new ArrayList<>();
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();
        remoteDataSource = RemoteDataSource.getInstance(mainUiThread, threadExecutor, RetrofitClient.getInstance().create(RestApiMethods.class));
        localDataSource = LocalDataSource.getInstance(mainUiThread, threadExecutor, AppDatabase.getAppDatabase(getActivity().getApplicationContext()));
        presenter = new MoviePresenter(this, remoteDataSource, localDataSource, threadExecutor, mainUiThread);
        setRetainInstance(true);


        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
            currentPage = category;
        }
        Log.d(LIFECYCLE, "onCreate is called for " + category);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        ButterKnife.bind(this, view);
        initializeUi(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovieList.setAdapter(movieAdapter);

        presenter.storeCategories();

        ItemClickSupport.addTo(rvMovieList).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showDetailActivity(position);
                    }
                });


        switch (category) {
            case MovieListActivity.TOP_RATED:
                if (savedInstanceState != null) {
                    newMovieIds = savedInstanceState.getIntegerArrayList(MovieListActivity.TOP_RATED);
                    myMovies = savedInstanceState.getParcelableArrayList(TOP_RATED_MOVIES);

                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    presenter.passOldMovieIds(new ArrayList<>(newMovieIds), myMovies);
                }
                Log.d("request", "request sent for :" + category);
                getMovies(category);

                break;
            case MovieListActivity.POPULAR:
                if (savedInstanceState != null) {
                    newMovieIds = savedInstanceState.getIntegerArrayList(MovieListActivity.POPULAR);
                    myMovies = savedInstanceState.getParcelableArrayList(POPULAR_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    presenter.passOldMovieIds(new ArrayList<>(newMovieIds), myMovies);
                }
                Log.d("request", "request sent for :" + category);
                getMovies(category);
                break;
            case MovieListActivity.NOW_PLAYING:
                if (savedInstanceState != null) {
                    newMovieIds = savedInstanceState.getIntegerArrayList(MovieListActivity.NOW_PLAYING);
                    myMovies = savedInstanceState.getParcelableArrayList(NOW_PLAYING_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    presenter.passOldMovieIds(new ArrayList<>(newMovieIds), myMovies);
                }
                Log.d("request", "request sent for :" + category);
                getMovies(category);
                break;
            case MovieListActivity.UPCOMING:
                if (savedInstanceState != null) {
                    newMovieIds = savedInstanceState.getIntegerArrayList(MovieListActivity.UPCOMING);
                    myMovies = savedInstanceState.getParcelableArrayList(UPCOMING_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);

                    presenter.passOldMovieIds(new ArrayList<>(newMovieIds), myMovies);
                }
                Log.d("request", "request sent for :" + category);
                getMovies(category);
                break;
            default:
                break;
        }

    }


    @Override
    public void showMovies(ArrayList<Movie> movies, ArrayList<Integer> movieIds) {

        myMovies = movies;
        newMovieIds = new ArrayList<>(movieIds);
        movieAdapter.update(movies);
    }


    @Override
    public void shouldShowPlaceholderText() {
        if (myMovies.isEmpty()) {
            rvMovieList.setVisibility(View.INVISIBLE);
            tvPlaceHolder.setVisibility(View.VISIBLE);
        } else {
            rvMovieList.setVisibility(View.VISIBLE);
            tvPlaceHolder.setVisibility(View.GONE);
        }
    }


    private void initializeUi(View view) {
        tvPlaceHolder = (TextView) view.findViewById(R.id.tvPlaceHolder);
        rvMovieList = (RecyclerView) view.findViewById(R.id.rvmovie_list);
        rvMovieList.setHasFixedSize(true);
        mContext = getActivity().getApplicationContext();
        layoutManager = new LinearLayoutManager(mContext);
        rvMovieList.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(mContext);


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

    }

    private void getMovies(String category) {
        presenter.getMovies(category);
    }


    //this is optional
    @Override
    public void showToastMessage(String message) {
        Log.d("ToastError", "I'm on category: " + category);
        super.showToastMessage(message);
    }


    private void showDetailActivity(int position) {

        String id = null;
        String title = null;
        String rating = null;
        String releaseDate = null;
        String overview = null;
        String posterPath = null;
        String backgroundPath = null;

        try {


            if (myMovies != null && !myMovies.isEmpty()) {
                id = String.valueOf(myMovies.get(position).getId());
                title = myMovies.get(position).getTitle();
                rating = myMovies.get(position).getVoteAverage().toString();
                releaseDate = myMovies.get(position).getReleaseDate();
                overview = myMovies.get(position).getOverview();
                posterPath = myMovies.get(position).getPosterPath();
                backgroundPath = myMovies.get(position).getBackdropPath();
            }


            //go to movie detail activity
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra(MOVIE_ID, id);

            intent.putExtra(MOVIE_TITLE, title);
            intent.putExtra(MOVIE_RATING, rating);
            intent.putExtra(MOVIE_RELEASE_DATE, releaseDate);
            intent.putExtra(MOVIE_OVERVIEW, overview);

            intent.putExtra(MOVIE_POSTER_PATH, posterPath);
            intent.putExtra(MOVIE_BACKGROUND_PATH, backgroundPath);

            startActivity(intent);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("saved_value", "my_saved_value");


        bundle.putString("checkCatName", category);
        switch (category) {
            case "top_rated":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putIntegerArrayList(MovieListActivity.TOP_RATED, newMovieIds);
                bundle.putParcelableArrayList(TOP_RATED_MOVIES, myMovies);
                presenter.storeMoviesToDb(myMovies, category);//store movies when view is about to destroy
                break;
            case "popular":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putIntegerArrayList(MovieListActivity.POPULAR, newMovieIds);
                bundle.putParcelableArrayList(POPULAR_MOVIES, myMovies);
                presenter.storeMoviesToDb(myMovies, category);//store movies when view is about to destroy
                break;
            case "now_playing":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putIntegerArrayList(MovieListActivity.NOW_PLAYING, newMovieIds);
                bundle.putParcelableArrayList(NOW_PLAYING_MOVIES, myMovies);
                presenter.storeMoviesToDb(myMovies, category);//store movies when view is about to destroy
                break;
            case "upcoming":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putIntegerArrayList(MovieListActivity.UPCOMING, newMovieIds);
                bundle.putParcelableArrayList(UPCOMING_MOVIES, myMovies);
                presenter.storeMoviesToDb(myMovies, category);//store movies when view is about to destroy
                break;
            default:
                break;
        }

    }


    public void deleteMovie(int position) {

        try {
            movieAdapter.remove(position);
            myMovies.remove(position);
            newMovieIds.remove(position);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void addMovie(Integer id, Movie movie) {

        try {
            movieAdapter.add(movieAdapter.getItemCount(), movie);
            myMovies.add(movie);
            newMovieIds.add(id);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


    public void showProgressBar() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });

    }

    public void hideProgressBar() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.hide();
                }

            }
        });
    }

}
