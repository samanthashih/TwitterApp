package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

        // Clean all elements of the recycler
        public void clear() {
            tweets.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Tweet> list) {
            tweets.addAll(list);
            notifyDataSetChanged();
        }



    //define view holder  = inner class -- start here!
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvTimestamp;
        TextView tvName;
        TextView tvReplyCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfile);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvReplyCount = itemView.findViewById(R.id.tvReplyCount);
        }

        //creating our own bind method to bind a tweet to view holder
        public void bind(Tweet tweet) {
            // take out diff attributes from tweet and use attr to fill out the itemviews
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText(context.getString(R.string.at)+tweet.user.screenName);

            // set profile img using glide
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(100)).into(ivProfileImage);

            // if tweet has an image, add it
            if (tweet.mediaImageUrl != "") {
                ivMedia.setVisibility(View.VISIBLE);
                Log.i("media", tweet.body  + " media link: " + tweet.mediaImageUrl);
                Glide.with(context).load(tweet.mediaImageUrl).transform(new RoundedCorners(70)).into(ivMedia);
            } else {
                Log.i("media", tweet.body + " media link: NONE");
                ivMedia.setVisibility(View.GONE);
            }

            tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));
            tvReplyCount.setText(""+tweet.retweetCount);
        }

        // stretch - adding timestamp
        // function to get timestamp from json data
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(rawJsonDate).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "1m";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + "m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + "h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + "d";
                }
            } catch (ParseException e) {
                Log.i("timestamp", "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }
    }

}

