package com.example.myapplication.Target;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.telephony.TelephonyScanManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.myapplication.DoctorModel;
import com.example.myapplication.ModelClasses.AddNewWorkPlanModel;
import com.example.myapplication.ModelClasses.UpdateStatus;
import com.example.myapplication.ModelClasses.UpdateWorkPlanStatus;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.R;
import com.example.myapplication.Target.Adapters.DoctorMorningRecyclerAdapter;
import com.example.myapplication.Target.Adapters.DoctorEveningRecyclerAdapter;
import com.example.myapplication.Target.utils.DoctorViewModel;
import com.example.myapplication.utils.SyncDataToDB;
import com.example.myapplication.Target.utils.TargetViewModel;
import com.example.myapplication.databinding.CustomCancelDialogBinding;
import com.example.myapplication.databinding.CustomRescheduleDialogBinding;
import com.example.myapplication.databinding.FragmentTargetBinding;
import com.example.myapplication.databinding.TargetRecyclerViewBinding;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.RecyclerOnItemClickListener;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

public class TargetFragment extends Fragment {
    private TargetViewModel targetViewModel;
    private FragmentTargetBinding mbinding;
    private DoctorMorningRecyclerAdapter morningListAdapter;
    private DoctorEveningRecyclerAdapter eveningListAdapter;
    private int morningFlag = 0, eveningFlag = 0;
    private  Location location ;
    private AlertDialog alertDialog;
    private CustomLocation customLocation;
    private View viewRoot;
    private int selectedDay,selectedMonth,selectedYear;
    private String selectedDate="";
    private Calendar calendar ;
    private DoctorViewModel doctorViewModel;


    @RequiresApi(api = Build.VERSION_CODES.O)





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mbinding= FragmentTargetBinding.inflate(inflater,container,false);
        Log.e("onCreateView","on create View Me ha ");

         viewRoot = mbinding.getRoot();

         doctorViewModel= new ViewModelProvider(this).get(DoctorViewModel.class);
         doctorViewModel.deleteAllSchedule();

         calendar = Calendar.getInstance();

        return viewRoot;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        targetViewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        Log.e("onviewCreated","on view created me ha me ha ");

//        try {
//            SettingUpDatePicker();
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//        }

