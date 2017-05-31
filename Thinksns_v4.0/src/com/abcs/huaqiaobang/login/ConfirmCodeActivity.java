package com.abcs.huaqiaobang.login;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmCodeActivity extends BaseActivity implements
        OnClickListener {
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public static String key, phone;
    EditText et_confirm;
    TextView txt_confirm_phone;
    int time = 60;
    TextView[] views = new TextView[6];
    LinearLayout grp_confirm;
    private TextView lastTime;
    private Button btn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        key = getIntent().getExtras().getString("key");
        phone = getIntent().getExtras().getString("phone");
        setContentView(R.layout.occft_activity_confirmcode);
        grp_confirm = (LinearLayout) findViewById(R.id.tljr_grp_confirm);
        et_confirm = (EditText) findViewById(R.id.tljr_et_confirm);
        txt_confirm_phone = (TextView) findViewById(R.id.tljr_txt_confirm_phone);
        lastTime = (TextView) findViewById(R.id.tljr_txt_confirm);
        btn = (Button) findViewById(R.id.dialog_entersecurity_btn);
        DecimalFormat mFormat = new DecimalFormat();
        mFormat.setGroupingSize(4);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setGroupingSeparator('-');
        mFormat.setDecimalFormatSymbols(dfs);

        if(!TextUtils.isEmpty(MyApplication.QU_HAO)){
            txt_confirm_phone.setText(MyApplication.QU_HAO+" "+URLEncoder.encode(phone));
        }else {
            txt_confirm_phone.setText(URLEncoder.encode(phone));
        }


//        txt_confirm_phone.setText("+86 "+ mFormat.format(Long.parseLong(phone)));
        findViewById(R.id.tljr_img_confirm_back).setOnClickListener(this);
        handler.post(runnable);
        for (int i = 0; i < views.length; i++) {
            views[i] = new TextView(this);
            views[i].setTextColor(getResources().getColor(
                    R.color.tljr_text_default));
            views[i].setTextSize(30);
            views[i].setGravity(Gravity.CENTER);
            views[i].setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
            grp_confirm.addView(views[i]);
        }
        et_confirm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                final String s = arg0.toString().trim();
                for (int i = 0; i < views.length; i++) {
                    if (i < s.length()) {
                        views[i].setText(s.charAt(i) + "");
                    } else {
                        views[i].setText("");
                    }
                }
                if (arg0.length() == 6) {
                    et_confirm.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            comfirmCode(s);
                        }
                    }, 200);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //company=huaqiao
                HttpRequest.sendPost(TLUrl.getInstance().URL_sendsms, "company=huaqiao&phone=" + phone,
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
                                    if (object.optInt("code") == 1) {
                                        String key = object.optString("result");
                                        ConfirmCodeActivity.this.key = key;
                                        ConfirmCodeActivity.this.phone = phone;
                                        time = 60;
                                        handler.post(runnable);
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
        });
//        smsObserver = new SmsObserver(this, handler);
//        getContentResolver().registerContentObserver(SMS_INBOX, true,
//                smsObserver);
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
            for (int i = 0; i < views.length; i++) {
                if (i < s.length()) {
                    views[i].setText(s.charAt(i) + "");
                } else {
                    views[i].setText("");
                }
            }
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    et_confirm.setText(s);
                }
            }, 500);
        }
    }

    private void comfirmCode(final String code) {


        HttpRequest.sendPost(TLUrl.getInstance().URL_matchcode, "code=" + code + "&id=" + key
                + "&method=regist", new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                LogUtil.e("comfirmCode", msg);
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(2);
                // {"code":"-1021"}
                // {"code":1,"result":"55691a10e224548e3a00000c"}
                try {
                    JSONObject object = new JSONObject(msg);
                    if (object.optInt("code") == 1) {
                        String key = object.optString("result");
                        Intent intent = new Intent(ConfirmCodeActivity.this,
                                RegiestLastActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", key);
                        bundle.putString("code", code);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
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

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tljr_img_confirm_back:// 返回
                Intent intent = new Intent(ConfirmCodeActivity.this,
                        RegiestActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    private void showError(int code) {
        String msg = "";
        switch (code) {
            case 1:
                msg = "手机注册失败";
                break;
            case -1021:
                msg = "短信未找到";
                break;
            case -1022:
                msg = "发送短信失败";

            default:
                msg = "验证码验证失败";
                break;
        }
        showMessage(msg);
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
                    ProgressDlgUtil.showProgressDlg("", ConfirmCodeActivity.this);
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
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (time > 0) {
                handler.postDelayed(runnable, 1000);
                time--;
                btn.setVisibility(View.GONE);
                lastTime.setVisibility(View.VISIBLE);
                lastTime.setText("重新获取(" + time + ")");
//				showMessage("获取失败，请重试!");
            } else {
                lastTime.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                showMessage("获取失败，请重试!");
            }
        }
    };

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
}
