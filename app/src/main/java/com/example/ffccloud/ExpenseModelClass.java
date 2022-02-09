package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ExpenseModelClass implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int expenseID_pk;

    @SerializedName("Emp_Expenses_Id")
    private int expenseID;

    @SerializedName("User_Id")
    private int userID;

    @SerializedName("Exp_Id")
    private int expenseTypeId;

    @SerializedName("Expense_Date")
    private String date;

    @SerializedName("Expense_Remarks")
    private String remarks;

    @SerializedName("Amount")
    private String amount;


    private String expenseType;

    public ExpenseModelClass() {
    }

    public ExpenseModelClass(int expenseID, int userID, int expenseTypeId, String date, String remarks, String amount, String expenseType) {
        this.expenseID = expenseID;
        this.userID = userID;
        this.expenseTypeId = expenseTypeId;
        this.date = date;
        this.remarks = remarks;
        this.amount = amount;
        this.expenseType = expenseType;
    }

    protected ExpenseModelClass(Parcel in) {
        expenseID_pk = in.readInt();
        expenseID = in.readInt();
        userID = in.readInt();
        expenseTypeId = in.readInt();
        date = in.readString();
        remarks = in.readString();
        amount = in.readString();
        expenseType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(expenseID_pk);
        dest.writeInt(expenseID);
        dest.writeInt(userID);
        dest.writeInt(expenseTypeId);
        dest.writeString(date);
        dest.writeString(remarks);
        dest.writeString(amount);
        dest.writeString(expenseType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpenseModelClass> CREATOR = new Creator<ExpenseModelClass>() {
        @Override
        public ExpenseModelClass createFromParcel(Parcel in) {
            return new ExpenseModelClass(in);
        }

        @Override
        public ExpenseModelClass[] newArray(int size) {
            return new ExpenseModelClass[size];
        }
    };

    public int getExpenseID() {
        return expenseID;
    }

    public int getExpenseID_pk() {
        return expenseID_pk;
    }

    public void setExpenseID_pk(int expenseID_pk) {
        this.expenseID_pk = expenseID_pk;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(int expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }
}
