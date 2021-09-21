package com.example.myapplication.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Query;

import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.Login.GetUserMenuModel;
import com.example.myapplication.Login.GetUserSettingModel;
import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;
import com.example.myapplication.RoueActivity.RouteActivityModel;
import com.example.myapplication.DoctorModel;
import com.example.myapplication.ScheduleModel;

import java.util.List;

@Dao
public interface FfcDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GetUserInfoModel u);

    @Query("Select Is_Device_Confg from User_Info")
    Boolean device_config();


    @Query("Select Device_Address from User_Info")
    String device_address();
    @Query("Select *from User_Info")
    GetUserInfoModel getuserInfo();

    @Update
    void update(GetUserInfoModel u);

    @Query("Delete from User_Info")
    void delete_previous_user();

    @Query("Select *from User_Info")
    GetUserInfoModel getLoginUser();



    /*
    //
    // ********User Menue Queries********
    //
     */



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertMenu(GetUserMenuModel userMenuModel);

    @Query("Select menuState from User_Menu")
    List<String> get_menu_State();

    @Query("Delete from User_Menu")
    void delete_all_menu();

    @Query("Select *from User_Menu Order By Menu_Order Asc")
    boolean sortMenus();

    /*
    //
    // *******User Setting Queries*******
    //
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertSetting(GetUserSettingModel userSettingModel);


    @Query("Delete from User_Settings")
    void delete_all_setting();

    @Query("Select Configuration_Name from User_Settings where value = 1")
    List<String> getOnConfigurations();

        /*
    //
    // *******User data Sync Queries*******
    //
     */

    @Insert
    void insertClassification(List<ClassificationModel> list);

    @Insert
    void insertGrades(List<GradingModel> list);

    @Insert
    void insertQualification(List<QualificationModel> list);

    @Query("Delete From Classification")
    void deleteAllClassification();

    @Query("Delete From Grade")
    void deleteAllGrade();

    @Query("Delete From Qualification")
    void deleteAllQualification();

    @Query("Select *from Classification")
    LiveData<List<ClassificationModel>> getAllClassification();

    @Query("Select *from Grade")
    LiveData<List<GradingModel>> getAllGrades();

    @Query("Select *from Qualification")
    LiveData<List<QualificationModel>> getAllQualification();
    /*
    //
    // *******Route Activity Queries*******
    //
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRoute(RouteActivityModel routeActivityModel);

    @Query("Update Route_Activity Set End_date_time= :end_time, End_coordinates = :end_cood Where End_date_time ISNULL AND NOT Main_Activity = 'Start Day'  ")
    void setEnd(String end_time, String end_cood);

    @Query("Update Route_Activity Set End_date_time= :end_time, End_coordinates = :end_cood Where End_date_time ISNULL ")
    void setEndDay(String end_time, String end_cood);

    @Query("Select * from ROUTE_ACTIVITY")
    List<RouteActivityModel> getAll();

      /*
    //
    // *******Target  Queries*******
    //
     */
    @Insert
    void insertWorkPlanDoctor(List<DoctorModel> doctorModel);
    @Update
    void updateDoctor(DoctorModel doctorModel);

    @Query("Select *from Doctor Where shift = 'Evening' ")
    LiveData<List<DoctorModel>> getAllEveningDoctors();

    @Query("Select *from Doctor Where shift = 'Morning' ")
    LiveData<List<DoctorModel>> getAllMorningDoctors();

    @Delete
    void DeleteWorkPlanDoctor(DoctorModel doctorModel);

    @Query("Delete From Doctor")
    void deleteAllWorkPlanDoctor();

    @Query("Select *From Doctor where workDate=:date and status = 'Morning'")
    LiveData<List<DoctorModel>> getMorningDoctorByDate(String date);

    @Query("Select *From Doctor where workDate = :date and status = 'Evening'")
    LiveData<List<DoctorModel>> getEveninggDoctorByDate(String date);

    @Query("Select *From Doctor")
    LiveData<List<DoctorModel>> getAllWorkPlanDocotors();



        /*
    //
    // *******Doctor  Queries*******
    //
     */

    @Insert
    void insertDoctor(List<FilteredDoctoredModel> list);

    @Query("Delete from FilterDoctor")
    void deleteAllDoctor();

    @Query("Select *from FilterDoctor")
    LiveData<List<FilteredDoctoredModel>> getAllFilterDoctor();
    @Query("Select *from FilterDoctor" +
            " Where (:doc_classification = 'All' or classificationTitle = :doc_classification) "+
            "and ( :doc_grade = 'All' or gradeTitle= :doc_grade)")
    LiveData<List<FilteredDoctoredModel>> getQuerydocotor(String doc_classification, String doc_grade);

    @Insert
    void insertSchedule(ScheduleModel model);
    @Insert
    void insertAllSchedule(List<ScheduleModel> list);

    @Query("select *from Schedule")
    LiveData<List<ScheduleModel>> getAllSchedule();

    @Query("Delete from Schedule")
    void deleteAllSchedule();

}