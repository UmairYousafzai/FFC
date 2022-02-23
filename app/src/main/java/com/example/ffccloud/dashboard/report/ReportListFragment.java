package com.example.ffccloud.dashboard.report;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.R;
import com.example.ffccloud.dashboard.report.viewmodel.ReportListViewModel;
import com.example.ffccloud.databinding.FragmentReportListBinding;
import com.example.ffccloud.model.ReportFilter;
import com.example.ffccloud.utils.CustomLocation;

public class ReportListFragment extends Fragment {

    private FragmentReportListBinding mBinding;
    private ReportListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentReportListBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        mBinding.setViewModel(viewModel);

        if(getArguments()!=null)
        {
            ReportFilter reportFilter = ReportListFragmentArgs.fromBundle(getArguments()).getReportFilter();
            if (reportFilter!=null)
            {
                viewModel.getReports(reportFilter.getFromDate(),reportFilter.getToDate(),reportFilter.getEmpId(),reportFilter.getStatusID());
            }
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

                if (coordinates!=null&&!coordinates.isEmpty())
                {
                    String[] coordinatesString = coordinates.split(",");
                    String address= new CustomLocation(requireContext()).getCompleteAddressString(Double.parseDouble(coordinatesString[0]),Double.parseDouble(coordinatesString[1]));
                    String locationString= "geo:"+ coordinates+"?q="+ coordinates+(address);
                    Uri mapIntentUri= Uri.parse(locationString);
                    Intent mapIntent= new Intent(Intent.ACTION_VIEW,mapIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(requireContext().getPackageManager())!=null)
                    {
                        requireContext().startActivity(mapIntent);
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "Coordinates are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}