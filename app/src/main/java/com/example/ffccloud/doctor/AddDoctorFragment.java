package com.example.ffccloud.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

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

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.SupplierMainModel;
import com.example.ffccloud.ModelClasses.SupplierModelNew;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.ModelClasses.QualificationModel;
import com.example.ffccloud.ModelClasses.RegionModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentAddDoctorBinding;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
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

public class AddDoctorFragment extends Fragment {

    private FragmentAddDoctorBinding mBinding;
    private ArrayList<String> regionList = new ArrayList<>(), gradeArray = new ArrayList<>(), genderArray = new ArrayList<>(), classificationArray = new ArrayList<>(), qualificationArray = new ArrayList<>();
    private List<ClassificationModel> classificationModelList;
    private List<QualificationModel> qualificationModelList;
    private List<GradingModel> gradingModelList;
    private final HashMap<String, Integer> classificatoinHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> qualificationHashMapForId = new HashMap<>();
    private HashMap<String, Integer> regionHashmap = new HashMap<>();

    private HashMap<Integer, String> qualificationHashMapForTitle = new HashMap<>();
    private HashMap<Integer, String> classificationHashMapForTitle = new HashMap<>();
    private HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();
    private UserViewModel userViewModel;
    private NavController navController;
    private MedicineAdapter medicineAdapter;
    private String locationString;
    private String locationAddress;


    private List<SupplierItemLinking> medicineModalList= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        MutableLiveData<InsertProductModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        liveData.observe(getViewLifecycleOwner(), new Observer<InsertProductModel>() {
            @Override
            public void onChanged(InsertProductModel model) {
                if (model != null) {
                    SupplierItemLinking medicineModal = new SupplierItemLinking();
                    medicineModal.setItHead(model.getTitleProduct());
                    medicineModal.setIsRegistered(true);
                    medicineModal.setItCode(String.valueOf(model.getItem_Code()));
                    medicineModal.setSupplierItemLinkIdDtl(0);


                    medicineModalList.add(medicineModal);
                    medicineAdapter.setMedicineModalList(medicineModalList);


                }


            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();
        btnListener();
        getUserSyncData();
        getRegion();
    }

    private void setUpRecyclerView() {

        mBinding.medicineRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicineAdapter = new MedicineAdapter();
        mBinding.medicineRecycler.setAdapter(medicineAdapter);
    }
    private void btnListener() {

        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked())
                {
                    AddDoctorFragmentDirections.ActionAddDoctorFragmentToAddProductFragment action = AddDoctorFragmentDirections.actionAddDoctorFragmentToAddProductFragment();
                    action.setKey(1);
                    navController.navigate(action);
                }
                else {

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


                            if (mBinding.location.isChecked()) {
                                 locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.location.setText(locationAddress);
                                 locationString = String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude());
                            } else {

                                mBinding.location.setText("Enable Location");
                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                } else {
                    showOpenLocationSettingDialog();

                }
            }
        });


        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSupplierModelForSave();
            }
        });
    }
    private void setUpSupplierModelForSave() {

        String name = Objects.requireNonNull(mBinding.idDocName.getText()).toString();
        String phone = Objects.requireNonNull(mBinding.idDocPhone.getText()).toString();
        String address = Objects.requireNonNull(mBinding.idDocAdress.getText()).toString();
        String email = Objects.requireNonNull(mBinding.idDocEmail.getText()).toString();
        int region = 0;
        int gradeID=0;
        if (gradingModelList.size()>0)
        {
            gradeID= gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());

        }        RadioButton radioButton = mBinding.getRoot().findViewById(mBinding.docTimingRadioGroup.getCheckedRadioButtonId());
        String shitType = radioButton.getText().toString();


        if (name.length() > 0) {
            if (phone.length() > 0) {
                if (address.length() > 0) {
                    if (regionList.size()>0) {
                        region = regionHashmap.get(mBinding.spinnerRegion.getSelectedItem().toString());

                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompanyId(1);
                        supplierModelNew.setCountryId(1);
                        supplierModelNew.setLocationId(1);
                        supplierModelNew.setProjectId(1);
                        supplierModelNew.setSupplierCode(0);
                        supplierModelNew.setSupplierId(0);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setPhoneNo(phone);
                        supplierModelNew.setSupplierName(name);
                        supplierModelNew.setRegionId(String.valueOf(region));
                        supplierModelNew.setUserSubType(CONSTANTS.USER_SUB_TYPE_DOCTOR);
                        supplierModelNew.setUserTypeName("Dr");
                        supplierModelNew.setGradeId(gradeID);
                        supplierModelNew.setEmail(email);
                        supplierModelNew.setSupplierItemLinkingList(medicineModalList);
                        supplierModelNew.setLoc_Cord(locationString);
                        supplierModelNew.setLoc_Cord_Address(locationAddress);
                        supplierModelNew.setQualification_Id(qualificationHashMapForId.get(mBinding.spinnerQualification.getSelectedItem().toString()));
                        supplierModelNew.setClassification_Id(classificatoinHashMapForId.get(mBinding.spinnerClassification.getSelectedItem().toString()));
                        supplierModelNew.setGender(mBinding.spinnerGender.getSelectedItem().toString());
                        supplierModelNew.setShift_Type(shitType);


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

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSupplier(model);

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
    public void getUserSyncData() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());

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
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), " " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
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


        AddMedicineDialogBinding binding= AddMedicineDialogBinding.inflate(getLayoutInflater());
        alertDialog.setView(binding.getRoot());
        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName=Objects.requireNonNull(binding.etCompany.getText()).toString();
                String medicineName=Objects.requireNonNull(binding.etName.getText()).toString();
                if (companyName.length()>0)
                {
                    if (medicineName.length()>0)
                    {
                        SupplierItemLinking medicineModal = new SupplierItemLinking();
                        medicineModal.setCompName(companyName);
                        medicineModal.setIsRegistered(false);
                        medicineModal.setItHead(medicineName);
                        medicineModal.setSupplierItemLinkIdDtl(0);
                        medicineModalList.add(medicineModal);
                        medicineAdapter.setMedicineModalList(medicineModalList);
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Please enter medicine name", Toast.LENGTH_SHORT).show();

                    }


                }else
                {
                    Toast.makeText(requireContext(), "Please enter company", Toast.LENGTH_SHORT).show();

                }

                alertDialog.dismiss();
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