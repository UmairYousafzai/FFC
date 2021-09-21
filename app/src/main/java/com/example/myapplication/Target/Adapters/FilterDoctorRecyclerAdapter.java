package com.example.myapplication.Target.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.Target.DoctorListFragmentDirections;
import com.example.myapplication.databinding.FilterDoctorProfileCardBinding;

import java.util.ArrayList;
import java.util.List;

public class FilterDoctorRecyclerAdapter extends RecyclerView.Adapter<FilterDoctorRecyclerAdapter.FilterDocViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<FilteredDoctoredModel> filteredDoctoredModelList,filteredDoctoredModelsListFull;
    private Fragment fragment;

    public FilterDoctorRecyclerAdapter(Fragment fragment) {
        filteredDoctoredModelList= new ArrayList<>();
        filteredDoctoredModelsListFull= new ArrayList<>();
        this.fragment= fragment;
    }

    @NonNull
    @Override
    public FilterDocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FilterDoctorProfileCardBinding binding = FilterDoctorProfileCardBinding.inflate(layoutInflater, parent, false);

        return new FilterDocViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterDocViewHolder holder, int position) {

        FilteredDoctoredModel model= filteredDoctoredModelList.get(position);
        holder.mBinding.setDoctor(model);
        holder.mBinding.executePendingBindings();
        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorListFragmentDirections.ActionDoctorListFragmentToFilteredDocFullInfoFragment action = DoctorListFragmentDirections.actionDoctorListFragmentToFilteredDocFullInfoFragment();
                action.setDoctorId(model.getDoctorId());
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(action);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredDoctoredModelList == null ? 0 : filteredDoctoredModelList.size();
    }

    public void setFilteredDoctoredModelList(List<FilteredDoctoredModel> list)
    {
        filteredDoctoredModelList.clear();
        filteredDoctoredModelList = list;
        filteredDoctoredModelsListFull= new ArrayList<>(filteredDoctoredModelList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return docListFilter;
    }

    private Filter docListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<FilteredDoctoredModel> filterList = new ArrayList<>();

            if (constraint==null||constraint.length()==0)
            {
                filterList= filteredDoctoredModelsListFull;
            }
            else
            {
                String filterPattern= constraint.toString().trim().toLowerCase();
                for (FilteredDoctoredModel model : filteredDoctoredModelsListFull) {
                    if (model.getName().toLowerCase().trim().contains(filterPattern))
                    {
                        filterList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values=filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredDoctoredModelList.clear();
            filteredDoctoredModelList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class FilterDocViewHolder extends RecyclerView.ViewHolder
    {
        FilterDoctorProfileCardBinding mBinding;
        public FilterDocViewHolder(@NonNull FilterDoctorProfileCardBinding binding) {
            super(binding.getRoot());
            mBinding= binding;
        }
    }
}
