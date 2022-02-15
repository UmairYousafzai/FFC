package com.example.ffccloud.dashboard.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.ActionCustomDialogBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentEmployeeExpensesDetailBinding;
import com.example.ffccloud.dashboard.expense.viewmodel.EmployeeExpensesDetailViewModel;
import com.example.ffccloud.model.GetEmployeeExpensesDetail;

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

//                if (expensesDetail!=null && !expensesDetail.getAction().isEmpty())
//                {
//                    if (!expensesDetail.isCancelled())
//                    {
//                        if (!expensesDetail.getAction().equals("UnApproved"))
//                        {
//                            showDialog(expensesDetail);
//                        }
//                        else
//                        {
//                            Toast.makeText(requireContext(), "Action Not Performed Because Of Cancel Expense", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                    else if (!expensesDetail.isApproved())
//                    {
//                        if (!expensesDetail.getAction().equals("UnApproved") && !expensesDetail.getAction().equals("1") )
//                        {
//                            showDialog(expensesDetail);
//                        }
//                        else
//                        {
//                            Toast.makeText(requireContext(), "Action Not Performed Because Of Cancel Expense", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(requireContext(), "Action Not Performed Because Of Cancel Expense", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
                if (expensesDetail!=null)
                {
                    showSetActionDialog(expensesDetail);
                }

            }
        });
    }

    public void showSetActionDialog(GetEmployeeExpensesDetail expensesDetail)
    {


        if (!expensesDetail.isCancelled())
        {
            ActionCustomDialogBinding dialogBinding = ActionCustomDialogBinding.inflate(getLayoutInflater());
            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(true).create();
            alertDialog.show();
            if (expensesDetail.isApproved())
            {
                dialogBinding.radioBtnApproved.setVisibility(View.GONE);
            }
            dialogBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId== R.id.radio_btn_approved)
                    {

                        expensesDetail.setAction("1");
                    }
                    else
                    {

                        expensesDetail.setAction("2");

                    }
                    alertDialog.dismiss();
                    showConfirmationDialog(expensesDetail);
                }
            });
        }
        else
        {
            Toast.makeText(requireContext(), "Expense is already cancel." , Toast.LENGTH_SHORT).show();
        }

    }

    public void showConfirmationDialog(GetEmployeeExpensesDetail expensesDetail)
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
                viewModel.updateExpenseStatus(expensesDetail,userId);
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