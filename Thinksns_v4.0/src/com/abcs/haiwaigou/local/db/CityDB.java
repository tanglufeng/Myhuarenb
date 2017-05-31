package com.abcs.haiwaigou.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zjz on 2016/10/19.
 */

public class CityDB extends SQLiteOpenHelper {
    //数据库名
    static String name = "recent_city.db";
    //游标工厂
    static SQLiteDatabase.CursorFactory factory = null;
    //数据库版本
    static int version = 1;

    public CityDB(Context context) {
        super(context, name, factory, version);
    }

    public static final String TABLE_NAME = "city";
    public static final String ID = "id";
    public static final String COUNTRY_CN_NAME = "countryName";
    public static final String COUNTRY_ID = "countryId";
    public static final String CITY_NAME = "cityName";
    public static final String CITY_EN_NAME = "cityEnName";
    public static final String CITY_ID = "cityId";
    public static final String AREA_ID = "areaId";
    public static final String TIME = "time";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create Table if not exists " + TABLE_NAME + " ( "
                + ID + " integer primary key autoincrement,"
                + COUNTRY_CN_NAME + " varchar not null,"
                + COUNTRY_ID + " varchar,"
                + CITY_NAME + " varchar,"
                + CITY_EN_NAME + " varchar,"
                + CITY_ID + " varchar,"
                + AREA_ID + " varchar,"
                + TIME + "varchar);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
