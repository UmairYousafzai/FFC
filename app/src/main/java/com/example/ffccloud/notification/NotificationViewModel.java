package com.example.ffccloud.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.model.Notification;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private NotificationRepository notificationRepository;


    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationRepository= NotificationRepository.getInstance(application);

    }

    public void insertNotification(Notification notification)
    {
        notificationRepository.insertNotification(notification);
    }



    public void deleteNotification(Notification notification)
    {
        notificationRepository.deleteNotification(notification);
    }

    public void deleteAllNotifications()
    {
        notificationRepository.deleteAllNotifications();
    }

    public LiveData<List<Notification>> getAllNotifications()
    {
        return notificationRepository.getAllNotifications();
    }
}
