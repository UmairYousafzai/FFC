package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

public class GetSupplierModel implements Parcelable {

    private int Supplier_Id;
    private String supplierLinkId;
    private String Supplier_Name;
    private String Supplier_Code;
    private String Address;
    private boolean isRegister;

    public GetSupplierModel() {
    }

    public GetSupplierModel(int supplier_Id, String supplierLinkId, String supplier_Name, String supplier_Code, String address, boolean isRegister) {
        Supplier_Id = supplier_Id;
        this.supplierLinkId = supplierLinkId;
        Supplier_Name = supplier_Name;
        Supplier_Code = supplier_Code;
        Address = address;
        this.isRegister = isRegister;
    }

    protected GetSupplierModel(Parcel in) {
        Supplier_Id = in.readInt();
        supplierLinkId = in.readString();
        Supplier_Name = in.readString();
        Supplier_Code = in.readString();
        Address = in.readString();
        isRegister = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Supplier_Id);
        dest.writeString(supplierLinkId);
        dest.writeString(Supplier_Name);
        dest.writeString(Supplier_Code);
        dest.writeString(Address);
        dest.writeByte((byte) (isRegister ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetSupplierModel> CREATOR = new Creator<GetSupplierModel>() {
        @Override
        public GetSupplierModel createFromParcel(Parcel in) {
            return new GetSupplierModel(in);
        }

        @Override
        public GetSupplierModel[] newArray(int size) {
            return new GetSupplierModel[size];
        }
    };

    public String getSupplier_Code() {
        return Supplier_Code;
    }

    public void setSupplier_Code(String supplier_Code) {
        Supplier_Code = supplier_Code;
    }

    public String getSupplierLinkId() {
        return supplierLinkId;
    }

    public void setSupplierLinkId(String supplierLinkId) {
        this.supplierLinkId = supplierLinkId;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int supplier_Id) {
        Supplier_Id = supplier_Id;
    }

    public String getSupplier_Name() {
        return Supplier_Name;
    }

    public void setSupplier_Name(String supplier_Name) {
        Supplier_Name = supplier_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


}
