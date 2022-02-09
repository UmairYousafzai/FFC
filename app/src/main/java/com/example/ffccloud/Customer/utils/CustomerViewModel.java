package com.example.ffccloud.Customer.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.CustomerModel;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;
    private LiveData<List<CustomerModel>> allCustomer;

    public CustomerViewModel(@NonNull Application application) {
        super(application);

        customerRepository = new CustomerRepository(application.getApplicationContext());

        allCustomer= customerRepository.getAllCustomer();
    }

    public void deleteAllCustomers()
    {
        customerRepository.deleteCustomers();
    }

    public CustomerModel getCustomerById(int id)
    {
        return customerRepository.getCustomerById(id);
    }

    public LiveData<List<CustomerModel>> getAllCustomer()
    {
        return allCustomer;
    }
}
