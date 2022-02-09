package com.example.ffccloud.model;

import com.google.gson.annotations.SerializedName;

public class SaveNewWorkPlanModel {

    @SerializedName("Date")
    String date;
    @SerializedName("Doctor_Ids")
    String doctorIds;
    @SerializedName("Emp_Id")
    long EmpId;
    @SerializedName("Remarks")
    String Remarks;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(String doctorIds) {
        this.doctorIds = doctorIds;
    }

    public long getEmpId() {
        return EmpId;
    }

    public void setEmpId(long empId) {
        EmpId = empId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
