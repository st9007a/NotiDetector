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
        if(mNotification != null && MainActivity.IS_BLOCK){
            Bundle data = new Bundle();
            data.putString("title", mNotification.extras.getString(Notification.EXTRA_TITLE));
            data.putCharSequence("text", mNotification.extras.getCharSequence(Notification.EXTRA_TEXT));
            data.putCharSequence("subtext", mNotification.extras.getCharSequence(Notification.EXTRA_SUB_TEXT));
            data.putString("pkgname", sbn.getPackageName());
            data.putString("key", sbn.getKey());
            data.putString("tag", sbn.getTag());

            Intent intent = new Intent(MainActivity.INTENT_ACTION_NOTIFICATION);
            intent.putExtras(data);
            sendBroadcast(intent);

            this.cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

}
