package com.example.ffccloud.AttendanceActivity;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AttendanceRepository {


    private FFC_DAO mDao;
    Application application;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());
    private final LiveData<List<AttendanceModel>> allAttendance;


    public AttendanceRepository(Application application) {
        this.application = application;
        FfcDatabase database = FfcDatabase.getInstance(application);
        mDao = database.dao();
        allAttendance = mDao.getAllAttendance();

    }

    public LiveData<List<AttendanceModel>> getAllAttendance( )
    {
        return allAttendance;
    }

    public void insertAttendance(AttendanceModel attendanceModel) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertAttendance(attendanceModel);
            }
        });
    }

    public void deleteAttendance(AttendanceModel attendanceModel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAttendance(attendanceModel);
            }
        });
    }
    public void deleteAllAttendance( ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllAttendance();
            }
        });
    }


//    public class InsertAttendanceAsyncTask extends AsyncTask<AttendanceModel,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(AttendanceModel... attendanceModels) {
//            mDao.insertAttendance(attendanceModels[0]);
//            return null;
//        }
//    }
//
//    public class DeleteAttendanceAsyncTask extends AsyncTask<AttendanceModel,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(AttendanceModel... attendanceModels) {
//            mDao.deleteAttendance(attendanceModels[0]);
//            return null;
//        }
//    }
//
//    public class DeleteAllAttendanceAsyncTask extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDao.deleteAllAttendance();
//            return null;
//        }
//    }



}
