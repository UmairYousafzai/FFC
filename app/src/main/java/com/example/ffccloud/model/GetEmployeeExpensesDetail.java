package com.example.ffccloud.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.ffccloud.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEmployeeExpensesDetail extends BaseObservable {

    private String Company_Id;
    private String Country_Id;
    private String Location_Id;
    private String Project_Id;
    private int Session_Id;
    @SerializedName("Emp_Expenses_Id")
    @Expose
    private double empExpensesId;
    @SerializedName("Requested_Amount")
    @Expose
    private double requestedAmount;
    @SerializedName("Approved_Amount")
    @Expose
    private double approvedAmount;
    @SerializedName("Expense_Remarks")
    @Expose
    private String expenseRemarks;
    @SerializedName("Approved")
    @Expose
    private boolean approved;
    @SerializedName("Cancelled")
    @Expose
    private boolean cancelled;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("Exp_Title")
    @Expose
    private String expTitle;
    @SerializedName("Status_Remarks")
    @Expose
    @Bindable
    private String statusRemarks;
    @Bindable
    private String approvedAmountString;

    @Bindable
    private String Action;


    public String getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public String getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(String country_Id) {
        Country_Id = country_Id;
    }

    public String getLocation_Id() {
        return Location_Id;
    }

    public void setLocation_Id(String location_Id) {
        Location_Id = location_Id;
    }

    public String getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(String project_Id) {
        Project_Id = project_Id;
    }

    public int getSession_Id() {
        return Session_Id;
    }

    public void setSession_Id(int session_Id) {
        Session_Id = session_Id;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {

            Action = action;
            notifyChange();



    }

    public GetEmployeeExpensesDetail()
    {
        approvedAmountString =String.valueOf(approvedAmount);
    }

    public String getApprovedAmountString() {
        return approvedAmountString;
    }

    public void setApprovedAmountString(String approvedAmountString) {
        if (!approvedAmountString.equals(this.approvedAmountString))
        {
            if (approvedAmountString.contains("."))
            {
                this.approvedAmountString = approvedAmountString;
                approvedAmount =Double.parseDouble(approvedAmountString);
                notifyChange();
            }

        }

    }

    public double getEmpExpensesId() {
        return empExpensesId;
    }

    public void setEmpExpensesId(double empExpensesId) {
        this.empExpensesId = empExpensesId;
    }


    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;

    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
        approvedAmountString = String.valueOf(approvedAmount);
    }

    public String getExpenseRemarks() {
        return expenseRemarks;
    }

    public void setExpenseRemarks(String expenseRemarks) {
        this.expenseRemarks = expenseRemarks;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;

    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getExpTitle() {
        return expTitle;
    }

    public void setExpTitle(String expTitle) {
        this.expTitle = expTitle;

    }

    public String getStatusRemarks() {
        return statusRemarks;


    }

    public void setStatusRemarks(String statusRemarks) {
        if (!statusRemarks.equals(this.statusRemarks))
        {
            this.statusRemarks = statusRemarks;
            notifyChange();
        }

    }
}
