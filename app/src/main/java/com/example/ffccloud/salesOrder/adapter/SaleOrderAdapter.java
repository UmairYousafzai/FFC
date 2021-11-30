package com.example.ffccloud.salesOrder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ffccloud.GetSaleOrderModel;
import com.example.ffccloud.databinding.SaleOrderCardBinding;

import java.util.ArrayList;
import java.util.List;

public class SaleOrderAdapter extends RecyclerView.Adapter<SaleOrderAdapter.saleOrderViewHolder>{


    private LayoutInflater layoutInflater;
    private List<GetSaleOrderModel> getSaleOrderModelList;

    public SaleOrderAdapter() {

        getSaleOrderModelList = new ArrayList<>();
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

        GetSaleOrderModel getSaleOrderModel = new GetSaleOrderModel();
        getSaleOrderModel = getSaleOrderModelList.get(position);

        holder.mBinding.setSaleOrder(getSaleOrderModel);
        holder.mBinding.executePendingBindings();

        if (getSaleOrderModel.getPost())
        {
            holder.mBinding.status.setText("Posted");
        }
        else  if (getSaleOrderModel.isClose())
        {
            holder.mBinding.status.setText("Closed");
        }
        else  if (getSaleOrderModel.isCancel())
        {
            holder.mBinding.status.setText("Canceled");
        }



    }

    @Override
    public int getItemCount() {
        return getSaleOrderModelList ==null? 0: getSaleOrderModelList.size();
    }

    public  void setGetSaleOrderModelList(List<GetSaleOrderModel> list)
    {
        if (list!=null)
        {
            getSaleOrderModelList = list;
        }
        else
        {
            getSaleOrderModelList.clear();
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
