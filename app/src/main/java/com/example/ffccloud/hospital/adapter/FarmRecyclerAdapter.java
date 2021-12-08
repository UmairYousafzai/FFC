package com.example.ffccloud.hospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.FarmModel;
import com.example.ffccloud.databinding.FarmCardBinding;

import java.util.ArrayList;
import java.util.List;

public class FarmRecyclerAdapter extends RecyclerView.Adapter<FarmRecyclerAdapter.FarmViewHolder> {

    private LayoutInflater layoutInflater;
    private List<FarmModel> farmModelList;

    public FarmRecyclerAdapter() {

        farmModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        FarmCardBinding binding = FarmCardBinding.inflate(layoutInflater,parent,false);

        return new FarmViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {

        FarmModel farmModel = new FarmModel();
        farmModel = farmModelList.get(position);

        holder.mBinding.setFarm(farmModel);
        holder.mBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return farmModelList ==null? 0: farmModelList.size();
    }

    public  void setFarmModelList(List<FarmModel> list)
    {
        if (list!=null)
        {
            farmModelList = list;
        }
        else
        {
            farmModelList.clear();
        }
        notifyDataSetChanged();
    }

    public  class FarmViewHolder extends RecyclerView.ViewHolder
    {
        FarmCardBinding mBinding;

        public FarmViewHolder(@NonNull FarmCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    farmModelList.remove(farmModelList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
