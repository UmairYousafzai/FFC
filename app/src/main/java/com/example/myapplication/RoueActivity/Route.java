package com.example.myapplication.RoueActivity;

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

import java.time.LocalTime;

public class Route {

    public static String loc = "NULL";
    public static String locNetwork = "NULL";



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
}

