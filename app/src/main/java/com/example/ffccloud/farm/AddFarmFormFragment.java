package com.example.ffccloud.farm;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.adapter.SupplierRecyclerViewAdapter;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddFarmFormBinding;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.model.GetSupplierDetailModel;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.model.SupplierLinking;
import com.example.ffccloud.model.SupplierMainModel;
import com.example.ffccloud.model.SupplierModelNew;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFarmFormFragment extends Fragment {

    private FragmentAddFarmFormBinding mBinding;
    private NavController navController;
    private final List<ContactPersons> contactPersonsList = new ArrayList<>();
    private final List<SupplierItemLinking> medicineModalList = new ArrayList<>();
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private MedicineAdapter medicineAdapter;
    private final ArrayList<String> animalArrayList = new ArrayList<>();
    private final ArrayList<String> regionList = new ArrayList<>();
    private final HashMap<String, Integer> regionHashmapForID = new HashMap<>();
    private final HashMap<Integer, String> regionHashmapForTitle = new HashMap<>();
    private String locationString,supplierCode="0";
    private String locationAddress;
    private int supplierID = 0;
    private final List<GetSupplierModel> supplierDetailModelList = new ArrayList<>();
    private SupplierRecyclerViewAdapter supplierRecyclerViewAdapter;
    private String callingAddBtn;
    private boolean isSpinnerUpdate = false,isLocationUpdate=false;
    private int animalPosition = -1, regionPosition = -1;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddFarmFormBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        if (!isSpinnerUpdate) {
            isSpinnerUpdate = true;
            setupAnimalSpinner();
            getRegion();
        }

        setUpRecyclerView();
        getLiveData();
        assert getArguments() != null;
        supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();
        if (supplierID > 0 && callingAddBtn == null) {

            //setup fields if edit request has been made
            getSupplierByID(supplierID);
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        if (callingAddBtn != null) {
            medicineAdapter.setMedicineModalList(medicineModalList);
            supplierRecyclerViewAdapter.setGetSupplierModelList(supplierDetailModelList);
            contactRecyclerAdapter.setContactPersonsList(contactPersonsList);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, animalArrayList);
            mBinding.animalSpinner.setAdapter(adapter);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
            mBinding.regionSpinner.setAdapter(adapter1);
            if (regionPosition != -1) {
                mBinding.regionSpinner.setSelection(regionPosition);

            }

            if (animalPosition != -1) {
                mBinding.animalSpinner.setSelection(animalPosition);

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
    public void getLiveData() {

        MutableLiveData<GetSupplierModel> supplierLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.DOCTOR_SUPPLIER_KEY);
        supplierLiveData.observe(getViewLifecycleOwner(), new Observer<GetSupplierModel>() {
            @Override
            public void onChanged(GetSupplierModel model) {
                if (model != null) {
                    if (callingAddBtn.equals("DoctorAddBtn")) {
                        if (supplierDetailModelList.size() > 0) {
                            if (supplierDetailModelList.get(supplierDetailModelList.size() - 1) != model) {
                                supplierDetailModelList.add(model);
                                supplierRecyclerViewAdapter.setGetSupplierModelList(supplierDetailModelList);
                            }
                        } else {
                            supplierDetailModelList.add(model);
                        }


                    }

                }


            }
        });

        MutableLiveData<InsertProductModel> productLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        productLiveData.observe(getViewLifecycleOwner(), new Observer<InsertProductModel>() {
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
        mBinding.etOwnerName.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getSupplierName());
        mBinding.etContact.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getPhoneNo());
        mBinding.etAddress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());
        if (!getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress().isEmpty()) {
            mBinding.locationCheckbox.setChecked(true);
            mBinding.locationCheckbox.setText(String.format("%s%s", getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress(), getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord()));
            locationAddress= getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCordAddress();
            locationString =getSupplierDetailModel.getSupplierModelNewList().get(0).getLocCord();
        }


        mBinding.etNumberOfAnimal.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getNoOfAnimals()));

        int regionId = (int) getSupplierDetailModel.getSupplierModelNewList().get(0).getRegionId();

        String regionTitle = regionHashmapForTitle.get(regionId);

        mBinding.regionSpinner.setSelection(regionList.indexOf(regionTitle));


        medicineModalList.clear();
        medicineModalList.addAll(getSupplierDetailModel.getSupplierItemLinkingList());
        medicineAdapter.setMedicineModalList(medicineModalList);
        contactPersonsList.clear();
        contactPersonsList.addAll(getSupplierDetailModel.getContactPersonsList());
        contactRecyclerAdapter.setContactPersonsList(contactPersonsList);

        supplierDetailModelList.clear();
        for (SupplierLinking model : getSupplierDetailModel.getSupplierLinkingList()) {
            GetSupplierModel supplierModel = new GetSupplierModel();
            supplierModel.setSupplier_Name(model.getSupplierName());
            supplierModel.setAddress(model.getAddress());
            supplierModel.setSupplier_Id(model.getSupplierId());
            supplierModel.setSupplierLinkId(model.getSupplierLinkId());
            supplierDetailModelList.add(supplierModel);
        }
        supplierRecyclerViewAdapter.setGetSupplierModelList(supplierDetailModelList);

        String animalsMainType = getSupplierDetailModel.getSupplierModelNewList().get(0).getAnimalsMainType();
        String animalsSUbType = getSupplierDetailModel.getSupplierModelNewList().get(0).getAnimalsSubType();

        switch (animalsMainType) {
            case "Large animal":
                mBinding.largeAnimalRadioBtn.setChecked(true);
                setupAnimalSpinner();
                mBinding.animalSpinner.setSelection(animalArrayList.indexOf(animalsSUbType));
                break;
            case "Sheep Goat":
                mBinding.SheepGoatRadioBtn.setChecked(true);
                setupAnimalSpinner();
                mBinding.animalSpinner.setSelection(animalArrayList.indexOf(animalsSUbType));

                break;
            case "Poultry":
                mBinding.poultryRadioBtn.setChecked(true);
                setupAnimalSpinner();
                mBinding.animalSpinner.setSelection(animalArrayList.indexOf(animalsSUbType));

                break;
        }

        String shift = getSupplierDetailModel.getSupplierModelNewList().get(0).getShiftType();
        if (shift != null) {
            if (shift.equals("Morning")) {
                mBinding.morningRadioBtn.setChecked(true);
            } else if (shift.equals("Evening")) {
                mBinding.eveningRadioBtn.setChecked(true);
            }
        }

    }

    private void setupAnimalSpinner() {

        animalArrayList.clear();
        if (mBinding.largeAnimalRadioBtn.isChecked()) {
            animalArrayList.add("Frezein");
            animalArrayList.add("Cross Breed");
            animalArrayList.add("Sahiwal");
            animalArrayList.add("Any Other");
        } else if (mBinding.SheepGoatRadioBtn.isChecked()) {
            animalArrayList.add("N/A");
        } else if (mBinding.poultryRadioBtn.isChecked()) {
            animalArrayList.add("Layer");
            animalArrayList.add("Broiler");
            animalArrayList.add("Domestic");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, animalArrayList);
        mBinding.animalSpinner.setAdapter(adapter);
    }

    private void setUpRecyclerView() {

        //setting up contact recycler
        mBinding.contactRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        contactRecyclerAdapter = new ContactRecyclerAdapter();
        mBinding.contactRecycler.setAdapter(contactRecyclerAdapter);

        //setting up medicine adapter
        mBinding.medicineRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicineAdapter = new MedicineAdapter();
        mBinding.medicineRecycler.setAdapter(medicineAdapter);

        //setting up doctor recycler

        mBinding.doctorListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        supplierRecyclerViewAdapter = new SupplierRecyclerViewAdapter(this);
        supplierRecyclerViewAdapter.setKey(2);
        mBinding.doctorListRecyclerview.setAdapter(supplierRecyclerViewAdapter);

    }

    private void btnListener() {
        supplierRecyclerViewAdapter.SetOnClickListener(new SupplierRecyclerViewAdapter.SetOnClickListener() {
            @Override
            public void onClick(GetSupplierModel supplierModel) {


                supplierDetailModelList.remove(supplierModel);
            }
        });

        mBinding.btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionList.size() > 0) {
                    callingAddBtn = "";
                    callingAddBtn = "DoctorAddBtn";
                    int region = regionHashmapForID.get(mBinding.regionSpinner.getSelectedItem().toString());
                    AddFarmFormFragmentDirections.ActionAddFarmFormFragmentToSearchDoctorFragment action = AddFarmFormFragmentDirections.actionAddFarmFormFragmentToSearchDoctorFragment();
                    action.setKey(1);
                    action.setRegionID(region);
                    navController.navigate(action);
                } else {
                    Toast.makeText(requireContext(), "Please select region", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked()) {
                    callingAddBtn = "";
                    callingAddBtn = "MedicineAddBtn";
                    AddFarmFormFragmentDirections.ActionAddFarmFormFragmentToAddProductFragment action = AddFarmFormFragmentDirections.actionAddFarmFormFragmentToAddProductFragment();
                    action.setKey(1);


                    navController.navigate(action);
                } else {

                    //1 key is for the understanding for function to show medicine dialog
                    showDialog(1);
                }


            }


        });


        mBinding.btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingAddBtn = "";
                callingAddBtn = "ContactAddBtn";
                showDialog(2);
            }
        });

        mBinding.animalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setupAnimalSpinner();
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

                                locationString = location.getLatitude() + "," +location.getLongitude() ;
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude())+
                                        locationString;
                                mBinding.locationCheckbox.setText(locationAddress);
                            } else {
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
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());
                    mBinding.locationCheckbox.setChecked(false);

                }
            }
        });

        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpSupplierModelForSave();
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

        mBinding.animalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animalPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpSupplierModelForSave() {

        String name = "", phone = "", address = "", animalMainType = "", animalSubType = "", numberOfAnimal = "",shitType="";
        RadioButton radioButton = mBinding.getRoot().findViewById(mBinding.animalRadioGroup.getCheckedRadioButtonId());

        int userId = 0, region = 0;

        try {
            name = Objects.requireNonNull(mBinding.etOwnerName.getText()).toString();
            phone = Objects.requireNonNull(mBinding.etContact.getText()).toString();
            address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
            animalMainType = radioButton.getText().toString();
            animalSubType = mBinding.animalSpinner.getSelectedItem().toString();
            RadioButton radioButtonShift = mBinding.getRoot().findViewById(mBinding.docTimingRadioGroup.getCheckedRadioButtonId());
            shitType = radioButtonShift.getText().toString();
            userId = SharedPreferenceHelper.getInstance(requireContext()).getUserID();

            region = regionHashmapForID.get(mBinding.regionSpinner.getSelectedItem().toString());

            numberOfAnimal = mBinding.etNumberOfAnimal.getText().toString();
        } catch (Exception e) {
            Log.e("Farm save error:",e.getMessage());
        }

        if (name.length() > 0) {
            if (phone.length() > 0) {
                if (address.length() > 0) {
                    if (regionList.size() > 0) {


                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompany_Id(1);
                        supplierModelNew.setUserId(userId);
                        supplierModelNew.setCountry_Id(1);
                        supplierModelNew.setLocation_Id(1);
                        supplierModelNew.setProject_Id(1);
                        supplierModelNew.setShift_Type(shitType);
                        supplierModelNew.setSupplier_Code(supplierCode);
                        supplierModelNew.setSupplier_Id(supplierID);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setPhone_No(phone);
                        supplierModelNew.setSupplier_Name(name);
                        supplierModelNew.setRegion_Id(String.valueOf(region));
                        supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_FARM);
                        supplierModelNew.setUserTypeName("F");
                        supplierModelNew.setAnimals_Main_Type(animalMainType);
                        supplierModelNew.setAnimals_Sub_Type(animalSubType);
                        supplierModelNew.setNo_Of_Animals(numberOfAnimal);
                        supplierModelNew.setSupplierItemLinkingList(medicineModalList);
                        supplierModelNew.setContactPersonsList(contactPersonsList);
                        supplierModelNew.setLoc_Cord(locationString);
                        supplierModelNew.setIs_update_Loc_Cord(isLocationUpdate);
                        supplierModelNew.setLoc_Cord_Address(locationAddress);
                        List<SupplierLinking> supplierLinkingList = new ArrayList<>();
                        for (GetSupplierModel model : supplierDetailModelList) {
                            SupplierLinking supplierLinking = new SupplierLinking();
                            supplierLinking.setAddress(model.getAddress());
                            supplierLinking.setIsRegistered(true);
                            if (model.getSupplierLinkId() != null && model.getSupplierLinkId().length() > 0) {
                                supplierLinking.setSupplierLinkId(model.getSupplierLinkId());
                            } else {
                                supplierLinking.setSupplierLinkId("0");
                            }
                            supplierLinking.setSupplierId(model.getSupplier_Id());
                            supplierLinking.setSupplierLinkId("0");
                            supplierLinking.setSupplierName(model.getSupplier_Name());
                            supplierLinkingList.add(supplierLinking);
                        }
                        supplierModelNew.setSupplierLinkingList(supplierLinkingList);
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

    private void showDialog(int key) {


        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();

        if (key == 1) {
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
        } else if (key == 2) {

            AddContactDialogBinding binding = AddContactDialogBinding.inflate(getLayoutInflater());
            alertDialog.setView(binding.getRoot());
            alertDialog.show();

            binding.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = Objects.requireNonNull(binding.etName.getText()).toString();
                    String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();

                    if (name.length() > 0) {
                        if (phone.length() == 11) {
                            ContactPersons contactPersons = new ContactPersons();
                            contactPersons.setContact_Person_ContactNo(phone);
                            contactPersons.setContact_Person_Design(binding.etDesignation.getText().toString());
                            contactPersons.setContact_Person_Email(binding.etEmail.getText().toString());
                            contactPersons.setContact_Person_Name(name);
                            contactPersonsList.add(contactPersons);
                            contactRecyclerAdapter.setContactPersonsList(contactPersonsList);
                            alertDialog.dismiss();
                        } else {
                            binding.etPhoneLayout.setError("Enter Phone Number");
                        }

                    } else {
                        binding.etNameLayout.setError("Enter name");
                    }


                }
            });
        }


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

}