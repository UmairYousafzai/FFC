package com.example.ffccloud.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Meeting extends BaseObservable {

    private String Work_From_Date;
    private String Work_To_Date;
    private String Emp_Ids;
    private String Work_Plan_Type;
    @Bindable
    private String Location_Name;
    @Bindable
    private String Remarks;
    private int Emp_Host_Id;

    public Meeting() {

        Emp_Ids = "";
        Location_Name = "";
        Remarks="";
    }

    public String getWork_From_Date() {
        return Work_From_Date;
    }

    public void setWork_From_Date(String work_From_Date) {

        Work_From_Date = work_From_Date;

    }

    public String getWork_To_Date() {
        return Work_To_Date;
    }

    public void setWork_To_Date(String work_To_Date) {

        Work_To_Date = work_To_Date;

    }

    public String getEmp_Ids() {
        return Emp_Ids;
    }

    public void setEmp_Ids(String emp_Ids) {
            Emp_Ids = emp_Ids;

    }

    public String getWork_Plan_Type() {
        return Work_Plan_Type;
    }

    public void setWork_Plan_Type(String work_Plan_Type) {

        Work_Plan_Type = work_Plan_Type;


    }

    public String getLocation_Name() {
        return Location_Name;
    }

    public void setLocation_Name(String location_Name) {
        if (this.Location_Name != null && !this.Location_Name.equals(location_Name)) {
            Location_Name = location_Name;
            notifyChange();

        }
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {

        if (this.Remarks != null && !this.Remarks.equals(remarks)) {
            Remarks = remarks;
            notifyChange();

        }
    }

    public int getEmp_Host_Id() {
        return Emp_Host_Id;
    }

    public void setEmp_Host_Id(int emp_Host_Id) {
        Emp_Host_Id = emp_Host_Id;
    }
}
