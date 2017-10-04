package com.example.leapfrog.movielistingmvp.data.movie;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leapfrog.movielistingmvp.R;
import com.example.leapfrog.movielistingmvp.data.models.Movie;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;


import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemHolder> {

    private List<Movie> movies;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    public Context mContext;

    public MovieAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        movies = new ArrayList<>();
        this.mContext = context;
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        layoutInflater = LayoutInflater.from(context);
        movies.clear();
        this.movies = movies;
    }

    @Override
    public MovieAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemCardView = layoutInflater.inflate(R.layout.movie_detail, parent, false);
        return new ItemHolder(itemCardView, this);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ItemHolder holder, int position) {

        Movie tbl = movies.get(position);
        holder.populateView(tbl, mContext);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemHolder item, int position);
    }

    public void add(int location, Movie iName) {
        movies.add(location, iName);
        notifyItemInserted(location);
    }

    public void addAll(List<Movie> tbllist) {
        movies.clear();
        movies = tbllist;
        notifyDataSetChanged();
    }

    public void remove(int location) {
        if (location >= movies.size())
            return;

        movies.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MovieAdapter parent;
        ImageView ivMoviePoster;
        TextView tvMovieTitle, tvMovieDescription;

        public View view;


        public ItemHolder(View cView, MovieAdapter parent) {
            super(cView);
            view = cView;
            view.setOnClickListener(this);
            this.parent = parent;

            ivMoviePoster = (ImageView) view.findViewById(R.id.ivMoviePoster);
            tvMovieDescription = (TextView) view.findViewById(R.id.tvMovieDescription);
            tvMovieTitle = (TextView) view.findViewById(R.id.tvMovieTitle);


        }

        public void populateView(Movie movie, Context context) {

            try {
                tvMovieTitle.setText(movie.getTitle());
                tvMovieDescription.setText(movie.getOverview());


                Glide.with(context)
                        .load(RetrofitClient.IMG_URL + movie.getPosterPath()) // Image URL
                        .centerCrop() // Image scale type
                        .crossFade()
//                    .override(800, 500) // Resize image
                        .placeholder(R.drawable.profileplaceholder) // Place holder image
                        .error(R.drawable.profileplaceholder) // On error image
                        .into(ivMoviePoster); // ImageView to display image
            } catch (Exception ex) {
                ex.printStackTrace();

            }


        }


        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition());
            }
        }
    }


    public void update(List<Movie> movieList) {
        movies.clear();
        for (Movie model : movieList) {
            movies.add(model);
        }
        notifyDataSetChanged();
    }


}
