package com.example.myapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class AddNewWorkPlanModel {

    @SerializedName("Work_Id")
    private long workId;
    @SerializedName("Work_From_Date")
    private String workFromDate;
    @SerializedName("Work_To_Date")
    private String workToDate;
    @SerializedName("Work_Plan")

    private String workPlan;
    @SerializedName("Doctor_Id")
    private long doctorId;
    @SerializedName("Remarks1")
    private String remarks1;
    @SerializedName("Emp_Id")
    private int empId;

    public AddNewWorkPlanModel()
    {

    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public String getWorkFromDate() {
        return workFromDate;
    }

    public void setWorkFromDate(String workFromDate) {
        this.workFromDate = workFromDate;
    }

    public String getWorkToDate() {
        return workToDate;
    }

    public void setWorkToDate(String workToDate) {
        this.workToDate = workToDate;
    }

    public String getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }
}
