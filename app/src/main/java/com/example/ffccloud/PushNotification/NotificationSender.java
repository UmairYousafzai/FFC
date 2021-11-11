package com.example.ffccloud.PushNotification;

public class NotificationSender {
    public Data message;
    public String to;

    public NotificationSender(Data message, String to) {
        this.message = message;
        this.to = to;
    }

    public NotificationSender() {
    }

    public Data getMessage() {
        return message;
    }

    public void setMessage(Data message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
