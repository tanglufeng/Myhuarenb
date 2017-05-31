package com.abcs.huaqiaobang.ytbt.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */
public class MyPushReceiver extends PushMessageReceiver {
    private String result;

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(final Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.i("info", responseString);
        if (errorCode == 0) {
            // 绑定成功
            Log.d("info", "绑定成功");
            context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putBoolean("isReload", false).commit();
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpMethod.GET, TLUrl.URL_GET_VOIP
                    + "User/saveonlynumber?uid="
                    + MyApplication.getInstance().getUid() + "&onlynumber="
                    + channelId, new RequestCallBack<String>() {
                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Tool.removeProgressDialog();
                    Log.i("info", arg1);
                    Tool.showInfo(context, "登陆失败");
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    Log.i("info", arg0.result);
                    Tool.removeProgressDialog();
//					Intent intent = new Intent(context, MainActivity.class);
                    Intent intent2 = new Intent();
                    intent2.setAction("com.xbb.mybc.action");
//					MyApplication.getInstance().getMainActivity().startActivity(intent);
//                    MyApplication.getInstance().getMainActivity().sendBroadcast(intent2);
                }
            });
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    @Override
    public void onDelTags(Context arg0, int arg1, List<String> arg2,
                          List<String> arg3, String arg4) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onListTags(Context arg0, int arg1, List<String> arg2,
                           String arg3) {
        // TODO Auto-generated method stub

    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        int flag = 0;
        if(message!=null){
            MyUpdateUI.sendUpdateCollection(context,MyUpdateUI.GOODSNEWS);
            SharedPreferences.Editor editor= Util.preference.edit();
            editor.putString("goods_news",message);
            editor.commit();
        }
        Log.d("info", messageString);
//        for (Activity activity : MyApplication.list) {
//            if (activity instanceof com.abcs.huaqiaobang.ytbt.im.MainActivity) {
//                flag = 1;
//                break;
//            }
//        }
//        if (flag == 0) {
//        MyApplication.getInstance().getMainActivity().mHandler.sendEmptyMessage(MainActivity.NOTIFYCATION_CHANGE);
//        }

        switch (message.charAt(message.length() - 1)) {
            case '0':// 加好友
                result = message.substring(0, message.lastIndexOf("#division"));
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject obj = array.getJSONObject(0);
                    AddFriendRequestBean bean = new AddFriendRequestBean();
                    bean.setId(obj.optString("id"));
                    bean.setNickname(obj.optString("nickname"));
                    bean.setUid(obj.optString("uid"));
                    bean.setAvadar(obj.optString("avatar"));
                    bean.setVoip(obj.optString("voipAccount"));
                    bean.setTime(System.currentTimeMillis());
                    bean.setState(0);
                    if (MyApplication.requests.contains(bean)) {
                        int position = MyApplication.requests.indexOf(bean);
                        if (MyApplication.requests.get(
                                MyApplication.requests.indexOf(bean)).getState() != 0
                                || !MyApplication.requests.get(position).getId()
                                .equals(bean.getId())) {
                            MyApplication.requests.remove(position);
                        } else {
                            return;
                        }
                    }
                    MyApplication.requests.add(bean);
                    SDKCoreHelper.getInstance().alertMag(false);
                    MyApplication.dbUtils.saveOrUpdateAll(MyApplication.requests);
                    Intent intent = new Intent(
                            GlobalConstant.ACTION_ADDFRIEND_REQUEST);
                    intent.putExtra("requests", MyApplication.requests);
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case '1':// 删除
                result = message.substring(0, message.lastIndexOf("#division"));
                try {
                    User user = MyApplication.dbUtils
                            .findFirst(Selector.from(User.class).where("uid", "=",
                                    Integer.parseInt(result)));
                    MyApplication.users.remove(user);
                    MyApplication.dbUtils.delete(user);
                    MyApplication.friends
                            .remove(user.getRemark().trim().equals("") ? user
                                    .getNickname() : user.getRemark());
                    context.sendBroadcast(new Intent(GlobalConstant.ACTION_UPDATE_FRIENDS));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case '2':// 同意
                result = message.substring(0, message.lastIndexOf("#division"));
                getFriendInfo(result, context);
                break;
        }

        // // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        // if (!TextUtils.isEmpty(customContentString)) {
        // JSONObject customJson = null;
        // try {
        // customJson = new JSONObject(customContentString);
        // String myvalue = null;
        // if (!customJson.isNull("mykey")) {
        // myvalue = customJson.getString("mykey");
        // }
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    private void getFriendInfo(final String result, final Context context) {
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpMethod.GET, TLUrl.URL_GET_VOIP
                        + "User/findoneuid?uid=" + result,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
//						myhandler.postDelayed(new Runnable() {
//
//							@Override
//							public void run() {
//								getFriendInfo(result);
//							}
//						}, 10000);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject obj = new JSONObject(arg0.result);
                            if (obj.getInt("status") == 1) {
                                JSONObject object = obj.getJSONArray("msg")
                                        .getJSONObject(0);
                                User user = new User();
                                user.setAvatar(object.optString("avatar"));
                                user.setNickname(object.optString("nickname"));
                                user.setRemark("");
                                user.setUid(Integer.parseInt(result));
                                user.setVoipAccout(object
                                        .optString("voipAccount"));
                                if (!MyApplication.users.contains(user)) {
                                    MyApplication.users.add(user);
                                    MyApplication.friends.put(user.getNickname(),
                                            user);
                                }
                                MyApplication.dbUtils.saveOrUpdate(user);
                                context.sendBroadcast(new Intent(
                                        GlobalConstant.ACTION_UPDATE_FRIENDS));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }

    @Override
    public void onNotificationArrived(Context arg0, String arg1, String arg2,
                                      String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNotificationClicked(Context arg0, String arg1, String arg2,
                                      String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSetTags(Context arg0, int arg1, List<String> arg2,
                          List<String> arg3, String arg4) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnbind(Context arg0, int arg1, String arg2) {
        // TODO Auto-generated method stub
        android.os.Process.killProcess(android.os.Process.myPid());

    }

}
