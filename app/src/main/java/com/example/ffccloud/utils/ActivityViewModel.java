package com.example.ffccloud.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.ModelClasses.Activity;

import java.util.List;

public class ActivityViewModel extends AndroidViewModel {
    private final ActivityRepository repository;
    private final LiveData<List<Activity>> allActivity;
    private final LiveData<List<Activity>> taskActivity;
    private final LiveData<List<Activity>> queryActivity;
    private final LiveData<List<Activity>> withoutTaskActivity;


    public ActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ActivityRepository(application);
        allActivity = repository.getAllActivity();
        taskActivity = repository.getTaskActivity();
        queryActivity = repository.getQueryActivity();
        withoutTaskActivity = repository.getWithoutTaskActivities();
    }

    public void deleteAllActivity()
    {
        repository.deleteAllActivity();
    }

    public void deleteActivity(Activity activity)
    {
        repository.deleteActivity(activity);
    }
    public void insertActivity(Activity activity)
    {
        repository.insertActivity(activity);
    }
    public void updateActivity(Activity activity)
    {
        repository.updateActivity(activity);
    }

    public LiveData<List<Activity>> getAllActivity( )
    {
        return allActivity;
    }
    public LiveData<List<Activity>> getTaskActivity( )
    {
        return taskActivity;
    }
    public LiveData<List<Activity>> getQueryActivity( )
    {
        return queryActivity;
    }
    public LiveData<List<Activity>> getWithoutTaskActivity( )
    {
        return withoutTaskActivity;
    }
}
