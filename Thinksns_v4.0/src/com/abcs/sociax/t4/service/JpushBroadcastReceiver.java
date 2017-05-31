package com.abcs.sociax.t4.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.abcs.haiwaigou.yyg.activity.ShowLotteryMsgDialog;
import com.abcs.haiwaigou.yyg.activity.YYGActivity;
import com.abcs.haiwaigou.yyg.activity.YYGLotteryMsgActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.constant.AppConstant;
import com.abcs.sociax.db.UserSqlHelper;
import com.abcs.sociax.net.Request;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.android.ThinksnsActivity;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.exception.UserDataInvalidException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 类说明：即时聊天极光推送
 *
 * @author Zoey
 * @version 1.0
 * @date 2015-12-29
 */
public class JpushBroadcastReceiver extends BroadcastReceiver {

    String TAG = "JPUSHRECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {// 自定义消息不会展示在通知栏，完全要开发者写代码去处理

            Log.e("Thinksns", "jpush 收到推送消息title：" + bundle.getString(JPushInterface.EXTRA_TITLE) + "/message/"
                    + bundle.getString(JPushInterface.EXTRA_MESSAGE) + "/extras /" + bundle.getString(JPushInterface.EXTRA_EXTRA)
                    + "/content_type /" + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE)
                    + "/msgID/" + bundle.getString(JPushInterface.EXTRA_MSG_ID));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {// 在这里可以做些统计，或者做些其他工作

            Log.e("Thinksns", "jpush 收到通知title：" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE) + "/message/"
                    + bundle.getString(JPushInterface.EXTRA_MESSAGE) + "/extras /" + bundle.getString(JPushInterface.EXTRA_EXTRA)
                    + "/content_type /" + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE)
                    + "/ID/" + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID) + "/msgID/" + bundle.getString(JPushInterface.EXTRA_MSG_ID));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("Thinksns", "jpush 用户打开了通知result:" + result);

            if (HasLoginUser(context)) {
                try {
                    JSONObject jpush_result = new JSONObject(result);
                    String push_type = jpush_result.getString("push_type");
                    Intent intent_skip = null;
                    //收到聊天信息
                    if (push_type.equals("message")) {
                        intent_skip = new Intent(context, ActivityHome.class);
                        intent_skip.putExtra("from", "jpush");
                        intent_skip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent_skip);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (Util.isAppAlive(context, "com.abcs.huaqiaobang")) {
                //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
                //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
                //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
                //DetailActivity前，要先启动MainActivity。
                Log.i("zjz", "the app process is alive");
                if (HasLoginUser(context)) {
                    try {
                        JSONObject jpush_result = new JSONObject(result);
                        String push_type = jpush_result.optString("push_type");
                        Intent intent_skip = null;
                        //收到聊天信息
                        if (push_type.equals("message")) {
                            intent_skip = new Intent(context, ActivityHome.class);
                            intent_skip.putExtra("from", "jpush");
                            intent_skip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent_skip);
                        }

                        Log.i("zjz","push_userId"+jpush_result.optString("userId"));
//                        Log.i("zjz","userId"+MyApplication.getInstance().self.getId());
//                        if (jpush_result.optString("pushType").equals("1")&& MyApplication.getInstance().self.getId().equals(jpush_result.optString("userId"))) {
                        if (jpush_result.optString("pushType").equals("1")) {
                            Intent mainIntent = new Intent(context, YYGLotteryMsgActivity.class);
                            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                            //如果Task栈不存在MainActivity实例，则在栈顶创建
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.putExtra("isEntity", jpush_result.optString("isEntity").equals("1"));
                            mainIntent.putExtra("goodsName",jpush_result.optString("goodsName"));
                            mainIntent.putExtra("periodsNum",jpush_result.optString("periodsNum"));
                            mainIntent.putExtra("activityId",jpush_result.optString("activityId"));
                            mainIntent.putExtra("hqbGoodsId",jpush_result.optString("hqbGoodsId"));
                            context.startActivity(mainIntent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
                Log.i("zjz", "the app process is dead");
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.abcs.huaqiaobang");
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                if (HasLoginUser(context)) {
                    try {
                        JSONObject jpush_result = new JSONObject(result);
                        String push_type = jpush_result.optString("push_type");
                        Intent intent_skip = null;
                        //收到聊天信息
                        if (push_type.equals("message")) {
                            intent_skip = new Intent(context, ActivityHome.class);
                            intent_skip.putExtra("from", "jpush");
                            intent_skip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent_skip);
                        }

                        if (jpush_result.optString("pushType").equals("1")) {
                            Bundle args = new Bundle();
                            args.putString("periodsNum", jpush_result.optString("periodsNum"));
                            args.putString("goodsName", jpush_result.optString("goodsName"));
                            args.putString("goodsImg", jpush_result.optString("goodsImg"));
                            args.putString("isEntity", jpush_result.optString("isEntity"));
                            args.putString("activityId", jpush_result.optString("activityId"));
                            args.putString("hqbGoodsId", jpush_result.optString("hqbGoodsId"));
                            args.putString("userId",jpush_result.optString("userId"));
                            launchIntent.putExtra(AppConstant.YYG_PUSH, args);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(launchIntent);
                }


            }

        } else {
            Log.d(TAG, "jpush =============Unhandled intent - " + intent.getAction());
        }
    }

    public boolean HasLoginUser(Context context) {
        UserSqlHelper db = ((Thinksns) context.getApplicationContext()).getUserSql();
        try {
            if (db != null) {
                ModelUser user = db.getLoginedUser();
                Request.setToken(user.getToken());
                Request.setSecretToken(user.getSecretToken());
                Thinksns.setMy(user);

                Log.v("ActivityHome", "/uid/" + user.getUid() + "/getUid/" + Thinksns.getMy().getUid());
                return true;
            }
            return false;
        } catch (UserDataInvalidException e) {
            return false;
        }
    }
}
