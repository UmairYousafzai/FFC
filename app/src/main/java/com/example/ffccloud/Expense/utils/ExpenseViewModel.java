package com.example.ffccloud.Expense.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.ExpenseModelClass;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;
    private final LiveData<List<ExpenseModelClass>>  allExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);

        expenseRepository= new ExpenseRepository(application.getApplicationContext());
        allExpense= expenseRepository.getAllExpense();
    }

    public void insertExpense(ExpenseModelClass expenseModelClass)
    {

                expenseRepository.insertExpense(expenseModelClass);

    }

    public void deleteAllExpense( )
    {

        expenseRepository.deleteAllExpense();

    }
    public void deleteExpense(ExpenseModelClass expenseModelClass)
    {

                expenseRepository.deleteExpense(expenseModelClass);

    }

    public void updateExpense(ExpenseModelClass expenseModelClass)
    {

                expenseRepository.updateExpense(expenseModelClass);

    }
    public boolean isExpenseExists( )
    {

        return expenseRepository.isExpenseExists();

    }
    public LiveData<List<ExpenseModelClass>> getAllExpense( )
    {
        return allExpense;
    }
}
