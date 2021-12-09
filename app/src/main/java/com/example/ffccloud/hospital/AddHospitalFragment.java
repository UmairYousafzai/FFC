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

import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.GetSupplierDetailModel;
import com.example.ffccloud.ModelClasses.SupplierLinking;
import com.example.ffccloud.ModelClasses.SupplierMainModel;
import com.example.ffccloud.ModelClasses.SupplierModelNew;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.SupplierItemLinking;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.adapter.SupplierRecyclerViewAdapter;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddHospitalBinding;
import com.example.ffccloud.databinding.OtherFarmDialogBinding;
import com.example.ffccloud.farm.AddFarmFormFragmentArgs;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
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
    private GasDaysRecyclerAdapter gasDaysRecyclerAdapter;
    private List<String> gasDaysList = new ArrayList<>();
    private NavController navController;
    private MedicineAdapter medicineAdapter;
    private int supplierID;
    private List<GetSupplierModel> doctorModelList = new ArrayList<>();
    private List<GetSupplierModel> FarmModelList = new ArrayList<>();
    private SupplierRecyclerViewAdapter doctorRecyclerViewAdapter;
    private SupplierRecyclerViewAdapter farmRecyclerViewAdapter;
    private List<SupplierItemLinking> medicineModalList= new ArrayList<>();
    private String callingAddBtn;



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
        assert getArguments() != null;
        supplierID = AddFarmFormFragmentArgs.fromBundle(getArguments()).getSupplierId();
        if (supplierID > 0&&callingAddBtn==null){

          //  setup fields if edit request has been made
            getSupplierByID(supplierID);
        }

        MutableLiveData<GetSupplierModel> doctorsupplierLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.DOCTOR_SUPPLIER_KEY);
        doctorsupplierLiveData.observe(getViewLifecycleOwner(), new Observer<GetSupplierModel>() {
            @Override
            public void onChanged(GetSupplierModel model) {
                if (model!=null)
                {

                    if (callingAddBtn.equals("DoctorAddBtn"))
                    {
                        doctorModelList.add(model);
                        doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
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
                if (model!=null)
                {
                    if (callingAddBtn.equals("FarmAddBtn"))
                    {
                        FarmModelList.add(model);
                        farmRecyclerViewAdapter.setGetSupplierModelList(FarmModelList);
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
                    if (callingAddBtn.equals("MedicineAddBtn"))
                    {
                        SupplierItemLinking medicineModal = new SupplierItemLinking();
                        medicineModal.setItHead(model.getTitleProduct());
                        medicineModal.setIsRegistered(true);
                        medicineModal.setSupplierItemLinkIdDtl("0");
                        medicineModal.setItCode(model.getItem_Code());


                        medicineModalList.add(medicineModal);
                        medicineAdapter.setMedicineModalList(medicineModalList);
                    }



                }


            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (callingAddBtn!=null)
        {
            medicineAdapter.setMedicineModalList(medicineModalList);
            doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);
            farmRecyclerViewAdapter.setGetSupplierModelList(FarmModelList);


        }

        setUpGradeSpinner();
        btnListener();


    }

    private void setUpRecyclerView() {


        mBinding.gasDaysRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        gasDaysRecyclerAdapter = new GasDaysRecyclerAdapter();
        mBinding.gasDaysRecyclerview.setAdapter(gasDaysRecyclerAdapter);

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

        mBinding.btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBinding.registerFarmRadioBtn.isChecked())
                {
                    callingAddBtn= "";
                    callingAddBtn= "FarmAddBtn";
                    farmRecyclerViewAdapter.setCalledSupplier("Farm");
                    AddHospitalFragmentDirections.ActionAddHospitalFragmentToFarmListFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToFarmListFragment();
                    action.setKey(1);
                    navController.navigate(action);
                }
                else
                {
                    showAddFarmDialog();

                }

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
                    callingAddBtn= "";
                    callingAddBtn= "MedicineAddBtn";
                 AddHospitalFragmentDirections.ActionAddHospitalFragmentToAddProductFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToAddProductFragment();
                    action.setKey(1);
                    navController.navigate(action);
                }
                else {

                    showDialog();
                }
            }
        });
        mBinding.btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingAddBtn= "";
                callingAddBtn= "DoctorAddBtn";
                AddHospitalFragmentDirections.ActionAddHospitalFragmentToSupplierDoctorFragment action = AddHospitalFragmentDirections.actionAddHospitalFragmentToSupplierDoctorFragment();
                action.setKey(1);
                navController.navigate(action);
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
        mBinding.etNoOfCases.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getNoOfAnimals()));
        mBinding.etAddress.setText(getSupplierDetailModel.getSupplierModelNewList().get(0).getAddress());

        mBinding.etSizeOfHospital.setText(String.valueOf(getSupplierDetailModel.getSupplierModelNewList().get(0).getSize()));


        medicineModalList.clear();
        medicineModalList.addAll(getSupplierDetailModel.getSupplierItemLinkingList());
        medicineAdapter.setMedicineModalList(medicineModalList);

        doctorModelList.clear();
        for (SupplierLinking model:getSupplierDetailModel.getSupplierLinkingList())
        {
            GetSupplierModel supplierModel = new GetSupplierModel();
            supplierModel.setSupplier_Name(model.getSupplierName());
            supplierModel.setAddress(model.getAddress());
            supplierModel.setSupplier_Id(model.getSupplierId());
            doctorModelList.add(supplierModel);
        }
        doctorRecyclerViewAdapter.setGetSupplierModelList(doctorModelList);

        int gradeID= getSupplierDetailModel.getSupplierModelNewList().get(0).getGradeId();

        String grade =  gradingHashMapForTitle.get(gradeID);

        mBinding.spinnerGrade.setSelection(gradeArray.indexOf(grade));







    }

    private void setUpSupplierModelForSave() {

        String name = Objects.requireNonNull(mBinding.etName.getText()).toString();
        String address = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
        int gradeID = gradingHashMapForId.get(mBinding.spinnerGrade.getSelectedItem().toString());



        if (name.length() > 0) {

                if (address.length() > 0) {


                        SupplierModelNew supplierModelNew = new SupplierModelNew();

                        supplierModelNew.setCompany_Id(1);
                        supplierModelNew.setCountry_Id(1);
                        supplierModelNew.setLocation_Id(1);
                        supplierModelNew.setProject_Id(1);
                        supplierModelNew.setSupplier_Code("0");
                        supplierModelNew.setSupplier_Id(0);
                        supplierModelNew.setAddress(address);
                        supplierModelNew.setGrade_id(gradeID);
                        supplierModelNew.setGas_Days(Objects.requireNonNull(mBinding.etGasDays.getText()).toString());
                        supplierModelNew.setSize(mBinding.etSizeOfHospital.getText().toString());
                        supplierModelNew.setSupplier_Name(name);
                        supplierModelNew.setUser_Sub_Type(CONSTANTS.USER_SUB_TYPE_HOSPITAL);
                        supplierModelNew.setUserTypeName("H");
                    supplierModelNew.setNo_Of_Animals(mBinding.etNoOfCases.getText().toString());
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

                    GetSupplierModel farmSupplierModel= new GetSupplierModel();
                    farmSupplierModel.setAddress(binding.etFarmAddress.getText().toString());
                    farmSupplierModel.setSupplier_Name(name);
                    FarmModelList.add(farmSupplierModel);
                    farmRecyclerViewAdapter.setGetSupplierModelList(FarmModelList);
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
                            medicineModal.setSupplierItemLinkIdDtl("0");
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