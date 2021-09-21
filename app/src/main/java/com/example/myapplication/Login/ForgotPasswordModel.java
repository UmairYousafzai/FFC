package com.example.myapplication.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("RcKey")
    @Expose
    private Integer rcKey;
    @SerializedName("StrMessage")
    @Expose
    private String strMessage;
    @SerializedName("NotifyMessage")
    @Expose
    private Object notifyMessage;
    @SerializedName("CrudNo")
    @Expose
    private Integer crudNo;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("ReturnId")
    @Expose
    private Double returnId;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public Integer getRcKey() {
        return rcKey;
    }

    public void setRcKey(Integer rcKey) {
        this.rcKey = rcKey;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public Object getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(Object notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public Integer getCrudNo() {
        return crudNo;
    }

    public void setCrudNo(Integer crudNo) {
        this.crudNo = crudNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getReturnId() {
        return returnId;
    }

    public void setReturnId(Double returnId) {
        this.returnId = returnId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

}
