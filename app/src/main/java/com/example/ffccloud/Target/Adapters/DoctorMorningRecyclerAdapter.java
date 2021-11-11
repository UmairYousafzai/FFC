package com.example.ffccloud.Target.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.databinding.CardViewDoctorMorningTargetBinding;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.RecyclerOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorMorningRecyclerAdapter extends RecyclerView.Adapter<DoctorMorningRecyclerAdapter.DoctorMorningViewHolder> {

    private Context mContext;
    private List<DoctorModel> doctorModelList;
    private LayoutInflater layoutInflater;
    private int flag=0;
    private RecyclerOnItemClickListener mListener;
    private CustomLocation customLocation;
    private Activity activity;
    private Location currrentLocation;
    private ProgressDialog progressDialog;
    public DoctorMorningRecyclerAdapter(Context mContext, RecyclerOnItemClickListener mListener, Activity activity) {
        this.mContext = mContext;
        doctorModelList= new ArrayList<>();
        this.mListener = mListener;
        customLocation= new CustomLocation(mContext);
        this.activity = activity;
        CustomLocation.CustomLocationResults results= new CustomLocation.CustomLocationResults() {
            @Override
            public void gotLocation(Location location) {
                currrentLocation=location;
            }
        };
        customLocation.getLastLocation(results);
    }

    @NonNull
    @Override
    public DoctorMorningRecyclerAdapter.DoctorMorningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CardViewDoctorMorningTargetBinding mbinding = CardViewDoctorMorningTargetBinding.inflate(layoutInflater, parent, false);
        return new DoctorMorningRecyclerAdapter.DoctorMorningViewHolder(mbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorMorningRecyclerAdapter.DoctorMorningViewHolder holder, int position) {
        DoctorModel doctorModel = doctorModelList.get(position);
        progressDialog = new ProgressDialog(mContext);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.setMessage("Lodaing");
                progressDialog.show();
                Location givenLocation = new Location("");

                if (currrentLocation != null && givenLocation.getLatitude()!=0 && givenLocation.getLongitude()!=0)
                {
                    String[] locationString = doctorModel.getCoordinates().split(",");

                    givenLocation.setLatitude(Double.parseDouble(locationString[0]));
                    givenLocation.setLongitude(Double.parseDouble(locationString[1]));
                    String distance= String.valueOf(currrentLocation.distanceTo(givenLocation)/1000) ;
                    doctorModel.setDistance(distance.substring(0,3)+" "+"Km");
                    holder.mbinding.setDoctorMorning(doctorModel);
                    holder.mbinding.executePendingBindings();
                }
                else {
                    doctorModel.setDistance("0.0");
                    holder.mbinding.setDoctorMorning(doctorModel);
                    holder.mbinding.executePendingBindings();
                }
                progressDialog.cancel();
            }
        }, 1000);





    }
    public void clearData()
    {

        if (doctorModelList!=null&&doctorModelList.size()>0)
        {
            int size = doctorModelList.size();
            doctorModelList.clear();
            notifyItemRangeRemoved(0,size);
        }

    }
    public void setFlag(int value)
    {
        flag=value;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (flag==0&&doctorModelList.size()>0)
        {
            return 1;
        }
        else if (flag==0 && doctorModelList.size()==0)
        {
            return doctorModelList.size();
        }
        else
        {
            return doctorModelList.size();
        }

    }

    //    public double getDistanceFromCurrentLocation(String location) {
//        CustomLocation.CustomLocationResults results= new CustomLocation.CustomLocationResults() {
//            @Override
//            public void gotLocation(Location location) {
//                currrentLocation=location;
//            }
//        };
//        customLocation.getLastLocation(mContext,activity,results);
//        String[] locationString = location.split(",");
//        Location givenLocation = new Location("");
//        givenLocation.setLatitude(Double.parseDouble(locationString[0]));
//        givenLocation.setLongitude(Double.parseDouble(locationString[1]));
//        if (currrentLocation != null && givenLocation != null)
//        {
//            return currrentLocation.distanceTo(givenLocation);
//        }
//        else
//        {
//            return 0;
//        }
//
//
//
//    }
    public void setDoctorModelList(List<DoctorModel> list)
    {

        doctorModelList.clear();
        doctorModelList.addAll(list);

        notifyDataSetChanged();
    }
    public void removeItem(DoctorModel doctorModel)
    {
        if (doctorModelList.contains(doctorModel)) {
            doctorModelList.remove(doctorModel);
        }
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        currrentLocation=location;
        notifyDataSetChanged();
    }


    public class DoctorMorningViewHolder extends RecyclerView.ViewHolder
    {

        private final CardViewDoctorMorningTargetBinding mbinding;
        public DoctorMorningViewHolder(@NonNull CardViewDoctorMorningTargetBinding binding) {
            super(binding.getRoot());

            mbinding= binding;

            mbinding.btnCancelMorningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,1);
                    }
                }
            });
            mbinding.btnRescheduleMorningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,2);
                    }
                }
            });
            mbinding.targetCardMorning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,10);
                    }
                }
            });

            mbinding.btnMapMorningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (doctorModelList.get(position).getCoordinates()!=null)
                    {
                        String locationString= "geo:"+doctorModelList.get(position).getCoordinates()+"?q=3";
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
