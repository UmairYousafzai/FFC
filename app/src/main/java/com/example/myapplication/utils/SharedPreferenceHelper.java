package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {

    private SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static final String SHARED_PREFERENCE_NAME = "SharedPreference";
    public static final String TOKEN = "Token";



    public static final String ATTENDANCE_CONFIGURATION = "Attendance Configuration";
    public static final String BASE_URL = "http://88.99.49.132/FFCCloudapi/";
    public static final Integer ID = null;
    public static final String emp_ID = "Emp ID";
    public static final String  userID = "User ID";
    public static final String  userPassword = "User Password";
    public static final String Login_State = "LoginState";
    private static SharedPreferenceHelper helperInstance = null;
    private final SharedPreferences sharedPreferences;
    public String activity = "Activity 0";
    public boolean startDay = false;
    private final String DocListState = "DocListState";
    private final String FilterDocListState ="FilterDocListState";



    public boolean  getAttendanceConfiguration() {
        return sharedPreferences.getBoolean(ATTENDANCE_CONFIGURATION,true);
    }
    public void setAttendanceConfiguration(boolean configuration) {
        sharedPreferences.edit().putBoolean(ATTENDANCE_CONFIGURATION, configuration).apply();    }

    public boolean getFlterDocListState() {
        return sharedPreferences.getBoolean(FilterDocListState,false);
    }

    public void setFlterDocListState(boolean flterDocListState) {
        sharedPreferences.edit().putBoolean(FilterDocListState, flterDocListState).apply();    }

    public void setEmpID(int id)
    {
        sharedPreferences.edit().putInt(emp_ID, id).apply();;

    }
    public int getEmpID() {
        return sharedPreferences.getInt(emp_ID,0);
    }
    public void setUserPassword(String password)
    {
        sharedPreferences.edit().putString(userPassword, password).apply();;

    }
    public String getUserPassword() {
        return sharedPreferences.getString(userPassword," ");
    }
    public void setUserID(int id)
    {
        sharedPreferences.edit().putInt(userID, id).apply();;

    }
    public int getUserID() {
        return sharedPreferences.getInt(userID,0);
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
        sharedPreferences.edit().putBoolean(String.valueOf(startDay), start).apply();
    }

}
