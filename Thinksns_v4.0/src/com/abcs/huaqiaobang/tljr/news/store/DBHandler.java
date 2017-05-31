package com.abcs.huaqiaobang.tljr.news.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "hqnews.db";
    public static final String TABLE_NAME = "hqnewsCmd";
    public static final String TABLE_HOTNEWS = "hotnewsCmd";
    public static final String TABLE_LAYOUTTYPE = "layoutCmd";//首页布局排放
    private static final int VERSION = 4;
    private static final String TABLE_CREATE = "create table " + TABLE_NAME + " (id integer primary key autoincrement,key text,typeName text,cmdContent text,jsonLength TEXT)";
    private static final String TABLE_HNCREATE = "create table " + TABLE_HOTNEWS + " (id integer primary key autoincrement,key text,typeName text,cmdContent text,jsonLength TEXT)";
    private static final String TABLE_CREATE_LAYOUTTYPE = "create table " + TABLE_LAYOUTTYPE + " (id integer primary key autoincrement,key text,cmdContent text,jsonLength TEXT)";

    protected DBHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_HNCREATE);
        db.execSQL(TABLE_CREATE_LAYOUTTYPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("NewsFragment", "upgrade-----");
    }

}




