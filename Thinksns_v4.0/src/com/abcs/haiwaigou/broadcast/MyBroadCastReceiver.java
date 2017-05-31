package com.abcs.haiwaigou.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by zjz on 2015/12/26.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    Context context;


    public MyBroadCastReceiver(Context context, UpdateUI updateUI) {
        this.context = context;
        this.updateUI = updateUI;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_MAIN)) {
//            updateUI.updateui(intent);
//        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_LOGIN)){
//            updateUI.loginReceive(intent);
//        }
        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_SHOPCAR_ADD)){
            updateUI.updateShopCar(intent);
        }

        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_UPDATECARNUM)){
            updateUI.updateCarNum(intent);
        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_HIDEMOHU)){
//            updateUI.hidemohu(intent);
//        }
        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_COLLECTION)){
            updateUI.updateCollection(intent);
        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_JIEXIAO)){
//            updateUI.updateJieXiao(intent);
//        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_ALLGOODS)){
//            updateUI.updateAllGoods(intent);
//        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_RECORD)){
//            updateUI.updateRecord(intent);
//        }
//        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_COUNTRY_TUIJIAN)){
//            updateUI.updateCountryTuijian(intent);
//        }
        if(intent.getAction().equals(BroadcastIntent.BROADCAST_ACTION_UPDATE)){
            updateUI.update(intent);
        }
    }



    //region 回调接口
    UpdateUI updateUI;
    //对外接口
    public interface  UpdateUI
    {

//        void loginReceive(Intent intent);
//        void updateui(Intent intent);
        void updateShopCar(Intent intent);
//        void updateJieXiao(Intent intent);
//        void updateAllGoods(Intent intent);
//        void updateRecord(Intent intent);
        void updateCarNum(Intent intent);
//        void hidemohu(Intent intent);
        void updateCollection(Intent intent);
        void update(Intent intent);
//        void updateCountryTuijian(Intent intent);
    }
    //endregion

    //region 注册广播
    public void register()
    {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_MAIN);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_LOGIN);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_SHOPCAR_ADD);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_JIEXIAO);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_ALLGOODS);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_RECORD);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_COUNTRY_TUIJIAN);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_UPDATECARNUM);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_HIDEMOHU);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_COLLECTION);
        intentFilter.addAction(BroadcastIntent.BROADCAST_ACTION_UPDATE_COMMENT);
        context.registerReceiver(this,intentFilter);
    }
    //endregion

    //region 注销广播
    public void unRegister()
    {
        context.unregisterReceiver(this);
    }
    //endregion
}
