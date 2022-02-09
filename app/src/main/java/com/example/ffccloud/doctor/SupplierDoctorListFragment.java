package com.example.ffccloud.doctor;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.adapter.SupplierRecyclerViewAdapter;
import com.example.ffccloud.databinding.FragmentDoctorListFormBinding;
import com.example.ffccloud.farm.FarmListFragmentArgs;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierDoctorListFragment extends Fragment {

    private FragmentDoctorListFormBinding mBinding;
    private SupplierRecyclerViewAdapter adapter;
    private NavController navController;
    private int regionId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentDoctorListFormBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        setUpRecyclerView();

        assert getArguments() != null;
        int key = SupplierDoctorListFragmentArgs.fromBundle(getArguments()).getKey();
        regionId = SupplierDoctorListFragmentArgs.fromBundle(getArguments()).getRegionID();

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
        searchViewListener();
        getSupplier();
        btnListener();

    }

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections= SupplierDoctorListFragmentDirections.actionSupplierDoctorFragmentToAddDoctorFragment();
                navController.navigate(navDirections);
            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.doctorListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SupplierRecyclerViewAdapter(this);
        adapter.setDestinationID(2);
        mBinding.doctorListRecyclerview.setAdapter(adapter);

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
        if (regionId==0)
        {
            userID=SharedPreferenceHelper.getInstance(requireContext()).getUserID();
        }

        Call<List<GetSupplierModel>> call = ApiClient.getInstance().getApi().getSupplier("Dr",userID,regionId,0);

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
                        adapter.setCalledSupplier("Doctor");
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