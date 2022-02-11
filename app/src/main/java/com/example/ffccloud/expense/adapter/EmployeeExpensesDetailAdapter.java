package com.example.ffccloud.expense.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.EmployeeExpenseDetailCardBinding;
import com.example.ffccloud.expense.utils.EmployeeExpensesDetailViewModel;
import com.example.ffccloud.model.GetEmployeeExpensesDetail;

import java.util.ArrayList;
import java.util.List;

public class EmployeeExpensesDetailAdapter  extends RecyclerView.Adapter<EmployeeExpensesDetailAdapter.ExpensesDetailsViewHolder>{

    private List<GetEmployeeExpensesDetail> expensesDetailList;
    private LayoutInflater layoutInflater;
    private EmployeeExpensesDetailViewModel viewModel;
    private OnClick listener;

    public void setOnClickListener(OnClick listener)
    {
        this.listener =listener;
    }

    public EmployeeExpensesDetailAdapter(EmployeeExpensesDetailViewModel viewModel)
    {
        this.viewModel = viewModel;
        expensesDetailList= new ArrayList<>();
    }

    @NonNull
    @Override
    public ExpensesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        EmployeeExpenseDetailCardBinding binding = EmployeeExpenseDetailCardBinding.inflate(layoutInflater,parent,false);
        return new ExpensesDetailsViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesDetailsViewHolder holder, int position) {

        GetEmployeeExpensesDetail expensesDetail = expensesDetailList.get(position);
        if (expensesDetail.isApproved())
        {
            expensesDetail.setAction("Post");
            holder.mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#ff669900"));

        }
        else if (expensesDetail.isCancelled())
        {
            expensesDetail.setAction("Cancel");
            holder.mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#CF0015"));

        }else
        {
            expensesDetail.setAction("UnApproved");
            holder.mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#286A9C"));

        }
        holder.mBinding.setExpenseDetail(expensesDetail);
        holder.mBinding.setViewModel(viewModel);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return expensesDetailList.size();
    }

    public void setExpensesDetailList(List<GetEmployeeExpensesDetail> list) {
        expensesDetailList.clear();
        if (list != null)
        {
            expensesDetailList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public  class ExpensesDetailsViewHolder extends RecyclerView.ViewHolder
    {
         EmployeeExpenseDetailCardBinding mBinding;

        public ExpensesDetailsViewHolder(@NonNull EmployeeExpenseDetailCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;

            mBinding.actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0&&listener!=null)
                    {
                        if (position==1)
                        {
                           mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#ff669900"));

                        }
                        else
                        {
                          mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#CF0015"));

                        }
                        GetEmployeeExpensesDetail expensesDetail = expensesDetailList.get(getAdapterPosition());
                        if (mBinding.etAmount.getText()!=null && mBinding.etAmount.getText().toString().length()>0)
                        {
                            expensesDetail.setApprovedAmount(Double.parseDouble(mBinding.etAmount.getText().toString()));

                        }
                        listener.onItemSelected(expensesDetailList.get(getAdapterPosition()),position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }

    public interface OnClick
    {
         void onItemSelected(GetEmployeeExpensesDetail expensesDetail,int position);
    }
}
