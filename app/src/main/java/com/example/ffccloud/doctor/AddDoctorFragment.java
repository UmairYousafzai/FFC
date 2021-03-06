package com.example.ffccloud.doctor;

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

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddDoctorBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentArgs;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.model.ClassificationModel;
import com.example.ffccloud.model.GetSupplierDetailModel;
import com.example.ffccloud.model.GradingModel;
import com.example.ffccloud.model.QualificationModel;
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

public class AddDoctorFragment extends Fragment {

    private FragmentAddDoctorBinding mBinding;
    private final ArrayList<String> regionList = new ArrayList<>();
    private final ArrayList<String> gradeArray = new ArrayList<>();
    private final ArrayList<String> genderArray = new ArrayList<>();
    private final ArrayList<String> classificationArray = new ArrayList<>();
    private final ArrayList<String> qualificationArray = new ArrayList<>();
    private List<ClassificationModel> classificationModelList;
    private List<QualificationModel> qualificationModelList;
    private List<GradingModel> gradingModelList;
    private final HashMap<String, Integer> classificatoinHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> qualificationHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> regionHashmapID = new HashMap<>();

    private final HashMap<Integer, String> qualificationHashMapForTitle = new HashMap<>();
    private final HashMap<Integer, String> classificationHashMapForTitle = new HashMap<>();
    private final HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();
    private final HashMap<Integer, String> regionHashmapTitle = new HashMap<>();
    private UserViewModel userViewModel;
    private NavController navController;
    private MedicineAdapter medicineAdapter;
    private String locationString,supplierCode="0";
    private String locationAddress;
    private final List<SupplierItemLinking> medicineModalList = new ArrayList<>();
    private int supplierID=0;
    private String callingAddBtn;
    private boolean isSpinnerUpdate = false,isLocationUpdate=false;
    private int gradePosition = -1, regionPosition = -1, classificationPosition = -1, qualificationPosition = -1, genderPosition = -1;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mBinding = FragmentAddDoctorBinding.inflate(inflater, container, false);
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
            getUserSyncData();
        }


        assert getArguments() != null;
        supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();
        if (supplierID > 0 && callingAddBtn == null) {

            //setup fields if edit request has been made
            getSupplierByID(supplierID);
        }

    }

    public void getLiveData() {
        MutableLiveData<InsertProductModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        liveData.observe(getViewLifecycleOwner(), new Observer<InsertProductModel>() {
            @Override
            public void onChanged(InsertProductModel model) {
                if (model != null) {
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
        });

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
                    mBinding.location.setText("Enable Location");
                    mBinding.location.setChecked(false);
                    mBinding.idCoordinates.setText(addressCoordinates);
                    isLocationUpdate=true;

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (callingAddBtn != null) {
            ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderArray);
            ArrayAdapter<String> classificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, classificationArray);
            mBinding.spinnerClassification.setAdapter(classificationAdapter);
            mBinding.spinnerGender.setAdapter(genderAdapter);

            ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, qualificationArray);
            mBinding.spinnerQualification.setAdapter(qualificationAdapter);

            ArrayAdapter<String> gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, gradeArray);
            mBinding.spinnerGrade.setAdapter(gradingAdapter);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
            mBinding.spinnerRegion.setAdapter(adapter);

            medicineAdapter.setMedicineModalList(medicineModalList);
            if (regionPosition != -1) {
                mBinding.spinnerRegion.setSelection(regionPosition);

            }
            if (gradePosition != -1) {
                mBinding.spinnerGrade.setSelection(gradePosition);
            }
            if (classificationPosition != -1) {
                mBinding.spinnerClassification.setSelection(classificationPosition);
            }
            if (qualificationPosition != -1) {
                mBinding.spinnerQualification.setSelection(qualificationPosition);
            }
            if (genderPosition != -1) {
                mBinding.spinnerGender.setSelection(genderPosition);
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
                    mBinding.location.setChecked(false);
                    mBinding.location.setText("Enable Location");
                    locationAddress="";
                }
            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.medicineRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicineAdapter = new MedicineAdapter();
        mBinding.medicineRecycler.setAdapter(medicineAdapter);
    }

    private void btnListener() {
        mBinding.btnLocationPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callingAddBtn="locationPicker";
                navController.navigate(R.id.action_addDoctorFragment_to_location_pickerFragment);
            }
        });

        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked()) {
                    callingAddBtn = "";
                    callingAddBtn = "MedicineAddBtn";
                    AddDoctorFragmentDirections.ActionAddDoctorFragmentToAddProductFragment action = AddDoctorFragmentDirections.actionAddDoctorFragmentToAddProductFragment();
                    action.setKey(1);
                    navController.navigate(action);
                } else {

                    showDialog();
                }
            }
        });

        mBinding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomLocation customLocation = new CustomLocation(requireContext());

                if (customLocation.isLocationEnabled()) {
                    CustomLocation.CustomLocationResults results = new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {

                            if (supplierID!=0)
                            {
                                isLocationUpdate=true;
                            }
                            if (mBinding.location.isChecked()) {
                                locationString = location.getLatitude() + "," +location.getLongitude() ;
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.location.setText(locationAddress+ locationString);
                            } else {
                                locationAddress="";
                                locationString="";
                                mBinding.location.setText("Enable Location");
                                mBinding.location.setChecked(false);
                                mBinding.idCoordinates.setText("");
                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                } else {
                    mBinding.location.setChecked(false);
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(), requireContext());

                }
            }
        });


        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSupplierModelForSave();
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

        mBinding.spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regionPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.spinnerQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualificationPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.spinnerClassification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classificationPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderPosition = position;

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
                } else {
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
        mBinding.idDocName.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierName());
        mBinding.idDocPhone.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getPhoneNo());
        mBinding.idDocAdress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());
        mBinding.idDocEmail.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getEmail());

        if(getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress().length()>0 ||
                getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().length()>0)
        {
            mBinding.location.setText(String.format("%s%s", getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress(), getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord()));
            mBinding.location.setChecked(true);
            locationAddress=getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress();
        }

        if (getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord().length()>0)
        {
            mBinding.location.setChecked(true);

            locationString=getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord();
        }

        medicineModalList.clear();
        if (getSupplierDetailModel.getSupplierItemLinkingList() != null) {
            medicineModalList.addAll(getSupplierDetailModel.getSupplierItemLinkingList());
            medicineAdapter.setMedicineModalList(medicineModalList);
        }


        int classificationId = getSupplierDetailModel.getSupplierModelNewList().get(0).getClassificationId();
        int qualificationID = getSupplierDetailModel.getSupplierModelNewList().get(0).getQualificationId();
        int gradeID = getSupplierDetailModel.getSupplierModelNewList().get(0).getGrade();
        int regionID = (int)getSupplierDetailModel.getSupplierModelNewList().get(0).getRegionId();


        String classification = classificationHashMapForTitle.get(classificationId);
        String qualification = qualificationHashMapForTitle.get(qualificationID);
        String grade = gradingHashMapForTitle.get(gradeID);
        String region = regionHashmapTitle.get(regionID);

        mBinding.spinnerClassification.setSelection(classificationArray.indexOf(classification));
        mBinding.spinnerQualification.setSelection(qualificationArray.indexOf(qualification));
        mBinding.spinnerGrade.setSelection(gradeArray.indexOf(grade));
        mBinding.spinnerRegion.setSelection(regionList.indexOf(region));

        String gender = getSupplierDetailModel.getSupplierModelNewList().get(0).getGender();
        if (gender != null) {
            switch (gender) {
                case "Male":
                    mBinding.spinnerGender.setSelection(genderArray.indexOf("Male"));
                    break;
                case "Female":
                    mBinding.spinnerGender.setSelection(genderArray.indexOf("Female"));
                    break;
                case "Other":
                    mBinding.spinnerGender.setSelection(genderArray.indexOf("Other"));
                    break;
            }
        }

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

        String name = "", phone = "", address = "", email = "", shitType = "";
        int userId, region = 0, gradeID = 0;

        if (mBinding.idDocName.getText()!=null && mBinding.idDocName.getText().toString().length()>0)
        {
            name = mBinding.idDocName.getText().toString();
        }

        if (mBinding.idDocPhone.getText()!=null && mBinding.idDocPhone.getText().toString().length()>0)
        {
            phone = Objects.requireNonNull(mBinding.idDocPhone.getText()).toString();

        }

        if (mBinding.idDocAdress.getText()!=null && mBinding.idDocAdress.getText().toString().length()>0)
        {
            address = mBinding.idDocAdress.getText().toString();
        }
        if (mBinding.idDocEmail.getText()!=null && mBinding.idDocEmail.getText().toString().length()>0)
        {
            email = mBinding.idDocEmail.getText().toString();
        }

        userId = SharedPreferenceHelper.getInstance(requireContext()).getUserID();

        try {

            if (gradingModelList.size() > 0) {
                gradeID = gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());

            }
            RadioButton radioButton = mBinding.getRoot().findViewById(mBinding.docTimingRadioGroup.getCheckedRadioButtonId());
            shitType = radioButton.getText().toString();
            region = regionHashmapID.get(mBinding.spinnerRegion.getSelectedItem().toString());

        } catch (Exception e) {
            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (!name.isEmpty() ) {
            mBinding.etNameLayout.setError(null);
            if (!phone.isEmpty()) {

                mBinding.etPhoneLayout.setError(null);

                if (!address.isEmpty()) {

                    mBinding.etAddressLayout.setError(null);

                    if (regionList.size() > 0) {


                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompany_Id(1);
                        supplierModelNew.setUserId(userId);
                        supplierModelNew.setCountry_Id(1);
                        supplierModelNew.setLocation_Id(1);
                        supplierModelNew.setProject_Id(1);
                        supplierModelNew.setSupplier_Code(supplierCode);
                        supplierModelNew.setSupplier_Id(supplierID);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setPhone_No(phone);
                        supplierModelNew.setSupplier_Name(name);
                        supplierModelNew.setRegion_Id(String.valueOf(region));
                        supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_DOCTOR);
                        supplierModelNew.setUserTypeName("Dr");
                        supplierModelNew.setGrade_id(gradeID);
                        supplierModelNew.setEmail(email);
                        supplierModelNew.setSupplierItemLinkingList(medicineModalList);
                        supplierModelNew.setIs_update_Loc_Cord(isLocationUpdate);
                        supplierModelNew.setLoc_Cord(locationString);
                        supplierModelNew.setLoc_Cord_Address(locationAddress);
                        supplierModelNew.setQualification_Id(qualificationHashMapForId.get(mBinding.spinnerQualification.getSelectedItem().toString()));
                        supplierModelNew.setClassification_Id(classificatoinHashMapForId.get(mBinding.spinnerClassification.getSelectedItem().toString()));
                        supplierModelNew.setGender(mBinding.spinnerGender.getSelectedItem().toString());
                        supplierModelNew.setShift_Type(shitType);


                        saveSupplier(supplierModelNew);
                    } else {

                        mBinding.spinnerRegion.requestFocus();
                        Toast.makeText(requireContext(), "Please select region", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mBinding.etAddressLayout.setError("Please enter the address");
                    mBinding.idDocAdress.requestFocus();
                    Toast.makeText(requireContext(), "Please enter the address", Toast.LENGTH_SHORT).show();
                }

            } else {
                mBinding.etPhoneLayout.setError("Please enter the phone number");
                mBinding.idDocPhone.requestFocus();
                Toast.makeText(requireContext(), "Please enter the phone number", Toast.LENGTH_SHORT).show();
            }

        } else {
            mBinding.etNameLayout.setError("Please enter the name");
            mBinding.idDocName.requestFocus();
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

    public void getUserSyncData() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        userViewModel.getAllClassification().observe(getViewLifecycleOwner(), new Observer<List<ClassificationModel>>() {
            @Override
            public void onChanged(List<ClassificationModel> classificationModels) {

                classificationModelList = classificationModels;


                setClassificationSpinners();
                progressDialog.dismiss();

            }
        });
        userViewModel.getAllQualification().observe(getViewLifecycleOwner(), new Observer<List<QualificationModel>>() {
            @Override
            public void onChanged(List<QualificationModel> qualificationModels) {
                qualificationModelList = qualificationModels;

                setQualificationSpinners();
                progressDialog.dismiss();
            }
        });
        userViewModel.getAllGrades().observe(getViewLifecycleOwner(), new
                Observer<List<GradingModel>>() {
                    @Override
                    public void onChanged(List<GradingModel> gradingModels) {
                        gradingModelList = gradingModels;

                        setGradeSpinners();

                        progressDialog.dismiss();

                    }
                });
    }


    public void setClassificationSpinners() {
        genderArray.clear();
        classificationArray.clear();
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Other");

        for (ClassificationModel model : classificationModelList) {
            classificationArray.add(model.getClassification_Title());
            classificatoinHashMapForId.put(model.getClassification_Title(), model.getClassification_Id());
            classificationHashMapForTitle.put(model.getClassification_Id(), model.getClassification_Title());
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderArray);
        ArrayAdapter<String> classificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, classificationArray);
        mBinding.spinnerClassification.setAdapter(classificationAdapter);
        mBinding.spinnerGender.setAdapter(genderAdapter);


    }

    public void setQualificationSpinners() {

        qualificationArray.clear();


        for (QualificationModel model : qualificationModelList) {
            qualificationArray.add(model.getQualification_Title());
            qualificationHashMapForId.put(model.getQualification_Title(), model.getQualification_Id());
            qualificationHashMapForTitle.put(model.getQualification_Id(), model.getQualification_Title());

        }


        ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, qualificationArray);
        mBinding.spinnerQualification.setAdapter(qualificationAdapter);


    }

    public void setGradeSpinners() {

        gradeArray.clear();


        for (GradingModel model : gradingModelList) {
            gradeArray.add(model.getGrade_Title());
            gradingHashMapForId.put(model.getGrade_Title(), model.getGrade_Id());
            gradingHashMapForTitle.put(model.getGrade_Id(), model.getGrade_Title());

        }


        ArrayAdapter<String> gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, gradeArray);
        mBinding.spinnerGrade.setAdapter(gradingAdapter);


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
                    if (response.body()!=null)
                    {
                        regionHashmapID.clear();
                        regionList.clear();
                        List<RegionModel> regionModelList = new ArrayList<>();
                        regionModelList = response.body();
                        for (RegionModel model : regionModelList) {
                            regionList.add(model.getName());
                            regionHashmapID.put(model.getName(), model.getRegionId());
                            regionHashmapTitle.put(model.getRegionId(), model.getName());

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
                        mBinding.spinnerRegion.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(requireContext(), " " + response.message(), Toast.LENGTH_SHORT).show();
                    if (response.message().equals("Unauthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext());
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

}