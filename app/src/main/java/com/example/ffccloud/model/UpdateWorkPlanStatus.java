package com.example.ffccloud.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "workPlanStatus")
public class UpdateWorkPlanStatus {

    @PrimaryKey(autoGenerate =true)
    private int statusID;

    @SerializedName("Doctor_Id")
    public int Doctor_Id;


    @SerializedName("Work_Id")
    public String Work_Id;
    @SerializedName("Emp_Id")
    public long Emp_Id;

    public long getEmp_Id() {
        return Emp_Id;
    }

    public void setEmp_Id(long emp_Id) {
        Emp_Id = emp_Id;
    }

    @SerializedName("Status")
    public String Status;
    @SerializedName("Remarks1")
    public String Remarks;
    //    @SerializedName("Remarks2")
//    public String Remarks2 ;

    @SerializedName("Visit_On")
    public String Visit_On;
    @SerializedName("Visit_Cord")
    public String Visit_Cord;


    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public int getDoctor_Id() {
        return Doctor_Id;
    }

    public void setDoctor_Id(int doctor_Id) {
        Doctor_Id = doctor_Id;
    }

    public String getWork_Id() {
        return Work_Id;
    }

    public void setWork_Id(String work_Id) {
        Work_Id = work_Id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
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
}
