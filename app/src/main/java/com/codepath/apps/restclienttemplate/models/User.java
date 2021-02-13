package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

// Represents the person associated with each tweet
public class User {
    protected String name;
    protected String screenName;
    protected String publicImageUrl;

    public User(String name, String screenName, String publicImageUrl) {
        this.name = name;
        this.screenName = screenName;
        this.publicImageUrl = publicImageUrl;
    }

    public static User getUserFromJsonObject(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("name");
        String screenName = jsonObject.getString("screen_name");
        String publicImageUrl = jsonObject.getString("profile_image_url_https");

        return new User(name, screenName, publicImageUrl);
    }

    // GETTERS AND SETTERS


    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPublicImageUrl() {
        return publicImageUrl;
    }
}