        setPullToFresh();


    }
    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume","on resume me ha ");

        if (isLocationEnabled())
        {

            BtnListener();

            setUpRecyclerView();

            SettingUpDatePicker();
            getDataFromViewModel();

        }
        else
        {


            new SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE)
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("onViewStateRestored","on view restored me ha");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","on start me ha");

    }



    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause","on pause me ha");

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop","on stop me ha");


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState"," on save instance me ha");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView"," on destroy view me ha");



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","on destroy me ha");

    }


    public void setUpRecyclerView()
    {

        morningListAdapter = new DoctorMorningRecyclerAdapter(getContext(), new RecyclerOnItemClickListener() {
            @Override
            public void GetDoctorOnClick(DoctorModel doctorModel, int position, int check) {

                if (check==1) {
                    setUpCancelWorkPlanAlertDialog(doctorModel, 3);

                }
                else if (check==2)
                {
                    setUpRescheduleWorkPlanAlertDialog(doctorModel, 3);
                }
                else if (check==10)
                {

                    TargetFragmentDirections.ActionTargetToTargetPostMenuFragment action = TargetFragmentDirections.
                            actionTargetToTargetPostMenuFragment(doctorModel);
                    Navigation.findNavController(viewRoot).navigate(action);
                }
            }
        },getActivity());
        eveningListAdapter = new DoctorEveningRecyclerAdapter(getContext(), new RecyclerOnItemClickListener() {
            @Override
            public void GetDoctorOnClick(DoctorModel doctorModel, int position, int check) {
                if (check==1) {
                    setUpCancelWorkPlanAlertDialog(doctorModel, 3);

                }
                else if (check==2)
                {
                    setUpRescheduleWorkPlanAlertDialog(doctorModel, 3);
                }
                else if (check==10)
                {

                    TargetFragmentDirections.ActionTargetToTargetPostMenuFragment action = TargetFragmentDirections.
                            actionTargetToTargetPostMenuFragment(doctorModel);
                    Navigation.findNavController(viewRoot).navigate(action);

                }
            }
        },getActivity());

        mbinding.targetRecycler.docListmorningRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mbinding.targetRecycler.docListRecyclerEvening.setLayoutManager(new LinearLayoutManager(getActivity()));
        eveningListAdapter.notifyDataSetChanged();
        morningListAdapter.notifyDataSetChanged();
    }
    private void SettingUpDatePicker() {


        Calendar cldr = Calendar.getInstance();


         selectedDay = (cldr.get(Calendar.DAY_OF_MONTH));
         selectedMonth= cldr.get(Calendar.MONTH);
         selectedYear= cldr.get(Calendar.YEAR);

        int checkmonth= (selectedMonth%10);
        int checkday = (selectedDay%10);
        String mDay = null,mMonth = null,mYear= String.valueOf(selectedYear);
        if (checkmonth>0 && selectedMonth<10 ) {
            mMonth = "0"+(selectedMonth+1);
            //          date = day + "-" + "0" + (month + 1) + "-" + (year);
        }
        else
        {
            mMonth=String.valueOf(selectedMonth+1);

        }

        if (checkday>0 && selectedDay<10){
            mDay = "0" + (selectedDay);

        }
        else
        {
            mDay= String.valueOf(selectedDay);

        }

        mbinding.datePickerTimeline.setInitialDate(selectedYear, selectedMonth-1 , selectedDay-1);




        mbinding.datePickerTimeline.setSelected(true);
        mbinding.datePickerTimeline.requestFocus();
        String date = mDay + "-" + mMonth+ "-" +mYear;

        Log.e("DateTag","Date is:"+date);
        if (selectedDate.equals(""))
        {
            cldr.set(selectedYear,selectedMonth,selectedDay);

            mbinding.datePickerTimeline.setActiveDate(cldr);
            getTargetViewModelData(date);

        }
        else
        {
            getTargetViewModelData(selectedDate);
            calendar.set(selectedYear,selectedMonth,selectedDay);
            mbinding.datePickerTimeline.setActiveDate(calendar);


        }

    }


    public void getDataFromViewModel( )
    {



        mbinding.datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                String date;
                selectedDay=day;
                selectedMonth= month;
                selectedYear = year;
                int checkmonth= (month%10);
                int checkday = (day%10);
                String mDay = null,mMonth = null,mYear= String.valueOf(year);
                if (checkmonth>0 && month<10 ) {
                    mMonth = "0"+(month+1);
           //          date = day + "-" + "0" + (month + 1) + "-" + (year);
                }
                else
                {
                    mMonth=String.valueOf(month+1);

                }

                    if (checkday>0 && day<10){
                    mDay = "0" + (day);

                }
                else
                {
                    mDay= String.valueOf(day);

                }
                date = mDay + "-" + (mMonth) + "-" + (mYear);

                selectedDate= date;
                Log.d("date", "date select ho rai hai");

                calendar.set(year,month,day);
                mbinding.datePickerTimeline.setActiveDate(calendar);
                getTargetViewModelData(date);

            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

            }
        });


    }

    public void getTargetViewModelData( String date)
    {

        targetViewModel.getAllEveningDoctorsByDate(date).observe(getViewLifecycleOwner(), new Observer<List<DoctorModel>>() {
            @Override
            public void onChanged(List<DoctorModel> list) {
                if (list!=null)
                {
                    mbinding.targetRecycler.docListRecyclerEvening.setAdapter(eveningListAdapter);

                    eveningListAdapter.setDoctorModelList(list);
                    eveningListAdapter.notifyDataSetChanged();
                }
                else
                {
                    eveningListAdapter.clearData();

                }
            }
        });


        targetViewModel.getAllMorningDoctorsByDate(date).observe(getViewLifecycleOwner(), new Observer<List<DoctorModel>>() {
            @Override
            public void onChanged(List<DoctorModel> list) {
                if (list!=null)
                {
                    mbinding.targetRecycler.docListmorningRecycler.setAdapter(morningListAdapter);
                    morningListAdapter.setDoctorModelList(list);
                    morningListAdapter.notifyDataSetChanged();
                }
                else
                {
                    morningListAdapter.clearData();
                }

            }
        });


//        targetViewModel.getAllEveningDoctorLiveData().observe(requireActivity(), new Observer<List<DoctorModel>>() {
//            @Override
//            public void onChanged(List<DoctorModel> list) {
//                List<DoctorModel> filterListevening= new ArrayList<>();
//
//                String Date;
//
//                for (DoctorModel model:list)
//                {
//                    Date = model.getWorkDate();
//
//                    if (Date.equals(date) && model.getShift().equals("Evening"))
//                    {
//                        filterListevening.add(model);
//                    }
//                }
//                if (filterListevening.size()>0) {
//                    mbinding.targetRecycler.docListRecyclerEvening.setAdapter(eveningListAdapter);
//
//                    eveningListAdapter.setDoctorModelList(filterListevening);
//                    eveningListAdapter.notifyDataSetChanged();
//                }
//                else
//                {
//                    eveningListAdapter.clearData();
//
//                }
//
//            }
//        });
//        targetViewModel.getAllMorningDoctorLiveData().observe(requireActivity(), new Observer<List<DoctorModel>>() {
//            @Override
//            public void onChanged(List<DoctorModel> list) {
//                List<DoctorModel> filterList= new ArrayList<>();
//
//
//                String Date;
//
//                for (DoctorModel model:list)
//                {
//                    Date= model.getWorkDate();
//                    if (Date.equals(date)&& model.getShift().equals("Morning"))
//                    {
//                        filterList.add(model);
//                    }
//                }
//                if (filterList.size()>0) {
//                    mbinding.targetRecycler.docListmorningRecycler.setAdapter(morningListAdapter);
//                    morningListAdapter.setDoctorModelList(filterList);
//                    morningListAdapter.notifyDataSetChanged();
//                }
//                else
//                {
//                    morningListAdapter.clearData();
//
//                }
//
//
//
//
//            }
//        });
    }
    public void BtnListener()
    {

        mbinding.targetRecycler.morningDoctorShowlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morningFlag ==0) {

                    morningListAdapter.setFlag(1);
                    morningFlag =1;
                    float deg = (mbinding.targetRecycler.morningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.morningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }
                else
                {
                    morningListAdapter.setFlag(0);
                    morningFlag =0;
                    float deg = (mbinding.targetRecycler.morningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.morningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }
            }
        });

        mbinding.targetRecycler.eveningDoctorShowlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eveningFlag==0)
                {
                    eveningListAdapter.setFlag(1);
                    eveningFlag=1;
                    float deg = (mbinding.targetRecycler.eveningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.eveningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }
                else
                {
                    eveningListAdapter.setFlag(0);
                    eveningFlag=0;
                    float deg = (mbinding.targetRecycler.eveningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.eveningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }

            }
        });

        mbinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_target_fragment_to_addworkPlanFragment);
            }
        });
