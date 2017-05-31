package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.utils.Code;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONObject;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private ImageView code;
    private String getCode = null; // 获取验证码的值
    private String userName = null;// 用户名
    private String method = null;// 类型
    private EditText edit;
    private String getemailorphone;
    private RelativeLayout rl_phone;
    private EditText ed_phone1, ed_phone2, edit_code;
    private TextView tv_updata;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.hwg_activity_forget_pwd,null);
        }
        setContentView(view);
        findView();
    }

    private void findView() {
        ed_phone1 = (EditText) findViewById(R.id.edit_pwd1);
        ed_phone2 = (EditText) findViewById(R.id.edit_pwd2);
        edit_code = (EditText) findViewById(R.id.edit_code);
        tv_updata = (TextView) findViewById(R.id.updata);
        code = (ImageView) findViewById(R.id.tljr_txt_futures_join_confirm);
        edit = (EditText) findViewById(R.id.tljr_et_futures_join_confirm);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        code.setImageBitmap(Code.getInstance().getBitmap());
        getCode = Code.getInstance().getCode();
        userName = getIntent().getStringExtra("userName");
        method = getIntent().getStringExtra("method");
        if (method.equals("email")) {
            ((TextView) findViewById(R.id.you)).setText("您已绑定的邮箱 :");
            ((TextView) findViewById(R.id.sure)).setText("通过邮箱找回");
            getemailorphone = getIntent().getStringExtra("email");
        } else if (method.equals("phone")) {
            ((TextView) findViewById(R.id.you)).setText("您已绑定的手机 :");
            ((TextView) findViewById(R.id.sure)).setText("通过手机找回");
            getemailorphone = getIntent().getStringExtra("phone");
        }
        ((TextView) findViewById(R.id.number)).setText(getemailorphone);
        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(this);
        findViewById(R.id.tljr_txt_futures_join_confirm).setOnClickListener(
                this);
        findViewById(R.id.sure_phone).setOnClickListener(this);
        tv_updata.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tljr_per_btn_lfanhui:
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(arg0.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
            case R.id.sure:
                Log.i("zjz", "getCode :" + getCode + "   edi :"
                        + edit.getText().toString());
                if (!edit.getText().toString().equalsIgnoreCase(getCode)) {
                    showToast("验证码输入错误!");
                    return;
                }
                if (rl_phone.getVisibility() == View.GONE && edit.getText().toString().equalsIgnoreCase(getCode)) {
                    findpassword();
                    findViewById(R.id.sure).setEnabled(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            findViewById(R.id.sure).setEnabled(true);
                        }
                    },10000);


                }
                break;
            case R.id.tljr_txt_futures_join_confirm:
                code.setImageBitmap(Code.getInstance().getBitmap());
                getCode = Code.getInstance().getCode();
                break;
            case R.id.sure_phone:
                findpasswordphone();
                break;
            case R.id.updata:
                if (time <= 0) {
                    findpassword2();
                    time = 30;
                    tv_updata.setText("（" + time + "）秒后可重新发送短信验证码");
                    tv_updata.setTextColor(Color.GRAY);
                    handler.postDelayed(run, 1000);
                }
                break;
            default:
                break;
        }
    }

    String resultStr = "";

    private void findpassword() {
        // 通过邮箱/手机找回
        HttpRequest.sendPost( TLUrl.getInstance().URL_forgotpwd, "company=huaqiao&uname=" + userName + "&method=" + method, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                Log.i("zjz", "findpass :" + msg);
                if (msg.length() == 0) {
                    showToast("连接服务器失败，请重试");
                    return;
                } else {
                    try {
                        JSONObject result = new JSONObject(msg);
                        int code = result.optInt("code");
                        if (method.equals("email")) {
                            if (code == 1) {
                                resultStr = result.optString("result");
                                handler.sendEmptyMessage(0);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        } else {
                            if (code == 1) {
                                resultStr = result.optString("result");
                                handler.sendEmptyMessage(2);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

        });
    }

    private void findpassword2() {
        // 通过邮箱/手机找回
        HttpRequest.sendPost( TLUrl.getInstance().URL_forgotpwd, "company=huaqiao&uname=" + userName + "&method=" + method, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {

            }
        });
    }

    private void findpasswordphone() {
        ProgressDlgUtil.showProgressDlg("Loading...",this);
        String pwd1 = ed_phone1.getText().toString();
        String pwd2 = ed_phone2.getText().toString();
        String code = edit_code.getText().toString();
        if (!pwd1.equals("") || !pwd2.equals("") || !code.equals("")) {
            if (pwd1.length() > 5) {
                if (pwd1.equals(pwd2)) {
                    HttpRequest.sendPost(TLUrl.getInstance().URL_changepwd, "id=" + resultStr
                            + "&upass=" + pwd1 + "&repass=" + pwd2 + "&code="
                            + code, new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("zjz", "phone :" + msg);
                                    try{
                                        JSONObject ob =new JSONObject(msg);
                                        if (ob.optInt("code") == 1) {
                                            handler.sendEmptyMessage(6);
                                        }else {
                                            showToast("修改失败！请重试！");
                                        }
                                    }catch(Exception e){

                                    }finally {
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                }
                            });
                        }
                    });
                } else {
                   showToast( "密码填写不一致");
                }
            } else {
                showToast( "密码最少6位数哦！");
            }
        } else {
            showToast( "输入框不能为空");
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    showDialog(resultStr);
                    break;
                case 1:
                    showToast("找回密码失败");
                    break;
                case 2:
                    rl_phone.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(3);
                    findViewById(R.id.sure).setVisibility(View.GONE);
                    break;
                case 3:
                    int y = (int) rl_phone.getHeight() + (int) rl_phone.getY();
                    Animation animation = new TranslateAnimation(0, 0, -y, 0);
                    animation.setFillAfter(true);
                    animation.setDuration(500);
                    rl_phone.startAnimation(animation);
                    time = 30;
                    tv_updata.setText("重新获取(" + time + ")");
                    tv_updata.setTextColor(Color.GRAY);
                    handler.postDelayed(run, 1000);
                    break;
                case 4:
                    handler.post(run);
                    break;
                case 5:
                    tv_updata.setText("获取短信验证码");
                    tv_updata.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 6:
                    showDialog("修改成功");
                    break;
                default:
                    break;
            }
        }
    };

    int time = 0;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (time > 0) {
                tv_updata.setText("（" + time + "）秒后可重新发送短信验证码");
                time--;
                handler.sendEmptyMessageDelayed(4, 1000);
            } else {
                handler.sendEmptyMessage(5);
            }
        }
    };

    public void showDialog(final String msg) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ForgetPwdActivity.this);
                builder.setTitle("提示");
                builder.setMessage(msg);
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                InputTools.HideKeyboard(view);
                                finish();
                            }
                        });
                builder.show();
            }
        });

    }
}
