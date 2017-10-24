package com.example.chris.androidmasters.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chris.androidmasters.ProjectListActivity;
import com.example.chris.androidmasters.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by chris on 20/10/2017.
 */

public class FirebaseNotifications extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Included Data: " + remoteMessage.getData());
        Log.d(TAG, "Key Data: " + remoteMessage.getData().get("key"));

        NewProject("Project Boost",remoteMessage.getNotification().getBody());
    }

    public void NewProject(String title,String message){
        String appname = getResources().getString(R.string.app_name);

        Intent intent = new Intent(this, ProjectListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker(title)
                .setContentTitle("A Project has been Added")
                .setContentText(message)
                .setSmallIcon(R.drawable.vector_rocket)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

//        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(notificationSound);
        Notification notification = builder.build();

//        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
