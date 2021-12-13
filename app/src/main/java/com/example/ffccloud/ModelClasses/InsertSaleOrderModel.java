package com.example.ffccloud.ModelClasses;

import com.example.ffccloud.InsertProductModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsertSaleOrderModel {

    @SerializedName("lstSalesOrderDetail")
    private List<InsertProductModel> productModelList;

    @SerializedName("lsttermsandcond")
    private List<TermAndConditionModel> termAndConditionModelList;

    private int Company_Id;
    private int Country_Id;
    private int Location_Id;
    private int Project_Id;
    private int Sale_Order_Id;

    private String Sale_Order_Date;
    private String Delivery_Date;
    private String Delivery_Location;

    private int Ledger_Balance;
    private int Credit_Limit;
    private int Supplier_Id;
    private int Priority_Id;
    private int Delivery_Mode_Id;

    public InsertSaleOrderModel() {
    }

    public InsertSaleOrderModel(List<InsertProductModel> productModelList, List<TermAndConditionModel> termAndConditionModelList, int company_Id, int country_Id, int location_Id, int project_Id, int sale_Order_Id, String sale_Order_Date, String delivery_Date, String delivery_Location, int ledger_Balance, int credit_Limit, int supplier_Id, int priority_Id, int delivery_Mode_Id) {
        this.productModelList = productModelList;
        this.termAndConditionModelList = termAndConditionModelList;
        Company_Id = company_Id;
        Country_Id = country_Id;
        Location_Id = location_Id;
        Project_Id = project_Id;
        Sale_Order_Id = sale_Order_Id;
        Sale_Order_Date = sale_Order_Date;
        Delivery_Date = delivery_Date;
        Delivery_Location = delivery_Location;
        Ledger_Balance = ledger_Balance;
        Credit_Limit = credit_Limit;
        Supplier_Id = supplier_Id;
        Priority_Id = priority_Id;
        Delivery_Mode_Id = delivery_Mode_Id;
    }

    public List<InsertProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<InsertProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    public List<TermAndConditionModel> getTermAndConditionModelList() {
        return termAndConditionModelList;
    }

    public void setTermAndConditionModelList(List<TermAndConditionModel> termAndConditionModelList) {
        this.termAndConditionModelList = termAndConditionModelList;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(int country_Id) {
        Country_Id = country_Id;
    }

    public int getLocation_Id() {
        return Location_Id;
    }

    public void setLocation_Id(int location_Id) {
        Location_Id = location_Id;
    }

    public int getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(int project_Id) {
        Project_Id = project_Id;
    }

    public int getSale_Order_Id() {
        return Sale_Order_Id;
    }

    public void setSale_Order_Id(int sale_Order_Id) {
        Sale_Order_Id = sale_Order_Id;
    }

    public String getSale_Order_Date() {
        return Sale_Order_Date;
    }

    public void setSale_Order_Date(String sale_Order_Date) {
        Sale_Order_Date = sale_Order_Date;
    }

    public String getDelivery_Date() {
        return Delivery_Date;
    }

    public void setDelivery_Date(String delivery_Date) {
        Delivery_Date = delivery_Date;
    }

    public String getDelivery_Location() {
        return Delivery_Location;
    }

    public void setDelivery_Location(String delivery_Location) {
        Delivery_Location = delivery_Location;
    }

    public int getLedger_Balance() {
        return Ledger_Balance;
    }

    public void setLedger_Balance(int ledger_Balance) {
        Ledger_Balance = ledger_Balance;
    }

    public int getCredit_Limit() {
        return Credit_Limit;
    }

    public void setCredit_Limit(int credit_Limit) {
        Credit_Limit = credit_Limit;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int supplier_Id) {
        Supplier_Id = supplier_Id;
    }

    public int getPriority_Id() {
        return Priority_Id;
    }

    public void setPriority_Id(int priority_Id) {
        Priority_Id = priority_Id;
    }

    public int getDelivery_Mode_Id() {
        return Delivery_Mode_Id;
    }

    public void setDelivery_Mode_Id(int delivery_Mode_Id) {
        Delivery_Mode_Id = delivery_Mode_Id;
    }
}
