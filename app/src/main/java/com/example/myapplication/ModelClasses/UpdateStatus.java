package com.example.myapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class UpdateStatus {
    @SerializedName("RcKey")
    String RcKey;
    @SerializedName("StrMessage")

    String StrMessage;
    @SerializedName("NotifyMessage")

    String NotifyMessage;
    @SerializedName("CrudNo")

    int CrudNo;
    @SerializedName("Status")

    int Status;
    @SerializedName("ReturnId")

    int ReturnId;

    public String getStrMessage() {
        return StrMessage;
    }

    public int getStatus() {
        return Status;
    }
}
