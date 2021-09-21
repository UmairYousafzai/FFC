package com.example.myapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class UpdateWorkPlanStatus {
    @SerializedName("Doctor_Id")
    public long Doctor_Id;


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

    public long getDoctor_Id() {
        return Doctor_Id;
    }

    public void setDoctor_Id(long doctor_Id) {
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
