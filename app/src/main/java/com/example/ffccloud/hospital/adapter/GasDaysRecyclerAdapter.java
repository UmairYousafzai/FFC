package com.example.ffccloud.hospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.FarmModel;
import com.example.ffccloud.databinding.GasDayCardBinding;

import java.util.ArrayList;
import java.util.List;

public class GasDaysRecyclerAdapter extends RecyclerView.Adapter<GasDaysRecyclerAdapter.GasDaysViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> gasDaysList;

    public GasDaysRecyclerAdapter() {

        gasDaysList = new ArrayList<>();
    }

    @NonNull
    @Override
    public GasDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        GasDayCardBinding binding = GasDayCardBinding.inflate(layoutInflater,parent,false);

        return new GasDaysViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull GasDaysViewHolder holder, int position) {

        String gasDays = gasDaysList.get(position);

        holder.mBinding.tvDate.setText(gasDays);


    }

    @Override
    public int getItemCount() {
        return gasDaysList ==null? 0: gasDaysList.size();
    }

    public  void setGasDaysList(List<String> list)
    {
        if (list!=null)
        {
            gasDaysList = list;
        }
        else
        {
            gasDaysList.clear();
        }
        notifyDataSetChanged();
    }

    public  class GasDaysViewHolder extends RecyclerView.ViewHolder
    {
        GasDayCardBinding mBinding;

        public GasDaysViewHolder(@NonNull GasDayCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gasDaysList.remove(gasDaysList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
