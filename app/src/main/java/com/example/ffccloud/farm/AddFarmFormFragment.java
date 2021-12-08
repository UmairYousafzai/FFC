package com.example.ffccloud.farm;

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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.SupplierMainModel;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.ModelClasses.RegionModel;
import com.example.ffccloud.ModelClasses.SupplierModelNew;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentAddFarmFormBinding;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomLocation;
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
    private List<ContactPersons> contactPersonsList = new ArrayList<>();
    private List<SupplierItemLinking> medicineModalList = new ArrayList<>();
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private MedicineAdapter medicineAdapter;
    private ArrayList<String> animalArrayList = new ArrayList<>(), regionList = new ArrayList<>();
    private HashMap<String, Integer> regionHashmap = new HashMap<>();
    private String locationString;
    private String locationAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddFarmFormBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        btnListener();

        setupAnimalSpinner();
        getRegion();
        navController = NavHostFragment.findNavController(this);

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
                    medicineModal.setSupplierItemLinkIdDtl(0);
                    medicineModal.setItCode(String.valueOf(model.getItem_Code()));

                    medicineModalList.add(medicineModal);
                    medicineAdapter.setMedicineModalList(medicineModalList);


                }


            }
        });


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

    }

    private void btnListener() {

        mBinding.btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections directions = AddFarmFormFragmentDirections.actionAddFarmFormFragmentToSearchDoctorFragment();
                navController.navigate(directions);
            }
        });
        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked()) {
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


                            if (mBinding.locationCheckbox.isChecked()) {
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.locationCheckbox.setText(locationAddress);
                                 locationString = String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude());
                            } else {

                                mBinding.locationCheckbox.setText("Enable Location");
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

        String name = Objects.requireNonNull(mBinding.etOwnerName.getText()).toString();
        String phone = Objects.requireNonNull(mBinding.etContact.getText()).toString();
        String address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
        RadioButton radioButton = mBinding.getRoot().findViewById(mBinding.animalRadioGroup.getCheckedRadioButtonId());
        String animalMainType = radioButton.getText().toString();



        if (name.length() > 0) {
            if (phone.length() > 0) {
                if (address.length() > 0) {
                    if (regionList.size()>0) {
                        int region = regionHashmap.get(mBinding.regionSpinner.getSelectedItem().toString());

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
                        supplierModelNew.setUserSubType(CONSTANTS.USER_SUB_TYPE_FARM);
                        supplierModelNew.setUserTypeName("F");
                        supplierModelNew.setAnimalsMainType(animalMainType);
                        supplierModelNew.setAnimalsSubType(mBinding.animalSpinner.getSelectedItem().toString());
                        supplierModelNew.setNoOfAnimals(Objects.requireNonNull(mBinding.etNumberOfAnimal.getText()).toString());
                        supplierModelNew.setSupplierItemLinkingList(medicineModalList);
                        supplierModelNew.setContactPersonsList(contactPersonsList);
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
                            medicineModal.setSupplierItemLinkIdDtl(0);
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
                        if (phone.length() > 11) {
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
                    mBinding.regionSpinner.setAdapter(adapter);
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

}