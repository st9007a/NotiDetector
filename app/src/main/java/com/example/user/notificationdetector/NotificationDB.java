package com.example.user.notificationdetector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.user.notificationdetector.DbConstants.PKG_NAME;
import static com.example.user.notificationdetector.DbConstants.SUB_TEXT;
import static com.example.user.notificationdetector.DbConstants.TABLE_NAEM;
import static com.example.user.notificationdetector.DbConstants.TEXT;
import static com.example.user.notificationdetector.DbConstants.TITLE;


public class NotificationDB extends SQLiteOpenHelper {

    private static NotificationDB mInstance = null;

    private final static String DB_NAME = "notification.db";
    private final static int DB_VERSION = 1;


    private Context mCxt;

    public static NotificationDB getInstance(Context context) {
        if(mInstance == null)
            mInstance = new NotificationDB(context.getApplicationContext());

        return mInstance;
    }

    public NotificationDB (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mCxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String INIT =
                "CREATE TABLE" + TABLE_NAEM + " (" +
                PKG_NAME + " CHAR," +
                TITLE + " CHAR," +
                TEXT + " CHAR,"+
                SUB_TEXT + " CHAR);";

        db.execSQL(INIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAEM;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}
