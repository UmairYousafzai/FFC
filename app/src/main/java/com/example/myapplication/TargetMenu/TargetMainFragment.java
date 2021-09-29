package com.example.myapplication.TargetMenu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.R;
import com.example.myapplication.RoueActivity.Route;
import com.example.myapplication.RoueActivity.RouteActivityModel;
import com.example.myapplication.databinding.FragmentTargetMainBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TargetMainFragment extends Fragment {
    private NavController navController;
    private FragmentTargetMainBinding mBinding;
    private ActivityViewModel activityViewModel;
    private long pressedTime=0;
    private Activity runningActivity= new Activity();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentTargetMainBinding.inflate(inflater,container,false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    requireActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        navController = NavHostFragment.findNavController(this);

        activityViewModel.getAllActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {

                if (activities!=null&&!activities.isEmpty())
                runningActivity=activities.get(0);

            }
        });

        btnListener();
    }


    public void btnListener()
    {
        Permission permission = new Permission(requireContext(),requireActivity());

        mBinding.showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetMainFragmentDirections.actionNavTargetMainToShowRouteFragment());
            }
        });

        mBinding.targetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeActivity();
                openActivity(CONSTANTS.TARGET,CONSTANTS.TARGET,0);

            }
        });

        mBinding.officeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
                openActivity(CONSTANTS.OFFICE,CONSTANTS.OFFICE,0);

            }
        });

        mBinding.privateTravelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
                openActivity(CONSTANTS.PRIVATE_TRAVEL,CONSTANTS.PRIVATE_TRAVEL,0);

            }
        });

        mBinding.localTravelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeActivity();
                openActivity(CONSTANTS.LOCAL_TRAVEL,CONSTANTS.LOCAL_TRAVEL,0);
            }
        });


    }


    public void closeActivity()
    {
        if (!runningActivity.getMainActivity().equals(CONSTANTS.START_DAY))
        {
            CustomLocation customLocation= new CustomLocation();

            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
            String formattedDate = df.format(c);
            Permission permission= new Permission(requireContext(),requireActivity());

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (permission.isLocationEnabled())
                {
                    CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {
                            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                            runningActivity.setEndCoordinates(locationString);
                            runningActivity.setEndDateTime(formattedDate);
                            String totalTime=calculateTotalTime(formattedDate,runningActivity.getStartDateTime());
                            runningActivity.setTotalTime(totalTime);
                            activityViewModel.updateActivity(runningActivity);


                        }
                    };

                    customLocation.getLastLocation(requireContext(),requireActivity(),locationResults);
                }
                else
                {
                    showDialog();
                }

            }
            else {
                permission.getLocationPermission();
            }

        }


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

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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
                        if (mainActivity.equals(CONSTANTS.PRIVATE_TRAVEL))
                        {
                            TargetMainFragmentDirections.ActionNavTargetMainToFragmentMenu action= TargetMainFragmentDirections.actionNavTargetMainToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.PRIVATE_TRAVEL);
                            navController.navigate(action);
                        }
                        else if (mainActivity.equals(CONSTANTS.LOCAL_TRAVEL))
                        {
                            TargetMainFragmentDirections.ActionNavTargetMainToFragmentMenu action= TargetMainFragmentDirections.actionNavTargetMainToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.LOCAL_TRAVEL);
                            navController.navigate(action);
                        }
                        else if (mainActivity.equals(CONSTANTS.OFFICE))
                        {
                            TargetMainFragmentDirections.ActionNavTargetMainToFragmentMenu action= TargetMainFragmentDirections.actionNavTargetMainToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.OFFICE);
                            navController.navigate(action);
                        }
                        else if (mainActivity.equals(CONSTANTS.TARGET))
                        {
                            navController.navigate(TargetMainFragmentDirections.actionNavTargetMainToNavTargetSubMenu());

                        }



                    }
                };

                customLocation.getLastLocation(requireContext(),requireActivity(),locationResults);
            }
            else
            {
                progressDialog.dismiss();
               showDialog();
            }

        }
        else {
            progressDialog.dismiss();

            permission.getLocationPermission();
        }


    }
    private String calculateTotalTime(String formattedDate, String startDateTime) {

        String endTime= formattedDate.substring(10,17),startTime=startDateTime.substring(10,17);
        int endHours= Integer.parseInt(formattedDate.substring(10,11)), endMinutes= Integer.parseInt(formattedDate.substring(13,14))
                ,endSeconds=Integer.parseInt(formattedDate.substring(16,17));
        int startHours= Integer.parseInt(startDateTime.substring(10,11)), startMinutes= Integer.parseInt(startDateTime.substring(13,14))
                ,startSeconds=Integer.parseInt(startDateTime.substring(16,17));

        return (Math.abs(endHours-startHours))+":"+(Math.abs(endMinutes-startMinutes))+":"+(Math.abs(endSeconds-startSeconds));
    }
    public void showDialog()
    {
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