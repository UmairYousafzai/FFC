package com.example.ffccloud.dashboard.workplan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ffccloud.R;
import com.example.ffccloud.dashboard.workplan.viewmodel.AllWorkPlanViewModel;
import com.example.ffccloud.dashboard.workplan.viewmodel.WorkPlanViewModel;
import com.example.ffccloud.databinding.FragmentAllWorkPlanBinding;
import com.example.ffccloud.utils.CustomLocation;

public class AllWorkPlanFragment extends Fragment {

    private FragmentAllWorkPlanBinding mBinding;
    private AllWorkPlanViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      mBinding= FragmentAllWorkPlanBinding.inflate(inflater,container,false);

      return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel= new ViewModelProvider(this).get(AllWorkPlanViewModel.class);

        mBinding.setViewModel(viewModel);

        if (getArguments()!=null)
        {
            viewModel.getAllWorkPlan(AllWorkPlanFragmentArgs.fromBundle(getArguments()).getWorkPlanId());
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
                Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}