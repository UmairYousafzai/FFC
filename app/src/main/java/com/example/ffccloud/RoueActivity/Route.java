package com.example.ffccloud.RoueActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.ffccloud.model.Activity;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.Permission;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Route {

    public static String loc = "NULL";
    public static String locNetwork = "NULL";
    private Context mContext;
    private android.app.Activity mActivity;
    private ActivityViewModel activityViewModel;

    public Route(Context mContext, android.app.Activity mActivity ) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        ViewModelStoreOwner owner= (ViewModelStoreOwner) mActivity;
        this.activityViewModel = new ViewModelProvider(owner).get(ActivityViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void locationfinder(final Context context) {
        LocationManager locationManagerGPS = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationManager locationManagerNetowrk = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                // docLocationCord.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
                loc = location.getLatitude() + "," + location.getLongitude();
                //  Toast.makeText(context,loc,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        locationManagerNetowrk.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locNetwork = location.getLatitude() + "," + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public RouteActivityModel StartActivity(Context context, String Main_Activity, String Sub_Activity, int Task_id, String Start_time,
                                            Boolean Is_Uploaded){

        RouteActivityModel userModel = new RouteActivityModel();

        userModel.setMain_Activity(Main_Activity);
        userModel.setSub_Activity(Sub_Activity);
        userModel.setTask_Id(0);
        userModel.setStart_date_time(String.valueOf(LocalTime.now()));
        userModel.setIs_Uploaded(false);
        userModel.setEnd_date_time(null);



        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            locationfinder(context);
        }

        locationfinder(context);
        if (loc.equals("NULL"))
            loc = locNetwork;
        userModel.setStart_coordinates(loc);
        return userModel;

    }


    public String EndActivity(){

        if (loc.equals("NULL"))
            loc = locNetwork;
        return  loc;


    }

    public void openActivity(String mainActivity,String subActivity,int taskID)
    {
        CustomLocation customLocation= new CustomLocation(mContext);

        Activity activity = new Activity();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c);
        Permission permission= new Permission(mContext,mActivity);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (permission.isLocationEnabled())
            {
                CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                    @Override
                    public void gotLocation(Location location) {
                        activity.setMainActivity(mainActivity);
                        activity.setSubActivity(subActivity);
                        activity.setStartDateTime(formattedDate);
                        activity.setTaskID(taskID);
                        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                        activity.setStartCoordinates(locationString);
                        activityViewModel.insertActivity(activity);


                    }
                };

                customLocation.getLastLocation(locationResults);
            }
            else
            {
                showDialog();
            }

        }
        else {

            permission.getLocationPermission();
            permission.getCOARSELocationPermission();
        }


    }
    public void showDialog()
    {
//        new SweetAlertDialog(mContext)
//                .setTitleText("Please turn on  location for this action.")
//                .setContentText("Do you want to open location setting.")
//                .setConfirmText("Yes")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                        sweetAlertDialog.dismiss();
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        mContext.startActivity(intent);
//                    }
//                })
//                .setCancelText("Cancel")
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismiss();
//                    }
//                }).show();
    }
}

