package com.abcs.haiwaigou.broadcast;

import android.content.Context;
import android.content.Intent;

import static com.abcs.haiwaigou.broadcast.BroadcastIntent.BROADCAST_ACTION_UPDATECARNUM;

/**
 * Created by zjz on 2015/12/26.
 */
public class MyUpdateUI {

    public static final String COLLECTTION="00000x1";
    public static final String ADDRESS="00000x2";
    public static final String CART="00000x3";
    public static final String ORDER="00000x4";
    public static final String COMMENT="00000x5";
    public static final String GOODCOMMENT="00000x6";
    public static final String MIDDLECOMMENT="00000x7";
    public static final String ALLCOMMENT="00000x8";
    public static final String BADCOMMENT="00000x9";
    public static final String RETURN="00000x10";
    public static final String GOODSDETAIL="00000x11";
    public static final String TUWENDETAIL="00000x12";
    public static final String RECHARGE="00000x13";
    public static final String GOODSNEWS="00000x14";
    public static final String CHANGEUSER="00000x15";
    public static final String COMMITCASH="00000x16";
    public static final String NEWS="00000x17";
    public static final String MYORDERNUM="00000x18";
    public static final String TURN2COMMENT="00000x19";
    public static final String TURN2DETAIL="00000x20";
    public static final String SHOWCOMMENTTITLE="00000x21";
    public static final String HIDECOMMENTTITLE="00000x22";
    public static final String MYHWG="00000x23";
    public static final String YYGBUY="00000x24";
    public static final String YYGLOTTERY="00000x25";
    public static final String YYGREFUND="00000x26";
    public static final String YYGSETADDRESS="00000x27";
    public static final String BINDCOMPANY="00000x28";
    public static final String LOCALFRAGMENT="00000x29";
    public static final String LOCALMYPUBLISH="00000x30";

    //region 发送更新UI广播
    public static  void sendUpdate(Context context,boolean success)
    {
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_MAIN);
        intent.putExtra("success",success);
//        intent.putExtra("msg",msg);
        context.sendBroadcast(intent);
    }
    //endregion

    public static void sendLogin(Context context){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_LOGIN);
        context.sendBroadcast(intent);
    }

    public static void sendCarAdd(Context context,boolean success){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_SHOPCAR_ADD);
//        intent.putExtra("num",num);
        intent.putExtra("success",success);
        context.sendBroadcast(intent);
    }

    public static void sendUpdateCarNum(Context context){
        Intent intent=new Intent(BROADCAST_ACTION_UPDATECARNUM);
//        intent.putExtra("num",num);
        context.sendBroadcast(intent);
    }
    public static void sendJieXiao(Context context){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_JIEXIAO);
        context.sendBroadcast(intent);
    }

    public static void sendAllDoods(Context context){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_ALLGOODS);
        context.sendBroadcast(intent);
    }


    public static void sendRecord(Context context){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_RECORD);
        context.sendBroadcast(intent);
    }
    public static void sendTuijian(Context context){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_COUNTRY_TUIJIAN);
        context.sendBroadcast(intent);
    }
    public static void sendHideMohu(Context context,int position){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_HIDEMOHU);
        intent.putExtra("position",position);
        context.sendBroadcast(intent);
    }
    public static void sendUpdateCollection(Context context,String type){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_COLLECTION);
        intent.putExtra("type",type);
        context.sendBroadcast(intent);
    }
    public static void update(Context context,String type){
        Intent intent=new Intent(BroadcastIntent.BROADCAST_ACTION_UPDATE);
        intent.putExtra("type",type);
        context.sendBroadcast(intent);

    }
}
