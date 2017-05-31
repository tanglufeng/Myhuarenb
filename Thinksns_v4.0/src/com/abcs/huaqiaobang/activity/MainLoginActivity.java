package com.abcs.huaqiaobang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.ForgetPwdActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.NoticeDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.CustomExceptionHandler;
import com.abcs.huaqiaobang.wxapi.RegisterActivity;
import com.abcs.huaqiaobang.wxapi.loginmodule.TencentAuthHandler;
import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.abcs.sociax.android.R;
import com.abcs.sociax.api.Api;
import com.abcs.sociax.db.UserSqlHelper;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.model.ModelUser;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thinksns.sociax.thinksnsbase.bean.ListData;
import com.thinksns.sociax.thinksnsbase.bean.SociaxItem;
import com.thinksns.sociax.thinksnsbase.exception.DataInvalidException;
import com.thinksns.sociax.thinksnsbase.network.ApiHttpClient;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainLoginActivity extends BaseActivity implements View.OnClickListener,IWXAPIEventHandler {

    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.tv_register)
    TextView tvRegister;
    @InjectView(R.id.rl_register)
    RelativeLayout rlRegister;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.img_clear_uname)
    ImageView imgClearUname;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.img_show_pwd)
    ImageView imgShowPwd;
    @InjectView(R.id.img_clear_upwd)
    ImageView imgClearUpwd;
    @InjectView(R.id.tljr_txt_wjmm)
    TextView tljrTxtWjmm;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.t_phone_login)
    TextView tPhoneLogin;
    @InjectView(R.id.tljr_img_lweibo)
    ImageView tljrImgLweibo;
    @InjectView(R.id.img_login_wx)
    ImageView imgLoginWx;
    @InjectView(R.id.img_login_qq)
    ImageView imgLoginQq;
    @InjectView(R.id.img_bottom)
    ImageView imgBottom;
    @InjectView(R.id.t_cancel)
    TextView tCancel;
    @InjectView(R.id.t_email)
    TextView tEmail;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.relative_bottom)
    RelativeLayout relativeBottom;

    public static Activity other;
    private TencentAuthHandler mTencentAuthHandler;
    private IWXAPI api;
    public static String appid = "wx9906731d4fadfdaa";
    private boolean isLogin = false;
    MainLoginActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mian);
        ButterKnife.inject(this);
        activity = this;
        api = WXAPIFactory.createWXAPI(this, appid, false);
        api.registerApp(appid);

        api.handleIntent(getIntent(), this);
        // HttpRequest.sendGet("http://120.24.214.108:3000/api/logout/wechat",
        // "");
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
        mTencentAuthHandler = new TencentAuthHandler(this);
        mTencentAuthHandler.logout(this);
        initUI();
        initTextChange();
        initListener();
    }
    private void initTextChange() {
        imgClearUname.setVisibility(etUsername.getText().toString().length() != 0 ? View.VISIBLE : View.INVISIBLE);
        imgClearUpwd.setVisibility(etPwd.getText().toString().length()!=0?View.VISIBLE:View.INVISIBLE);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    imgClearUname.setVisibility(View.INVISIBLE);
                } else {
                    imgClearUname.setVisibility(View.VISIBLE);
                }
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    imgClearUpwd.setVisibility(View.INVISIBLE);
                } else {
                    imgClearUpwd.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initUI() {
        if (Util.preference != null
                && Util.preference.getString("lizai_userName", "").length() > 0) {
            etUsername.setText(Util.preference.getString("lizai_userName", ""));
        }
    }


    private void initListener() {
        tPhone.setOnClickListener(this);
        tCancel.setOnClickListener(this);
        tEmail.setOnClickListener(this);
        tPhoneLogin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        imgLoginQq.setOnClickListener(this);
        imgLoginWx.setOnClickListener(this);
        tljrTxtWjmm.setOnClickListener(this);
        rlRegister.setOnClickListener(this);
        imgClearUname.setOnClickListener(this);
        imgClearUpwd.setOnClickListener(this);
        imgShowPwd.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_login_qq:
                Util.isThirdLogin = true;
                sendQQ();
                break;
            case R.id.img_login_wx:
                MobclickAgent.onEvent(this, "WXLogin");
                if (!api.isWXAppInstalled()) {
                    showToast("请先安装微信");
                    return;
                }
                ProgressDlgUtil.showProgressDlg("登录中",
                        this);
                isLogin = true;
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "text";
                Util.isThirdLogin = true;
                other = this;
                api.sendReq(req);
                break;
            case R.id.rl_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.tljr_txt_wjmm:
                forgetPwd();
//                wanJiMiMa();
                break;
            case R.id.btn_login:
                if (etUsername.getText().toString().trim().equals("")
                        || etPwd.getText().toString().trim().equals("")) {
                    showToast("请输入用户名或密码");
                } else {
                    Util.isThirdLogin = false;
                    Util.preference.edit().putString("logintype", "").commit();
                    sendLogin();
                }
                break;
            case R.id.t_cancel:
                relativeBottom.setVisibility(View.GONE);
                break;
            case R.id.t_phone:
                findpwd(false);
                relativeBottom.setVisibility(View.GONE);
                break;
            case R.id.t_email:
                findpwd(true);
                relativeBottom.setVisibility(View.GONE);
                break;
            case R.id.relative_bottom:
                v.setVisibility(View.GONE);
                break;
            case R.id.img_clear_uname:
                etUsername.getText().clear();
                break;
            case R.id.img_clear_upwd:
                etPwd.getText().clear();
                break;
        }
    }

    Handler handler = new Handler();


    private JSONObject jsonObject;
    protected void forgetPwd() {
        final String userName = etUsername.getText().toString().trim();
        if (userName.length() <= 0) {
            showToast("请输入你要找回密码的帐号");
            etUsername.requestFocus();
            return;
        }
        ProgressDlgUtil.showProgressDlg("", this);

        HttpRequest.sendPost(TLUrl.getInstance().URL_getbindinfo, "uname=" + userName,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        System.err.println("msg:" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                ProgressDlgUtil.stopProgressDlg();
                                if (msg.length() == 0) {
                                    showToast("请求失败,请检查网络重试");
                                    return;
                                }

                                try {
                                    JSONObject object=new JSONObject(msg);
                                    int code = object.optInt("code");
                                    if (code == 1) {
                                        jsonObject = object.getJSONObject("result");
                                        if (jsonObject.has("email") && jsonObject.has("phone")) {
                                            if (jsonObject.optBoolean("emailVerify") && jsonObject.optBoolean("phoneVerify")) {
                                                relativeBottom.setVisibility(View.VISIBLE);
                                            } else if (jsonObject.optBoolean("emailVerify") && !jsonObject.optBoolean("phoneVerify")) {
                                                //通过邮箱找回
                                                findpwd(true);
                                            } else if (!jsonObject.optBoolean("emailVerify") && jsonObject.optBoolean("phoneVerify")) {
                                                //通过手机找回
                                                findpwd(false);
                                            } else {
                                                // 未验证
                                                showToast("您的邮箱和手机皆未通过验证,无法找回密码");
                                            }
                                        } else if (jsonObject.has("email")) {
                                            //通过邮箱找回
                                            findpwd(true);
                                        } else if (jsonObject.has("phone")) {
                                            //通过手机找回
                                            findpwd(false);
                                        } else {
                                            showToast("未绑定邮箱、手机或者三方登录,如需找回密码,请联系客服!");
                                        }

                                    } else {
                                        showToast(code);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                com.alibaba.fastjson.JSONObject result = com.alibaba.fastjson.JSONObject.parseObject(msg);
//                                int code = result.getIntValue("code");
//                                if (code == 1) {
//                                    com.alibaba.fastjson.JSONObject obj = result.getJSONObject("result");
//                                    if (obj.containsKey("email") && obj.containsKey("phone")) {
//                                        if (obj.getBooleanValue("emailVerify") && obj.getBooleanValue("phoneVerify")) {
//                                            relativeBottom.setVisibility(View.VISIBLE);
//                                        } else if (obj.getBooleanValue("emailVerify") && !obj.getBooleanValue("phoneVerify")) {
//                                            //通过邮箱找回
//                                            findpwd(true, obj);
//                                        } else if (!obj.getBooleanValue("emailVerify") && obj.getBooleanValue("phoneVerify")) {
//                                            //通过手机找回
//                                            findpwd(false, obj);
//                                        } else {
//                                            // 未验证
//                                            showToast("您的邮箱和手机皆未通过验证,无法找回密码");
//                                        }
//                                    } else if (obj.containsKey("email")) {
//                                        //通过邮箱找回
//                                        findpwd(true, obj);
//                                    } else if (obj.containsKey("phone")) {
//                                        //通过手机找回
//                                        findpwd(false, obj);
//                                    } else {
//                                        showToast("未绑定邮箱、手机或者三方登录,如需找回密码,请联系客服!");
//                                    }
//
//                                } else {
//                                    showToast(code);
//                                }

                            }
                        });
                    }
                });
    }

    public void findpwd(boolean isemail) {
        try {
            if (isemail) {
                String email = jsonObject.optString("email");
                if (jsonObject.optBoolean("emailVerify")) {
                    Intent intent = new Intent(MainLoginActivity.this, ForgetPwdActivity.class);
                    intent.putExtra("userName", etUsername.getText().toString().trim());
                    intent.putExtra("method", "email");
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    // 未验证
                    showToast("您的邮箱未通过验证,无法找回密码");
                }
            } else {
                String phone = jsonObject.optString("phone");
                if (jsonObject.optBoolean("phoneVerify")) {
                    Intent intent = new Intent(MainLoginActivity.this, ForgetPwdActivity.class);
                    intent.putExtra("userName", etUsername.getText().toString().trim());
                    intent.putExtra("method", "phone");
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else {
                    // 未验证
                    showToast("您的手机未通过验证,无法找回密码");
                }
            }
        } catch (Exception e) {

        }
    }

    private void sendLogin() {
        if (!ServerUtils.isConnect(this)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showToast("登录失败,请检查您的网络");
                }
            });
            return;
        }
        NoticeDialog.showNoticeDlg("登录中", this);

        //执行验证请求
      //  new Api.Oauth().authorize(etUsername.getText().toString().trim(), etPwd.getText().toString().trim(), mListener);

        String param = "uname=" + etUsername.getText().toString().trim() + "&upass="
                + etPwd.getText().toString().trim() + "&from=华侨帮";
        HttpRequest.sendPost(TLUrl.getInstance().URL_login, param, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                // TODO Auto-generated method stub

                if (msg.length() == 0) {
                    showToast("服务器异常，请稍后再试");
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        if (code == 1) {
                            String token = json.getString("result");
                            Util.token = token;
                            WXLoginForToken(token);
                        } else if (code == -1000) {
//                            sendRegist(name.getText().toString().trim(), pwd.getText().toString().trim());
                            showToast(code);
                        } else {
                            showToast(code);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    NoticeDialog.stopNoticeDlg();
                }
            }
        });
    }



    final ApiHttpClient.HttpResponseListener mListener = new ApiHttpClient.HttpResponseListener() {

        @Override
        public void onSuccess(final Object result) {
            if(result instanceof ModelUser) {
                ModelUser authorizeResult = (ModelUser)result;
                //获取个人数据
                new Api.Users().show(authorizeResult, this);
            }else if(result instanceof ListData<?>) {
                ListData<SociaxItem> list = (ListData<SociaxItem>) result;
                if(list != null && list.size() == 1) {
                    ModelUser loginedUser = (ModelUser)list.get(0);
                    //保存用户信息
                    Thinksns.setMy(loginedUser);
                    //添加用户信息至数据库
                    UserSqlHelper db = UserSqlHelper.getInstance(activity);
                    db.addUser(loginedUser, true);
                    String username = loginedUser.getUserName();
                    if (!db.hasUname(username))
                        db.addSiteUser(username);
//                    Message errorMessage = new Message();
//                    errorMessage.arg1 = DialogHandler.AUTH_DOWN;
//                    dialogHandler.sendMessage(errorMessage);

                }
            }

        }

        @Override
        public void onError(Object result) {
//            dialog.setContent(result.toString());
//            Message errorMessage = new Message();
//            errorMessage.arg1 = DialogHandler.CLOSE_DIALOG;
//            dialogHandler.sendMessageDelayed(errorMessage, 1000);
        }
    };


    private void WXLoginForToken(final String token) {
        Log.e("WECHAT", "自己服务器的" + token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {

                        Log.e("WX", msg.toString());
                        // TODO Auto-generated method stub
                        try {
                            final JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.e("WECHAT", "自己服务器的code" + code);
                                if (code == 1) {
                                    Util.token = token;
                                    Util.preference.edit().putString("token", Util.token).commit();

                                    JSONObject obj = new JSONObject(json.getString("result"));


                                    JSONObject sns = obj.getJSONObject("sns");

                                    if (!sns.has("uid")){
                                        showToast("登录失败,请检查您的网络");
                                        return;
                                    }



                                    ModelUser newUser = new ModelUser(sns);
                                    newUser.setFace(obj.optString("avatar"));
                                    newUser.setPostUface(obj.optString("avatar"));

                                    LogUtil.i("haologin","avator: "+newUser.getFace());

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



                                    loginSuccess(json.getString("result"));

                                    Log.e("WECHAT", "Util.token =" + Util.token);
                                } else {
                                    showToast(code);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (DataInvalidException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showToast(int code) {
        String msg = "";
        switch (code) {
            case -1:
                msg = "参数非法";
                break;
            case -1000:
                msg = "用户不存在";
                break;
            case -1001:
                msg = "用户名或密码错误";
                break;
            case -1002:
                msg = "帐号停权";
                break;
            case -1003:
                msg = "用户详情未找到";
                break;
            case -1004:
                msg = "密码不一样";
                break;
            case -1005:
                msg = "注册失败";
                break;
            case -1006:
                msg = "用户已存在";
                break;
            case -1007:
                msg = "微信配置文件未找到";
                break;
            case -1008:
                msg = "非法的Code";
                break;
            case -1009:
                msg = "请重新登录";// "非法的token"
                break;
            case -1014:
                msg = "您未绑定邮箱,如需找回密码,请联系客服!";
                break;
            case -1017:
                msg = "邮箱未验证";
                break;
            case -1018:
                msg = "手机未验证";
                break;
            case -1019:
                msg = "验证方式未找到";
                break;
            case -2001:
                msg = "分享成功";
                break;
            case -2002:
                msg = "分享取消";
                System.out.println("分享取消");
                break;
            case -2003:
                msg = "分享拒绝";
                break;
            default:
                break;
        }
        final String tishi = msg;
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showToast(tishi);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencentAuthHandler.callOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event != null
                && event.getRepeatCount() == 0) {
//            loginSuccess("");
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int SHOW_CONTENT = 0;

    private void sendQQ() {
        // TODO Auto-generated method stub
        ProgressDlgUtil.showProgressDlg("登录中", this);
        mTencentAuthHandler.login(this, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                /**
                 * <pre>
                 * {
                 *   "id": "55474453a835b8965c000001",
                 *   "avatar": "http://q.qlogo.cn/qqapp/222222/826130D0401E93B7617C316520CD1B38/100",
                 *   "last": {
                 *     "__v": 0,
                 *     "platform": "qq",
                 *     "time": 1430979785988,
                 *     "_id": "554b04c9956c164b7800000c",
                 *     "user": "55474453a835b8965c000001",
                 *     "ip": "59.40.98.63"
                 *   },
                 *   "nickname": "MitKey",
                 *   "uname": "222222::826130D0401E93B7617C316520CD1B38"
                 * }
                 * </pre>
                 */

                ProgressDlgUtil.stopProgressDlg();
                Log.i("QQ", "msg" + response);
                loginSuccess(response);
                // try {
                // final org.json.JSONObject jsonObject = new
                // org.json.JSONObject(
                // response);
                // Message msg = new Message();
                // msg.what = SHOW_CONTENT;
                // msg.obj = jsonObject;
                // mHandler.sendMessage(msg);
                //
                // // 头像
                // if (jsonObject.has("avatar")) {
                // new Thread() {
                // @Override
                // public void run() {
                // Bitmap bitmap = null;
                // try {
                // bitmap = getbitmap(jsonObject
                // .getString("avatar"));
                // } catch (JSONException e) {
                // }
                // Message msg = new Message();
                // msg.obj = bitmap;
                // msg.what = 2;
                // mHandler.sendMessage(msg);
                // }
                // }.start();
                // }
                // Log.e("MainActivity.onClickQQLogin", jsonObject.toString());
                // } catch (org.json.JSONException e1) {
                // // TODO Auto-generated catch block
                // e1.printStackTrace();
                // }
            }

            @Override
            public void onError(String exception) {
                ProgressDlgUtil.stopProgressDlg();

                Message msg = new Message();
                msg.what = SHOW_CONTENT;
                msg.obj = exception;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancel() {
                ProgressDlgUtil.stopProgressDlg();

                Message msg = new Message();
                msg.what = SHOW_CONTENT;
                msg.obj = "取消qq登录授权";
                handler.sendMessage(msg);
            }
        });
    }

    private void loginSuccess(String msg) {
        if (msg.length() > 0 && !Util.isThirdLogin) {
            SharedPreferences.Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", true);
//			editor.putString("lizai_userName", );
            editor.putString("lizai_pwd", etPwd.getText().toString().trim());
            editor.commit();
        } else if (msg.length() > 0 && Util.isThirdLogin) {
            SharedPreferences.Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", false);
            editor.commit();
        }
        Intent intent = new Intent("com.abct.occft.hq.login");
        intent.putExtra("type", "login");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);

        Intent it = new Intent(this, ActivityHome.class);
        it.putExtra("msg", msg);
        startActivity(it);
        finish();
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
        ProgressDlgUtil.stopProgressDlg();

        if (MainLoginActivity.other != null) {
            MainLoginActivity.other.finish();
            MainLoginActivity.other = null;
        }

    }


    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXerrCode", resp.errCode + "");
        if (MyApplication.getInstance().self != null && !isLogin) {
            // 分享s
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    // 分享成功
                    showToast("分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    showToast("用户已取消");
                    // 分享取消
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    // 分享拒绝
                    showToast("用户取消权限");
                    break;
            }
            // Intent intent = new Intent(MainLoginActivity.this,
            // ShareActivity.class);
            // startActivity(intent);
            finish();
            return;
        }

        ProgressDlgUtil.stopProgressDlg();
        if (resp.errCode == 0 && resp instanceof SendAuth.Resp) {
            ProgressDlgUtil.showProgressDlg("登录中", this);
            String wxcode = ((SendAuth.Resp) resp).code;
            Log.e("WECHAT", "官方" + wxcode);
            HttpRequest.sendGet(TLUrl.getInstance().URL_wechat, "code=" + wxcode + "&appid="
                    + appid + "&iou=1", new HttpRevMsg() {

                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    try {
                        JSONObject json = new JSONObject(msg);
                        if (json != null && json.has("code")) {
                            int code = json.getInt("code");
                            if (code == 1) {
                                Util.preference.edit().putString("logintype", "WX").commit();
                                WXLoginForToken(json.getString("result"));
                                isLogin = false;
                            } else {
                                showToast("网络不好,请稍后重试");
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
