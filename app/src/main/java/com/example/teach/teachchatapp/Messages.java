package com.example.teach.teachchatapp;

import java.util.Date;

/**
 * Created by TeacH on 5/18/2016.
 */
public class Messages {

    private int id;
    private String nickname;
    private String message;
    private String timestamp;


    public Messages(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
