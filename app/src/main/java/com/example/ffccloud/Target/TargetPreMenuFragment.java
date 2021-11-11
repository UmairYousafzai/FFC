package com.example.ffccloud.Target;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.databinding.FragmentTargetSubMenuBinding;


public class TargetPreMenuFragment extends Fragment {
    private NavController navController;
    private FragmentTargetSubMenuBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=  FragmentTargetSubMenuBinding.inflate(inflater,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        mBinding.innerTextSystemSuggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToNavTarget());
            }
        });
        mBinding.linearWorkPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToNavTarget());
            }
        });

        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToNavTarget());

            }
        });

        mBinding.btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToNavTarget());

            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToFragmentMenu());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return mBinding.getRoot();
    }


}