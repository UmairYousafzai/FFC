package com.example.myapplication.Target.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClasses.AreasByEmpIdModel;
import com.example.myapplication.ModelClasses.DoctorsByAreaIdsModel;
import com.example.myapplication.databinding.CustomAddWorkplanDialogCardBinding;

import java.util.ArrayList;
import java.util.List;

public class AddWorkPlanDialogAdapter extends RecyclerView.Adapter<AddWorkPlanDialogAdapter.AddWorkPlanDialogViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<AreasByEmpIdModel> areaModelList,areaListFull;
    private int key=0;
    private List<DoctorsByAreaIdsModel> doctorList, doctorsListFull;
    DialogRecyclerItemListener mListener;

    public AddWorkPlanDialogAdapter(int key, DialogRecyclerItemListener listener) {
        this.key = key;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AddWorkPlanDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CustomAddWorkplanDialogCardBinding binding= CustomAddWorkplanDialogCardBinding.inflate(layoutInflater,parent,false);
        return new AddWorkPlanDialogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddWorkPlanDialogViewHolder holder, int position) {

        if (key==2)
        {
            AreasByEmpIdModel model = areaModelList.get(position);
            String text = model.getAreaTittle();
            holder.mBinding.docListCardNameTextview.setText(text);
            holder.mBinding.docListCardCheckbox.setChecked(model.isChecked());
        }
        else if (key==3) {

            holder.mBinding.docListCardNameTextview.setText(doctorList.get(position).getName());
            holder.mBinding.docListCardCheckbox.setChecked(doctorList.get(position).isIschecked());
        }

    }

    @Override
    public int getItemCount() {
        if (key==2)
        {
            return areaModelList==null?0:areaModelList.size();

        }
        else
        {
            return doctorList==null?0:doctorList.size();

        }
    }

    public void setAreaModelList(List<AreasByEmpIdModel> list)
    {
        areaModelList= list;
        areaListFull = new ArrayList<>(areaModelList);
        notifyDataSetChanged();
    }

    public void setDoctorModelList(List<DoctorsByAreaIdsModel> list)
    {
        doctorList= list;
        doctorsListFull = new ArrayList<>(doctorList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return dialogFilter;
    }


    private final Filter dialogFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if (key==2)
            {
                List<AreasByEmpIdModel> filterList= new ArrayList<>();

                if (constraint==null||constraint.length()==0)
                {
                    filterList= areaListFull;
                }
                else {
                    String filterPattern= constraint.toString().toLowerCase().trim();
                    for (AreasByEmpIdModel model:areaListFull)
                    {
                        if (model.getAreaTittle().toString().toLowerCase().trim().contains(filterPattern))
                        {
                            filterList.add(model);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= filterList;
                return filterResults;
            }
            else
            {
                List<DoctorsByAreaIdsModel> filterList= new ArrayList<>();

                if (constraint==null||constraint.length()==0)
                {
                    filterList= doctorsListFull;
                }
                else {
                    String filterPattern= constraint.toString().toLowerCase().trim();
                    for (DoctorsByAreaIdsModel model:doctorsListFull)
                    {
                        if (model.getName().toString().toLowerCase().trim().contains(filterPattern))
                        {
                            filterList.add(model);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= filterList;
                return filterResults;

            }

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (key == 2)
            {
                areaModelList.clear();
                areaModelList.addAll((List)results.values);
                notifyDataSetChanged();
            }
            else
            {
                doctorList.clear();
                doctorList.addAll((List) results.values);
                notifyDataSetChanged();
            }

        }
    };

    public   interface DialogRecyclerItemListener{

        void onItemListenerArea(AreasByEmpIdModel model ,boolean add);
        void onItemListenerTarget(DoctorsByAreaIdsModel model ,boolean add);

    }

    public  class AddWorkPlanDialogViewHolder extends RecyclerView.ViewHolder
    {
        CustomAddWorkplanDialogCardBinding mBinding;

        public AddWorkPlanDialogViewHolder(@NonNull CustomAddWorkplanDialogCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;

            mBinding.docListCardCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int position = getAdapterPosition();
                        if (mBinding.docListCardCheckbox.isChecked()) {

                            if (key == 2)
                            {
                                areaModelList.get(position).setChecked(true);
                                String id = String.valueOf(areaModelList.get(position).getAreaId());
                                mListener.onItemListenerArea(areaModelList.get(position),true);
                            }
                            else
                            {
                                doctorList.get(position).setIschecked(true);
                                String id = String.valueOf(doctorList.get(position).getId());
                                mListener.onItemListenerTarget(doctorList.get(position),true);
                            }

                        }
                        else
                        {
                            if (key == 2)
                            {
                                areaModelList.get(position).setChecked(false);
                                String id = String.valueOf(areaModelList.get(position).getAreaId());
                                mListener.onItemListenerArea(areaModelList.get(position),false);
                            }
                            else
                            {
                                doctorList.get(position).setIschecked(false);

                                String id = String.valueOf(doctorList.get(position).getId());
                                mListener.onItemListenerTarget(doctorList.get(position),false);
                            }
                        }
                    }
                }
            });
        }
    }
}
