package com.example.ffccloud.farm;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.ModelClasses.GetSupplierDetailModel;
import com.example.ffccloud.ModelClasses.SupplierModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.adapter.SupplierRecyclerViewAdapter;
import com.example.ffccloud.databinding.FragmentFarmListBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmListFragment extends Fragment {

    private FragmentFarmListBinding mBinding;
    private SupplierRecyclerViewAdapter adapter;
    private NavController navController;
    private int regionID=0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFarmListBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setUpRecyclerView();
        assert getArguments() != null;
        int key = FarmListFragmentArgs.fromBundle(getArguments()).getKey();
        regionID = FarmListFragmentArgs.fromBundle(getArguments()).getRegionID();
        if (key==1)
        {
         adapter.setKey(key);
         mBinding.addBtn.setVisibility(View.GONE);

        }

    }



    @Override
    public void onResume() {
        super.onResume();
        setPullToFresh();
        getSupplier();
        searchViewListener();
        btnListener();

    }

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections navDirections = FarmListFragmentDirections.actionFarmListFragmentToAddFarmFormFragment();
                navController.navigate(navDirections);
            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.farmListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SupplierRecyclerViewAdapter(this);
        adapter.setDestinationID(1);
        mBinding.farmListRecyclerview.setAdapter(adapter);

    }
    private void searchViewListener() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeLayout.setRefreshing(false);
                getSupplier();
            }
        });
    }

    private void getSupplier() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        int userID=0;
        if (regionID==0)
        {
            userID=SharedPreferenceHelper.getInstance(requireContext()).getUserID();
        }
        Call<List<GetSupplierModel>>  call = ApiClient.getInstance().getApi().getSupplier("F",userID,regionID);

        call.enqueue(new Callback<List<GetSupplierModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetSupplierModel>> call, @NotNull Response<List<GetSupplierModel>> response) {
                if(response.body()!=null)
                {
                    if (response.body().size()==0)
                    {
                        mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                    }
                    else {
                        mBinding.tvNothingFound.setVisibility(View.GONE);
                        List<GetSupplierModel> getSupplierModelList = response.body();
                        adapter.clearList();
                        adapter.setGetSupplierModelList(getSupplierModelList);
                        adapter.setCalledSupplier("Farm");
                    }
                    progressDialog.dismiss();

                }
                else
                {
                    progressDialog.dismiss();
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<GetSupplierModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}