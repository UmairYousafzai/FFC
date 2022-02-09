package com.example.ffccloud.worker.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.model.AddNewWorkPlanModel;
import com.example.ffccloud.model.UpdateWorkPlanStatus;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UploadDataRepository {
    private FFC_DAO mDao;
    Context mContext;
    private final List<UpdateWorkPlanStatus> allWorkPlanStatus;
    private final List<AddNewWorkPlanModel> allWorkPlan;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());



    public UploadDataRepository( Context context) {
        FfcDatabase database = FfcDatabase.getInstance(context.getApplicationContext());
        mDao = database.dao();
        this.mContext = context;
        allWorkPlanStatus = mDao.getAllWorkPlanStatus();
        allWorkPlan = mDao.getAllWorkPlan();

    }


    public void insertWorkPlanStatus(UpdateWorkPlanStatus updateWorkPlanStatus) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertWorkPlanStatus(updateWorkPlanStatus);
            }
        });    }


    public void DeleteAllWorkPlanStatus( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllWorkPlanStatus();
            }
        });
    }


    public List<UpdateWorkPlanStatus> getAllWorkPlanStatus()
    {
        return allWorkPlanStatus;
    }



    public void insertWorkPlan(AddNewWorkPlanModel workPlanModel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertWorkPlan(workPlanModel);
            }
        });    }


    public void DeleteAllWorkPlan( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllWorkPlan();
            }
        });
    }


    public List<AddNewWorkPlanModel> getAllWorkPlan()
    {
        return allWorkPlan;
    }

    public DoctorModel getWorkPlanById(int id)
    {
        return mDao.getWorkPlanByID(id);

    }
    public boolean isWorkPlanExists()
    {
        return mDao.isWorkPlanExists();

    }
    public boolean isWorkPlanStatusExists()
    {
        return mDao.isWorkPlanStatusExists();

    }

    public void deleteWorkPlan(DoctorModel doctorModel)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.DeleteWorkPlanDoctor(doctorModel);
            }
        });
    }

}
