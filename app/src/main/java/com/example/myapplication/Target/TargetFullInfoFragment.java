package com.example.myapplication.Target;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.DoctorModel;
import com.example.myapplication.MainActivity;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.ModelClasses.UpdateStatus;
import com.example.myapplication.ModelClasses.UpdateWorkPlanStatus;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.R;
import com.example.myapplication.Target.utils.TargetViewModel;
import com.example.myapplication.TargetMenu.TargetMenuFragmentDirections;
import com.example.myapplication.databinding.CustomCompeleteDialogBinding;
import com.example.myapplication.databinding.FragmentTargetFullInfoBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetFullInfoFragment extends Fragment {

    private View view;
    private FragmentTargetFullInfoBinding mBinding;
    private DoctorModel doctorModel;
    private AlertDialog alertDialog;
    private Location location;
    private TargetViewModel targetViewModel;
    private ActivityViewModel activityViewModel;
    private List<Activity> taskActivities;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentTargetFullInfoBinding.inflate(inflater, container, false);
        view = mBinding.getRoot();

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        targetViewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        setInfoWorkPlan();
        btnListener();
    }

    public void setInfoWorkPlan() {
        Bundle bundle = getArguments();
        doctorModel = TargetFullInfoFragmentArgs.fromBundle(bundle).getDoctorModel();
        mBinding.workName.setText(doctorModel.getName());
        mBinding.workLocation.setText(doctorModel.getAddress());
        mBinding.workDistanceFromCurrentLoc.setText(doctorModel.getDistance());
    }

    public void btnListener() {
        Permission permission = new Permission(requireContext(),requireActivity());

        mBinding.btnCompelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             completeAndDeleteWorkPlan(3,2);



            }
        });
        mBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAndDeleteWorkPlan(4,2);
            }
        });

        mBinding.callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorModel.getPhone() != null && !doctorModel.getPhone().isEmpty()) {
                    String PhoneString = "tel:" + doctorModel.getPhone();

                    Uri callUri = Uri.parse(PhoneString);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(callUri);
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        permission.getCallPermission();
                    } else {
                        startActivity(callIntent);

                    }
                } else {
                    Toast.makeText(requireContext(), "Phone Number Is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBinding.messageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorModel.getPhone() != null && !doctorModel.getPhone().isEmpty()) {
                    String smsPhoneString = "smsto:" + doctorModel.getPhone();
                    Uri smsUri = Uri.parse(smsPhoneString);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(smsUri);
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                       permission.getSMSPermission();
                    } else {
                        startActivity(smsIntent);

                    }
                } else {
                    Toast.makeText(requireContext(), "Phone Number Is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBinding.findCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doctorModel.getCoordinates() != null && !doctorModel.getCoordinates().isEmpty()) {
                    String locationString = "geo:" + doctorModel.getCoordinates();
                    Uri mapIntentUri = Uri.parse(locationString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Toast.makeText(requireContext(), "Location Coordinates Are Empty", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
// distance key for the user option , what type of distance filter he want to use
    private void completeAndDeleteWorkPlan(int key,int distanceKey) {
        String[] locationString = doctorModel.getCoordinates().split(",");


        Location workPlanLocation= new Location("");
        workPlanLocation.setLatitude(Double.parseDouble(locationString[0]));
        workPlanLocation.setLongitude(Double.parseDouble(locationString[1]));
        CustomLocation.CustomLocationResults locationResults = new CustomLocation.CustomLocationResults() {
            @Override
            public void gotLocation(Location location1) {
                location = location1;


                double distance= location.distanceTo(workPlanLocation);

                if (distanceKey==1)
                {

                    if (distance<=10)
                    {
                        saveWorkPlane(key);
                    }
                    else
                    {
                        new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                                .setContentText("You Are Out Of WorkPlan Location Radius")
                                .show();
                    }

                }
                else if (distanceKey==2)
                {
                    if (distance<=10)
                    {

                        saveWorkPlane(key);
                    }
                    else
                    {
                        new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("You Are Out Of WorkPlan Radius")
                                .setContentText("Do you want to Proceed.")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        saveWorkPlane(key);

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
                else if (distance==3)
                {
                    saveWorkPlane(key);

                }
            }
        };

        CustomLocation customLocation = new CustomLocation();
        customLocation.getLastLocation(getContext(), getActivity(), locationResults);





    }

    public void saveWorkPlane(int key)
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        CustomCompeleteDialogBinding dialogBinding = CustomCompeleteDialogBinding.inflate(getLayoutInflater());

        alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        alertDialog.show();
        dialogBinding.saveRemarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String check = dialogBinding.remarksEdittext.getText().toString();


                if (!check.equals("")) {
                    if (location != null) {
                        List<UpdateWorkPlanStatus> list = new ArrayList<>();
                        String remarks = dialogBinding.remarksEdittext.getText().toString();
                        int userId = SharedPreferenceHelper.getInstance(getActivity()).getUserId();
                        String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
                        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                        String dateString = dateFormat.format(date);
                        UpdateWorkPlanStatus updateWorkPlanStatus = new UpdateWorkPlanStatus();

                        updateWorkPlanStatus.setDoctor_Id(doctorModel.getDoctorId());
                        updateWorkPlanStatus.setEmp_Id(userId);
                        updateWorkPlanStatus.setRemarks(remarks);
                        updateWorkPlanStatus.setStatus(String.valueOf(key));
                        updateWorkPlanStatus.setVisit_On(String.valueOf(dateString));
                        updateWorkPlanStatus.setVisit_Cord(locationString);

                        if (doctorModel.getWorkId() > 0) {
                            updateWorkPlanStatus.setWork_Id(String.valueOf(doctorModel.getWorkId()));
                        }

                        list.add(updateWorkPlanStatus);
                        Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
                        call.enqueue(new Callback<UpdateStatus>() {
                            @Override
                            public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {

                                if (response.body() != null) {
                                    UpdateStatus updateStatus = response.body();
                                    Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();
                                    targetViewModel.DeleteDoctor(doctorModel);
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();

                                    openActivity(CONSTANTS.TARGET,CONSTANTS.COMPLETE,3);
                                    activityViewModel.getTaskActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
                                        @Override
                                        public void onChanged(List<Activity> activities) {

                                            taskActivities= activities;
                                            closeActivity();
                                        }
                                    });




                                }


                            }

                            @Override
                            public void onFailure(Call<UpdateStatus> call, Throwable t) {

                                t.getMessage();
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please turn on location", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                    progressDialog.dismiss();
                }
            }
        });
        dialogBinding.btnCloseCompeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    if (permission.isLocationEnabled())
                    {
                        CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                            @Override
                            public void gotLocation(Location location) {

                                String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

                                if(taskActivities!=null&&taskActivities.size()>0)
                                {
                                    for (Activity activity:taskActivities)
                                    {
                                        activity.setEndCoordinates(locationString);
                                        activity.setEndDateTime(formattedDate);

                                        String totalTime=calculateTotalTime(formattedDate,activity.getStartDateTime());
                                        activity.setTotalTime(totalTime);
                                        activityViewModel.updateActivity(activity);
                                        navController.popBackStack(R.id.targetPostMenuFragment,true);

                                    }
                                }



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
                    permission.getCOARSELocationPermission();
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
                        activity.setTaskID(taskID);
                        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                        activity.setStartCoordinates(locationString);
                        activityViewModel.insertActivity(activity);
                        progressDialog.dismiss();

                        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

                        NavigationView navigationView= requireActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

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
            permission.getCOARSELocationPermission();
        }


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