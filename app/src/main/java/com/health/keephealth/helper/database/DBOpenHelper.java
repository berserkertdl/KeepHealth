package com.health.keephealth.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.health.keephealth.helper.utils.L;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class DBOpenHelper extends SQLiteOpenHelper {


    private final static String DB_NAME = "healthDB.db";
    private static int DEFAULT_DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DB_NAME, null, DEFAULT_DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weight_info (" +
                "id integer PRIMARY KEY autoincrement not null," +
                "weight float default 0," +
                "add_time timestamp default (datetime('now','localtime'))," +
                "comment text," +
                "update_time timestamp )");
        L.i(DBOpenHelper.class,"create table successful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.i(DBOpenHelper.class,"update db successful");
    }
}
