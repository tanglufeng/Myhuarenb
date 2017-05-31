package com.abcs.haiwaigou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zjz on 2016/3/29.
 */
public class HaiwaigouDB extends SQLiteOpenHelper {


    private static final String HWG_DB_NAME = "haiwaigou.db";
    public static final String TABLE_BANNER = "hqnewsCmd";
    public static final String TABLE_BANNER_AD = "hwgbannerad";
    public static final String TABLE_MUYING = "hwgmuying";
    public static final String TABLE_FOOD = "hwgfood";
    public static final String TABLE_REMAI = "hwgremai";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static final int VERSION = 1;
    private static final String TABLE_BANNER_CREATE = "create table " + TABLE_BANNER + " (id integer primary key autoincrement,url varchar(50))";
    private static final String TABLE_BANNERAD_CREATE = "create table " + TABLE_BANNER_AD + " (id integer primary key autoincrement,url varchar(50))";
    private static final String TABLE_MUYING_CREATE = "create table " + TABLE_MUYING + " (id integer primary key autoincrement,url varchar(50),gid varchar(20))";
    private static final String TABLE_FOOD_CREATE = "create table " + TABLE_FOOD + " (id integer primary key autoincrement,url varchar(50),gid varchar(20))";
    private static final String TABLE_REMAI_CREATE = "create table " + TABLE_REMAI + " (id integer primary key autoincrement,url varchar(50),gid varchar(20),money DOUBLE(20),title varchar(50))";

    public HaiwaigouDB(Context context) {
        super(context, HWG_DB_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_BANNER_CREATE);
        db.execSQL(TABLE_BANNERAD_CREATE);
        db.execSQL(TABLE_MUYING_CREATE);
        db.execSQL(TABLE_FOOD_CREATE);
        db.execSQL(TABLE_REMAI_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
