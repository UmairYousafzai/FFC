package com.example.ffccloud.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.ffccloud.Login.LoginActivity;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.CustomDialogBinding;


public class Permission {
    
    private Context mContext;
    private Activity mActivity;
    private  AlertDialog alertDialog;

    public Permission(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }
    public void getWriteStoragePermission() {

        String[] permissionArray = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });




        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }
    public void getLocationPermission() {

        String[] permissionArray = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }

    public void getCOARSELocationPermission() {

        String[] permissionArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });



        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }
    public void getCameraPermission() {

        String[] permissionArray = new String[]{Manifest.permission.CAMERA};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });



        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();

        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }

    public void getStoragePermission() {

        String[] permissionArray = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });



        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }

    public void getCallPermission() {

        String[] permissionArray = new String[]{Manifest.permission.CALL_PHONE};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });



        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {


            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();



        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }


    public void getSMSPermission() {

        String[] permissionArray = new String[]{Manifest.permission.SEND_SMS};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.SEND_SMS)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();



        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }

    public void getPhoneStatePermission() {

        String[] permissionArray = new String[]{Manifest.permission.READ_PHONE_STATE};
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder( mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();


        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(mActivity,
                        permissionArray,
                        CONSTANTS.PERMISSION_REQUEST_CODE);
            }
        });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_PHONE_STATE)) {

            dialogBinding.title.setText("Storage Permission Needed");
            dialogBinding.body.setText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showOpenLocationSettingDialog() {


        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(mActivity.getLayoutInflater());
        alertDialog = new AlertDialog.Builder(mContext).setView(dialogBinding.getRoot()).setCancelable(true).create();
        dialogBinding.title.setText("Please turn on  location for this action.");
        dialogBinding.body.setText("Do you want to open location setting.");
        alertDialog.show();

        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

    }
}
