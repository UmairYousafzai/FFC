package com.example.ffccloud.Target;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.ModelClasses.AddNewWorkPlanModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.ModelClasses.UpdateWorkPlanStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.Target.Adapters.DoctorMorningRecyclerAdapter;
import com.example.ffccloud.Target.Adapters.DoctorEveningRecyclerAdapter;
import com.example.ffccloud.Target.utils.DoctorViewModel;
import com.example.ffccloud.databinding.CustomCancelDialogBinding;
import com.example.ffccloud.databinding.CustomRescheduleDialogBinding;
import com.example.ffccloud.databinding.FragmentTargetBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SaveData;
import com.example.ffccloud.utils.SyncDataToDB;
import com.example.ffccloud.Target.utils.TargetViewModel;

import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.RecyclerOnItemClickListener;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.worker.UploadWorker;
import com.example.ffccloud.worker.utils.UploadDataRepository;
import com.vivekkaushik.datepicker.OnDateSelectedListener;
//import com.vivekkaushik.datepicker.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class TargetFragment extends Fragment {
    private TargetViewModel targetViewModel;
    private FragmentTargetBinding mbinding;
    private DoctorMorningRecyclerAdapter morningListAdapter;
    private DoctorEveningRecyclerAdapter eveningListAdapter;
    private int morningFlag = 0, eveningFlag = 0;
    private Location location;
    private AlertDialog alertDialog;
    private int selectedDay, selectedMonth, selectedYear;
    private String selectedDate = "", remarksForCancel = "", remarksForReschedule = "";
    private Calendar calendar;
    private final boolean mic_check = false;
    private CustomCancelDialogBinding dialogBinding;
    private CustomRescheduleDialogBinding rescheduleDialogBinding;
    private ProgressDialog progressDialog;
    private NavController navController;
    private UploadDataRepository uploadDataRepository;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mbinding = FragmentTargetBinding.inflate(inflater, container, false);
        Log.e("onCreateView", "on create View Me ha ");

        View viewRoot = mbinding.getRoot();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        DoctorViewModel doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
        doctorViewModel.deleteAllSchedule();
        navController = NavHostFragment.findNavController(this);
        calendar = Calendar.getInstance();

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        targetViewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        uploadDataRepository = new UploadDataRepository(requireContext());
        Log.e("onviewCreated", "on view created me ha me ha ");

