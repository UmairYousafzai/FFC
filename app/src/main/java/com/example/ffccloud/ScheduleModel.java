package com.example.ffccloud;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Schedule")
public class ScheduleModel {

    @PrimaryKey(autoGenerate = true)
    private int id_pk;

    @SerializedName("Region_Title")
    String regionTittle;
    @SerializedName("Area_Title")
    String areaTittle;


    @SerializedName("DoctorSchedules_Id")

    private Integer scheduleId;
    @SerializedName("Doctor_Id")

    private Integer doctorId;
    @SerializedName("Ac_No")

    private Integer acNo;
    @SerializedName("Sub_Head_Code")

    private Integer subHeadCode;
    @SerializedName("Day_Id")

    private Integer dayId;
    @SerializedName("Opening_Time")

    private String openingTime;
    @SerializedName("Closing_Time")

    private String closingTime;
    @SerializedName("Coordinates")

    private String coordinates;
    @SerializedName("Primary_Loc")

    private Boolean primaryLoc;
    @SerializedName("Country_Id")

    private Integer countryId;
    @SerializedName("Company_Id")

    private Integer companyId;
    @SerializedName("Location_Id")

    private Integer locationId;
    @SerializedName("Project_Id")

    private Integer projectId;
    @SerializedName("FYear")

    private Integer fYear;
    @SerializedName("Remarks")

    @Ignore
    private Object remarks;
    @SerializedName("Account_No")

    private Integer accountNo;
    @SerializedName("Account_Title")

    @Ignore
    private Object accountTitle;
    @SerializedName("Posted")

    private Boolean posted;
    @SerializedName("Post")

    private Boolean post;
    @SerializedName("Cancel")

    private Boolean cancel;
    @SerializedName("Active")

    private Boolean active;
    @SerializedName("User_Id")

    private Integer userId;
    @SerializedName("Session_Id")

    private Integer sessionId;

    public String getRegionTittle() {
        return regionTittle;
    }

    public void setRegionTittle(String regionTittle) {
        this.regionTittle = regionTittle;
    }

    public String getAreaTittle() {
        return areaTittle;
    }

    public void setAreaTittle(String areaTittle) {
        this.areaTittle = areaTittle;
    }

    public int getId_pk() {
        return id_pk;
    }

    public void setId_pk(int id_pk) {
        this.id_pk = id_pk;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getAcNo() {
        return acNo;
    }

    public void setAcNo(Integer acNo) {
        this.acNo = acNo;
    }

    public Integer getSubHeadCode() {
        return subHeadCode;
    }

    public void setSubHeadCode(Integer subHeadCode) {
        this.subHeadCode = subHeadCode;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Boolean getPrimaryLoc() {
        return primaryLoc;
    }

    public void setPrimaryLoc(Boolean primaryLoc) {
        this.primaryLoc = primaryLoc;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getFYear() {
        return fYear;
    }

    public void setFYear(Integer fYear) {
        this.fYear = fYear;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Integer accountNo) {
        this.accountNo = accountNo;
    }

    public Object getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(Object accountTitle) {
        this.accountTitle = accountTitle;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public Boolean getPost() {
        return post;
    }

    public void setPost(Boolean post) {
        this.post = post;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

}
