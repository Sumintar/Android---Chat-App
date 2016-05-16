package com.example.teach.teachchatapp;

/**
 * Created by TeacH on 5/16/2016.
 */
public class Persons {

    private String name = null;
    private String message = null;

    public Persons(String name, String message)
    {
        this.name = name;
        this.message = message;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
