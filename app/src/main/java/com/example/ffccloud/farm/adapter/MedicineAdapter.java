package com.example.ffccloud.farm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.Medicine_modal;
import com.example.ffccloud.databinding.ContactPersonsCardBinding;
import com.example.ffccloud.databinding.MedicineCardBinding;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {


    private LayoutInflater layoutInflater;
    private List<Medicine_modal> medicineModalList;

    public MedicineAdapter() {

        medicineModalList= new ArrayList<>();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        MedicineCardBinding binding = MedicineCardBinding.inflate(layoutInflater,parent,false);

        return new MedicineViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {

        Medicine_modal medicineModal = new Medicine_modal();
        medicineModal = medicineModalList.get(position);

        holder.mBinding.setMedicine(medicineModal);
        holder.mBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return medicineModalList==null? 0:medicineModalList.size();
    }

    public  void setMedicineModalList(List<Medicine_modal> list)
    {
        if (list!=null)
        {
            medicineModalList = list;
        }
        else
        {
            medicineModalList.clear();
        }
        notifyDataSetChanged();
    }

    public  class MedicineViewHolder extends RecyclerView.ViewHolder
    {
        MedicineCardBinding mBinding;

        public MedicineViewHolder(@NonNull MedicineCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    medicineModalList.remove(medicineModalList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
