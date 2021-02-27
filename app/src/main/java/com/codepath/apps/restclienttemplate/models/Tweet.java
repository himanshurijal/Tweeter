package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    protected User user;
    protected String text;
    protected String createdAt;

    // Empty constructor needed for Parceler
    public Tweet() { }

    public Tweet(User user, String text, String createdAt) {
        this.user = user;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static List<Tweet> getListOfTweetsFromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> listOfTweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJsonObject = jsonArray.getJSONObject(i);
            listOfTweets.add(Tweet.getTweetFromJsonObject(tweetJsonObject));
        }

        return listOfTweets;
    }

    public static Tweet getTweetFromJsonObject(JSONObject jsonObject) throws JSONException {
        User user = User.getUserFromJsonObject(jsonObject.getJSONObject("user"));
        String text = jsonObject.getString("text");
        String createdAt = jsonObject.getString("created_at");
        return new Tweet(user, text, createdAt);
    }

    public String getTimeDifference() {
        return TimeFormatter.getTimeDifference(getCreatedAt());
    }

    // GETTERS AND SETTERS

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
