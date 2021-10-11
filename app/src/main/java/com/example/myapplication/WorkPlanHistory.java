package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class WorkPlanHistory {


    @SerializedName("Work_From_Date")

    private String workFromDate;
    @SerializedName("Remarks2")

    private String remarks2;

    @SerializedName("Visit_On")

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWorkFromDate() {
        return workFromDate;
    }

    public void setWorkFromDate(String workFromDate) {
        this.workFromDate = workFromDate;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

}