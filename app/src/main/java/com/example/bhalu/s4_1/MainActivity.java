package com.example.bhalu.s4_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.load_image);
        final ImageView imageView = findViewById(R.id.image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.nasa.gov/planetary/apod").newBuilder();
                urlBuilder.addQueryParameter("api_key", "0fkJfLdklR7EvXAlmxGH3Vr2tXKXSQzNi8Pv0s2T");
                String url = urlBuilder.build().toString();
                Log.d("debug", url);
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback() {          // asyncronous call by enqueue
                    @Override
                    public void onFailure(com.squareup.okhttp.Request request, IOException throwable) {
                        throwable.printStackTrace();
                        Log.d("debug", "fail");
                    }

                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        if (response.code() == 200) {
                            Log.d("debug",response.body().toString());
                            try {
                                JSONObject jsonRootObject = new JSONObject(response.body().string());
                                final String explaination = jsonRootObject.getString("url");
                                Log.d("debug", explaination);
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.d("UI thread", "I am the UI thread");
                                        Picasso.with(getApplicationContext()).load(explaination).into(imageView);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("debug",response.body().toString());
                    }
                });

            }
        });
    }


}
