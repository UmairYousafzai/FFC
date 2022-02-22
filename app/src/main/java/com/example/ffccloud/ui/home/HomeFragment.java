package com.example.ffccloud.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.model.Activity;
import com.example.ffccloud.R;
import com.example.ffccloud.Target.utils.DoctorViewModel;

import com.example.ffccloud.databinding.FragmentHomeBinding;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CONSTANTS;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mbinding;
    private NavController navController;
    private ActivityViewModel activityViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mbinding = FragmentHomeBinding.inflate(inflater,container,false);
        DoctorViewModel doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
         activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        if (!Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).isShowing()) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        }
        doctorViewModel.deleteAllSchedule();
      navController = NavHostFragment.findNavController(this);
        return mbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel.getWithoutTaskActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {


                if (!activities.isEmpty() )
                {
                    String endDate=activities.get(activities.size()-1).getEndDateTime();

                    if (endDate==null)
                    {
                        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);

                        NavigationView navigationView= requireActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.nav_start_day).setEnabled(false);
                        Activity activity= new Activity();

                        activity= activities.get(activities.size()-1);

                        if (activity.getMainActivity().equals(CONSTANTS.START_DAY))
                        {


                            navController.navigate(HomeFragmentDirections.actionNavHomeToNavTargetMain());
                        }
                        else if (activity.getMainActivity().equals(CONSTANTS.PRIVATE_TRAVEL))
                        {
                            HomeFragmentDirections.ActionNavHomeToFragmentMenu action = HomeFragmentDirections.actionNavHomeToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.PRIVATE_TRAVEL);
                            navController.navigate(action);
                        }
                        else if (activity.getMainActivity().equals(CONSTANTS.LOCAL_TRAVEL))
                        {
                            HomeFragmentDirections.ActionNavHomeToFragmentMenu action = HomeFragmentDirections.actionNavHomeToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.LOCAL_TRAVEL);
                            navController.navigate(action);
                        }
                        else if (activity.getMainActivity().equals(CONSTANTS.OFFICE))
                        {
                            HomeFragmentDirections.ActionNavHomeToFragmentMenu action = HomeFragmentDirections.actionNavHomeToFragmentMenu();
                            action.setSelectedMenu(CONSTANTS.OFFICE);
                            navController.navigate(action);
                        }
                        else if (activity.getMainActivity().equals(CONSTANTS.TARGET))
                        {

                            navController.navigate(HomeFragmentDirections.actionNavHomeToNavTargetSubMenu());
                        }
                    }



                }
            }
        });


    }


}