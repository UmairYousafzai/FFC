package com.example.ffccloud.dashboard.workplan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.Target.Adapters.DoctorMorningRecyclerAdapter;
import com.example.ffccloud.databinding.CardViewDoctorMorningTargetBinding;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.RecyclerOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AllWorkPlanAdapter extends RecyclerView.Adapter<AllWorkPlanAdapter.AllWorkPlanViewHolder>{


    private final Context mContext;
    private final List<DoctorModel> doctorModelList;
    private LayoutInflater layoutInflater;
    private int flag=0;
    private int key=0;

    public AllWorkPlanAdapter(Context mContext,int key) {
        this.mContext = mContext;
        doctorModelList= new ArrayList<>();
        this.key= key;


    }

    @NonNull
    @Override
    public AllWorkPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CardViewDoctorMorningTargetBinding mbinding = CardViewDoctorMorningTargetBinding.inflate(layoutInflater, parent, false);
        return new AllWorkPlanViewHolder(mbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllWorkPlanViewHolder holder, int position) {
        DoctorModel doctorModel = doctorModelList.get(position);

        doctorModel.setDistance("0.0");

        if (key==1)
        {
            holder.mbinding.btnLayout.setVisibility(View.GONE);
            holder.mbinding.simpleRatingBar.setVisibility(View.GONE);

        }
        holder.mbinding.setDoctorMorning(doctorModel);
        holder.mbinding.executePendingBindings();

    }


    @Override
    public int getItemCount() {


            return doctorModelList.size();

    }



    public void setDoctorModelList(List<DoctorModel> list)
    {

        if (list!=null&&list.size()>0)
        {
            doctorModelList.clear();
            doctorModelList.addAll(list);
        }else
        {
            doctorModelList.clear();
        }


        notifyDataSetChanged();
    }




    public class AllWorkPlanViewHolder extends RecyclerView.ViewHolder
    {

        private final CardViewDoctorMorningTargetBinding mbinding;

        public AllWorkPlanViewHolder(@NonNull CardViewDoctorMorningTargetBinding binding) {
            super(binding.getRoot());

            mbinding= binding;



            mbinding.btnMapMorningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (doctorModelList.get(position).getCoordinates()!=null && !doctorModelList.get(position).getCoordinates().isEmpty())
                    {
                        String[] coordinates = doctorModelList.get(position).getCoordinates().split(",");
                        String address= new CustomLocation(mContext).getCompleteAddressString(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
                        String locationString= "geo:"+doctorModelList.get(position).getCoordinates()+"?q="+ doctorModelList.get(position).getCoordinates()+(address);
                        Uri mapIntentUri= Uri.parse(locationString);
                        Intent mapIntent= new Intent(Intent.ACTION_VIEW,mapIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(mContext.getPackageManager())!=null)
                        {
                            mContext.startActivity(mapIntent);
                        }
                    }
                }
            });



        }
    }
}
