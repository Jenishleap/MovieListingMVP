package com.example.leapfrog.movielistingmvp.data.moviedetail;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.leapfrog.movielistingmvp.R;
import com.example.leapfrog.movielistingmvp.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class YoutubeThumbnailAdapter extends RecyclerView.Adapter<YoutubeThumbnailAdapter.ItemHolder> {

    private List<String> videoUrls;
    private YoutubeThumbnailAdapter.OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    public Context mContext;

    public YoutubeThumbnailAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        videoUrls = new ArrayList<>();
        this.mContext = context;
    }

    public YoutubeThumbnailAdapter(Context context, List<String> movies) {
        layoutInflater = LayoutInflater.from(context);
        movies.clear();
        this.videoUrls = movies;
    }

    @Override
    public YoutubeThumbnailAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemCardView = (View) layoutInflater.inflate(R.layout.youtubethumnail, parent, false);
        return new YoutubeThumbnailAdapter.ItemHolder(itemCardView, this);
    }

    @Override
    public void onBindViewHolder(YoutubeThumbnailAdapter.ItemHolder holder, int position) {

        String videoUrl = videoUrls.get(position);
        holder.populateView(videoUrl, mContext);
    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }

    public void setOnItemClickListener(YoutubeThumbnailAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public YoutubeThumbnailAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(YoutubeThumbnailAdapter.ItemHolder item, int position);
    }

    public void add(int location, String iName) {
        videoUrls.add(location, iName);
        notifyItemInserted(location);
    }

    public void addAll(List<String> tbllist) {
        videoUrls.clear();
        videoUrls = tbllist;
        notifyDataSetChanged();
    }

    public void remove(int location) {
        if (location >= videoUrls.size())
            return;

        videoUrls.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private YoutubeThumbnailAdapter parent;
        ImageView ivYoutubeThumbmail;

        public View view;


        public ItemHolder(View cView, YoutubeThumbnailAdapter parent) {
            super(cView);
            view = cView;
            view.setOnClickListener(this);
            this.parent = parent;

            ivYoutubeThumbmail = (ImageView) view.findViewById(R.id.ivYoutubethumbnail);


        }


        public void populateView(String thumbnailUrl, Context context) {

            Glide.with(context)
                    .load(thumbnailUrl) // Image URL
                    .centerCrop() // Image scale type
                    .crossFade()
//                    .override(800, 500) // Resize image
                    .placeholder(R.drawable.profileplaceholder) // Place holder image
                    .error(R.drawable.profileplaceholder) // On error image
                    .into(ivYoutubeThumbmail); // ImageView to display image

        }


        @Override
        public void onClick(View v) {
            final YoutubeThumbnailAdapter.OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition());
            }
        }
    }


    public void update(List<String> movieList) {
        videoUrls.clear();
        for (String model : movieList) {
            videoUrls.add(model);
        }
        notifyDataSetChanged();
    }


}
