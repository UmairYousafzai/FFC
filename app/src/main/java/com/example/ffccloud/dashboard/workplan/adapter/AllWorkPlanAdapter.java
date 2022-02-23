package com.example.ffccloud.dashboard.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.dashboard.workplan.viewmodel.AllWorkPlanViewModel;
import com.example.ffccloud.databinding.AllWorkPlanCardBinding;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;

public class AllWorkPlanAdapter extends RecyclerView.Adapter<AllWorkPlanAdapter.AllWorkPlanViewHolder>{


    private final List<LookUpWorkPlan> workPlanList;
    private LayoutInflater layoutInflater;
    private AllWorkPlanViewModel viewModel;


    public AllWorkPlanAdapter(AllWorkPlanViewModel viewModel) {

        workPlanList = new ArrayList<>();
        this.viewModel= viewModel;

    }

    @NonNull
    @Override
    public AllWorkPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        AllWorkPlanCardBinding mbinding = AllWorkPlanCardBinding.inflate(layoutInflater, parent, false);
        return new AllWorkPlanViewHolder(mbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllWorkPlanViewHolder holder, int position) {
        LookUpWorkPlan lookUpWorkPlan = workPlanList.get(position);


        holder.mbinding.setWorkPlan(lookUpWorkPlan);
        holder.mbinding.setViewModel(viewModel);
        holder.mbinding.executePendingBindings();

    }


    @Override
    public int getItemCount() {


            return workPlanList.size();

    }



    public void setWorkPlanList(List<LookUpWorkPlan> list)
    {

        if (list!=null&&list.size()>0)
        {
            workPlanList.clear();
            workPlanList.addAll(list);
        }else
        {
            workPlanList.clear();
        }


        notifyDataSetChanged();
    }




    public class AllWorkPlanViewHolder extends RecyclerView.ViewHolder
    {

        private final AllWorkPlanCardBinding mbinding;

        public AllWorkPlanViewHolder(@NonNull AllWorkPlanCardBinding binding) {
            super(binding.getRoot());

            mbinding= binding;



//            mbinding.btnLocation.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int position = getAdapterPosition();
//
//                    if (workPlanList.get(position).getCustomerLocCord()!=null && !workPlanList.get(position).getCustomerLocCord().isEmpty())
//                    {
//                        String[] coordinates = workPlanList.get(position).getCustomerLocCord().split(",");
//                        String address= new CustomLocation(v.getContext()).getCompleteAddressString(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
//                        String locationString= "geo:"+ workPlanList.get(position).getCustomerLocCord()+"?q="+ workPlanList.get(position).getCustomerLocCord()+(address);
//                        Uri mapIntentUri= Uri.parse(locationString);
//                        Intent mapIntent= new Intent(Intent.ACTION_VIEW,mapIntentUri);
//                        mapIntent.setPackage("com.google.android.apps.maps");
//
//                        if (mapIntent.resolveActivity(mContext.getPackageManager())!=null)
//                        {
//                            mContext.startActivity(mapIntent);
//                        }
//                    }
//                }
//            });



        }
    }
}
