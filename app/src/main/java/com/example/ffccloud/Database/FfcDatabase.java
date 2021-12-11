package com.example.ffccloud.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ffccloud.AttendanceActivity.AttendanceModel;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.Login.GetUserMenuModel;
import com.example.ffccloud.Login.GetUserSettingModel;
import com.example.ffccloud.ModelClasses.Activity;
import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.FilteredDoctoredModel;
import com.example.ffccloud.ModelClasses.DeliveryModeModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.ModelClasses.QualificationModel;
import com.example.ffccloud.RoueActivity.RouteActivityModel;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.ScheduleModel;

@Database(entities = {GetUserInfoModel.class, GetUserMenuModel.class, GetUserSettingModel.class ,
        RouteActivityModel.class, DoctorModel.class, ClassificationModel.class, GradingModel.class, QualificationModel.class,
        FilteredDoctoredModel.class, ScheduleModel.class, Activity.class, AttendanceModel.class, LocationRequestedUser.class,
        DeliveryModeModel.class}, version = 1, exportSchema = false)
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