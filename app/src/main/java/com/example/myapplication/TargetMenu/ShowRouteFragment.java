package com.example.myapplication.TargetMenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentShowRouteBinding;
import com.example.myapplication.utils.ActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ShowRouteFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private FragmentShowRouteBinding mBinding;
    private ShowRouteAdapter adapter;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentShowRouteBinding.inflate(inflater,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//        Toolbar toolbar=  (Toolbar)requireActivity().findViewById(R.id.custom_toolbar) ;
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
        navController = NavHostFragment.findNavController(this);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        activityViewModel.getAllActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                if (activities!=null) {
                    adapter = new ShowRouteAdapter(activities);
                    mBinding.routeRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mBinding.routeRecycler.setAdapter(adapter);

                }
            }
        });

        mBinding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityViewModel.deleteAllActivity();
                BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(true);

                NavigationView navigationView= requireActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(true);
                navController.navigate(ShowRouteFragmentDirections.actionShowRouteFragmentToNavHome());

            }
        });


        return mBinding.getRoot();
    }
}