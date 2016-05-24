package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by TeacH on 5/23/2016.
 */

public class GetDataAsynTask extends AsyncTask<String, Void, Boolean>{


    private String data;
    private JSONArray jsonArray;
    public ListView lvMessages;
    private String siteUrlDownload;
    public int lastId;
    ArrayList<Messages> messagesArrayList;
    public Context appCont;



    public GetDataAsynTask(Context appCont, String data, String siteUrlDownload, ListView lvMessages) {
        this.data = data;
        this.siteUrlDownload = siteUrlDownload;
        this.lvMessages = lvMessages;
        this.appCont = appCont;
        //this.jsonArray = jsonArray;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public ListView getLvMessages() {
        return lvMessages;
    }

    public void setLvMessages(ListView lvMessages) {
        this.lvMessages = lvMessages;
    }

    public String getSiteUrlDownload() {
        return siteUrlDownload;
    }

    public void setSiteUrlDownload(String siteUrlDownload) {
        this.siteUrlDownload = siteUrlDownload;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        {
            try {
                //HttpClient client = new DefaultHttpClient();
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet httpGet = new HttpGet(getSiteUrlDownload());
                HttpResponse response = client.execute(httpGet);

                InputStream inputStream = response.getEntity().getContent();

                if(inputStream != null) {
                    data = convertInputStreamToString(inputStream);
                    jsonArray = new JSONArray(data);
                    JSONArrayToArrayList(jsonArray);
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

            try {
                CustomAdapter customAdapter = new CustomAdapter(appCont, messagesArrayList);
                lvMessages.setAdapter(customAdapter);
            }
            catch (Exception e){
                Toast.makeText(appCont, "SetAdapter error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

        }

    }

    // Convert Input Stream to String
    public String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        bufferedReader.close();
        return result;
    }




    public ArrayList<Messages> JSONArrayToArrayList(JSONArray jsonArray)
    {
        messagesArrayList = new ArrayList();


        try {

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                Messages messages = new Messages();

                messages.setId(jsonData.getInt("id"));
                messages.setNickname(jsonData.getString("nickname"));
                messages.setMessage(jsonData.getString("message"));
                messages.setTimestamp(jsonData.getString("timestamp"));


                messagesArrayList.add(messages);

            }
            Collections.reverse(messagesArrayList);
            //Find the last ID and put it in lastID, we use it later to check if there is new messages
            int value;
            int max = -1;
            for(int i = 0; i < messagesArrayList.size(); i++){
                value = messagesArrayList.get(i).getId();
                if(max < value){
                    max = value;
                }
            }

            Messages.setUserNickname(SharedPref.getNickname(appCont));
            return messagesArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



}
