package com.abcs.haiwaigou.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abcs.haiwaigou.model.Goods;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/3/29.
 */
public class HaiwaigouDAO {
    //需要上下文
    Context context;
    //需要Db对象
    HaiwaigouDB db;
    //使用构造传递上下文，和实例化Db对象
    //Db对象需要依赖上下文
    public HaiwaigouDAO(Context context) {
        this.context = context;
        db = new HaiwaigouDB(context);
    }

    //添加方法
    public void insertBanner(Goods goods)
    {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",goods.getPicarr());
        //添加方法insert
        sqLiteDatabase.insert(HaiwaigouDB.TABLE_BANNER, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }

    public void insertBannerAD(Goods goods)
    {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",goods.getPicarr());
        //添加方法insert
        sqLiteDatabase.insert(HaiwaigouDB.TABLE_BANNER_AD, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }

    public void insertMuYing(Goods goods)
    {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        //添加方法insert
        sqLiteDatabase.insert(HaiwaigouDB.TABLE_MUYING, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }

    public void insertFood(Goods goods)
    {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        //添加方法insert
        sqLiteDatabase.insert(HaiwaigouDB.TABLE_FOOD, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }
    public void insertRemai(Goods goods)
    {
        //获得SQLiteDatabase对象
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //值对象ContentValues,用于添加和修改
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        contentValues.put("money",goods.getMoney());
        contentValues.put("title",goods.getTitle());
        //添加方法insert
        sqLiteDatabase.insert(HaiwaigouDB.TABLE_REMAI, null, contentValues);
        //关闭
        sqLiteDatabase.close();
    }


    //删除方法
    public void delBanner(int id)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        //第二个参数为查询的条件,id为字段名，?为占位符
        //第三个参数?占位符的值
        //返回值Wie影响行数
        int i=  sqLiteDatabase.delete(HaiwaigouDB.TABLE_BANNER,"id=?",new String[]{String.valueOf(id)} );
        Log.i("zjz", "i:" + i);
        sqLiteDatabase.close();
    }
    //修改方法
    public void updateBanner(Goods goods)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //这里只修改内容，不修改日期
        contentValues.put("url",goods.getPicarr());
        sqLiteDatabase.update(HaiwaigouDB.TABLE_BANNER, contentValues, "id=?", new String[]{String.valueOf(goods.getId())});
        sqLiteDatabase.close();
    }
    public void updateBannerAd(Goods goods)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //这里只修改内容，不修改日期
        contentValues.put("url",goods.getPicarr());
        sqLiteDatabase.update(HaiwaigouDB.TABLE_BANNER_AD,contentValues,"id=?",new String[]{String.valueOf(goods.getId())});
        sqLiteDatabase.close();
    }
    public void updateMuYing(Goods goods)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //这里只修改内容，不修改日期
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        sqLiteDatabase.update(HaiwaigouDB.TABLE_MUYING,contentValues,"gid=?",new String[]{String.valueOf(goods.getGoods_id())});
        sqLiteDatabase.close();
    }
    public void updateFood(Goods goods)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //这里只修改内容，不修改日期
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        sqLiteDatabase.update(HaiwaigouDB.TABLE_FOOD,contentValues,"gid=?", new String[]{String.valueOf(goods.getGoods_id())});
        sqLiteDatabase.close();
    }
    public void updateRemai(Goods goods)
    {
        SQLiteDatabase sqLiteDatabase=  db.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //这里只修改内容，不修改日期
        contentValues.put("url",goods.getPicarr());
        contentValues.put("gid",goods.getGoods_id());
        contentValues.put("money",goods.getMoney());
        contentValues.put("title",goods.getTitle());
        sqLiteDatabase.update(HaiwaigouDB.TABLE_REMAI,contentValues,"gid=?",new String[]{String.valueOf(goods.getGoods_id())});
        sqLiteDatabase.close();
    }
    //查询所有方法
    public ArrayList<Goods> selectByAllBanner() {
        ArrayList<Goods> temp=new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, HaiwaigouDB.TABLE_BANNER, new String[]{"id,url"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            Goods goods = new Goods();
            goods.setId(cursor.getInt(0));
            goods.setPicarr(cursor.getString(1));
            temp.add(goods);
        }
        sqLiteDatabase.close();
        return  temp;
    }
    public ArrayList<Goods> selectByAllBannerAd() {
        ArrayList<Goods> temp=new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, HaiwaigouDB.TABLE_BANNER_AD, new String[]{"id,url"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            Goods goods = new Goods();
            goods.setId(cursor.getInt(0));
            goods.setPicarr(cursor.getString(1));
            temp.add(goods);
        }
        sqLiteDatabase.close();
        return  temp;
    }
    public ArrayList<Goods> selectByAllMuYing() {
        ArrayList<Goods> temp=new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, HaiwaigouDB.TABLE_MUYING, new String[]{"id,url,gid"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            Goods goods = new Goods();
            goods.setId(cursor.getInt(0));
            goods.setPicarr(cursor.getString(1));
            goods.setGoods_id(cursor.getString(2));
            temp.add(goods);
        }
        sqLiteDatabase.close();
        return  temp;
    }
    public ArrayList<Goods> selectByAllFood() {
        ArrayList<Goods> temp=new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, HaiwaigouDB.TABLE_FOOD, new String[]{"id,url,gid"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            Goods goods = new Goods();
            goods.setId(cursor.getInt(0));
            goods.setPicarr(cursor.getString(1));
            goods.setGoods_id(cursor.getString(2));
            temp.add(goods);
        }
        sqLiteDatabase.close();
        return  temp;
    }
    public ArrayList<Goods> selectByAllRemai() {
        ArrayList<Goods> temp=new ArrayList();
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        //使用SQLiteDatabase对象的query方法获取Cursor对象
        Cursor cursor = sqLiteDatabase.query(true, HaiwaigouDB.TABLE_REMAI, new String[]{"id,url,gid,money,title"}, null, null, null, null, "id desc", null);
        while (cursor.moveToNext()) {
            Goods goods = new Goods();
            goods.setId(cursor.getInt(0));
            goods.setPicarr(cursor.getString(1));
            goods.setGoods_id(cursor.getString(2));
            goods.setMoney(cursor.getDouble(3));
            goods.setTitle(cursor.getString(4));
            temp.add(goods);
        }
        sqLiteDatabase.close();
        return  temp;
    }

}
