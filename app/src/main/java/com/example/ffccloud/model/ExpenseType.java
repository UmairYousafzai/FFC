package com.example.ffccloud.model;

import com.google.gson.annotations.SerializedName;

public class ExpenseType {
    @SerializedName("Exp_Id")
    int expenseID;
    @SerializedName("Exp_Title")
    String expenseTitle;

    public ExpenseType() {
    }

    public ExpenseType(int expenseID, String expenseTitle) {
        this.expenseID = expenseID;
        this.expenseTitle = expenseTitle;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }
}
