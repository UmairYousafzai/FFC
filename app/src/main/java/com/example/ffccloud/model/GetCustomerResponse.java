package com.example.ffccloud.model;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.CustomerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetCustomerResponse {
    @SerializedName("Table")
    @Expose
    private List<CustomerModel> customerModelList ;
    @SerializedName("Table1")
    @Expose
    private List<ContactPersons> contactPersonsList ;

    public GetCustomerResponse() {
        contactPersonsList= new ArrayList<>();
        customerModelList= new ArrayList<>();
    }

    public List<CustomerModel> getCustomerModelList() {
        return customerModelList;
    }

    public void setCustomerModelList(List<CustomerModel> customerModelList) {
        this.customerModelList = customerModelList;
    }

    public List<ContactPersons> getContactPersonsList() {
        return contactPersonsList;
    }

    public void setContactPersonsList(List<ContactPersons> contactPersonsList) {
        this.contactPersonsList = contactPersonsList;
    }
}
