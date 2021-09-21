package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "Doctor")
public class DoctorModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int dotorId_pk;

    @SerializedName("Work_Id")
    @Expose
    private long workId;
    @SerializedName("Doctor_Id")
    @Expose
    private long doctorId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("ProfileImage")
    @Expose
    private String profileImage;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("Classification")
    @Expose
    private String classification;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Marked")
    @Expose
    private Boolean marked;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Coordinates")
    @Expose
    private String coordinates;
    @SerializedName("Shift")
    @Expose
    private String shift;
    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("From_Date")
    @Expose
    private String fromDate;
    @SerializedName("To_Date")
    @Expose
    private String toDate;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Work_date")
    @Expose
    private String workDate;

    @SerializedName("WorkTime")
    @Expose
    private String WorkTime;

    private String distance;


    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkTime() {
        return WorkTime;
    }

    public void setWorkTime(String workTime) {
        WorkTime = workTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getDotorId_pk() {
        return dotorId_pk;
    }

    public void setDotorId_pk(int dotorId_pk) {
        this.dotorId_pk = dotorId_pk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
