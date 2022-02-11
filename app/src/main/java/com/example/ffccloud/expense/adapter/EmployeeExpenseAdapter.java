package com.example.ffccloud.expense.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.databinding.EmployeesExpenseCardBinding;
import com.example.ffccloud.expense.utils.ExpenseViewModel;
import com.example.ffccloud.model.EmployeeExpense;

import java.util.ArrayList;
import java.util.List;

public class EmployeeExpenseAdapter extends RecyclerView.Adapter<EmployeeExpenseAdapter.EmployeeExpenseViewHolder> {

    private LayoutInflater layoutInflater;
    private List<EmployeeExpense> employeeExpenseList;
    private ExpenseViewModel expenseViewModel;

    public EmployeeExpenseAdapter(ExpenseViewModel expenseViewModel)
    {
        employeeExpenseList = new ArrayList<>();
        this.expenseViewModel= expenseViewModel;
    }


    @NonNull
    @Override
    public EmployeeExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        EmployeesExpenseCardBinding binding = EmployeesExpenseCardBinding.inflate(layoutInflater,parent,false);

        return new EmployeeExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeExpenseViewHolder holder, int position) {

        holder.mBinding.setExpense(employeeExpenseList.get(position));
        holder.mBinding.setEmployeeExpenseViewModel(expenseViewModel);
    }

    @Override
    public int getItemCount() {
        return employeeExpenseList.size();
    }

    public void setEmployeeExpenseList(List<EmployeeExpense> list)
    {
        employeeExpenseList.clear();

        if (list!=null)
        {
            employeeExpenseList.addAll(list);
        }

        notifyDataSetChanged();

    }

    public void clearList()
    {
        employeeExpenseList.clear();
        notifyDataSetChanged();
    }

    public static class EmployeeExpenseViewHolder extends RecyclerView.ViewHolder
    {
        EmployeesExpenseCardBinding mBinding;

        public EmployeeExpenseViewHolder(@NonNull EmployeesExpenseCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;
        }
    }
}
