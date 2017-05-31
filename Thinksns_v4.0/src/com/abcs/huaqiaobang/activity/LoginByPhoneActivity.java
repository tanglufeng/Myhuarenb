package com.abcs.huaqiaobang.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.utils.ACache;
import com.abcs.hqbtravel.wedgt.SelectedQuHaoDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.db.UserSqlHelper;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.exception.DataInvalidException;
import com.thinksns.sociax.thinksnsbase.network.ApiHttpClient;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginByPhoneActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.hwg_bind_title)
    RelativeLayout hwgBindTitle;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.img_clear_phone)
    ImageView imgClearPhone;
    @InjectView(R.id.et_vcode)
    EditText etVcode;
    @InjectView(R.id.t_get_code)
    TextView tGetCode;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.linear_first_bind_phone)
    LinearLayout linearFirstBindPhone;
    @InjectView(R.id.tv_quhao)  // 区号
     TextView tv_quhao;

    @InjectView(R.id.liner_quhao)  // 获取区号
    LinearLayout liner_quhao;
    int count = 60;
    ScheduledExecutorService scheduledExecutorService;
    boolean isFinish;
    ACache aCache;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone2);
        ButterKnife.inject(this);
        aCache = ACache.get(this);
        initListener();
//        smsObserver = new SmsObserver(this, handler);
//        getContentResolver().registerContentObserver(SMS_INBOX, true,
//                smsObserver);
    }

    private void initListener() {
        imgClearPhone.setVisibility(etPhone.getText().toString().length() != 0 ? View.VISIBLE : View.INVISIBLE);
        imgClearPhone.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        liner_quhao.setOnClickListener(this);
        tGetCode.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    imgClearPhone.setVisibility(View.INVISIBLE);
                } else {
                    imgClearPhone.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private SelectedQuHaoDialog quHaoDialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.img_clear_phone:
                etPhone.getText().clear();
                break;
            case R.id.liner_quhao:  // 选择区号

               /* Intent ui=new Intent(this,SelectQuHaoActivity.class);
                startActivityForResult(ui,9);*/

                if(quHaoDialog==null){
                    quHaoDialog=new SelectedQuHaoDialog(this);
                }

                quHaoDialog.show();
                quHaoDialog.setQuHao(new SelectedQuHaoDialog.GetQuHao() {
                    @Override
                    public void setQHao(String numbers) {

                        Log.i("zds_sse",numbers+"");

                        if(!TextUtils.isEmpty(numbers)){
                            tv_quhao.setText(numbers);
                        }

                    }
                });

                break;
            case R.id.btn_login:
                String phone2 = etPhone.getText().toString().trim();
                String code = etVcode.getText().toString().trim();
                if (phone2.length() == 0) {
                    showToast("手机号码不能为空");
                } else if (code.length() == 0) {
                    showToast("请输入验证码");
                } else {

                    sendLogin(URLEncoder.encode(tv_quhao.getText().toString().trim()+etPhone.getText().toString().trim()), code);
                    InputTools.HideKeyboard(v);
                }

                break;
            case R.id.t_get_code:
                String phone = etPhone.getText().toString().trim();
                if (phone.length() == 0) {
                    showToast("请输入手机号码！");
                }
//                if (phone.length() == 0 || !isValidMobile(phone)) {
//                    showToast("请输入正确的手机号码！");
//                }
                else {
                    sendSMScode(URLEncoder.encode(tv_quhao.getText().toString().trim()+phone));
                    InputTools.HideKeyboard(v);
                }

                break;
        }
    }

    String id;


