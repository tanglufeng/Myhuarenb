package com.abcs.huaqiaobang.login;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.presenter.MyPrsenter;
import com.abcs.huaqiaobang.tljr.data.InitData;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.LabelBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.UserBean;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.abcs.huaqiaobang.ytbt.voicemeeting.ContactEntity;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingEntity;
import com.abcs.sociax.t4.android.ActivityHome;
import com.lidroid.xutils.exception.DbException;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.yuntongxun.ecsdk.ECInitParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import static com.abcs.huaqiaobang.ytbt.util.TLUrl.URL_GET_VOIP;

/**
 * @author xbw 接收Umeng推送过来的分享广播
 * @version 创建时间：2015-6-2 上午10:29:06
 */
public class LoginResultReceiver extends BroadcastReceiver {
    ActivityHome activity;
    private SDKCoreHelper sdkCoreHelper = SDKCoreHelper.getInstance();
    public static LoginResultReceiver instance;
    private String userName;
    private String pwd;
    private ProgressDialog dialog;
    private com.abcs.huaqiaobang.ytbt.bean.User user;

    public static LoginResultReceiver getInstance(ActivityHome context) {
        if (null == instance) {
            instance = new LoginResultReceiver(context);
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.abct.occft.hq.login");
            try {
                context.registerReceiver(instance, filter);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return instance;
    }

    public LoginResultReceiver(ActivityHome context) {
        // TODO Auto-generated constructor stub
        this.activity = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getStringExtra("type").equals("login")
                && !intent.getStringExtra("msg").equals("")) {


            loginResult(intent.getStringExtra("msg"));

            Log.i("zjz","登录成功！！！！");
//            getUserLevel();//用户等级，经验
//            getUserEvent();
//            activity.my.loadData();
//            activity.huanQiuShiShi.refreshFragment();
            //zjz更新海外购购物车数量
//            activity.main.initCarNum();


//            activity.mHandler.sendEmptyMessage(5);
//			if(MyApplication.getInstance().self!=null) {
//				activity.main.getHotNews();
//			}


//            MyApplication myApplication = MyApplication.getInstance();
//            ChartActivity.getYunZhiToken(myApplication.self.getId(), myApplication.self.getNickName()
//                    , null, myApplication.self.getAvatarUrl());
        } else if (intent.getStringExtra("type").equals("logout")) {
//            activity.main.logout();
            if (!Util.preference.getString("logintype", "").equals("")) {
                Util.preference.edit().putString("lizai_userName", "");
            }
            Util.preference.edit().putString("lizai_pwd", "").commit();
            Util.preference.edit().putString("logintype", "").commit();
            Util.preference.edit().putString("token", "").commit();
            Util.preference.edit().putString("cityhistory", "").commit();

            //zjz
            Util.preference.edit().putBoolean("isDefault", false).commit();
            Util.preference.edit().putString("address_id", "").commit();
            Util.preference.edit().putString("area_id", "").commit();
            Util.preference.edit().putString("city_id", "").commit();
            Util.preference.edit().putString("address", "").commit();
            Util.preference.edit().putString("name", "").commit();
            Util.preference.edit().putString("phone", "").commit();

            InitData.isrefreshByUser = true;
            MyApplication.getInstance().self = null;
            MyApplication.getInstance().setMykey(null);
            MyUpdateUI.sendUpdateCarNum(activity);
            if (activity.my != null)
                new MyPrsenter(activity.my).loginSuccess();
        }
//		activity.current.initUser();
//		 activity.mHandler.sendEmptyMessage(101);
    }

    private void loginResult(String msg) {
        LogUtil.e("loginResult", msg);
//        dialog = new ProgressDialog(activity);
//        dialog.setMessage("正在登陆");
//        dialog.show();

//        ProgressDlgUtil.showProgressDlg("正在登陆", activity);
        try {
            JSONObject object = new JSONObject(msg);
            MyApplication.getInstance().self = new User();
            MyApplication.getInstance().self.setBindId(object.optString("id"));
            MyApplication.getInstance().self.setId(object.optString("uid"));
            MyApplication.getInstance().self.setUserName(object
                    .optString("uname"));
            MyApplication.getInstance().self.setNickName(object
                    .optString("nickname"));
//            MyApplication.getInstance().self.setArea(object
//                    .optString("location"));
            MyApplication.getInstance().self.setFrom(object.optString("from"));
            MyApplication.getInstance().self.setLast(object
                    .optJSONObject("last"));
            MyApplication.getInstance().self.setSex(object
                    .optString("gender"));
            MyApplication.getInstance().self.setAvatarUrl(object
                    .optString("avatar"));
            MyApplication.getInstance().setMykey(object
                    .optString("key"));
            JSONObject bind = object.getJSONObject("bind");
            if (!bind.has("err")) {
                MyApplication.getInstance().self.setEmail(bind
                        .optString("email"));
                MyApplication.getInstance().self.setPhone(bind
                        .optString("phone"));
                MyApplication.getInstance().self.setInvail(bind
                        .optBoolean("emailVerify"));
                Log.i("emailVerify", "emailVerify" + bind
                        .optBoolean("emailVerify"));
                MyApplication.getInstance().self.setIsInvailPhone(bind
                        .optBoolean("phoneVerify"));
            }
            if (!Util.isThirdLogin) {

                Editor editor = Util.preference.edit();
                if (!Util.preference.getString("logintype", "").equals("")) {
                    editor.putString("lizai_userName", "");
                } else {
                    editor.putString("lizai_userName",
                            MyApplication.getInstance().self.getUserName());
                }
                editor.putString("news_p_id", object.optString("uid"));

                editor.commit();
            }
            userName = Util.preference.getString("lizai_userName", "");
            pwd = Util.preference.getString("lizai_pwd", "");
            user = new com.abcs.huaqiaobang.ytbt.bean.User();
            user.setId(object.getString("id"));
            user.setUname(object.getString("uname"));
            user.setFromUser(object.getString("from"));
            user.setUid(object.getInt("uid"));
            user.setNickname(object
                    .getString("nickname"));
            user.setAvatar(object.getString("avatar"));
            user.setGender(object.getInt("gender"));
            user.setLocation(object
                    .getString("location"));
            MyApplication.getInstance().setAvater(
                    object.getString("avatar"));
            MyApplication.getInstance().setUid(
                    object.getInt("uid"));
            MyApplication
                    .getInstance()
                    .setOwnernickname(
                            object.getString("nickname"));

//            loginForHWG();
            Log.i("zjz", "userName=" + userName);
            Log.i("zjz", "object.getString(uname)=" + object.getString("uname"));
            Log.i("zjz", "login_key=" + MyApplication.getInstance().getMykey());
//            if (activity.my != null)
//                new MyPrsenter(activity.my).loginSuccess();
//            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.CHANGEUSER);
//            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.MYORDERNUM);
            loginForPOINT();
//            loginForVoip(user);
            loginForYYG(user);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loginForYYG(com.abcs.huaqiaobang.ytbt.bean.User user) {
//        ModelUser snsUser=Thinksns.getMy();
        Log.i("zjz","snsUserName="+"nickname=" + user.getNickname() + "&userId="
                + user.getUid() + "&avator=" + user.getAvatar() +"&userName="+URLEncoder.encode(user.getUname())+"&alias="+user.getUid());

        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_login , "nickname=" + user.getNickname() + "&userId="
                + user.getUid() + "&avator=" + user.getAvatar() +"&userName="+URLEncoder.encode(user.getUname())+"&alias="+user.getUid(), new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                Log.i("zjz", "login_for_yyg=" + msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("status") == 1) {
                        Util.isYYGLogin=true;
                        Log.i("zjz", "一元购登录成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void loginForPOINT() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_login_for_point, "user_name=" + URLEncoder.encode(Util.preference.getString("lizai_userName", "")) + "&password="
                + Util.preference.getString("lizai_pwd", "") + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("code") == 200) {
                        if (json.optString("datas").contains("成功"))
                            Log.i("zjz", "成功获得积分");
                    }
                    Log.i("zjz", "login_for_point=" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void loginForHWG() {

        HttpRequest.sendPost(TLUrl.getInstance().URL_LOGINFORHWG, "username=" + Util.preference.getString("lizai_userName", "") + "&password="
                + Util.preference.getString("lizai_pwd", "") + "&client=wap", new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.has("code")) {
                        int code = json.getInt("code");
                        Log.i("zjz", "code=" + code);
                        Log.i("zjz", msg);
                        if (code == 200) {
                            JSONObject object = json.getJSONObject("datas");
                            String token = object.optString("key");
                            if ("".equals(token)) {

                                sendRegister();
                            } else {
                                Log.i("zjz", "token=" + token);
                                MyApplication.getInstance().setMykey(token);
                                loginForVoip(user);
                            }
//                            Util.token = token;
//                            WXLoginForToken(token);
//                            MyOrder(token);
//                            finish();
                        } else {
                            activity.showMessage("登录失败，网路繁忙");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });


    }

    private void sendRegister() {


//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String param = "username=" + userName + "&password=" + pwd + "&password_confirm=" + pwd + "&client=wap";

        HttpRequest.sendPost(TLUrl.getInstance().URL_HWGREGISTER, param, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        Log.i("zjz", "code=" + code);
                        Log.i("zjz", msg);
                        if (code == 200) {
                            JSONObject object = json.getJSONObject("datas");
                            String token = object.optString("key");
                            if ("".equals(token)) {

                                activity.showMessage("登录失败");
                            } else {
                                Log.i("zjz", "token=" + token);
                                MyApplication.getInstance().setMykey(token);
                                loginForVoip(user);
                            }
//                            Util.token = token;
//                            WXLoginForToken(token);
//                            MyOrder(token);
//                            finish();
                        } else {
                            activity.showMessage("登录失败，网路繁忙");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
//        requestQueue.add(new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_HWGREGISTER + param, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//                JSONObject object = jsonObject;
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//                ProgressDlgUtil.stopProgressDlg();
//                activity.showMessage("登录失败，网路繁忙");
//            }
//        }));


    }

    protected void loginForVoip(final com.abcs.huaqiaobang.ytbt.bean.User user) {
        ProgressDlgUtil.stopProgressDlg();
        HttpRequest.sendPost(com.abcs.huaqiaobang.ytbt.util.TLUrl.URL_GET_VOIP + "User/userlogin", "uid="
                        + user.getUid() + "&avatar=" + user.getAvatar() + "&nickname="
                        + user.getNickname() + "&apiKey=cBxRk7dz68YyKQEflhSM6P8K" + "&secretKey=KgljGqrPasD22yGCDfUEydntAjGy5mKA",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        Log.i("xbb1", msg + 123);
                        if (msg.length() <= 0) {
//                            Tool.removeProgressDialog();
                            ProgressDlgUtil.stopProgressDlg();
                            activity.showMessage("系统繁忙");
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            JSONObject object = jsonObject.getJSONObject("msg");
                            if (jsonObject.getInt("status") == 1) {
                                UserBean userBean = new UserBean();
                                userBean.setDateCreate(object
                                        .getString("dateCreated"));
                                userBean.setVoipAccount(object
                                        .getString("voipAccount"));
                                userBean.setVoipPwd(object.getString("voipPwd"));
                                MyApplication.getInstance().setUserBean(
                                        userBean);
                                user.setVoipAccout(object
                                        .getString("voipAccount"));
                                MyApplication.getInstance().setUser(user);
                                if (!Util.preference.getString("voipAccount", "").equals(user.getVoipAccout())) {

                                    MyApplication.requests.clear();
                                    try {
                                        MyApplication.dbUtils
                                                .dropTable(GroupMemberBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(ConversationBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(User.class);
                                        MyApplication.dbUtils
                                                .dropTable(UserBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(GroupBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(AddFriendRequestBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(LabelBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(TopConversationBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(MsgBean.class);
                                        MyApplication.dbUtils
                                                .dropTable(MeetingEntity.class);
                                        MyApplication.dbUtils
                                                .dropTable(ContactEntity.class);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    Editor editor = activity.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                                    editor.putBoolean("isShake", false);
                                    editor.putBoolean("isSound", true);
                                    MyApplication.isShake = false;
                                    MyApplication.isSound = true;
                                    editor.commit();
                                }
                                Util.preference.edit().putString("voipAccount", user.getVoipAccout()).commit();
                                sdkCoreHelper.init(activity,
                                        ECInitParams.LoginMode.FORCE_LOGIN,
                                        userBean.getVoipAccount(),
                                        userBean.getVoipPwd(), dialog);
                                return;
                            }
//                            Tool.removeProgressDialog();

                            Toast.makeText(activity, msg.toString(),
                                    Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("xbb2", e.toString());
//                            Tool.removeProgressDialog();
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void getUserLevel(final Complete complete) {
        if (MyApplication.getInstance().self != null) {
            HttpRequest.sendHttpsGet(TLUrl.getInstance().URL_LEVEL + "/" + MyApplication.getInstance().self.getId(), "",
                    new HttpRevMsg() {
                        @Override
                        public void revMsg(String msg) {
                            try {
                                JSONObject object = new JSONObject(msg).getJSONObject("result");
                                MyApplication.getInstance().self.setLevel(object.optInt("level"));
                                MyApplication.getInstance().self.setLevelNeed((float) object.optDouble("need", 0));
                                MyApplication.getInstance().self.setLevelNeedTotal((float) object.optDouble("needtotal", 0));
                                MyApplication.getInstance().self.setLevelTotal((float) object.optDouble("total", 0));
                                MyApplication.getInstance().self.setLevelUnit(object.optString("unit"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                complete.complete();
                            }
                        }
                    });
        } else {
        }
    }

    public static void getUserEvent(final Complete complete) {
        if (MyApplication.getInstance().self != null) {
            HttpRequest.sendHttpsGet(TLUrl.getInstance().URL_LEVEL + "/" + MyApplication.getInstance().self.getId()
                    + "/event", "", new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    try {
                        JSONObject object = new JSONObject(msg).getJSONObject("result");
                        MyApplication.getInstance().self.setIntegral(object.getInt("number"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        complete.complete();
                    }
                }
            });
        }
    }


}
