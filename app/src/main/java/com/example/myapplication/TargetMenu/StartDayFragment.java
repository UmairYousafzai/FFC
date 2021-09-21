package com.example.myapplication.TargetMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.R;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class StartDayFragment extends Fragment {
    MaterialButton startday;
    NavController navController;
    FfcDatabase ffcDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffcDatabase = FfcDatabase.getInstance(getContext());
        SharedPreferenceHelper.getInstance(getContext()).setActivity("Start Day");

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
        View root = inflater.inflate(R.layout.fragment_start_day, container, false);
        init(root);

        List<String> config = ffcDatabase.dao().getOnConfigurations();

        startday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(config.contains("Attendance_Activity")){
                    navController.navigate(R.id.nav_attendance);
                }
                else{
                    navController.navigate(R.id.nav_target_main);
                }

            }
        });

        return root;
    }

    private void init(View root) {
        startday = root.findViewById(R.id.startday);
    }
}