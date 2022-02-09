package com.example.ffccloud.Customer.utils;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CustomerRepository {

    private FFC_DAO mDao;
    private LiveData<List<CustomerModel>> allCustomer;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CustomerRepository(Context context) {

        mDao = FfcDatabase.getInstance(context).dao();
        allCustomer= mDao.getAllCustomer();
    }


    public void insertCustomers(List<CustomerModel> customerModels)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertAllCustomer(customerModels);
            }
        });
    }

    public void deleteCustomers()
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllCustomer( );
            }
        });
    }

    public CustomerModel getCustomerById(int id)
    {
        return mDao.getCustomerById(id);
    }
    public LiveData<List<CustomerModel>> getAllCustomer()
    {
        return allCustomer;
    }

}
