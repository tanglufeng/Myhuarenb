package com.abcs.huaqiaobang.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.AddressActivity;
import com.abcs.haiwaigou.activity.BindPhoneActivity;
import com.abcs.haiwaigou.utils.DataCleanManager;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.NoticeDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.ShareActivity;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettinsActivity extends BaseActivity {

    @InjectView(R.id.tv_cache)
    TextView tvCache;
    @InjectView(R.id.tv_pwd)
    TextView tvPwd;
    @InjectView(R.id.tv_Email)
    TextView tvEmail;
    @InjectView(R.id.tv_Phone)
    TextView tvPhone;
    @InjectView(R.id.btn_exit)
    Button btnExit;
    boolean isPayPwd = false;
    @InjectView(R.id.rl_information)
    RelativeLayout rlInformation;
    @InjectView(R.id.rl_modifyPwd)
    RelativeLayout rlModifyPwd;
    @InjectView(R.id.rl_bindPhone)
    RelativeLayout rlBindPhone;
    @InjectView(R.id.rl_bindEmail)
    RelativeLayout rlBindEmail;
    @InjectView(R.id.rl_address)
    RelativeLayout rlAddress;
    @InjectView(R.id.rl_clean)
    RelativeLayout rlClean;
    @InjectView(R.id.t_version)
    TextView tVersion;
    @InjectView(R.id.tv_paypwd)
    TextView tvPaypwd;
    @InjectView(R.id.rl_modifyPayPwd)
    RelativeLayout rlModifyPayPwd;
    private String paypwd;
    boolean isBindEmail;
    boolean isBindPhone;
    boolean isBindSuccess;
    String email;
    String phone;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settins);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            setTranslucentStatus(true);
            StatusBarCompat.compat(this, Color.parseColor("#ffffff"));
        }
        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(this));
            Log.i("zjz", "缓存=" + DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MyApplication.getInstance().self == null) {
            btnExit.setVisibility(View.GONE);
        } else {
            initUser();
        }
        if (Util.isThirdLogin) {
            rlModifyPayPwd.setVisibility(View.GONE);
        }
        if (MyApplication.getInstance().getMykey() == null) {
            rlInformation.setVisibility(View.GONE);
            rlModifyPwd.setVisibility(View.GONE);
            rlBindPhone.setVisibility(View.GONE);
            rlBindEmail.setVisibility(View.GONE);
            rlAddress.setVisibility(View.GONE);
            rlModifyPayPwd.setVisibility(View.GONE);
        }

        tVersion.setText(getResources().getString(R.string.current_version) + getVersion());
        IntentFilter filter = new IntentFilter("com.abcs.huaqiaobang.changeuser");
        registerReceiver(receiver, filter);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == "com.abcs.huaqiaobang.changeuser") {
                initUser();
            }
        }
    };

    private Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initView();
            }
        }
    };

    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    private void initView() {

        if (MyApplication.getInstance().self != null && MyApplication.getInstance().self.getEmail() != null&&tvEmail != null) {
            if (!MyApplication.getInstance().self.isInvail()) {
                tvEmail.setText(MyApplication.getInstance().self.getEmail() + "(未验证)");
            } else {
                tvEmail.setText(MyApplication.getInstance().self.getEmail() + "(已验证)");
            }
        } else {
            if (tvEmail != null)
                tvEmail.setText("请绑定邮箱");
        }
//        if (isBindEmail && tvEmail != null) {
//            tvEmail.setText(email);
//        } else if (isBindSuccess && tvEmail != null) {
//            tvEmail.setText("请去" + email + "进行验证");
//            tvEmail.setTextSize(12);
//        } else if (tvEmail != null) {
//            tvEmail.setText("请绑定邮箱");
//        }
        if (tvPhone != null)
            tvPhone.setText(isBindPhone ? phone : "请绑定手机号");
    }

    private void initUser() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_member, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.optString("member_email_bind").equals("1")) {
                                    email = data.optString("member_email");
                                    isBindEmail = true;
                                } else if (!data.optString("member_email").equals("null")) {
                                    email = data.optString("member_email");
                                    isBindSuccess = true;
                                } else {
                                    isBindSuccess = false;
                                    isBindEmail = false;
                                }
                                if (data.optString("member_mobile_bind").equals("1")) {
                                    phone = data.optString("member_mobile");
                                    isBindPhone = true;
                                } else {
                                    isBindPhone = false;
                                }

                                paypwd = data.optString("member_paypwd");
                                Log.i("zjz", "pwd=" + paypwd);
                                if (tvPwd != null)
                                    tvPwd.setText(paypwd.equals("null") ? "设置支付密码" : "修改支付密码");
                                Log.i("zjz", "isTrue=" + (paypwd.equals("null")));
                                if (paypwd.equals("null")) {
                                    isPayPwd = false;
                                } else {
                                    isPayPwd = true;
                                }


                                MyHandler.sendEmptyMessage(1);
