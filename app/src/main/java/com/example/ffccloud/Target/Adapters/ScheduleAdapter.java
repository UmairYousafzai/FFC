package com.example.ffccloud.Target.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ScheduleModel;
import com.example.ffccloud.databinding.FilterDocShecduleCardBinding;

import java.util.HashMap;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.FullDocInfoShecduleViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ScheduleModel> scheduleModelList;
    private HashMap<Integer, String> dayHashMap = new HashMap<>();
    private String[] days;

    public ScheduleAdapter() {

        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < days.length; i++) {
            dayHashMap.put(i + 1, days[i]);
        }
    }

    @NonNull
    @Override
    public FullDocInfoShecduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        FilterDocShecduleCardBinding binding = FilterDocShecduleCardBinding.inflate(layoutInflater, parent, false);

        return new FullDocInfoShecduleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FullDocInfoShecduleViewHolder holder, int position) {
        String day = dayHashMap.get(scheduleModelList.get(position).getDayId());

        ScheduleModel model = scheduleModelList.get(position);

        holder.mBinding.txtDay.setText(day);
        holder.mBinding.setScheduleModel(model);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return scheduleModelList == null ? 0 : scheduleModelList.size();
    }

    public void setScheduleModelList(List<ScheduleModel> list) {
        scheduleModelList = list;
        notifyDataSetChanged();
    }

    public void setSchedule(ScheduleModel scheduleModel)
    {
        scheduleModelList.add(scheduleModel);
        notifyItemChanged(scheduleModelList.size());
    }

    public static class FullDocInfoShecduleViewHolder extends RecyclerView.ViewHolder {
        FilterDocShecduleCardBinding mBinding;

        public FullDocInfoShecduleViewHolder(@NonNull FilterDocShecduleCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
