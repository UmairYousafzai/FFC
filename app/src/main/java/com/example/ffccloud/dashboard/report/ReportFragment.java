package com.example.ffccloud.dashboard.report;

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

import com.example.ffccloud.dashboard.report.viewmodel.ReportViewModel;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentReportBinding;
import com.example.ffccloud.model.LookUpWorkPlan;

public class ReportFragment extends Fragment {

    private FragmentReportBinding mBinding ;
    private LookUpWorkPlan report;
    private ReportViewModel viewModel;
    private NavController navController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentReportBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        navController = NavHostFragment.findNavController(this);

        mBinding.setViewModel(viewModel);

        if (getArguments()!=null)
        {
            report= ReportFragmentArgs.fromBundle(getArguments()).getReportFilter();
            mBinding.setReport(report);
        }

        getLiveData();

    }

    @Override
    public void onStop() {
        super.onStop();

        viewModel.getCoordinatesMutableLiveData().setValue(null);
    }

    private void getLiveData() {

        viewModel.getCoordinatesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String coordinates) {

                if (coordinates!=null&& !coordinates.isEmpty())
                {
                    showDialog(coordinates);
                }
                else
                {
                    Toast.makeText(requireContext(), "Coordinates are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog(String coordinates) {

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(requireActivity().getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        dialogBinding.title.setText("Report");
        dialogBinding.body.setText("Do you want to Proceed.");
        dialogBinding.btnCheckLocation.setVisibility(View.VISIBLE);
        alertDialog.show();

        dialogBinding.btnYes.setVisibility(View.GONE);

        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        dialogBinding.btnCheckLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                ReportFragmentDirections.ActionReportFragmentToMapFragment action =
                        ReportFragmentDirections.actionReportFragmentToMapFragment();
                action.setCoordinates(coordinates);
                action.setWorkPlanName(report.getCustomerName());
                navController.navigate(action);
            }
        });

    }
}