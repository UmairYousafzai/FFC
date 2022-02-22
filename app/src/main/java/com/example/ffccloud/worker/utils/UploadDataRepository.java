package com.example.ffccloud.worker.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

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
    private List<UpdateWorkPlanStatus> allWorkPlanStatus;
    private List<AddNewWorkPlanModel> allWorkPlan;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());
    private CallBackListener callBackListener;



    public UploadDataRepository( Context context) {
        FfcDatabase database = FfcDatabase.getInstance(context.getApplicationContext());
        mDao = database.dao();
        this.mContext = context;

    }

    public void setCallBackListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
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


    public void  getAllWorkPlanStatus()
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                allWorkPlanStatus = mDao.getAllWorkPlanStatus();
                if (callBackListener!=null)
                {
                    callBackListener.onDataReceived(allWorkPlanStatus,1);

                }

            }
        });

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


    public void getAllWorkPlan()
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                allWorkPlan = mDao.getAllWorkPlan();
                if (callBackListener!=null)
                {
                    callBackListener.onDataReceived(allWorkPlan,2);

                }
            }
        });
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

    public interface CallBackListener{

         void onDataReceived(Object object,int key);

    }

}
