package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

import static android.widget.Toast.*;

public class ComposeActivity extends AppCompatActivity {
    // REST CLIENT
    TweeterClient tweeterClient;

    // MODEL

    // VIEW
    EditText etCompose;
    Button btnTweet;

    // ADAPTER


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // REST Client
        tweeterClient = TweeterApplication.getRestClient(this);

        // VIEW
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        // Make API call to Twitter to post the Tweet.
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this,
                            "Tweet cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tweetContent.length() > 140) {
                    Toast.makeText(ComposeActivity.this,
                            "Tweet is too long.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    tweeterClient.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Tweet tweet = Tweet.getTweetFromJsonObject(json.jsonObject);
                                Intent goBackToHomeTimeline = new Intent();
                                goBackToHomeTimeline.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, goBackToHomeTimeline);
                                finish();
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
        });
    }
}