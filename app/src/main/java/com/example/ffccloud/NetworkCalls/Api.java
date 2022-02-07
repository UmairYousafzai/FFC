package com.example.ffccloud.NetworkCalls;

import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.GetProductModel;
import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.Login.GetUserMenuModel;
import com.example.ffccloud.Login.ForgotPasswordModel;
import com.example.ffccloud.Login.GetUserSettingModel;
import com.example.ffccloud.Login.TokenResponseModel;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.model.AddNewWorkPlanModel;
import com.example.ffccloud.model.AreaModel;
import com.example.ffccloud.model.AreasByEmpIdModel;
import com.example.ffccloud.model.ClassificationModel;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.model.DeliveryModeModel;
import com.example.ffccloud.model.DoctorsByAreaIdsModel;
import com.example.ffccloud.FilteredDoctoredModel;
import com.example.ffccloud.FilteredDoctorInfomationModel;
import com.example.ffccloud.model.ExpenseType;
import com.example.ffccloud.model.GetCustomerResponse;
import com.example.ffccloud.model.GetLedgerBalanceModel;
import com.example.ffccloud.model.GetSaleOrderDetail;
import com.example.ffccloud.model.GetSupplierDetailModel;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.model.InsertSaleOrderModel;
import com.example.ffccloud.model.QualificationModel;
import com.example.ffccloud.model.RateListModel;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.GetSaleOrderModel;
import com.example.ffccloud.model.SupplierModelNew;
import com.example.ffccloud.SaveDoctorModel;
import com.example.ffccloud.model.SaveNewWorkPlanModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.model.UpdateWorkPlanStatus;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.WorkPlanHistory;

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

    @GET("api/FFCAppApi/GetDoctorByEmployee")
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

    @GET("api/AppUser/FFCGetTeamUsers/{Emp_ID}")
    Call<List<UserModel>> getUsersForTracking(@Header("Authorization") String token, @Path("Emp_ID") int Emp_ID);

    @POST("api/SupplierApi/InsertUpdate")
    Call<UpdateStatus>  insertCustomer(@Body CustomerModel customerModel);

    @GET("api/SupplierApi/GetAllbyUser")
    Call<List<CustomerModel>>  getAllCustomer(@Query("Company_Id") int companyID,
                                              @Query("Country_Id") int Country_Id,
                                              @Query("UserType") String userType,
                                              @Query("UserId") int UserId);

    @GET("api/SalesOrder/Get_SalesOrderInvoice_List")
    Call<List<GetSaleOrderModel>>  getSalesOrder(@Query("Company_Id") int companyID,
                                                 @Query("Location_Id") int Location_Id,
                                                 @Query("Country_Id") int Country_Id,
                                                 @Query("Project_Id") int Project_Id,
                                                 @Query("From_Date") String From_Date,
                                                 @Query("To_Date") String To_Date,
                                                 @Query("Supplier_Id") int Supplier_Id,
                                                 @Query("Status") int Status,
                                                 @Query("DateStatus") int DateStatus,
                                                 @Query("PriorityStatus") int PriorityStatus);



    @GET("api/SharedFunction/ItSearch")
    Call<List<GetProductModel>> getAllProducts(@Header("Authorization") String token,
                                               @Query("It") String it,
                                               @Query("Company_Id") int Company_Id,
                                               @Query("Country_Id") int Country_Id,
                                               @Query("IsStatus") int IsStatus,
                                               @Query("LocationId") int LocationId );
    @POST("api/SalesOrder/InsertUpdate")
    Call<UpdateStatus>  insertSaleOrder(@Body InsertSaleOrderModel saleOrderModel);

    @POST("api/SupplierApi/InsertUpdateSupplier")
    Call<UpdateStatus>  insertSupplier(@Body SupplierModelNew supplierModelNew);

    @GET("api/SupplierApi/GetFFCSupplierByUser")
    Call<List<GetSupplierModel>> getSupplier(@Query("User_Type")String userType, @Query("UserId")int userID,  @Query("Region_Id")int regionID);

    @GET("api/SupplierApi/GetFFCSupplierById")
    Call<GetSupplierDetailModel> getSupplierDetail(@Query("Supplier_id")int supplierID);

    @GET("api/SalesOrder/Get_Delivery_Mode")
    Call<List<DeliveryModeModel>> getDeliveryModeTypes(@Header("Authorization") String token, @Query("Country_Id")int Country_Id, @Query("Company_Id") int Company_Id);

    @GET("api/SalesOrder/Get_LedgerBalance")
    Call<GetLedgerBalanceModel> getLedgerBalance(@Header("Authorization") String token, @Query("Company_Id")int Company_Id,
                                                 @Query("Country_Id") int Country_Id,
                                                 @Query("Location_Id") int Location_Id,
                                                 @Query("Project_Id") int Project_Id,
                                                 @Query("Supplier_Id") int Supplier_Id);

    @GET("api/SalesOrder/Get_RateList")
    Call<List<RateListModel>> getRateList(@Header("Authorization") String token,
                                         @Query("Country_Id") int Country_Id,
                                         @Query("Location_Id") int Location_Id,
                                         @Query("Project_Id") int Project_Id,
                                         @Query("Sale_Order_Date") String Sale_Order_Date,
                                         @Query("Sample_Id") int Sample_Id,
                                         @Query("Supplier_Id") int Supplier_Id,
                                         @Query("Item_id") int Item_id);

    @GET("api/SalesOrder/GetByID")
    Call<GetSaleOrderDetail> getSaleOrderDetail(@Query("Company_Id") int Company_Id,
                                                      @Query("Location_Id") int Location_Id,
                                                      @Query("Country_Id") int Country_Id,
                                                      @Query("Project_Id") int Project_Id,
                                                      @Query("Sale_Order_Id") int Sale_Order_Id);

    @POST("api/FFCAppApi/GetALLExpenses")
    Call<List<ExpenseType>> getExpenseType(@Header("Authorization") String token,
                                           @Query("Company_ID") int companyId,
                                           @Query("Country_ID") int countryId,
                                           @Query("Loction_ID") int locationId);

    @POST("api/FFCAppApi/SaveExpensesList")
    Call<UpdateStatus> insertExpenses(@Header("Authorization") String token, @Body List<ExpenseModelClass> sendExpenseModels);

    @GET("api/SupplierApi/GetByID")
    Call<GetCustomerResponse>  getCustomerByCode(@Query("Company_Id") int companyID,
                                                 @Query("Country_Id") int Country_Id,
                                                 @Query("Supplier_Code") String  customerCode);
}

