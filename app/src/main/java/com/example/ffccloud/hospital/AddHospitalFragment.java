package com.example.ffccloud.hospital;

import android.app.ProgressDialog;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ffccloud.FarmModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.SupplierMainModel;
import com.example.ffccloud.ModelClasses.SupplierModelNew;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddHospitalBinding;
import com.example.ffccloud.databinding.OtherFarmDialogBinding;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.hospital.adapter.FarmRecyclerAdapter;
import com.example.ffccloud.hospital.adapter.GasDaysRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
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
    private ArrayList<String>gradeArray=new ArrayList<>();
    private List<GradingModel> gradingModelList;
    private FarmRecyclerAdapter farmRecyclerAdapter;
    private GasDaysRecyclerAdapter gasDaysRecyclerAdapter;
    private List<FarmModel> farmModelList= new ArrayList<>();
    private List<String> gasDaysList = new ArrayList<>();
    private NavController navController;
    private MedicineAdapter medicineAdapter;

    private List<SupplierItemLinking> medicineModalList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAddHospitalBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        setUpRecyclerView();

    }

    @Override
    public void onResume() {
        super.onResume();


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
        setUpGradeSpinner();
        btnListener();


    }

    private void setUpRecyclerView() {

        mBinding.farmRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        farmRecyclerAdapter = new FarmRecyclerAdapter();
        mBinding.farmRecycler.setAdapter(farmRecyclerAdapter);

        mBinding.gasDaysRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        gasDaysRecyclerAdapter = new GasDaysRecyclerAdapter();
        mBinding.gasDaysRecyclerview.setAdapter(gasDaysRecyclerAdapter);

        mBinding.medicineRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicineAdapter = new MedicineAdapter();
        mBinding.medicineRecycler.setAdapter(medicineAdapter);
    }

    private void btnListener() {

        mBinding.btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddFarmDialog();

            }
        });

        mBinding.btnAddGasDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String days = Objects.requireNonNull(mBinding.etGasDays.getText()).toString();
                if (days.length()>2)
                {
                    gasDaysList.add(days);
                    gasDaysRecyclerAdapter.setGasDaysList(gasDaysList);

                }
                else {
                    Toast.makeText(requireContext(), "Please enter days", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBinding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.companyMedicineRadioBtn.isChecked())
                {
                 AddHospitalFragmentDirections.ActionAddHospitalFragmentToAddProductFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToAddProductFragment();
                    action.setKey(1);
                    navController.navigate(action);
                }
                else {

                    showDialog();
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

        String name = Objects.requireNonNull(mBinding.etName.getText()).toString();
        String address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
        int gradeID = gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());



        if (name.length() > 0) {

                if (address.length() > 0) {


                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompanyId(1);
                        supplierModelNew.setCountryId(1);
                        supplierModelNew.setLocationId(1);
                        supplierModelNew.setProjectId(1);
                        supplierModelNew.setSupplierCode(0);
                        supplierModelNew.setSupplierId(0);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setGradeId(gradeID);
                        supplierModelNew.setGasDays(Objects.requireNonNull(mBinding.etGasDays.getText()).toString());
                        supplierModelNew.setSize(Objects.requireNonNull(mBinding.etSizeOfHospital.getText()).toString());
                        supplierModelNew.setSupplierName(name);
                        supplierModelNew.setUserSubType(CONSTANTS.USER_SUB_TYPE_HOSPITAL);
                        supplierModelNew.setUserTypeName("H");
                        supplierModelNew.setNoOfAnimals(Objects.requireNonNull(mBinding.etNoOfCases.getText()).toString());
                        supplierModelNew.setSupplierItemLinkingList(medicineModalList);


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

    private void showAddFarmDialog() {

        OtherFarmDialogBinding binding= OtherFarmDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).create();
        alertDialog.show();

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Objects.requireNonNull(binding.etFarmName.getText()).toString();

                if (name.length()>0)
                {
                    FarmModel farmModel = new FarmModel(name, Objects.requireNonNull(binding.etFarmAddress.getText()).toString());
                    farmModelList.add(farmModel);
                    farmRecyclerAdapter.setFarmModelList(farmModelList);
                }
                else
                {
                    Toast.makeText(requireContext(), " Please enter name", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
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




}