package com.example.ffccloud.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentMapsBinding;
import com.example.ffccloud.model.TrackerModel;

import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.Permission;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private FragmentMapsBinding mBinding;
    private Permission permission ;
    private String workCoordinates;
    private String workPlanName;
    private boolean isWorkPlanShowing=false;

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            String locationString = location.getLatitude() + "," + location.getLongitude();
            Bundle extras = location.getExtras();
            boolean isMockLocation = extras != null && extras.getBoolean(FusedLocationProviderClient.KEY_MOCK_LOCATION, false);

            if (!isMockLocation){
                if (isWorkPlanShowing)
                {
                    drawCurrentLocation(locationString);
                }

            }else
            {
                Toast.makeText(requireContext(), "Mock location is ON \n Please Disable Mock Location", Toast.LENGTH_SHORT).show();
                stopLocationUpdates();
            }


        }
    };
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap=googleMap;

            if (!isWorkPlanShowing)
            {
                if (workCoordinates!=null && !workCoordinates.isEmpty())
                {
                    drawWorkMarker();
                }
            }




        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        mBinding = FragmentMapsBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permission= new Permission(requireContext(),requireActivity());

        if (getArguments()!=null)
        {
            workCoordinates = MapsFragmentArgs.fromBundle(getArguments()).getCoordinates();
            workPlanName =MapsFragmentArgs.fromBundle(getArguments()).getWorkPlanName();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (permission.isLocationEnabled())
            {
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }

                setupLocationGetter();
            }
            else
            {
                CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
            }


        }
        else
        {
            permission.getLocationPermission();
        }

    }

    @SuppressLint("MissingPermission")
    private void setupLocationGetter() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest =  LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4000);
        mLocationRequest.setFastestInterval(4000);

        // setting LocationRequest
        // on FusedLocationClient
        LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    @Override
    public void onStop() {
        super.onStop();

        stopLocationUpdates();
    }

    private void drawWorkMarker() {
        if (mMap!=null)
        {
            Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_location_on_24,null);
            BitmapDescriptor icon = BitmapFromVector(drawable);



                String[] locationString =workCoordinates.split(",");
                LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                mMap.addMarker(new MarkerOptions().position(marker).title(workPlanName).icon(icon));
                drawCircle(marker);
            isWorkPlanShowing=true;
        }
    }

    private void drawCurrentLocation(String location) {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.current_location);
        BitmapDescriptor icon = BitmapFromVector(drawable);


        drawWorkMarker();

        String[] locationString =location.split(",");
        LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
        mMap.addMarker(new MarkerOptions().position(marker).title(workPlanName).icon(icon));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 18.0f));

    }
    private BitmapDescriptor BitmapFromVector(Drawable vectorDrawable) {
        // below line is use to generate a drawable.

        // below line is use to set bounds to our vector drawable.
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        }

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = null;
        if (vectorDrawable != null) {
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        if (vectorDrawable != null) {
            vectorDrawable.draw(canvas);
        }

        // after generating our bitmap we are returning our bitmap.
        if (bitmap != null) {
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }else
        {
            return null;
        }
    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(10);
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 18.0f));

    }

    public void stopLocationUpdates()
    {
        LocationServices.getFusedLocationProviderClient(requireContext()).removeLocationUpdates(mLocationCallback);
    }
}