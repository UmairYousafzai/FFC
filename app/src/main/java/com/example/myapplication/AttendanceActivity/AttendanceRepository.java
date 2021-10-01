package com.example.myapplication.AttendanceActivity;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapplication.Database.FfcDAO;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.utils.ActivityRepository;

import java.util.List;

public class AttendanceRepository {


    private FfcDAO mDao;
    Application application;
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
        new InsertAttendanceAsyncTask().execute(attendanceModel);
    }

    public void deleteAttendance(AttendanceModel attendanceModel) {
        new DeleteAttendanceAsyncTask().execute(attendanceModel);
    }
    public void deleteAllAttendance( ) {
        new DeleteAllAttendanceAsyncTask().execute();
    }


    public class InsertAttendanceAsyncTask extends AsyncTask<AttendanceModel,Void,Void>
    {


        @Override
        protected Void doInBackground(AttendanceModel... attendanceModels) {
            mDao.insertAttendance(attendanceModels[0]);
            return null;
        }
    }

    public class DeleteAttendanceAsyncTask extends AsyncTask<AttendanceModel,Void,Void>
    {


        @Override
        protected Void doInBackground(AttendanceModel... attendanceModels) {
            mDao.deleteAttendance(attendanceModels[0]);
            return null;
        }
    }

    public class DeleteAllAttendanceAsyncTask extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAllAttendance();
            return null;
        }
    }
}
