package com.example.ffccloud.Expense.utils;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FfcDAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.ModelClasses.Expense;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private FfcDAO mDao;
    private LiveData<List<Expense>> allExpenses;
    private Executor executor = Executors.newSingleThreadExecutor();


    public ExpenseRepository(Context context) {

        mDao= FfcDatabase.getInstance(context).dao();
        allExpenses = mDao.getAllExpenses();

    }

    public void insertExpense(Expense expense)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertExpense(expense);
            }
        });
    }

    public void deleteExpense( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllExpense();
            }
        });
    }

    public LiveData<List<Expense>> getAllExpense( )
    {
        return allExpenses;
    }
}
