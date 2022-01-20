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


    private static SyncDataToDB syncDataToDB;



    public static synchronized SyncDataToDB getInstance()
    {
        if (syncDataToDB==null)
        {
            syncDataToDB = new SyncDataToDB();
        }
        return syncDataToDB;
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
                    CustomsDialog.getInstance().loginAgain(activity,context);
                    progressDialog.dismiss();

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
                                      progressDialog.dismiss();
                } else {
                    CustomsDialog.getInstance().loginAgain(activity,context);
                    progressDialog.dismiss();
                }

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
                    progressDialog.dismiss();
                } else {
                    CustomsDialog.getInstance().loginAgain(activity,context);
                    progressDialog.dismiss();
                }

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
                    progressDialog.dismiss();
                } else {
                    CustomsDialog.getInstance().loginAgain(activity,context);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<QualificationModel>> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        Call<List<DeliveryModeModel>> call = ApiClient.getInstance().getApi().getDeliveryModeTypes(token, 1, 1);

        call.enqueue(new Callback<List<DeliveryModeModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Response<List<DeliveryModeModel>> response) {
                if (response.isSuccessful()) {
                    List<DeliveryModeModel> list = response.body();
                    repository.InsertDeliveryModes(list);
                    progressDialog.dismiss();
                } else {
                    CustomsDialog.getInstance().loginAgain(activity,context);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DeliveryModeModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
