package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {

    private SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static final String SHARED_PREFERENCE_NAME = "SharedPreference";
    public static final String TOKEN = "Token";
    public static final String BASE_URL = "http://161.97.178.106/FFCCloudapi/";
    public static final Integer ID = null;
    public static final String  USER_ID = "User ID";
    public static final String  Permission_state = "User ID";
    public static final String Login_State = "LoginState";
    private static SharedPreferenceHelper helperInstance = null;
    private SharedPreferences sharedPreferences;
    public String activity = "Activity 0";
    public boolean startDay = false;
    private String DocListState = "DocListState";
    private String FilterDocListState ="FilterDocListState";

    public boolean getFlterDocListState() {
        return sharedPreferences.getBoolean(FilterDocListState,true);
    }

    public void setFlterDocListState(boolean flterDocListState) {
        sharedPreferences.edit().putBoolean(FilterDocListState, flterDocListState).apply();    }

    public void setUserId(int id)
    {
        sharedPreferences.edit().putInt(USER_ID, id).apply();;

    }
    public int  getUserId() {
        return sharedPreferences.getInt(USER_ID,0);
    }

    public Boolean getGetDocListState() {
        return sharedPreferences.getBoolean(DocListState,true);
    }

    public void setGetDocListState(Boolean getDocListState) {
         sharedPreferences.edit().putBoolean(DocListState, getDocListState).apply();
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new SharedPreferenceHelper(context);
        }
        return helperInstance;
    }

    public String getToken()
    {

        return sharedPreferences.getString(TOKEN, "null");
    }

    public void setToken(String token)
    {
        sharedPreferences.edit().putString(TOKEN, token).apply();
    }

    public String getBaseUrl()
    {

        return sharedPreferences.getString(BASE_URL, BASE_URL);
    }

    public void setBaseUrl(String baseUrl)
    {
        sharedPreferences.edit().putString(BASE_URL, baseUrl).apply();
    }
    public int getID()
    {

        return sharedPreferences.getInt("ID",0);
    }

    public void setID(int id)
    {
        sharedPreferences.edit().putInt(String.valueOf(ID), id).apply();
    }

    public Boolean getLogin_State() {
        return sharedPreferences.getBoolean(Login_State, false);
    }
    public Boolean setLogin_State(boolean state)
    {
        sharedPreferences.edit().putBoolean(Login_State, state).apply();
        return state;
    }

    public String getActivity() {
        return sharedPreferences.getString(String.valueOf(activity), "");
    }
    public void setActivity(String act) {
        sharedPreferences.edit().putString(String.valueOf(activity), act).apply();
    }
    public Boolean getStart() {
        return sharedPreferences.getBoolean(String.valueOf(startDay), false);
    }
    public void setStart(Boolean start) {
        sharedPreferences.edit().putBoolean(String.valueOf(activity), startDay).apply();
    }

}
