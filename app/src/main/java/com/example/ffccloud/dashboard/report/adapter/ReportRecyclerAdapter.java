package com.example.ffccloud.dashboard.report.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.dashboard.report.viewmodel.ReportListViewModel;
import com.example.ffccloud.databinding.ReportCardBinding;
import com.example.ffccloud.model.LookUpWorkPlan;

import java.util.ArrayList;
import java.util.List;

public class ReportRecyclerAdapter extends RecyclerView.Adapter<ReportRecyclerAdapter.ReportViewHolder> {



    private final List<LookUpWorkPlan> reportList;
    private LayoutInflater layoutInflater;
    private ReportListViewModel viewModel;


    public ReportRecyclerAdapter(ReportListViewModel viewModel) {

        reportList = new ArrayList<>();
        this.viewModel= viewModel;

    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ReportCardBinding mbinding = ReportCardBinding.inflate(layoutInflater, parent, false);
        return new ReportViewHolder(mbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        LookUpWorkPlan lookUpWorkPlan = reportList.get(position);


        if (lookUpWorkPlan.getStatus().equals("Pending"))
        {
            holder.mbinding.tvStatus.setBackgroundColor(Color.parseColor("#689EB8"));

        }
        else if (lookUpWorkPlan.getStatus().equals("Cancel"))
        {
            holder.mbinding.tvStatus.setBackgroundColor(Color.RED);

        }
        else
        {
            holder.mbinding.tvStatus.setBackgroundColor(Color.GREEN);

        }
        holder.mbinding.setReport(lookUpWorkPlan);
        holder.mbinding.setViewModel(viewModel);
        holder.mbinding.executePendingBindings();

    }


    @Override
    public int getItemCount() {


        return reportList.size();

    }



    public void setReportList(List<LookUpWorkPlan> list)
    {

        if (list!=null&&list.size()>0)
        {
            reportList.clear();
            reportList.addAll(list);
        }else
        {
            reportList.clear();
        }


        notifyDataSetChanged();
    }




    public class ReportViewHolder extends RecyclerView.ViewHolder
    {

        private final ReportCardBinding mbinding;

        public ReportViewHolder(@NonNull ReportCardBinding binding) {
            super(binding.getRoot());

            mbinding= binding;







        }
    }
}
