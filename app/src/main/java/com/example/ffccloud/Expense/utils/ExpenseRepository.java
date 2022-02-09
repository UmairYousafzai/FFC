package com.example.ffccloud.Expense.utils;

import static com.example.ffccloud.utils.CONSTANTS.EMPLOYEE_EXPENSE_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.EmployeeExpense;
import com.example.ffccloud.utils.CONSTANTS;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {
    private final FFC_DAO mDao;
    private final LiveData<List<ExpenseModelClass>> allExpenses;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private NetworkCallListener callListener;

    public ExpenseRepository(Context context) {

        mDao= FfcDatabase.getInstance(context).dao();
        allExpenses = mDao.getAllExpenses();

    }

    public void setCallListener(NetworkCallListener listener)
    {
        callListener = listener;
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
  public boolean isExpenseExists( )
    {

                return mDao.isExpenseExists();

    }

    public LiveData<List<ExpenseModelClass>> getAllExpense( )
    {
        return allExpenses;
    }

    public void getEmployeesExpenses(String token,String month,int userID)
    {
        Call<List<EmployeeExpense>> call= ApiClient.getInstance().getApi().getEmployeeExpense(token,month,userID);

        call.enqueue(new Callback<List<EmployeeExpense>>() {
            @Override
            public void onResponse(@NonNull Call<List<EmployeeExpense>> call, @NonNull Response<List<EmployeeExpense>> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        callListener.onCallResponse(response.body(), EMPLOYEE_EXPENSE_RESPONSE);
                    }
                }
                else
                {
                    if (response.errorBody() != null) {
                        callListener.onCallResponse(response.errorBody().toString(),SERVER_ERROR_RESPONSE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EmployeeExpense>> call, @NonNull Throwable t) {
                callListener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);

            }
        });
    }
}
