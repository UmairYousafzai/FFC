package com.example.ffccloud.medicalStore;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.SupplierCompDetail;
import com.example.ffccloud.databinding.CompaniesDialogBinding;
import com.example.ffccloud.databinding.FragmentAddMedicalStoreBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentArgs;
import com.example.ffccloud.medicalStore.adapter.CompanyRecyclerViewAdapter;
import com.example.ffccloud.model.GetSupplierDetailModel;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.model.SupplierMainModel;
import com.example.ffccloud.model.SupplierModelNew;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
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
    private final ArrayList<String> regionList = new ArrayList<>();
    private final ArrayList<String> gradeArray = new ArrayList<>();
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private final HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();
    private final HashMap<String, Integer> regionHashmapForID = new HashMap<>();
    private final HashMap<Integer, String> regionHashmapForTitle = new HashMap<>();
    private UserViewModel userViewModel;
    private List<GradingModel> gradingModelList;
    private CompanyRecyclerViewAdapter adapter;
    private final List<SupplierCompDetail> companyModelList = new ArrayList<>();
    private String locationString,supplierCode="0";
    private String locationAddress;
    private int supplierID;
    private String callingAddBtn;
    private NavController navController;
    private boolean isLocationUpdate=false;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentAddMedicalStoreBinding.inflate(inflater, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        getRegion();

        btnListener();
        setUpGradeSpinner();
        if (getArguments() != null) {
            supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();
        }
        if (supplierID > 0 && callingAddBtn == null) {

            //setup fields if edit request has been made
            getSupplierByID(supplierID);
        }
        setUpCompanyRecyclerView();
        textListener();
        getLiveData();
    }

    private void getLiveData() {

        MutableLiveData<String> locationPickerLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.LOCATION);
        locationPickerLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String addressCoordinates) {

                if (addressCoordinates!=null&& !addressCoordinates.isEmpty())
                {
                    String[] location= addressCoordinates.split(":");

                    locationAddress=location[0];
                    locationString = location[1];
                    mBinding.locationCheckBox.setText("Enable Location");
                    mBinding.locationCheckBox.setChecked(false);
                    mBinding.idCoordinates.setText(addressCoordinates);
                    isLocationUpdate=true;

                }
            }
        });
    }

    private void textListener() {

        mBinding.idCoordinates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s!=null)
                {
                    locationString = s.toString();
                    locationAddress="";
                    mBinding.locationCheckBox.setText("Enable Location");
                    mBinding.locationCheckBox.setChecked(false);
                }
            }
        });
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


                        for (GradingModel model : gradingModels) {
                            gradeArray.add(model.getGrade_Title());
                            gradingHashMapForId.put(model.getGrade_Title(), model.getGrade_Id());
                            gradingHashMapForTitle.put(model.getGrade_Id(), model.getGrade_Title());

                        }


                        ArrayAdapter<String> gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, gradeArray);
                        mBinding.spinnerGrade.setAdapter(gradingAdapter);


                    }
                });
    }

    private void btnListener() {
        mBinding.btnLocationPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingAddBtn="locationPicker";
                navController.navigate(R.id.action_addMedicalStoreFragment_to_location_pickerFragment);
            }
        });
        mBinding.locationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomLocation customLocation = new CustomLocation(requireContext());

                if (customLocation.isLocationEnabled()) {
                    CustomLocation.CustomLocationResults results = new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {

                            if (supplierID > 0) {
                                isLocationUpdate = true;
                            }
                            if (mBinding.locationCheckBox.isChecked()) {


                                locationString = location.getLatitude() + "," +location.getLongitude() ;
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude())+
                                        locationString;
                                mBinding.locationCheckBox.setText(locationAddress);
                            } else {

                                locationAddress="";
                                locationString="";
                                mBinding.locationCheckBox.setText("Enable Location");
                                mBinding.locationCheckBox.setChecked(false);
                                mBinding.idCoordinates.setText("");


                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                } else {
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
                    mBinding.locationCheckBox.setChecked(false);

                }
            }
        });

        mBinding.btnAddCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingAddBtn = "";
                callingAddBtn = "CompanyAddBtn";
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
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<GetSupplierDetailModel> call = ApiClient.getInstance().getApi().getSupplierDetail(supplierID);

        call.enqueue(new Callback<GetSupplierDetailModel>() {
            @Override
            public void onResponse(@NotNull Call<GetSupplierDetailModel> call, @NotNull Response<GetSupplierDetailModel> response) {

                if (response.isSuccessful())
                {
                    if (response.body() != null) {
                        GetSupplierDetailModel getSupplierDetailModel = response.body();
                        setUpFields(getSupplierDetailModel);

                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();

                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<GetSupplierDetailModel> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private void setUpFields(GetSupplierDetailModel getSupplierDetailModel) {
        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierCode()!=null)
        {
            supplierCode =getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierCode();
        }
        mBinding.etName.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierName());
        mBinding.etContact.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getPhoneNo());
        mBinding.etAddress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());
        if(getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress().length()>0||
                getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().length()>0)
        {
            mBinding.locationCheckBox.setText(String.format("%s%s", getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress(), getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord()));
            mBinding.locationCheckBox.setChecked(true);
            locationAddress=getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress();
        }

        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().length()>0)
        {
            locationString=getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord();
        }
        mBinding.etMonthlySale.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getMonthlySale()));


        int gradeID = getSupplierDetailModel.getSupplierModelNewList().get(0).getGrade();

        String grade = gradingHashMapForTitle.get(gradeID);


        mBinding.spinnerGrade.setSelection(gradeArray.indexOf(grade));

        int regionId = (int) getSupplierDetailModel.getSupplierModelNewList().get(0).getRegionId();

        String regionTitle = regionHashmapForTitle.get(regionId);

        mBinding.spinnerRegion.setSelection(regionList.indexOf(regionTitle));

        companyModelList.clear();
        companyModelList.addAll(getSupplierDetailModel.getSupplierCompDetailList());
        adapter.setCompanyModelList(companyModelList);

        String shift = getSupplierDetailModel.getSupplierModelNewList().get(0).getShiftType();

        if (shift != null) {
            switch (shift) {
                case "Morning":
                    mBinding.morningRadioBtn.setChecked(true);
                    break;
                case "Evening":
                    mBinding.eveningRadioBtn.setChecked(true);
                    break;
                case "FullDay":
                    mBinding.fullDayRadioBtn.setChecked(true);
                    break;
            }
        }
    }


    private void setUpSupplierModelForSave() {


        String name = " ", phone = " ", address = " ",monthlySale= " ",shitType="";
        int gradeID = 0,region =0,userId = 0;



        try {

            RadioButton radioButtonShift = mBinding.getRoot().findViewById(mBinding.TimingRadioGroup.getCheckedRadioButtonId());
            shitType = radioButtonShift.getText().toString();
            userId = SharedPreferenceHelper.getInstance(requireContext()).getUserID();
            name = Objects.requireNonNull(mBinding.etName.getText()).toString();
            phone = Objects.requireNonNull(mBinding.etContact.getText()).toString();
            address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
            gradeID = gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());
            region = regionHashmapForID.get(mBinding.spinnerRegion.getSelectedItem().toString());
           monthlySale = mBinding.etMonthlySale.getText().toString();
        }
        catch (Exception e)
        {
            Log.e("MedicalStore save error",":"+e.getMessage());
        }


        if (name.length() > 0) {
            if (phone.length() > 0) {
                if (address.length() > 0) {
                    if (regionList.size() > 0) {

                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompany_Id(1);
                        supplierModelNew.setUserId(userId);
                        supplierModelNew.setCountry_Id(1);
                        supplierModelNew.setShift_Type(shitType);
                        supplierModelNew.setLocation_Id(1);
                        supplierModelNew.setProject_Id(1);
                        supplierModelNew.setSupplier_Code(supplierCode);
                        supplierModelNew.setSupplier_Id(supplierID);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setPhone_No(phone);
                        supplierModelNew.setSupplier_Name(name);
                        supplierModelNew.setRegion_Id(String.valueOf(region));
                        supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_STORE);
                        supplierModelNew.setUserTypeName("Str");
                        supplierModelNew.setGrade_id(gradeID);
                        supplierModelNew.setMonthly_Sale(monthlySale);
                        supplierModelNew.setSupplierCompDetailList(companyModelList);
                        supplierModelNew.setLoc_Cord(locationString);
                        supplierModelNew.setIs_update_Loc_Cord(isLocationUpdate);
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
        progressDialog.setCancelable(false);
        progressDialog.show();


        SupplierMainModel model = new SupplierMainModel();
        model.setSupplierModelNew(supplierModelNew);

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSupplier(supplierModelNew);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null) {
                        UpdateStatus updateStatus = response.body();
                        Toast.makeText(requireContext(), " " + updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                        if (updateStatus.getStatus()==1)
                        {
                            navController.popBackStack();
                        }
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), " " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                String name = Objects.requireNonNull(binding.etCompanyName.getText()).toString();
                if (name.length() > 0) {
                    String type;
                    if (binding.radioButtonDistributer.isChecked()) {
                        type = "Distributor";
                    } else {
                        type = "Stockist";
                    }

                    SupplierCompDetail model = new SupplierCompDetail();
                    model.setCompName(name);
                    model.setType(type);
                    model.setSupplierCompIdDtl("0");
                    companyModelList.add(model);
                    adapter.setCompanyModelList(companyModelList);
                    Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
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
                    if (response.body()!=null)
                    {

                        regionHashmapForTitle.clear();
                        regionHashmapForID.clear();
                        regionList.clear();
                        List<RegionModel> regionModelList = new ArrayList<>();
                        regionModelList = response.body();
                        for (RegionModel model : regionModelList) {
                            regionList.add(model.getName());
                            regionHashmapForID.put(model.getName(), model.getRegionId());
                            regionHashmapForTitle.put(model.getRegionId(), model.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
                        mBinding.spinnerRegion.setAdapter(adapter);
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