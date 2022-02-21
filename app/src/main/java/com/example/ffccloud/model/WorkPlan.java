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

    @SerializedName("Work_Plan_No")
    @Expose
    private String workPlanNo;

    @SerializedName("Created_By")
    @Expose
    private double createdBy;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;
    @SerializedName("Work_Id")
    @Expose
    private double workId;
    @SerializedName("Work_From_Date")
    @Expose
    private String workFromDate;
    @SerializedName("Work_To_Date")
    @Expose
    private String workToDate;
    @SerializedName("Work_Plan")
    @Expose
    private String workPlan;
    @SerializedName("Doctor_Id")
    @Expose
    private double doctorId;

    @SerializedName("Remarks1")
    @Expose
    private String remarks1;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Visit_Cord")
    @Expose
    private String visitCord;

    public String getWorkPlanNo() {
        return workPlanNo;
    }

    public void setWorkPlanNo(String workPlanNo) {
        this.workPlanNo = workPlanNo;
    }

    public double getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(double createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public double getWorkId() {
        return workId;
    }

    public void setWorkId(double workId) {
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

    public double getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(double doctorId) {
        this.doctorId = doctorId;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisitCord() {
        return visitCord;
    }

    public void setVisitCord(String visitCord) {
        this.visitCord = visitCord;
    }

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
