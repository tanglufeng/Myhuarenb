package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.wedgt.SelectedQuHaoDialog;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditBindActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.hwg_bind_title)
    RelativeLayout hwgBindTitle;
    @InjectView(R.id.tljr_ImageView03)
    ImageView tljrImageView03;
    @InjectView(R.id.et_bind_phone)
    EditText etBindPhone;
    @InjectView(R.id.btn_get_vcode)
    Button btnGetVcode;
    @InjectView(R.id.tljr_ImageView0)
    ImageView tljrImageView0;
    @InjectView(R.id.et_vcode)
    EditText etVcode;
    @InjectView(R.id.btn_bind_phone)
    Button btnBindPhone;
    @InjectView(R.id.linear_bind_phone)
    LinearLayout linearBindPhone;
    @InjectView(R.id.tljr_ImageView10)
    ImageView tljrImageView10;
    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.relative_code)
    RelativeLayout relativeCode;
    @InjectView(R.id.btn_bind_email)
    Button btnBindEmail;
    @InjectView(R.id.linear_bind_email)
    RelativeLayout linearBindEmail;
    @InjectView(R.id.ed_paypwd)
    EditText edPaypwd;
    @InjectView(R.id.ed_confirm_paypwd)
    EditText edConfirmPaypwd;
    @InjectView(R.id.btn_edit_paypwd)
    Button btnEditPaypwd;
    @InjectView(R.id.linear_edit_paypwd)
    LinearLayout linearEditPaypwd;

    @InjectView(R.id.tv_quhao)  // 区号
            TextView tv_quhao;

    @InjectView(R.id.liner_quhao)  // 获取区号
            LinearLayout liner_quhao;


    String type;
    String bind_phone;
    int count = 60;
    ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_edit_bind);
        ButterKnife.inject(this);
        MyApplication.REGISTERACTIVITYS.add(this);
        type = getIntent().getStringExtra("type");
        tTitle.setText(getIntent().getStringExtra("title"));
        initView();
        setOnListener();
    }

    private void initView() {
        if (type.equals(BindPhoneActivity.BINDPHONE)) {
            linearBindPhone.setVisibility(View.VISIBLE);
            linearBindEmail.setVisibility(View.GONE);
            linearEditPaypwd.setVisibility(View.GONE);
        } else if (type.equals(BindPhoneActivity.BINDEMAIL)) {
            linearBindPhone.setVisibility(View.GONE);
            linearBindEmail.setVisibility(View.VISIBLE);
            linearEditPaypwd.setVisibility(View.GONE);
        } else if (type.equals(BindPhoneActivity.BINDPAYPWD)) {
            linearBindPhone.setVisibility(View.GONE);
            linearBindEmail.setVisibility(View.GONE);
            linearEditPaypwd.setVisibility(View.VISIBLE);
        }
    }

    private void setOnListener() {
        findViewById(R.id.tljr_img_regiest_back).setOnClickListener(this);
        btnGetVcode.setOnClickListener(this);
        liner_quhao.setOnClickListener(this);
        btnBindPhone.setOnClickListener(this);
        btnBindEmail.setOnClickListener(this);
        btnEditPaypwd.setOnClickListener(this);
    }

    //重置读秒中
    public static final int WHAT_RESET_ING = 1;
    //重置读秒结束
    public static final int WHAT_RESET_ED = 2;
    MyHandler handler = new MyHandler(new WeakReference<EditBindActivity>(EditBindActivity.this));

    static class MyHandler extends Handler {
        WeakReference<EditBindActivity> weakReference;

        public MyHandler(WeakReference<EditBindActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            EditBindActivity temp = weakReference.get();
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
        btnGetVcode.setText("重新获取" + "(" + count + ")");

    }

    private void reseted() {
        btnGetVcode.setText("获取短信验证");
        btnGetVcode.setEnabled(true);
        count = 60;
    }

    private void startReset() {
        btnGetVcode.setEnabled(false);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count--;
            Message message = handler.obtainMessage();
            //发送读秒过程消息
            message.what = WHAT_RESET_ING;
            message.sendToTarget();
            if (count == 0) {
                scheduledExecutorService.shutdown();
                message = handler.obtainMessage();
                //发送读秒结束消息
                message.what = WHAT_RESET_ED;
                message.sendToTarget();
            }
        }
    };


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9&&resultCode==9&&data!=null){

            String quhao=data.getStringExtra("quhao");
            if(!TextUtils.isEmpty(quhao)){
                tv_quhao.setText(quhao);
            }
        }
    }*/

    SelectedQuHaoDialog quHaoDialog;

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.tljr_img_regiest_back:
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.liner_quhao:  // 选择区号

              /*  Intent ui=new Intent(this,SelectQuHaoActivity.class);
                startActivityForResult(ui,9);*/

                if(quHaoDialog==null){
                    quHaoDialog=new SelectedQuHaoDialog(this);
                }

                quHaoDialog.show();
                quHaoDialog.setQuHao(new SelectedQuHaoDialog.GetQuHao() {
                    @Override
                    public void setQHao(String numbers) {

                        if(!TextUtils.isEmpty(numbers)){
                            tv_quhao.setText(numbers);
                        }

                    }
                });

                break;
            case R.id.btn_get_vcode:
                String phone = etBindPhone.getText().toString().trim();
                if (phone.length() == 0) {
                    showToast("请输入手机号码！");
                }
