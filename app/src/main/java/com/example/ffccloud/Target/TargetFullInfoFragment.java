package com.example.ffccloud.Target;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.ModelClasses.Activity;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.ModelClasses.UpdateWorkPlanStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.Target.utils.TargetViewModel;

import com.example.ffccloud.databinding.CustomCompeleteDialogBinding;
import com.example.ffccloud.databinding.CustomSelectActivityDialogBinding;
import com.example.ffccloud.databinding.FragmentTargetFullInfoBinding;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.Permission;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.SyncDataToDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

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
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayAdapter<String> stageAdapter,statusAdapter;
    private String hostRemarks="",empRemarks="",productsRemarks="";
    private int interestLevel=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentTargetFullInfoBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        view = mBinding.getRoot();

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        targetViewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        bottomSheetBehavior= BottomSheetBehavior.from(mBinding.feedbackBottomSheet.getRoot());

        setUpBottomSheet();

        setInfoWorkPlan();
        btnListener();
        bottomSheetBtnListener();
    }

    private void setUpBottomSheet() {

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        mBinding.feedbackBottomSheet.headerBtn.setImageDrawable(drawable);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        mBinding.feedbackBottomSheet.headerBtn.setImageDrawable(drawable);

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        mBinding.feedbackBottomSheet.headerBtn.setImageDrawable(drawable);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        stageAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.stage));
        statusAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.status));
        mBinding.feedbackBottomSheet.autoStage.setAdapter(stageAdapter);
        mBinding.feedbackBottomSheet.autoStatus.setAdapter(statusAdapter);





    }

    public void bottomSheetBtnListener()
    {
        mBinding.feedbackBottomSheet.voiceRecordBtnHostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.feedbackBottomSheet.voiceRecordBtnEmpRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 2);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.feedbackBottomSheet.voiceRecordBtnRemarksProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 3);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.feedbackBottomSheet.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Save Successfully. ")
                        .setContentText("You want to create another activity")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                showSelectActivityDialog();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("NO")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });


        mBinding.feedbackBottomSheet.interestLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton= mBinding.feedbackBottomSheet.getRoot().findViewById(checkedId);
                String check = radioButton.getText().toString();
                switch (check)
                {
                    case "Low":
                    {
                     if (radioButton.isChecked())
                     {
                         interestLevel=1;
                         mBinding.feedbackBottomSheet.radioButtonLow.setBackgroundColor(getResources().getColor(R.color.APP_Theme_Color));
                         mBinding.feedbackBottomSheet.radioButtonMedium.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                         mBinding.feedbackBottomSheet.radioButtonHigh.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                     }

                        break;
                    }
                    case "Medium":
                    {
                        if (radioButton.isChecked())
                        {
                            interestLevel=2;
                            mBinding.feedbackBottomSheet.radioButtonMedium.setBackgroundColor(getResources().getColor(R.color.APP_Theme_Color));
                            mBinding.feedbackBottomSheet.radioButtonLow.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                            mBinding.feedbackBottomSheet.radioButtonHigh.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                        }

                        break;
                    }
                    case "High":
                    {
                        if (radioButton.isChecked())
                        {
                            interestLevel=3;
                            mBinding.feedbackBottomSheet.radioButtonMedium.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                            mBinding.feedbackBottomSheet.radioButtonLow.setBackgroundColor(getResources().getColor(R.color.Light_APP_Theme_Color));
                            mBinding.feedbackBottomSheet.radioButtonHigh.setBackgroundColor(getResources().getColor(R.color.APP_Theme_Color));
                        }

                        break;
                    }
                }
            }
        });
    }

    public void showSelectActivityDialog()
    {
        CustomSelectActivityDialogBinding dialogBinding= CustomSelectActivityDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        alertDialog.show();

        ArrayList<String> activityArrayList= new ArrayList<>();
        activityArrayList.add(CONSTANTS.ACTIVITY_CALL);
        activityArrayList.add(CONSTANTS.ACTIVITY_MEETING);
        activityArrayList.add(CONSTANTS.ACTIVITY_FOLLOWUP);
        ArrayAdapter<String> activityArrayAdapter= new ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,activityArrayList);

        dialogBinding.activtyDropDown.setAdapter(activityArrayAdapter);

        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogBinding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !dialogBinding.activtyDropDown.getText().toString().equals("Activity Type") )
                {
                    alertDialog.dismiss();

                    TargetFullInfoFragmentDirections.ActionTargetPostMenuFragmentToActivityTaskFragment action = TargetFullInfoFragmentDirections.actionTargetPostMenuFragmentToActivityTaskFragment();
                    action.setActivityType(dialogBinding.activtyDropDown.getText().toString());
                    navController.navigate(action);
                }

            }
        });


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

        mBinding.historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long doc_id= doctorModel.getDoctorId();

                TargetFullInfoFragmentDirections.ActionTargetPostMenuFragmentToNavHistory action = TargetFullInfoFragmentDirections.actionTargetPostMenuFragmentToNavHistory();
                action.setDocId(doc_id);
                navController.navigate(action);
            }
        });
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

        CustomLocation customLocation = new CustomLocation(requireContext());
        customLocation.getLastLocation(locationResults);





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
                        int userId = SharedPreferenceHelper.getInstance(getActivity()).getEmpID();
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
                                else
                                {
                                    new SyncDataToDB(requireActivity().getApplication(),requireContext()).loginAgain(response.message());
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


                CustomLocation customLocation= new CustomLocation(requireContext());

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
                                Location startLocation = new Location("");

                                if(taskActivities!=null&&taskActivities.size()>0)
                                {
                                    for (Activity activity:taskActivities)
                                    {

                                        if (activity.getStartCoordinates()!=null&& !activity.getStartCoordinates().isEmpty())
                                        {
                                            String[] locationStringArray = activity.getStartCoordinates().split(",");
                                            startLocation.setLatitude(Double.parseDouble(locationStringArray[0]));
                                            startLocation.setLongitude(Double.parseDouble(locationStringArray[1]));
                                            String endAddress = customLocation.getCompleteAddressString(location.getLatitude(),location.getLongitude());
                                            String distance= String.valueOf(location.distanceTo(startLocation));
                                            activity.setEndAddress(endAddress);
                                            activity.setDistance(distance);

                                        }
                                        else
                                        {
                                            activity.setEndAddress("");
                                            activity.setDistance("");
                                        }




                                        String endAddress = customLocation.getCompleteAddressString(location.getLatitude(),location.getLongitude());
                                        activity.setEndAddress(endAddress);
                                        activity.setEndCoordinates(locationString);
                                        activity.setEndDateTime(formattedDate);

                                        String totalTime=calculateTotalTime(formattedDate,activity.getStartDateTime());
                                        activity.setTotalTime(totalTime);
                                        activityViewModel.updateActivity(activity);
                                        navController.popBackStack(R.id.target_fragment,false);

                                    }
                                }



                            }
                        };

                        customLocation.getLastLocation(locationResults);
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

    public void openActivity(String mainActivity,String subActivity,int taskID)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                hostRemarks= hostRemarks+" "+result.get(0);
                mBinding.feedbackBottomSheet.hostReviewRemarksEdittext.setText(hostRemarks);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                empRemarks = empRemarks+" "+result.get(0);
                mBinding.feedbackBottomSheet.empRemarks.setText(empRemarks);
            }
        }
        else if (requestCode == 3) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                productsRemarks = productsRemarks+" "+result.get(0);
                mBinding.feedbackBottomSheet.remarksAboutProdactEdittext.setText(productsRemarks);
            }
        }
    }
}