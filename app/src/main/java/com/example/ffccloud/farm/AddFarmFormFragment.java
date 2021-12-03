package com.example.ffccloud.farm;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.GetProductModel;
import com.example.ffccloud.Medicine_modal;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddFarmFormBinding;
import com.example.ffccloud.farm.adapter.MedicineAdapter;
import com.example.ffccloud.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFarmFormFragment extends Fragment {

    private FragmentAddFarmFormBinding mBinding;
    private NavController navController;
    private List<ContactPersonsModel> contactPersonsModelList = new ArrayList<>();
    private List<Medicine_modal> medicineModalList= new ArrayList<>();
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private MedicineAdapter medicineAdapter;
    private List<GetProductModel> productModelList= new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddFarmFormBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        navController = NavHostFragment.findNavController(this);

        MutableLiveData<GetProductModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        liveData.observe(getViewLifecycleOwner(), new Observer<GetProductModel>() {
            @Override
            public void onChanged(GetProductModel model) {
                if (model!=null)
                {
                    productModelList.add(model);
                }


            }
        });
        btnListener();
        setUpRecyclerView();
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
                if (mBinding.companyMedicineRadioBtn.isChecked())
                {
                    AddFarmFormFragmentDirections.ActionAddFarmFormFragmentToAddProductFragment action = AddFarmFormFragmentDirections.actionAddFarmFormFragmentToAddProductFragment();
                    action.setKey(1);


                    navController.navigate(action);
                }
                else {

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
    }
    private void showDialog(int key) {


        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();

        if (key==1)
        {
            AddMedicineDialogBinding  binding= AddMedicineDialogBinding.inflate(getLayoutInflater());
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
        else if (key==2)
        {

            AddContactDialogBinding binding = AddContactDialogBinding.inflate(getLayoutInflater());
            alertDialog.setView(binding.getRoot());
            alertDialog.show();

            binding.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = Objects.requireNonNull(binding.etName.getText()).toString();
                    String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();

                    if (name.length()>0)
                    {
                        if (phone.length()>11)
                        {
                            ContactPersonsModel contactPersonsModel = new ContactPersonsModel();
                            contactPersonsModel.setContact_Person_ContactNo(phone);
                            contactPersonsModel.setContact_Person_Design(binding.etDesignation.getText().toString());
                            contactPersonsModel.setContact_Person_Email(binding.etEmail.getText().toString());
                            contactPersonsModel.setContact_Person_Name(name);
                            contactPersonsModelList.add(contactPersonsModel);
                            contactRecyclerAdapter.setContactPersonsModelList(contactPersonsModelList);
                            alertDialog.dismiss();
                        }
                        else
                        {
                            binding.etPhoneLayout.setError("Enter Phone Number");
                        }

                    }
                    else
                    {
                        binding.etNameLayout.setError("Enter name");
                    }




                }
            });
        }


    }
}