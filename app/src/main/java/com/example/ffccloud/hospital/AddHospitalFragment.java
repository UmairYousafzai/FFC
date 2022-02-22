package com.example.ffccloud.hospital;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.adapter.SupplierRecyclerViewAdapter;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddHospitalBinding;
import com.example.ffccloud.databinding.OtherFarmDialogBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentArgs;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.model.GetSupplierDetailModel;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.model.SupplierLinking;
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

public class AddHospitalFragment extends Fragment {

    private FragmentAddHospitalBinding mBinding;
    private UserViewModel userViewModel;
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();
    private ArrayList<String> gradeArray = new ArrayList<>();
    private List<GradingModel> gradingModelList;
    //    private GasDaysRecyclerAdapter gasDaysRecyclerAdapter;
//    private List<String> gasDaysList = new ArrayList<>();
    private NavController navController;
    private MedicineAdapter medicineAdapter;
    private int supplierID = 0;
    private List<GetSupplierModel> doctorModelList = new ArrayList<>();
    private List<GetSupplierModel> farmModelList = new ArrayList<>();
    private SupplierRecyclerViewAdapter doctorRecyclerViewAdapter;
    private SupplierRecyclerViewAdapter farmRecyclerViewAdapter;
    private List<SupplierItemLinking> medicineModalList = new ArrayList<>();
    private String callingAddBtn;
    private HashMap<String, Integer> regionHashmapForID = new HashMap<>();
    private HashMap<Integer, String> regionHashmapForTitle = new HashMap<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private String locationString, numberOfCases = "",supplierCode="0";
    private String locationAddress;
    private int gradePosition = -1, regionPosition = -1;
    private boolean isLocationChecked = false, isLocationUpdate = false;
    private boolean isSpinnerUpdate = false;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAddHospitalBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        setUpRecyclerView();
        getLiveData();

        if (!isSpinnerUpdate) {
            isSpinnerUpdate = true;
            getRegion();
            setUpGradeSpinner();

        }


