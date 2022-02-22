package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LookUpWorkPlan {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Day")
    @Expose
    private String day;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("ToTime")
    @Expose
    private String toTime;
    @SerializedName("P_Id")
    @Expose
    private double customerId;
    @SerializedName("P_Name")
    @Expose
    private String customerName;
    @SerializedName("P_Email")
    @Expose
    private String customerEmail;
    @SerializedName("P_Phone")
    @Expose
    private String customerPhone;
    @SerializedName("P_Qualification")
    @Expose
    private String customerQualification;
    @SerializedName("P_Classification")
    @Expose
    private String customerClassification;
    @SerializedName("P_Grade")
    @Expose
    private Object customerGrade;
    @SerializedName("P_Address")
    @Expose
    private String customerAddress;
    @SerializedName("P_Timings")
    @Expose
    private String customerTimings;
    @SerializedName("P_Loc_Cord_Address")
    @Expose
    private String customerLocCordAddress;
    @SerializedName("P_Loc_Cord")
    @Expose
    private String customerLocCord;
    @SerializedName("Work_Plan")
    @Expose
    private String workPlan;
    @SerializedName("w_Status")
    @Expose
    private String status;
    @SerializedName("Visit_On")
    @Expose
    private Object visitOn;
    @SerializedName("Visit_Cord")
    @Expose
    private Object visitCord;
    @SerializedName("Visit_Address")
    @Expose
    private Object visitAddress;
    @SerializedName("Visit_distance_Ver")
    @Expose
    private Object visitDistanceVer;
    @SerializedName("Work_Plan_Remarks")
    @Expose
    private String workPlanRemarks;
    @SerializedName("Work_Plan_Exe_Remarks")
    @Expose
    private Object workPlanExeRemarks;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("Reg_Name")
    @Expose
    private String regionName;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public double getCustomerId() {
        return customerId;
    }

    public void setCustomerId(double customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerQualification() {
        return customerQualification;
    }

    public void setCustomerQualification(String customerQualification) {
        this.customerQualification = customerQualification;
    }

    public String getCustomerClassification() {
        return customerClassification;
    }

    public void setCustomerClassification(String customerClassification) {
        this.customerClassification = customerClassification;
    }

    public Object getCustomerGrade() {
        return customerGrade;
    }

    public void setCustomerGrade(Object customerGrade) {
        this.customerGrade = customerGrade;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTimings() {
        return customerTimings;
    }

    public void setCustomerTimings(String customerTimings) {
        this.customerTimings = customerTimings;
    }

    public String getCustomerLocCordAddress() {
        return customerLocCordAddress;
    }

    public void setCustomerLocCordAddress(String customerLocCordAddress) {
        this.customerLocCordAddress = customerLocCordAddress;
    }

    public String getCustomerLocCord() {
        return customerLocCord;
    }

    public void setCustomerLocCord(String customerLocCord) {
        this.customerLocCord = customerLocCord;
    }

    public String getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getVisitOn() {
        return visitOn;
    }

    public void setVisitOn(Object visitOn) {
        this.visitOn = visitOn;
    }

    public Object getVisitCord() {
        return visitCord;
    }

    public void setVisitCord(Object visitCord) {
        this.visitCord = visitCord;
    }

    public Object getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(Object visitAddress) {
        this.visitAddress = visitAddress;
    }

    public Object getVisitDistanceVer() {
        return visitDistanceVer;
    }

    public void setVisitDistanceVer(Object visitDistanceVer) {
        this.visitDistanceVer = visitDistanceVer;
    }

    public String getWorkPlanRemarks() {
        return workPlanRemarks;
    }

    public void setWorkPlanRemarks(String workPlanRemarks) {
        this.workPlanRemarks = workPlanRemarks;
    }

    public Object getWorkPlanExeRemarks() {
        return workPlanExeRemarks;
    }

    public void setWorkPlanExeRemarks(Object workPlanExeRemarks) {
        this.workPlanExeRemarks = workPlanExeRemarks;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
