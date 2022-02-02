package com.example.ffccloud.Target;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ffccloud.model.AreasByEmpIdModel;
import com.example.ffccloud.model.DoctorsByAreaIdsModel;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.model.SaveNewWorkPlanModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentAddworkPlanBinding;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddworkPlanFragment extends Fragment {

    private FragmentAddworkPlanBinding mBinding;
    private View view;
    private int mDay, mMonth, mYear, key;
    private boolean isAreaDialogOpen = false, isDoctorDialogOpen = false;
    private AddWorkPlanDialogFragment dialogFragment;
    private List<DoctorsByAreaIdsModel> doctorModelList;
    private List<AreasByEmpIdModel> areasModelList;
    private String dataIDs = "", dataTittles = "", mDate = "", remarks = "";
    private NavController navController;
    private NavBackStackEntry navBackStackEntry;
    private final HashMap<String, Integer> regionHashmap = new HashMap<>();
    private final ArrayList<String> regionList = new ArrayList<>();
    private List<String> clientList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddworkPlanBinding.inflate(inflater, container, false);
        view = mBinding.getRoot();
//         dataIDs = AddworkPlanFragmentArgs.fromBundle(getArguments()).getAreaIds();
//         dataTittles = AddworkPlanFragmentArgs.fromBundle(getArguments()).getAreaTittles();
//         key = AddworkPlanFragmentArgs.fromBundle(getArguments()).getKey();
        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().show();

        }


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

                if (event.equals(Lifecycle.Event.ON_RESUME) && navBackStackEntry.getSavedStateHandle().contains("Object2") && key == 3) {

                    doctorModelList = navBackStackEntry.getSavedStateHandle().get("Object2");
                    setuptextFields();


                }
