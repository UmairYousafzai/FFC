package com.example.myapplication.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.DoctorModel;
import com.example.myapplication.SplashScreen.SplashActivity;
import com.example.myapplication.Target.utils.TargetRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataToDB {

    Application application;
    Context mContext;
    String errorMessage ="";


    public SyncDataToDB(Application application, Context context) {
        this.application = application;
        mContext=context;
    }

    public String saveDoctorsList(int empId)
    {
        TargetRepository targetRepository = new TargetRepository(application);
        String token = SharedPreferenceHelper.getInstance(application.getBaseContext()).getToken();
        Call<List<DoctorModel>> call= ApiClient.getInstance().getApi().GetDoctorsByEmployeeId(token, empId);
        call.enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<DoctorModel>> call, @NotNull Response<List<DoctorModel>> response) {
                List<DoctorModel> doctorModelList =  new ArrayList<>();

                if (response.isSuccessful())
                {
                    doctorModelList = response.body();

                    targetRepository.InsertDoctor(doctorModelList);
                    Toast.makeText(application,"Work Plan Added.\n Number Of Work Plan Added Is : "+doctorModelList.size(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    errorMessage= response.message();
                    new SweetAlertDialog( mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(response.message()+"\nSession Expire Please Login Again")
                            .setConfirmText("Cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    SharedPreferenceHelper.getInstance(mContext).setLogin_State(false);
                                    Intent intent = new Intent(mContext, SplashActivity.class);
                                    mContext.startActivity(intent);

                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DoctorModel>> call, @NotNull Throwable t) {
               errorMessage= t.getMessage();

            }
        });


        return errorMessage;

    }

    public void SyncData(int empId)
    {
//        ProgressDialog progressDialog= new ProgressDialog(application.getBaseContext());
//        progressDialog.setTitle("Loading");
//        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(application.getBaseContext()).getToken();
        UserRepository repository = new UserRepository(application);

        Call<List<ClassificationModel>> callClassification= ApiClient.getInstance().getApi().GetClassification(token);
        callClassification.enqueue(new Callback<List<ClassificationModel>>() {
            @Override
            public void onResponse(Call<List<ClassificationModel>> call, Response<List<ClassificationModel>> response) {
                List<ClassificationModel> classificationModelList= new ArrayList<>();

                if (response.body()!=null)
                {
                    classificationModelList = response.body();
                    repository.InsertClassifications(classificationModelList);
 //                   progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<ClassificationModel>> call, Throwable t) {
                Toast.makeText(application.getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



        Call<List<GradingModel>> callGrade= ApiClient.getInstance().getApi().GetGrading(token);
        callGrade.enqueue(new Callback<List<GradingModel>>() {
            @Override
            public void onResponse(Call<List<GradingModel>> call, Response<List<GradingModel>> response) {
                List<GradingModel> gradingModelList= new ArrayList<>();

                if (response.body()!=null)
                {
                    gradingModelList = response.body();
                    repository.InsertGrades(gradingModelList);
//                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<GradingModel>> call, Throwable t) {
                Toast.makeText(application.getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        Call<List<QualificationModel>> callQualification= ApiClient.getInstance().getApi().GetQualification(token);
        callQualification.enqueue(new Callback<List<QualificationModel>>() {
            @Override
            public void onResponse(Call<List<QualificationModel>> call, Response<List<QualificationModel>> response) {
                List<QualificationModel> qualificationModelList= new ArrayList<>();

                if (response.body()!=null)
                {
                    qualificationModelList = response.body();
                    repository.InsertQualifications(qualificationModelList);
//                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<QualificationModel>> call, Throwable t) {
                Toast.makeText(application.getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
