package com.example.ffccloud.dashboard.workplan;

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

import com.example.ffccloud.databinding.FragmentPendingWorkPlansBinding;
import com.example.ffccloud.dashboard.workplan.viewmodel.WorkPlanViewModel;


public class PendingWorkPlansFragment extends Fragment {

    private FragmentPendingWorkPlansBinding mBinding;
    private WorkPlanViewModel viewModel;

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


        getLiveData();
    }

    private void getLiveData() {

        viewModel.getServerErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
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