        if (getArguments() != null) {
            supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();

        }
        if (supplierID > 0 && callingAddBtn == null) {

            //  setup fields if edit request has been made
            getSupplierByID(supplierID);
        }

    }

    public void getLiveData() {
        MutableLiveData<GetSupplierModel> doctorsupplierLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.DOCTOR_SUPPLIER_KEY);
        doctorsupplierLiveData.observe(getViewLifecycleOwner(), new Observer<GetSupplierModel>() {
            @Override
            public void onChanged(GetSupplierModel model) {
                if (model != null) {

                    if (callingAddBtn.equals("DoctorAddBtn")) {
                        if (doctorModelList.size() > 0) {
                            if (doctorModelList.get(doctorModelList.size() - 1) != model) {
                                model.setRegister(true);
                                doctorModelList.add(model);
                                doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
                            }
                        } else {
                            model.setRegister(true);
                            doctorModelList.add(model);

                        }

                    }


                }


            }
        });

        MutableLiveData<GetSupplierModel> farmSupplierLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.FARM_SUPPLIER_KEY);
        farmSupplierLiveData.observe(getViewLifecycleOwner(), new Observer<GetSupplierModel>() {
            @Override
            public void onChanged(GetSupplierModel model) {
                if (model != null) {
                    if (callingAddBtn.equals("FarmAddBtn")) {
                        if (farmModelList.size() > 0) {
                            if (farmModelList.get(farmModelList.size() - 1) != model) {
                                model.setRegister(true);
                                farmModelList.add(model);
                                farmRecyclerViewAdapter.setGetSupplierModelList(farmModelList);
                            }
                        } else {
                            model.setRegister(true);
                            farmModelList.add(model);
                        }

                    }


                }


            }
        });


        MutableLiveData<InsertProductModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        liveData.observe(getViewLifecycleOwner(), new Observer<InsertProductModel>() {
            @Override
            public void onChanged(InsertProductModel model) {
                if (model != null) {
                    if (callingAddBtn.equals("MedicineAddBtn")) {
                        SupplierItemLinking medicineModal = new SupplierItemLinking();

                        medicineModalList.size();
                        medicineModal.setItHead(model.getTitleProduct());
                        medicineModal.setIsRegistered(true);
                        medicineModal.setSupplierItemLinkIdDtl("0");
                        medicineModal.setItCode(model.getItem_Code());
                        if (medicineModalList.size() > 0) {
                            if (medicineModalList.get(medicineModalList.size() - 1).getItCode() != medicineModal.getItCode()) {
                                medicineModalList.add(medicineModal);
                                medicineAdapter.setMedicineModalList(medicineModalList);
                            }
                        } else {
                            medicineModalList.add(medicineModal);
                        }

                    }


                }


            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (callingAddBtn != null) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
            mBinding.regionSpinner.setAdapter(adapter);

            ArrayAdapter<String> gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, gradeArray);
            mBinding.spinnerGrade.setAdapter(gradingAdapter);
            medicineAdapter.setMedicineModalList(medicineModalList);
            doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
            farmRecyclerViewAdapter.setGetSupplierModelList(farmModelList);
            if (regionPosition != -1) {
                mBinding.regionSpinner.setSelection(regionPosition);

            }
            if (gradePosition != -1) {
                mBinding.spinnerGrade.setSelection(gradePosition);
            }


            if (isLocationChecked) {
                mBinding.locationCheckbox.setChecked(true);
                mBinding.locationCheckbox.setText(locationAddress);
            }

        }


        btnListener();
        textListener();
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
                    mBinding.locationCheckbox.setText("Enable Location");
                    mBinding.locationCheckbox.setChecked(false);
                }
            }
        });
    }
    private void setUpRecyclerView() {


//        mBinding.gasDaysRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
//        gasDaysRecyclerAdapter = new GasDaysRecyclerAdapter();
//        mBinding.gasDaysRecyclerview.setAdapter(gasDaysRecyclerAdapter);

        mBinding.medicineRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicineAdapter = new MedicineAdapter();
        mBinding.medicineRecycler.setAdapter(medicineAdapter);

        mBinding.doctorListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctorRecyclerViewAdapter = new SupplierRecyclerViewAdapter(this);
        doctorRecyclerViewAdapter.setKey(2);
        mBinding.doctorListRecyclerview.setAdapter(doctorRecyclerViewAdapter);

        mBinding.farmRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        farmRecyclerViewAdapter = new SupplierRecyclerViewAdapter(this);
        farmRecyclerViewAdapter.setKey(2);
        mBinding.farmRecycler.setAdapter(farmRecyclerViewAdapter);
    }

    private void btnListener() {

        doctorRecyclerViewAdapter.SetOnClickListener(new SupplierRecyclerViewAdapter.SetOnClickListener() {
            @Override
            public void onClick(GetSupplierModel supplierModel) {

                if (supplierModel != null) {

                    doctorModelList.remove(supplierModel);

                }
            }
        });
        farmRecyclerViewAdapter.SetOnClickListener(new SupplierRecyclerViewAdapter.SetOnClickListener() {
            @Override
            public void onClick(GetSupplierModel supplierModel) {

                if (supplierModel != null) {

                    farmModelList.remove(supplierModel);

                }
            }
        });

        mBinding.btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBinding.registerFarmRadioBtn.isChecked()) {
                    if (regionList.size() > 0) {

                        int region = 0;
                        try {
                            region = regionHashmapForID.get(mBinding.regionSpinner.getSelectedItem().toString());

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        callingAddBtn = "";
                        callingAddBtn = "FarmAddBtn";
                        farmRecyclerViewAdapter.setCalledSupplier("Farm");
                        AddHospitalFragmentDirections.ActionAddHospitalFragmentToFarmListFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToFarmListFragment();
                        action.setKey(1);
                        action.setRegionID(region);
                        navController.navigate(action);
                    }


                } else {
                    showAddFarmDialog();

                }

            }
        });

