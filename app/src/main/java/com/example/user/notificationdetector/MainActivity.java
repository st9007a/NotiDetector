package com.example.user.notificationdetector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_ACTION_NOTIFICATION = "user.notificationdetector";
    public static String NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static String NOTIFICATION_ACCESS = "enabled_notification_listeners";
    public static boolean IS_BLOCK = false;

    protected CheckBox isBlock;

    protected TextView title;
    protected TextView text;
    protected TextView subText;
    protected TextView pkgName;
    protected TextView key;
    protected TextView tag;
    protected TextView count;

    protected  mBroadcastReceiver broadcastReceiver = new mBroadcastReceiver();
    private CheckBox.OnCheckedChangeListener checkBlock = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            IS_BLOCK = isBlock.isChecked();

            if(!IS_BLOCK) {
                title.setText("Title");
                text.setText("Text");
                subText.setText("SubText");
                pkgName.setText("PkgName");
                key.setText("Key");
                tag.setText("Tag");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isBlock = (CheckBox)findViewById(R.id.isblock);
        isBlock.setOnCheckedChangeListener(checkBlock);

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        subText = (TextView)findViewById(R.id.subtext);
        pkgName = (TextView)findViewById(R.id.pkgname);
        key = (TextView)findViewById(R.id.key);
        tag = (TextView)findViewById(R.id.tag);
        count = (TextView)findViewById(R.id.count);

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

        SharedPreferences settings = getApplicationContext().getSharedPreferences("message", Activity.MODE_PRIVATE);
        text.setText(settings.getString("text", "123"));
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
            subText.setText(notification.getCharSequence("subtext"));
            pkgName.setText(notification.getString("pkgname"));
            key.setText(notification.getString("key"));
            tag.setText(notification.getString("tag"));
        }
    }

}
