package com.example.ffccloud.medicalStore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.CompanyModel;
import com.example.ffccloud.Medicine_modal;
import com.example.ffccloud.databinding.CompanyCardBinding;

import java.util.ArrayList;
import java.util.List;

public class CompanyRecyclerViewAdapter extends RecyclerView.Adapter<CompanyRecyclerViewAdapter.CompanyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CompanyModel> companyModelList;

    public CompanyRecyclerViewAdapter() {

        companyModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CompanyCardBinding binding = CompanyCardBinding.inflate(layoutInflater,parent,false);

        return new CompanyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {

        CompanyModel companyModel = new CompanyModel();
        companyModel = companyModelList.get(position);

        holder.mBinding.setCompany(companyModel);
        holder.mBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return companyModelList ==null? 0: companyModelList.size();
    }

    public  void setCompanyModelList(List<CompanyModel> list)
    {
        if (list!=null)
        {
            companyModelList = list;
        }
        else
        {
            companyModelList.clear();
        }
        notifyDataSetChanged();
    }

    public  class CompanyViewHolder extends RecyclerView.ViewHolder
    {
        CompanyCardBinding mBinding;

        public CompanyViewHolder(@NonNull CompanyCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    companyModelList.remove(companyModelList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
