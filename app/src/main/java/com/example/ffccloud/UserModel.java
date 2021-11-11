package com.example.ffccloud;

import java.util.HashMap;
import java.util.Map;

public class UserModel {


    protected String id;
    protected String Email;
    protected String password;
    protected String lastLocation;
    protected String UserName;
    private String FatherName;
    private String CNIC;
    private boolean requestAccepted;
    private Map<String,String> assignedUsers = new HashMap<>();
    public UserModel() {
    }

    public UserModel(String id, String email, String password, String lastLocation, String userName, String fatherName, String CNIC, boolean requestAccepted, Map<String, String> assignedUsers) {
        this.id = id;
        Email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        UserName = userName;
        FatherName = fatherName;
        this.CNIC = CNIC;
        this.requestAccepted = requestAccepted;
        this.assignedUsers = assignedUsers;
    }



    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public boolean isRequestAccepted() {
        return requestAccepted;
    }

    public void setRequestAccepted(boolean requestAccepted) {
        this.requestAccepted = requestAccepted;
    }

    public Map<String, String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(Map<String, String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }
}
