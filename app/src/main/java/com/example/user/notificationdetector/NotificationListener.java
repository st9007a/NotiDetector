package com.example.user.notificationdetector;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import static com.example.user.notificationdetector.DbConstants.PKG_NAME;
import static com.example.user.notificationdetector.DbConstants.SUB_TEXT;
import static com.example.user.notificationdetector.DbConstants.TABLE_NAEM;
import static com.example.user.notificationdetector.DbConstants.TEXT;
import static com.example.user.notificationdetector.DbConstants.TITLE;

public class NotificationListener extends NotificationListenerService {

    private NotificationDB db = null;

    @Override
    public void onCreate() {
        super.onCreate();
        db = NotificationDB.getInstance(this);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

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

            if(MainActivity.IS_ACTIVITY_ACT)
                sendBroadcast(intent);
            else insert(data);

            this.cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

    private void insert(Bundle bundle) {
        ContentValues values = new ContentValues();
        values.put(PKG_NAME, bundle.getString("pkgname"));
        values.put(TITLE, bundle.getString("title"));
        values.put(TEXT, bundle.getCharSequence("text").toString());
        values.put(SUB_TEXT, bundle.getCharSequence("subtext").toString());

        db.getWritableDatabase().insert(TABLE_NAEM, null, values);
    }

}
