package com.example.ffccloud.expense.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.databinding.ExpenseCardBinding;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseListViewHolder>{
    private final List<ExpenseModelClass> expenseModelClassList;
    private LayoutInflater layoutInflater;
    private SetOnClickListener listener;

    public ExpenseListAdapter() {

        expenseModelClassList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ExpenseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater= LayoutInflater.from(parent.getContext());
        }

        ExpenseCardBinding binding = ExpenseCardBinding.inflate(layoutInflater,parent,false);


        return new ExpenseListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListViewHolder holder, int position) {

        ExpenseModelClass expenseModelClass = new ExpenseModelClass();
        expenseModelClass = expenseModelClassList.get(position);

        holder.mBinding.setExpenseModelClass(expenseModelClass);
        holder.mBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return expenseModelClassList.size();
    }
    public void setOnClickListener(SetOnClickListener listener)
    {
        this.listener =listener;

    }

    public interface SetOnClickListener
    {
         void setOnClickListener(ExpenseModelClass expenseModelClass ,int key);
    }

    public void setExpenseModelClassList(List<ExpenseModelClass> list)
    {
        expenseModelClassList.clear();

        if (list!=null&&list.size()>0)
        {
            expenseModelClassList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public class ExpenseListViewHolder extends RecyclerView.ViewHolder
    {

        ExpenseCardBinding mBinding;

        public ExpenseListViewHolder(@NonNull ExpenseCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position= getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION)
                    {
                        expenseModelClassList.remove(expenseModelClassList.get(position));
                        listener.setOnClickListener(expenseModelClassList.get(position),1);
                    }
                }
            });

            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position= getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION)
                    {
                        listener.setOnClickListener(expenseModelClassList.get(position),2);
                    }
                }
            });
        }
    }
}
