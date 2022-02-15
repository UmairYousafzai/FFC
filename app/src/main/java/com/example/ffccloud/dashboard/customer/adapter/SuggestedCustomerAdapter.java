package com.example.ffccloud.dashboard.customer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.dashboard.customer.viewmodel.CustomerViewModel;
import com.example.ffccloud.databinding.DashBoardCutomerCardBinding;
import com.example.ffccloud.expense.utils.ExpenseViewModel;
import com.example.ffccloud.model.DashBoardCustomer;
import com.example.ffccloud.model.EmployeeExpense;

import java.util.ArrayList;
import java.util.List;

public class SuggestedCustomerAdapter extends RecyclerView.Adapter<SuggestedCustomerAdapter.CustomerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DashBoardCustomer> dashBoardCustomerList;
    private CustomerViewModel viewModel;

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

        holder.mBinding.setCustomer(dashBoardCustomerList.get(position));
        holder.mBinding.setViewModel(viewModel);
        holder.mBinding.executePendingBindings();
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

    public static class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        DashBoardCutomerCardBinding mBinding;

        public CustomerViewHolder(@NonNull DashBoardCutomerCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;
        }
    }
}