//                if (phone.length() == 0 || !isValidMobile(phone)) {
//                    showToast("请输入正确的手机号码！");
//                }
                else {
                    sendSMScode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_bind_phone:
                String code = etVcode.getText().toString().trim();
                if (code.length() == 0) {
                    showToast("请输入手机获取到的验证码！");
                } else {
                    verifySMSCode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_bind_email:
                String email = etEmail.getText().toString().trim();
                if (email.length() == 0||!isEmail(email)) {
                    showToast("请输入正确的邮箱！");
                } else {
                    bindEmail();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);

                break;
            case R.id.btn_edit_paypwd:
                setPaypwd();
                break;
        }
    }

    private void setPaypwd() {
        String pwd=edPaypwd.getText().toString();
        if (pwd.equals("")) {
            showToast("支付密码不能为空！");
            return;
        } else if (pwd.length()<6){
            showToast("支付密码不能少于6位字符！");
            return;
        } else if (edConfirmPaypwd.getText().toString().equals("")) {
            showToast("确认支付密码不能为空！");
            return;
        } else if (!edPaypwd.getText().toString().equals(edConfirmPaypwd.getText().toString())) {
            showToast("两次密码不一样！请重新输入！");
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_paypwd, "&password=" + edPaypwd.getText().toString() + "&confirm_password=" + edConfirmPaypwd.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                showToast(object.optString("datas"));
                                sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
                                Iterator<BaseActivity> iterator = MyApplication.REGISTERACTIVITYS.iterator();
                                while (iterator.hasNext()) {
                                    iterator.next().finish();
                                }
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


    private void bindEmail() {

        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_email_bangding, "&email=" + etEmail.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                showToast(object.optString("datas"));
                                sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
                                Iterator<BaseActivity> iterator = MyApplication.REGISTERACTIVITYS.iterator();
                                while (iterator.hasNext()) {
                                    iterator.next().finish();
                                }
//                                Intent intent=new Intent();
//                                intent.putExtra("bind_email",etEmail.getText().toString());
//                                setResult(4,intent);
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                showToast("输入的邮箱格式不正确！请重新输入！");
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            showToast("输入的邮箱格式不正确！请重新输入！");
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });

    }

    private void verifySMSCode() {
        ProgressDlgUtil.showProgressDlg("Loading...", EditBindActivity.this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_phone_bind, "&key=" + MyApplication.getInstance().getMykey() + "&mobile=" + bind_phone + "&vcode=" + etVcode.getText().toString(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                showToast(object.optString("datas"));
                                if (object.optString("datas").contains("成功")) {
                                    sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
                                    Iterator<BaseActivity> iterator = MyApplication.REGISTERACTIVITYS.iterator();
                                    while (iterator.hasNext()) {
                                        iterator.next().finish();
                                    }

//                                    Intent intent = new Intent();
//                                    intent.putExtra("bind_phone", bind_phone);
//                                    setResult(3, intent);
                                }
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

    private void sendSMScode() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_phone_bind_first, "act=security&op=send_modify_mobile" + "&mobile=" + URLEncoder.encode(tv_quhao.getText().toString().trim()+etBindPhone.getText().toString())+ "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                showToast(object.optString("datas"));
//                                Toast.makeText(BindPhoneActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
                                if (object.optString("datas").contains("成功")) {
                                    startReset();

                                    bind_phone =URLEncoder.encode(tv_quhao.getText().toString().trim()+etBindPhone.getText().toString());
                                }

                            } else {
//                                Toast.makeText(getActivity(), "输入的邮箱格式不正确！请重新输入！", Toast.LENGTH_SHORT).show();
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public Boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);


        return m.matches();
    }

    private boolean isValidEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    private boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    @Override
    protected void onDestroy() {
        MyApplication.REGISTERACTIVITYS.remove(this);
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
