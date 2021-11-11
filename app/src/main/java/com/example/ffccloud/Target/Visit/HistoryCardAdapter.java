package com.example.ffccloud.Target.Visit;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.WorkPlanHistory;
import com.example.ffccloud.databinding.HistroyCardBinding;


import java.util.ArrayList;
import java.util.List;

public class HistoryCardAdapter extends RecyclerView.Adapter<HistoryCardAdapter.HistoryCardViewHolder> {
    private LayoutInflater layoutInflater;
    private List<WorkPlanHistory> workPlanHistoryList;

    public HistoryCardAdapter() {
        workPlanHistoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        HistroyCardBinding binding = HistroyCardBinding.inflate(layoutInflater, parent, false);

        return new HistoryCardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCardViewHolder holder, int position) {
        WorkPlanHistory workPlanHistory = workPlanHistoryList.get(position);
        String date[] = dateTimeFormat(workPlanHistory.getWorkFromDate()).split(" ");
        String time = date[0]+"\n"+date[1];
        workPlanHistory.setTime(time);
        workPlanHistory.setWorkFromDate(date[2]);
        holder.mBinding.setWorkPlanHistory(workPlanHistory);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return workPlanHistoryList == null ? 0 : workPlanHistoryList.size();
    }

    public void setWorkPlanHistoryList(List<WorkPlanHistory> list) {
        if (list != null) {
            workPlanHistoryList.addAll(list);
            notifyDataSetChanged();
        }

    }

    private String dateTimeFormat(String t) {
        String result;

        String[] dateTime = t.split("T");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");
        String dated = date[2] + "/" + date[1] + "/" + date[0];
        String timed = "";

        int hours = Integer.valueOf(time[0]);

        if (hours >= 12) {
            if (hours == 12) {
                timed = hours + ":" + time[1] + " PM";
            } else {
                hours = hours - 12;
                timed = hours + ":" + time[1] + " PM";
            }

        } else if (hours == 0) {
            timed = 12 + ":" + time[1] + " AM";
        } else {
            timed = hours + ":" + time[1] + " AM";
        }


        //  result = dated+" "+timed;

        result = timed + " " + dated;
        return result;
    }

    public static class HistoryCardViewHolder extends RecyclerView.ViewHolder {
        HistroyCardBinding mBinding;

        public HistoryCardViewHolder(@NonNull HistroyCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
