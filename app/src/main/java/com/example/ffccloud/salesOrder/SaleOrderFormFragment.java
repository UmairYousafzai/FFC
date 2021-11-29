package com.example.ffccloud.salesOrder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentSaleOrderFormBinding;
import com.example.ffccloud.utils.CONSTANTS;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SaleOrderFormFragment extends Fragment {

    private FragmentSaleOrderFormBinding mBinding;
    private NavController navController;
    private CustomerModel customerModel;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSaleOrderFormBinding.inflate(inflater,container,false);

        return mBinding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        navController = NavHostFragment.findNavController(this);
        MutableLiveData<CustomerModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        liveData.observe(getViewLifecycleOwner(), new Observer<CustomerModel>() {
            @Override
            public void onChanged(CustomerModel model) {
                if (model!=null)
                {
                    customerModel = model;
                    mBinding.tvSelectCustomer.setText(customerModel.getPartyName());
                }


            }
        });
        btnListener();
    }

    private void btnListener() {
        mBinding.addProductBtn.setOnClickListener(v -> {
            NavDirections action = SaleOrderFormFragmentDirections.actionSaleOrderFormFragmentToAddProductFragment();
            navController.navigate(action);

        });

        mBinding.tvSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaleOrderFormFragmentDirections.ActionSaleOrderFormFragmentToCustomerListFragment action = SaleOrderFormFragmentDirections.actionSaleOrderFormFragmentToCustomerListFragment();
                action.setCallingFragmentKey(1);
                navController.navigate(action);
            }
        });
    }
}