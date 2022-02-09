package com.example.ffccloud.model;

public class TermAndConditionModel {

    private int Sale_Order_Terms_Id;
    private int Sale_Order_Id ;
    private String Terms_Details  ;

    public int getSale_Order_Terms_Id() {
        return Sale_Order_Terms_Id;
    }

    public void setSale_Order_Terms_Id(int sale_Order_Terms_Id) {
        Sale_Order_Terms_Id = sale_Order_Terms_Id;
    }

    public int getSale_Order_Id() {
        return Sale_Order_Id;
    }

    public void setSale_Order_Id(int sale_Order_Id) {
        Sale_Order_Id = sale_Order_Id;
    }

    public String getTerms_Details() {
        return Terms_Details;
    }

    public void setTerms_Details(String terms_Details) {
        Terms_Details = terms_Details;
    }
}
