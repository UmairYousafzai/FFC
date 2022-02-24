package com.example.ffccloud.meeting.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.Target.Adapters.AddWorkPlanDialogAdapter;
import com.example.ffccloud.databinding.EmployeeCardBinding;
import com.example.ffccloud.model.AreasByEmpIdModel;
import com.example.ffccloud.model.DoctorsByAreaIdsModel;
import com.example.ffccloud.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;

    private List<Employee> employeeList, employeeListFull;
    private EmployeeRecyclerItemListener mListener;

    public EmployeeListAdapter() {

        employeeList = new ArrayList<>();
        employeeListFull = new ArrayList<>();

    }



    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        EmployeeCardBinding binding= EmployeeCardBinding.inflate(layoutInflater,parent,false);
        return new EmployeeListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {




            holder.mBinding.employeeNameTextview.setText(employeeList.get(position).getUserName());
            holder.mBinding.employeeCardCheckbox.setChecked(employeeList.get(position).isChecked());


    }

    @Override
    public int getItemCount() {


            return employeeList ==null?0: employeeList.size();


    }



    public void setEmployeeList(List<Employee> list)
    {
        if (list!=null&& list.size()>0)
        {
            employeeList.clear();
            employeeList.addAll(list);
            employeeListFull = new ArrayList<>(employeeList);
        }

        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return dialogFilter;
    }


    private final Filter dialogFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {




                List<Employee> filterList= new ArrayList<>();

                if (constraint==null||constraint.length()==0)
                {
                    filterList= employeeListFull;
                }
                else {
                    String filterPattern= constraint.toString().toLowerCase().trim();
                    for (Employee model: employeeListFull)
                    {
                        if (model.getUserName().toLowerCase().trim().contains(filterPattern))
                        {
                            filterList.add(model);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= filterList;
                return filterResults;



        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {



                employeeList.clear();
                if (results.values!=null)
                {
                    employeeList.addAll((List) results.values);
                }

                notifyDataSetChanged();


        }
    };

    public void removeEmployee(Employee employee)
    {
        employeeList.remove(employee);
    }

    public void setmListener(EmployeeRecyclerItemListener mListener) {
        this.mListener = mListener;
    }

    public   interface EmployeeRecyclerItemListener{

        void onItemListener(Employee model ,boolean add);

    }

    public  class EmployeeListViewHolder extends RecyclerView.ViewHolder
    {
        EmployeeCardBinding mBinding;

        public EmployeeListViewHolder(@NonNull EmployeeCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;

            mBinding.employeeCardCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int position = getAdapterPosition();
                        if (mBinding.employeeCardCheckbox.isChecked()) {

                                employeeList.get(position).setChecked(true);
                                String id = String.valueOf(employeeList.get(position).getId());
                                mListener.onItemListener(employeeList.get(position),true);

                        }
                        else
                        {

                                employeeList.get(position).setChecked(false);

                                String id = String.valueOf(employeeList.get(position).getId());
                                mListener.onItemListener(employeeList.get(position),false);

                        }
                    }
                }
            });
        }
    }
}
