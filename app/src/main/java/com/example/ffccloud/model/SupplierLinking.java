package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierLinking {


    @SerializedName("Supplier_Link_Id_dtl")
    @Expose
    private String supplierLinkId;
    @SerializedName("Supplier_Id")
    @Expose
    private int supplierId;
    @SerializedName("Supplier_Name")
    @Expose
    private String supplierName;
    @SerializedName("Is_Registered")
    @Expose
    private boolean isRegistered;
    @SerializedName("Address")
    @Expose
    private String address;

    private String Type;
    private String user_Type;

    public String getUser_Type() {
        return user_Type;
    }

    public void setUser_Type(String user_Type) {
        this.user_Type = user_Type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getSupplierLinkId() {
        return supplierLinkId;
    }

    public void setSupplierLinkId(String supplierLinkId) {
        this.supplierLinkId = supplierLinkId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
