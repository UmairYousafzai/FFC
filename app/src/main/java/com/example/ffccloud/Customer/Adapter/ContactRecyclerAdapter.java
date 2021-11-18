package com.example.ffccloud.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.databinding.ContactPersonsCardBinding;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private LayoutInflater layoutInflater;
    private List<ContactPersonsModel> contactPersonsModelList;



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ContactPersonsCardBinding binding = ContactPersonsCardBinding.inflate(layoutInflater,parent,false);

        return new ContactViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ContactPersonsModel contactPersonsModel = new ContactPersonsModel();
        contactPersonsModel = contactPersonsModelList.get(position);

        holder.mBinding.setContactPerson(contactPersonsModel);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return contactPersonsModelList==null? 0:contactPersonsModelList.size();
    }

    public  void setContactPersonsModelList(List<ContactPersonsModel> list)
    {
        if (list!=null)
        {
            contactPersonsModelList = list;
        }
        else
        {
            contactPersonsModelList.clear();
        }
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        ContactPersonsCardBinding mBinding;

        public ContactViewHolder(@NonNull ContactPersonsCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}
