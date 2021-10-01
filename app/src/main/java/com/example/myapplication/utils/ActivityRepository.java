package com.example.myapplication.utils;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.myapplication.Database.FfcDAO;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.ModelClasses.ClassificationModel;


import java.util.List;

public class ActivityRepository {


    private FfcDAO mDao;
    Application application;
    private final LiveData<List<Activity>> allActivities;
    private  LiveData<List<Activity>> taskActivities;
    private  LiveData<List<Activity>> queryActivities;
    private  LiveData<List<Activity>> withoutTaskActivities;



    public ActivityRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allActivities = mDao.getAllActivities();
        taskActivities = mDao.getTaskActivities();
        queryActivities = mDao.getQueryActivity();
        withoutTaskActivities =  mDao.getActivityWithoutTask();


    }
    public LiveData<List<Activity>> getWithoutTaskActivities( )
    {
        return withoutTaskActivities;
    }

    public LiveData<List<Activity>> getQueryActivity( )
    {
        return queryActivities;
    }


    public LiveData<List<Activity>> getTaskActivity()
    {
        return taskActivities;
    }
    public LiveData<List<Activity>> getAllActivity()
    {
        return allActivities;
    }

    public void insertActivity(Activity activity) {
        new ActivityRepository.InsertActivityAsyncTask().execute(activity);
    }

    public void updateActivity(Activity activity) {
        new ActivityRepository.UpdateActivityAsyncTask().execute(activity);
    }

    public void deleteActivity(Activity activity)
    {
        new ActivityRepository.DeleteActivityAsyncTask().execute(activity);
    }
    public void deleteAllActivity()
    {
        new ActivityRepository.DeleteAllActivityAsyncTask().execute();
    }

    public class InsertActivityAsyncTask extends AsyncTask<Activity,Void,Void>
    {


        @Override
        protected Void doInBackground(Activity... activities) {
            mDao.insertActivity(activities[0]);
            return null;
        }
    }

    public class DeleteActivityAsyncTask extends AsyncTask<Activity,Void,Void>
    {


        @Override
        protected Void doInBackground(Activity... activities) {
            mDao.deleteActivity(activities[0]);
            return null;
        }
    }
    public class DeleteAllActivityAsyncTask extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAllActivity();
            return null;
        }
    }
    public class UpdateActivityAsyncTask extends AsyncTask<Activity,Void,Void>
    {


        @Override
        protected Void doInBackground(Activity... activities) {
            mDao.updateActivity(activities[0]);
            return null;
        }
    }
}
