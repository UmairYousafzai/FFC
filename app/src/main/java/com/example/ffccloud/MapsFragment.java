package com.example.ffccloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.model.TrackerModel;

import com.example.ffccloud.databinding.FragmentMaps2Binding;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.Permission;

import com.google.android.gms.location.LocationCallback;
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
    private List<TrackerModel> trackerModelList= new ArrayList<>();
    private FragmentMaps2Binding mBinding;
    private int selectedDay, selectedMonth, selectedYear;
    private String selectedDate = "";
    private List<LatLng> latLngList= new ArrayList<>();
    private Permission permission ;
    private String workCoordinates;
    private String workPlanName;
    private boolean isWorkPlanShowing=false;

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            String locationString = location.getLatitude() + "," + location.getLongitude();
            if (isWorkPlanShowing)
            {
                drawCurrentLocation(locationString);
            }


        }
    };
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap=googleMap;
//            LatLng marker1 = new LatLng(31.4752825, 74.2545129);
//            LatLng marker2 = new LatLng(31.464473, 74.260460);
//            LatLng marker3 = new LatLng(31.441021, 74.275934);
//            LatLng marker4 = new LatLng(31.485343, 74.318541);
//            Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_location_on_24);
//            Canvas canvas = new Canvas();
//            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//            canvas.setBitmap(bitmap);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            drawable.draw(canvas);
//            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
//            mMap.addMarker(new MarkerOptions().position(marker1).title("Marker First").icon(icon));
//            mMap.addMarker(new MarkerOptions().position(marker2).title("Marker Second"));
//            mMap.addMarker(new MarkerOptions().position(marker3).title("Marker Three"));
//            drawable = getResources().getDrawable(R.drawable.ic_baseline_destination_end_24);
//            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//            canvas.setBitmap(bitmap);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            drawable.draw(canvas);
//            icon = BitmapDescriptorFactory.fromBitmap(bitmap);
//            mMap.addMarker(new MarkerOptions().position(marker4).title("Marker Four").icon(icon));
//
//            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
//                    .clickable(true)
//                    .add(marker1,marker2,marker3,marker4).color(getResources().getColor(R.color.blue)));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker2,10.0f));

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

        mBinding = FragmentMaps2Binding.inflate(inflater,container,false);
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
        mMap.clear();
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
        circleOptions.radius(100);
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