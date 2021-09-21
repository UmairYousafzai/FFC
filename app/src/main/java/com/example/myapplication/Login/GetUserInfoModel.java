package com.example.myapplication.Login;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "User_Info")
public class GetUserInfoModel {

    @Nullable
    @ColumnInfo(name = "lstclsUserRole")
    @SerializedName("lstclsUserRole")
    @Expose
    private String lstclsUserRole;


    @Nullable
    @ColumnInfo(name = "lstclsDefLocationUser")
    @SerializedName("lstclsDefLocationUser")
    @Expose
    private String lstclsDefLocationUser;



    @Nullable
    @ColumnInfo(name = "lstCurrentUserRight")
    @SerializedName("lstCurrentUserRight")
    @Expose
    private String lstCurrentUserRight;


    @Nullable
    @ColumnInfo(name = "Image")
    @SerializedName("Image")
    @Expose
    private String image;

    @PrimaryKey
    @SerializedName("ID")
    @Expose
    private Integer iD;




    @Nullable
    @ColumnInfo(name = "UserName")
    @SerializedName("UserName")
    @Expose
    private String userName;



    @Nullable
    @ColumnInfo(name = "Gender")
    @SerializedName("Gender")
    @Expose
    private String gender;



    @Nullable
    @ColumnInfo(name = "MaritalStatus")
    @SerializedName("MaritalStatus")
    @Expose
    private String maritalStatus;



    @Nullable
    @ColumnInfo(name = "FatherName")
    @SerializedName("FatherName")
    @Expose
    private String fatherName;



    @Nullable
    @ColumnInfo(name = "CNIC")
    @SerializedName("CNIC")
    @Expose
    private String cNIC;



    @Nullable
    @ColumnInfo(name = "Mobile")
    @SerializedName("Mobile")
    @Expose
    private String mobile;



    @Nullable
    @ColumnInfo(name = "GuardianNo")
    @SerializedName("GuardianNo")
    @Expose
    private String guardianNo;



    @Nullable
    @ColumnInfo(name = "Email")
    @SerializedName("Email")
    @Expose
    private String email;



    @Nullable
    @ColumnInfo(name = "Password")
    @SerializedName("Password")
    @Expose
    private String password;



    @Nullable
    @ColumnInfo(name = "HomeAddress")
    @SerializedName("HomeAddress")
    @Expose
    private String homeAddress;



    @Nullable
    @ColumnInfo(name = "RoleID")
    @SerializedName("RoleID")
    @Expose
    private Integer roleID;



    @Nullable
    @ColumnInfo(name = "Emp_Id")
    @SerializedName("Emp_Id")
    @Expose
    private Integer empId;



    @Nullable
    @ColumnInfo(name = "Remarks")
    @SerializedName("Remarks")
    @Expose
    private String remarks;



    @Nullable
    @ColumnInfo(name = "IsAllowToLogin")
    @SerializedName("IsAllowToLogin")
    @Expose
    private Boolean isAllowToLogin;



    @Nullable
    @ColumnInfo(name = "strRoleID")
    @SerializedName("strRoleID")
    @Expose
    private String strRoleID;



    @Nullable
    @ColumnInfo(name = "SMSAlerts")
    @SerializedName("SMSAlerts")
    @Expose
    private Boolean sMSAlerts;



    @Nullable
    @ColumnInfo(name = "Code_Genereted_Date_Time")
    @SerializedName("Code_Genereted_Date_Time")
    @Expose
    private String codeGeneretedDateTime;



    @Nullable
    @ColumnInfo(name = "Code_Expiry_Time")
    @SerializedName("Code_Expiry_Time")
    @Expose
    private String codeExpiryTime;



    @Nullable
    @ColumnInfo(name = "Code")
    @SerializedName("Code")
    @Expose
    private Integer code;



    @Nullable
    @ColumnInfo(name = "Session_Id")
    @SerializedName("Session_Id")
    @Expose
    private Long sessionId;



    @Nullable
    @ColumnInfo(name = "Verified")
    @SerializedName("Verified")
    @Expose
    private Boolean verified;



    @Nullable
    @ColumnInfo(name = "Is_Device_Confg")
    @SerializedName("Is_Device_Confg")
    @Expose
    private Boolean isDeviceConfg;



    @Nullable
    @ColumnInfo(name = "Device_Address")
    @SerializedName("Device_Address")
    @Expose
    private String deviceAddress;

    public String getLstclsUserRole() {
        return lstclsUserRole;
    }

    public void setLstclsUserRole(String lstclsUserRole) {
        this.lstclsUserRole = lstclsUserRole;
    }

    public String getLstclsDefLocationUser() {
        return lstclsDefLocationUser;
    }

    public void setLstclsDefLocationUser(String lstclsDefLocationUser) {
        this.lstclsDefLocationUser = lstclsDefLocationUser;
    }

    public String getLstCurrentUserRight() {
        return lstCurrentUserRight;
    }

    public void setLstCurrentUserRight(String lstCurrentUserRight) {
        this.lstCurrentUserRight = lstCurrentUserRight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCNIC() {
        return cNIC;
    }

    public void setCNIC(String cNIC) {
        this.cNIC = cNIC;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGuardianNo() {
        return guardianNo;
    }

    public void setGuardianNo(String guardianNo) {
        this.guardianNo = guardianNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsAllowToLogin() {
        return isAllowToLogin;
    }

    public void setIsAllowToLogin(Boolean isAllowToLogin) {
        this.isAllowToLogin = isAllowToLogin;
    }

    public String getStrRoleID() {
        return strRoleID;
    }

    public void setStrRoleID(String strRoleID) {
        this.strRoleID = strRoleID;
    }

    public Boolean getSMSAlerts() {
        return sMSAlerts;
    }

    public void setSMSAlerts(Boolean sMSAlerts) {
        this.sMSAlerts = sMSAlerts;
    }

    public String getCodeGeneretedDateTime() {
        return codeGeneretedDateTime;
    }

    public void setCodeGeneretedDateTime(String codeGeneretedDateTime) {
        this.codeGeneretedDateTime = codeGeneretedDateTime;
    }

    public String getCodeExpiryTime() {
        return codeExpiryTime;
    }

    public void setCodeExpiryTime(String codeExpiryTime) {
        this.codeExpiryTime = codeExpiryTime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getIsDeviceConfg() {
        return isDeviceConfg;
    }

    public void setIsDeviceConfg(Boolean isDeviceConfg) {
        this.isDeviceConfg = isDeviceConfg;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
