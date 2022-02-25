package com.example.ffccloud.Target.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.DoctorModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TargetRepository {

    private final FFC_DAO mDao;
    Context mContext;
    private final LiveData<List<DoctorModel>> allMorningDoctors;
    private final LiveData<List<DoctorModel>> allEveningDoctors;
    private final LiveData<List<DoctorModel>> allDoctors;
    private final Executor executor= Executors.newSingleThreadExecutor();
    private final Handler handler= new Handler(Looper.getMainLooper());

    public TargetRepository(Context context) {
        FfcDatabase database = FfcDatabase.getInstance(context);
        mDao = database.dao();
        this.mContext = context;
        allMorningDoctors = mDao.getAllMorningDoctors();
        allEveningDoctors = mDao.getAllEveningDoctors();
        allDoctors = mDao.getAllWorkPlanDoctors();

    }

    public LiveData<List<DoctorModel>> getAllEveningDoctorsByDate(String date) {
        return mDao.getEveningDoctorByDate(date);
    }

    public LiveData<List<DoctorModel>> getAllMorningDoctorsByDate(String date) {
        return mDao.getMorningDoctorByDate(date);
    }
    public LiveData<List<DoctorModel>> getAllFullDayDoctorsByDate(String date) {
        return mDao.getFullDoctorByDate(date);
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
