package com.example.ffccloud.Target.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FfcDAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.DoctorModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TargetRepository {

    private FfcDAO mDao;
    Application application;
    private final LiveData<List<DoctorModel>> allMorningDoctors;
    private final LiveData<List<DoctorModel>> allEveningDoctors;
    private  LiveData<List<DoctorModel>> allEveningDoctorsByDate;
    private  LiveData<List<DoctorModel>> allMorningDoctorsByDate;
    private  LiveData<List<DoctorModel>> allDoctors;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());

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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertWorkPlanDoctor(list);
            }
        });
    }

    public void DeleteDoctor(DoctorModel doctorModel)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.DeleteWorkPlanDoctor(doctorModel);
            }
        });
    }

    public void DeleteAllDoctor( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllWorkPlanDoctor();
            }
        });      }
    public void UpdateDoctor(DoctorModel doctorModel)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.updateDoctor(doctorModel);
            }
        });
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


//
//    public class InsertDoctorAsyncTask extends AsyncTask<List<DoctorModel>,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(List<DoctorModel>... lists) {
//            mDao.insertWorkPlanDoctor(lists[0]);
//            return null;
//        }
//    }
//    public class DeleteDocotrAsycTask extends AsyncTask<DoctorModel,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(DoctorModel... lists) {
//            mDao.DeleteWorkPlanDoctor(lists[0]);
//            return null;
//        }
//    }
//    public class UpdateDocotrAsycTask extends AsyncTask<DoctorModel,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(DoctorModel... lists) {
//            mDao.updateDoctor(lists[0]);
//            return null;
//        }
//    }
//
//    public class DeleteAllDocotrAsycTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllWorkPlanDoctor();
//            return null;
//        }
//    }
}
