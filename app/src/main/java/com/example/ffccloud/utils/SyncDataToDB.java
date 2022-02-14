package com.example.ffccloud.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.ffccloud.Customer.utils.CustomerRepository;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.model.ClassificationModel;
import com.example.ffccloud.model.DeliveryModeModel;

import com.example.ffccloud.model.ExpenseType;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.model.QualificationModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.Target.utils.TargetRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataToDB {


    private static SyncDataToDB syncDataToDB;



    public static synchronized SyncDataToDB getInstance()
    {
        if (syncDataToDB==null)
        {
            syncDataToDB = new SyncDataToDB();
        }
        return syncDataToDB;
    }
    public void getCustomerData(Context mContext) {

        CustomerRepository customerRepository= new CustomerRepository(mContext);
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading Customer");
        progressDialog.setCancelable(false);
        progressDialog.show();

        int userID= SharedPreferenceHelper.getInstance(mContext).getUserID();

        Call<List<CustomerModel>> call = ApiClient.getInstance().getApi().getAllCustomer(1,1,"C",userID);

        call.enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<CustomerModel>> call, @NotNull Response<List<CustomerModel>> response) {


                if (response.isSuccessful())
                {

                    if(response.body()!=null)
                    {
                        if (response.body().size() != 0) {
                            customerRepository.insertCustomers(response.body());
                        }
                        progressDialog.dismiss();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, ""+response.errorBody(), Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(mContext, ""+response.message(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(@NotNull Call<List<CustomerModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void saveDoctorsList(int empId,Context context,Activity activity) {
        ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        TargetRepository targetRepository = new TargetRepository(context);
        targetRepository.DeleteAllDoctor();
        String token = SharedPreferenceHelper.getInstance(context).getToken();
        Call<List<DoctorModel>> call = ApiClient.getInstance().getApi().GetDoctorsByEmployeeId(token, empId);
        call.enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DoctorModel>> call, @NotNull Response<List<DoctorModel>> response) {
                List<DoctorModel> doctorModelList = new ArrayList<>();

                if (response.isSuccessful()) {
                    doctorModelList = response.body();


                    targetRepository.InsertDoctor(doctorModelList);
                    progressDialog.dismiss();
                    Toast.makeText(context, "Work Plan Added.\n Number Of Work Plan Added Is : " + (doctorModelList != null ? doctorModelList.size() : 0), Toast.LENGTH_LONG).show();
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DoctorModel>> call, @NotNull Throwable t) {

                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });


    }

    public void SyncData(int empId,Context context,Activity activity) {
        ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(context).getToken();
        UserRepository repository = new UserRepository(context);

        Call<List<ClassificationModel>> callClassification = ApiClient.getInstance().getApi().GetClassification(token);
        callClassification.enqueue(new Callback<List<ClassificationModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<ClassificationModel>> call, @NotNull Response<List<ClassificationModel>> response) {
                List<ClassificationModel> classificationModelList = new ArrayList<>();

                if (response.isSuccessful() ) {
                    classificationModelList = response.body();
                    repository.InsertClassifications(classificationModelList);
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<List<ClassificationModel>> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        Call<List<GradingModel>> callGrade = ApiClient.getInstance().getApi().GetGrading(token);
        callGrade.enqueue(new Callback<List<GradingModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GradingModel>> call, @NotNull Response<List<GradingModel>> response) {
                List<GradingModel> gradingModelList = new ArrayList<>();

                if (response.isSuccessful()) {
                    gradingModelList = response.body();
                    repository.InsertGrades(gradingModelList);
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<List<GradingModel>> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        Call<List<QualificationModel>> callQualification = ApiClient.getInstance().getApi().GetQualification(token);
        callQualification.enqueue(new Callback<List<QualificationModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<QualificationModel>> call, @NotNull Response<List<QualificationModel>> response) {
                List<QualificationModel> qualificationModelList = new ArrayList<>();

                if (response.isSuccessful()) {
                    qualificationModelList = response.body();
                    repository.InsertQualifications(qualificationModelList);
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<List<QualificationModel>> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        Call<List<DeliveryModeModel>> callDeliveryModes = ApiClient.getInstance().getApi().getDeliveryModeTypes(token, 1, 1);

        callDeliveryModes.enqueue(new Callback<List<DeliveryModeModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Response<List<DeliveryModeModel>> response) {
                if (response.isSuccessful()) {
                    List<DeliveryModeModel> list = response.body();
                    repository.InsertDeliveryModes(list);
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<ExpenseType>> call = ApiClient.getInstance().getApi().getExpenseType(token, 1, 1, 1);

        call.enqueue(new Callback<List<ExpenseType>>() {
            @Override
            public void onResponse(@NotNull Call<List<ExpenseType>> call, @NotNull Response<List<ExpenseType>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        repository.InsertExpenseType(response.body());
                    } else {
                        Toast.makeText(context, "Expense types not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.message().equals("UnAuthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(activity,context);
                    }
                    if (response.errorBody() != null) {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<ExpenseType>> call, @NotNull Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
