package com.example.teach.teachchatapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    public String data;
    int lastID;
    private String nickname, message;
    private final String siteUrlUpload = "http://banetest.d-logic.net/android_scripts/chat_app/upload.php";
    //private String siteUrl = "http://banetest.d-logic.net/android_scripts/chat_app/";
    private final String siteUrlDownload = "http://banetest.d-logic.net/android_scripts/chat_app/download.php";
    private final int appRefresh = 1000;

    private Button btnSend;
    EditText etMessage;

    ArrayList<Messages> messagesArrayList;
    ListView lvMessages;
    ListAdapter customAdapter;
    JSONArray jsonArray;

    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //String nicknameCheck = sharedPref.getString("nickname", null);
        String nicknameCheck = sharedPref.getString("nickname", null);

        lvMessages = (ListView)findViewById(R.id.lvMessages);
        btnSend = (Button) findViewById(R.id.btnSend);
        etMessage = (EditText) findViewById(R.id.etMessage);


        nickname = SharedPref.getNickname(getApplicationContext());

        refreshApplication();



        Log.i("teach", "on create()" + nickname);
        // If nickname didn't set (app running for the first time), go to choose nickname pop out activity
        if (nickname != null & !nickname.equals("nickname")) {
            nickname = SharedPref.getNickname(getApplicationContext());
        } else {
            Log.i("teach", "nickname is: " + nickname);
            Log.i("teach", "nickname from SharedPref is: " + SharedPref.getNickname(this));
            Intent intent = new Intent(this, PopOutNickname.class);
            startActivity(intent);
        }

        new GetDataAsynTask(this, data, siteUrlDownload, lvMessages).execute(); // Load messages when app starts




        // Button for sending message
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMessage.getText().toString().equals("")) {
                    message = etMessage.getText().toString();
                    new PostDataAsyncTask().execute();
                    etMessage.setText("");

                } else {
                    Toast.makeText(MainActivity.this, "Enter a message first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



        // Send JSON to PHP script which upload message to a database
        public class PostDataAsyncTask extends AsyncTask<String, Void, Boolean> {
            @Override
            protected Boolean doInBackground(String... params) {

                String jsonString = getMsgToJson(nickname, message).toString();

                //HttpClient client = new DefaultHttpClient();
                HttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(siteUrlUpload);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("json", jsonString));
                try {
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                    client.execute(post);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }


            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                if (result) {
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    new GetDataAsynTask(getApplication(), data, siteUrlDownload, lvMessages).execute();
                } else {
                    Toast.makeText(MainActivity.this, "Message is NOT sent", Toast.LENGTH_SHORT).show();
                }
            }

        }



    //Convert nickname and message from the phone to JSON object
    private JSONObject getMsgToJson(String nickname, String message)
    {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nickname", nickname);
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public void refreshApplication() {
        handler = new android.os.Handler();
        handler.postDelayed(mRunnable, 5000);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            new GetDataAsynTask(getApplication(), data, siteUrlDownload, lvMessages).execute();
            handler.postDelayed(mRunnable, appRefresh);
        }
    };

}
