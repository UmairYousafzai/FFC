package com.example.ffccloud.ModelClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "delivery_Mode")
public class DeliveryModeModel {

    @PrimaryKey(autoGenerate = true)
    private int local_db_id;

    @SerializedName("Company_Id")
    @Expose
    private double companyId;
    @SerializedName("Country_Id")
    @Expose
    private double countryId;
    @SerializedName("Delivery_Mode_Id")
    @Expose
    private double deliveryModeId;
    @SerializedName("Delivery_Mode")
    @Expose
    private String deliveryMode;

    public int getLocal_db_id() {
        return local_db_id;
    }

    public void setLocal_db_id(int local_db_id) {
        this.local_db_id = local_db_id;
    }

    public double getCompanyId() {
        return companyId;
    }

    public void setCompanyId(double companyId) {
        this.companyId = companyId;
    }

    public double getCountryId() {
        return countryId;
    }

    public void setCountryId(double countryId) {
        this.countryId = countryId;
    }

    public double getDeliveryModeId() {
        return deliveryModeId;
    }

    public void setDeliveryModeId(double deliveryModeId) {
        this.deliveryModeId = deliveryModeId;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }
}
