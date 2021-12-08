package com.example.ffccloud.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.databinding.ContactPersonsCardBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private LayoutInflater layoutInflater;
    private List<ContactPersons> contactPersonsList;

    public ContactRecyclerAdapter() {

        contactPersonsList = new ArrayList<>();
    }

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

        ContactPersons contactPersons = new ContactPersons();
        contactPersons = contactPersonsList.get(position);

        holder.mBinding.setContactPerson(contactPersons);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return contactPersonsList ==null? 0: contactPersonsList.size();
    }

    public  void setContactPersonsList(List<ContactPersons> list)
    {
        if (list!=null)
        {
            contactPersonsList = list;
        }
        else
        {
            contactPersonsList.clear();
        }
        notifyDataSetChanged();
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder
    {
        ContactPersonsCardBinding mBinding;

        public ContactViewHolder(@NonNull ContactPersonsCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    contactPersonsList.remove(contactPersonsList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
