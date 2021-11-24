package com.example.ffccloud.salesOrder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.SaleOrderModel;
import com.example.ffccloud.databinding.SaleOrderCardBinding;

import java.util.ArrayList;
import java.util.List;

public class SaleOrderAdapter extends RecyclerView.Adapter<SaleOrderAdapter.saleOrderViewHolder>{


    private LayoutInflater layoutInflater;
    private List<SaleOrderModel> saleOrderModelList;

    public SaleOrderAdapter() {

        saleOrderModelList= new ArrayList<>();
    }

    @NonNull
    @Override
    public saleOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        SaleOrderCardBinding binding = SaleOrderCardBinding.inflate(layoutInflater,parent,false);

        return new saleOrderViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull saleOrderViewHolder holder, int position) {

        SaleOrderModel saleOrderModel = new SaleOrderModel();
        saleOrderModel = saleOrderModelList.get(position);

        holder.mBinding.setSaleOrder(saleOrderModel);
        holder.mBinding.executePendingBindings();

        if (saleOrderModel.getPost())
        {
            holder.mBinding.status.setText("Posted");
        }
        else  if (saleOrderModel.isClose())
        {
            holder.mBinding.status.setText("Closed");
        }
        else  if (saleOrderModel.isCancel())
        {
            holder.mBinding.status.setText("Canceled");
        }



    }

    @Override
    public int getItemCount() {
        return saleOrderModelList==null? 0:saleOrderModelList.size();
    }

    public  void setSaleOrderModelList(List<SaleOrderModel> list)
    {
        if (list!=null)
        {
            saleOrderModelList = list;
        }
        else
        {
            saleOrderModelList.clear();
        }
        notifyDataSetChanged();
    }

    public  class saleOrderViewHolder extends RecyclerView.ViewHolder
    {
        SaleOrderCardBinding mBinding;

        public saleOrderViewHolder(@NonNull SaleOrderCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

        }
    }
}
