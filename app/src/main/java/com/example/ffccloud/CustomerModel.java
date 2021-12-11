package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerModel implements  Parcelable {

    private int Company_Id;
    private int Country_Id;

    private int Location_Id;
    private int Project_Id;

    @SerializedName("Supplier_Id")
    private int Supplier_Id;
    private long userID;
    private long salesManID;
    private String UserTypeName;

    @SerializedName("Supplier_Code")
    private String partyCode ;
    @SerializedName("Supplier_Name")
    private String partyName ;
    @SerializedName("Abbreviation")
    private String partyAbbreviation  ;

    private String Focal_Person ;
    private String CNICNo ;

    private String Sales_Tax_No ;
    private String NTN;

    private double Cr_Limit ;
    private double Cr_Limit_Amount  ;
    private boolean Apply_Cr_Limit;

    private String Email ;
    private String EMail_CC ;
    private String EMail_BCC ;

    private String contacts;
    private String Fax_No ;
    private int City_Id ;

    private String Payee ;
    private String Address ;
    private String Instruction ;
    private String Comments ;

    private boolean Is_Company;
    private String Prompt_Type;

    @SerializedName("Mobile_No")
    @Expose
    private String mobileNo;
    private List<ContactPersons> Contact_PersonsList;

    public CustomerModel() {
    }


    protected CustomerModel(Parcel in) {
        Company_Id = in.readInt();
        Country_Id = in.readInt();

        Location_Id = in.readInt();
        Project_Id = in.readInt();
        Supplier_Id = in.readInt();
        userID = in.readLong();
        salesManID = in.readLong();
        UserTypeName = in.readString();
        partyCode = in.readString();
        partyName = in.readString();
        partyAbbreviation = in.readString();
        Focal_Person = in.readString();
        CNICNo = in.readString();
        Sales_Tax_No = in.readString();
        NTN = in.readString();
        Cr_Limit = in.readDouble();
        Cr_Limit_Amount = in.readDouble();
        Apply_Cr_Limit = in.readByte() != 0;
        Email = in.readString();
        EMail_CC = in.readString();
        EMail_BCC = in.readString();
        contacts = in.readString();
        Fax_No = in.readString();
        City_Id = in.readInt();
        Payee = in.readString();
        Address = in.readString();
        Instruction = in.readString();
        Comments = in.readString();
        Is_Company = in.readByte() != 0;
        Prompt_Type = in.readString();
        mobileNo = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };



    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPrompt_Type() {
        return Prompt_Type;
    }

    public void setPrompt_Type(String prompt_Type) {
        Prompt_Type = prompt_Type;
    }

    public List<ContactPersons> getContact_PersonsList() {
        return Contact_PersonsList;
    }

    public void setContact_PersonsList(List<ContactPersons> contact_PersonsList) {
        Contact_PersonsList = contact_PersonsList;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(int country_Id) {
        Country_Id = country_Id;
    }

    public int getLocation_Id() {
        return Location_Id;
    }

    public void setLocation_Id(int location_Id) {
        Location_Id = location_Id;
    }

    public int getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(int project_Id) {
        Project_Id = project_Id;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int partyID) {
        this.Supplier_Id = partyID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(long salesManID) {
        this.salesManID = salesManID;
    }

    public String getUserTypeName() {
        return UserTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        UserTypeName = userTypeName;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyAbbreviation() {
        return partyAbbreviation;
    }

    public void setPartyAbbreviation(String partyAbbreviation) {
        this.partyAbbreviation = partyAbbreviation;
    }

    public String getFocal_Person() {
        return Focal_Person;
    }

    public void setFocal_Person(String focal_Person) {
        Focal_Person = focal_Person;
    }

    public String getCNICNo() {
        return CNICNo;
    }

    public void setCNICNo(String CNICNo) {
        this.CNICNo = CNICNo;
    }

    public String getSales_Tax_No() {
        return Sales_Tax_No;
    }

    public void setSales_Tax_No(String sales_Tax_No) {
        Sales_Tax_No = sales_Tax_No;
    }

    public String getNTN() {
        return NTN;
    }

    public void setNTN(String NTN) {
        this.NTN = NTN;
    }

    public double getCr_Limit() {
        return Cr_Limit;
    }

    public void setCr_Limit(double cr_Limit) {
        Cr_Limit = cr_Limit;
    }

    public double getCr_Limit_Amount() {
        return Cr_Limit_Amount;
    }

    public void setCr_Limit_Amount(double cr_Limit_Amount) {
        Cr_Limit_Amount = cr_Limit_Amount;
    }

    public boolean isApply_Cr_Limit() {
        return Apply_Cr_Limit;
    }

    public void setApply_Cr_Limit(boolean apply_Cr_Limit) {
        Apply_Cr_Limit = apply_Cr_Limit;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEMail_CC() {
        return EMail_CC;
    }

    public void setEMail_CC(String EMail_CC) {
        this.EMail_CC = EMail_CC;
    }

    public String getEMail_BCC() {
        return EMail_BCC;
    }

    public void setEMail_BCC(String EMail_BCC) {
        this.EMail_BCC = EMail_BCC;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getFax_No() {
        return Fax_No;
    }

    public void setFax_No(String fax_No) {
        Fax_No = fax_No;
    }

    public int getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int city_Id) {
        City_Id = city_Id;
    }

    public String getPayee() {
        return Payee;
    }

    public void setPayee(String payee) {
        Payee = payee;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        Instruction = instruction;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public boolean isIs_Company() {
        return Is_Company;
    }

    public void setIs_Company(boolean is_Company) {
        Is_Company = is_Company;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Company_Id);
        dest.writeInt(Country_Id);
        dest.writeInt(Location_Id);
        dest.writeInt(Project_Id);

        dest.writeInt(Supplier_Id);
        dest.writeLong(userID);
        dest.writeLong(salesManID);
        dest.writeString(UserTypeName);
        dest.writeString(partyCode);
        dest.writeString(partyName);
        dest.writeString(partyAbbreviation);
        dest.writeString(Focal_Person);
        dest.writeString(CNICNo);
        dest.writeString(Sales_Tax_No);
        dest.writeString(NTN);
        dest.writeDouble(Cr_Limit);
        dest.writeDouble(Cr_Limit_Amount);
        dest.writeByte((byte) (Apply_Cr_Limit ? 1 : 0));
        dest.writeString(Email);
        dest.writeString(EMail_CC);
        dest.writeString(EMail_BCC);
        dest.writeString(contacts);
        dest.writeString(Fax_No);
        dest.writeInt(City_Id);
        dest.writeString(Payee);
        dest.writeString(Address);
        dest.writeString(Instruction);
        dest.writeString(Comments);
        dest.writeByte((byte) (Is_Company ? 1 : 0));
        dest.writeString(Prompt_Type);
        dest.writeString(mobileNo);
    }
}
