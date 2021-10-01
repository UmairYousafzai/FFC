package com.example.myapplication.Target.Adapters;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DoctorModel;
import com.example.myapplication.databinding.CardViewDoctorEveningTargetBinding;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.RecyclerOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorEveningRecyclerAdapter extends RecyclerView.Adapter<DoctorEveningRecyclerAdapter.DoctorViewHolder> {

    private Context mContext;
    private List<DoctorModel> doctorModelList;
    private LayoutInflater layoutInflater;
    private int flag=0;
    private RecyclerOnItemClickListener mListener;
    private CustomLocation customLocation;
    private Activity activity;
    private Location currrentLocation;
    private ProgressDialog progressDialog;
    public DoctorEveningRecyclerAdapter(Context mContext, RecyclerOnItemClickListener mListener, Activity activity) {
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
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CardViewDoctorEveningTargetBinding mbinding = CardViewDoctorEveningTargetBinding.inflate(layoutInflater, parent, false);
        return new DoctorViewHolder(mbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        DoctorModel doctorModel = doctorModelList.get(position);
        progressDialog = new ProgressDialog(mContext);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.setMessage("Loading");
                progressDialog.show();
                Location givenLocation = new Location("");

                if (currrentLocation != null && givenLocation.getLatitude()!=0 && givenLocation.getLongitude()!=0)
                {
                    String[] locationString = doctorModel.getCoordinates().split(",");
                    givenLocation.setLatitude(Double.parseDouble(locationString[0]));
                    givenLocation.setLongitude(Double.parseDouble(locationString[1]));
                    String distance= String.valueOf(currrentLocation.distanceTo(givenLocation)/1000) ;
                    doctorModel.setDistance(distance.substring(0,3)+" "+"Km");
                    holder.mbinding.setDoctorEvening(doctorModel);
                    holder.mbinding.executePendingBindings();
                }
                else {
                    doctorModel.setDistance("0.0");
                    holder.mbinding.setDoctorEvening(doctorModel);
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


    public class DoctorViewHolder extends RecyclerView.ViewHolder
    {

        private CardViewDoctorEveningTargetBinding mbinding;
        public DoctorViewHolder(@NonNull CardViewDoctorEveningTargetBinding binding) {
            super(binding.getRoot());

            mbinding= binding;

            mbinding.btnCancelEveningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,1);
                    }
                }
            });
            mbinding.btnRescheduleEveningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,2);
                    }
                }
            });
            mbinding.targetCardEvening.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        int position = getAdapterPosition();
                        mListener.GetDoctorOnClick(doctorModelList.get(position),position,10);
                    }
                }
            });

            mbinding.btnMapEveningCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (doctorModelList.get(position).getCoordinates()!=null&&!doctorModelList.get(position).getCoordinates().isEmpty())
                    {
                        String locationString= "geo:"+doctorModelList.get(position).getCoordinates();
                        Uri mapIntentUri= Uri.parse(locationString);
                        Intent mapIntent= new Intent(Intent.ACTION_VIEW,mapIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(mContext.getPackageManager())!=null)
                        {
                            mContext.startActivity(mapIntent);
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext,"Location Coordinates Are Empty",Toast.LENGTH_LONG).show();
                    }
                }
            });



        }
    }
}
