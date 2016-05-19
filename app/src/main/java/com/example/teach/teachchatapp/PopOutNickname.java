package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PopOutNickname extends AppCompatActivity {

    private Button btnNickname;
    private EditText etNickname;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_out_nickname);

        btnNickname = (Button)findViewById(R.id.btnNickname);
        etNickname = (EditText)findViewById(R.id.etNickname);

        SetPopOut();


        btnNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNickname.getText().toString().equals("")){
                    nickname = SetNickname(etNickname.getText().toString());
                    Toast.makeText(PopOutNickname.this, nickname + " is saved as nickname", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PopOutNickname.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(PopOutNickname.this, "You have to type your nickname", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(PopOutNickname.this, "You have to type your nickname", Toast.LENGTH_SHORT).show();
    }


    // Set activity window to 80% width and 50%, and it's transparent
    public void SetPopOut()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.5));
    }


    public String SetNickname(String nickname)
    {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPref.edit();
        spEditor.putString("nickname", nickname);
        spEditor.apply();

        return nickname;
    }


}
