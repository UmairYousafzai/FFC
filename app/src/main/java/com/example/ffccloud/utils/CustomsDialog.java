package com.example.ffccloud.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.R;
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

    public void showDialog(String message,String title ,Activity activity,Context context,int key) {
        this.activity = activity;



        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( context).setView(dialogBinding.getRoot()).setCancelable(true).create();
        dialogBinding.title.setText(title);
        dialogBinding.body.setText(message);
        if (key==1)
        {
            Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_success_26,null);
            dialogBinding.icon.setImageDrawable(drawable);
        }
        else if (key==2)
        {
            Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_warning_24,null);
            dialogBinding.icon.setImageDrawable(drawable);
        }
        alertDialog.show();

        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    public void loginAgain(Activity activity,Context context) {
        FfcDatabase ffcDatabase;
        ffcDatabase = FfcDatabase.getInstance(activity.getBaseContext());


        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Error");
        dialogBinding.body.setText("Session Expire Please Login Again");
        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ffcDatabase.dao().deleteAllDeliveryModes();

                SharedPreferenceHelper.getInstance(context).setLogin_State(false);
                Intent intent = new Intent(context, SplashActivity.class);
                activity.startActivity(intent);
            }
        });
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }


    public void showOpenLocationSettingDialog(Activity activity,Context context) {



        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(activity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dialogBinding.getRoot()).create();
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
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
