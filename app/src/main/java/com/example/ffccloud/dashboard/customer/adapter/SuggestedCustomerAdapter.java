package com.example.ffccloud.dashboard.customer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.R;
import com.example.ffccloud.dashboard.customer.viewmodel.CustomerViewModel;
import com.example.ffccloud.databinding.DashBoardCutomerCardBinding;
import com.example.ffccloud.model.DashBoardCustomer;

import java.util.ArrayList;
import java.util.List;

public class SuggestedCustomerAdapter extends RecyclerView.Adapter<SuggestedCustomerAdapter.CustomerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DashBoardCustomer> dashBoardCustomerList;
    private CustomerViewModel viewModel;
    private String type;

    public SuggestedCustomerAdapter(CustomerViewModel customerViewModel)
    {
        dashBoardCustomerList = new ArrayList<>();
        this.viewModel= customerViewModel;
    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DashBoardCutomerCardBinding binding = DashBoardCutomerCardBinding.inflate(layoutInflater,parent,false);

        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

        DashBoardCustomer customer= dashBoardCustomerList.get(position);
        holder.mBinding.setCustomer(customer);
        holder.mBinding.setViewModel(viewModel);
        holder.mBinding.executePendingBindings();

        if(customer.getUserType().equals("F"))
        {
            holder.mBinding.imageView.setImageResource(R.drawable.ic_farm_svgrepo_com);
        }
        else if(customer.getUserType().equals("Dr"))
        {
            holder.mBinding.imageView.setImageResource(R.drawable.ic_doctor);
        } else if(customer.getUserType().equals("H"))
        {
            holder.mBinding.imageView.setImageResource(R.drawable.ic_hospital);
        } else if(customer.getUserType().equals("Str"))
        {
            holder.mBinding.imageView.setImageResource(R.drawable.ic_medical_pharmacy_store);
        }
    }

    @Override
    public int getItemCount() {
        return dashBoardCustomerList.size();
    }

    public void setDashBoardCustomerList(List<DashBoardCustomer> list)
    {
        dashBoardCustomerList.clear();

        if (list!=null)
        {
            dashBoardCustomerList.addAll(list);
        }

        notifyDataSetChanged();

    }

    public void clearCustomer(DashBoardCustomer customer)
    {
        dashBoardCustomerList.remove(customer);
        notifyDataSetChanged();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        DashBoardCutomerCardBinding mBinding;

        public CustomerViewHolder(@NonNull DashBoardCutomerCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;
        }
    }
}
