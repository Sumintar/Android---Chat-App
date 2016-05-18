package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    String data;
    int lastID;
    private String nickname;
    private String message;
    private Button btnSend;
    private final String siteUrlUpload = "http://banetest.d-logic.net/android_scripts/chat_app/upload.php";
    private String siteUrl = "http://banetest.d-logic.net/android_scripts/chat_app/";
    private final String siteUrlDownload = "http://banetest.d-logic.net/android_scripts/chat_app/download.php";
    EditText etMessage;
    JSONObject jsonObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String nicknameCheck = sharedPref.getString("nickname", null);

        if (nicknameCheck != null) {
            nickname = getNickname();
        } else {
            Intent intent = new Intent(this, PopOutNickname.class);
            startActivity(intent);
        }


        btnSend = (Button) findViewById(R.id.btnSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        nickname = getNickname();
        Log.i("teach", "asdf");
        new GetDataAsyncTask().execute();

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


    public class GetDataAsyncTask extends AsyncTask<String, Void, Boolean>{


        @Override
        protected Boolean doInBackground(String... strings) {
            {
                try {
                    //HttpClient client = new DefaultHttpClient();
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet(siteUrlDownload);
                    HttpResponse response = client.execute(httpGet);

                    InputStream inputStream = response.getEntity().getContent();

                    if(inputStream != null) {
                        data = convertInputStreamToString(inputStream);

                        jsonObj = new JSONObject(data);

                    }
                    else {
                        data = "input Stream is null!";
                    }

                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    data = "Error: " + e;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {

            }
            else
            {

            }
        }

        public String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }
    }







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
                } else {
                    Toast.makeText(MainActivity.this, "Message is NOT sent", Toast.LENGTH_SHORT).show();
                }
            }

        }


    private String getNickname()
    {
        String nickname = sharedPref.getString("nickname", "");
        return nickname;
    }




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

    private List<Messages> JSONArrayToArrayList()
    {
        List<Messages> messagesArrayList = new ArrayList<Messages>();

        String msg = "message";
        try {
            JSONArray jsonArray = new JSONArray(jsonObj);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                Messages messages = new Messages();

                messages.setId(jsonData.getInt("id"));
                messages.setNickname(jsonData.getString("nickname"));
                messages.setMessage(jsonData.getString("nickname"));
                messages.setTimestamp(jsonData.getString("timestamp"));

                messagesArrayList.add(messages);
            }
            //Find the last ID and put it in lastID, we use it later to check if there is new messages
            int value;
            int max = -1;
            for(int i = 0; i < messagesArrayList.size(); i++){
                value = messagesArrayList.get(i).getId();
                if(max < value){
                    max = value;
                }
            }
            lastID = max;

            return messagesArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
