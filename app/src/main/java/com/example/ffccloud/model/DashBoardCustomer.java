package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardCustomer {
    @SerializedName("Doctor_Id")
    @Expose
    private double doctorId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("Suggested_DateTime")
    @Expose
    private String suggestedDateTime;
    @SerializedName("User_Type_Dec")
    @Expose
    private String userTypeDec;
    @SerializedName("User_Type")
    @Expose
    private String userType;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(double doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSuggestedDateTime() {
        return suggestedDateTime;
    }

    public void setSuggestedDateTime(String suggestedDateTime) {
        this.suggestedDateTime = suggestedDateTime;
    }

    public String getUserTypeDec() {
        return userTypeDec;
    }

    public void setUserTypeDec(String userTypeDec) {
        this.userTypeDec = userTypeDec;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
