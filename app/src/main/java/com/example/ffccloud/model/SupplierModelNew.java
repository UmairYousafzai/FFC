package com.example.ffccloud.model;

import com.example.ffccloud.SupplierCompDetail;
import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.SupplierItemLinking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierModelNew {



    private double Company_Id;

    private double Country_Id;

    private double Location_Id;

    private double Project_Id;

    private int Supplier_Id;

    private String Supplier_Code;

    private String Supplier_Name;

    private String Address;

    private String Email;

    private String Phone_No;

    private String Comments;

    private String Instruction;

    private String UserTypeName;

    private String User_Sub_Type;

    private int UserId;

    private String Animals_Main_Type;

    private String Animals_Sub_Type;

    private String No_Of_Animals;

    private int Grade_id;

    private String Size;

    private String Gas_Days;

    private String Monthly_Sale;

    private String Region_Id;

    private String Loc_Cord;
    private String Loc_Cord_Address;
    private boolean Is_update_Loc_Cord;
    private int Classification_Id;
    private int Qualification_Id ;
    private String Gender  ;
    private String Shift_Type   ;
    private String Cell_No   ;
    private String Mobile_No   ;



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


    public String getCell_No() {
        return Cell_No;
    }

    public void setCell_No(String cell_No) {
        Cell_No = cell_No;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

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

    public double getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        this.Company_Id = company_Id;
    }

    public double getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(int country_Id) {
        this.Country_Id = country_Id;
    }

    public double getLocation_Id() {
        return Location_Id;
    }

    public void setLocation_Id(int location_Id) {
        this.Location_Id = location_Id;
    }

    public double getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(int project_Id) {
        this.Project_Id = project_Id;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int supplier_Id) {
        this.Supplier_Id = supplier_Id;
    }

    public String getSupplier_Code() {
        return Supplier_Code;
    }

    public void setSupplier_Code(String supplier_Code) {
        this.Supplier_Code = supplier_Code;
    }

    public String getSupplier_Name() {
        return Supplier_Name;
    }

    public void setSupplier_Name(String supplier_Name) {
        this.Supplier_Name = supplier_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone_No() {
        return Phone_No;
    }

    public void setPhone_No(String phone_No) {
        this.Phone_No = phone_No;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        this.Comments = comments;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        this.Instruction = instruction;
    }

    public String getUserTypeName() {
        return UserTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.UserTypeName = userTypeName;
    }

    public String getUser_Sub_Type() {
        return User_Sub_Type;
    }

    public void setUser_Sub_Type(String user_Sub_Type) {
        this.User_Sub_Type = user_Sub_Type;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }

    public String getAnimals_Main_Type() {
        return Animals_Main_Type;
    }

    public void setAnimals_Main_Type(String animals_Main_Type) {
        this.Animals_Main_Type = animals_Main_Type;
    }

    public String getAnimals_Sub_Type() {
        return Animals_Sub_Type;
    }

    public void setAnimals_Sub_Type(String animals_Sub_Type) {
        this.Animals_Sub_Type = animals_Sub_Type;
    }

    public String getNo_Of_Animals() {
        return No_Of_Animals;
    }

    public void setNo_Of_Animals(String no_Of_Animals) {
        this.No_Of_Animals = no_Of_Animals;
    }

    public int getGrade_id() {
        return Grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.Grade_id = grade_id;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        this.Size = size;
    }

    public String getGas_Days() {
        return Gas_Days;
    }

    public void setGas_Days(String gas_Days) {
        this.Gas_Days = gas_Days;
    }

    public String getMonthly_Sale() {
        return Monthly_Sale;
    }

    public void setMonthly_Sale(String monthly_Sale) {
        this.Monthly_Sale = monthly_Sale;
    }

    public String getRegion_Id() {
        return Region_Id;
    }

    public void setRegion_Id(String region_Id) {
        this.Region_Id = region_Id;
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

