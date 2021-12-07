package com.example.ffccloud.ModelClasses;

import com.example.ffccloud.CompanyModel;
import com.example.ffccloud.ContactPersonsModel;
import com.example.ffccloud.Medicine_modal;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierModelNew {

    @SerializedName("Company_Id")
    @Expose
    private Long companyId;
    @SerializedName("Country_Id")
    @Expose
    private Long countryId;
    @SerializedName("Location_Id")
    @Expose
    private Long locationId;
    @SerializedName("Project_Id")
    @Expose
    private Long projectId;
    @SerializedName("Supplier_Id")
    @Expose
    private Long supplierId;
    @SerializedName("Supplier_Code")
    @Expose
    private Long supplierCode;
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
    private Long userId;
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
    private Long gradeId;
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
    @SerializedName("ContactPersonsModelList")
    @Expose
    private List<ContactPersonsModel> ContactPersonsModelList = null;
    @SerializedName("SupplierLinkingList")
    @Expose
    private List<SupplierLinking> supplierLinkingList = null;
    @SerializedName("SupplierItemLinkingList")
    @Expose
    private List<Medicine_modal> supplierItemLinkingList = null;
    @SerializedName("SupplierCompDetailList")
    @Expose
    private List<CompanyModel> supplierCompDetailList = null;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Long supplierCode) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
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

    public List<ContactPersonsModel> getContactPersonsModelList() {
        return ContactPersonsModelList;
    }

    public void setContactPersonsModelList(List<ContactPersonsModel> ContactPersonsModelList) {
        this.ContactPersonsModelList = ContactPersonsModelList;
    }

    public List<SupplierLinking> getSupplierLinkingList() {
        return supplierLinkingList;
    }

    public void setSupplierLinkingList(List<SupplierLinking> supplierLinkingList) {
        this.supplierLinkingList = supplierLinkingList;
    }

    public List<Medicine_modal> getSupplierItemLinkingList() {
        return supplierItemLinkingList;
    }

    public void setSupplierItemLinkingList(List<Medicine_modal> supplierItemLinkingList) {
        this.supplierItemLinkingList = supplierItemLinkingList;
    }

    public List<CompanyModel> getSupplierCompDetailList() {
        return supplierCompDetailList;
    }

    public void setSupplierCompDetailList(List<CompanyModel> supplierCompDetailList) {
        this.supplierCompDetailList = supplierCompDetailList;
    }

}
}
