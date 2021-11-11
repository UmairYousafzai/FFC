package com.example.ffccloud.Login;

public class LoginModel {

    private String strEmailAddress;
    private String strPassword;

    public LoginModel(String EmailAddress, String Password) {
        strEmailAddress = EmailAddress;
        strPassword = Password;
    }

    public void setStrEmailAddress(String strEmailAddress) {
        this.strEmailAddress = strEmailAddress;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }
    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public String getStrPassword() {
        return strPassword;
    }


}
