package com.example.ffccloud.Expense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentExpenseListBinding;

import org.jetbrains.annotations.NotNull;

public class ExpenseListFragment extends Fragment {

    private FragmentExpenseListBinding mBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding= FragmentExpenseListBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        btnListener();
    }

    private void btnListener() {

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections navDirections = ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseFormFragment();
                navController.navigate(navDirections);

            }
        });
    }
}