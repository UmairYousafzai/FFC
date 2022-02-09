package com.example.ffccloud.notification;

import android.content.Context;

import androidx.lifecycle.LiveData;


import com.example.ffccloud.Database.FFC_DAO;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.model.Notification;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationRepository {


    private final FFC_DAO mDao;
    private final LiveData<List<Notification>> notificationList;
    private static NotificationRepository notificationRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private NotificationRepository(Context context) {

        mDao = FfcDatabase.getInstance(context).dao();
        notificationList = mDao.getAllNotifications();
    }

    public static synchronized NotificationRepository getInstance(Context context) {
        if (notificationRepository == null) {
            notificationRepository = new NotificationRepository(context);

        }

        return notificationRepository;
    }

    public void insertNotification(Notification notification) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertNotification(notification);
            }
        });

    }

    public void deleteNotification(Notification notification) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteNotification(notification);
            }
        });

    }

    public Notification getNotification(String senderID, String message) {

        return mDao.getNotification(senderID, message);


    }

    public LiveData<Integer> getNotificationCount() {

        return mDao.getNotificationCount();


    }

    public void deleteAllNotifications() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllNotifications();
            }
        });

    }

    public LiveData<List<Notification>> getAllNotifications() {
        return notificationList;
    }

}
