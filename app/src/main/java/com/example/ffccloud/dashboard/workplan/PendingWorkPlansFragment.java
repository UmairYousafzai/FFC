package com.example.ffccloud.dashboard.workplan;

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

import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentPendingWorkPlansBinding;
import com.example.ffccloud.dashboard.workplan.viewmodel.WorkPlanViewModel;
import com.example.ffccloud.model.DashBoardCustomer;
import com.example.ffccloud.model.WorkPlan;


public class PendingWorkPlansFragment extends Fragment {

    private FragmentPendingWorkPlansBinding mBinding;
    private WorkPlanViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentPendingWorkPlansBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WorkPlanViewModel.class);
        mBinding.setViewModel(viewModel);

        navController= NavHostFragment.findNavController(this);

        getLiveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.getToastMessage().setValue(null);
        viewModel.getSelectedWorkPlan().setValue(null);

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
        viewModel.getSelectedWorkPlan().observe(getViewLifecycleOwner(), new Observer<WorkPlan>() {
            @Override
            public void onChanged(WorkPlan workPlan) {

                if (workPlan!=null)
                {
                    if (workPlan.getAction().equals("0"))
                    {
                        PendingWorkPlansFragmentDirections.ActionWorkPlanFragmentToAllWorkPlanFragment action=
                                PendingWorkPlansFragmentDirections.actionWorkPlanFragmentToAllWorkPlanFragment();
                        action.setWorkPlanId((int)workPlan.getWorkPlanMId());

                        navController.navigate(action);
                    }
                    else
                    {
                        showDialog(workPlan);
                    }

                }
            }
        });
    }

    private void showDialog(WorkPlan workPlan ) {

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        if (workPlan.getAction().equals("1"))
        {
            dialogBinding.title.setText("Work Plan");
            dialogBinding.body.setText("Are You Sure You Want To Approve?");
        }else if (workPlan.getAction().equals("2"))
        {
            dialogBinding.title.setText("Customer");
            dialogBinding.body.setText("Are You Sure You Want To Cancel?");
        }


        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewModel.updateWorkPlan(workPlan);
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