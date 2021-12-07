package com.example.ffccloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medicine_modal {

    @SerializedName("Supplier_Item_Link_Id_dtl")
    @Expose
    private Long supplierItemLinkIdDtl;
    @SerializedName("It_Code")
    @Expose
    private String itCode;
    @SerializedName("It_Head")
    @Expose
    private String itHead;
    @SerializedName("Is_Registered")
    @Expose
    private String isRegistered;
    @SerializedName("Comp_Name")
    @Expose
    private String compName;

    public Long getSupplierItemLinkIdDtl() {
        return supplierItemLinkIdDtl;
    }

    public void setSupplierItemLinkIdDtl(Long supplierItemLinkIdDtl) {
        this.supplierItemLinkIdDtl = supplierItemLinkIdDtl;
    }

    public String getItCode() {
        return itCode;
    }

    public void setItCode(String itCode) {
        this.itCode = itCode;
    }

    public String getItHead() {
        return itHead;
    }

    public void setItHead(String itHead) {
        this.itHead = itHead;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
