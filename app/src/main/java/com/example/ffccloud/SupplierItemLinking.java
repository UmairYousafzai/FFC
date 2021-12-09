package com.example.ffccloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierItemLinking {

    @SerializedName("Supplier_Item_Link_Id_dtl")
    @Expose
    private String supplierItemLinkIdDtl;
    @SerializedName("It_Code")
    @Expose
    private int itCode;
    @SerializedName("It_Head")
    @Expose
    private String itHead;
    @SerializedName("Is_Registered")
    @Expose
    private boolean isRegistered;
    @SerializedName("Comp_Name")
    @Expose
    private String compName;

    public String getSupplierItemLinkIdDtl() {
        return supplierItemLinkIdDtl;
    }

    public void setSupplierItemLinkIdDtl(String supplierItemLinkIdDtl) {
        this.supplierItemLinkIdDtl = supplierItemLinkIdDtl;
    }

    public int getItCode() {
        return itCode;
    }

    public void setItCode(int itCode) {
        this.itCode = itCode;
    }

    public String getItHead() {
        return itHead;
    }

    public void setItHead(String itHead) {
        this.itHead = itHead;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
