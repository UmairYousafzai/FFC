package com.example.ffccloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierCompDetail {


    @SerializedName("Supplier_Comp_Id_dtl")
    @Expose
    private String supplierCompIdDtl;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Comp_Name")
    @Expose
    private String compName;

    public String getSupplierCompIdDtl() {
        return supplierCompIdDtl;
    }

    public void setSupplierCompIdDtl(String supplierCompIdDtl) {
        this.supplierCompIdDtl = supplierCompIdDtl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }


}
