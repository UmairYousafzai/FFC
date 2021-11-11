package com.example.ffccloud.Tracking;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.R;
import com.example.ffccloud.Tracking.Adapter.RequestsRecyclerViewAdapter;
import com.example.ffccloud.databinding.FragmentLocationRequestBinding;
import com.example.ffccloud.utils.UserViewModel;

import java.util.List;

public class LocationRequestFragment extends Fragment {

    private FragmentLocationRequestBinding mBinding;
    private UserViewModel userViewModel;
    private RequestsRecyclerViewAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentLocationRequestBinding.inflate(inflater,container,false);

        userViewModel= new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();



        setUpRecyclerView();
        getRequestFromDB();



    }

    private void getRequestFromDB() {
        userViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<LocationRequestedUser>>() {
            @Override
            public void onChanged(List<LocationRequestedUser> LocationRequestedUsers) {
                adapter.setUserList(LocationRequestedUsers);
            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.locationRequestRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter= new RequestsRecyclerViewAdapter(requireContext(),requireActivity());
        mBinding.locationRequestRecycler.setAdapter(adapter);
    }
}