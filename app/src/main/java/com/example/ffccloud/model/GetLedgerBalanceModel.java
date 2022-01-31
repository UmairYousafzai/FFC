package com.example.ffccloud.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLedgerBalanceModel {


    @SerializedName("Company_Id")
    @Expose
    private int companyId;
    @SerializedName("Country_Id")
    @Expose
    private int countryId;
    @SerializedName("Location_Id")
    @Expose
    private int locationId;
    @SerializedName("Project_Id")
    @Expose
    private int projectId;

    @Expose
    private int supplierId;
    @SerializedName("Credit_Limit")
    @Expose
    private int creditLimit;


    @SerializedName("LedgerBalance")
    @Expose
    private int ledgerBalance;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(int ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }
}