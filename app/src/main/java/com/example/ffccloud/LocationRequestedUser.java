package com.example.ffccloud;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LocationRequestedUser")
public class LocationRequestedUser {

    @PrimaryKey(autoGenerate = true)
    private int id_db;

    private String id;
    private String email;
    private String lastLocation;
    private String userName;
    private int sharingState;
    private int notificationID;

    public LocationRequestedUser(String id, String email, String userName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
    }

    public LocationRequestedUser(int id_db, String id, String email, String lastLocation, String userName, int sharingState) {

        this.id_db = id_db;
        this.id = id;
        this.email = email;
        this.lastLocation = lastLocation;
        this.userName = userName;
        this.sharingState = sharingState;
    }

    public LocationRequestedUser() {
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getSharingState() {
        return sharingState;
    }

    public void setSharingState(int sharingState) {
        this.sharingState = sharingState;
    }

    public int getId_db() {
        return id_db;
    }

    public void setId_db(int id_db) {
        this.id_db = id_db;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
