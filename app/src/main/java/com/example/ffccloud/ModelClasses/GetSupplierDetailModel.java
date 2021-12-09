package com.example.ffccloud.ModelClasses;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.SupplierCompDetail;
import com.example.ffccloud.SupplierItemLinking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSupplierDetailModel {

    @SerializedName("Table")
    @Expose
    private List<SupplierModel> supplierModelNewList;

    @SerializedName("Table1")
    @Expose
    private List<ContactPersons> contactPersonsList;

    @SerializedName("Table2")
    @Expose
    private List<SupplierCompDetail> supplierCompDetailList;

    @SerializedName("Table3")
    @Expose
    private List<SupplierItemLinking> supplierItemLinkingList;

    @SerializedName("Table4")
    @Expose
    private List<SupplierLinking> supplierLinkingList;

    public List<SupplierModel> getSupplierModelNewList() {
        return supplierModelNewList;
    }

    public void setSupplierModelNewList(List<SupplierModel> supplierModelNewList) {
        this.supplierModelNewList = supplierModelNewList;
    }

    public List<ContactPersons> getContactPersonsList() {
        return contactPersonsList;
    }

    public void setContactPersonsList(List<ContactPersons> contactPersonsList) {
        this.contactPersonsList = contactPersonsList;
    }

    public List<SupplierCompDetail> getSupplierCompDetailList() {
        return supplierCompDetailList;
    }

    public void setSupplierCompDetailList(List<SupplierCompDetail> supplierCompDetailList) {
        this.supplierCompDetailList = supplierCompDetailList;
    }

    public List<SupplierItemLinking> getSupplierItemLinkingList() {
        return supplierItemLinkingList;
    }

    public void setSupplierItemLinkingList(List<SupplierItemLinking> supplierItemLinkingList) {
        this.supplierItemLinkingList = supplierItemLinkingList;
    }

    public List<SupplierLinking> getSupplierLinkingList() {
        return supplierLinkingList;
    }

    public void setSupplierLinkingList(List<SupplierLinking> supplierLinkingList) {
        this.supplierLinkingList = supplierLinkingList;
    }
}
