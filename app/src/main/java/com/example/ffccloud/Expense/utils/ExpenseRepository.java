package com.example.ffccloud.Expense.utils;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.ExpenseModelClass;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private FFC_DAO mDao;
    private LiveData<List<ExpenseModelClass>> allExpenses;
    private Executor executor = Executors.newSingleThreadExecutor();


    public ExpenseRepository(Context context) {

        mDao= FfcDatabase.getInstance(context).dao();
        allExpenses = mDao.getAllExpenses();

    }

    public void insertExpense(ExpenseModelClass expenseModelClass)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertExpense(expenseModelClass);
            }
        });
    }

    public void deleteAllExpense( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllExpense();
            }
        });
    }
    public void deleteExpense(ExpenseModelClass expenseModelClass)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteExpense(expenseModelClass);
            }
        });
    }

    public void updateExpense(ExpenseModelClass expenseModelClass)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.updateExpense(expenseModelClass);
            }
        });
    }

    public LiveData<List<ExpenseModelClass>> getAllExpense( )
    {
        return allExpenses;
    }
}
