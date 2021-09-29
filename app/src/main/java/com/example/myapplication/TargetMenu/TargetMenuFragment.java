package com.example.myapplication.TargetMenu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTargetMenuBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TargetMenuFragment extends Fragment {
    private NavController navController;
    private FragmentTargetMenuBinding mBinding;
    private long pressedTime=0;
    private ActivityViewModel activityViewModel;
    private String activityTAG ;
    private Activity runningActivity= new Activity();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding= FragmentTargetMenuBinding.inflate(getLayoutInflater(),container,false);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    requireActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        navController = NavHostFragment.findNavController(this);


            String check = TargetMenuFragmentArgs.fromBundle(getArguments()).getSelectedMenu();
            if (!check.isEmpty())
            {
                activityTAG=check;
            }



        activityViewModel.getAllActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {

                if (activities!=null&&activities.size()>0)
                {
                    runningActivity=activities.get(activities.size()-1);

                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setupLayout();
        btnListener();
    }


    public void setupLayout()
    {
        if (activityTAG!=null)
        {
            switch (activityTAG) {
                case CONSTANTS.OFFICE:
                    mBinding.cardOffice.setVisibility(View.GONE);
                    ((MainActivity)requireActivity()).getSupportActionBar().setTitle(activityTAG);
                    mBinding.headerText.setText(activityTAG);
                    break;
                case CONSTANTS.LOCAL_TRAVEL:
                    mBinding.localTravelCard.setVisibility(View.GONE);
                    ((MainActivity)requireActivity()).getSupportActionBar().setTitle(activityTAG);
                    mBinding.headerText.setText(activityTAG);
                    break;
                case CONSTANTS.PRIVATE_TRAVEL:
                    mBinding.cardPrivateTravel.setVisibility(View.GONE);
                    ((MainActivity)requireActivity()).getSupportActionBar().setTitle(activityTAG);
                    mBinding.headerText.setText(activityTAG);
                    break;
            }
        }

    }

   public void btnListener()
   {
       Permission permission= new Permission(requireContext(),requireActivity());

       mBinding.showRoute.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               navController.navigate(TargetMenuFragmentDirections.actionFragmentMenuToShowRouteFragment());
           }
       });
       mBinding.targetCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               closeActivity();
               openActivity(CONSTANTS.TARGET,CONSTANTS.TARGET,0);
               activityTAG=CONSTANTS.TARGET;
           }
       });

       mBinding.cardOffice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               closeActivity();
               openActivity(CONSTANTS.OFFICE,CONSTANTS.OFFICE,0);
               ((MainActivity)requireActivity()).getSupportActionBar().setTitle("Office");
               mBinding.headerText.setText("Office");
               activityTAG=CONSTANTS.OFFICE;

           }
       });


       mBinding.cardPrivateTravel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               closeActivity();
               openActivity(CONSTANTS.PRIVATE_TRAVEL,CONSTANTS.PRIVATE_TRAVEL,0);
               ((MainActivity)requireActivity()).getSupportActionBar().setTitle("Private Travel");
               mBinding.headerText.setText("Private Travel");
               activityTAG=CONSTANTS.PRIVATE_TRAVEL;

           }
       });

       mBinding.localTravelCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               closeActivity();
               openActivity(CONSTANTS.LOCAL_TRAVEL,CONSTANTS.LOCAL_TRAVEL,0);
               ((MainActivity)requireActivity()).getSupportActionBar().setTitle("Local Travel");
               mBinding.headerText.setText("Local Travel");
               activityTAG=CONSTANTS.LOCAL_TRAVEL;

           }
       });
   }


    public void closeActivity()
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
                            mBinding.cardPrivateTravel.setVisibility(View.GONE);
                            mBinding.cardOffice.setVisibility(View.VISIBLE);
                            mBinding.localTravelCard.setVisibility(View.VISIBLE);

                        }
                        else if (mainActivity.equals(CONSTANTS.LOCAL_TRAVEL))
                        {
                            mBinding.localTravelCard.setVisibility(View.GONE);
                            mBinding.cardPrivateTravel.setVisibility(View.VISIBLE);
                            mBinding.cardOffice.setVisibility(View.VISIBLE);
                        }
                        else if (mainActivity.equals(CONSTANTS.OFFICE))
                        {
                            mBinding.cardOffice.setVisibility(View.GONE);
                            mBinding.cardPrivateTravel.setVisibility(View.VISIBLE);
                            mBinding.localTravelCard.setVisibility(View.VISIBLE);

                        }
                        else if (mainActivity.equals(CONSTANTS.TARGET))
                        {
                            navController.navigate(TargetMenuFragmentDirections.actionFragmentMenuToNavTargetSubMenu());

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
        int endHours= Integer.parseInt(formattedDate.substring(10,12)), endMinutes= Integer.parseInt(formattedDate.substring(13,15))
                ,endSeconds=Integer.parseInt(formattedDate.substring(16,18));
        int startHours= Integer.parseInt(startDateTime.substring(10,12)), startMinutes= Integer.parseInt(startDateTime.substring(13,15))
                ,startSeconds=Integer.parseInt(startDateTime.substring(16,18));

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