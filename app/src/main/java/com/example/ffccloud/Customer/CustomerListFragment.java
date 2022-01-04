package com.example.ffccloud.Customer;

import android.app.ProgressDialog;
import android.os.Bundle;

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

import com.example.ffccloud.Customer.Adapter.CustomerListRecyclerAdapter;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.FragmentCustomerListBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListFragment extends Fragment {

    private FragmentCustomerListBinding mBinding;
    private CustomerListRecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    private NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentCustomerListBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        navController = NavHostFragment.findNavController(this);

        setupRecyclerView();
        getCustomerData();
        btnListener();
        searchViewListener();
        setPullToFresh();
        //if key = 1 then it means calling fragment is salesOrder Fragment
        assert getArguments() != null;
        int callingFragmentKey= CustomerListFragmentArgs.fromBundle(getArguments()).getCallingFragmentKey();
        if (callingFragmentKey==1)
        {
            mBinding.addBtn.setVisibility(View.GONE);
            adapter.setCallingFragmentKey(callingFragmentKey);
        }


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

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = CustomerListFragmentDirections.actionCustomerListFragmentToAddCustomerFragment();

                navController.navigate(action);


            }
        });
    }

    private void getCustomerData() {

        int userID= SharedPreferenceHelper.getInstance(requireContext()).getUserID();

        Call<List<CustomerModel>> call = ApiClient.getInstance().getApi().getAllCustomer(1,1,"C",userID);

        call.enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<CustomerModel>> call, @NotNull Response<List<CustomerModel>> response) {

                if(response.body()!=null)
                {
                    if (response.body().size()==0)
                    {
                        mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                    }
                    else {
                        mBinding.tvNothingFound.setVisibility(View.GONE);
                        adapter.setContactPersonsModelList(response.body());
                        progressDialog.dismiss();
                    }

                }
                else
                {
                    progressDialog.dismiss();
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<CustomerModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                mBinding.tvNothingFound.setVisibility(View.VISIBLE);

            }
        });
    }
    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeLayout.setRefreshing(false);
               getCustomerData();
            }
        });
    }
    private void setupRecyclerView() {
        mBinding.customerListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CustomerListRecyclerAdapter(this);
        mBinding.customerListRecyclerview.setAdapter(adapter);
    }
}