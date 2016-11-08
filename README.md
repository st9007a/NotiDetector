# Notification Detector

### Development

+ Android Studio
+ API 21 (V5.0)

### Notification Listener Service

##### 1. Get the permission

+ Write the follow in AndroidManifest.xml
+ Change **android:name** and **android:label** to meet your project
+ Reference : 
    + [Android Serveice](https://developer.android.com/guide/topics/manifest/service-element.html?hl=zh-tw)
    + [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService.html)
    + [Github: NotificationListener](https://github.com/gabrielemariotti/androiddev/tree/master/NotificationListener44)

```xml
<service 
    android:name="[@subclass_name implements the service]"
    android:label="[@service_name show for user]"
    android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
    <intent-filter>
        <action android:name="android.service.notification.NotificationListenerService" />
    </intent-filter>
</service>
```

##### 2. Extends **NotificationListenerService** class

+ Create a class to extend NotificationListenerService
+ Override two methods : **onNotificationPosted** , **onNotificationRemoved**

```java
public class INotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //called when get a notification
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //called when remove a notification
    }
}
```

