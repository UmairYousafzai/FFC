package com.example.ffccloud.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.R;
import com.example.ffccloud.Target.MapsFragmentArgs;
import com.example.ffccloud.databinding.FragmentLocationPickerBinding;
import com.example.ffccloud.databinding.FragmentMapsBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
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

import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class LocationPickerFragment extends Fragment {


    private GoogleMap mMap;
    private FragmentLocationPickerBinding mBinding;
    private Permission permission ;
    private GoogleMap.OnCameraIdleListener cameraIdleListener;
    private GoogleMap.OnCameraMoveListener cameraMoveListener;
    private NavController navController;
    private String locationString ,address;


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap=googleMap;
            mMap.setOnCameraIdleListener(cameraIdleListener);
            mMap.setOnCameraMoveListener(cameraMoveListener);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_location_on_24,null);
            BitmapDescriptor icon = BitmapFromVector(drawable);
            CustomLocation customLocation= new CustomLocation(requireContext());
            CustomLocation.CustomLocationResults locationResults = new CustomLocation.CustomLocationResults() {
                @Override
                public void gotLocation(Location location) {
                    LatLng marker =new LatLng(location.getLatitude(),location.getLongitude());
                    String makerTitle = getCompleteAddressString(marker.latitude,marker.longitude);
                    if (icon!=null)
                    {
                        mMap.addMarker(new MarkerOptions().position(marker).title(makerTitle).icon(icon));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.0f));
                        locationString = location.getLatitude() +","+location.getLongitude();
                        address= makerTitle;
                    }

                }
            };
            customLocation.getLastLocation(locationResults);

        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        mBinding = FragmentLocationPickerBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permission= new Permission(requireContext(),requireActivity());

        navController= NavHostFragment.findNavController(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (permission.isLocationEnabled())
            {
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.location_picker_map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }

                drawWorkMarker();

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

        btnListener();

    }

    private void btnListener() {

        mBinding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.LOCATION,address+":"+locationString);
                navController.popBackStack();
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    private void drawWorkMarker() {

            Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_location_on_24,null);
            BitmapDescriptor icon = BitmapFromVector(drawable);

            cameraIdleListener = new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    mBinding.pinLocation.setVisibility(View.GONE);

                    if (icon!=null)
                    {
                        LatLng marker =mMap.getCameraPosition().target;
                        String makerTitle = getCompleteAddressString(marker.latitude,marker.longitude);
                        Objects.requireNonNull(mMap.addMarker(new MarkerOptions().position(marker).title(makerTitle).icon(icon))).showInfoWindow();

                        locationString = marker.latitude +","+marker.longitude;
                        address= makerTitle;
                    }

                }
            };

            cameraMoveListener= new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                    mMap.clear();
                    mBinding.pinLocation.setVisibility(View.VISIBLE);

                }
            };



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

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
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

}