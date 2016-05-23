package com.example.teach.teachchatapp;


/**
 * Created by TeacH on 5/18/2016.
 */
public class Messages {

    private int id;
    private String nickname;
    private String message;
    private String timestamp;
    private static String userNickname;


    public Messages(){}

    public static String getUserNickname() {
        return userNickname;
    }

    public static void setUserNickname(String userNickname) {
        Messages.userNickname = userNickname;
    }

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
