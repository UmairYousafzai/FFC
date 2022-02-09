package com.example.ffccloud.PushNotification;


import static com.example.ffccloud.utils.CONSTANTS.LOCATION_NOTIFICATION_TITLE;
import static com.example.ffccloud.utils.CONSTANTS.MESSAGE_NOTIFICATION_CHANNEL_ID;
import static com.example.ffccloud.utils.CONSTANTS.MESSAGE_NOTIFICATION_TITLE;

import android.app.Notification;
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
import com.example.ffccloud.notification.NotificationRepository;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.UserRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private UserRepository userRepository;

    private LocationRequestedUser user;


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
        // notificationId is a unique int for each notification that you must define

        int notificationID = new Random().nextInt(Integer.MAX_VALUE);
        int numberOFNotification=0;



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
        if (title.equals(MESSAGE_NOTIFICATION_TITLE)) {
            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.chatFragment)
                    .setArguments(bundle)
                    .createPendingIntent();
        } else if (title.equals(LOCATION_NOTIFICATION_TITLE)) {
            user.setNotificationID(notificationID);
            addUserToDb(user);
            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.usersListFragment)
                    .createPendingIntent();
        } else {
//            user.setNotificationID(notificationID);
//            addUserToDb(user);

            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.usersListFragment)
                    .createPendingIntent();
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MESSAGE_NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.img_erp_cloud_logo);


        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
        builder.setNumber(numberOFNotification);
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "Your_channel_id";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);
//            builder.setChannelId(channelId);
//        }
//        if (user.getId().equals(SharedPreferenceHelper.getInstance(this).getPreviousUserID())) {
//            notificationID = SharedPreferenceHelper.getInstance(this).getMessageNotificationID();
//        } else {
//            SharedPreferenceHelper.getInstance(this).setPreviousUserID(user.getId());
//            SharedPreferenceHelper.getInstance(this).setMessage_Notification_ID(notificationID);
//
//        }

         notificationID = insertNotification(title, message, notificationID,user.getId());


        mNotificationManager.notify(notificationID, builder.build());
    }

    public void addUserToDb(LocationRequestedUser senderUser) {

        if (!userRepository.isUserExists(senderUser.getEmail())) {


            userRepository.insertUser(senderUser);

        }


    }

    private int insertNotification(String title, String message, int notificationID,String senderId) {


        int number = SharedPreferenceHelper.getInstance(this).getNUMBER_OF_NOTIFICATION() +1;

        SharedPreferenceHelper.getInstance(this).setNUMBER_OF_NOTIFICATION(number);

        NotificationRepository repository= NotificationRepository.getInstance(getApplication());
        com.example.ffccloud.model.Notification notification = repository.getNotification(senderId,title);
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm", Locale.getDefault());
        String formattedTime = dateFormat.format(date);
        if (notification!=null)
        {

            repository.deleteNotification(notification);
            notificationID= notification.getNotificationId();


        }


        notification = new com.example.ffccloud.model.Notification(
                notificationID, title, message, formattedTime,senderId);
        repository.insertNotification(notification);

        return notificationID;
    }

}
