package com.example.ffccloud.Target;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ffccloud.ModelClasses.AreasByEmpIdModel;
import com.example.ffccloud.ModelClasses.DoctorsByAreaIdsModel;
import com.example.ffccloud.ModelClasses.SaveNewWorkPlanModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentAddworkPlanBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.SyncDataToDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddworkPlanFragment extends Fragment {

    private FragmentAddworkPlanBinding mbinding;
    private View view;
    private int mDay, mMonth, mYear, key;
    private boolean isAreaDialogOpen = false, isDoctorDialogOpen = false;
    private AddWorkPlanDialogFragment dialogFragment;
    private List<DoctorsByAreaIdsModel> doctorModelList;
    private List<AreasByEmpIdModel> areasModelList;
    private String dataIDs = "", dataTittles = "", mDate="";
    private NavController navController;
    private NavBackStackEntry navBackStackEntry;
    private String remarks="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mbinding = FragmentAddworkPlanBinding.inflate(inflater, container, false);
        view = mbinding.getRoot();
//         dataIDs = AddworkPlanFragmentArgs.fromBundle(getArguments()).getAreaIds();
//         dataTittles = AddworkPlanFragmentArgs.fromBundle(getArguments()).getAreaTittles();
//         key = AddworkPlanFragmentArgs.fromBundle(getArguments()).getKey();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        navBackStackEntry = navController.getBackStackEntry(R.id.addworkPlanFragment);
        LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

                if (event.equals(Lifecycle.Event.ON_RESUME) && navBackStackEntry.getSavedStateHandle().contains("Object") && key == 2) {


                    areasModelList = navBackStackEntry.getSavedStateHandle().get("Object");
                    getActivity();


                    setuptextFields();

                } else if (event.equals(Lifecycle.Event.ON_RESUME) && navBackStackEntry.getSavedStateHandle().contains("Object2") && key == 3) {
                    doctorModelList = navBackStackEntry.getSavedStateHandle().get("Object2");
                    setuptextFields();
                }
            }
        };

        navBackStackEntry.getLifecycle().addObserver(lifecycleEventObserver);
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    navBackStackEntry.getLifecycle().removeObserver(lifecycleEventObserver);
                }
            }
        });


        btnListener();

    }

    public void setuptextFields() {
        if (key == 2 && areasModelList != null) {
            dataTittles = "";
            dataIDs= "";

            for (AreasByEmpIdModel model : areasModelList) {
                dataTittles = dataTittles + model.getAreaTittle() + ",";
                dataIDs = dataIDs + model.getAreaId() + ",";
            }
            mbinding.Areas.setText(dataTittles);
            mbinding.Doctors.setText("");

        } else if (doctorModelList != null && key == 3) {
            dataTittles = "";
            dataIDs= "";
            for (DoctorsByAreaIdsModel model : doctorModelList) {
                dataTittles = dataTittles + model.getName() + ",";
                dataIDs = dataIDs + model.getId() + ",";
            }
            mbinding.Doctors.setText(dataTittles);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void btnListener() {
        mbinding.workPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth= month%10, checkday = (dayOfMonth%10);;
                        String mMonth,mDay;
                        if (checkMonth>0&&month<9)
                        {
                            mMonth= "0"+(month+1);
                        }
                        else
                        {
                            mMonth=String.valueOf(month+1);

                        }

                        if (checkday>0 && dayOfMonth<10){
                            mDay = "0" + (dayOfMonth);

                        }
                        else
                        {
                            mDay= String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;

                        mbinding.workPlanDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mbinding.Areas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAreaDialogOpen) {
//                    dialogFragment.dismiss();
                    isAreaDialogOpen = false;
                } else {
                    key = 2;
                    AddworkPlanFragmentDirections.ActionAddworkPlanFragmentToAddWorkPlanDialogFragment action = AddworkPlanFragmentDirections.actionAddworkPlanFragmentToAddWorkPlanDialogFragment();
                    action.setKey(key);

                    navController.navigate(action);
                    isAreaDialogOpen = true;
                }


            }
        });
        mbinding.Doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDoctorDialogOpen) {
                    //   dialogFragment.dismiss();
                    isDoctorDialogOpen = false;
                } else {
                    key = 3;
                    AddworkPlanFragmentDirections.ActionAddworkPlanFragmentToAddWorkPlanDialogFragment action = AddworkPlanFragmentDirections.actionAddworkPlanFragmentToAddWorkPlanDialogFragment();
                    action.setKey(key);
                    action.setDataIds(dataIDs);
                    navController.navigate(action);
                    isDoctorDialogOpen = true;
                }


            }
        });
        mbinding.micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    ActivityResultLauncher.launch(intent);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mbinding.remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remarks= s.toString();
            }
        });

        mbinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbinding.saveBtn.setEnabled(false);
                String remarks = mbinding.remarks.getText().toString().trim();
                int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
                if (!remarks.isEmpty() && !dataTittles.isEmpty() && !mDate.isEmpty()) {

                    SaveNewWorkPlanModel model = new SaveNewWorkPlanModel();
                    model.setDate(mDate);
                    model.setDoctorIds(dataIDs);
                    model.setEmpId(id);
                    model.setRemarks(remarks);

                    saveWorkPlan(model);
                    mbinding.Areas.setText(R.string.select_areas);
                    mbinding.Doctors.setText(R.string.select_doctors);
                    mbinding.workPlanDate.setText("");
                    mbinding.remarks.setText("");
                    dataTittles = "";
                    mDate = "";
                }
            }
        });
    }

    private void saveWorkPlan(SaveNewWorkPlanModel model) {

        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().AddNewWorkPlan(token, model);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {
                if (response.body() != null) {
                    UpdateStatus updateStatus = response.body();

                    new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Add Work Plan")
                            .setContentText(updateStatus.getStrMessage())
                            .show();
                }
                else
                {
                    new SyncDataToDB(requireActivity().getApplication(),requireContext()).loginAgain(response.message());
                }
                mbinding.saveBtn.setEnabled(true);

            }

            @Override
            public void onFailure(Call<UpdateStatus> call, Throwable t) {
                mbinding.saveBtn.setEnabled(true);

                new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Add Work Plan")
                        .setContentText(t.getMessage())
                        .show();            }
        });
    }

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if ( data != null) {
                            ArrayList<String> resultString = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            remarks=remarks+" "+resultString.get(0);
                            mbinding.remarks.setText(remarks);
                        }
                    }
                }
            });



}