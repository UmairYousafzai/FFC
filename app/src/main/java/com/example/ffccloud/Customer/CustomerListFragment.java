package com.example.ffccloud.Customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentCustomerListBinding;

public class CustomerListFragment extends Fragment {

    private FragmentCustomerListBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentCustomerListBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setupRecyclerView();
    }

    private void setupRecyclerView() {

    }
}