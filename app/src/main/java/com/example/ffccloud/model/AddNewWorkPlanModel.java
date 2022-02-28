package com.example.ffccloud.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "workPlan")
public class AddNewWorkPlanModel {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    @SerializedName("Work_Id")
    private long workId;
    @SerializedName("Work_From_Date")
    private String workFromDate;
    @SerializedName("Work_To_Date")
    private String workToDate;
    @SerializedName("Work_Plan")

    private String workPlan;
    @SerializedName("Doctor_Id")
    private int doctorId;
    @SerializedName("Remarks1")
    private String remarks1;
    @SerializedName("Emp_Id")
    private int empId;

    @SerializedName("Visit_On")
    private String Visit_On;
    @SerializedName("Visit_Cord")
    private String Visit_Cord;

    @SerializedName("Visit_Address")
    private String visitAddress;
    @SerializedName("Visit_distance_Ver")
    private double visitDistance;
    public AddNewWorkPlanModel()
    {

    }

    public String getVisit_On() {
        return Visit_On;
    }

    public void setVisit_On(String visit_On) {
        Visit_On = visit_On;
    }

    public String getVisit_Cord() {
        return Visit_Cord;
    }

    public void setVisit_Cord(String visit_Cord) {
        Visit_Cord = visit_Cord;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public double getVisitDistance() {
        return visitDistance;
    }

    public void setVisitDistance(double visitDistance) {
        this.visitDistance = visitDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
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
