package com.example.ffccloud.Tracking.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.example.ffccloud.R;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationService extends Service {
    private DatabaseReference databaseReference;
    CustomLocation customLocation;
    private  String userID = String.valueOf(SharedPreferenceHelper.getInstance(this).getUserID());

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult!=null)
            {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("lastLocation");
                Location location = locationResult.getLastLocation();
                String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                databaseReference.setValue(locationString).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Location :", locationString);

                    }
                });

            }

        }
    };
    public LocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service : ","on create");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        customLocation = new CustomLocation(this);
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(CONSTANTS.ACTION_START_LOCATION_SERVICE)) {
                    showNotification();
                }
                if (action.equals(CONSTANTS.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationUpdates();
                    stopForeground(true);
                    stopSelf();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service : ","on destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void showNotification() {



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId");

        ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        } else {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        }


        builder.setSmallIcon(R.drawable.img_erp_cloud_logo)
                .setContentText("Location Sharing Is Enable ")
                .setContentTitle("Location");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define

        Notification notification = builder.build();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (customLocation.isLocationEnabled()) {

                // Initializing LocationRequest
                // object with appropriate methods
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(4000);
                mLocationRequest.setFastestInterval(4000);

                // setting LocationRequest
                // on FusedLocationClient
                LocationServices.getFusedLocationProviderClient(this)
                        .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        }
        else {
            Toast.makeText(this, "Please Allow Permission Access in App Setting.", Toast.LENGTH_SHORT).show();
        }

        startForeground(123, notification);

    }
    public void stopLocationUpdates()
    {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
    }
}