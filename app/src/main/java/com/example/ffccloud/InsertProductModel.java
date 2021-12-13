package com.example.ffccloud;

import android.os.Parcel;
import android.os.Parcelable;

public class InsertProductModel implements Parcelable{

    private int Item_Code;
    private int Sale_Order_Id;
    private int Unit_Id;
    private double Qty;
    private double Rate;
    private double Amount;
    private double Disc_Percentage;
    private double Disc_Amount ;
    private double Discounted_Value  ;
    private double ST_Percentage   ;
    private double ST_Amount;
    private double Net_Amount ;
    private String Comments	 ;

    private String titleProduct;
    private String unitProduct;

    public InsertProductModel() {
    }


    protected InsertProductModel(Parcel in) {
        Item_Code = in.readInt();
        Sale_Order_Id = in.readInt();
        Unit_Id = in.readInt();
        Qty = in.readDouble();
        Rate = in.readDouble();
        Amount = in.readDouble();
        Disc_Percentage = in.readDouble();
        Disc_Amount = in.readDouble();
        Discounted_Value = in.readDouble();
        ST_Percentage = in.readDouble();
        ST_Amount = in.readDouble();
        Net_Amount = in.readDouble();
        Comments = in.readString();
        titleProduct = in.readString();
        unitProduct = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Item_Code);
        dest.writeInt(Sale_Order_Id);
        dest.writeInt(Unit_Id);
        dest.writeDouble(Qty);
        dest.writeDouble(Rate);
        dest.writeDouble(Amount);
        dest.writeDouble(Disc_Percentage);
        dest.writeDouble(Disc_Amount);
        dest.writeDouble(Discounted_Value);
        dest.writeDouble(ST_Percentage);
        dest.writeDouble(ST_Amount);
        dest.writeDouble(Net_Amount);
        dest.writeString(Comments);
        dest.writeString(titleProduct);
        dest.writeString(unitProduct);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InsertProductModel> CREATOR = new Creator<InsertProductModel>() {
        @Override
        public InsertProductModel createFromParcel(Parcel in) {
            return new InsertProductModel(in);
        }

        @Override
        public InsertProductModel[] newArray(int size) {
            return new InsertProductModel[size];
        }
    };

    public String getTitleProduct() {
        return titleProduct;
    }

    public void setTitleProduct(String titleProduct) {
        this.titleProduct = titleProduct;
    }

    public String getUnitProduct() {
        return unitProduct;
    }

    public void setUnitProduct(String unitProduct) {
        this.unitProduct = unitProduct;
    }

    public int getItem_Code() {
        return Item_Code;
    }

    public void setItem_Code(int item_Code) {
        Item_Code = item_Code;
    }

    public int getUnit_Id() {
        return Unit_Id;
    }

    public void setUnit_Id(int unit_Id) {
        Unit_Id = unit_Id;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getDisc_Percentage() {
        return Disc_Percentage;
    }

    public void setDisc_Percentage(double disc_Percentage) {
        Disc_Percentage = disc_Percentage;
    }

    public double getDisc_Amount() {
        return Disc_Amount;
    }

    public void setDisc_Amount(double disc_Amount) {
        Disc_Amount = disc_Amount;
    }

    public double getDiscounted_Value() {
        return Discounted_Value;
    }

    public void setDiscounted_Value(double discounted_Value) {
        Discounted_Value = discounted_Value;
    }

    public double getST_Percentage() {
        return ST_Percentage;
    }

    public void setST_Percentage(double ST_Percentage) {
        this.ST_Percentage = ST_Percentage;
    }

    public double getST_Amount() {
        return ST_Amount;
    }

    public void setST_Amount(double ST_Amount) {
        this.ST_Amount = ST_Amount;
    }

    public double getNet_Amount() {
        return Net_Amount;
    }

    public void setNet_Amount(double net_Amount) {
        Net_Amount = net_Amount;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public int getSale_Order_Id() {
        return Sale_Order_Id;
    }

    public void setSale_Order_Id(int sale_Order_Id) {
        Sale_Order_Id = sale_Order_Id;
    }
}
