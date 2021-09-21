package com.example.myapplication.Target.utils;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapplication.Database.FfcDAO;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.ScheduleModel;

import java.util.List;

public class DoctorRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<FilteredDoctoredModel>> allFilterDoctor;
    private final LiveData<List<ScheduleModel>> allSchedule;




    public DoctorRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allFilterDoctor = mDao.getAllFilterDoctor();
        allSchedule = mDao.getAllSchedule();

    }

    public void insertSchedule(List<ScheduleModel> list) {
        new DoctorRepository.InsertScheduleAsyncTask().execute(list);
    }
    public void deleteAllSchedule( )
    {
        new DoctorRepository.DeleteAllScheduleAsyncTask().execute();
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
        new DoctorRepository.InsertFilterDoctorAsyncTask().execute(list);
    }

    public void DeleteAllFilterDoctor( )
    {
        new DoctorRepository.DeleteAllFilterDoctorAsyncTask().execute();
    }

    public LiveData<List<FilteredDoctoredModel>> getAllFilterDoctor()
    {
        return allFilterDoctor;
    }



    public class InsertFilterDoctorAsyncTask extends AsyncTask<List<FilteredDoctoredModel>,Void,Void>
    {


        @Override
        protected Void doInBackground(List<FilteredDoctoredModel>... lists) {
            mDao.insertDoctor(lists[0]);
            return null;
        }
    }
    public class InsertScheduleAsyncTask extends AsyncTask<List<ScheduleModel>,Void,Void>
    {


        @Override
        protected Void doInBackground(List<ScheduleModel>... lists) {
            mDao.insertAllSchedule(lists[0]);
            return null;
        }
    }




    public class DeleteAllFilterDoctorAsyncTask extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAllDoctor();
            return null;
        }
    }


    public class DeleteAllScheduleAsyncTask extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAllSchedule();
            return null;
        }
    }

}
