package com.example.ffccloud.utils;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.ModelClasses.DeliveryModeModel;

import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.ModelClasses.QualificationModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.SplashScreen.SplashActivity;
import com.example.ffccloud.Target.utils.TargetRepository;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataToDB {

    private Application application;
    private Context mContext;
    private  Activity activity;

    public SyncDataToDB(Application application, Context context, Activity activity) {
        this.application = application;
        mContext = context;
        this.activity = activity;
    }

    public void saveDoctorsList(int empId) {
        TargetRepository targetRepository = new TargetRepository(application);
        targetRepository.DeleteAllDoctor();
        String token = SharedPreferenceHelper.getInstance(application.getBaseContext()).getToken();
        Call<List<DoctorModel>> call = ApiClient.getInstance().getApi().GetDoctorsByEmployeeId(token, empId);
        call.enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DoctorModel>> call, @NotNull Response<List<DoctorModel>> response) {
                List<DoctorModel> doctorModelList = new ArrayList<>();

                if (response.isSuccessful()) {
                    doctorModelList = response.body();


                    targetRepository.InsertDoctor(doctorModelList);
                    Toast.makeText(application, "Work Plan Added.\n Number Of Work Plan Added Is : " + (doctorModelList != null ? doctorModelList.size() : 0), Toast.LENGTH_LONG).show();
                } else {
                    loginAgain(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DoctorModel>> call, @NotNull Throwable t) {


            }
        });


    }

    public void SyncData(int empId) {
//        ProgressDialog progressDialog= new ProgressDialog(application.getBaseContext());
//        progressDialog.setTitle("Loading");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(application.getBaseContext()).getToken();
        UserRepository repository = new UserRepository(application);

        Call<List<ClassificationModel>> callClassification = ApiClient.getInstance().getApi().GetClassification(token);
        callClassification.enqueue(new Callback<List<ClassificationModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<ClassificationModel>> call, @NotNull Response<List<ClassificationModel>> response) {
                List<ClassificationModel> classificationModelList = new ArrayList<>();

                if (response.body() != null) {
                    classificationModelList = response.body();
                    repository.InsertClassifications(classificationModelList);
                    //                   progressDialog.dismiss();
                } else {
                    loginAgain(response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<ClassificationModel>> call, @NotNull Throwable t) {
                Toast.makeText(application.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call<List<GradingModel>> callGrade = ApiClient.getInstance().getApi().GetGrading(token);
        callGrade.enqueue(new Callback<List<GradingModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GradingModel>> call, @NotNull Response<List<GradingModel>> response) {
                List<GradingModel> gradingModelList = new ArrayList<>();

                if (response.body() != null) {
                    gradingModelList = response.body();
                    repository.InsertGrades(gradingModelList);
//                    progressDialog.dismiss();
                } else {
                    loginAgain(response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<GradingModel>> call, @NotNull Throwable t) {
                Toast.makeText(application.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call<List<QualificationModel>> callQualification = ApiClient.getInstance().getApi().GetQualification(token);
        callQualification.enqueue(new Callback<List<QualificationModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<QualificationModel>> call, @NotNull Response<List<QualificationModel>> response) {
                List<QualificationModel> qualificationModelList = new ArrayList<>();

                if (response.body() != null) {
                    qualificationModelList = response.body();
                    repository.InsertQualifications(qualificationModelList);
//                    progressDialog.dismiss();
                } else {
                    loginAgain(response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<QualificationModel>> call, @NotNull Throwable t) {
                Toast.makeText(application.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<DeliveryModeModel>> call = ApiClient.getInstance().getApi().getDeliveryModeTypes(token, 1, 1);

        call.enqueue(new Callback<List<DeliveryModeModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Response<List<DeliveryModeModel>> response) {
                if (response.body() != null) {
                    List<DeliveryModeModel> list = response.body();
                    repository.InsertDeliveryModes(list);
//                    progressDialog.dismiss();
                } else {
                    loginAgain(response.message());
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Throwable t) {
//                progressDialog.dismiss();
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loginAgain(String message) {
         FfcDatabase ffcDatabase;
        ffcDatabase = FfcDatabase.getInstance(mContext);


        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
       AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Error");
        dialogBinding.body.setText("Session Expire Please Login Again");
        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ffcDatabase.dao().deleteAllDeliveryModes();

                SharedPreferenceHelper.getInstance(mContext).setLogin_State(false);
                Intent intent = new Intent(mContext, SplashActivity.class);
                mContext.startActivity(intent);
            }
        });


    }
}
