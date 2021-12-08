package com.example.ffccloud.ModelClasses;

import com.example.ffccloud.SupplierCompDetail;
import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.SupplierItemLinking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierModelNew {


    @SerializedName("Company_Id")
    @Expose
    private Integer companyId;
    @SerializedName("Country_Id")
    @Expose
    private Integer countryId;
    @SerializedName("Location_Id")
    @Expose
    private Integer locationId;
    @SerializedName("Project_Id")
    @Expose
    private Integer projectId;
    @SerializedName("Supplier_Id")
    @Expose
    private Integer supplierId;
    @SerializedName("Supplier_Code")
    @Expose
    private Integer supplierCode;
    @SerializedName("Supplier_Name")
    @Expose
    private String supplierName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Phone_No")
    @Expose
    private String phoneNo;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("Instruction")
    @Expose
    private String instruction;
    @SerializedName("UserTypeName")
    @Expose
    private String userTypeName;
    @SerializedName("User_Sub_Type")
    @Expose
    private String userSubType;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("Animals_Main_Type")
    @Expose
    private String animalsMainType;
    @SerializedName("Animals_Sub_Type")
    @Expose
    private String animalsSubType;
    @SerializedName("No_Of_Animals")
    @Expose
    private String noOfAnimals;
    @SerializedName("Grade_id")
    @Expose
    private Integer gradeId;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("Gas_Days")
    @Expose
    private String gasDays;
    @SerializedName("Monthly_Sale")
    @Expose
    private String monthlySale;
    @SerializedName("Region_Id")
    @Expose
    private String regionId;

    private String Loc_Cord;
    private String Loc_Cord_Address;
    private boolean Is_update_Loc_Cord;
    private int Classification_Id;
    private int Qualification_Id ;
    private String Gender  ;
    private String Shift_Type   ;



    @SerializedName("ContactPersonsList")
    @Expose
    private List<ContactPersons> contactPersonsList = null;
    @SerializedName("SupplierLinkingList")
    @Expose
    private List<SupplierLinking> supplierLinkingList = null;
    @SerializedName("SupplierItemLinkingList")
    @Expose
    private List<SupplierItemLinking> supplierItemLinkingList = null;
    @SerializedName("SupplierCompDetailList")
    @Expose
    private List<SupplierCompDetail> supplierCompDetailList = null;


    public boolean isIs_update_Loc_Cord() {
        return Is_update_Loc_Cord;
    }

    public int getClassification_Id() {
        return Classification_Id;
    }

    public void setClassification_Id(int classification_Id) {
        Classification_Id = classification_Id;
    }

    public int getQualification_Id() {
        return Qualification_Id;
    }

    public void setQualification_Id(int qualification_Id) {
        Qualification_Id = qualification_Id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getShift_Type() {
        return Shift_Type;
    }

    public void setShift_Type(String shift_Type) {
        Shift_Type = shift_Type;
    }

    public String getLoc_Cord() {
        return Loc_Cord;
    }

    public void setLoc_Cord(String loc_Cord) {
        Loc_Cord = loc_Cord;
    }

    public String getLoc_Cord_Address() {
        return Loc_Cord_Address;
    }

    public void setLoc_Cord_Address(String loc_Cord_Address) {
        Loc_Cord_Address = loc_Cord_Address;
    }

    public boolean getIs_update_Loc_Cord() {
        return Is_update_Loc_Cord;
    }

    public void setIs_update_Loc_Cord(boolean is_update_Loc_Cord) {
        Is_update_Loc_Cord = is_update_Loc_Cord;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Integer supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getUserSubType() {
        return userSubType;
    }

    public void setUserSubType(String userSubType) {
        this.userSubType = userSubType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnimalsMainType() {
        return animalsMainType;
    }

    public void setAnimalsMainType(String animalsMainType) {
        this.animalsMainType = animalsMainType;
    }

    public String getAnimalsSubType() {
        return animalsSubType;
    }

    public void setAnimalsSubType(String animalsSubType) {
        this.animalsSubType = animalsSubType;
    }

    public String getNoOfAnimals() {
        return noOfAnimals;
    }

    public void setNoOfAnimals(String noOfAnimals) {
        this.noOfAnimals = noOfAnimals;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGasDays() {
        return gasDays;
    }

    public void setGasDays(String gasDays) {
        this.gasDays = gasDays;
    }

    public String getMonthlySale() {
        return monthlySale;
    }

    public void setMonthlySale(String monthlySale) {
        this.monthlySale = monthlySale;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public List<ContactPersons> getContactPersonsList() {
        return contactPersonsList;
    }

    public void setContactPersonsList(List<ContactPersons> contactPersonsList) {
        this.contactPersonsList = contactPersonsList;
    }

    public List<SupplierLinking> getSupplierLinkingList() {
        return supplierLinkingList;
    }

    public void setSupplierLinkingList(List<SupplierLinking> supplierLinkingList) {
        this.supplierLinkingList = supplierLinkingList;
    }

    public List<SupplierItemLinking> getSupplierItemLinkingList() {
        return supplierItemLinkingList;
    }

    public void setSupplierItemLinkingList(List<SupplierItemLinking> supplierItemLinkingList) {
        this.supplierItemLinkingList = supplierItemLinkingList;
    }

    public List<SupplierCompDetail> getSupplierCompDetailList() {
        return supplierCompDetailList;
    }

    public void setSupplierCompDetailList(List<SupplierCompDetail> supplierCompDetailList) {
        this.supplierCompDetailList = supplierCompDetailList;
    }

}

