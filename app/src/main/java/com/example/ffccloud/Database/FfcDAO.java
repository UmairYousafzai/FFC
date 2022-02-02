package com.example.ffccloud.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Query;

import com.example.ffccloud.AttendanceActivity.AttendanceModel;
import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.Login.GetUserMenuModel;
import com.example.ffccloud.Login.GetUserSettingModel;
import com.example.ffccloud.model.Activity;
import com.example.ffccloud.model.AddNewWorkPlanModel;
import com.example.ffccloud.model.ClassificationModel;
import com.example.ffccloud.FilteredDoctoredModel;
import com.example.ffccloud.model.DeliveryModeModel;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.model.Notification;
import com.example.ffccloud.model.QualificationModel;
import com.example.ffccloud.model.UpdateWorkPlanStatus;
import com.example.ffccloud.RoueActivity.RouteActivityModel;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.ScheduleModel;

import java.util.List;

@Dao
public interface FfcDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUserInfo(GetUserInfoModel u);

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
    @Insert
    void insertDeliveryModes(List<DeliveryModeModel> list);

    @Query("Delete From Classification")
    void deleteAllClassification();

    @Query("Delete From Grade")
    void deleteAllGrade();

    @Query("Delete From Qualification")
    void deleteAllQualification();

    @Query("Delete From delivery_Mode")
    void deleteAllDeliveryModes();

    @Query("Select *from Classification")
    LiveData<List<ClassificationModel>> getAllClassification();

    @Query("Select *from Grade")
    LiveData<List<GradingModel>> getAllGrades();

    @Query("Select *from Qualification")
    LiveData<List<QualificationModel>> getAllQualification();

    @Query("Select *from delivery_Mode")
    LiveData<List<DeliveryModeModel>> getAllDeliveryModes();

    @Insert
    void insertUser(LocationRequestedUser user);

    @Delete
    void deleteUser(LocationRequestedUser user);

    @Query("Delete from LocationRequestedUser")
    void deleteAllUser();

    @Query("Select *From LocationRequestedUser")
    LiveData<List<LocationRequestedUser>> getAllUser();

    @Query("Select Exists (Select *from LocationRequestedUser where email= :email)")
    boolean isUserExists(String email);
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

    @Query("Select *from Doctor Where shift = 'Evening' order by dotorId_pk desc ")
    LiveData<List<DoctorModel>> getAllEveningDoctors();

    @Query("Select *from Doctor Where shift = 'Morning' order by dotorId_pk desc ")
    LiveData<List<DoctorModel>> getAllMorningDoctors();

    @Delete
    void DeleteWorkPlanDoctor(DoctorModel doctorModel);

    @Query("Delete From Doctor")
    void deleteAllWorkPlanDoctor();

    @Query("Select *From Doctor where (fromDate=:date) order by dotorId_pk desc")
    LiveData<List<DoctorModel>> getMorningDoctorByDate(String date);

//     +"and (shift = 'Morning')"
    @Query("Select *From Doctor where (fromDate = :date) order by dotorId_pk desc")
    LiveData<List<DoctorModel>> getEveningDoctorByDate(String date);

    // + " and (shift = 'Evening')"

    @Query("Select *From Doctor order by dotorId_pk desc")
    LiveData<List<DoctorModel>> getAllWorkPlanDoctors();



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
            " Where (:doc_classification = 'All' or classificationTitle = :doc_classification) " +
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


            /*
    //
    // *******Activity  Queries*******
    //
     */


    @Insert
    void insertActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);

    @Update
    void updateActivity(Activity activity);

    @Query("Select *From activity")
    LiveData<List<Activity>> getAllActivities();

    @Query("Select *From activity where Sub_Activity Not in ( 'Private Travel','Start Day','Local Travel','Target','Office')" +
            "and Main_Activity Not in ('Private Travel','Start Day','Local Travel','Office')")
    LiveData<List<Activity>> getTaskActivities();

    @Query("Delete from activity")
    void deleteAllActivity();

    @Query("Select *From activity Where End_DateTime is null and End_Cord is null ")
    LiveData<List<Activity>> getQueryActivity(  );
    @Query("Select *From activity Where End_DateTime is null and End_Cord is null " +
            "and Sub_Activity != 'Complete' ")
    LiveData<List<Activity>> getActivityWithoutTask(  );


                /*
    //
    // *******Attendance  Queries*******
    //
     */


    @Insert
    void insertAttendance(AttendanceModel attendanceModel);

    @Delete
    void deleteAttendance(AttendanceModel attendanceModel);

    @Query("Delete from activity")
    void deleteAllAttendance();

    @Query("Select *From Attendance")
    LiveData<List<AttendanceModel>> getAllAttendance();

                /*
    //
    // *******UploadData  Queries*******
    //
     */


    @Insert
    void insertWorkPlanStatus(UpdateWorkPlanStatus updateWorkPlanStatus);

    @Delete
    void deleteWorkPlanStatus(UpdateWorkPlanStatus updateWorkPlanStatus);

    @Query("Delete from workPlanStatus")
    void deleteAllWorkPlanStatus();

    @Query("Select *From workPlanStatus")
    List<UpdateWorkPlanStatus> getAllWorkPlanStatus();


    @Insert
    void insertWorkPlan(AddNewWorkPlanModel workPlanModel);

    @Delete
    void deleteWorkPlan(AddNewWorkPlanModel workPlanModel);

    @Query("Delete from workPlan")
    void deleteAllWorkPlan();

    @Query("Select *From workPlan")
    List<AddNewWorkPlanModel> getAllWorkPlan();



    @Query("Select *From Doctor where doctorId= :workPlanID")
    DoctorModel getWorkPlanByID(int workPlanID);

    @Query("Select Exists (Select *from workPlan)")
    boolean isWorkPlanExists( );

    @Query("Select Exists (Select *from workPlanStatus)")
    boolean isWorkPlanStatusExists( );


                  /*
    //
    // *******Expense  Queries*******
    //
     */

    @Insert
    void insertExpense(ExpenseModelClass expenseModelClass);

    @Query("Delete from ExpenseModelClass")
    void deleteAllExpense();
    @Delete
    void deleteExpense(ExpenseModelClass expenseModelClass);

    @Update
    void updateExpense(ExpenseModelClass expenseModelClass);

    @Query("Select *From ExpenseModelClass order by expenseID_pk desc")
    LiveData<List<ExpenseModelClass>> getAllExpenses();





                  /*
    //
    // *******Notification  Queries*******
    //
     */


    @Insert
    void insertNotification(Notification notification);

    @Query("delete from Notification")
    void deleteAllNotifications();

    @Delete
    void deleteNotification(Notification notification);

    @Query("select *from notification order by id desc")
    LiveData<List<Notification>> getAllNotifications();

    @Query("select count(id) from notification ")
    LiveData<Integer> getNotificationCount();

    @Query("Select *from Notification where senderId= :senderID and notificationTitle=:message ")
    Notification getNotification(String senderID,String message);
}