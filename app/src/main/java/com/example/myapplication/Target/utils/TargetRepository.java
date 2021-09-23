package com.example.myapplication.Target.utils;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapplication.Database.FfcDAO;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.DoctorModel;

import java.util.List;

public class TargetRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<DoctorModel>> allMorningDoctors;
    private final LiveData<List<DoctorModel>> allEveningDoctors;
    private  LiveData<List<DoctorModel>> allEveningDoctorsByDate;
    private  LiveData<List<DoctorModel>> allMorningDoctorsByDate;
    private  LiveData<List<DoctorModel>> allDoctors;


    public TargetRepository(Application application) {
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allMorningDoctors = mDao.getAllMorningDoctors();
        allEveningDoctors = mDao.getAllEveningDoctors();
        allDoctors = mDao.getAllWorkPlanDocotors();

    }

    public LiveData<List<DoctorModel>> getAllEveningDoctorsByDate(String date) {
        allEveningDoctorsByDate= mDao.getEveninggDoctorByDate(date);
        return allEveningDoctorsByDate;
    }

    public LiveData<List<DoctorModel>> getAllMorningDoctorsByDate(String date) {
        allMorningDoctorsByDate = mDao.getMorningDoctorByDate(date);
        return allMorningDoctorsByDate;
    }

    public void InsertDoctor(List<DoctorModel> list) {
        new InsertDoctorAsyncTask().execute(list);
    }

    public void DeleteDoctor(DoctorModel doctorModel)
    {
        new DeleteDocotrAsycTask().execute(doctorModel);
    }

    public void DeleteAllDoctor( )
    {
        new DeleteAllDocotrAsycTask().execute();
    }
    public void UpdateDoctor(DoctorModel doctorModel)
    {
        new UpdateDocotrAsycTask().execute(doctorModel);
    }

    public LiveData<List<DoctorModel>> getAllMorningDoctors()
    {
        return allMorningDoctors;
    }

    public LiveData<List<DoctorModel>> getAllEveningDoctors()
    {
        return allEveningDoctors;
    }
    public LiveData<List<DoctorModel>> getAllDoctors()
    {
        return allDoctors;
    }



    public class InsertDoctorAsyncTask extends AsyncTask<List<DoctorModel>,Void,Void>
    {


        @Override
        protected Void doInBackground(List<DoctorModel>... lists) {
            mDao.insertWorkPlanDoctor(lists[0]);
            return null;
        }
    }
    public class DeleteDocotrAsycTask extends AsyncTask<DoctorModel,Void,Void>
    {


        @Override
        protected Void doInBackground(DoctorModel... lists) {
            mDao.DeleteWorkPlanDoctor(lists[0]);
            return null;
        }
    }
    public class UpdateDocotrAsycTask extends AsyncTask<DoctorModel,Void,Void>
    {


        @Override
        protected Void doInBackground(DoctorModel... lists) {
            mDao.updateDoctor(lists[0]);
            return null;
        }
    }

    public class DeleteAllDocotrAsycTask extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAllWorkPlanDoctor();
            return null;
        }
    }
}