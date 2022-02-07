package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "Doctor")
public class DoctorModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private  int dotorId_pk;

    @SerializedName("Work_Id")
    @Expose
    private long workId;
    @SerializedName("Doctor_Id")
    @Expose
    private int doctorId;
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

    private String Work_Plan;

    public DoctorModel() {
    }


    protected DoctorModel(Parcel in) {
        dotorId_pk = in.readInt();
        workId = in.readLong();
        doctorId = in.readInt();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        profileImage = in.readString();
        qualification = in.readString();
        classification = in.readString();
        address = in.readString();
        byte tmpMarked = in.readByte();
        marked = tmpMarked == 0 ? null : tmpMarked == 1;
        remarks = in.readString();
        coordinates = in.readString();
        shift = in.readString();
        area = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        status = in.readString();
        workDate = in.readString();
        WorkTime = in.readString();
        distance = in.readString();
        Work_Plan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dotorId_pk);
        dest.writeLong(workId);
        dest.writeInt(doctorId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(profileImage);
        dest.writeString(qualification);
        dest.writeString(classification);
        dest.writeString(address);
        dest.writeByte((byte) (marked == null ? 0 : marked ? 1 : 2));
        dest.writeString(remarks);
        dest.writeString(coordinates);
        dest.writeString(shift);
        dest.writeString(area);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(status);
        dest.writeString(workDate);
        dest.writeString(WorkTime);
        dest.writeString(distance);
        dest.writeString(Work_Plan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
        @Override
        public DoctorModel createFromParcel(Parcel in) {
            return new DoctorModel(in);
        }

        @Override
        public DoctorModel[] newArray(int size) {
            return new DoctorModel[size];
        }
    };

    public String getWork_Plan() {
        return Work_Plan;
    }

    public void setWork_Plan(String work_Plan) {
        Work_Plan = work_Plan;
    }

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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
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


}
