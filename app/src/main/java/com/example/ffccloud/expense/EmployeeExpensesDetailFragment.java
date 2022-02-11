package com.example.ffccloud.expense;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.R;
import com.example.ffccloud.SplashScreen.SplashActivity;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentEmployeeExpensesDetailBinding;
import com.example.ffccloud.expense.utils.EmployeeExpensesDetailViewModel;
import com.example.ffccloud.model.GetEmployeeExpensesDetail;
import com.example.ffccloud.utils.SharedPreferenceHelper;

public class EmployeeExpensesDetailFragment extends Fragment {


    private FragmentEmployeeExpensesDetailBinding mBinding;
    private EmployeeExpensesDetailViewModel viewModel;
    private String month="";
    private int userId=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      mBinding= FragmentEmployeeExpensesDetailBinding.inflate(inflater,container,false);

      return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null)
        {
            userId= EmployeeExpensesDetailFragmentArgs.fromBundle(getArguments()).getUserId();
            month= EmployeeExpensesDetailFragmentArgs.fromBundle(getArguments()).getMonth();
        }

        viewModel = new ViewModelProvider(this).get(EmployeeExpensesDetailViewModel.class);
        mBinding.setViewModel(viewModel);

        setupLiveDataListener();
    }



    private void setupLiveDataListener() {
        viewModel.fetchEmployeeExpenses(month,userId);
        viewModel.onClick();

        viewModel.getServerError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s!=null)
                {
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getExpensesDetailMutableLiveData().observe(getViewLifecycleOwner(), new Observer<GetEmployeeExpensesDetail>() {
            @Override
            public void onChanged(GetEmployeeExpensesDetail expensesDetail) {

                if (expensesDetail!=null)
                {
                    if (!expensesDetail.isCancelled())
                    {
                        if (!expensesDetail.getAction().equals("UnApproved"))
                        {
                            showDialog(expensesDetail);
                        }

                    }else {

                        if (!expensesDetail.getAction().equals("UnApproved"))
                        {
                            showDialog(expensesDetail);
                        }
                    }


                }

            }
        });
    }

    public void showDialog(GetEmployeeExpensesDetail expensesDetail)
    {

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Expense");
        dialogBinding.body.setText("Are You Sure You Want To Update Status?");
        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expensesDetail.setCompany_Id("1");
                expensesDetail.setCountry_Id("1");
                expensesDetail.setProject_Id("1");
                expensesDetail.setLocation_Id("1");
                expensesDetail.setSession_Id(1);
                viewModel.updateExpenseStatus(expensesDetail);

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