package com.example.user.notificationdetector;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class INotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        if(mNotification != null){
            Bundle extras = mNotification.extras;
            Intent intent = new Intent(MainActivity.INTENT_ACTION_NOTIFICATION);
            intent.putExtras(extras);
            sendBroadcast(intent);

            this.cancelNotification(sbn.getKey());

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

}
