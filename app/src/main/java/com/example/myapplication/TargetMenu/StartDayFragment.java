package com.example.myapplication.TargetMenu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStartDayBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StartDayFragment extends Fragment {
    private NavController navController;
    private FragmentStartDayBinding mBinding;
    private ActivityViewModel activityViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding= FragmentStartDayBinding.inflate(inflater,container,false);


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        navController = NavHostFragment.findNavController(this);

        btnListener();
    }

    public void btnListener()
    {

        mBinding.startday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity(CONSTANTS.START_DAY,CONSTANTS.START_DAY,0);
                SharedPreferenceHelper.getInstance(requireContext()).setStart(true);
            }
        });

    }

    public void openActivity(String mainActivity,String subActivity,int taskID)
    {   SweetAlertDialog progressDialog= new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
        progressDialog.setTitleText("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CustomLocation customLocation= new CustomLocation();

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
                        activity.setStartCoordinates(locationString);
                        activityViewModel.insertActivity(activity);
                        progressDialog.dismiss();

                        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

                        NavigationView navigationView= requireActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

                        navController.navigate(StartDayFragmentDirections.actionNavStartDayToNavTargetMain());


                    }
                };

                customLocation.getLastLocation(requireContext(),requireActivity(),locationResults);
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