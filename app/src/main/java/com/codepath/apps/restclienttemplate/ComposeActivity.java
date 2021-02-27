package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView tvCharCounter;
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
        tvCharCounter = findViewById(R.id.tvCharCounter);
        btnTweet = findViewById(R.id.btnTweet);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            // Disable "Tweet" button if character count is
            // more than 280
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tweetLength = etCompose.getText().toString().length();
                String tweetLengthInString = String.valueOf(etCompose.getText().toString().length());
                tvCharCounter.setText(tweetLengthInString + "/280");

                if(tweetLength > 280) {
                    btnTweet.setEnabled(false);
                }
                else {
                    btnTweet.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

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
                else if(tweetContent.length() > 280) {
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