package com.example.ffccloud;

import static com.example.ffccloud.utils.CONSTANTS.LOCATION_NOTIFICATION_CHANNEL_ID;
import static com.example.ffccloud.utils.CONSTANTS.MESSAGE_NOTIFICATION_CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channelMessage = new NotificationChannel(
                    MESSAGE_NOTIFICATION_CHANNEL_ID,
                    "Message Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channelMessage.setDescription("This Notification show messages .");
            NotificationChannel channelLocation = new NotificationChannel(
                    LOCATION_NOTIFICATION_CHANNEL_ID,
                    "Message Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channelLocation.setDescription("This Notification show Location .");

            NotificationManager mNotificationManager= getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channelLocation);
            mNotificationManager.createNotificationChannel(channelMessage);

        }

    }
}
