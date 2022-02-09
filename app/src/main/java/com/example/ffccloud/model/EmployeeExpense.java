package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeExpense {
    @SerializedName("User_Id")
    @Expose
    private double userId;
    @SerializedName("Un_Approved")
    @Expose
    private int unApproved;
    @SerializedName("Approved")
    @Expose
    private int approved;
    @SerializedName("Cancelled")
    @Expose
    private int cancelled;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("Emp_Image")
    @Expose
    private String empImage;

    public double getUserId() {
        return userId;
    }

    public void setUserId(double userId) {
        this.userId = userId;
    }

    public int getUnApproved() {
        return unApproved;
    }

    public void setUnApproved(int unApproved) {
        this.unApproved = unApproved;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpImage() {
        return empImage;
    }

    public void setEmpImage(String empImage) {
        this.empImage = empImage;
    }
}
