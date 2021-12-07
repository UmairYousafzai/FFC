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
import android.widget.Toast;

import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.FarmModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.Medicine_modal;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddHospitalBinding;
import com.example.ffccloud.databinding.OtherFarmDialogBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentDirections;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.hospital.adapter.FarmRecyclerAdapter;
import com.example.ffccloud.hospital.adapter.GasDaysRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    private List<Medicine_modal> medicineModalList= new ArrayList<>();

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
                    Medicine_modal medicineModal = new Medicine_modal();
                    medicineModal.setMedicineName(model.getTitleProduct());
                    medicineModal.setCompany(true);

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
                            Medicine_modal medicineModal = new Medicine_modal();
                            medicineModal.setCompanyName(companyName);
                            medicineModal.setCompany(false);
                            medicineModal.setMedicineName(medicineName);
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