//        try {
//            SettingUpDatePicker();
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//        }


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume", "on resume me ha ");

        if (isLocationEnabled()) {

            BtnListener();

            setUpRecyclerView();

            SettingUpDatePicker();
            getDataFromViewModel();
            setPullToFresh();


        } else {


            CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());


        }
    }


    public void setUpRecyclerView() {

        morningListAdapter = new DoctorMorningRecyclerAdapter(getContext(), new RecyclerOnItemClickListener() {
            @Override
            public void GetDoctorOnClick(DoctorModel doctorModel, int position, int check) {

                if (check == 1) {
                    setUpCancelWorkPlanAlertDialog(doctorModel, 3);


                } else if (check == 2) {
                    setUpRescheduleWorkPlanAlertDialog(doctorModel, 3);
                } else if (check == 10) {

                    TargetFragmentDirections.ActionTargetToTargetPostMenuFragment action = TargetFragmentDirections.
                            actionTargetToTargetPostMenuFragment(doctorModel);
                    navController.navigate(action);

                }
            }
        }, getActivity());
        eveningListAdapter = new DoctorEveningRecyclerAdapter(getContext(), new RecyclerOnItemClickListener() {
            @Override
            public void GetDoctorOnClick(DoctorModel doctorModel, int position, int check) {
                if (check == 1) {
                    setUpCancelWorkPlanAlertDialog(doctorModel, 3);

                } else if (check == 2) {
                    setUpRescheduleWorkPlanAlertDialog(doctorModel, 3);
                } else if (check == 10) {

                    TargetFragmentDirections.ActionTargetToTargetPostMenuFragment action = TargetFragmentDirections.
                            actionTargetToTargetPostMenuFragment(doctorModel);
                    navController.navigate(action);

                }
            }
        }, getActivity());

        mbinding.targetRecycler.docListmorningRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mbinding.targetRecycler.docListRecyclerEvening.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DoctorModel doctorModel= new DoctorModel();
//        doctorModel.setAddress("lahore");
//        doctorModel.setClassification("Abc");
//        doctorModel.setDistance("29");
//        doctorModel.setEmail("abc@gmail.com");
//        doctorModel.setName("khan");
//        List<DoctorModel> doctorModels= new ArrayList<>();
//        doctorModels.add(doctorModel);
        mbinding.targetRecycler.docListRecyclerEvening.setAdapter(eveningListAdapter);
        mbinding.targetRecycler.docListmorningRecycler.setAdapter(morningListAdapter);

//        eveningListAdapter.setDoctorModelList(doctorModels);
        eveningListAdapter.notifyDataSetChanged();
        morningListAdapter.notifyDataSetChanged();
    }

    private void SettingUpDatePicker() {


        Calendar cldr = Calendar.getInstance();


        selectedDay = (cldr.get(Calendar.DAY_OF_MONTH));
        selectedMonth = cldr.get(Calendar.MONTH);
        int month = selectedMonth+1;
        selectedYear = cldr.get(Calendar.YEAR);

        int checkmonth = (month % 10);
        int checkday = (selectedDay % 10);
        String mDay, mMonth, mYear = String.valueOf(selectedYear);
        if (checkmonth > 0 && selectedMonth < 9) {
            mMonth = "0" + (month );
            //          date = day + "-" + "0" + (month + 1) + "-" + (year);
        } else {
            mMonth = String.valueOf(selectedMonth );

        }

        if (checkday > 0 && selectedDay < 10) {
            mDay = "0" + (selectedDay);

        } else {
            mDay = String.valueOf(selectedDay);

        }

        mbinding.datePickerTimeline.setInitialDate(selectedYear, selectedMonth - 1, selectedDay - 1);


        mbinding.datePickerTimeline.setSelected(true);
        mbinding.datePickerTimeline.requestFocus();
        String date = mDay + "/" + mMonth + "/" + mYear;
        Log.e("DateTag", "Date is:" + date);
        if (selectedDate.equals("")) {
            cldr.set(selectedYear, selectedMonth, selectedDay);

            mbinding.datePickerTimeline.setActiveDate(cldr);
            getTargetViewModelData(date);

        } else {
            getTargetViewModelData(selectedDate);
            calendar.set(selectedYear, selectedMonth, selectedDay);
            mbinding.datePickerTimeline.setActiveDate(calendar);


        }

    }


    public void getDataFromViewModel() {


        mbinding.datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                String date;
                selectedDay = day;
                selectedMonth = month+1;
                selectedYear = year;
                int checkmonth = (month+1 % 10);
                int checkday = (day % 10);
                String mDay = null, mMonth = null, mYear = String.valueOf(year);
                if (checkmonth > 0 && month < 9) {
                    mMonth = "0" + (month + 1);
                    //          date = day + "-" + "0" + (month + 1) + "-" + (year);
                } else {
                    mMonth = String.valueOf(month + 1);

                }

                if (checkday > 0 && day < 9) {
                    mDay = "0" + (day);

                } else {
                    mDay = String.valueOf(day);

                }
                date = mDay + "/" + mMonth + "/" + mYear;

                selectedDate = date;
                Log.d("date", "date select ho rai hai");

                calendar.set(year, month, day);
                mbinding.datePickerTimeline.setActiveDate(calendar);
                getTargetViewModelData(date);

            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

            }
        });


    }

    public void getTargetViewModelData(String date) {

//        targetViewModel.getAllEveningDoctorsByDate(date).observe(getViewLifecycleOwner(), new Observer<List<DoctorModel>>() {
//            @Override
//            public void onChanged(List<DoctorModel> list) {
//                if (list.size() > 0) {
//
//                    eveningListAdapter.setDoctorModelList(list);
//                } else {
//                    eveningListAdapter.clearData();
//
//                }
//            }
//        });


        targetViewModel.getAllMorningDoctorsByDate(date).observe(getViewLifecycleOwner(), new Observer<List<DoctorModel>>() {
            @Override
            public void onChanged(List<DoctorModel> list) {
                if (list.size() > 0) {
                    morningListAdapter.setDoctorModelList(list);
                    morningListAdapter.notifyDataSetChanged();
                } else {
                    morningListAdapter.clearData();
                }

            }
        });


    }

    public void BtnListener() {

        mbinding.targetRecycler.morningDoctorShowlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morningFlag == 0) {

                    morningListAdapter.setFlag(1);
                    morningFlag = 1;
                    float deg = (mbinding.targetRecycler.morningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.morningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                } else {
                    morningListAdapter.setFlag(0);
                    morningFlag = 0;
                    float deg = (mbinding.targetRecycler.morningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.morningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }
            }
        });

        mbinding.targetRecycler.eveningDoctorShowlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eveningFlag == 0) {
                    eveningListAdapter.setFlag(1);
                    eveningFlag = 1;
                    float deg = (mbinding.targetRecycler.eveningDoctorShowlistBtn.getRotation() == 180F) ? 0F : 180F;
                    mbinding.targetRecycler.eveningDoctorShowlistBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                } else {
                    eveningListAdapter.setFlag(0);
                    eveningFlag = 0;
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


    public void setPullToFresh() {

        mbinding.targetRecycler.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLocationEnabled()) {
                    List<UpdateWorkPlanStatus> updateWorkPlanStatusList = new ArrayList<>();
                    updateWorkPlanStatusList = uploadDataRepository.getAllWorkPlanStatus();
                    List<AddNewWorkPlanModel> addNewWorkPlanModelList = new ArrayList<>();
                    addNewWorkPlanModelList = uploadDataRepository.getAllWorkPlan();
                    if (isNetworkConnected())
                    {
                        if (updateWorkPlanStatusList.size()>0)
                        {
                            SaveData.getInstance(requireContext()).SaveWorkPlanStatus();
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                        }
                        else
                        {
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                            int id = SharedPreferenceHelper.getInstance(getContext()).getEmpID();

                            targetViewModel.DeleteAllDoctor();
//                           SyncDataToDB.getInstance().saveDoctorsList(id,requireContext(),requireActivity());
                        }

                        if (addNewWorkPlanModelList.size()>0)
                        {
                            SaveData.getInstance(requireContext()).saveWorkPlan();
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                        }
                        else
                        {
                            if (updateWorkPlanStatusList.size()>0)
                            {
                                mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                                int id = SharedPreferenceHelper.getInstance(getContext()).getEmpID();

                                targetViewModel.DeleteAllDoctor();

//                                SyncDataToDB.getInstance().saveDoctorsList(id,requireContext(),requireActivity());
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                    }


                } else {

                   CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
                }

            }

        });

    }

    public void setUpCancelWorkPlanAlertDialog(DoctorModel doctorModel, int key) {

        if (isLocationEnabled()) {
            CustomLocation customLocation = new CustomLocation(requireContext());
            dialogBinding = CustomCancelDialogBinding.inflate(getLayoutInflater());
            alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
            alertDialog.show();
            CustomLocation.CustomLocationResults locationResults = new CustomLocation.CustomLocationResults() {
                @Override
                public void gotLocation(Location location1) {
                    location = location1;
                }
            };


            dialogBinding.remarksEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    remarksForCancel = s.toString();

                }
            });
            dialogBinding.speechToTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent
                            = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                    try {
                        startActivityForResult(intent, CONSTANTS.REQUEST_CODE_SPEECH_INPUT_FOR_CANCEL_DIALOG);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            customLocation.getLastLocation(locationResults);
            dialogBinding.saveRemarksBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialogBinding.saveRemarksBtn.setEnabled(false);
                    String check = dialogBinding.remarksEdittext.getText().toString();
                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Loading....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

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
                            if (isNetworkConnected()) {
                                Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
                                call.enqueue(new Callback<UpdateStatus>() {
                                    @Override
                                    public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                                        if (response.body() != null) {


                                            UpdateStatus updateStatus = response.body();
                                            Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();
                                            eveningListAdapter.removeItem(doctorModel);
                                            morningListAdapter.removeItem(doctorModel);
                                            dialogBinding.saveRemarksBtn.setEnabled(true);

                                            targetViewModel.DeleteDoctor(doctorModel);
                                            progressDialog.dismiss();
                                            alertDialog.dismiss();
                                        } else {
                                            progressDialog.dismiss();
                                        }


                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                                        progressDialog.dismiss();
                                        t.getMessage();
                                    }
                                });
                            } else {
                                eveningListAdapter.removeItem(doctorModel);
                                morningListAdapter.removeItem(doctorModel);
                                dialogBinding.saveRemarksBtn.setEnabled(true);

                                targetViewModel.DeleteDoctor(doctorModel);
                                progressDialog.dismiss();
                                alertDialog.dismiss();
                                uploadDataRepository.insertWorkPlanStatus(updateWorkPlanStatus);
                                generateWorkRequest(CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN);
                            }

                        } else {
                            Toast.makeText(getActivity(), "Please turn on location", Toast.LENGTH_LONG).show();
                        }
                    } else {
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
        } else {

            CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
        }


    }


    public void setUpRescheduleWorkPlanAlertDialog(DoctorModel doctorModel, int key) {
        rescheduleDialogBinding = CustomRescheduleDialogBinding.inflate(getLayoutInflater());

        alertDialog = new AlertDialog.Builder(requireContext()).setView(rescheduleDialogBinding.getRoot()).setCancelable(false).create();
        alertDialog.show();
        rescheduleDialogBinding.speechToTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, CONSTANTS.REQUEST_CODE_SPEECH_INPUT_FOR_RESCHEDULE_DIALOG);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        rescheduleDialogBinding.remarksEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remarksForReschedule = remarksForReschedule + s.toString();

            }
        });

        rescheduleDialogBinding.textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int myear = calendar.get(Calendar.YEAR);
                int mday = calendar.get(Calendar.DAY_OF_MONTH);
                int mmonth = calendar.get(Calendar.MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        if ((dayOfMonth - mday) >= 0 && (month - mmonth) >= 0) {

                            int checkMonth = month % 10, checkday = (dayOfMonth % 10);
                            ;
                            String mMonth, mDay;
                            if (checkMonth > 0 && month < 10) {
                                mMonth = "0" + (month + 1);
                            } else {
                                mMonth = String.valueOf(month + 1);

                            }

                            if (checkday > 0 && dayOfMonth < 10) {
                                mDay = "0" + (dayOfMonth);

                            } else {
                                mDay = String.valueOf(dayOfMonth);

                            }
                            String mDate = mMonth + "/" + mDay + "/" + year;
                            rescheduleDialogBinding.textDate.setText(mDate);

                        } else {
                            rescheduleDialogBinding.textDate.setError("Select date forward from current date");
                        }


                    }
                }, myear, mmonth, mday);
                datePickerDialog.show();
            }
        });

        rescheduleDialogBinding.textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String openingTime = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        rescheduleDialogBinding.textTime.setText(openingTime);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        rescheduleDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    rescheduleDialogBinding.saveBtn.setEnabled(false);
                    String remarkCheck = rescheduleDialogBinding.remarksEdittext.getText().toString();
                    String dateCheck = rescheduleDialogBinding.textDate.getText().toString();
                    String timeCheck = rescheduleDialogBinding.textTime.getText().toString();
                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Loading....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (!remarkCheck.equals("") && !dateCheck.equals("") && !timeCheck.equals("")) {
                        int userId = SharedPreferenceHelper.getInstance(getActivity()).getEmpID();
                        String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
                        AddNewWorkPlanModel addNewWorkPlanModel = new AddNewWorkPlanModel();
                        addNewWorkPlanModel.setDoctorId(doctorModel.getDoctorId());
                        addNewWorkPlanModel.setEmpId(userId);
                        addNewWorkPlanModel.setRemarks1(remarkCheck);
                        String dateTime = dateCheck + " " + timeCheck;
                        addNewWorkPlanModel.setWorkFromDate(dateTime);
                        addNewWorkPlanModel.setWorkToDate(dateTime);
                        addNewWorkPlanModel.setWorkPlan("Meeting");
                        addNewWorkPlanModel.setWorkId(doctorModel.getWorkId());

                        if (isNetworkConnected())
                        {
                            Call<UpdateStatus> call = ApiClient.getInstance().getApi().RescheduleWorkPlan(token, addNewWorkPlanModel);
                            call.enqueue(new Callback<UpdateStatus>() {
                                @Override
                                public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                                    if (response.body() != null) {


                                        UpdateStatus updateStatus = response.body();
                                        Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();

//
                                        targetViewModel.DeleteDoctor(doctorModel);
                                        morningListAdapter.removeItem(doctorModel);
                                        eveningListAdapter.removeItem(doctorModel);
                                        rescheduleDialogBinding.saveBtn.setEnabled(true);
                                        progressDialog.dismiss();
                                        alertDialog.dismiss();
                                    }else
                                    {
                                        progressDialog.dismiss();

                                    }


                                }

                                @Override
                                public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                                    progressDialog.dismiss();
                                    t.getMessage();
                                }
                            });
                        }
                        else
                        {
                            targetViewModel.DeleteDoctor(doctorModel);
                            morningListAdapter.removeItem(doctorModel);
                            eveningListAdapter.removeItem(doctorModel);
                            rescheduleDialogBinding.saveBtn.setEnabled(true);
                            progressDialog.dismiss();
                            alertDialog.dismiss();
                            uploadDataRepository.insertWorkPlan(addNewWorkPlanModel);
                            generateWorkRequest(CONSTANTS.WORK_REQUEST_RESCHEDULE_WORK_PLAN);
                        }


                    } else {
                        dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                        progressDialog.dismiss();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        rescheduleDialogBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANTS.REQUEST_CODE_SPEECH_INPUT_FOR_CANCEL_DIALOG) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                remarksForCancel = remarksForCancel + " " + result.get(0);
                dialogBinding.remarksEdittext.setText(remarksForCancel);
            }
        } else if (requestCode == CONSTANTS.REQUEST_CODE_SPEECH_INPUT_FOR_RESCHEDULE_DIALOG) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                remarksForReschedule = remarksForReschedule + " " + result.get(0);
                rescheduleDialogBinding.remarksEdittext.setText(remarksForReschedule);
            }
        }
    }


    public boolean isNetworkConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        } else {
            return connected = false;
        }

    }

    private void generateWorkRequest(String workRequest) {

        Data mainData;
        if (workRequest.equals(CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN))
        {

            mainData= new Data.Builder()
                    .putString(CONSTANTS.WORK_REQUEST_TAG,CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN)

                    .build();
        }else
        {
            mainData= new Data.Builder()
                    .putString(CONSTANTS.WORK_REQUEST_TAG,CONSTANTS.WORK_REQUEST_RESCHEDULE_WORK_PLAN)

                    .build();

        }

        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(true).setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest oneTimeWorkRequest= new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(mainData)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Toast.makeText(requireContext(), ""+workInfo.getState().name(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}