package com.example.teach.teachchatapp;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by TeacH on 5/24/2016.
 */
public class Notifications extends IntentService{


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Notifications(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
