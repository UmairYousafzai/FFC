package com.example.ffccloud.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.Customer.CustomerListFragmentDirections;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.databinding.CustomerCardBinding;

import java.util.List;

public class CustomerListRecyclerAdapter extends RecyclerView.Adapter<CustomerListRecyclerAdapter.CustomerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CustomerModel> customerModelList;
    private final Fragment fragment;
    private int callingFragmentKey=0;

    public CustomerListRecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CustomerCardBinding binding = CustomerCardBinding.inflate(layoutInflater,parent,false);

        return new CustomerViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

        CustomerModel customerModel = new CustomerModel();
        customerModel = customerModelList.get(position);

        holder.mBinding.setCustomer(customerModel);
        holder.mBinding.executePendingBindings();
        holder.mBinding.tvCity.setText(String.valueOf(customerModel.getCity_Id()));


    }

    @Override
    public int getItemCount() {
        return customerModelList==null? 0:customerModelList.size();
    }


    public void setCallingFragmentKey(int callingFragmentKey) {
        this.callingFragmentKey = callingFragmentKey;
    }


    public  void setContactPersonsModelList(List<CustomerModel> list)
    {
        if (list!=null)
        {
            customerModelList = list;
        }
        else
        {
            customerModelList.clear();
        }
        notifyDataSetChanged();
    }

    public  class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        CustomerCardBinding mBinding;

        public CustomerViewHolder(@NonNull CustomerCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    CustomerListFragmentDirections.ActionCustomerListFragmentToAddCustomerFragment action = CustomerListFragmentDirections.actionCustomerListFragmentToAddCustomerFragment();
                    action.setCustomer(customerModelList.get(getAdapterPosition()));
                    navController.navigate(action);


                }
            });
        }
    }
}
