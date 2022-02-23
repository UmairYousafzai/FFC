package com.example.ffccloud.dashboard.report;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.ffccloud.R;
import com.example.ffccloud.dashboard.report.viewmodel.ReportFilterViewModel;
import com.example.ffccloud.databinding.FragmentReportFilterBinding;
import com.example.ffccloud.model.ReportFilter;

import java.util.Calendar;


public class ReportFilterFragment extends Fragment {

    private FragmentReportFilterBinding mBinding;
    private ReportFilterViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentReportFilterBinding.inflate(inflater,container,false);
        return  mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel= new ViewModelProvider(this).get(ReportFilterViewModel.class);
        navController= NavHostFragment.findNavController(this);

        mBinding.setViewModel(viewModel);
        viewModel.getEmployees();
        dateListener();
        getLiveData();
    }

    @Override
    public void onStop() {
        super.onStop();

        viewModel.getReportFilterMutableLiveData().setValue(null);
        viewModel.getClickMutableLivedata().setValue(null);
        viewModel.getClickMutableLivedata().setValue(0);
    }

    private void getLiveData() {

        viewModel.getReportFilterMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ReportFilter>() {
            @Override
            public void onChanged(ReportFilter reportFilter) {

                if (reportFilter!=null)
                {
                    ReportFilterFragmentDirections.ActionReportFilterFragmenttToReportListFragment action =
                            ReportFilterFragmentDirections.actionReportFilterFragmenttToReportListFragment(reportFilter);
                    navController.navigate(action);

                }
            }
        });

        viewModel.getFromDateError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    mBinding.fromDateLayout.setError(s);
                }
            }
        });
        viewModel.getToDateError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    mBinding.toDateLayout.setError(s);
                }
            }
        });
    }

    private void dateListener() {

        viewModel.getClickMutableLivedata().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer key) {

                if (key!=0)
                {
                    openDateDialog(key);
                }


            }
        });

    }

    private void openDateDialog(Integer key) {

        Calendar calendar = Calendar.getInstance();
        int day= calendar.get(Calendar.DAY_OF_MONTH), year= calendar.get(Calendar.YEAR),month= calendar.get(Calendar.MONTH)+1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int checkMonth = (month ) % 10, checkDay = (dayOfMonth % 10);

                String mMonth, mDay;
                if (checkMonth > 0 && month < 9) {
                    mMonth = "0" + (month );
                } else {
                    mMonth = String.valueOf(month);

                }

                if (checkDay > 0 && dayOfMonth < 10) {
                    mDay = "0" + (dayOfMonth);

                } else {
                    mDay = String.valueOf(dayOfMonth);

                }
                String date= year + mMonth + mDay;

                if (key==1)
                {
                    viewModel.getFromDateMutableLivedata().setValue(date);
                    date= year+"/"+mMonth+"/"+mDay;
                    mBinding.tvFromDate.setText(date);
                    mBinding.fromDateLayout.setError(null);

                }
                else
                {
                    viewModel.getToDateMutableLivedata().setValue(date);
                    date= year+"/"+mMonth+"/"+mDay;
                    mBinding.tvToDate.setText(date);
                    mBinding.toDateLayout.setError(null);

                }
            }
        },year,month,day);
        datePickerDialog.show();
    }
}