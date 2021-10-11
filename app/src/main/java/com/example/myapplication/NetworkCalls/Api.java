package com.example.myapplication.NetworkCalls;

import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.Login.GetUserMenuModel;
import com.example.myapplication.Login.ForgotPasswordModel;
import com.example.myapplication.Login.GetUserSettingModel;
import com.example.myapplication.Login.TokenResponseModel;
import com.example.myapplication.DoctorModel;
import com.example.myapplication.ModelClasses.AddNewWorkPlanModel;
import com.example.myapplication.ModelClasses.AreaModel;
import com.example.myapplication.ModelClasses.AreasByEmpIdModel;
import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.ModelClasses.DoctorsByAreaIdsModel;
import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.FilteredDoctorInfomationModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;
import com.example.myapplication.ModelClasses.RegionModel;
import com.example.myapplication.ModelClasses.SaveDoctorModel;
import com.example.myapplication.ModelClasses.SaveNewWorkPlanModel;
import com.example.myapplication.ModelClasses.UpdateStatus;
import com.example.myapplication.ModelClasses.UpdateWorkPlanStatus;
import com.example.myapplication.WorkPlanHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("token")
    Call<TokenResponseModel> token(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );
    @POST("api/Login/SendCode")
    Call<ForgotPasswordModel> forgot_password(
            @Query("username") String username
    );

    @GET("api/Admin/Update_Password")
    Call<String> new_password(
            @Query("UserEmail") String userEmail,
            @Query("Code") String code,
            @Query("New_password") String new_password
    );

    @GET("api/AppUser/GetUserInfo")
    Call<GetUserInfoModel> get_user(
            @Query("Username") String username,
            @Query("Password") String password
    );

    @GET("api/FFCAppApi/GetMenus")
    Call<List<GetUserMenuModel>> get_user_menu(
            @Header("Authorization") String token,
            @Query("UserId") int id
    );

    @GET("api/FFCAppApi/GetConfig")
    Call<List<GetUserSettingModel>> get_user_setting(
            @Header("Authorization") String token,
            @Query("UserId") int id
    );

    @GET("api/FFCAppApi/GetDoctorByEmployee  ")
    Call<List<DoctorModel>> GetDoctorsByEmployeeId(@Header("Authorization") String token, @Query("Emp_Id") int emp_Id);

    @POST("api/FFCAppApi/UpdateWorkPlanStatus")
    Call<UpdateStatus> UpdateWorkPlanStatus(@Header("Authorization") String token, @Header("Content-Type") String type, @Body List<UpdateWorkPlanStatus> updateWorkPlanStatus);

    @POST("api/FFCAppApi/ReScheduleWorkPlan")
    Call<UpdateStatus> RescheduleWorkPlan(@Header("Authorization") String token, @Body AddNewWorkPlanModel addNewWorkPlanModelList);

    @GET("api/FFCAppApi/GetAreaByEmpId")
    Call<List<AreasByEmpIdModel>> GetAreaByEmpId(@Header("Authorization") String token, @Query("Emp_Id") int id);

    @GET("api/FFCAppApi/GetDoctorsByAreaIds")
    Call<List<DoctorsByAreaIdsModel>> GetDoctorsByAreaIds(@Header("Authorization") String token, @Query("ids") String ids);

    @POST("api/FFCAppApi/SaveNewWorkPlan")
    Call<UpdateStatus> AddNewWorkPlan(@Header("Authorization") String token, @Body SaveNewWorkPlanModel addNewWorkPlanModelList);

    @GET("api/DoctorSetupApi/Get_Classification")
    Call<List<ClassificationModel>> GetClassification(@Header("Authorization") String token);

    @GET("api/DoctorSetupApi/Get_Qualification")
    Call<List<QualificationModel>> GetQualification(@Header("Authorization") String token);

    @GET("api/DoctorSetupApi/Get_Grade")
    Call<List<GradingModel>> GetGrading(@Header("Authorization") String token);

    @GET("api/FFCAppApi/GetDoctorByEmployeeId")
    Call<List<FilteredDoctoredModel>> GetFilteredDoctorsByEmployeeId(@Header("Authorization") String token, @Query("Emp_Id") int emp_Id);


    @GET("api/DoctorController/GetById")
    Call<FilteredDoctorInfomationModel> GetFullFilteredDoctor(@Header("Authorization") String token, @Query("Doctor_Id") int doc_id);


    @POST("api/DoctorController/Save")
    Call<UpdateStatus> SendSaveDoctor(@Header("Authorization") String token, @Header("Content-Type") String type, @Body SaveDoctorModel saveDoctorModel);

    @GET("api/FFCAppApi/GetDDLRegions_D/{CompanyId}/{CountryID}/{Location_Id}/{Emp_Id}")
        //@GET("api/AreaSetupApi/GetDDLRegions_D/{CompanyId}/{CountryID}/{Location_Id}")
    Call<List<RegionModel>> getRegion(@Header("Authorization") String token, @Path("CompanyId") int companyId, @Path("CountryID") int countryId, @Path("Location_Id") int locationId, @Path("Emp_Id") int emp_Id);

    @GET("api/FFCAppApi/GetDDLAreaEmployees/{CompanyId}/{CountryID}/{Location_Id}/{Sub_Head_Code}/{Emp_Id}")
        // @GET("api/AreaSetupApi/GetDDLAreaD/{CompanyId}/{CountryID}/{Location_Id}/{Ac_Id}")
    Call<List<AreaModel>> getArea(@Header("Authorization") String token, @Path("CompanyId") int companyId, @Path("CountryID") int countryId, @Path("Location_Id") int locationId, @Path("Sub_Head_Code") int AreaId, @Path("Emp_Id") int Emp_Id);

    @GET("api/FFCAppApi/WorkPlanVisitHistory")
    Call<List<WorkPlanHistory>> GetWorkPlanHistory(@Header("Authorization") String token, @Query("Doctor_Id") int doctor_Id, @Query("Emp_Id") int emp_Id);
}

