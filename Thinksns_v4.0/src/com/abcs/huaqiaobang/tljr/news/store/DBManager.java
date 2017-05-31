package com.abcs.huaqiaobang.tljr.news.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abcs.huaqiaobang.util.LogUtil;
import com.alibaba.fastjson.JSONObject;

public class DBManager {
    public static final String Tag = "LocalDBManager";
    private DBHandler dbHandler;

    public DBManager(Context context) {
        dbHandler = new DBHandler(context);
    }

    private boolean checkSpeciesLegal(String species) {
        try {
            int num = Integer.valueOf(species);
            if (!(num >= 0)) {
                LogUtil.e(Tag, "==插入到本地数据库前，检测异常，  服务器返回的  species 错误："
                        + species);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LogUtil.e(Tag, "===插入到本地数据库前，检测异常，  服务器返回的  species 错误");
            return false;
        }
        return true;
    }

    /**
     * 清除非法数据: key 带 - 符号 ，如 key = i-1
     */
    public void clearInvalidData() {
        String sql = "    delete from " + DBHandler.TABLE_NAME
                + " where key like '%-%'";

        SQLiteDatabase wDatabase = dbHandler.getWritableDatabase();
        wDatabase.execSQL(sql);
        wDatabase.close();
        dbHandler.close();
    }

    /**
     * @param jsonObject cmd 指令Stirng 转换为的 JsonObject
     * @param typeName
     * @return
     */
    public boolean insertOnceRequestNews(JSONObject jsonObject, String tablename) {
        LogUtil.e("HqssFragment", "开始插入----------------");
        String species = jsonObject.containsKey("species") ? jsonObject.getString("species") : "50";
        String key = jsonObject.containsKey("key") ? jsonObject.getString("key") : "0";

        if (!checkSpeciesLegal(species)) {
            return false;
        }

        if (queryTypeCountByKey(key) != 0) {
            LogUtil.e("HqssFragment", "====== 已存在该新闻,重新覆盖本地数据库 key= " + key + " ,species" + species);
            return false;
        }
        LogUtil.e("HqssFragment", "======     插入本地数据库  key=  " + key + " ,species" + species);


        SQLiteDatabase wDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("key", key);
        contentValues.put("typeName", species);
        contentValues.put("cmdContent", jsonObject.toString());
        contentValues.put("jsonLength", jsonObject.toString().length());
//         boolean flag = wDatabase.insert(DBHandler.TABLE_NAME, null,
//                 contentValues) == -1 ? false : true;
        boolean flag = wDatabase.insert(tablename, null,
                contentValues) == -1 ? false : true;
        wDatabase.close();
        return flag;
    }

    /**
     * 查询 新闻分类名称 的新闻条数
     *
     * @param species
     * @return
     */
    private long queryTypeCountByTypeName(String typeName) {
        long count = -1;
        String sql = "select count(*) from " + DBHandler.TABLE_NAME
                + " where typeName =?";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql, new String[]{typeName});
        if (rawQuery.moveToFirst()) {
            count = rawQuery.getLong(0);
        }
        rawQuery.close();
        rDatabase.close();

        return count;
    }

    /**
     * @param key
     * @return 返回 -1 表示，不存在该 key.
     */
    private long queryTypeCountByKey(String key) {
        long count = 0;
        String sql = "select count(*) from " + DBHandler.TABLE_NAME
                + " where key =?";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql, new String[]{key});
        if (rawQuery.moveToFirst()) {
            count = rawQuery.getLong(0);
        }
        rawQuery.close();
        rDatabase.close();
        return count;
    }

    /**
     * 查询历史新闻
     *
     * @param key
     * @return
     */
    public String queryOld(String key) {
        String content = null;
        // select cmdContent from newsCmd where key <'o800' and key like '%o%'
        // limit 1;
        String sql = "select cmdContent from " + DBHandler.TABLE_NAME
                + " where key < ? and key like ? limit 1";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql,
                new String[]{key, "%" + key.substring(0, 1) + "%"});
        if (rawQuery.moveToFirst()) {
            do {
                content = rawQuery.getString(rawQuery
                        .getColumnIndex("cmdContent"));
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        rDatabase.close();

        return content;
    }

    /**
     * 请求最新新闻。 对本地来说，就是换类型
     *
     * @param species
     * @return
     */
    public String[] queryNew(String typeName, String tablename) {
        String content = null;
        String length = null;
        // select * from newsCmd where species = 0 ORDER BY KEY desc LIMIT 1 ;
        String sql = "select cmdContent ,jsonLength from " + tablename
                + " where typeName = ? ORDER BY key desc LIMIT 1";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql, new String[]{typeName});
        if (rawQuery.moveToFirst()) {
            do {
                content = rawQuery.getString(rawQuery
                        .getColumnIndex("cmdContent"));

                length = rawQuery.getString(rawQuery
                        .getColumnIndex("jsonLength"));

            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        rDatabase.close();

        if (length == null) {
            length = "200";
        }
        String[] n = new String[]{content, length};
        return n;
    }

    //查询布局排放顺序


    public String queryLayout(String from) {


        String content = null;
        String sql = "select * from " + DBHandler.TABLE_LAYOUTTYPE + " where key = ?";
        SQLiteDatabase database = dbHandler.getReadableDatabase();

        Cursor cursor = database.rawQuery(sql, new String[]{from});
        if (cursor.moveToFirst()) {
            do {
                content = cursor.getString(cursor.getColumnIndex("cmdContent"));
            } while (cursor.moveToNext());

        }
        cursor.close();
        database.close();


        return content;
    }

    public boolean insertLayout(String cmd, String from) {


        SQLiteDatabase wDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("key", from);
        contentValues.put("cmdContent", cmd);
        contentValues.put("jsonLength", cmd.length());
//         boolean flag = wDatabase.insert(DBHandler.TABLE_NAME, null,
//                 contentValues) == -1 ? false : true;
        boolean flag = wDatabase.insert(DBHandler.TABLE_LAYOUTTYPE, null,
                contentValues) == -1 ? false : true;
        wDatabase.close();
        Log.i("MainFragment", "插入" + flag);
        return flag;
    }

    public boolean updateLayout(String cmd, String from) {


        SQLiteDatabase wDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


//        contentValues.put("key", from);
        contentValues.put("cmdContent", cmd);
//        contentValues.put("jsonLength", cmd.length());
//         boolean flag = wDatabase.insert(DBHandler.TABLE_NAME, null,
//                 contentValues) == -1 ? false : true;
        boolean flag = wDatabase.update(DBHandler.TABLE_LAYOUTTYPE, contentValues, "key=?", new String[]{from})
                == -1 ? false : true;
        wDatabase.close();
        Log.i("MainFragment", "插入" + flag);
        return flag;
    }
}
  


