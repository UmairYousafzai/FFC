package com.example.ffccloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSaleOrderModel {


    @SerializedName("sno")
    @Expose
    private double sno;
    @SerializedName("Sale_Order_Id")
    @Expose
    private double saleOrderId;
    @SerializedName("Sale_Order_Date")
    @Expose
    private String saleOrderDate;
    @SerializedName("Sale_Order_No")
    @Expose
    private String saleOrderNo;
    @SerializedName("Post")
    @Expose
    private boolean post;
    @SerializedName("IsClose")
    @Expose
    private boolean isClose;
    @SerializedName("Cancel")
    @Expose
    private boolean cancel;
    @SerializedName("Minute")
    @Expose
    private int minute;
    @SerializedName("Hours")
    @Expose
    private int hours;
    @SerializedName("Days")
    @Expose
    private int days;
    @SerializedName("Week")
    @Expose
    private int week;
    @SerializedName("Supplier_Id")
    @Expose
    private double supplierId;
    @SerializedName("Supplier_Name")
    @Expose
    private String supplierName;
    @SerializedName("Net_Amount")
    @Expose
    private double netAmount;
    @SerializedName("Cancelled")
    @Expose
    private int cancelled;
    @SerializedName("Closed")
    @Expose
    private int closed;
    @SerializedName("Posted")
    @Expose
    private int posted;
    @SerializedName("UnPosted")
    @Expose
    private int unPosted;

    public double getSno() {
        return sno;
    }

    public void setSno(double sno) {
        this.sno = sno;
    }

    public double getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(double saleOrderId) {
        this.saleOrderId = saleOrderId;
    }

    public String getSaleOrderDate() {
        return saleOrderDate;
    }

    public void setSaleOrderDate(String saleOrderDate) {
        this.saleOrderDate = saleOrderDate;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public boolean getPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setIsClose(boolean isClose) {
        this.isClose = isClose;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public double getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(double supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getPosted() {
        return posted;
    }

    public void setPosted(int posted) {
        this.posted = posted;
    }

    public int getUnPosted() {
        return unPosted;
    }

    public void setUnPosted(int unPosted) {
        this.unPosted = unPosted;
    }
}
