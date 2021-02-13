package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Represents each Tweet
public class Tweet {
    protected User user;
    protected String text;
    protected String createdAt;

    public Tweet(User user, String text, String createdAt) {
        this.user = user;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static List<Tweet> getListOfTweets(JSONArray jsonArray) throws JSONException {
        List<Tweet> listOfTweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJsonObject = jsonArray.getJSONObject(i);

            User user = User.getUserFromJsonObject(tweetJsonObject.getJSONObject("user"));
            String text = tweetJsonObject.getString("text");
            String createdAt = tweetJsonObject.getString("created_at");

            listOfTweets.add(new Tweet(user, text, createdAt));
        }

        return listOfTweets;
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
