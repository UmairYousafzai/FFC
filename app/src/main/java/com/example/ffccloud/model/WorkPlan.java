package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkPlan {

    @SerializedName("Work_Plan_MId")
    @Expose
    private double workPlanMId;
    @SerializedName("Work_Plan_Date")
    @Expose
    private String workPlanDate;
    @SerializedName("Work_Trans_Date")
    @Expose
    private String workTransDate;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("Emp_Id")
    @Expose
    private double empId;
    @SerializedName("Emp_Image")
    @Expose
    private String empImage;

    public double getWorkPlanMId() {
        return workPlanMId;
    }

    public void setWorkPlanMId(double workPlanMId) {
        this.workPlanMId = workPlanMId;
    }

    public String getWorkPlanDate() {
        return workPlanDate;
    }

    public void setWorkPlanDate(String workPlanDate) {
        this.workPlanDate = workPlanDate;
    }

    public String getWorkTransDate() {
        return workTransDate;
    }

    public void setWorkTransDate(String workTransDate) {
        this.workTransDate = workTransDate;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public double getEmpId() {
        return empId;
    }

    public void setEmpId(double empId) {
        this.empId = empId;
    }

    public String getEmpImage() {
        return empImage;
    }

    public void setEmpImage(String empImage) {
        this.empImage = empImage;
    }
}
