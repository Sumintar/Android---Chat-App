package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    private String nickname;
    private Button btnSend;

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getNickname()
    {

        String nickname = sharedPref.getString("nickname", "");
        return nickname;
    }
}
