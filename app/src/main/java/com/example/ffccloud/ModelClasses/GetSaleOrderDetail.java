package com.example.ffccloud.ModelClasses;

import com.example.ffccloud.InsertProductModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSaleOrderDetail {

    @SerializedName("Table")
    @Expose
    private List<GetSaleOrderTableOneModel> table = null;
    @SerializedName("Table1")
    @Expose
    private List<InsertProductModel> table1 = null;

    @SerializedName("Table3")
    @Expose
    private List<TermAndConditionModel> table3 = null;

    public List<GetSaleOrderTableOneModel> getTable() {
        return table;
    }

    public void setTable(List<GetSaleOrderTableOneModel> table) {
        this.table = table;
    }

    public List<InsertProductModel> getTable1() {
        return table1;
    }

    public void setTable1(List<InsertProductModel> table1) {
        this.table1 = table1;
    }



    public List<TermAndConditionModel> getTable3() {
        return table3;
    }

    public void setTable3(List<TermAndConditionModel> table3) {
        this.table3 = table3;
    }
}
