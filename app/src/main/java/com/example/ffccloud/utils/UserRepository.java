package com.example.ffccloud.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FfcDAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.ModelClasses.QualificationModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<QualificationModel>> allQualification;
    private final LiveData<List<ClassificationModel>> allClassification;
    private  LiveData<List<GradingModel>> allGrades;
    private final LiveData<List<LocationRequestedUser>> allUsers;

    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());


    public UserRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allClassification = mDao.getAllClassification();
        allGrades = mDao.getAllGrades();
        allQualification = mDao.getAllQualification();
        allUsers = mDao.getAllUser();

    }



    public void InsertClassifications(List<ClassificationModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertClassification(list);
            }
        });    }
    public void InsertGrades(List<GradingModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertGrades(list);
            }
        });
    }
    public void InsertQualifications(List<QualificationModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertQualification(list);
            }
        });    }

    public void DeleteAllClassification( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllClassification();
            }
        });
    }
    public void DeleteAllGrades( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllGrade();
            }
        });    }
    public void DeleteAllQualification( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllQualification();
            }
        });    }

    public LiveData<List<ClassificationModel>> getAllClassification()
    {
        return allClassification;
    }

    public LiveData<List<GradingModel>> getAllGrades()
    {
        return allGrades;
    }
    public LiveData<List<QualificationModel>> getAllQualification()
    {
        return allQualification;
    }


    public void insertUser(LocationRequestedUser user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertUser(user);
            }
        });      }


    public void deleteAllUser( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllUser();
            }
        });
    }
    public void deleteUser( LocationRequestedUser user)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteUser(user);
            }
        });
    }

    public boolean isUserExists(String email)
    {
        return mDao.isUserExists(email);
    }
    public LiveData<List<LocationRequestedUser>> getAllUser()
    {
        return allUsers;
    }

}