//                else if (event.equals(Lifecycle.Event.ON_RESUME) && navBackStackEntry.getSavedStateHandle().contains("Object2") && key == 3) {
//                    areasModelList = navBackStackEntry.getSavedStateHandle().get("Object");
//
//                    setuptextFields();
//                }
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
        getRegion();
        setupClientSpinner();
    }

    public void setuptextFields() {
//        if (key == 2 && areasModelList != null) {
//            dataTittles = "";
//            dataIDs = "";
//
//            for (AreasByEmpIdModel model : areasModelList) {
//                dataTittles = dataTittles + model.getAreaTittle() + ",";
//                dataIDs = dataIDs + model.getAreaId() + ",";
//            }
//            mbinding.Areas.setText(dataTittles);
//            mbinding.Doctors.setText("");
//
//        } else
        if (doctorModelList != null && key == 3) {
            dataTittles = "";
            dataIDs = "";
            for (DoctorsByAreaIdsModel model : doctorModelList) {
                dataTittles = dataTittles + model.getName() + ", ";
                dataIDs = dataIDs + model.getId() + ", ";
            }
            mBinding.clients.setText(dataTittles);

        }
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    private void setupClientSpinner() {

        clientList.add("Hospitals");
        clientList.add("Doctors");
        clientList.add("Farms");
        clientList.add("Medical Stores");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, clientList);
        mBinding.clientTypeSpinner.setAdapter(adapter);

    }

    public void btnListener() {
        mBinding.workPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth = (month + 1) % 10, checkday = (dayOfMonth % 10);

                        String mMonth, mDay;
                        if (checkMonth > 0 && month < 9) {
                            mMonth = "0" + (month + 1);
                        } else {
                            mMonth = String.valueOf(month + 1);

                        }

                        if (checkday > 0 && dayOfMonth < 10) {
                            mDay = "0" + (dayOfMonth);

                        } else {
                            mDay = String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;

                        mBinding.workPlanDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


//        mBinding.regionSpinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isAreaDialogOpen) {
//                    isAreaDialogOpen = false;
//                } else {
//                    key = 2;
//                    AddworkPlanFragmentDirections.ActionAddworkPlanFragmentToAddWorkPlanDialogFragment action = AddworkPlanFragmentDirections.actionAddworkPlanFragmentToAddWorkPlanDialogFragment();
//                    action.setKey(key);
//
//                    navController.navigate(action);
//                    isAreaDialogOpen = true;
//                }
//
//
//            }
//        });
        mBinding.clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDoctorDialogOpen) {
                    isDoctorDialogOpen = false;
                } else {
                    key = 3;
                    AddworkPlanFragmentDirections.ActionAddworkPlanFragmentToAddWorkPlanDialogFragment action = AddworkPlanFragmentDirections.actionAddworkPlanFragmentToAddWorkPlanDialogFragment();
                    action.setKey(key);

                    String type="";
                    if (mBinding.clientTypeSpinner.getText().toString().equals("Hospitals")) {
                        action.setClientType("H");
                        type="H";

                    } else if (mBinding.clientTypeSpinner.getText().toString().equals("Doctors")) {
                        type="Dr";

                    } else if (mBinding.clientTypeSpinner.getText().toString().equals("Farms")) {
                        type="F";

                    } else if (mBinding.clientTypeSpinner.getText().toString().equals("Medical Stores")){

                        type="Str";


                    }
                    int regionId=0;
                    if (mBinding.regionSpinner.getText()!=null && !mBinding.regionSpinner.getText().toString().equals(""))
                    {
                        regionId =  regionHashmap.get(mBinding.regionSpinner.getText().toString());

                    }
                    if (regionId!=0)
                    {
                        mBinding.regionSpinnerLayout.setError(null);

                        if (!type.equals(""))
                        {
                            mBinding.clientTypeSpinnerLayout.setError(null);
                            action.setClientType(type);
                            action.setRegionID(regionId);
                            navController.navigate(action);
                            isDoctorDialogOpen = true;
                        }else
                        {
                            Toast.makeText(requireContext(), "Please Select client type", Toast.LENGTH_SHORT).show();
                            mBinding.clientTypeSpinnerLayout.setError("Please select client type.");
                            mBinding.clientTypeSpinnerLayout.requestFocus();
                        }

                    }else
                    {
                        Toast.makeText(requireContext(), "Please select region", Toast.LENGTH_SHORT).show();
                        mBinding.regionSpinnerLayout.setError("Please select region.");
                        mBinding.regionSpinnerLayout.requestFocus();
                    }

                }


            }
        });
        mBinding.micBtn.setOnClickListener(new View.OnClickListener() {
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


        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
                mBinding.saveBtn.setEnabled(false);
                if (mBinding.remarks.getText() != null && mBinding.remarks.getText().toString().length() > 0) {
                    remarks = mBinding.remarks.getText().toString().trim();
                }


                if (mDate != null && mDate.length() > 1) {
                    mBinding.workPlanDateLayout.setError(null);
                    if (mBinding.clientTypeSpinner.getText()!=null && !mBinding.clientTypeSpinner.getText().toString().equals("Select Client Type"))
                    {
                        mBinding.clientTypeSpinnerLayout.setError(null);

                        if (dataIDs != null && dataIDs.length()>1) {

                            mBinding.clientsLayout.setError(null);

                            if (remarks != null && remarks.length() > 1) {

                                mBinding.remarks.setError(null);

                                SaveNewWorkPlanModel model = new SaveNewWorkPlanModel();
                                model.setDate(mDate);
                                model.setDoctorIds(dataIDs);
                                model.setEmpId(id);
                                model.setRemarks(remarks);

                                saveWorkPlan(model);

                            } else {

                                mBinding.remarks.setError("Please enter remarks");
                                mBinding.remarks.requestFocus();
                                mBinding.saveBtn.setEnabled(true);
                            }

                        } else {
                            mBinding.clientsLayout.setError("Please select clients");
                            mBinding.clientsLayout.requestFocus();
                            mBinding.saveBtn.setEnabled(true);

                        }

                    }
                    else
                    {
                        mBinding.clientTypeSpinnerLayout.setError("Please select client type.");
                        mBinding.clientTypeSpinnerLayout.requestFocus();
                        mBinding.saveBtn.setEnabled(true);
                    }

                } else {
                    mBinding.workPlanDateLayout.setError("Please select date");
                    mBinding.workPlanDateLayout.requestFocus();
                    mBinding.saveBtn.setEnabled(true);
                }



            }
        });
    }

    private void saveWorkPlan(SaveNewWorkPlanModel model) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving...");
        progressDialog.setTitle("Work Plan");
        progressDialog.show();

        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().AddNewWorkPlan(token, model);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        UpdateStatus updateStatus = response.body();

                        CustomsDialog.getInstance().showDialog(updateStatus.getStrMessage(), "Add work plan", requireActivity(), requireContext());

                        if (updateStatus.getStatus()==1)
                        {
                            refreshFields();
                        }

                    }

                } else {
                    CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                }
                mBinding.saveBtn.setEnabled(true);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                mBinding.saveBtn.setEnabled(true);
                CustomsDialog.getInstance().showDialog(t.getMessage(), "Add work plan", requireActivity(), requireContext());
                progressDialog.dismiss();

            }
        });
    }

    private void refreshFields() {





            mBinding.clientsLayout.setHint(R.string.select_client);
            mBinding.clients.setText(null);
            mBinding.clientTypeSpinnerLayout.setHint(R.string.select_client_type);
            mBinding.clientTypeSpinner.setText(null);
            mBinding.workPlanDate.setText(null);
            mBinding.remarks.setText(null);
            dataTittles = null;
            dataIDs = null;
            mDate = null;
            remarks=null;

    }

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            ArrayList<String> resultString = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            remarks = remarks + " " + resultString.get(0);
                            mBinding.remarks.setText(remarks);
                        }
                    }
                }
            });

    public void getRegion() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
        Call<List<RegionModel>> call = ApiClient.getInstance().getApi().getRegion(token, 1, 1, 1, id);

        call.enqueue(new Callback<List<RegionModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RegionModel>> call, @NotNull Response<List<RegionModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body()!=null && response.body().size()>0)
                    {
                        regionHashmap.clear();
                        regionList.clear();
                        List<RegionModel> regionModelList = new ArrayList<>();
                        regionModelList = response.body();

                        for (RegionModel model : regionModelList) {
                            regionList.add(model.getName());
                            regionHashmap.put(model.getName(), model.getRegionId());

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
                        mBinding.regionSpinner.setAdapter(adapter);
                    }


                } else {
                    if (response.message().equals("Unauthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext());
                    }
                    Toast.makeText(requireContext(), " " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<RegionModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), " " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}