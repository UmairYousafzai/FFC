package com.example.ffccloud.Customer;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.FragmentAddCustomerBinding;

public class AddCustomerFragment extends Fragment {

    private FragmentAddCustomerBinding mBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddCustomerBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        btnListener();

    }

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {

        AddContactDialogBinding binding = AddContactDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog= new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).setCancelable(false).create();
        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}