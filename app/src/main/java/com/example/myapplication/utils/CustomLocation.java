package com.example.myapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;

public class CustomLocation {
    private Context mContext;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final Location[] location = new Location[1];
    double latitude, longitude;
    private Timer timer;
    private CustomLocationResults customLocationResults;

    public void getLastLocation(Context mContext, Activity activity, CustomLocationResults results) {


        this.mContext = mContext;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        customLocationResults=results;
        timer= new Timer();
        timer.schedule(new GetLastLocation(),500);

    }



    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public class GetLastLocation extends TimerTask
    {


        @Override
        public void run() {

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled()) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            if (task.getResult()!=null)
                            {
                                Location location= task.getResult();
                                customLocationResults.gotLocation(location);
                            }
                            else
                            {
                                LocationCallback mLocationCallback = new LocationCallback() {

                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        Location mLastLocation = locationResult.getLastLocation();
                                        customLocationResults.gotLocation(mLastLocation);
                                    }
                                };

                                // Initializing LocationRequest
                                // object with appropriate methods
                                LocationRequest mLocationRequest = new LocationRequest();
                                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                mLocationRequest.setInterval(5);
                                mLocationRequest.setFastestInterval(0);
                                mLocationRequest.setNumUpdates(1);

                                // setting LocationRequest
                                // on FusedLocationClient
                                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

                                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());



                            }

                        }
                    });
                }
            }
        }
    }

    public interface CustomLocationResults
    {
        void gotLocation(Location location);
    }
}