/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9&&resultCode==9&&data!=null){

            String quhao=data.getStringExtra("quhao");
            if(!TextUtils.isEmpty(quhao)){
                tv_quhao.setText(quhao);
            }
        }
    }*/

    private void sendLogin(String phone, String code) {
        Log.i("zds_quhao",phone+"");
        ProgressDlgUtil.showProgressDlg("正在登录中...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_login_by_phone, "phone=" +phone + "&id=" + id + "&code=" + code + "&platform=huaqiao", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.length() == 0) {
                            showToast("服务器异常，请稍后再试");
                            return;
                        }
                        try {

                            Log.i("zds",msg+"");

                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                if (code == 1) {
                                    String token = json.getString("result");
                                    Util.token = token;
                                    WXLoginForToken(token);
                                } else {
                                    showError(code);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });
            }
        });
    }

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
                            Log.i("zjz", "login_msg=" + msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.e("WECHAT", "自己服务器的code" + code);
                                if (code == 1) {
                                    Util.token = token;
                                    Util.preference.edit().putString("token", Util.token).commit();

                                    JSONObject obj = new JSONObject(json.getString("result"));


                                    JSONObject sns = obj.getJSONObject("sns");

                                    if (!sns.has("uid")) {
                                        showToast("登录失败,请检查您的网络");
                                        return;
                                    }


                                    ModelUser newUser = new ModelUser(sns);
                                    newUser.setFace(obj.optString("avatar"));
                                    newUser.setPostUface(obj.optString("avatar"));

                                    LogUtil.i("haologin", "avator: " + newUser.getFace());

                                    String oauth_token = sns.getString("oauth_token");
                                    String oauth_token_secret = sns.getString("oauth_token_secret");
                                    ApiHttpClient.TOKEN = oauth_token;
                                    ApiHttpClient.TOKEN_SECRET = oauth_token_secret;
                                    newUser.setToken(oauth_token);
                                    newUser.setSecretToken(oauth_token_secret);

                                    Thinksns.setMy(newUser);
                                    //添加用户信息至数据库
                                    UserSqlHelper db = UserSqlHelper.getInstance(LoginByPhoneActivity.this);
                                    db.addUser(newUser, true);
                                    String username = newUser.getUserName();
                                    if (!db.hasUname(username))
                                        db.addSiteUser(username);


                                    loginSuccess(json.getString("result"));

                                    Log.e("WECHAT", "Util.token =" + Util.token);
                                } else {
                                    showError(code);
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

    private void loginSuccess(String msg) {
        if (msg.length() > 0 && !Util.isThirdLogin) {
            SharedPreferences.Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", true);
            if (aCache.getAsString(TLUrl.getInstance().LOGINTOKEN) != null)
                aCache.remove(TLUrl.getInstance().LOGINTOKEN);
            aCache.put(TLUrl.getInstance().LOGINTOKEN, Util.token, 5 * 24 * 60 * 60);
//			editor.putString("lizai_userName", );
//            editor.putString("lizai_pwd", etPwd.getText().toString().trim());
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

        if (WXEntryActivity.other != null) {
            WXEntryActivity.other.finish();
            WXEntryActivity.other = null;
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ProgressDlgUtil.showProgressDlg("", LoginByPhoneActivity.this);
                    break;
                case 2:
                    ProgressDlgUtil.stopProgressDlg();
                    break;

                default:
                    showToast(msg.obj.toString());
                    break;
            }
        }
    };

    private void showError(int code) {
        String msg = "";
        switch (code) {
            case -1000:
                msg = "用户不存在";
                break;
            case -1001:
                msg = "用户名或密码错误";
                break;
            case 1:
                msg = "手机登录成功";
                break;
            case -1021:
                msg = "短信未找到";
                break;
            case -1022:
                msg = "发送短信失败";
                break;
            default:
                msg = "手机短信发送失败";
                break;
        }
        showMessage(msg);
    }

    private void showMessage(String msg) {
        Message message = new Message();
        message.obj = msg;
        handler.sendMessage(message);
    }


    //重置读秒中
    public static final int WHAT_RESET_ING = 1;
    //重置读秒结束
    public static final int WHAT_RESET_ED = 2;
    MyHandler myhandler = new MyHandler(new WeakReference<LoginByPhoneActivity>(LoginByPhoneActivity.this));

    static class MyHandler extends Handler {
        WeakReference<LoginByPhoneActivity> weakReference;

        public MyHandler(WeakReference<LoginByPhoneActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            LoginByPhoneActivity temp = weakReference.get();
            switch (msg.what) {
                case WHAT_RESET_ING:
                    temp.reseting();
                    break;
                case WHAT_RESET_ED:
                    temp.reseted();
                    break;
            }
        }
    }

    private void reseting() {
        tGetCode.setText("重新获取" + "(" + count + ")");

    }

    private void reseted() {
        tGetCode.setText("获取验证码");
        tGetCode.setClickable(true);
        count = 60;
    }

    private void startReset() {
        tGetCode.setClickable(false);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count--;
            Message message = myhandler.obtainMessage();
            //发送读秒过程消息
            message.what = WHAT_RESET_ING;
            if (!isFinish) {
                message.sendToTarget();
                if (count == 0) {
                    scheduledExecutorService.shutdown();
                    message = myhandler.obtainMessage();
                    //发送读秒结束消息
                    message.what = WHAT_RESET_ED;
                    message.sendToTarget();
                }
            }

        }
    };

    private void sendSMScode(final String phone) {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_sendsms, "phone=" + phone + "&company=huaqiao&method=login",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        // TODO Auto-generated method stub
                        LogUtil.e("getKey", msg);
                        handler.sendEmptyMessage(2);
                        // {"code":1,"result":"556912bfe224548e3a000006"}
                        // {"code":""}
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "loginByPhone=" + msg);
                            if (object.optInt("code") == 1) {
                                id = object.optString("result");
                                startReset();
                                showToast("短信验证已发送至" + etPhone.getText().toString().trim());
                            } else {
                                showError(object.optInt("code"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            showError(0);
                        }
                    }
                });
    }

    private boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    /**
     * 从字符串中截取连续6位数字 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }

    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "address", "person"};// "_id",
        // "address",
        // "person",, "date",
        // "type
        String where = " date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            final String s = getDynamicPassword(body);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    etVcode.setText(s);
                }
            }, 500);
        }
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinish = true;
    }
}
