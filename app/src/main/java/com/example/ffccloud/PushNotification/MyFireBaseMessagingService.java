package com.example.ffccloud.PushNotification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.R;
import com.example.ffccloud.utils.UserRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private UserRepository userRepository;
    private LocationRequestedUser user;
    private String senderID;
    private DatabaseReference databaseReference;


    NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String senderEmail = remoteMessage.getData().get("email");
        String userID = remoteMessage.getData().get("userID");
        String userName = remoteMessage.getData().get("userName");
        userRepository = new UserRepository(getApplication());


        user = new LocationRequestedUser(userID, senderEmail, userName);
        createNotification(title, message);


    }

    public void createNotification(String title, String message) {

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);


        Bundle bundle = new Bundle();
        bundle.putString("recevierID", user.getId());
        PendingIntent pendingIntent;
        if (title.equals("Message")) {
//            pendingIntent = new NavDeepLinkBuilder(this)
//                    .setGraph(R.navigation.nav_graph)
//                    .setDestination(R.id.chatFragment)
//                    .setArguments( bundle)
//                    .createPendingIntent();
        } else if (title.equals("Location Request")) {

            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.usersListFragment)
                    .createPendingIntent();
        } else {
            addUserToDb(user);

            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.usersListFragment)
                    .createPendingIntent();
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        } else {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        }


        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
//        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(100, builder.build());
    }

    public void addUserToDb(LocationRequestedUser senderUser) {

        if (!userRepository.isUserExists(senderUser.getEmail())) {


            userRepository.insertUser(senderUser);

        }


    }

}
