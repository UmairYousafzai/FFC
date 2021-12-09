package com.example.ffccloud.adapter;

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

import com.example.ffccloud.GetSupplierModel;
import com.example.ffccloud.databinding.SupplierCardBinding;
import com.example.ffccloud.doctor.SupplierDoctorListFragmentDirections;
import com.example.ffccloud.farm.FarmListFragmentDirections;
import com.example.ffccloud.hospital.HospitalListFragmentDirections;
import com.example.ffccloud.medicalStore.MedicalStoreListFragmentDirections;
import com.example.ffccloud.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SupplierRecyclerViewAdapter extends RecyclerView.Adapter<SupplierRecyclerViewAdapter.SupplierViewHolder> implements Filterable {


    private LayoutInflater layoutInflater;
    private List<GetSupplierModel> getSupplierModelList;
    private final List<GetSupplierModel> getSupplierModelListFull;
    private Fragment fragment;
    private int key=0;
    private int destinationID=0;
    private String calledSupplier ="";

    public SupplierRecyclerViewAdapter(Fragment fragment) {

        getSupplierModelList = new ArrayList<>();
        getSupplierModelListFull = new ArrayList<>();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        SupplierCardBinding binding = SupplierCardBinding.inflate(layoutInflater,parent,false);

        return new SupplierViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        if (key==1)
        {
            holder.mBinding.btnEdit.setVisibility(View.GONE);
            holder.mBinding.btnAdd.setVisibility(View.VISIBLE);

        }
        else if (key==2)
        {
            holder.mBinding.btnEdit.setVisibility(View.GONE);
            holder.mBinding.btnAdd.setVisibility(View.GONE);
            holder.mBinding.btnClose.setVisibility(View.VISIBLE);
        }

        GetSupplierModel supplierModel = new GetSupplierModel();
        supplierModel = getSupplierModelList.get(position);

        holder.mBinding.setSupplier(supplierModel);
        holder.mBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return getSupplierModelList ==null? 0: getSupplierModelList.size();
    }
    public void setKey(int key)
    {
        this.key = key;
    }

    public void setDestinationID(int id)
    {
        this.destinationID = id;
    }
    public  void setGetSupplierModelList(List<GetSupplierModel> list)
    {
        if (list!=null)
        {
            getSupplierModelList = list;
            getSupplierModelListFull.addAll(getSupplierModelList);
        }
        else
        {
            getSupplierModelList.clear();
        }
        notifyDataSetChanged();
    }

    public void setCalledSupplier(String calledSupplier)
    {
        this.calledSupplier = calledSupplier;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            List<GetSupplierModel> filterList= new ArrayList<>();

            if (constraint==null||constraint.length()==0)
            {
                filterList= getSupplierModelListFull;
            }
            else {
                String filterPattern= constraint.toString().toLowerCase().trim();
                for (GetSupplierModel model:getSupplierModelListFull)
                {
                    if (model.getSupplier_Name().toLowerCase().trim().contains(filterPattern))
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


            getSupplierModelList.clear();
            getSupplierModelList.addAll((List)results.values);
            notifyDataSetChanged();



        }
    };

    public  class SupplierViewHolder extends RecyclerView.ViewHolder
    {
        SupplierCardBinding mBinding;

        public SupplierViewHolder(@NonNull SupplierCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    int supplierID= getSupplierModelList.get(getAdapterPosition()).getSupplier_Id();

                    if (destinationID==1)
                    {

                        FarmListFragmentDirections.ActionFarmListFragmentToAddFarmFormFragment action = FarmListFragmentDirections.actionFarmListFragmentToAddFarmFormFragment();
                        action.setSupplierId(supplierID);
                        navController.navigate(action);
                    }else if (destinationID==2)
                    {
                        SupplierDoctorListFragmentDirections.ActionSupplierDoctorFragmentToAddDoctorFragment action = SupplierDoctorListFragmentDirections.actionSupplierDoctorFragmentToAddDoctorFragment();
                        action.setSupplierId(supplierID);
                        navController.navigate(action);
                    }
                    else if (destinationID==3)
                    {
                        HospitalListFragmentDirections.ActionHospitalListFragmentToAddHospitalFragment action = HospitalListFragmentDirections.actionHospitalListFragmentToAddHospitalFragment();
                        action.setSupplierId(supplierID);
                        navController.navigate(action);
                    }   else if (destinationID==4)
                    {
                        MedicalStoreListFragmentDirections.ActionMedicalStoreListFragmentToAddMedicalStoreFragment action =MedicalStoreListFragmentDirections.actionMedicalStoreListFragmentToAddMedicalStoreFragment();
                        action.setSupplierId(supplierID);
                        navController.navigate(action);
                    }

                }
            });
            mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(fragment);

                    GetSupplierModel getSupplierModel= getSupplierModelList.get(getAdapterPosition());
                    if (calledSupplier.equals("Doctor"))
                    {
                        Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.DOCTOR_SUPPLIER_KEY,getSupplierModel);
                        navController.popBackStack();
                    }
                    else if (calledSupplier.equals("Farm"))
                    {
                        Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.FARM_SUPPLIER_KEY,getSupplierModel);
                        navController.popBackStack();
                    }





                }
            });
            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupplierModelList.remove(getSupplierModelList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });

        }
    }
}