//

    }


    public void setPullToFresh()
    {

            mbinding.targetRecycler.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (isLocationEnabled()) {
                        mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                        int id = SharedPreferenceHelper.getInstance(getContext()).getUserId();

                        targetViewModel.DeleteAllDoctor();
                        SyncDataToDB syncDataToDB = new SyncDataToDB(requireActivity().getApplication(),requireContext());
                        String errorMessage= syncDataToDB.saveDoctorsList(id);
                        if (!errorMessage.equals(""))
                        {

                        }
                    }
                    else
                    {

                        new SweetAlertDialog(requireContext())
                                .setTitleText("Please turn on  location for this action.")
                                .setContentText("Do you want to open location setting.")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mbinding.targetRecycler.swipeLayout.setRefreshing(false);

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
                                        mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                                    }
                                }).show();
                    }

                }

            });

    }

    public void setUpCancelWorkPlanAlertDialog(DoctorModel doctorModel, int key)
    {

        if (isLocationEnabled())
        {
            customLocation = new CustomLocation();
            CustomCancelDialogBinding dialogBinding = CustomCancelDialogBinding.inflate(getLayoutInflater());

            alertDialog= new AlertDialog.Builder(getContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
            alertDialog.show();
            CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                @Override
                public void gotLocation(Location location1) {
                    location=location1;
                }
            };

            customLocation.getLastLocation(getContext(),getActivity(),locationResults);
            dialogBinding.saveRemarksBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    String check=dialogBinding.remarksEdittext.getText().toString();


                    if (!check.equals("") ) {
                        if (location != null) {
                            List<UpdateWorkPlanStatus> list = new ArrayList<>();
                            String remarks = dialogBinding.remarksEdittext.getText().toString();
                            int userId = SharedPreferenceHelper.getInstance(getActivity()).getUserId();
                            String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
                            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                            Date date =  Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
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
                                        eveningListAdapter.removeItem(doctorModel);
                                        morningListAdapter.removeItem(doctorModel);

                                        targetViewModel.DeleteDoctor(doctorModel);

                                        alertDialog.dismiss();
                                    }


                                }

                                @Override
                                public void onFailure(Call<UpdateStatus> call, Throwable t) {

                                    t.getMessage();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getActivity(),"Please turn on location",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                    }
                }
            });

            dialogBinding.btnCloseCancelDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
        else
        {
            new SweetAlertDialog(requireContext())
                    .setTitleText("Please turn on  location for this action.")
                    .setContentText("Do you want to open location setting.")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);

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
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                        }
                    }).show();
        }


    }


    public void setUpRescheduleWorkPlanAlertDialog(DoctorModel doctorModel, int key)
    {
        CustomRescheduleDialogBinding dialogBinding = CustomRescheduleDialogBinding.inflate(getLayoutInflater());

         alertDialog= new AlertDialog.Builder(getContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        alertDialog.show();

        dialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String remarkCheck= dialogBinding.remarksEdittext.getText().toString();
                String dateCheck= dialogBinding.dateEditText.getText().toString();
                String timeCheck= dialogBinding.timeEditText.getText().toString();

                if (!remarkCheck.equals("") && !dateCheck.equals("") && !timeCheck.equals("") ) {
                    int userId = SharedPreferenceHelper.getInstance(getActivity()).getUserId();
                    String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
                    AddNewWorkPlanModel addNewWorkPlanModel= new AddNewWorkPlanModel();
                    addNewWorkPlanModel.setDoctorId(doctorModel.getDoctorId());
                    addNewWorkPlanModel.setEmpId(userId);
                    addNewWorkPlanModel.setRemarks1(remarkCheck);
                    String dateTime = dateCheck+" "+timeCheck;
                    addNewWorkPlanModel.setWorkFromDate(dateTime);
                    addNewWorkPlanModel.setWorkToDate(dateTime);
                    addNewWorkPlanModel.setWorkPlan("Meeting");
                    addNewWorkPlanModel.setWorkId(doctorModel.getWorkId());


                    Call<UpdateStatus> call = ApiClient.getInstance().getApi().RescheduleWorkPlan(token,addNewWorkPlanModel);
                    call.enqueue(new Callback<UpdateStatus>() {
                        @Override
                        public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {

                            if (response.body() != null) {




                                UpdateStatus updateStatus= response.body();
                                Toast.makeText(getContext(),updateStatus.getStrMessage(),Toast.LENGTH_LONG).show();
//                                eveningListAdapter.removeItem(doctorModel);
//                                morningListAdapter.removeItem(doctorModel);
//
//                                targetViewModel.DeleteDoctor(doctorModel);

                                alertDialog.dismiss();
                            }





                        }

                        @Override
                        public void onFailure(Call<UpdateStatus> call, Throwable t) {

                            t.getMessage();
                        }
                    });
                }
                else
                {
                    dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                }
            }
        });

        dialogBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}