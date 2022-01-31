package com.example.ffccloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateListModel {

    @SerializedName("Price_List_Id")
    @Expose
    private double priceListId;
    @SerializedName("Price_List_Code")
    @Expose
    private String priceListCode;
    @SerializedName("It_Code")
    @Expose
    private double itCode;
    @SerializedName("Sale_Rate")
    @Expose
    private double saleRate;
    @SerializedName("Disc_Percentage")
    @Expose
    private double discPercentage;
    @SerializedName("IsReg")
    @Expose
    private int isReg;
    @SerializedName("ST_Percnt")
    @Expose
    private double sTPercnt;
    @SerializedName("ST_UR_Percnt")
    @Expose
    private double sTURPercnt;
    @SerializedName("DisOffer_id")
    @Expose
    private double disOfferId;
    @SerializedName("Dis_Offer")
    @Expose
    private double disOffer;
    @SerializedName("bonus_adj")
    @Expose
    private double bonusAdj;
    @SerializedName("bonus_adj_amt")
    @Expose
    private double bonusAdjAmt;
    @SerializedName("Price_list_dis")
    @Expose
    private double priceListDis;
    @SerializedName("Price_list_dis_amt")
    @Expose
    private double priceListDisAmt;
    @SerializedName("bonus_adj_per")
    @Expose
    private double bonusAdjPer;

    public double getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(double priceListId) {
        this.priceListId = priceListId;
    }

    public String getPriceListCode() {
        return priceListCode;
    }

    public void setPriceListCode(String priceListCode) {
        this.priceListCode = priceListCode;
    }

    public double getItCode() {
        return itCode;
    }

    public void setItCode(double itCode) {
        this.itCode = itCode;
    }

    public double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(double saleRate) {
        this.saleRate = saleRate;
    }

    public double getDiscPercentage() {
        return discPercentage;
    }

    public void setDiscPercentage(double discPercentage) {
        this.discPercentage = discPercentage;
    }

    public int getIsReg() {
        return isReg;
    }

    public void setIsReg(int isReg) {
        this.isReg = isReg;
    }

    public double getsTPercnt() {
        return sTPercnt;
    }

    public void setsTPercnt(double sTPercnt) {
        this.sTPercnt = sTPercnt;
    }

    public double getsTURPercnt() {
        return sTURPercnt;
    }

    public void setsTURPercnt(double sTURPercnt) {
        this.sTURPercnt = sTURPercnt;
    }

    public double getDisOfferId() {
        return disOfferId;
    }

    public void setDisOfferId(double disOfferId) {
        this.disOfferId = disOfferId;
    }

    public double getDisOffer() {
        return disOffer;
    }

    public void setDisOffer(double disOffer) {
        this.disOffer = disOffer;
    }

    public double getBonusAdj() {
        return bonusAdj;
    }

    public void setBonusAdj(double bonusAdj) {
        this.bonusAdj = bonusAdj;
    }

    public double getBonusAdjAmt() {
        return bonusAdjAmt;
    }

    public void setBonusAdjAmt(double bonusAdjAmt) {
        this.bonusAdjAmt = bonusAdjAmt;
    }

    public double getPriceListDis() {
        return priceListDis;
    }

    public void setPriceListDis(double priceListDis) {
        this.priceListDis = priceListDis;
    }

    public double getPriceListDisAmt() {
        return priceListDisAmt;
    }

    public void setPriceListDisAmt(double priceListDisAmt) {
        this.priceListDisAmt = priceListDisAmt;
    }

    public double getBonusAdjPer() {
        return bonusAdjPer;
    }

    public void setBonusAdjPer(double bonusAdjPer) {
        this.bonusAdjPer = bonusAdjPer;
    }
}
