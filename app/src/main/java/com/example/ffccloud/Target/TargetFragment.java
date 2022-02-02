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

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.model.AddNewWorkPlanModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.model.UpdateWorkPlanStatus;
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
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
//import com.vivekkaushik.datepicker.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

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
    private CustomRescheduleDialogBinding rescheduleBinding;
    private ProgressDialog progressDialog;
    private NavController navController;
    private UploadDataRepository uploadDataRepository;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mbinding = FragmentTargetBinding.inflate(inflater, container, false);

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

        if (isLocationEnabled()) {

            BtnListener();

            setUpRecyclerView();

            SettingUpDatePicker();
            dateClickListener();
            setPullToFresh();


        } else {


            CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(), requireContext());


        }

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume", "on resume me ha ");

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

        mbinding.targetRecycler.docListRecyclerEvening.setAdapter(eveningListAdapter);
        mbinding.targetRecycler.docListmorningRecycler.setAdapter(morningListAdapter);

//        eveningListAdapter.notifyDataSetChanged();
//        morningListAdapter.notifyDataSetChanged();
    }

    private void SettingUpDatePicker() {
        DateTime dateTime = new DateTime();


        selectedDay = dateTime.getDayOfMonth();
        selectedMonth = dateTime.getMonthOfYear();

        selectedYear = dateTime.getYear();

        int checkmonth = (selectedMonth % 10);
        int checkday = (selectedDay % 10);
        String mDay, mMonth, mYear = String.valueOf(selectedYear);
        if (checkmonth > 0 && selectedMonth < 9) {
            mMonth = "0" + (selectedMonth);
            //          date = day + "-" + "0" + (month + 1) + "-" + (year);
        } else {
            mMonth = String.valueOf(selectedMonth);

        }

        if (checkday > 0 && selectedDay < 10) {
            mDay = "0" + (selectedDay);

        } else {
            mDay = String.valueOf(selectedDay);

        }

        mbinding.datePickerTimeline
                .setOffset(30)
                .setDateSelectedColor(getResources().getColor(R.color.APP_Theme_Color))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setTodayDateTextColor(getResources().getColor(R.color.white))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setDayOfWeekTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setUnselectedDayTextColor(getResources().getColor(R.color.black))
                .showTodayButton(false)
                .init();


        mbinding.datePickerTimeline.setSelected(true);
        String date = mDay + "/" + mMonth + "/" + mYear;
        Log.e("DateTag", "Date is:" + date);
        if (selectedDate.equals("")  || selectedDate.equals(date) ) {

            mbinding.datePickerTimeline.setDate(new DateTime());
            getTargetViewModelData(date);

        } else {
            getTargetViewModelData(selectedDate);

            mbinding.datePickerTimeline.setDate(new DateTime().minusDays(selectedDay-dateTime.getDayOfMonth()));

        }

    }


    public void dateClickListener() {
        mbinding.datePickerTimeline.setListener(new DatePickerListener() {
            @Override
            public void onDateSelected(DateTime dateSelected) {
                String date;
                selectedDay = dateSelected.getDayOfMonth();
                selectedMonth = dateSelected.getMonthOfYear();
                selectedYear = dateSelected.getYear();
                int checkmonth = (selectedMonth % 10);
                int checkday = (selectedDay % 10);
                String mDay = null, mMonth = null, mYear = String.valueOf(selectedYear);
                if (checkmonth > 0 && selectedMonth < 9) {
                    mMonth = "0" + (selectedMonth);
                    //          date = day + "-" + "0" + (month + 1) + "-" + (year);
                } else {
                    mMonth = String.valueOf(selectedMonth);

                }

                if (checkday > 0 && selectedDay < 9) {
                    mDay = "0" + (selectedDay);

                } else {
                    mDay = String.valueOf(selectedDay);

                }
                date = mDay + "/" + mMonth + "/" + mYear;

                selectedDate = date;
                Log.d("date", "date select ho rai hai");


                getTargetViewModelData(date);

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
                if (list != null && list.size() > 0) {
                    morningListAdapter.setDoctorModelList(list);

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
                    if (isNetworkConnected()) {
                        if (updateWorkPlanStatusList.size() > 0) {
                            SaveData.getInstance(requireContext()).SaveWorkPlanStatus();
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                        } else {
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                            int id = SharedPreferenceHelper.getInstance(getContext()).getEmpID();

                            targetViewModel.DeleteAllDoctor();
                            SyncDataToDB.getInstance().saveDoctorsList(id, requireContext(), requireActivity());
                        }

                        if (addNewWorkPlanModelList.size() > 0) {
                            SaveData.getInstance(requireContext()).saveWorkPlan();
                            mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                        } else {
                            if (updateWorkPlanStatusList.size() > 0) {
                                mbinding.targetRecycler.swipeLayout.setRefreshing(false);

                                int id = SharedPreferenceHelper.getInstance(getContext()).getEmpID();

                                targetViewModel.DeleteAllDoctor();

                                SyncDataToDB.getInstance().saveDoctorsList(id, requireContext(), requireActivity());
                            }
                        }
                    } else {
                        mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                        Toast.makeText(requireContext(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    mbinding.targetRecycler.swipeLayout.setRefreshing(false);
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(), requireContext());
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
                    if (location1 != null) {
                        location = location1;
                    }

                }
            };


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
                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Loading....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    dialogBinding.saveRemarksBtn.setEnabled(false);
                    if (dialogBinding.remarksEdittext.getText() != null && dialogBinding.remarksEdittext.getText().toString().length() > 0) {
                        remarksForCancel = dialogBinding.remarksEdittext.getText().toString();
                    }
                    String locationString = "";
                    if (location != null) {
                        locationString = location.getLatitude() + "," + location.getLongitude();

                    }
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                    String dateString = dateFormat.format(date);

                    if (remarksForCancel != null && remarksForCancel.length() > 0) {
                        if (locationString.length() > 0) {

                            List<UpdateWorkPlanStatus> list = new ArrayList<>();
                            int userId = SharedPreferenceHelper.getInstance(getActivity()).getEmpID();


                            UpdateWorkPlanStatus updateWorkPlanStatus = new UpdateWorkPlanStatus();

                            updateWorkPlanStatus.setDoctor_Id(doctorModel.getDoctorId());
                            updateWorkPlanStatus.setEmp_Id(userId);
                            updateWorkPlanStatus.setRemarks(remarksForCancel);
                            updateWorkPlanStatus.setStatus(String.valueOf(key));
                            updateWorkPlanStatus.setVisit_On(dateString);
                            updateWorkPlanStatus.setVisit_Cord(locationString);

                            if (doctorModel.getWorkId() > 0) {
                                updateWorkPlanStatus.setWork_Id(String.valueOf(doctorModel.getWorkId()));
                            }

                            list.add(updateWorkPlanStatus);
                            if (isNetworkConnected()) {
                                cancelWorkPlanStatus(list, doctorModel);
                            } else {
                                remarksForCancel = "";
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
                            Toast.makeText(requireContext(), "Please turn on Location", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            dialogBinding.saveRemarksBtn.setEnabled(true);
                        }


                    } else {
                        dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                        progressDialog.dismiss();
                        dialogBinding.saveRemarksBtn.setEnabled(true);
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

            CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(), requireContext());
        }


    }

    private void cancelWorkPlanStatus(List<UpdateWorkPlanStatus> list, DoctorModel doctorModel) {
        String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        UpdateStatus updateStatus = response.body();
                        Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();
                        eveningListAdapter.removeItem(doctorModel);
                        morningListAdapter.removeItem(doctorModel);
                        dialogBinding.saveRemarksBtn.setEnabled(true);
                        remarksForCancel = "";
                        targetViewModel.DeleteDoctor(doctorModel);
                        progressDialog.dismiss();
                        alertDialog.dismiss();
                    } else {

                        Toast.makeText(requireContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    if (response.message().equals("Unauthorized")) {
                        CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setUpRescheduleWorkPlanAlertDialog(DoctorModel doctorModel, int key) {
        if (doctorModel != null) {
            rescheduleBinding = CustomRescheduleDialogBinding.inflate(getLayoutInflater());

            alertDialog = new AlertDialog.Builder(requireContext()).setView(rescheduleBinding.getRoot()).setCancelable(false).create();
            alertDialog.show();
            rescheduleBinding.speechToTextBtn.setOnClickListener(new View.OnClickListener() {
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


            rescheduleBinding.textDate.setOnClickListener(new View.OnClickListener() {
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
                                rescheduleBinding.textDate.setText(mDate);

                            } else {
                                rescheduleBinding.textDate.setError("Select date forward from current date");
                            }


                        }
                    }, myear, mmonth, mday);
                    datePickerDialog.show();
                }
            });

            rescheduleBinding.textTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String openingTime = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                            rescheduleBinding.textTime.setText(openingTime);
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });
            rescheduleBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    rescheduleBinding.saveBtn.setEnabled(false);

                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Loading....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    String dateCheck = "", timeCheck = "";

                    if (rescheduleBinding.remarksEdittext.getText() != null && rescheduleBinding.remarksEdittext.getText().toString().length() > 0) {
                        remarksForReschedule = rescheduleBinding.remarksEdittext.getText().toString();
                    }
                    if (rescheduleBinding.textDate.getText() != null && rescheduleBinding.textDate.getText().toString().length() > 0) {
                        dateCheck = rescheduleBinding.textDate.getText().toString();
                    }

                    if (rescheduleBinding.textTime.getText() != null && rescheduleBinding.textTime.getText().toString().length() > 0) {
                        timeCheck = rescheduleBinding.textTime.getText().toString();
                    }

                    if (remarksForReschedule != null && remarksForReschedule.length() > 0) {
                        rescheduleBinding.remarksEdittext.setError(null);
                        if (dateCheck.length() > 0) {
                            rescheduleBinding.textDate.setError(null);
                            if (timeCheck.length() > 0) {
                                rescheduleBinding.textTime.setError(null);
                                int userId = SharedPreferenceHelper.getInstance(getActivity()).getEmpID();

                                AddNewWorkPlanModel addNewWorkPlanModel = new AddNewWorkPlanModel();

                                addNewWorkPlanModel.setDoctorId(doctorModel.getDoctorId());
                                addNewWorkPlanModel.setEmpId(userId);
                                addNewWorkPlanModel.setRemarks1(remarksForReschedule);
                                String dateTime = dateCheck + " " + timeCheck;
                                addNewWorkPlanModel.setWorkFromDate(dateTime);
                                addNewWorkPlanModel.setWorkToDate(dateTime);
                                addNewWorkPlanModel.setWorkPlan("Meeting");
                                addNewWorkPlanModel.setWorkId(doctorModel.getWorkId());

                                if (isNetworkConnected()) {
                                    rescheduleWorkPlanStatus(doctorModel, addNewWorkPlanModel);
                                } else {
                                    targetViewModel.DeleteDoctor(doctorModel);
                                    morningListAdapter.removeItem(doctorModel);
                                    eveningListAdapter.removeItem(doctorModel);
                                    rescheduleBinding.saveBtn.setEnabled(true);
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();
                                    uploadDataRepository.insertWorkPlan(addNewWorkPlanModel);
                                    generateWorkRequest(CONSTANTS.WORK_REQUEST_RESCHEDULE_WORK_PLAN);
                                }

                            } else {
                                Toast.makeText(requireContext(), "Please select time", Toast.LENGTH_SHORT).show();
                                rescheduleBinding.textTime.setError("Please select time");
                                rescheduleBinding.textTime.requestFocus();
                                progressDialog.dismiss();
                                rescheduleBinding.saveBtn.setEnabled(true);

                            }

                        } else {
                            Toast.makeText(requireContext(), "Please select date", Toast.LENGTH_SHORT).show();
                            rescheduleBinding.textDate.setError("Please select date");
                            rescheduleBinding.textDate.requestFocus();
                            progressDialog.dismiss();
                            rescheduleBinding.saveBtn.setEnabled(true);

                        }

                    } else {
                        Toast.makeText(requireContext(), "Please enter remarks", Toast.LENGTH_SHORT).show();
                        rescheduleBinding.remarksEdittext.setError("Please enter remarks");
                        rescheduleBinding.remarksEdittext.requestFocus();
                        progressDialog.dismiss();
                        rescheduleBinding.saveBtn.setEnabled(true);

                    }

                }
            });

            rescheduleBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Doctor is empty", Toast.LENGTH_SHORT).show();
        }


    }

    public void rescheduleWorkPlanStatus(DoctorModel doctorModel, AddNewWorkPlanModel addNewWorkPlanModel) {
        String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().RescheduleWorkPlan(token, addNewWorkPlanModel);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        UpdateStatus updateStatus = response.body();
                        Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();


                        targetViewModel.DeleteDoctor(doctorModel);
                        morningListAdapter.removeItem(doctorModel);
                        eveningListAdapter.removeItem(doctorModel);
                        rescheduleBinding.saveBtn.setEnabled(true);

                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(requireContext(), ""+response.message(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    if (response.message().equals("Unauthorized")) {
                        CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                    }
                    Toast.makeText(requireContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                rescheduleBinding.remarksEdittext.setText(remarksForReschedule);
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
        if (workRequest.equals(CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN)) {

            mainData = new Data.Builder()
                    .putString(CONSTANTS.WORK_REQUEST_TAG, CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN)

                    .build();
        } else {
            mainData = new Data.Builder()
                    .putString(CONSTANTS.WORK_REQUEST_TAG, CONSTANTS.WORK_REQUEST_RESCHEDULE_WORK_PLAN)

                    .build();

        }

        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(true).setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(mainData)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Toast.makeText(requireContext(), "" + workInfo.getState().name(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}