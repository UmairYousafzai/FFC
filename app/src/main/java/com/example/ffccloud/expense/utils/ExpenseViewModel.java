package com.example.ffccloud.expense.utils;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.expense.adapter.EmployeeExpenseAdapter;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.EmployeeExpense;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;
    private final LiveData<List<ExpenseModelClass>> allExpense;
    private final MutableLiveData<EmployeeExpense> employeeExpenseMutableLiveData;
    private final MutableLiveData<List<EmployeeExpense>> employeeExpenseList;
    private final MutableLiveData<String> serverError;
    private final EmployeeExpenseAdapter adapter;
    private final HashMap<String, String> monthHashMap;
    private final List<String> monthList;
    private final ArrayAdapter<String> customSpinnerAdapter;


    public ExpenseViewModel(@NonNull Application application) {
        super(application);

        expenseRepository = new ExpenseRepository(application.getApplicationContext());
        allExpense = expenseRepository.getAllExpense();
        employeeExpenseMutableLiveData = new MutableLiveData<>();
        employeeExpenseList = new MutableLiveData<>();
        serverError= new MutableLiveData<>();
        adapter = new EmployeeExpenseAdapter(this);
        monthHashMap = new HashMap<>();
        monthList = new ArrayList<>();
        setUpMonthSpinner();
        customSpinnerAdapter= new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item,monthList);
    }

    public void insertExpense(ExpenseModelClass expenseModelClass) {

        expenseRepository.insertExpense(expenseModelClass);

    }

    public void deleteAllExpense() {

        expenseRepository.deleteAllExpense();

    }

    public void deleteExpense(ExpenseModelClass expenseModelClass) {

        expenseRepository.deleteExpense(expenseModelClass);

    }

    public void updateExpense(ExpenseModelClass expenseModelClass) {

        expenseRepository.updateExpense(expenseModelClass);

    }

    public boolean isExpenseExists() {

        return expenseRepository.isExpenseExists();

    }

    public LiveData<List<ExpenseModelClass>> getAllExpense() {
        return allExpense;
    }


    public void onClick(EmployeeExpense employeeExpense) {
        employeeExpenseMutableLiveData.setValue(employeeExpense);
    }

    public MutableLiveData<EmployeeExpense> getEmployeeExpenseOnClick() {
        return employeeExpenseMutableLiveData;
    }

    public void setEmployeeExpenseAtAdapter(List<EmployeeExpense> list) {
        adapter.setEmployeeExpenseList(list);
    }

    public MutableLiveData<List<EmployeeExpense>> getEmployeeExpenseList() {

        return employeeExpenseList;
    }

    public void fetchEmployeeExpenses(String month)
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();
        int userId = SharedPreferenceHelper.getInstance(getApplication()).getUserID();

        expenseRepository.getEmployeesExpenses(token, month, userId);

        expenseRepository.setCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (key == CONSTANTS.EMPLOYEE_EXPENSE_RESPONSE) {
                    employeeExpenseList.setValue((List<EmployeeExpense>) response);
                }
                else if (key==CONSTANTS.SERVER_ERROR_RESPONSE)
                {
                    String message= response.toString();
                    serverError.setValue(message);
                }
            }
        });
    }
    public MutableLiveData<String> getServerError()
    {
        return serverError;
    }

    public EmployeeExpenseAdapter getAdapter() {
        return adapter;
    }

    public String getMonth(String month)
    {
        return monthHashMap.get(month);
    }
    public ArrayAdapter<String> getCustomSpinnerAdapter() {



        return customSpinnerAdapter;


    }

    public void setUpMonthSpinner()
    {
        monthHashMap.clear();

        Calendar calendar = Calendar.getInstance();
        monthList.add("January");
        monthHashMap.put(monthList.get(0), calendar.get(Calendar.YEAR) + "01");

        monthList.add("February");
        monthHashMap.put(monthList.get(1), calendar.get(Calendar.YEAR) + "02");

        monthList.add("March");
        monthHashMap.put(monthList.get(2), calendar.get(Calendar.YEAR) + "03");

        monthList.add("April");
        monthHashMap.put(monthList.get(3), calendar.get(Calendar.YEAR) + "04");

        monthList.add("May");
        monthHashMap.put(monthList.get(4), calendar.get(Calendar.YEAR) + "05");

        monthList.add("June");
        monthHashMap.put(monthList.get(5), calendar.get(Calendar.YEAR) + "06");

        monthList.add("July");
        monthHashMap.put(monthList.get(6), calendar.get(Calendar.YEAR) + "07");

        monthList.add("August");
        monthHashMap.put(monthList.get(7), calendar.get(Calendar.YEAR) + "08");

        monthList.add("September");
        monthHashMap.put(monthList.get(8), calendar.get(Calendar.YEAR) + "09");

        monthList.add("October");
        monthHashMap.put(monthList.get(9), calendar.get(Calendar.YEAR) + "10");

        monthList.add("November");
        monthHashMap.put(monthList.get(10), calendar.get(Calendar.YEAR) + "11");

        monthList.add("December");
        monthHashMap.put(monthList.get(11), calendar.get(Calendar.YEAR) + "12");
    }
}
