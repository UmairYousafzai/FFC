package com.example.myapplication.Target.Visit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.SplashScreen.SplashActivity;
import com.example.myapplication.WorkPlanHistory;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.databinding.FragmentHistoryBinding;
import com.example.myapplication.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    private NavController navController;
    private FragmentHistoryBinding mBinding;
    private HistoryCardAdapter adapter;
    private int doc_ID;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mBinding = FragmentHistoryBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        doc_ID = (int) HistoryFragmentArgs.fromBundle(getArguments()).getDocId();

        setupRecyclerView();
        getWorkPlanHistory();
    }

    private void setupRecyclerView() {

        adapter = new HistoryCardAdapter();
        mBinding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.historyRecyclerView.setAdapter(adapter);
    }

    public void getWorkPlanHistory()
    {
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int UserID = SharedPreferenceHelper.getInstance(requireContext()).getUserID();

        Call<List<WorkPlanHistory>> call = ApiClient.getInstance().getApi().GetWorkPlanHistory(token,doc_ID,UserID);
        call.enqueue(new Callback<List<WorkPlanHistory>>() {
            @Override
            public void onResponse(Call<List<WorkPlanHistory>> call, Response<List<WorkPlanHistory>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<WorkPlanHistory> list = new ArrayList<>();
                        list= response.body();
                        adapter.setWorkPlanHistoryList(list);


                    } else {

                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<List<WorkPlanHistory>> call, Throwable t) {

            }
        });




    }
}