package com.example.administrator.books;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class DbTools extends SQLiteOpenHelper {
    public DbTools(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void update(String table, Map<String, Object> map, String column, Object value) {
        String sql = SqlUtil.update(table, map, column, value);
        Log.i("MyInfo_sql_update", sql);
        getWritableDatabase().execSQL(sql);
    }

    public void insert(String table, Map<String, Object> map) {
        String sql = SqlUtil.insert(table, map);
        Log.i("MyInfo_sql", sql);
        getWritableDatabase().execSQL(sql);

    }

    public int getKeyWordCount(String table, String col, String key) {
        Cursor cursor;
        if (col == null || key == null)
            cursor = getReadableDatabase().query(table, null, null, null, null, null, null);
        else
            cursor = getReadableDatabase().query(table, new String[]{col}, col + " like '%" + key + "%'", null, null, null, null);

        return cursor.getCount();

    }

    public void delete(String table, String column, Object value) {

        String sql = SqlUtil.delete(table, column, value);
        Log.i("MyInfo_sql", sql);
        getWritableDatabase().execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        List<String> sqls = DbConstance.sqls;
        if (sqls.size() > 0) {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
