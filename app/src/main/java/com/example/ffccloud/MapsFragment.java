package com.example.ffccloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.model.TrackerModel;

import com.example.ffccloud.databinding.FragmentMaps2Binding;
import com.example.ffccloud.utils.Permission;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private List<TrackerModel> trackerModelList= new ArrayList<>();
    private FragmentMaps2Binding mBinding;
    private int selectedDay, selectedMonth, selectedYear;
    private String selectedDate = "";
    private List<LatLng> latLngList= new ArrayList<>();
    private Permission permission ;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
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




        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mBinding = FragmentMaps2Binding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permission= new Permission(requireContext(),requireActivity());


    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//        {
//            if (permission.isLocationEnabled())
//            {
//                SupportMapFragment mapFragment =
//                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//                if (mapFragment != null) {
//                    mapFragment.getMapAsync(callback);
//                }
//            }
//            else
//            {
//                showDialog();
//            }
//
//
//        }
//        else
//        {
//            permission.getLocationPermission();
//        }
//
//        addDummyData();
//        dateListener();
//    }
//
//    public void dateListener()
//    {
//        Calendar cldr = Calendar.getInstance();
//
//
//        selectedDay = (cldr.get(Calendar.DAY_OF_MONTH));
//        selectedMonth = cldr.get(Calendar.MONTH);
//        selectedYear = cldr.get(Calendar.YEAR);
//
//        int checkmonth = (selectedMonth % 10);
//        int checkday = (selectedDay % 10);
//        String mDay = null, mMonth = null, mYear = String.valueOf(selectedYear);
//        if (checkmonth > 0 && selectedMonth < 10) {
//            mMonth = "0" + (selectedMonth + 1);
//            //          date = day + "-" + "0" + (month + 1) + "-" + (year);
//        } else {
//            mMonth = String.valueOf(selectedMonth + 1);
//
//        }
//
//        if (checkday > 0 && selectedDay < 10) {
//            mDay = "0" + (selectedDay);
//
//        } else {
//            mDay = String.valueOf(selectedDay);
//
//        }
//
//        mBinding.datePickerTimeline.setInitialDate(selectedYear, selectedMonth - 1, selectedDay - 1);
//
//
//        mBinding.datePickerTimeline.setSelected(true);
//        mBinding.datePickerTimeline.requestFocus();
//        mBinding.datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
//                String date;
//                selectedDay = day;
//                selectedMonth = month;
//                selectedYear = year;
//                int checkmonth = (month % 10);
//                int checkday = (day % 10);
//                String mDay = null, mMonth = null, mYear = String.valueOf(year);
//                if (checkmonth > 0 && month < 10) {
//                    mMonth = "0" + (month + 1);
//                    //          date = day + "-" + "0" + (month + 1) + "-" + (year);
//                } else {
//                    mMonth = String.valueOf(month + 1);
//
//                }
//
//                if (checkday > 0 && day < 10) {
//                    mDay = "0" + (day);
//
//                } else {
//                    mDay = String.valueOf(day);
//
//                }
//                date = mDay + "-" + (mMonth) + "-" + (mYear);
//
//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                {
//                    if (permission.isLocationEnabled())
//                    {
//                        checkTrack(date);
//                    }
//                    else
//                    {
//                        showDialog();
//                    }
//
//
//                }
//                else
//                {
//                    permission.getLocationPermission();
//                }
//                selectedDate = date;
//                Log.d("date", "date select ho rai hai");
//
//
//            }
//
//            @Override
//            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
//
//            }
//        });
//    }
//
//    public void checkTrack(String date) {
//
//
//        if (mMap!=null)
//        {
//            mMap.clear();
//            Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_location_on_24);
//            Canvas canvas = new Canvas();
//            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//            for (int i =0;i<trackerModelList.size();i++)
//            {
//                TrackerModel trackerModel = trackerModelList.get(i);
//                if (trackerModel.getDate().equals(date))
//                {
//                    String[] locationString = trackerModel.getCoordinates().split(",");
//                    LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
//                    latLngList.add(marker);
//                    String dateTime= trackerModel.getTime()+"\n"+trackerModel.getDate();
//
//                    mMap.addMarker(new MarkerOptions().position(marker).title(dateTime));
//
//                }
//
//
//            }
//
//            if (latLngList!=null&&latLngList.size()>0)
//            {
//                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
//                        .clickable(true)
//                        .addAll(latLngList).color(getResources().getColor(R.color.blue)));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(2),12.0f));
//                latLngList= new ArrayList<>() ;
//            }
//            else
//            {
//                mMap.clear();
//
//            }
//
//        }
//        else
//        {
//            Toast.makeText(requireContext(), "Reselect Date", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }
//
//    public void addDummyData()
//    {
//        trackerModelList.add(new TrackerModel(1,"03:10","31.4752825, 74.2545129","16-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"04:10","31.464473, 74.260460","16-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"05:10","31.441021, 74.275934","16-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"06:10","31.485343, 74.318541","16-09-2021"));
//
//
//        trackerModelList.add(new TrackerModel(1,"03:10","31.4752825, 74.2545129","17-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"04:10","31.464473, 74.260460","17-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"05:10","31.441021, 74.275934","17-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"06:10","31.485343, 74.318541","17-09-2021"));
//
//        trackerModelList.add(new TrackerModel(1,"03:10","31.4752825, 74.2545129","20-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"04:10","31.464473, 74.260460","20-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"05:10","31.441021, 74.275934","20-09-2021"));
//        trackerModelList.add(new TrackerModel(1,"06:10","31.485343, 74.318541","20-09-2021"));
//    }
//
//    public void showDialog()
//    {
//
////        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
////                .setTitleText("Please turn on  location for this action.")
////                .setContentText("Do you want to open location setting.")
////                .setConfirmText("Yes")
////                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                    @Override
////                    public void onClick(SweetAlertDialog sweetAlertDialog) {
////                        sweetAlertDialog.dismiss();
////                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        requireContext().startActivity(intent);
////                    }
////                })
////                .setCancelText("Cancel")
////                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                    @Override
////                    public void onClick(SweetAlertDialog sweetAlertDialog) {
////                        sweetAlertDialog.dismiss();
////                    }
////                }).show();
//    }
//

}