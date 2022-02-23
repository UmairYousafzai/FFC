package com.example.ffccloud.dashboard.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.dashboard.customer.viewmodel.CustomerViewModel;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentSuggestedCustomersBinding;
import com.example.ffccloud.model.DashBoardCustomer;

import java.util.Objects;

public class SuggestedCustomersFragment extends Fragment {


    private FragmentSuggestedCustomersBinding mBinding;
    private CustomerViewModel viewModel;
    private NavController navController;
    private String type;



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

        if (getArguments()!=null)
        {
            type= SuggestedCustomersFragmentArgs.fromBundle(getArguments()).getSelectedCustomer();
        }

        if (type != null) {
            if (type.equals("Cancel"))
            {
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Cancel Customer");
                viewModel.setType(type);
                viewModel.fetchCancelCustomer();
            }
            else
            {
                viewModel.setType(type);
                viewModel.fetchSuggestedCustomer();
            }
        }



    }

    @Override
    public void onStop() {
        super.onStop();

        viewModel.getCustomerMutableLiveData().setValue(null);
        viewModel.getToastMessage().setValue(null);
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
        }else if (customer.getAction().equals("4"))
        {
            dialogBinding.title.setText("Customer");
            dialogBinding.body.setText("Are You Sure You Want To Cancel?");
        }
        else if (customer.getAction().equals("3"))
        {
            dialogBinding.title.setText("Customer");
            dialogBinding.body.setText("Are You Sure You Want To ignore?");
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