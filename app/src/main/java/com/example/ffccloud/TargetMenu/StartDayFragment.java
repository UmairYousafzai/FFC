package com.example.ffccloud.TargetMenu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.ModelClasses.Activity;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentStartDayBinding;
import com.example.ffccloud.salesOrder.ProductInfoBottomSheetDialogFragment;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.Permission;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class StartDayFragment extends Fragment {
    private NavController navController;
    private FragmentStartDayBinding mBinding;
    private ActivityViewModel activityViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding= FragmentStartDayBinding.inflate(inflater,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


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
                if (SharedPreferenceHelper.getInstance(requireContext()).getAttendanceConfiguration())
                {
                    navController.navigate(StartDayFragmentDirections.actionNavStartDayToNavAttendance());
                }
                else
                {

                    openActivity(CONSTANTS.START_DAY,CONSTANTS.START_DAY,0);
                    SharedPreferenceHelper.getInstance(requireContext()).setStart(true);
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void openActivity(String mainActivity, String subActivity, int taskID)
    {   ProgressDialog progressDialog= new ProgressDialog(requireContext());
    progressDialog.setMessage("Loading...");
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

                        navController.navigate(StartDayFragmentDirections.actionNavStartDayToNavTargetMain());


                    }
                };

                customLocation.getLastLocation(locationResults);
            }
            else
            {
                progressDialog.dismiss();
                CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity());
            }

        }
        else {
            progressDialog.dismiss();

            permission.getLocationPermission();
            permission.getCOARSELocationPermission();
        }


    }

}