//        mBinding.btnAddGasDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String days = Objects.requireNonNull(mBinding.etGasDays.getText()).toString();
//                if (days.length()>2)
//                {
//                    gasDaysList.add(days);
//                    gasDaysRecyclerAdapter.setGasDaysList(gasDaysList);
//
//                }
//                else {
//                    Toast.makeText(requireContext(), "Please enter days", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked()) {
                    callingAddBtn = "";
                    callingAddBtn = "MedicineAddBtn";
                    AddHospitalFragmentDirections.ActionAddHospitalFragmentToAddProductFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToAddProductFragment();
                    action.setKey(1);
                    navController.navigate(action);
                } else {

                    showDialog();
                }
            }
        });
        mBinding.btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionList.size() > 0) {

                    int region = regionHashmapForID.get(mBinding.regionSpinner.getSelectedItem().toString());
                    callingAddBtn = "";
                    callingAddBtn = "DoctorAddBtn";
                    AddHospitalFragmentDirections.ActionAddHospitalFragmentToSupplierDoctorFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToSupplierDoctorFragment();
                    action.setKey(1);
                    action.setRegionID(region);

                    navController.navigate(action);
                } else {
                    Toast.makeText(requireContext(), "Please select region", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSupplierModelForSave();
            }
        });
        mBinding.locationCheckbox.setOnClickListener(new View.OnClickListener() {
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
                            if (mBinding.locationCheckbox.isChecked()) {


                                isLocationChecked = true;
                                locationString = location.getLatitude() + "," +location.getLongitude() ;
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude())+
                                        locationString;
                                mBinding.locationCheckbox.setText(locationAddress);
                            } else {
                                isLocationChecked = false;
                                locationAddress="";
                                locationString="";
                                mBinding.locationCheckbox.setText("Enable Location");
                                mBinding.locationCheckbox.setChecked(false);
                                mBinding.idCoordinates.setText("");

                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                } else {
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(), requireContext());
                    mBinding.locationCheckbox.setChecked(false);

                }
            }
        });
        mBinding.spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradePosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regionPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                else {
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
        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getNoOfAnimals()!=0)
        {
            mBinding.etNoOfCases.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getNoOfAnimals()));
            numberOfCases =String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getNoOfAnimals());
        }
        mBinding.etAddress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());
        mBinding.etGasDays.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getGasDays());
        mBinding.etSizeOfHospital.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getSize()));

        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord() != null) {
            if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().length() > 0) {
                locationString = getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord();
            }
        }
        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress() != null) {
            if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress().length() > 0 ||
                    !getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().isEmpty()) {
                locationAddress = getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress();
                mBinding.locationCheckbox.setText(String.format("%s%s", getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress(), getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord()));
                mBinding.locationCheckbox.setChecked(true);
            }
        }


        int gradeID = getSupplierDetailModel.getSupplierModelNewList().get(0).getGrade();

        String grade = gradingHashMapForTitle.get(gradeID);

        mBinding.spinnerGrade.setSelection(gradeArray.indexOf(grade));
        gradePosition = mBinding.spinnerGrade.getSelectedItemPosition();

        int regionId = (int) getSupplierDetailModel.getSupplierModelNewList().get(0).getRegionId();

        String regionTitle = regionHashmapForTitle.get(regionId);

        mBinding.regionSpinner.setSelection(regionList.indexOf(regionTitle));
        regionPosition = mBinding.regionSpinner.getSelectedItemPosition();

        medicineModalList.clear();
        medicineModalList.addAll(getSupplierDetailModel.getSupplierItemLinkingList());
        medicineAdapter.setMedicineModalList(medicineModalList);

        doctorModelList.clear();
        for (SupplierLinking model : getSupplierDetailModel.getSupplierLinkingList()) {
            GetSupplierModel supplierModel = new GetSupplierModel();
            supplierModel.setSupplier_Name(model.getSupplierName());
            supplierModel.setAddress(model.getAddress());
            supplierModel.setSupplier_Id(model.getSupplierId());
            supplierModel.setSupplierLinkId(model.getSupplierLinkId());
            String userType = model.getUser_Type();
            if (userType != null) {
                if (userType.equals("Dr")) {
                    doctorModelList.add(supplierModel);

                } else if (userType.equals("F")) {
                    farmModelList.add(supplierModel);
                } else {
                    farmModelList.add(supplierModel);
                }
            }

        }
        farmRecyclerViewAdapter.setGetSupplierModelList(farmModelList);
        doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
        String shift = getSupplierDetailModel.getSupplierModelNewList().get(0).getShiftType();
        if (shift != null) {
            if (shift.equals("Morning")) {
                mBinding.morningRadioBtn.setChecked(true);
            } else if (shift.equals("Evening")) {
                mBinding.eveningRadioBtn.setChecked(true);
            } else if (shift.equals("FullDay")) {
                mBinding.fullDayRadioBtn.setChecked(true);
            }
        }

    }

    private void setUpSupplierModelForSave() {


        String name = "", address = "", gasDays = "", hospitalSize = "",shitType="";
        int gradeID = 0, userId, regionID = 0;
        if (mBinding.etName.getText() != null && mBinding.etName.getText().toString().length() > 0) {
            name = mBinding.etName.getText().toString();
        }
        if (mBinding.etAddress.getText() != null && mBinding.etAddress.getText().toString().length() > 0) {
            address = mBinding.etAddress.getText().toString();
        }
        if (mBinding.etName.getText() != null && mBinding.etName.getText().toString().length() > 0) {
            name = mBinding.etName.getText().toString();
        }

        if (mBinding.spinnerGrade.getSelectedItem() != null) {
            gradeID = gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());
        }
        if (mBinding.regionSpinner.getSelectedItem() != null) {
            regionID = regionHashmapForID.get(mBinding.regionSpinner.getSelectedItem().toString());
        }

        if (mBinding.etGasDays.getText() != null && mBinding.etGasDays.getText().toString().length() > 0) {
            gasDays = mBinding.etGasDays.getText().toString();

        }

        if (mBinding.etSizeOfHospital.getText() != null && mBinding.etSizeOfHospital.getText().toString().length() > 0) {
            hospitalSize = mBinding.etSizeOfHospital.getText().toString();
        }

        if (mBinding.etNoOfCases.getText() != null && mBinding.etNoOfCases.getText().toString().length() > 0) {
            numberOfCases = mBinding.etNoOfCases.getText().toString();
        }

            RadioButton radioButtonShift = mBinding.getRoot().findViewById(mBinding.TimingRadioGroup.getCheckedRadioButtonId());
            shitType = radioButtonShift.getText().toString();


        userId = SharedPreferenceHelper.getInstance(requireContext()).getUserID();


        if (name.length() > 0) {

            if (address.length() > 0) {


                SupplierModelNew supplierModelNew = new SupplierModelNew();

                supplierModelNew.setCompany_Id(1);
                supplierModelNew.setUserId(userId);
                supplierModelNew.setCountry_Id(1);
                supplierModelNew.setLocation_Id(1);
                supplierModelNew.setProject_Id(1);
                supplierModelNew.setShift_Type(shitType);
                supplierModelNew.setSupplier_Code(supplierCode);
                supplierModelNew.setSupplier_Id(supplierID);
                supplierModelNew.setRegion_Id(String.valueOf(regionID));
                supplierModelNew.setAddress(address);
                supplierModelNew.setGrade_id(gradeID);
                supplierModelNew.setGas_Days(gasDays);
                supplierModelNew.setSize(hospitalSize);
                supplierModelNew.setSupplier_Name(name);
                supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_HOSPITAL);
                supplierModelNew.setUserTypeName("H");
                supplierModelNew.setLoc_Cord(locationString);
                supplierModelNew.setLoc_Cord_Address(locationAddress);
                supplierModelNew.setIs_update_Loc_Cord(isLocationUpdate);
                supplierModelNew.setNo_Of_Animals(numberOfCases);
                supplierModelNew.setSupplierItemLinkingList(medicineModalList);
                supplierModelNew.setCell_No("0");
                supplierModelNew.setMobile_No("0");
                supplierModelNew.setPhone_No("0");

                List<SupplierLinking> supplierLinkingList = new ArrayList<>();
                for (GetSupplierModel model : doctorModelList) {
                    SupplierLinking supplierLinking = new SupplierLinking();
                    supplierLinking.setAddress(model.getAddress());
                    supplierLinking.setIsRegistered(true);
                    supplierLinking.setSupplierId(model.getSupplier_Id());
                    if (model.getSupplierLinkId() != null && model.getSupplierLinkId().length() > 0) {
                        supplierLinking.setSupplierLinkId(model.getSupplierLinkId());
                    } else {
                        supplierLinking.setSupplierLinkId("0");
                    }
                    supplierLinking.setType("Dr");
                    supplierLinking.setSupplierName(model.getSupplier_Name());
                    supplierLinkingList.add(supplierLinking);
                }
                for (GetSupplierModel model : farmModelList) {
                    SupplierLinking supplierLinking = new SupplierLinking();
                    supplierLinking.setAddress(model.getAddress());
                    supplierLinking.setIsRegistered(model.isRegister());
                    supplierLinking.setSupplierId(model.getSupplier_Id());
                    if (model.getSupplierLinkId() != null && model.getSupplierLinkId().length() > 0) {
                        supplierLinking.setSupplierLinkId(model.getSupplierLinkId());
                    } else {
                        supplierLinking.setSupplierLinkId("0");
                    }

                    supplierLinking.setType("F");
                    supplierLinking.setSupplierName(model.getSupplier_Name());
                    supplierLinkingList.add(supplierLinking);
                }
                supplierModelNew.setSupplierLinkingList(supplierLinkingList);

                saveSupplier(supplierModelNew);


            } else {
                Toast.makeText(requireContext(), "Please enter the address", Toast.LENGTH_SHORT).show();
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
                else {
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

    private void refreshFields() {

        mBinding.etName.setText("");
        mBinding.etAddress.setText("");
        mBinding.locationCheckbox.setText("");
        mBinding.locationCheckbox.setText("");
        mBinding.locationCheckbox.setChecked(false);
        mBinding.etSizeOfHospital.setText("");
        mBinding.etNoOfCases.setText("");
        doctorModelList.clear();
        doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
        farmModelList.clear();
        farmRecyclerViewAdapter.setGetSupplierModelList(farmModelList);
        mBinding.etGasDays.setText("");
        medicineModalList.clear();
        medicineAdapter.setMedicineModalList(medicineModalList);
        locationAddress="";
        locationString="";
        isLocationUpdate=false;
        isLocationChecked=false;
        supplierID=0;
    }

    private void showAddFarmDialog() {

        OtherFarmDialogBinding binding = OtherFarmDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).create();
        alertDialog.show();

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Objects.requireNonNull(binding.etFarmName.getText()).toString();

                if (name.length() > 0) {

                    GetSupplierModel farmSupplierModel = new GetSupplierModel();
                    if (binding.etFarmAddress.getText() != null && binding.etFarmAddress.getText().toString().length() > 0) {
                        farmSupplierModel.setAddress(binding.etFarmAddress.getText().toString());
                    }

                    farmSupplierModel.setSupplier_Name(name);
                    farmSupplierModel.setRegister(false);
                    farmModelList.add(farmSupplierModel);
                    farmRecyclerViewAdapter.setGetSupplierModelList(farmModelList);
                } else {
                    Toast.makeText(requireContext(), " Please enter name", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
    }


    private void setUpGradeSpinner() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
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

    private void showDialog() {


        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();


        AddMedicineDialogBinding binding = AddMedicineDialogBinding.inflate(getLayoutInflater());
        alertDialog.setView(binding.getRoot());
        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = Objects.requireNonNull(binding.etCompany.getText()).toString();
                String medicineName = Objects.requireNonNull(binding.etName.getText()).toString();
                if (companyName.length() > 0) {
                    if (medicineName.length() > 0) {
                        SupplierItemLinking medicineModal = new SupplierItemLinking();
                        medicineModal.setCompName(companyName);
                        medicineModal.setIsRegistered(false);
                        medicineModal.setItHead(medicineName);
                        medicineModal.setSupplierItemLinkIdDtl("0");
                        medicineModalList.add(medicineModal);
                        medicineAdapter.setMedicineModalList(medicineModalList);
                    } else {
                        Toast.makeText(requireContext(), "Please enter medicine name", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(requireContext(), "Please enter company", Toast.LENGTH_SHORT).show();

                }

                alertDialog.dismiss();
            }
        });
    }

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
                    if (response.body() != null) {
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
                        mBinding.regionSpinner.setAdapter(adapter);
                    }

                } else {

                    Toast.makeText(requireContext(), " " + response.message(), Toast.LENGTH_SHORT).show();
                    if (response.message().equals("Unauthorized")) {
                        CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                    }
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