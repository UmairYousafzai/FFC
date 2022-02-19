package com.example.ffccloud.dashboard.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.databinding.FragmentCustomerMenuBinding;


public class CustomerMenuFragment extends Fragment {

    private FragmentCustomerMenuBinding mBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentCustomerMenuBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);


        btnListener();

    }

    private void btnListener() {

        mBinding.cancelCustomerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMenuFragmentDirections.ActionCustomerMenuFragmentToSuggestedCustomerFragment action =
                        CustomerMenuFragmentDirections.actionCustomerMenuFragmentToSuggestedCustomerFragment();
                action.setSelectedCustomer("Cancel");
                navController.navigate(action);
            }
        });

        mBinding.btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMenuFragmentDirections.ActionCustomerMenuFragmentToSuggestedCustomerFragment action =
                        CustomerMenuFragmentDirections.actionCustomerMenuFragmentToSuggestedCustomerFragment();
                action.setSelectedCustomer("Cancel");
                navController.navigate(action);
            }
        });

        mBinding.suggestedCustomerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMenuFragmentDirections.ActionCustomerMenuFragmentToSuggestedCustomerFragment action =
                        CustomerMenuFragmentDirections.actionCustomerMenuFragmentToSuggestedCustomerFragment();
                action.setSelectedCustomer("Suggested");
                navController.navigate(action);
            }
        });

        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMenuFragmentDirections.ActionCustomerMenuFragmentToSuggestedCustomerFragment action =
                        CustomerMenuFragmentDirections.actionCustomerMenuFragmentToSuggestedCustomerFragment();
                action.setSelectedCustomer("Suggested");
                navController.navigate(action);
            }
        });
    }
}