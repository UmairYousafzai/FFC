package com.example.ffccloud.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.ModelClasses.CustomerModel;
import com.example.ffccloud.databinding.ContactPersonsCardBinding;
import com.example.ffccloud.databinding.CustomerCardBinding;

import java.util.List;

public class CustomerListRecyclerAdapter extends RecyclerView.Adapter<CustomerListRecyclerAdapter.CustomerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CustomerModel> customerModelList;



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



    }

    @Override
    public int getItemCount() {
        return customerModelList==null? 0:customerModelList.size();
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

    public static class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        CustomerCardBinding mBinding;

        public CustomerViewHolder(@NonNull CustomerCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}
