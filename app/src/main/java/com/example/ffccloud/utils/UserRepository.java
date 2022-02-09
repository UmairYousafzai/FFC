package com.example.ffccloud.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;


import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.model.ClassificationModel;
import com.example.ffccloud.model.DeliveryModeModel;
import com.example.ffccloud.model.ExpenseType;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.model.QualificationModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private final FFC_DAO mDao;
    private final LiveData<List<QualificationModel>> allQualification;
    private final LiveData<List<ClassificationModel>> allClassification;
    private final LiveData<List<GradingModel>> allGrades;
    private final LiveData<List<LocationRequestedUser>> allUsers;
    private final LiveData<List<DeliveryModeModel>> allDeliveryModes;
    private final LiveData<List<ExpenseType>> allExpenseTypes;

    private final Executor executor= Executors.newSingleThreadExecutor();
    private final Handler handler= new Handler(Looper.getMainLooper());


    public UserRepository(Context context) {

        FfcDatabase database = FfcDatabase.getInstance(context);
        mDao = database.dao();
        allClassification = mDao.getAllClassification();
        allGrades = mDao.getAllGrades();
        allQualification = mDao.getAllQualification();
        allUsers = mDao.getAllUser();
        allDeliveryModes = mDao.getAllDeliveryModes();
        allExpenseTypes= mDao.getAllExpenseType();

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

    public void InsertDeliveryModes(List<DeliveryModeModel> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertDeliveryModes(list);
            }
        });    }

    public void InsertExpenseType(List<ExpenseType> list) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertExpenseType(list);
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

    public void DeleteAllDeliveryModes( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllDeliveryModes();
            }
        });    }

    public void DeleteAllExpenseType( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllExpenseType();
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

    public LiveData<List<DeliveryModeModel>> getAllDeliveryModes()
    {
        return allDeliveryModes;
    }

    public LiveData<List<ExpenseType>> getAllExpenseTypes()
    {
        return allExpenseTypes;
    }

    public void insertUser(LocationRequestedUser user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertUser(user);
            }
        });      }


    public void deleteAllUser( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllUser();
            }
        });
    }

    public void deleteAllMenus( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.delete_all_menu();
            }
        });
    }
    public void deleteUser( LocationRequestedUser user)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteUser(user);
            }
        });
    }

    public boolean isUserExists(String email)
    {
        return mDao.isUserExists(email);
    }
    public LiveData<List<LocationRequestedUser>> getAllUser()
    {
        return allUsers;
    }

}
