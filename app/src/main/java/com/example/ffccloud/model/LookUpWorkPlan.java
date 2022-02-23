package com.example.ffccloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LookUpWorkPlan implements Parcelable {

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
    private String customerGrade;
    @SerializedName("P_Address")
    @Expose
    private String customerAddress;
    @SerializedName("P_Timings")
    @Expose
    private String customerTimings;
    @SerializedName("P_type")
    @Expose
    private String customerType;
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
    private String visitOn;
    @SerializedName("Visit_Cord")
    @Expose
    private String visitCord;
    @SerializedName("Visit_Address")
    @Expose
    private String visitAddress;
    @SerializedName("Visit_distance_Ver")
    @Expose
    private String visitDistanceVer;
    @SerializedName("Work_Plan_Remarks")
    @Expose
    private String workPlanRemarks;
    @SerializedName("Work_Plan_Exe_Remarks")
    @Expose
    private String visitRemarks;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;

    @SerializedName("emp_Father_Name")
    @Expose
    private String empFatherName;
    @SerializedName("Reg_Name")
    @Expose
    private String regionName;

    public LookUpWorkPlan() {
    }


    protected LookUpWorkPlan(Parcel in) {
        date = in.readString();
        day = in.readString();
        fromTime = in.readString();
        toTime = in.readString();
        customerId = in.readDouble();
        customerName = in.readString();
        customerEmail = in.readString();
        customerPhone = in.readString();
        customerQualification = in.readString();
        customerClassification = in.readString();
        customerGrade = in.readString();
        customerAddress = in.readString();
        customerTimings = in.readString();
        customerType = in.readString();
        customerLocCordAddress = in.readString();
        customerLocCord = in.readString();
        workPlan = in.readString();
        status = in.readString();
        visitOn = in.readString();
        visitCord = in.readString();
        visitAddress = in.readString();
        visitDistanceVer = in.readString();
        workPlanRemarks = in.readString();
        visitRemarks = in.readString();
        empName = in.readString();
        empFatherName = in.readString();
        regionName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(day);
        dest.writeString(fromTime);
        dest.writeString(toTime);
        dest.writeDouble(customerId);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(customerPhone);
        dest.writeString(customerQualification);
        dest.writeString(customerClassification);
        dest.writeString(customerGrade);
        dest.writeString(customerAddress);
        dest.writeString(customerTimings);
        dest.writeString(customerType);
        dest.writeString(customerLocCordAddress);
        dest.writeString(customerLocCord);
        dest.writeString(workPlan);
        dest.writeString(status);
        dest.writeString(visitOn);
        dest.writeString(visitCord);
        dest.writeString(visitAddress);
        dest.writeString(visitDistanceVer);
        dest.writeString(workPlanRemarks);
        dest.writeString(visitRemarks);
        dest.writeString(empName);
        dest.writeString(empFatherName);
        dest.writeString(regionName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LookUpWorkPlan> CREATOR = new Creator<LookUpWorkPlan>() {
        @Override
        public LookUpWorkPlan createFromParcel(Parcel in) {
            return new LookUpWorkPlan(in);
        }

        @Override
        public LookUpWorkPlan[] newArray(int size) {
            return new LookUpWorkPlan[size];
        }
    };

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

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

    public String getCustomerGrade() {
        return customerGrade;
    }

    public void setCustomerGrade(String customerGrade) {
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

    public String getEmpFatherName() {
        return empFatherName;
    }

    public void setEmpFatherName(String empFatherName) {
        this.empFatherName = empFatherName;
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

    public String getVisitOn() {
        return visitOn;
    }

    public void setVisitOn(String visitOn) {
        this.visitOn = visitOn;
    }

    public String getVisitCord() {
        return visitCord;
    }

    public void setVisitCord(String visitCord) {
        this.visitCord = visitCord;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getVisitDistanceVer() {
        return visitDistanceVer;
    }

    public void setVisitDistanceVer(String visitDistanceVer) {
        this.visitDistanceVer = visitDistanceVer;
    }

    public String getWorkPlanRemarks() {
        return workPlanRemarks;
    }

    public void setWorkPlanRemarks(String workPlanRemarks) {
        this.workPlanRemarks = workPlanRemarks;
    }

    public String getVisitRemarks() {
        return visitRemarks;
    }

    public void setVisitRemarks(String visitRemarks) {
        this.visitRemarks = visitRemarks;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }


}
