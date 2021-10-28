package com.example.healthcareapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionData {

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    public SessionData(Context context)
    {
        pref = context.getSharedPreferences(context.getPackageName(), 0);
        editor = pref.edit();
    }

    public void setString(String key, String value)
    {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key)
    {
        return pref.getString(key, null);
    }

    public void setObjectAsString(String key, String value)
    {
        Log.d(key, value);
        editor.putString(key, value);
        editor.commit();
    }

    public String getObjectAsString(String key)
    {
        return pref.getString(key, "Empty");
    }

}
