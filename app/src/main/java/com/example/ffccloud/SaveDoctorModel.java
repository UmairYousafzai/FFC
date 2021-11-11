package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ffccloud.ModelClasses.DoctorScheduleModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveDoctorModel implements Parcelable {

    @SerializedName("Doc_Status")
    public long Doc_Status;
    @SerializedName("Suggested_UserId")
    public long Suggested_UserId;
    @SerializedName("Company_Id")
    public long Company_Id;
    @SerializedName("Country_Id")
    public long Country_Id;
    @SerializedName("Doctor_Id")
    public long Doctor_Id;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Qualification_Id")
    public int Qualification_Id;
    @SerializedName("Classification_Id")
    public int Classification_Id;
    @SerializedName("Grade_Id")
    public int Grade_Id;
    @SerializedName("Timings")
    public int Timings;
    @SerializedName("Address")
    public String Address;
    @SerializedName("Products")
    public String Products;
    @SerializedName("Phone")
    public String Phone;
    @SerializedName("DOB")
    public String DOB;
    @SerializedName("Gender")
    public int Gender;
    @SerializedName("Email")
    public String Email;
    @SerializedName("ImagePath")
    public String ImagePath;
    public List<DoctorScheduleModel> Schedules;


    public SaveDoctorModel() {
    }

    protected SaveDoctorModel(Parcel in) {
        Doc_Status = in.readLong();
        Suggested_UserId = in.readLong();
        Company_Id = in.readLong();
        Country_Id = in.readLong();
        Doctor_Id = in.readLong();
        Name = in.readString();
        Qualification_Id = in.readInt();
        Classification_Id = in.readInt();
        Grade_Id = in.readInt();
        Timings = in.readInt();
        Address = in.readString();
        Products = in.readString();
        Phone = in.readString();
        DOB = in.readString();
        Gender = in.readInt();
        Email = in.readString();
        ImagePath = in.readString();
        Schedules = in.createTypedArrayList(DoctorScheduleModel.CREATOR);
    }

    public static final Creator<SaveDoctorModel> CREATOR = new Creator<SaveDoctorModel>() {
        @Override
        public SaveDoctorModel createFromParcel(Parcel in) {
            return new SaveDoctorModel(in);
        }

        @Override
        public SaveDoctorModel[] newArray(int size) {
            return new SaveDoctorModel[size];
        }
    };

    public void SaveDoctorModel()
    {

    }

    public long getDoc_Status() {
        return Doc_Status;
    }

    public void setDoc_Status(long doc_Status) {
        Doc_Status = doc_Status;
    }

    public long getSuggested_UserId() {
        return Suggested_UserId;
    }

    public void setSuggested_UserId(long suggested_UserId) {
        Suggested_UserId = suggested_UserId;
    }

    public long getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(long company_Id) {
        Company_Id = company_Id;
    }

    public long getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(long country_Id) {
        Country_Id = country_Id;
    }

    public long getDoctor_Id() {
        return Doctor_Id;
    }

    public void setDoctor_Id(long doctor_Id) {
        Doctor_Id = doctor_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQualification_Id() {
        return Qualification_Id;
    }

    public void setQualification_Id(int qualification_Id) {
        Qualification_Id = qualification_Id;
    }

    public int getClassification_Id() {
        return Classification_Id;
    }

    public void setClassification_Id(int classification_Id) {
        Classification_Id = classification_Id;
    }

    public int getGrade_Id() {
        return Grade_Id;
    }

    public void setGrade_Id(int grade_Id) {
        Grade_Id = grade_Id;
    }

    public int getTimings() {
        return Timings;
    }

    public void setTimings(int timings) {
        Timings = timings;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProducts() {
        return Products;
    }

    public void setProducts(String products) {
        Products = products;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public List<DoctorScheduleModel> getSchedules() {
        return Schedules;
    }

    public void setSchedules(List<DoctorScheduleModel> schedules) {
        Schedules = schedules;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Doc_Status);
        dest.writeLong(Suggested_UserId);
        dest.writeLong(Company_Id);
        dest.writeLong(Country_Id);
        dest.writeLong(Doctor_Id);
        dest.writeString(Name);
        dest.writeInt(Qualification_Id);
        dest.writeInt(Classification_Id);
        dest.writeInt(Grade_Id);
        dest.writeInt(Timings);
        dest.writeString(Address);
        dest.writeString(Products);
        dest.writeString(Phone);
        dest.writeString(DOB);
        dest.writeInt(Gender);
        dest.writeString(Email);
        dest.writeString(ImagePath);
        dest.writeTypedList(Schedules);
    }
}