//                                initView();
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }


    @OnClick({R.id.rl_information, R.id.rl_modifyPwd, R.id.rl_address, R.id.rl_clean,
            R.id.rl_about, R.id.rl_feedback, R.id.rl_share, R.id.btn_exit, R.id.rl_left
            , R.id.rl_bindEmail, R.id.rl_bindPhone, R.id.rl_update, R.id.rl_modifyPayPwd
    })
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {


            case R.id.rl_information:
                if (MyApplication.getInstance().self == null)
                    return;
                intent = new Intent(this, InformationActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_modifyPayPwd:
                intent = new Intent(this, ModifyPasswordActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_modifyPwd:
                if (MyApplication.getInstance().self == null)
                    return;
                if (isBindEmail || isBindPhone) {
                    intent = new Intent(this, BindPhoneActivity.class);
                    if (isPayPwd) {
                        intent.putExtra("isFirst", false);
                        intent.putExtra("title", "修改支付密码");
                    } else {
                        intent.putExtra("isFirst", true);
                        intent.putExtra("title", "设置支付密码");
                    }
                    intent.putExtra("type", BindPhoneActivity.BINDPAYPWD);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请先绑定手机号或邮箱！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rl_address:
                if (MyApplication.getInstance().self == null)
                    return;
                intent = new Intent(this, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_clean:
                try {
                    if (!tvCache.getText().toString().equals("0K")) {
                        new PromptDialog(this, "确定清空华人邦本地缓存数据？(此操作不可恢复)", new Complete() {
                            @Override
                            public void complete() {
                                try {
                                    DataCleanManager.clearAllCache(SettinsActivity.this);
                                    tvCache.setText(DataCleanManager.getTotalCacheSize(SettinsActivity.this));
                                    if (tvCache.getText().toString().equals("0K")) {
                                        showToast("清除缓存成功！");
                                    } else {
                                        showToast("清空缓存失败，请重试！");
                                    }
                                } catch (Exception e) {
                                    Log.i("zjz", "清空失败");
                                    showToast("清除缓存成功！");
                                    tvCache.setText("0K");
                                    e.printStackTrace();
                                }
                            }
                        }).show();
                    } else {
                        showToast("已经没有缓存！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_about:
                intent = new Intent(this, AboutWeActivity.class);
                intent.putExtra("url", "file:///android_asset/aboutus.html");
                intent.putExtra("title", "关于我们");
                intent.putExtra("aboutus", true);
                startActivity(intent);
                break;
            case R.id.rl_feedback:

                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_share:

                intent = new Intent(this, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:


                MyApplication.getInstance().getMainActivity().logout("true");
                finish();
                break;
            case R.id.rl_left:
                finish();
                break;

            case R.id.rl_bindEmail:
                intent = new Intent();
                intent.setClass(this, BindPhoneActivity.class);
                intent.putExtra("type", BindPhoneActivity.BINDEMAIL);
                if (isBindEmail) {

                    intent.putExtra("title", "修改绑定邮箱");
                    intent.putExtra("isFirst", false);
                } else {
                    intent.putExtra("title", "绑定邮箱");
                    intent.putExtra("isFirst", false);
                }
                startActivityForResult(intent, 4);
                break;

            case R.id.rl_bindPhone:
                intent = new Intent();
                intent.setClass(this, BindPhoneActivity.class);
                if (isBindPhone) {
                    intent.putExtra("isFirst", false);
                    intent.putExtra("title", "修改绑定手机号");
                } else {
                    intent.putExtra("isFirst", false);
                    intent.putExtra("title", "绑定手机号");
                }
                intent.putExtra("type", BindPhoneActivity.BINDPHONE);
                startActivityForResult(intent, 3);

                break;

            case R.id.rl_update:
//                upDateAPP();
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && resultCode == 1) {
//            modifyNickName(data.getStringExtra("name"));
//        }
//        if (data != null && requestCode == 2) {
//
//            modifySex(data.getStringExtra("sex"));
//            MyApplication.getInstance().self.setSex(data.getStringExtra("sex"));
//        }
        if (data != null && requestCode == 1) {
            MyApplication.getInstance().getMainActivity().logout("true");
            finish();
        }
        if (data != null && requestCode == 3) {
            initUser();
//            personalTvVPhone.setText(data.getStringExtra("bind_phone"));
        }
        if (data != null && requestCode == 4) {
            String email = data.getStringExtra("bind_email");
            if (email.length() > 8) {
                String start = email.substring(0, 3);
                String end = email.substring(email.length() - 5, email.length());
                tvEmail.setText(start + "****" + end + "(未验证)");
            } else {
                tvEmail.setText(data.getStringExtra("bind_email").substring(0) + "(未验证)");
            }
            String userName = Util.preference.getString("lizai_userName", "");
            String pwd = Util.preference.getString("lizai_pwd", "");
            if (userName.length() > 0 && pwd.length() > 0) {

                sendLogin(userName, pwd);
            }
//            initUser();
//            personalTvVEmail.setText(data.getStringExtra("bind_email"));
        }
    }

    private void sendLogin(String userName, String pwd) {
        String param = "uname=" + userName + "&upass=" + pwd + "&from=华侨帮";
        HttpRequest.sendPost(TLUrl.getInstance().URL_login, param, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                NoticeDialog.stopNoticeDlg();
                if (msg.length() == 0) {
                    NoticeDialog.stopNoticeDlg();
                    Toast.makeText(SettinsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        if (code == 1) {
                            String token = json.getString("result");
                            Util.token = token;
                            LoginForToken(token);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void LoginForToken(String token) {

        Log.i("tga", "LoginForToken=====" + token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {
                        // TODO Auto-generated method stub
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
                                    sendBroadcast(intent);

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
                        }
                    }
                });
    }


   /* private void upDateAPP() {

//        tvVUpdateDate.setText(getResources().getString(R.string.update_date) + Util.format.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在检查更新");
        dialog.show();
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {


                    @Override
                    public void onUpdateAvailable(final String result) {
                        dialog.dismiss();

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new PromptDialog(SettinsActivity.this, "有新版本，是否更新?",
                                new Complete() {

                                    @Override
                                    public void complete() {

                                        Log.i("tga", "check Bulid====");
                                        startDownloadTask(SettinsActivity.this,
                                                appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        dialog.dismiss();
                        Toast.makeText(SettinsActivity.this, "已经是最新版", Toast.LENGTH_SHORT).show();
                        Log.i("tga", "check Bulid====");
                    }
                });



        if (!MyApplication.isupdate) {
            UmengUpdateAgent.update(this);
            MyApplication.isupdate = true;
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ButterKnife.reset(this);
    }
}
