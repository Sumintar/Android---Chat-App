package com.example.teach.teachchatapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TeacH on 5/23/2016.
 */
public class SharedPref {

    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "prefs";

    public SharedPref() {
        // Blank
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getNickname(Context context) {
        return getPrefs(context).getString("userInfo", "nickname");
    }

    public static void setNickname(Context context, String nickname) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("userInfo", nickname);
        editor.commit();

    }

}
