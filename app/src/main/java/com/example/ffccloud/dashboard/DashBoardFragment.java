package com.example.ffccloud.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentDashBoardBinding;

public class DashBoardFragment extends Fragment {

    private FragmentDashBoardBinding mBinding;
    private NavController navController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentDashBoardBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        btnListener();

    }

    private void btnListener() {
        mBinding.expenseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_DashBoardFragment_to_employeeExpenseFragment);
            }
        });

        mBinding.workPlanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_DashBoardFragment_to_workplan_Fragment);
            }
        });

        mBinding.customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_DashBoardFragment_to_customer_menu_Fragment);
            }
        });
    }
}