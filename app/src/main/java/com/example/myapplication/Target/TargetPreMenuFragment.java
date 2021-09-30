package com.example.myapplication.Target;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;

public class TargetPreMenuFragment extends Fragment {
    private NavController navController;
    LinearLayout linear_system_suggested, linear_work_plan;

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
        View root = inflater.inflate(R.layout.fragment_target_sub_menu, container, false);
        init(root);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        linear_system_suggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TargetPreMenuFragmentDirections.actionNavTargetSubMenuToNavTarget());
            }
        });
        linear_work_plan.setOnClickListener(new View.OnClickListener() {
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

        return root;
    }

    private void init(View root) {
        linear_system_suggested = root.findViewById(R.id.linear_system_suggested);
        linear_work_plan = root.findViewById(R.id.linear_work_plan);

    }
}