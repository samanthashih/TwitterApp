package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user; // user model in tweet model so user also needs to be parcelable
    public String mediaImageUrl;
    public int retweetCount;
//    public int likeCount;
    public int id;

    public Tweet() {} // empty constructor needed for parcel library

    public static Tweet fromJSONObject (JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson( jsonObject.getJSONObject("user") );
        tweet.mediaImageUrl=getEntity(jsonObject.getJSONObject("entities"));
        tweet.retweetCount = jsonObject.getInt("retweet_count");
//        tweet.likeCount = jsonObject.getInt("favorite_count");
        tweet.id = jsonObject.getInt("id");
        return tweet;
    }

    public static String getEntity(JSONObject jsonObject) throws JSONException {
        JSONArray allMedia = jsonObject.has("media") ? jsonObject.getJSONArray("media") : null;
        String url = "";
        if (allMedia != null) {
            url =  allMedia.getJSONObject(0).getString("media_url_https");
        }
        Log.d("Tweet", url);
        return url;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJSONObject(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

}
