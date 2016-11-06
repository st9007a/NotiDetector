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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_ACTION_NOTIFICATION = "it.gmariotti.notification";
    public static String NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static String NOTIFICATION_ACCESS = "enabled_notification_listeners";

    protected  mBroadcastReceiver broadcastReceiver = new mBroadcastReceiver();

    protected TextView title;
    protected TextView text;
    protected TextView subtext;
    protected TextView pkgname;
    protected TextView key;
    protected TextView tag;

    protected TextView pkglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager p = getPackageManager();
        List<ApplicationInfo> apps = p.getInstalledApplications(0);
        List<ApplicationInfo> installed = new ArrayList<ApplicationInfo>();

        for(ApplicationInfo app : apps){
            if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installed.add(app);
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            } else {
                installed.add(app);
            }
        }

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        subtext = (TextView)findViewById(R.id.subtext);
        pkgname = (TextView)findViewById(R.id.pkgname);
        key = (TextView)findViewById(R.id.key);
        tag = (TextView)findViewById(R.id.tag);
        pkglist = (TextView)findViewById(R.id.pkglist);

        String packageList = "";
        for(int i=0;i<installed.size();i++){
            packageList += installed.get(i).packageName + "\n";
        }

        pkglist.setText(packageList);

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
        String pkgName = getPackageName();
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
