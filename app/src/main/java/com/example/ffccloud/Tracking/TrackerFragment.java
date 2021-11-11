package com.example.ffccloud.Tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentTrackerBinding;
import com.example.ffccloud.utils.CustomLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrackerFragment extends Fragment {

    private GoogleMap mMap;
    private CustomLocation customLocation;
    private List<LatLng> latLngList = new ArrayList<>();
    int couter = 0;
    private FragmentTrackerBinding mBinding;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Polyline polyline1;
    private boolean flag = true;
    private String requestedUserID;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentTrackerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_tracker);

        assert getArguments() != null;
         requestedUserID = TrackerFragmentArgs.fromBundle(getArguments()).getRecevierID();

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        getLocationFromFireBase();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        databaseReference.child(requestedUserID).child("lastLocation").removeEventListener(valueEventListener);
        flag = false;
        Log.d("Tracker ", "onDetach");
    }

    private void getLocationFromFireBase() {
        if (flag) {
            customLocation = new CustomLocation(requireContext());


            assert requestedUserID != null;

            BitmapDescriptor icon = BitmapFromVector(R.drawable.current_location);

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String[] locationString = location.split(",");
//                LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
//                mMap.addMarker(new MarkerOptions().position(marker).title(address).icon(BitmapFromVector(R.drawable.ic_current_location_svgrepo_com)));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker,15.0f));
                    String location = snapshot.getValue(String.class);

                    if (location != null && location.length() > 0) {
                        mMap.clear();

                        String[] locationString = location.split(",");
                        LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                        if (couter <= 1) {
                            String address = customLocation.getCompleteAddressString(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                            mMap.addMarker(new MarkerOptions().position(marker).title(address).icon(icon));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.0f));
                            latLngList.add(marker);
                            couter++;
                        } else {
                            latLngList.add(marker);

                        }

                        if (latLngList != null && latLngList.size() > 2) {


                            String distance = calculateDistance(latLngList.get(latLngList.size() - 1), latLngList.get(0));
                            if (distance != null) {
                                mBinding.distance.setText(distance);
                            }
                            String address = customLocation.getCompleteAddressString(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                            mBinding.tvAddress.setText(address);
                            mMap.addMarker(new MarkerOptions().position(latLngList.get(latLngList.size() - 1)).title(address).icon(icon));

                            Log.d("Tracker ", "onGetLocation: ");
                            polyline1 = mMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .addAll(latLngList).color(getResources().getColor(R.color.blue)));


                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            databaseReference.child(requestedUserID).child("lastLocation").addValueEventListener(valueEventListener);

        } else {

        }


    }

    public String calculateDistance(LatLng currentLatLng, LatLng startingLatLng) {

        double distanc1 = 0;

        for (int i = 0; i < latLngList.size() - 1; i++) {
            Location currentLocation = new Location(""), startingLocation = new Location("");
            currentLocation.setLongitude(latLngList.get(i + 1).longitude);
            currentLocation.setLatitude(latLngList.get(i + 1).latitude);

            startingLocation.setLatitude(latLngList.get(i).latitude);
            startingLocation.setLongitude(latLngList.get(i).longitude);
            distanc1 = distanc1 + startingLocation.distanceTo(currentLocation);
        }
//        Location currentLocation = new Location(""), startingLocation = new Location("");
//        currentLocation.setLongitude(currentLatLng.longitude);
//        currentLocation.setLatitude(currentLatLng.latitude);
//
//        startingLocation.setLatitude(startingLatLng.latitude);
//        startingLocation.setLongitude(startingLatLng.longitude);
        String distance = String.valueOf(distanc1 / 1000);
        distance = distance.substring(0, 4);
        distance = "Distance Covered: " + distance + " Km";

        return distance;

    }

    private BitmapDescriptor BitmapFromVector(int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}