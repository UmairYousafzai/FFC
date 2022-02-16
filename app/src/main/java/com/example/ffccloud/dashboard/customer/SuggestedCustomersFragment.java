package com.example.ffccloud.dashboard.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.dashboard.customer.viewmodel.CustomerViewModel;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentSuggestedCustomersBinding;
import com.example.ffccloud.model.DashBoardCustomer;

public class SuggestedCustomersFragment extends Fragment {


    private FragmentSuggestedCustomersBinding mBinding;
    private CustomerViewModel viewModel;
    private NavController navController;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSuggestedCustomersBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        mBinding.setViewModel(viewModel);

        getLiveData();



    }

    private void getLiveData() {

        viewModel.getToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getCustomerMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DashBoardCustomer>() {
            @Override
            public void onChanged(DashBoardCustomer customer) {

                if (customer!=null)
                {
                   if (!customer.getAction().equals("1"))
                   {
                       showDialog(customer);

                   }
                   else
                   {
                       checkUser(customer);
                       viewModel.getCustomerMutableLiveData().setValue(null);
                   }
                }

            }
        });
    }

    private void checkUser(DashBoardCustomer customer) {

        if (customer.getUserType().equals("Dr"))
        {
            SuggestedCustomersFragmentDirections.ActionSuggestedCustomerFragmentToAddDoctorFragment action =
                    SuggestedCustomersFragmentDirections.actionSuggestedCustomerFragmentToAddDoctorFragment();
            action.setSupplierId((int)customer.getDoctorId());
            navController.navigate(action);
        }
        else if (customer.getUserType().equals("F"))
        {
            SuggestedCustomersFragmentDirections.ActionSuggestedCustomerFragmentToAddFarmFragment action =
                    SuggestedCustomersFragmentDirections.actionSuggestedCustomerFragmentToAddFarmFragment();
            action.setSupplierId((int)customer.getDoctorId());
            navController.navigate(action);
        }
        else if (customer.getUserType().equals("H"))
        {
            SuggestedCustomersFragmentDirections.ActionSuggestedCustomerFragmentToAddHospitalFragment action =
                    SuggestedCustomersFragmentDirections.actionSuggestedCustomerFragmentToAddHospitalFragment();
            action.setSupplierId((int)customer.getDoctorId());
            navController.navigate(action);
        }
        else if (customer.getUserType().equals("Str"))
        {
            SuggestedCustomersFragmentDirections.ActionSuggestedCustomerFragmentToAddMedicalStoreFragment action =
                    SuggestedCustomersFragmentDirections.actionSuggestedCustomerFragmentToAddMedicalStoreFragment();
            action.setSupplierId((int)customer.getDoctorId());
            navController.navigate(action);
        }
    }

    private void showDialog(DashBoardCustomer customer) {

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        if (customer.getAction().equals("2"))
        {
            dialogBinding.title.setText("Customer");
            dialogBinding.body.setText("Are You Sure You Want To Approve?");
        }else
        {
            dialogBinding.title.setText("Customer");
            dialogBinding.body.setText("Are You Sure You Want To Cancel?");
        }

        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewModel.updateStatusCustomer(customer);
                alertDialog.dismiss();
            }
        });
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}