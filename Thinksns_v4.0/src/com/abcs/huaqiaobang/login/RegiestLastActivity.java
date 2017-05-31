package com.abcs.huaqiaobang.login;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;

import org.json.JSONException;
import org.json.JSONObject;

public class RegiestLastActivity extends BaseActivity implements OnClickListener {
    String key, code;
    EditText et_regiestlast_pwd, et_regiestlast_rpwd;
    Button btn_regiestllast_regiest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        key = getIntent().getExtras().getString("key");
        code = getIntent().getExtras().getString("code");
        setContentView(R.layout.occft_activity_regiestlast);
        et_regiestlast_pwd = (EditText) findViewById(R.id.tljr_et_regiestlast_pwd);
        et_regiestlast_rpwd = (EditText) findViewById(R.id.tljr_et_regiestlast_rpwd);
        btn_regiestllast_regiest = (Button) findViewById(R.id.tljr_btn_regiestllast_regiest);
        findViewById(R.id.tljr_img_regiestlast_back).setOnClickListener(this);
        btn_regiestllast_regiest.setOnClickListener(this);
    }

    private void regiest() {
        String pwd = et_regiestlast_pwd.getText().toString().trim();
        String rpwd = et_regiestlast_rpwd.getText().toString().trim();
        if (pwd.length() == 0) {
            et_regiestlast_pwd.requestFocus();
            showMessage("请输入密码");
            return;
        }
        if (rpwd.length() == 0) {
            showMessage("请输入确认密码");
            et_regiestlast_rpwd.requestFocus();
            return;
        }
        if (pwd.length() < 6 && pwd.length() > 12) {
            showMessage("密码长度不对");
            et_regiestlast_pwd.requestFocus();
            return;
        }
        if (!pwd.equals(rpwd)) {
            showMessage("两次密码不一致");
            et_regiestlast_rpwd.requestFocus();
            return;
        }
        sendRegiest(pwd, rpwd);
    }

    private void sendRegiest(final String pwd, final String rpwd) {
        handler.sendEmptyMessage(1);

        HttpRequest.sendPost(TLUrl.getInstance().URL_registbyphone, "code=" + code + "&id="
                        + key + "&method=regist&upass=" + pwd + "&repass=" + rpwd,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("sendRegiest", msg);
                        // TODO Auto-generated method stub
                        // {"code":"-1021"}验证码不对
                        // {"code":"-1006"}用户已存在
                        // {"code":1,"result":"8dcc5285a82399527b57c042264b9ade"}
                        handler.sendEmptyMessage(2);
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 1) {
                                showMessage("注册成功,正在登录中");
                                login(object.getString("result"));
                            } else {
                                showError(object.optInt("code"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            showError(-1005);
                        }
                    }
                });

//


    }

    private void login(String token) {
        handler.sendEmptyMessage(1);
        HttpRequest.sendGet(TLUrl.getInstance().URL_oauth, "token=" + token + "&iou=1",
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("login", msg);
                        handler.sendEmptyMessage(2);
                        if (msg.length() <= 0) {
                            showError(-1005);
                            return;
                        }
                        try {
                            final JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                if (code == 1) {
                                    loginSuccess(json.getString("result"));
                                } else {
                                    showError(code);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loginSuccess(String msg) {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("login", msg);
//		startActivity(intent);

        //////////////////////////////////////////

        if (msg.length() > 0) {
            Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", true);
            editor.putString("lizai_pwd", et_regiestlast_pwd.getText()
                    .toString().trim());
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

    }

    private void showError(int code) {
        String msg = "";
        switch (code) {
            case -1:
                msg = "参数非法";
                break;
            case -1000:
                msg = "用户不存在";
                break;
            case -1001:
                msg = "登录失败";
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
            case -1021:
                msg = "短信未找到";
                break;
            case -1022:
                msg = "发送短信失败";
                break;
            default:
                msg = "获取验证码失败";
                break;
        }
        showMessage(msg);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tljr_img_regiestlast_back:// 返回
                Intent intent = new Intent(RegiestLastActivity.this,
                        RegiestActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tljr_btn_regiestllast_regiest:// 下一步
                regiest();
                break;

            default:
                break;
        }
    }

    private void showMessage(String msg) {
        Message message = new Message();
        message.obj = msg;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ProgressDlgUtil.showProgressDlg("", RegiestLastActivity.this);
                    break;
                case 2:
                    ProgressDlgUtil.stopProgressDlg();
                    break;

                default:
                    showToast(msg.obj.toString());
                    break;
            }
        }

        ;
    };
}
