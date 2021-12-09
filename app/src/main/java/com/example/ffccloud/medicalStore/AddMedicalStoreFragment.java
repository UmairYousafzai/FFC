package com.example.ffccloud.medicalStore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.ffccloud.ModelClasses.GetSupplierDetailModel;
import com.example.ffccloud.ModelClasses.SupplierMainModel;
import com.example.ffccloud.ModelClasses.SupplierModelNew;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.SupplierCompDetail;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.ModelClasses.RegionModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.CompaniesDialogBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentAddMedicalStoreBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentArgs;
import com.example.ffccloud.medicalStore.adapter.CompanyRecyclerViewAdapter;
import com.example.ffccloud.utils.CONSTANTS;
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
    private List<SupplierCompDetail> companyModelList =  new ArrayList<>();
    private String locationString;
    private String locationAddress;
    private int supplierID;
    private String callingAddBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAddMedicalStoreBinding.inflate(inflater,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();
        if (supplierID > 0&&callingAddBtn==null){

            //setup fields if edit request has been made
            getSupplierByID(supplierID);
        }
        setUpCompanyRecyclerView();

    }

    @Override
    public void onResume() {
        super.onResume();

        getRegion();

        btnListener();
        setUpGradeSpinner();
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
                                 locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.locationCheckBox.setText(locationAddress);
                                 locationString = String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude());
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
                callingAddBtn= "";
                callingAddBtn= "CompanyAddBtn";
                showAddCompanyDialog();
            }
        });
        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSupplierModelForSave();
            }
        });

    }


    private void getSupplierByID(int supplierID) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading Supplier...");
        progressDialog.show();

        Call<GetSupplierDetailModel> call = ApiClient.getInstance().getApi().getSupplierDetail(supplierID);

        call.enqueue(new Callback<GetSupplierDetailModel>() {
            @Override
            public void onResponse(@NotNull Call<GetSupplierDetailModel> call, @NotNull Response<GetSupplierDetailModel> response) {

                if (response.body()!=null)
                {
                    progressDialog.dismiss();
                    GetSupplierDetailModel getSupplierDetailModel= response.body();
                    setUpFields(getSupplierDetailModel);

                }
                else {
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetSupplierDetailModel> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }

    private void setUpFields(GetSupplierDetailModel getSupplierDetailModel) {

        mBinding.etName.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierName());
        mBinding.etContact.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getPhoneNo());
        mBinding.etAddress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());
        mBinding.locationCheckBox.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress());
        mBinding.etMonthlySale.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getMonthlySale()));



        int gradeID= getSupplierDetailModel.getSupplierModelNewList().get(0).getGradeId();

        String grade =  gradingHashMapForTitle.get(gradeID);


        mBinding.spinnerGrade.setSelection(gradeArray.indexOf(grade));

        companyModelList.clear();
        companyModelList.addAll(getSupplierDetailModel.getSupplierCompDetailList());
        adapter.setCompanyModelList(companyModelList);





    }


    private void setUpSupplierModelForSave() {

        String name = Objects.requireNonNull(mBinding.etName.getText()).toString();
        String phone = Objects.requireNonNull(mBinding.etContact.getText()).toString();
        String address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
        int gradeID=0;
        if (gradeArray.size()>0)
        {
            gradeID= gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());

        }




        if (name.length() > 0) {
            if (phone.length() > 0) {
                if (address.length() > 0) {
                    if (regionList.size()>0) {
                        int region = regionHashmap.get(mBinding.spinnerRegion.getSelectedItem().toString());

                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompany_Id(1);
                        supplierModelNew.setCountry_Id(1);
                        supplierModelNew.setLocation_Id(1);
                        supplierModelNew.setProject_Id(1);
                        supplierModelNew.setSupplier_Code("0");
                        supplierModelNew.setSupplier_Id(0);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setPhone_No(phone);
                        supplierModelNew.setSupplier_Name(name);
                        supplierModelNew.setRegion_Id(String.valueOf(region));
                        supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_STORE);
                        supplierModelNew.setUserTypeName("Str");
                        supplierModelNew.setGrade_id(gradeID);
                        supplierModelNew.setMonthly_Sale(mBinding.etMonthlySale.getText().toString());
                        supplierModelNew.setSupplierCompDetailList(companyModelList);
                        supplierModelNew.setLoc_Cord(locationString);
                        supplierModelNew.setLoc_Cord_Address(locationAddress);


                        saveSupplier(supplierModelNew);
                    } else {
                        Toast.makeText(requireContext(), "Please select region", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(), "Please enter the address", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "Please enter the phone number", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(requireContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
        }


    }

    private void saveSupplier(SupplierModelNew supplierModelNew) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Saving....");
        progressDialog.show();


        SupplierMainModel model = new SupplierMainModel();
        model.setSupplierModelNew(supplierModelNew);

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSupplier(supplierModelNew);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
                if (response.body()!=null)
                {
                    UpdateStatus updateStatus = response.body();
                    Toast.makeText(requireContext(), " "+updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(requireContext(), " "+response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " "+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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

                    SupplierCompDetail model = new SupplierCompDetail();
                    model.setCompName(name);
                    model.setType(type);
                    model.setSupplierCompIdDtl("0");
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