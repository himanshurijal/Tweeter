package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import okhttp3.Headers;


public class TimelineActivity<o> extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;

    // REST client
    TweeterClient client;

    // MODEL
    List<Tweet> listOfTweets;

    // VIEW
    SwipeRefreshLayout srlTweets;
    RecyclerView rvTweets;

    // ADAPTER
    TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // REST client
        client = TweeterApplication.getRestClient(this);

        // Model
        listOfTweets = new ArrayList<>();

        // View
        srlTweets = findViewById(R.id.srlTweets);
        rvTweets = findViewById(R.id.rvTweets);

        // When the user slides downwards to refresh the app,
        // clear current tweets and make a new request
        // to the Twitter API to get a list of new tweets.
        srlTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline();
            }
        });

        // Configure the refreshing colors.
        srlTweets.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /*
         * Create and link TweetAdapter to RecyclerView.
         * Then, set the LayoutManager on the RecyclerView.
         */

        // Adapter
        tweetAdapter = new TweetAdapter(this, listOfTweets);
        rvTweets.setAdapter(tweetAdapter);

        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        populateHomeTimeline();
    }

    // Put custom "Compose Tweet" icon in Appbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Navigate to Compose Tweet Activity when user
    // clicks on "Compose Tweet" icon.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.miCompose) {
            Intent composeTweet = new Intent(this, ComposeActivity.class);
            startActivityForResult(composeTweet, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            listOfTweets.add(0, (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet")));
            tweetAdapter.notifyDataSetChanged();
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Get the JSON Array for tweets from the Twitter API endpoint.
    // Create list of Tweets, each with an associated User.
    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("json_object", "Object gotten successfully!" + json.toString());
                // Get json Array and pass it to Tweets.getListOfTweets
                JSONArray tweetsJsonArray = json.jsonArray;

                try {
                    tweetAdapter.clear();
                    tweetAdapter.addAll(Tweet.getListOfTweetsFromJsonArray(tweetsJsonArray));
                    // Signal that refreshing has finished
                    srlTweets.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }
}