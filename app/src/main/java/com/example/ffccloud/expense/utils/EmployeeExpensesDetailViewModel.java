package com.example.ffccloud.expense.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.expense.adapter.EmployeeExpensesDetailAdapter;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.GetEmployeeExpensesDetail;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.List;

public class EmployeeExpensesDetailViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;
    private final MutableLiveData<GetEmployeeExpensesDetail> expensesDetailMutableLiveData;
    private final MutableLiveData<List<GetEmployeeExpensesDetail>> expensesDetailListMutableLiveData;
    private final MutableLiveData<String> serverError;
    private final EmployeeExpensesDetailAdapter adapter;



    public EmployeeExpensesDetailViewModel(@NonNull Application application) {
        super(application);

        adapter = new EmployeeExpensesDetailAdapter(this);
        expenseRepository= new ExpenseRepository(application);
        expensesDetailMutableLiveData= new MutableLiveData<>();
        expensesDetailListMutableLiveData = new MutableLiveData<>();
        serverError= new MutableLiveData<>();
    }


    public void onClick( )
    {

        adapter.setOnClickListener(new EmployeeExpensesDetailAdapter.OnClick() {
            @Override
            public void onItemSelected(GetEmployeeExpensesDetail expensesDetail ,int position) {
                if (position==1)
                {
                    expensesDetail.setAction(String.valueOf(position));
                }
                else if (position==2)
                {
                    expensesDetail.setAction(String.valueOf(position));

                }
                else
                {
                    expensesDetail.setAction("UnApproved");

                }
                expensesDetailMutableLiveData.setValue(expensesDetail);
            }
        });

    }

    public void setExpenseDetailListInAdapter(List<GetEmployeeExpensesDetail> list)
    {
        adapter.setExpensesDetailList(list);

    }

    public MutableLiveData<List<GetEmployeeExpensesDetail>> getExpensesDetailListMutableLiveData() {
        return expensesDetailListMutableLiveData;
    }

    public MutableLiveData<GetEmployeeExpensesDetail> getExpensesDetailMutableLiveData() {
        return expensesDetailMutableLiveData;
    }

    public EmployeeExpensesDetailAdapter getAdapter() {
         return adapter;
    }

    public MutableLiveData<String> getServerError() {
        return serverError;
    }

    public void fetchEmployeeExpenses(String month,int userId)
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();
        expenseRepository.getEmployeesExpensesDetails(token, month, userId);

        expenseRepository.setCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (key == CONSTANTS.EMPLOYEE_EXPENSE_DETAIL_RESPONSE) {
                   adapter.setExpensesDetailList((List<GetEmployeeExpensesDetail>) response);
                }
                else if (key==CONSTANTS.SERVER_ERROR_RESPONSE)
                {
                    String message= response.toString();
                    serverError.setValue(message);
                }
            }
        });
    }

    public void updateExpenseStatus(GetEmployeeExpensesDetail expensesDetail,int userID)
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();
        expenseRepository.updateExpense(expensesDetail,userID,token);
    }
}

