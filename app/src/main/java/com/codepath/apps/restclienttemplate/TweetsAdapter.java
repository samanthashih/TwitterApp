package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

        Context context;
        List<Tweet> tweets;

        //constructor
        //pass in context and list of tweets
        public TweetsAdapter(Context context, List<Tweet> tweets) {
            this.context = context;
            this.tweets = tweets;
        }

        // for each row, inflate the layout
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //inflate the tweet layout we made
            View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false); //returns a view
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // need to get data at position
            Tweet tweet = tweets.get(position);
            // bind tweet with view holder we get from params
            holder.bind(tweet);
        }

        @Override
        public int getItemCount() {
            return tweets.size();
        }



    //define view holder  = inner class -- start here!
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfile);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
        }

        //creating our own bind method to bind a tweet to view holder
        public void bind(Tweet tweet) {
            // take out diff attributes from tweet and use attr to fill out the itemviews
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);

            // set profile img using glide
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
        }
    }

}

