package com.example.myapplication.utils;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.myapplication.Database.FfcDAO;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<QualificationModel>> allQualification;
    private final LiveData<List<ClassificationModel>> allClassification;
    private  LiveData<List<GradingModel>> allGrades;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());


    public UserRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allClassification = mDao.getAllClassification();
        allGrades = mDao.getAllGrades();
        allQualification = mDao.getAllQualification();

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


//
//    public class InsertClassificationAsyncTask extends AsyncTask<List<ClassificationModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<ClassificationModel>... lists) {
//            mDao.insertClassification(lists[0]);
//            return null;
//        }
//    }
//
//    public class InsertGradesAsyncTask extends AsyncTask<List<GradingModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<GradingModel>... lists) {
//            mDao.insertGrades(lists[0]);
//            return null;
//        }
//    }
//
//    public class InsertQualificationsAsyncTask extends AsyncTask<List<QualificationModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<QualificationModel>... lists) {
//            mDao.insertQualification(lists[0]);
//            return null;
//        }
//    }
//
//
//
//    public class DeleteAllClassificationAsycTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllClassification();
//            return null;
//        }
//    }
//    public class DeleteAllGradesAsycTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllGrade();
//            return null;
//        }
//    }
//    public class DeleteAllQualificationAsycTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllQualification();
//            return null;
//        }
//    }
}
