package com.example.myapplication.TargetMenu;

import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.Target.Adapters.ScheduleAdapter;
import com.example.myapplication.databinding.FilterDocShecduleCardBinding;
import com.example.myapplication.databinding.RouteCardBinding;

import java.util.ArrayList;
import java.util.List;

public class ShowRouteAdapter extends RecyclerView.Adapter<ShowRouteAdapter.ShowRouteAdapterViewHolder> {
    private LayoutInflater layoutInflater;
    List<Activity> activityList;

    public ShowRouteAdapter(List<Activity> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ShowRouteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RouteCardBinding binding = RouteCardBinding.inflate(layoutInflater, parent, false);

        return new ShowRouteAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowRouteAdapterViewHolder holder, int position) {
        Activity activity = new Activity();
        activity= activityList.get(position);

        holder.mBinding.mainActivity.setText("Main Activity: "+activity.getMainActivity());
        holder.mBinding.subActivity.setText("Sub Activity: "+activity.getSubActivity());
        holder.mBinding.activityId.setText("Activity ID: "+String.valueOf(activity.getActivityId()));
        holder.mBinding.taskid.setText("Task ID: "+String.valueOf(activity.getTaskID()));
        holder.mBinding.startTime.setText("Start Time: "+activity.getStartDateTime());
        holder.mBinding.startLocation.setText("Start Location: "+activity.getStartCoordinates());
        holder.mBinding.endTime.setText("End Time: "+activity.getEndDateTime());
        holder.mBinding.endLocation.setText("End Location: "+activity.getEndCoordinates());
        holder.mBinding.totalTime.setText("Total Time Spend: "+activity.getTotalTime());
        holder.mBinding.endLocationAddress.setText("End Location Address: "+activity.getEndAddress());
        holder.mBinding.startLocationAddress.setText("Start Location Address: "+activity.getStartAddress());
        holder.mBinding.distance.setText("Distance: "+activity.getDistance());


    }

    @Override
    public int getItemCount() {
        return activityList==null?0:activityList.size();
    }

    public static class ShowRouteAdapterViewHolder extends RecyclerView.ViewHolder {
        RouteCardBinding mBinding;

        public ShowRouteAdapterViewHolder(@NonNull RouteCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
