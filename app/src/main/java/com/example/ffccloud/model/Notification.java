package com.example.ffccloud.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notification {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private int notificationId;

    private String notificationTitle;

    private String notificationMessage;
    private String time;
    private String senderId;

    public Notification() {
    }

    public Notification(int notificationId, String notificationTitle, String notificationMessage, String time, String senderId) {
        this.notificationId = notificationId;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.time = time;
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
}
