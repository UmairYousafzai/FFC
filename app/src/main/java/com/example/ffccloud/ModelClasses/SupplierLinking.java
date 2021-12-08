package com.example.ffccloud.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierLinking {


    @SerializedName("Supplier_Link_Id_dtl")
    @Expose
    private Long supplierLinkIdDtl;
    @SerializedName("Supplier_Id")
    @Expose
    private String supplierId;
    @SerializedName("Supplier_Name")
    @Expose
    private String supplierName;
    @SerializedName("Is_Registered")
    @Expose
    private String isRegistered;
    @SerializedName("Address")
    @Expose
    private String address;

    public Long getSupplierLinkIdDtl() {
        return supplierLinkIdDtl;
    }

    public void setSupplierLinkIdDtl(Long supplierLinkIdDtl) {
        this.supplierLinkIdDtl = supplierLinkIdDtl;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
