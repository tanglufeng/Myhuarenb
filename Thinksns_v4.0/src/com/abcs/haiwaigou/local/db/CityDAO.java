package com.abcs.haiwaigou.local.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.abcs.haiwaigou.local.beans.City;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CityDAO {
    Context context;
    CityDB db;

    public CityDAO(Context context) {
        this.context = context;
        db = new CityDB(context);
    }

    public void insert(City city) {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityDB.COUNTRY_CN_NAME, city.getCountryCnName());
        contentValues.put(CityDB.COUNTRY_ID, city.getCountryId());
        contentValues.put(CityDB.CITY_NAME, city.getCate_name());
        contentValues.put(CityDB.CITY_EN_NAME, city.getCatename_en());
        contentValues.put(CityDB.CITY_ID, city.getCity_id());
        contentValues.put(CityDB.AREA_ID, city.getAreaId());
        contentValues.put(CityDB.TIME, String.valueOf(System.currentTimeMillis()));
        //添加方法insert
        sqLiteDatabase.insert(CityDB.TABLE_NAME, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }

    //查询所有方法
    public ArrayList<City> selectByAll() {
        ArrayList<City> temp = new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, CityDB.TABLE_NAME, new String[]{"id,countryName,countryId,cityName,cityEnName,cityId,areaId"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            City city = new City();
            city.setCountryCnName(cursor.getString(1));
            city.setCountryId(cursor.getString(2));
            city.setCate_name(cursor.getString(3));
            city.setCatename_en(cursor.getString(4));
            city.setCity_id(cursor.getString(5));
            city.setAreaId(cursor.getString(6));
//            city.setTime(cursor.getLong(7));
            temp.add(city);
        }
        cursor.close();
        sqLiteDatabase.close();
        return temp;
    }

    //查询前几条
    public ArrayList<City> selectSome() {
        ArrayList<City> temp = new ArrayList();
        SQLiteDatabase recentVisitDb = db.getWritableDatabase();
        Cursor cursor = recentVisitDb.rawQuery("select * from " + CityDB.TABLE_NAME + " order by time desc limit 0, 9",null);
        while (cursor.moveToNext()) {
            City city = new City();
            city.setCountryCnName(cursor.getString(1));
            city.setCountryId(cursor.getString(2));
            city.setCate_name(cursor.getString(3));
            city.setCatename_en(cursor.getString(4));
            city.setCity_id(cursor.getString(5));
            city.setAreaId(cursor.getString(6));
            city.setTime(cursor.getLong(7));
            temp.add(city);
        }
        cursor.close();
        recentVisitDb.close();
        return temp;
    }


    //修改方法
    public void updateOne(City city) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //这里只修改时间
        contentValues.put(CityDB.COUNTRY_CN_NAME, city.getCountryCnName());
        contentValues.put(CityDB.COUNTRY_ID, city.getCountryId());
        contentValues.put(CityDB.CITY_NAME, city.getCate_name());
        contentValues.put(CityDB.CITY_EN_NAME, city.getCatename_en());
        contentValues.put(CityDB.AREA_ID, city.getAreaId());
        contentValues.put(CityDB.TIME, System.currentTimeMillis());
        sqLiteDatabase.update(CityDB.TABLE_NAME, contentValues, "cityId=?", new String[]{city.getCity_id()});
        sqLiteDatabase.close();
    }

    public boolean selectByCityId(String cityId) {
        City temp = new City();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(true, CityDB.TABLE_NAME,null, "cityId=?", new String[]{cityId}, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            temp.setCountryCnName(cursor.getString(1));
        }
        sqLiteDatabase.close();
        if (TextUtils.isEmpty(temp.getCountryCnName()) || temp.getCountryCnName().equals("null"))
            return false;
        else
            return true;
    }

}
