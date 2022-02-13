package com.example.ffccloud.workplan.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.databinding.WorkPlanCardBinding;
import com.example.ffccloud.model.WorkPlan;
import com.example.ffccloud.workplan.viewmodel.WorkPlanViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkPlanListAdapter extends RecyclerView.Adapter<WorkPlanListAdapter.WorkPlanViewHolder>{

    private LayoutInflater layoutInflater;
    private List<WorkPlan> workPlanList;
    private WorkPlanViewModel viewModel;

    public WorkPlanListAdapter(WorkPlanViewModel viewModel)
    {
        workPlanList = new ArrayList<>();
        this.viewModel= viewModel;
    }


    @NonNull
    @Override
    public WorkPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        WorkPlanCardBinding binding = WorkPlanCardBinding.inflate(layoutInflater,parent,false);

        return new WorkPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkPlanViewHolder holder, int position) {

        holder.mBinding.setWorkPlan(workPlanList.get(position));
        holder.mBinding.setViewModel(viewModel);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return workPlanList.size();
    }

    public void setWorkPlanList(List<WorkPlan> list)
    {
        workPlanList.clear();

        if (list!=null)
        {
            workPlanList.addAll(list);
        }

        notifyDataSetChanged();

    }

    public void clearList()
    {
        workPlanList.clear();
        notifyDataSetChanged();
    }

    public static class WorkPlanViewHolder extends RecyclerView.ViewHolder
    {
        WorkPlanCardBinding mBinding;

        public WorkPlanViewHolder(@NonNull WorkPlanCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;
        }
    }
}
