package com.health.keephealth.helper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.health.keephealth.helper.utils.L;
import com.health.keephealth.helper.vo.WeightEntity;

import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();

    private static DBOpenHelper helper;
    private static SQLiteDatabase db;

    private static DBManager instance;

    private static boolean initialized = false;

    private Context mContext;

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private DBManager(Context context) {
        this.mContext = context;
        helper = new DBOpenHelper(mContext);
        db = helper.getWritableDatabase();
        initialized = true;
        L.i(TAG, "initialized done");
    }

    public static void initManger(Context context) {
        if (initialized) {
            L.e(TAG,"DBManager is initialized done,need't init");
            return;
//            throw new IllegalArgumentException("DBManager is initialized done,need't init");
        }
        instance = new DBManager(context);
    }

    public static boolean initialized() {
        return initialized;
    }

    public static void isInitialized() {
        if (!initialized) {
            L.e(TAG, "DBManager is not init");
        }
    }

    public static void insertWeightInfo(WeightEntity entity) {
        try {
            db.beginTransaction();
            db.execSQL("insert into weight_info values (null,?,?,?,datetime('now','localtime'))", new Object[]{entity.getWeight(), dateFormat.format(entity.getAdd_time()), entity.getComment()});
            db.setTransactionSuccessful();
            L.i(TAG, "insert successful");
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "insert error");
        } finally {
            db.endTransaction();
        }
    }

    public static void deleteWeightInfo(int id){
        try {
            db.beginTransaction();
            db.execSQL("delete from weight_info where id=?", new Object[]{id});
            db.setTransactionSuccessful();
            L.i(TAG, "delete successful");
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "delete error");
        } finally {
            db.endTransaction();
        }
    }

    public static void updateWeightInfo(WeightEntity vo) {
        try {
            db.beginTransaction();
            db.execSQL("update weight_info set weight=?,add_time=?,comment=? where id=?", new Object[]{vo.getWeight(), dateFormat.format(vo.getAdd_time()), vo.getComment(), vo.getId()});
            db.setTransactionSuccessful();
            L.i(TAG, "updateWeightInfo successful");
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "updateWeightInfo error");
        } finally {
            db.endTransaction();
        }
    }


    public static Cursor getAllWeigthInfo() {
        Cursor cursor = db.rawQuery("select id as _id,weight,add_time,comment from weight_info order by add_time desc", null);
        return cursor;
    }

    public static List<WeightEntity> getAllWeigthInfos() {
        Cursor cursor = db.rawQuery("select id as _id,weight,add_time,comment from weight_info order by add_time desc", null);
        List<WeightEntity> items = new ArrayList<WeightEntity>();
//        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
        String time = "";
        WeightEntity entity = null;
        while (cursor.moveToNext()) {
            entity = new WeightEntity();
            entity.setId(cursor.getInt(0));
            entity.setWeight(cursor.getFloat(1));
            time = cursor.getString(2);
            try {
                entity.setAdd_time(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            entity.setComment(cursor.getString(3));
            items.add(entity);
        }
        return items;
    }


    public static void destroy() {
        if (db != null) {
            db.close();
            db = null;
        }

        if (helper != null) {
            helper.close();
            helper = null;
        }
        instance = null;
        initialized = false;
        L.i(DBManager.class, "destroy done");
    }
}
