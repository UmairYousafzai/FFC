package com.example.ffccloud.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CustomLocation {
    private Context mContext;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final Location[] location = new Location[1];
    private Timer timer;
    private CustomLocationResults customLocationResults;
    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 500;


    public CustomLocation(Context mContext) {
        this.mContext = mContext;
    }

    public void getLastLocation(CustomLocationResults results) {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        customLocationResults=results;

            getLocation();

        


    }



    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void getLocation ()
    {


            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled()) {

                    Task<Location> locationTask=fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                        @Override
                        public boolean isCancellationRequested() {
                            return false;
                        }

                        @NonNull
                        @Override
                        public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                            return null;
                        }
                    });

                    locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location= task.getResult();
                            if(location!=null)
                            {
                                Bundle extras = location.getExtras();
                                boolean isMockLocation = extras != null && extras.getBoolean(FusedLocationProviderClient.KEY_MOCK_LOCATION, false);

                                if (!isMockLocation)
                                {
                                    customLocationResults.gotLocation(location);

                                }
                                else
                                {
                                    Toast.makeText(mContext, "Mock location is ON \n Please Disable Mock Location", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                    });



//                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                        @SuppressLint("MissingPermission")
//                        @Override
//                        public void onComplete(@NonNull Task<Location> task) {
//
//                            if (task.getResult()!=null)
//                            {
//                                Location location= task.getResult();
//                                customLocationResults.gotLocation(location);
//                            }
//                            else
//                            {
//                                LocationCallback mLocationCallback = new LocationCallback() {
//
//                                    @Override
//                                    public void onLocationResult(LocationResult locationResult) {
//                                        Location mLastLocation = locationResult.getLastLocation();
//                                        customLocationResults.gotLocation(mLastLocation);
//                                    }
//                                };
//
//                                // Initializing LocationRequest
//                                // object with appropriate methods
//                                LocationRequest mLocationRequest =  LocationRequest.create();
//                                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                                mLocationRequest.setInterval(UPDATE_INTERVAL);
//                                mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//                                mLocationRequest.setNumUpdates(1);
//
//                                // setting LocationRequest
//                                // on FusedLocationClient
//                                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
//
//                                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//
//
//
//                            }
//
//                        }
//                    });
                }
            }

    }

    public interface CustomLocationResults
    {
        void gotLocation(Location location);
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }
        return strAdd;
    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
        {
            return false;

        }
        else
        {
            Toast.makeText(context, "Mock location is ON \n Please Disable Mock Location", Toast.LENGTH_SHORT).show();
            return true;

        }
    }
}
