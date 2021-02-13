package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    // Pass in the context and the list of tweets
    Context context;
    List<Tweet> listOfTweets;

    public TweetAdapter(Context context, List<Tweet> listOfTweets) {
        this.context = context;
        this.listOfTweets = listOfTweets;
    }

    public void clear() {
        listOfTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> listOfTweets) {
        this.listOfTweets.addAll(listOfTweets);
        notifyDataSetChanged();
    }
    // Create the ViewhHolder.
    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlTweetContainer;
        ImageView ivProfileImage;
        TextView tvFullName;
        TextView tvScreenName;
        TextView tvDateCreated;
        TextView tvTweetBody;
        View vDividerLine;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.rlTweetContainer = itemView.findViewById(R.id.rlTweetContainer);
            this.ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            this.tvFullName = itemView.findViewById(R.id.tvFullName);
            this.tvScreenName = itemView.findViewById(R.id.tvScreenName);
            this.tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            this.tvTweetBody = itemView.findViewById(R.id.tvTweetBody);
            this.vDividerLine = itemView.findViewById(R.id.vDividerLine);
        }

        public void bind(Tweet tweet) {
            Glide.with(context).load(tweet.getUser().getPublicImageUrl()).into(ivProfileImage);
            tvFullName.setText(tweet.getUser().getName());
            tvScreenName.setText(tweet.getUser().getScreenName());
            tvDateCreated.setText(tweet.getTimeDifference());
            tvTweetBody.setText(tweet.getText());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listOfTweets.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfTweets.size();
    }
}
