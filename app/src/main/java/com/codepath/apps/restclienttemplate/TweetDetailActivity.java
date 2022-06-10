package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {
    Tweet tweet;

    //view object fields
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    ImageView ivMedia;
    TextView tvName;
    TextView tvReplyCount;
    TextView tvRetweetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detail);

        //set view object fields
        ivProfileImage = (ImageView) findViewById(R.id.ivProfile);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);
        tvReplyCount = (TextView) findViewById(R.id.tvReplyCount);
        tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);

        //unwrap movie passed in by intent, the simple name = key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        // Set views
        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvScreenName.setText(this.getString(R.string.at)+tweet.user.screenName);


        // set profile img using glide
        Glide.with(this).load(tweet.user.profileImageUrl).transform(new RoundedCorners(100)).into(ivProfileImage);

        // if tweet has an image, add it
        if (!tweet.mediaImageUrl.equals("")) {
            ivMedia.setVisibility(View.VISIBLE);
            Log.i("media", tweet.body  + " media link: " + tweet.mediaImageUrl);
            Glide.with(this).load(tweet.mediaImageUrl).transform(new RoundedCorners(70)).into(ivMedia);
        } else {
            Log.i("media", tweet.body + " media link: NONE");
            ivMedia.setVisibility(View.GONE);
        }

        tvReplyCount.setText(""+tweet.retweetCount);
        tvRetweetCount.setText(""+tweet.retweetCount);

    }
}
