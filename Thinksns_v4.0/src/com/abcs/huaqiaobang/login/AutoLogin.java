package com.abcs.huaqiaobang.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.db.UserSqlHelper;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.android.ThinksnsActivity;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.exception.DataInvalidException;
import com.thinksns.sociax.thinksnsbase.network.ApiHttpClient;
import com.thinksns.sociax.thinksnsbase.utils.ActivityStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * @author xbw
 * @version 创建时间：2015-5-27 下午2:31:14
 */
public class AutoLogin {
    private boolean isLoginSuccess = false;
    private boolean isSendSuccess = false;
    public static boolean isFirstStartApp = true;
    public ThinksnsActivity activity;
    private boolean isYYG = false;
    private Bundle bundle;
    private Handler handler = new Handler();
    private ACache aCache;

    public AutoLogin(ThinksnsActivity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        checkAutoLogin();
    }

    public AutoLogin(ThinksnsActivity activity, boolean isYYG, Bundle bundle) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.isYYG = isYYG;
        this.bundle = bundle;
        checkAutoLogin();
    }

    private void checkAutoLogin() {
        aCache = ACache.get(activity);
        if ("WX".equals(Util.preference.getString("logintype", "")) || "QQ".equals(Util.preference.getString("logintype", ""))) {
            Log.i("WX", "wx=" + Util.preference.getString("logintype", ""));
            Util.token = Util.preference.getString("token", "");
            Util.isThirdLogin = true;
            LoginForToken(Util.preference.getString("token", ""));

            Log.i("WX", "wx=" + Util.preference.getString("token", ""));

            return;

        }
        if (Util.preference != null
                && Util.preference.getBoolean("lizai_auto", false)
                && isFirstStartApp && MyApplication.getInstance().self == null) {
            isFirstStartApp = !isFirstStartApp;
            String userName = Util.preference.getString("lizai_userName", "");
            String pwd = Util.preference.getString("lizai_pwd", "");
            String token = aCache.getAsString(TLUrl.getInstance().LOGINTOKEN);
            if (!TextUtils.isEmpty(token)) {
                Log.e("zjz", "use_token_login");
                Util.token = token;
                LoginForToken(token);
            } else if (userName.length() > 0 && pwd.length() > 0) {
                Log.e("zjz", "use_send_login");
                sendLogin(userName, pwd);
            } else {
                Log.e("zjz", "需要重新登录");
                ActivityStack.startActivity(activity, WXEntryActivity.class);
                activity.finish();
            }
        }

    }

    private void sendLogin(String userName, String pwd) {
        String param = "uname=" + userName + "&upass=" + pwd + "&from=华侨帮";
//        NoticeDialog.showNoticeDlg("自动登录中...", activity);
        HttpRequest.sendPost(TLUrl.getInstance().URL_login, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
//                NoticeDialog.stopNoticeDlg();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "send_login_msg=" + msg);
                        if (msg.length() == 0) {
                            Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT).show();
                            if (!isSendSuccess) {
                                ActivityStack.startActivity(activity, WXEntryActivity.class);
                                activity.finish();
                            }
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                if (code == 1) {
                                    String token = json.getString("result");
                                    Util.token = token;
                                    if (aCache.getAsString(TLUrl.getInstance().LOGINTOKEN) != null)
                                        aCache.remove(TLUrl.getInstance().LOGINTOKEN);
                                    aCache.put(TLUrl.getInstance().LOGINTOKEN, Util.token, 5 * 24 * 60 * 60);

                                    LoginForToken(token);
                                    //登录成功
                                    isSendSuccess = true;
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.i("zjz", "==========jjj");
                        } finally {
                            if (!isSendSuccess) {
                                ActivityStack.startActivity(activity, WXEntryActivity.class);
                                activity.finish();
                            }
                        }
                    }
                });

            }
        });
    }

    private void LoginForToken(final String token) {

        Log.i("tga", "LoginForToken=====" + token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        // TODO Auto-generated method stub
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject json = new JSONObject(msg);
                                    if (json != null && json.has("code")) {
                                        int code = json.getInt("code");
                                        if (code == 1) {
                                            Intent intent = new Intent(
                                                    "com.abct.occft.hq.login");
                                            intent.putExtra("type", "login");
                                            intent.putExtra("msg",
                                                    json.getString("result"));
                                            activity.sendBroadcast(intent);


                                            loginResult(json.getString("result"));

                                            Util.token = token;
                                            Util.preference.edit().putString("token", Util.token).commit();

                                            JSONObject obj = new JSONObject(json.getString("result"));


                                            JSONObject sns = obj.getJSONObject("sns");

                                            if (!sns.has("uid")) {
//                                        showToast("登录失败,请检查您的网络");
                                                return;
                                            }


                                            ModelUser newUser = new ModelUser(sns);
                                            newUser.setFace(obj.optString("avatar"));
                                            newUser.setPostUface(obj.optString("avatar"));

                                            String oauth_token = sns.getString("oauth_token");
                                            String oauth_token_secret = sns.getString("oauth_token_secret");
                                            ApiHttpClient.TOKEN = oauth_token;
                                            ApiHttpClient.TOKEN_SECRET = oauth_token_secret;
                                            newUser.setToken(oauth_token);
                                            newUser.setSecretToken(oauth_token_secret);

                                            Thinksns.setMy(newUser);
                                            //添加用户信息至数据库
                                            UserSqlHelper db = UserSqlHelper.getInstance(activity);
                                            db.addUser(newUser, true);
                                            String username = newUser.getUserName();
                                            if (!db.hasUname(username))
                                                db.addSiteUser(username);

                                            isLoginSuccess = true;
                                            if (isYYG) {
                                                ActivityStack.startActivity(activity, ActivityHome.class, bundle);
                                            } else {
                                                ActivityStack.startActivity(activity, ActivityHome.class);
                                            }


                                            activity.finish();
                                            // final String result = json
                                            // .getString("result");
                                            // activity.shouYe.handler
                                            // .post(new Runnable() {
                                            //
                                            // @Override
                                            // public void run() {
                                            // // TODO Auto-generated
                                            // // method stub
                                            // activity.shouYe
                                            // .loginResult(result);
                                            // }
                                            // });
                                        }
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (DataInvalidException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (!isLoginSuccess) {
                                        ActivityStack.startActivity(activity, WXEntryActivity.class);
                                        activity.finish();
                                    }
                                }
                            }
                        });

                    }
                });
    }


    private com.abcs.huaqiaobang.ytbt.bean.User user;
    private String userName;
    private String pwd;

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

                SharedPreferences.Editor editor = Util.preference.edit();
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
        Log.i("zjz","paremas="+"nickname=" + user.getNickname() + "&userId="
                + user.getUid() + "&avator=" + user.getAvatar() + "&userName=" + user.getUname() + "&alias=" + user.getUid());

        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_login, "nickname=" + user.getNickname() + "&userId="
                + user.getUid() + "&avator=" + user.getAvatar() + "&userName=" + URLEncoder.encode(user.getUname()) + "&alias=" + user.getUid(), new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                Log.i("zjz", "login_for_yyg=" + msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("status") == 1) {
                        Util.isYYGLogin = true;
                        Log.i("zjz", "一元购登录成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void loginForPOINT() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_login_for_point, "user_name=" + Util.preference.getString("lizai_userName", "") + "&password="
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

}
