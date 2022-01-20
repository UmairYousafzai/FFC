package com.example.ffccloud.Expense.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.ModelClasses.Expense;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;
    private final LiveData<List<Expense>>  allExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);

        expenseRepository= new ExpenseRepository(application.getApplicationContext());
        allExpense= expenseRepository.getAllExpense();
    }

    public void insertExpense(Expense expense)
    {

                expenseRepository.insertExpense(expense);

    }

    public void deleteExpense( )
    {

        expenseRepository.deleteExpense();

    }

    public LiveData<List<Expense>> getAllExpense( )
    {
        return allExpense;
    }
}
