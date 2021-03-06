package com.example.ffccloud.TargetMenu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.model.Activity;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentTargetMainBinding;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TargetMainFragment extends Fragment {
    private NavController navController;
    private FragmentTargetMainBinding mBinding;
    private ActivityViewModel activityViewModel;
    private long pressedTime=0;



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


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void btnListener()
    {
        Permission permission = new Permission(requireContext(),requireActivity());



        mBinding.targetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openActivity(CONSTANTS.TARGET,CONSTANTS.TARGET,0);

            }
        });

        mBinding.officeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity(CONSTANTS.OFFICE,CONSTANTS.OFFICE,0);

            }
        });

        mBinding.privateTravelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CONSTANTS.PRIVATE_TRAVEL,CONSTANTS.PRIVATE_TRAVEL,0);

            }
        });

        mBinding.localTravelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity(CONSTANTS.LOCAL_TRAVEL,CONSTANTS.LOCAL_TRAVEL,0);
            }
        });


    }


    public void openActivity(String mainActivity,String subActivity,int taskID)
    {   ProgressDialog progressDialog= new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        CustomLocation customLocation= new CustomLocation(requireContext());

        Activity activity = new Activity();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
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

                        String startAddress = customLocation.getCompleteAddressString(location.getLatitude(),location.getLongitude());
                        activity.setStartAddress(startAddress);

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

                customLocation.getLastLocation(locationResults);
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

        int endHours=0,endMinutes=0,endSeconds=0,startHours=0,startMinutes=0,startSeconds=0;
        if (!startDateTime.isEmpty()&&!startDateTime.isEmpty())
        {
            endHours= Integer.parseInt(formattedDate.substring(11,13));
            endMinutes= Integer.parseInt(formattedDate.substring(14,16));
            endSeconds=Integer.parseInt(formattedDate.substring(17,19));
            startHours= Integer.parseInt(startDateTime.substring(11,13));
            startMinutes= Integer.parseInt(startDateTime.substring(14,16));
            startSeconds=Integer.parseInt(startDateTime.substring(17,19));
        }


        return (Math.abs(endHours-startHours))+":"+(Math.abs(endMinutes-startMinutes))+":"+(Math.abs(endSeconds-startSeconds));

    }
    public void showDialog()
    {
        CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
    }


}