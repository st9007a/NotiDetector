package com.example.user.notificationdetector;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_ACTION_NOTIFICATION = "it.gmariotti.notification";
    public static String NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static String NOTIFICATION_ACCESS = "enabled_notification_listeners";

    public static boolean IS_BLOCK = false;

    protected  mBroadcastReceiver broadcastReceiver = new mBroadcastReceiver();

    protected CheckBox isblock;

    protected TextView title;
    protected TextView text;
    protected TextView subtext;
    protected TextView pkgname;
    protected TextView key;
    protected TextView tag;

    private CheckBox.OnCheckedChangeListener chklistener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            IS_BLOCK = isblock.isChecked();

            if(!IS_BLOCK) {
                title.setText("Title");
                text.setText("Text");
                subtext.setText("SubText");
                pkgname.setText("PkgName");
                key.setText("Key");
                tag.setText("Tag");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isblock = (CheckBox)findViewById(R.id.isblock);
        isblock.setOnCheckedChangeListener(chklistener);

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        subtext = (TextView)findViewById(R.id.subtext);
        pkgname = (TextView)findViewById(R.id.pkgname);
        key = (TextView)findViewById(R.id.key);
        tag = (TextView)findViewById(R.id.tag);


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
        String access = Settings.Secure.getString(getContentResolver(), NOTIFICATION_ACCESS);
        String pkgName = getApplicationContext().getPackageName();
        return access.contains(pkgName);
    }

    private class mBroadcastReceiver extends BroadcastReceiver{
        public Bundle notification;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                notification = intent.getExtras();
                showNotificationInfo();
            }
        }

        private void showNotificationInfo(){
            title.setText(notification.getString("title"));
            text.setText(notification.getCharSequence("text"));
            subtext.setText(notification.getCharSequence("subtext"));
            pkgname.setText(notification.getString("pkgname"));
            key.setText(notification.getString("key"));
            tag.setText(notification.getString("tag"));
        }

    }

}
