package com.example.myapplication.AttendanceActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.R;
import com.example.myapplication.TargetMenu.StartDayFragmentDirections;
import com.example.myapplication.databinding.FragmentAttendanceBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AttendanceFragment extends Fragment {
    private NavController navController;
    private FragmentAttendanceBinding mBinding;
    private Bitmap selectedImage;
    private AttendanceViewModel attendanceViewModel;
    private AttendanceModel attendance;
    private FfcDatabase ffcDatabase;
    private ActivityViewModel activityViewModel;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAttendanceBinding.inflate(inflater,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//        Toolbar toolbar=  (Toolbar)requireActivity().findViewById(R.id.custom_toolbar) ;
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        ffcDatabase = FfcDatabase.getInstance(requireContext());

        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        activityViewModel =new ViewModelProvider(this).get(ActivityViewModel.class);

        btnListener();
    }

    private void btnListener() {

        mBinding.btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();



            }
        });
        mBinding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendance!=null&&SharedPreferenceHelper.getInstance(requireContext()).getAttendanceConfiguration()) {
                    attendanceViewModel.insertAttendance(attendance);
                    openActivity(CONSTANTS.START_DAY,CONSTANTS.START_DAY,0);
                }
                else
                {
                    Toast.makeText(requireContext(), "Please Mark Attendance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveAttendance(String IMEI)
    {
        Permission permission= new Permission(requireContext(),requireActivity());



        if (selectedImage!=null)
        {
            SweetAlertDialog pDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
            String formattedDate = df.format(c);
            CustomLocation customLocation= new CustomLocation(requireContext());

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (permission.isLocationEnabled())
                {
                    CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {

                            pDialog.dismiss();
                            String image = getBytesFromBitmap(selectedImage);
                            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                            int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
                            AttendanceModel attendanceModel = new AttendanceModel();
                            attendanceModel.setAttendanceCoordinates(locationString);
                            attendanceModel.setAttendanceImage(image);
                            attendanceModel.setDateTime(formattedDate);
                            attendanceModel.setEmpID(id);
                            attendanceModel.setImeiNumber(IMEI);
                            attendance=attendanceModel;
                            GetUserInfoModel loginUser = ffcDatabase.dao().getLoginUser();
                            mBinding.nameTextview.setText(loginUser.getUserName());
                            mBinding.imeiTextview.setText(IMEI);
                            mBinding.dateTimeTextview.setText(formattedDate);

                        }
                    };

                    customLocation.getLastLocation(locationResults);
                }
                else
                {
                    new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Please turn on  location for this action.")
                            .setContentText("Do you want to open location setting.")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    requireContext().startActivity(intent);
                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                    mBinding.attendanceImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_icon));
                    selectedImage= null;
                }

            }
            else
            {
                permission.getLocationPermission();
                mBinding.attendanceImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_icon));
                selectedImage= null;
            }


        }
        else
        {
            new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Please Mark Attendance")
                    .show();
        }

    }

    private void captureImage() {
        Permission permission = new Permission(requireContext(),requireActivity());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.getCameraPermission();
        } else {
            Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(requireContext().getPackageManager())!=null)
            {
                startActivityForResult(cameraIntent,0);
            }

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_CANCELED)
        {
            if (requestCode==0)
            {
                if (resultCode==RESULT_OK&&data!=null)
                {
                    selectedImage= (Bitmap) data.getExtras().get("data");
                    mBinding.attendanceImage.setImageBitmap(selectedImage);
                    String uniqueID;

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    {
                         uniqueID = UUID.randomUUID().toString();
                    }
                    else
                    {
                        uniqueID = getDeviceIMEI();

                    }

                    saveAttendance(uniqueID);
                }
            }
        }
    }


    public  String getDeviceIMEI( ) {
        Permission permission = new Permission(requireContext(),requireActivity());
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) requireActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                permission.getPhoneStatePermission();
            }
            else {
                deviceUniqueIdentifier = tm.getDeviceId();
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier = "0";
            }
        }
        return deviceUniqueIdentifier;
    }
    public String getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        String imgString = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        return imgString;
    }

    public void openActivity(String mainActivity, String subActivity, int taskID)
    {   SweetAlertDialog progressDialog= new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
        progressDialog.setTitleText("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CustomLocation customLocation= new CustomLocation(requireContext());

        Activity activity = new Activity();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c);
        Permission permission= new Permission(requireContext(),requireActivity());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (permission.isLocationEnabled())
            {
                CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                    @Override
                    public void gotLocation(Location location) {
                        activity.setMainActivity(mainActivity);
                        activity.setSubActivity(subActivity);
                        activity.setStartDateTime(formattedDate);
                        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                        String startAddress = customLocation.getCompleteAddressString(location.getLatitude(),location.getLongitude());
                        activity.setStartAddress(startAddress);
                        activity.setStartCoordinates(locationString);
                        activityViewModel.insertActivity(activity);
                        progressDialog.dismiss();

                        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

                        NavigationView navigationView= requireActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);
                        SharedPreferenceHelper.getInstance(requireContext()).setStart(true);

                        navController.navigate(AttendanceFragmentDirections.actionNavAttendanceToNavTargetMain());



                    }
                };

                customLocation.getLastLocation(locationResults);
            }
            else
            {
                progressDialog.dismiss();
                new SweetAlertDialog(requireContext())
                        .setTitleText("Please turn on  location for this action.")
                        .setContentText("Do you want to open location setting.")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                requireContext().startActivity(intent);
                            }
                        })
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }

        }
        else {
            progressDialog.dismiss();

            permission.getLocationPermission();
            permission.getCOARSELocationPermission();
        }


    }

}