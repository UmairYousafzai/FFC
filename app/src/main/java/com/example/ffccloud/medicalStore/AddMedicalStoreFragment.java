package com.example.ffccloud.medicalStore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ffccloud.CompanyModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.ModelClasses.RegionModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.CompaniesDialogBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentAddMedicalStoreBinding;
import com.example.ffccloud.medicalStore.adapter.CompanyRecyclerViewAdapter;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedicalStoreFragment extends Fragment {
    private FragmentAddMedicalStoreBinding mBinding;
    private ArrayList<String>  regionList = new ArrayList<>(),gradeArray=new ArrayList<>();
    private HashMap<String, Integer> regionHashmap = new HashMap<>();
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();

    private UserViewModel userViewModel;
    private List<GradingModel> gradingModelList;
    private CompanyRecyclerViewAdapter adapter;
    private List<CompanyModel> companyModelList =  new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAddMedicalStoreBinding.inflate(inflater,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        getRegion();

        btnListener();
        setUpGradeSpinner();
        setUpCompanyRecyclerView();
    }

    private void setUpCompanyRecyclerView() {

        mBinding.companyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CompanyRecyclerViewAdapter();
        mBinding.companyRecyclerView.setAdapter(adapter);
    }

    private void setUpGradeSpinner() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        userViewModel.getAllGrades().observe(getViewLifecycleOwner(), new
                Observer<List<GradingModel>>() {
                    @Override
                    public void onChanged(List<GradingModel> gradingModels) {
                        gradeArray.clear();
                        progressDialog.dismiss();


                        for (GradingModel model:gradingModels)
                        {
                            gradeArray.add(model.getGrade_Title());
                            gradingHashMapForId.put(model.getGrade_Title(), model.getGrade_Id());
                            gradingHashMapForTitle.put(model.getGrade_Id(),model.getGrade_Title());

                        }


                       ArrayAdapter<String> gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,gradeArray);
                        mBinding.spinnerGrade.setAdapter(gradingAdapter);




                    }
                });
    }

    private void btnListener() {

        mBinding.locationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomLocation customLocation = new CustomLocation(requireContext());

                if (customLocation.isLocationEnabled()) {
                    CustomLocation.CustomLocationResults results = new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {


                            if (mBinding.locationCheckBox.isChecked()) {
                                String address = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.locationCheckBox.setText(address);
                                String locationString = String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude());
                            } else {

                                mBinding.locationCheckBox.setText("Enable Location");
                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                }
                else {
                    showOpenLocationSettingDialog();

                }
            }
        });

        mBinding.btnAddCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCompanyDialog();
            }
        });

    }

    private void showAddCompanyDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();

        CompaniesDialogBinding binding = CompaniesDialogBinding.inflate(getLayoutInflater());

        alertDialog.setView(binding.getRoot());
        alertDialog.show();

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name= Objects.requireNonNull(binding.etCompanyName.getText()).toString();
                if (name.length()>0)
                {
                    String type;
                    if (binding.radioButtonDistributer.isChecked())
                    {
                        type="Distributor";
                    }
                    else
                    {
                        type="Stockist";
                    }

                    CompanyModel model = new CompanyModel(0,type,name);
                    companyModelList.add(model);
                    adapter.setCompanyModelList(companyModelList);
                    Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
                else
                {
                    Toast.makeText(requireContext(), "Add company name", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }



            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

    }


    public void getRegion() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
        Call<List<RegionModel>> call = ApiClient.getInstance().getApi().getRegion(token, 1, 1, 1, id);

        call.enqueue(new Callback<List<RegionModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RegionModel>> call, @NotNull Response<List<RegionModel>> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    regionHashmap.clear();
                    regionList.clear();
                    List<RegionModel> regionModelList = new ArrayList<>();
                    regionModelList = response.body();
                    for (RegionModel model : regionModelList) {
                        regionList.add(model.getName());
                        regionHashmap.put(model.getName(), model.getRegionId());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
                    mBinding.spinnerRegion.setAdapter(adapter);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), " "+response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RegionModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), " "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOpenLocationSettingDialog() {


        AlertDialog alertDialog;
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(requireActivity().getLayoutInflater());
        alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Please turn on  location for this action.");
        dialogBinding.body.setText("Do you want to open location setting.");
        alertDialog.show();

        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                requireContext().startActivity(intent);
            }
        });
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}