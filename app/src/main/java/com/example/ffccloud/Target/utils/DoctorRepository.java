package com.example.ffccloud.Target.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FfcDAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.FilteredDoctoredModel;
import com.example.ffccloud.ScheduleModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DoctorRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<FilteredDoctoredModel>> allFilterDoctor;
    private final LiveData<List<ScheduleModel>> allSchedule;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());



    public DoctorRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allFilterDoctor = mDao.getAllFilterDoctor();
        allSchedule = mDao.getAllSchedule();

    }

    public void insertSchedule(List<ScheduleModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertAllSchedule(list);
            }
        });      }
    public void deleteAllSchedule( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllSchedule();
            }
        });
    }

    public LiveData<List<ScheduleModel>> getAllSchedule()
    {
        return allSchedule;
    }
    public LiveData<List<FilteredDoctoredModel>> getQueryFilterDocList( String doc_classsification, String doc_grade)
    {
        return mDao.getQuerydocotor(doc_classsification,doc_grade);
    }

    public void InsertFilterDoctor(List<FilteredDoctoredModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertDoctor(list);
            }
        });
    }

    public void DeleteAllFilterDoctor( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllDoctor();
            }
        });
    }

    public LiveData<List<FilteredDoctoredModel>> getAllFilterDoctor()
    {
        return allFilterDoctor;
    }



//    public class InsertFilterDoctorAsyncTask extends AsyncTask<List<FilteredDoctoredModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<FilteredDoctoredModel>... lists) {
//            mDao.insertDoctor(lists[0]);
//            return null;
//        }
//    }
//    public class InsertScheduleAsyncTask extends AsyncTask<List<ScheduleModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<ScheduleModel>... lists) {
//            mDao.insertAllSchedule(lists[0]);
//            return null;
//        }
//    }
//
//
//
//
//    public class DeleteAllFilterDoctorAsyncTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllDoctor();
//            return null;
//        }
//    }
//
//
//    public class DeleteAllScheduleAsyncTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllSchedule();
//            return null;
//        }
//    }

}
