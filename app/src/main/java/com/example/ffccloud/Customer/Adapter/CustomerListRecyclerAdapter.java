package com.example.ffccloud.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.Customer.CustomerListFragmentDirections;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.databinding.CustomerCardBinding;
import com.example.ffccloud.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerListRecyclerAdapter extends RecyclerView.Adapter<CustomerListRecyclerAdapter.CustomerViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<CustomerModel> customerModelList,customerModelListFull;
    private final Fragment fragment;
    private int callingFragmentKey=0;

    public CustomerListRecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
        customerModelListFull = new ArrayList<>();
        customerModelList= new ArrayList<>();
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
        if (callingFragmentKey==1)
        {
            holder.mBinding.btnAdd.setVisibility(View.VISIBLE);
            holder.mBinding.btnEdit.setVisibility(View.GONE);

        }

        holder.mBinding.setCustomer(customerModel);
        holder.mBinding.executePendingBindings();


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
        if (list!=null&&list.size()>0)
        {
            customerModelListFull.clear();
            customerModelList.addAll(list);
            customerModelListFull.addAll(customerModelList);
        }
        else
        {
            customerModelList.clear();
        }
        notifyDataSetChanged();
    }



    @Override
    public Filter getFilter() {
        return filter;
    }


    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


                List<CustomerModel> filterList= new ArrayList<>();

                if (customerModelListFull.size()>0)
                {
                    if (constraint==null||constraint.length()==0)
                    {
                        filterList= customerModelListFull;
                    }
                    else {
                        String filterPattern= constraint.toString().toLowerCase().trim();
                        for (CustomerModel model:customerModelListFull)
                        {
                            if (model.getPartyName().toString().toLowerCase().trim().contains(filterPattern))
                            {
                                filterList.add(model);
                            }
                        }
                    }
                }


                FilterResults filterResults = new FilterResults();
                if (filterList.size()>0)
                {
                    filterResults.values= filterList;

                }
                return filterResults;



        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values!=null)
            {
                customerModelList.clear();
                customerModelList.addAll((List)results.values);
                notifyDataSetChanged();
            }





        }
    };


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
                    action.setCustomerCode(customerModelList.get(getAdapterPosition()).getPartyCode());
                    navController.navigate(action);


                }
            });

            mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(fragment);

                        Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.CUSTOMER_KEY, customerModelList.get(getAdapterPosition()));
                        navController.popBackStack();




                }
            });
        }
    }
}
