package com.example.ffccloud.farm;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.AddMedicineDialogBinding;
import com.example.ffccloud.databinding.FragmentAddFarmFormBinding;

public class AddFarmFormFragment extends Fragment {

    private FragmentAddFarmFormBinding mBinding;
    private NavController navController;


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

        btnListener();
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
                    NavDirections navDirections= AddFarmFormFragmentDirections.actionAddFarmFormFragmentToMedicineListFragment();

                    navController.navigate(navDirections);
                }
                else {

                    showAddMedicineDialog();
                }



            }


        });
    }
    private void showAddMedicineDialog() {

        AddMedicineDialogBinding  binding= AddMedicineDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).create();

        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}