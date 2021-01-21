package com.retailx.dreamdx.retailx.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    public static final String PREFS_KEY = "PREFS";

    // static variable single_instance of type Singleton
    private static SharedPreference single_instance = null;
    // static method to create instance of Singleton class
    public static SharedPreference getInstance()
    {
        if (single_instance == null)
            single_instance = new SharedPreference();

        return single_instance;
    }

    public SharedPreference() {
        super();
    }

    public void save(Context context, String key , String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREFS_KEY, text);

        editor.commit();
    }


    public void saveInt(Context context, String key , int text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt(PREFS_KEY, text);

        editor.commit();
    }

    public static void saveNew(Context context, String key , String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("shared-pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValueNew(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "shared-pref", Context.MODE_PRIVATE);
        return  sharedPref.getString(key,"");
    }

    public String getValue(Context context, String key) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY, "-1");
        return text;
    }

    public int getInt(Context context, String key) {
        SharedPreferences settings;
        int text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        text = settings.getInt(PREFS_KEY, -1);
        return text;
    }

    public void clearSharedPreference(Context context, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }
}