package com.example.ffccloud.dashboard.workplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ffccloud.R;
import com.example.ffccloud.dashboard.workplan.viewmodel.AllWorkPlanViewModel;
import com.example.ffccloud.dashboard.workplan.viewmodel.WorkPlanViewModel;
import com.example.ffccloud.databinding.FragmentAllWorkPlanBinding;

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

    }
}