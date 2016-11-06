package com.example.user.notificationdetector;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_ACTION_NOTIFICATION = "it.gmariotti.notification";
    public static String NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static String NOTIFICATION_ACCESS = "enabled_notification_listeners";

    protected  mBroadcastReceiver broadcastReceiver = new mBroadcastReceiver();

    protected TextView title;
    protected TextView text;
    protected TextView subtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        subtext = (TextView)findViewById(R.id.subtext);

        if(!isNotificationAccessEnabled()){
            Intent intent = new Intent(NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null)
            broadcastReceiver = new mBroadcastReceiver();

        registerReceiver(broadcastReceiver, new IntentFilter(INTENT_ACTION_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private boolean isNotificationAccessEnabled(){
        String access = Settings.Secure.getString(getApplicationContext().getContentResolver(), NOTIFICATION_ACCESS);
        String pkgName = getApplicationContext().getPackageName();
        return access.contains(pkgName);
    }

    private class mBroadcastReceiver extends BroadcastReceiver{
        public Bundle notification;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Bundle extras = intent.getExtras();

                notification = new Bundle();
                notification.putString("title", extras.getString(Notification.EXTRA_TITLE));
                notification.putCharSequence("text", extras.getCharSequence(Notification.EXTRA_TEXT));
                notification.putCharSequence("subtext", extras.getCharSequence(Notification.EXTRA_SUB_TEXT));

                showNotificationInfo();
            }
        }

        private void showNotificationInfo(){
            title.setText(notification.getString("title"));
            text.setText(notification.getCharSequence("text"));
            subtext.setText(notification.getCharSequence("subtext"));
        }

    }

}
