package com.example.ffccloud.Customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ffccloud.Customer.Adapter.CustomerListRecyclerAdapter;
import com.example.ffccloud.Customer.utils.CustomerViewModel;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.databinding.FragmentCustomerListBinding;
import com.example.ffccloud.utils.SyncDataToDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerListFragment extends Fragment {

    private FragmentCustomerListBinding mBinding;
    private CustomerListRecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    private NavController navController;
    private CustomerViewModel customerViewModel;
    private List<CustomerModel> customerModelList= new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentCustomerListBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


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

        customerViewModel.getAllCustomer().observe(getViewLifecycleOwner(), new Observer<List<CustomerModel>>() {
            @Override
            public void onChanged(List<CustomerModel> customerModels) {

                if (customerModels!=null)
                {
                    if (customerModels.size()>0)
                    {
                        mBinding.tvNothingFound.setVisibility(View.INVISIBLE);
                        mBinding.customerListRecyclerview.setVisibility(View.VISIBLE);


                    }
                    else
                    {
                        mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                        mBinding.customerListRecyclerview.setVisibility(View.INVISIBLE);
                    }
                    customerModelList= customerModels;
                    adapter.setContactPersonsModelList(customerModelList);

                }else
                {
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                    mBinding.customerListRecyclerview.setVisibility(View.INVISIBLE);
                }
                progressDialog.dismiss();
            }
        });


    }
    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeLayout.setRefreshing(false);
                if (isNetworkConnected())
                {
                    customerViewModel.deleteAllCustomers();

                    new SyncDataToDB().getCustomerData(requireContext());
                }
                else
                {
                    Toast.makeText(requireContext(), "Customer Loading Failed \n Please connect to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void setupRecyclerView() {
        mBinding.customerListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CustomerListRecyclerAdapter(this);
        mBinding.customerListRecyclerview.setAdapter(adapter);
    }

    public boolean isNetworkConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        } else {
            return connected = false;
        }

    }
}