package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    private String nickname;
    private String message;
    private Button btnSend;
    private String siteUrl = "http://banetest.d-logic.net/android_scripts/chat_app/upload.php";
    EditText etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String nicknameCheck = sharedPref.getString("nickname", null);

        if(nicknameCheck != null){
            nickname = getNickname();
        }
        else{
            Intent intent = new Intent(this, PopOutNickname.class);
            startActivity(intent);
        }


        btnSend = (Button)findViewById(R.id.btnSend);
        etMessage = (EditText)findViewById(R.id.etMessage);
        nickname = getNickname();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etMessage.getText().toString().equals("")){
                    message = etMessage.getText().toString();
                    new PostDataAsyncTask().execute();
                    etMessage.setText("");
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter a message first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





        public class PostDataAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            String jsonString = getMsgToJson(nickname, message).toString();

            //HttpClient client = new DefaultHttpClient();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(siteUrl);
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

            if(result)
            {
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
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
}
