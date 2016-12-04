package com.amador.androidbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by amador on 2/12/16.
 */

public class Preferences {

    public static final String KEY_PARAM_TOKEN = "KEY";
    private static Preferences instance;
    private static SharedPreferences sharedPreferences;

    public static Preferences getInstance(Context context){

        if(instance == null){

            instance = new Preferences(context);
        }

        return instance;
    }

    private Preferences(Context context){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private  SharedPreferences.Editor getEditor(){

        return sharedPreferences.edit();
    }

    public  void setToken(String valueToken){

        SharedPreferences.Editor editor = getEditor();
        editor.putString(KEY_PARAM_TOKEN, valueToken);
        editor.commit();

    }

    public  String getToken(){

        return sharedPreferences.getString(KEY_PARAM_TOKEN, null);
    }

}
