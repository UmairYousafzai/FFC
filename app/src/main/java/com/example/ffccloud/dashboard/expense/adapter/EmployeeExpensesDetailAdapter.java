package com.example.ffccloud.dashboard.expense.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.databinding.EmployeeExpenseDetailCardBinding;
import com.example.ffccloud.dashboard.expense.viewmodel.EmployeeExpensesDetailViewModel;
import com.example.ffccloud.model.GetEmployeeExpensesDetail;

import java.util.ArrayList;
import java.util.List;

public class EmployeeExpensesDetailAdapter  extends RecyclerView.Adapter<EmployeeExpensesDetailAdapter.ExpensesDetailsViewHolder>{

    private List<GetEmployeeExpensesDetail> expensesDetailList;
    private LayoutInflater layoutInflater;
    private final EmployeeExpensesDetailViewModel viewModel;
    private OnClick listener;
    private boolean check = false;
    private  int number;

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

         if (expensesDetail.isCancelled())
        {
            expensesDetail.setAction("2");
            holder.mBinding.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CF0015")));

//            holder.mBinding.btnAction.setBackgroundColor(Color.parseColor("#CF0015"));
            holder.mBinding.btnAction.setText("Cancel");

        }
        else if (expensesDetail.isApproved())
        {
            expensesDetail.setAction("1");
            holder.mBinding.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff669900")));
            holder.mBinding.btnAction.setText("Approved");

        }else
        {
            expensesDetail.setAction("0");
            holder.mBinding.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#286A9C")));
            holder.mBinding.btnAction.setText("UnApproved");

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
            number= expensesDetailList.size();
        }
        notifyDataSetChanged();
    }

    public  class ExpensesDetailsViewHolder extends RecyclerView.ViewHolder
    {
         EmployeeExpenseDetailCardBinding mBinding;

        public ExpensesDetailsViewHolder(@NonNull EmployeeExpenseDetailCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;
//            if (check)
//            {
//                mBinding.actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (position!=0&&listener!=null)
//                        {
//                            if (position==1)
//                            {
//                                mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#ff669900"));
//
//                            }
//                            else
//                            {
//                                mBinding.actionSpinner.setBackgroundColor(Color.parseColor("#CF0015"));
//
//                            }
//
//                            GetEmployeeExpensesDetail expensesDetail = expensesDetailList.get(getAdapterPosition());
//                            listener.onItemSelected(expensesDetailList.get(getAdapterPosition()),position);
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//            else
//            {
//                check= true;
//            }




        }
    }

    public interface OnClick
    {
         void onItemSelected(GetEmployeeExpensesDetail expensesDetail,int position);
    }
}
