package com.example.ffccloud.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.SplashScreen.SplashActivity;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;

public class CustomsDialog {

    private Activity activity;
    private static CustomsDialog customsDialog;


    public static synchronized CustomsDialog getInstance()
    {
        if (customsDialog==null)
        {
            customsDialog= new CustomsDialog();
        }
        return customsDialog;
    }

    public void showDialog(String message,String title ,Activity activity,Context context) {
        this.activity = activity;



        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( context).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText(title);
        dialogBinding.body.setText(message);
        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        dialogBinding.btnCancel.setVisibility(View.GONE);


    }

    public void loginAgain(Activity activity) {
        FfcDatabase ffcDatabase;
        ffcDatabase = FfcDatabase.getInstance(activity.getBaseContext());


        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(activity.getBaseContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Error");
        dialogBinding.body.setText("Session Expire Please Login Again");
        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ffcDatabase.dao().deleteAllDeliveryModes();

                SharedPreferenceHelper.getInstance(activity.getBaseContext()).setLogin_State(false);
                Intent intent = new Intent(activity.getBaseContext(), SplashActivity.class);
                activity.startActivity(intent);
            }
        });


    }


    public void showOpenLocationSettingDialog(Activity activity) {


        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(activity.getApplicationContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Please turn on  location for this action.");
        dialogBinding.body.setText("Do you want to open location setting.");
        alertDialog.show();

        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        });
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
