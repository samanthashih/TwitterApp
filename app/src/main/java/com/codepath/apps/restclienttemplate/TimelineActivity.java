package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = "TimelineActivity";
    private final int REQUEST_CODE = 20;
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client =  TwitterApp.getRestClient(this);

        // step 1 = find recycler view
        rvTweets = findViewById(R.id.rvTweets);
        // step 2 = initialize list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        // step 3 = configure recycler view - layout manager + adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this)); //set layout manager on rv class
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu -- adds items to action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; //return true to show menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // item = the menu item icon, when click on menu item which is the make a tweet button
        if (item.getItemId() == R.id.compose) { // if make tweet icon selected
            //Toast.makeText(this, "Compose!", Toast.LENGTH_SHORT).show(); //toast is pop up msg
            // navigate to compose activity
            Intent intent = new Intent(this, ComposeActivity.class); //coming from timelineactivity and going to composeactivity
            startActivityForResult(intent, REQUEST_CODE);
            return true; //return true to consume click of item
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet")); // get data from intent -- get tweet object

            // update recyclerview with tweet (make tweet show up on timeline)
            tweets.add(0, tweet); // modify data source w/ tweet
            adapter.notifyItemInserted(0); // update adapter
            rvTweets.smoothScrollToPosition(0); // have it auto scroll to position 0 (our new tweet)
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess :) " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "JSon exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure :( "+ response, throwable);
            }
        });
    }

    public void onLogoutButton(View view) {
        // forget who's logged in
        TwitterApp.getRestClient(this).clearAccessToken();

        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);

        // finish(); -- i think another way: just need this one line to log out
        // bc it finishes the timeline activity and then goes back to login activity
        // (bc we went from login activity --> timeline actvity)
    }
}