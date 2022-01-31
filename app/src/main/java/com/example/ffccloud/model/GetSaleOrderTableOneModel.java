package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSaleOrderTableOneModel {

    @SerializedName("Sale_Order_Id")
    @Expose
    private double saleOrderId;
    @SerializedName("Sale_Order_Date")
    @Expose
    private String saleOrderDate;
    @SerializedName("Sale_Order_No")
    @Expose
    private String saleOrderNo;
    @SerializedName("Sale_Order_Type")
    @Expose
    private Integer saleOrderType;
    @SerializedName("Direct_Invoice")
    @Expose
    private Boolean directInvoice;
    @SerializedName("Price_List_Id")
    @Expose
    private Object priceListId;
    @SerializedName("Priority_Id")
    @Expose
    private double priorityId;
    @SerializedName("Delivery_Mode_Id")
    @Expose
    private double deliveryModeId;
    @SerializedName("Delivery_Location_Id")
    @Expose
    private double deliveryLocationId;
    @SerializedName("Delivery_Date")
    @Expose
    private String deliveryDate;
    @SerializedName("Saleman_Commission")
    @Expose
    private double salemanCommission;
    @SerializedName("Salesman_ID")
    @Expose
    private double salesmanID;
    @SerializedName("Supplier_Id")
    @Expose
    private double supplierId;
    @SerializedName("Ledger_Balance")
    @Expose
    private double ledgerBalance;
    @SerializedName("Credit_Limit")
    @Expose
    private double creditLimit;
    @SerializedName("Credit_Limit_Apply")
    @Expose
    private Boolean creditLimitApply;
    @SerializedName("Sales_Location_Id")
    @Expose
    private double salesLocationId;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Post")
    @Expose
    private Boolean post;
    @SerializedName("IsClose")
    @Expose
    private Object isClose;
    @SerializedName("Cancel")
    @Expose
    private Object cancel;
    @SerializedName("Amount_Type")
    @Expose
    private Integer amountType;
    @SerializedName("Delivery_Location")
    @Expose
    private String deliveryLocation;
    @SerializedName("Request_Type")
    @Expose
    private Integer requestType;
    @SerializedName("Currency_Id")
    @Expose
    private Integer currencyId;

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

    public Integer getSaleOrderType() {
        return saleOrderType;
    }

    public void setSaleOrderType(Integer saleOrderType) {
        this.saleOrderType = saleOrderType;
    }

    public Boolean getDirectInvoice() {
        return directInvoice;
    }

    public void setDirectInvoice(Boolean directInvoice) {
        this.directInvoice = directInvoice;
    }

    public Object getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(Object priceListId) {
        this.priceListId = priceListId;
    }

    public double getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(double priorityId) {
        this.priorityId = priorityId;
    }

    public double getDeliveryModeId() {
        return deliveryModeId;
    }

    public void setDeliveryModeId(double deliveryModeId) {
        this.deliveryModeId = deliveryModeId;
    }

    public double getDeliveryLocationId() {
        return deliveryLocationId;
    }

    public void setDeliveryLocationId(double deliveryLocationId) {
        this.deliveryLocationId = deliveryLocationId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getSalemanCommission() {
        return salemanCommission;
    }

    public void setSalemanCommission(double salemanCommission) {
        this.salemanCommission = salemanCommission;
    }

    public double getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(double salesmanID) {
        this.salesmanID = salesmanID;
    }

    public double getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(double supplierId) {
        this.supplierId = supplierId;
    }

    public double getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(double ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getCreditLimitApply() {
        return creditLimitApply;
    }

    public void setCreditLimitApply(Boolean creditLimitApply) {
        this.creditLimitApply = creditLimitApply;
    }

    public double getSalesLocationId() {
        return salesLocationId;
    }

    public void setSalesLocationId(double salesLocationId) {
        this.salesLocationId = salesLocationId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getPost() {
        return post;
    }

    public void setPost(Boolean post) {
        this.post = post;
    }

    public Object getIsClose() {
        return isClose;
    }

    public void setIsClose(Object isClose) {
        this.isClose = isClose;
    }

    public Object getCancel() {
        return cancel;
    }

    public void setCancel(Object cancel) {
        this.cancel = cancel;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
