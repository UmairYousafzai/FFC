package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.ScheduleModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilteredDoctorInfomationModel implements Parcelable {

    @SerializedName("Qualification_Tittle")
    String qualification_title;

    @SerializedName("Classification_Tittle")
    String classification_title;

    @SerializedName("Grade_Tittle")
    String grade_title;



    @SerializedName("Schedules")

    private List<ScheduleModel> scheduleModels = null;
    @SerializedName("Doctor_Id")

    private int doctorId;
    @SerializedName("Name")

    private String name;
    @SerializedName("Qualification_Id")

    private int qualificationId;
    @SerializedName("Classification_Id")

    private int classificationId;
    @SerializedName("Grade_Id")

    private int gradeId;
    @SerializedName("Timings")

    private int timings;
    @SerializedName("Address")

    private String address;
    @SerializedName("Products")

    private String products;
    @SerializedName("Phone")

    private String phone;
    @SerializedName("DOB")

    private String dOB;
    @SerializedName("Gender")

    private int gender;
    @SerializedName("Email")

    private String email;
    @SerializedName("ImagePath")

    private String imagePath;
    @SerializedName("Country_Id")

    private int countryId;
    @SerializedName("Company_Id")

    private int companyId;
    @SerializedName("Location_Id")

    private int locationId;
    @SerializedName("Project_Id")

    private int projectId;
    @SerializedName("FYear")

    private int fYear;
    @SerializedName("Remarks")

    private Object remarks;
    @SerializedName("Account_No")

    private int accountNo;
    @SerializedName("Account_Title")

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

    private int userId;
    @SerializedName("Session_Id")

    private int sessionId;


    protected FilteredDoctorInfomationModel(Parcel in) {
        qualification_title = in.readString();
        classification_title = in.readString();
        grade_title = in.readString();
        doctorId = in.readInt();
        name = in.readString();
        qualificationId = in.readInt();
        classificationId = in.readInt();
        gradeId = in.readInt();
        timings = in.readInt();
        address = in.readString();
        products = in.readString();
        phone = in.readString();
        dOB = in.readString();
        gender = in.readInt();
        email = in.readString();
        imagePath = in.readString();
        countryId = in.readInt();
        companyId = in.readInt();
        locationId = in.readInt();
        projectId = in.readInt();
        fYear = in.readInt();
        accountNo = in.readInt();
        byte tmpPosted = in.readByte();
        posted = tmpPosted == 0 ? null : tmpPosted == 1;
        byte tmpPost = in.readByte();
        post = tmpPost == 0 ? null : tmpPost == 1;
        byte tmpCancel = in.readByte();
        cancel = tmpCancel == 0 ? null : tmpCancel == 1;
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        userId = in.readInt();
        sessionId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(qualification_title);
        dest.writeString(classification_title);
        dest.writeString(grade_title);
        dest.writeInt(doctorId);
        dest.writeString(name);
        dest.writeInt(qualificationId);
        dest.writeInt(classificationId);
        dest.writeInt(gradeId);
        dest.writeInt(timings);
        dest.writeString(address);
        dest.writeString(products);
        dest.writeString(phone);
        dest.writeString(dOB);
        dest.writeInt(gender);
        dest.writeString(email);
        dest.writeString(imagePath);
        dest.writeInt(countryId);
        dest.writeInt(companyId);
        dest.writeInt(locationId);
        dest.writeInt(projectId);
        dest.writeInt(fYear);
        dest.writeInt(accountNo);
        dest.writeByte((byte) (posted == null ? 0 : posted ? 1 : 2));
        dest.writeByte((byte) (post == null ? 0 : post ? 1 : 2));
        dest.writeByte((byte) (cancel == null ? 0 : cancel ? 1 : 2));
        dest.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        dest.writeInt(userId);
        dest.writeInt(sessionId);
    }

    public static final Creator<FilteredDoctorInfomationModel> CREATOR = new Creator<FilteredDoctorInfomationModel>() {
        @Override
        public FilteredDoctorInfomationModel createFromParcel(Parcel in) {
            return new FilteredDoctorInfomationModel(in);
        }

        @Override
        public FilteredDoctorInfomationModel[] newArray(int size) {
            return new FilteredDoctorInfomationModel[size];
        }
    };

    public String getQualification_title() {
        return qualification_title;
    }

    public void setQualification_title(String qualification_title) {
        this.qualification_title = qualification_title;
    }

    public String getClassification_title() {
        return classification_title;
    }

    public void setClassification_title(String classification_title) {
        this.classification_title = classification_title;
    }

    public String getGrade_title() {
        return grade_title;
    }

    public void setGrade_title(String grade_title) {
        this.grade_title = grade_title;
    }

    public List<ScheduleModel> getScheduleModels() {
        return scheduleModels;
    }

    public void setScheduleModels(List<ScheduleModel> scheduleModels) {
        this.scheduleModels = scheduleModels;
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

    public int getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(int qualificationId) {
        this.qualificationId = qualificationId;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getTimings() {
        return timings;
    }

    public void setTimings(int timings) {
        this.timings = timings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getFYear() {
        return fYear;
    }

    public void setFYear(int fYear) {
        this.fYear = fYear;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
