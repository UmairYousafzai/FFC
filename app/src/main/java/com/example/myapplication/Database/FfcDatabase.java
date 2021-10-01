package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.AttendanceActivity.AttendanceModel;
import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.Login.GetUserMenuModel;
import com.example.myapplication.Login.GetUserSettingModel;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;
import com.example.myapplication.RoueActivity.RouteActivityModel;
import com.example.myapplication.DoctorModel;
import com.example.myapplication.ScheduleModel;

@Database(entities = {GetUserInfoModel.class, GetUserMenuModel.class, GetUserSettingModel.class ,
        RouteActivityModel.class, DoctorModel.class, ClassificationModel.class, GradingModel.class, QualificationModel.class,
        FilteredDoctoredModel.class, ScheduleModel.class, Activity.class, AttendanceModel.class}, version = 1, exportSchema = false)
public abstract class FfcDatabase extends RoomDatabase {

    private static  FfcDatabase database;
    private static String DatabaseName = "FFCdatabase";

    public synchronized  static  FfcDatabase getInstance(Context context){
        if (database == null) {

            database = Room.databaseBuilder(context.getApplicationContext(), FfcDatabase.class, DatabaseName)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract FfcDAO dao();

}