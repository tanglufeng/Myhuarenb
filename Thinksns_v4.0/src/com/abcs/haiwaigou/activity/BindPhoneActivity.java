package com.abcs.haiwaigou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
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
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.tljr_textView2)
    TextView tljrTextView2;
    @InjectView(R.id.hwg_bind_title)
    RelativeLayout hwgBindTitle;
    @InjectView(R.id.tljr_ImageView03)
    ImageView tljrImageView03;
    @InjectView(R.id.et_bind_phone)
    EditText etBindPhone;
    @InjectView(R.id.btn_get_vcode)
    Button tGetVcode;
    @InjectView(R.id.tljr_ImageView0)
    ImageView tljrImageView0;
    @InjectView(R.id.et_vcode)
    EditText etVcode;
    @InjectView(R.id.btn_bind)
    Button btnBind;
    @InjectView(R.id.linear_first_bind_phone)
    LinearLayout linearFirstBindPhone;
    @InjectView(R.id.t_choose)
    TextView tChoose;
    @InjectView(R.id.img_province)
    ImageView imgProvince;
    @InjectView(R.id.relative_choose)
    RelativeLayout relativeChoose;
    @InjectView(R.id.btn_get_bindcode)
    Button btnGetBindcode;
    @InjectView(R.id.linear_choose)
    LinearLayout linearChoose;
    @InjectView(R.id.tljr_ImageView10)
    ImageView tljrImageView10;
    @InjectView(R.id.et_get_code)
    EditText etGetCode;
    @InjectView(R.id.relative_code)
    RelativeLayout relativeCode;
    @InjectView(R.id.btn_next)
    Button btnNext;
    @InjectView(R.id.zlist_choose)
    ZrcListView zlistChoose;
    @InjectView(R.id.linear_bind)
    RelativeLayout linearBind;
    @InjectView(R.id.linear_list)
    LinearLayout linearList;
    @InjectView(R.id.ed_paypwd)
    EditText edPaypwd;
    @InjectView(R.id.ed_confirm_paypwd)
    EditText edConfirmPaypwd;
    @InjectView(R.id.btn_edit_paypwd)
    Button btnEditPaypwd;
    @InjectView(R.id.linear_edit_paypwd)
    LinearLayout linearEditPaypwd;
    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.btn_bind_email)
    Button btnBindEmail;
    @InjectView(R.id.linear_bind_email)
    RelativeLayout linearBindEmail;

    @InjectView(R.id.tv_quhao)  // 区号
            TextView tv_quhao;

    @InjectView(R.id.liner_quhao)  // 获取区号
            LinearLayout liner_quhao;

    public final static String BINDEMAIL = "bind_email";
    public final static String BINDPHONE = "bind_phone";
    public final static String BINDPAYPWD = "bind_paypwd";
    public final static String APPLYCASH = "apply_cash";

    int count = 60;
    ScheduledExecutorService scheduledExecutorService;
    String bind_phone;
    String type;
    boolean isFirst;
    boolean isChoose;
    ArrayList<String> strings = new ArrayList<String>();

    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    ListAdapter listAdapter;

    String modify_type;
    String edit_type;
    boolean isFinish;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_bind_phone);
        ButterKnife.inject(this);
        MyApplication.REGISTERACTIVITYS.add(this);
        type = getIntent().getStringExtra("type");
        tljrTextView2.setText(getIntent().getStringExtra("title"));
        initView();
        setOnListener();
    }

    private void initView() {
        isFirst = getIntent().getBooleanExtra("isFirst", false);
        if (type.equals(BINDPHONE)) {
            linearFirstBindPhone.setVisibility(isFirst ? View.GONE : View.VISIBLE);
            linearBind.setVisibility(isFirst ? View.VISIBLE : View.GONE);
            linearEditPaypwd.setVisibility(View.GONE);
            linearBindEmail.setVisibility(View.GONE);
            edit_type = "modify_mobile";
        } else if (type.equals(BINDEMAIL)) {
            linearBindEmail.setVisibility(isFirst ? View.GONE : View.VISIBLE);
            linearFirstBindPhone.setVisibility(View.GONE);
            linearBind.setVisibility(isFirst ? View.VISIBLE : View.GONE);
            linearEditPaypwd.setVisibility(View.GONE);
            edit_type = "modify_email";
        } else if (type.equals(BINDPAYPWD)) {
            linearEditPaypwd.setVisibility(isFirst ? View.VISIBLE : View.GONE);
            linearBind.setVisibility(isFirst ? View.GONE : View.VISIBLE);
            linearFirstBindPhone.setVisibility(View.GONE);
            linearBindEmail.setVisibility(View.GONE);
            edit_type = "modify_paypwd";
        }else if(type.equals(APPLYCASH)){
            linearEditPaypwd.setVisibility(View.GONE);
            linearBind.setVisibility(View.VISIBLE);
            linearFirstBindPhone.setVisibility(View.GONE);
            linearBindEmail.setVisibility(View.GONE);
            edit_type = "pd_cash";
        }
        initListChoose();
    }

    private void initListChoose() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_member, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            strings.clear();
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.optString("member_email_bind").equals("1")) {
                                    strings.add(data.optString("member_email"));
                                }
                                if (data.optString("member_mobile_bind").equals("1")) {
                                    strings.add(data.optString("member_mobile"));
                                }
//                                initView();
                                Log.i("zjz", "strings=" + strings);
                                initListDate();
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

    private void initListDate() {
        listAdapter = new ListAdapter(this, strings, zlistChoose);
        if (zlistChoose != null) {
            zlistChoose.setAdapter(listAdapter);
        }
        listAdapter.notifyDataSetChanged();
    }

    class ListAdapter extends BaseAdapter {
        private ArrayList<String> strings;
        Activity activity;
        LayoutInflater inflater = null;
        ZrcListView listView;
        //    MyListView listView;
//        public Handler handler = new Handler();


        public ListAdapter(Activity activity, ArrayList<String> strings,
                           ZrcListView listView) {
            // TODO Auto-generated constructor stub

            this.activity = activity;
            this.strings = strings;
            this.listView = listView;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            ProvinceViewHolder mHolder = null;
            final String address = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.hwg_item_choose, null);
                mHolder = new ProvinceViewHolder();
                mHolder.t_province = (TextView) convertView.findViewById(R.id.t_province);
                mHolder.linear_root = (LinearLayout) convertView.findViewById(R.id.linear_root);

                convertView.setTag(mHolder);

            } else {
                mHolder = (ProvinceViewHolder) convertView.getTag();

            }
            if (address.contains("@")) {
                mHolder.t_province.setText("邮箱" + "[" + address + "]");
            } else {
                mHolder.t_province.setText("手机" + "[" + address + "]");
            }
            mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChoose = false;
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 180, 360).setDuration(durationRotate).start();
                    linearList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearList.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                    tChoose.setText(address);
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return strings == null ? 0 : strings.size();
        }

        @Override
        public String getItem(int position) {
            if (strings != null && strings.size() != 0) {
                if (position >= strings.size()) {
                    return strings.get(0);
                }
                return strings.get(position);
            }
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

    class ProvinceViewHolder {
        TextView t_province;
        LinearLayout linear_root;
    }

    //重置读秒中
    public static final int WHAT_RESET_ING = 1;
    //重置读秒结束
    public static final int WHAT_RESET_ED = 2;
    MyHandler handler = new MyHandler(new WeakReference<BindPhoneActivity>(BindPhoneActivity.this));

    static class MyHandler extends Handler {
        WeakReference<BindPhoneActivity> weakReference;

        public MyHandler(WeakReference<BindPhoneActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            BindPhoneActivity temp = weakReference.get();
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
        tGetVcode.setText("重新获取" + "(" + count + ")");
        btnGetBindcode.setText("重新获取" + "(" + count + ")");

    }

    private void reseted() {
        tGetVcode.setText("获取短信验证");
        btnGetBindcode.setText("获取安全验证");
        tGetVcode.setEnabled(true);
        btnGetBindcode.setEnabled(true);
        count = 60;
    }

    private void startReset() {
        tGetVcode.setEnabled(false);
        btnGetBindcode.setEnabled(false);
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
            if (!isFinish) {
                message.sendToTarget();
                if (count == 0) {
                    scheduledExecutorService.shutdown();
                    message = handler.obtainMessage();
                    //发送读秒结束消息
                    message.what = WHAT_RESET_ED;
                    message.sendToTarget();
                }
            }

        }
    };

    private void setOnListener() {
        findViewById(R.id.tljr_img_regiest_back).setOnClickListener(this);
        tGetVcode.setOnClickListener(this);
        liner_quhao.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnGetBindcode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tChoose.setOnClickListener(this);
        btnEditPaypwd.setOnClickListener(this);
        btnBindEmail.setOnClickListener(this);
    }

/*

    @Override
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
                imm.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
                finish();
                break;
            case R.id.liner_quhao:
               /* Intent ui=new Intent(this,SelectQuHaoActivity.class);
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
                if (phone.length() == 0 || !isValidMobile(phone)) {
                    showToast("请输入正确的手机号码！");
                } else {

                    sendSMScode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_bind:
                String code = etVcode.getText().toString().trim();
                if (code.length() == 0) {
                    showToast("请输入手机获取到的验证码！");
                } else {
                    verifySMSCode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.t_choose:
                isChoose = !isChoose;
                if (isChoose) {
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 0, 180).setDuration(durationRotate).start();
                    linearList.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(linearList, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 180, 360).setDuration(durationRotate).start();

                    linearList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linearList.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                }
                break;
            case R.id.btn_get_bindcode:
                String choose = tChoose.getText().toString().trim();
                if (choose.length() == 0) {
                    showToast("请选择验证方式！");
                } else {
                    sendSafeCode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_next:
                String safecode = etGetCode.getText().toString().trim();
                if (safecode.length() == 0) {
                    showToast("请输安全验证码！");
                } else {
                    verifySafeCode();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_edit_paypwd:
                setPaypwd();
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_bind_email:
                String email = etEmail.getText().toString().trim();
                if (email.length() == 0 || !isEmail(email)) {
                    showToast("请输入正确的邮箱！");
                } else {
                    bindEmail();
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
        }
    }


    private void bindEmail() {

        ProgressDlgUtil.showProgressDlg("Loading...", this);
        String geturl = TLUrl.getInstance().URL_user + "bind?iou=1";
        String param = "token=" + Util.token + "&type=email&company=huaqiao"
                + "&value=" + etEmail.getText().toString().trim();

        ProgressDlgUtil.showProgressDlg("修改中", this);
        HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                ProgressDlgUtil.stopProgressDlg();

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        if (msg.length() > 0) {
                            com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(msg);
                            int code = obj.getIntValue("code");

                            if (code == 1) {


                                showToast("绑定成功,已发验证邮件到您的邮箱,请到您的邮箱完成验证");
                                Intent intent = new Intent();
                                intent.putExtra("bind_email", etEmail.getText().toString());
                                setResult(4, intent);
                                finish();

                            } else {

                                if (code == -1015) {

                                    showToast("此邮箱已绑定其它帐号");


                                } else {

                                    showToast("绑定失败");
                                }

                            }
                        } else

                        {
                            showToast("修改失败");
                        }

                    }
                });
            }
        });

//        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_email_bangding, "&email=" + etEmail.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("code") == 200) {
//                                Log.i("zjz", "msg=" + msg);
//                                showToast(object.optString("datas"));
//                                Intent intent = new Intent();
//                                intent.putExtra("bind_email", etEmail.getText().toString());
//                                setResult(4, intent);
////                                sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
//                                finish();
//                                ProgressDlgUtil.stopProgressDlg();
//                            } else {
//                                showToast("输入的邮箱格式不正确！请重新输入！");
//                                ProgressDlgUtil.stopProgressDlg();
//                                Log.i("zjz", "goodsDetail:解析失败");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
//                            showToast("输入的邮箱格式不正确！请重新输入！");
//                            ProgressDlgUtil.stopProgressDlg();
//                        }
//                    }
//                });
//
//            }
//        });

    }

    private void setPaypwd() {
        if (edPaypwd.getText().toString().equals("")) {
            showToast("支付密码不能为空！");
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
                                sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
                                showToast(object.optString("datas"));
                                finish();
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

    private void verifySafeCode() {
        ProgressDlgUtil.showProgressDlg("Loading...", BindPhoneActivity.this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_verify_code + "&type=" + edit_type, "&key=" + MyApplication.getInstance().getMykey() + "&type=" + modify_type + "&auth_code=" + etGetCode.getText().toString(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "safe_msg=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.has("member_id")) {
                                    showToast("验证成功！");
                                    Intent intent =null;
                                    if(type.equals(APPLYCASH)){
                                        intent = new Intent(BindPhoneActivity.this, ApplyCashActivity.class);
                                        intent.putExtra("cash",Double.valueOf(data.optString("available_predeposit")));
                                        finish();
                                    }else {
                                        intent = new Intent(BindPhoneActivity.this, EditBindActivity.class);
                                        intent.putExtra("type", type);
                                        intent.putExtra("title", tljrTextView2.getText().toString());
                                    }
                                    startActivity(intent);
                                } else {
                                    showToast(object.optString("datas"));
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            showToast("验证码已被使用或超时，请重新获取验证码");
//                            Toast.makeText(getActivity(), "输入的邮箱格式不正确！请重新输入！", Toast.LENGTH_SHORT).show();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void sendSafeCode() {
        String type = null;
        if (tChoose.getText().toString().contains("@")) {
            type = "email";
            modify_type = "modify_email";
        } else {
            type = "mobile";
            modify_type = "modify_mobile";
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_phone_bind_step1, "act=security&op=send_auth_code" + "&type=" + type + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
                                startReset();
                            } else {
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

    private void verifySMSCode() {
        String geturl = TLUrl.getInstance().URL_user + "bind/phoneverify?iou=1";
        String param = "user=" + MyApplication.getInstance().self.getBindId()
                + "&id=" + orderId + "&code=" + etVcode.getText().toString().trim();
        ProgressDlgUtil.showProgressDlg("验证中", this);
        System.err.println("param:" + param);
        HttpRequest.sendPost(geturl, param, new HttpRevMsg() {

            @Override
            public void revMsg(final String msg) {
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz","verify_msg="+msg);
                            String code = object.getString("code");
                            if (code.equals("1")) {
                                showToast("绑定成功");
                                Intent intent = new Intent();
                                intent.putExtra("bind_phone", bind_phone);
                                setResult(3, intent);
                                finish();
                            } else if(code.equals("-1021")) {
                                showToast("验证码错误，请重试");
                            }else if(code.equals("-1016")){
                                showToast("该手机已被绑定");
                            }else {
                                showToast("验证码错误！请重试！");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
//        ProgressDlgUtil.showProgressDlg("Loading...", BindPhoneActivity.this);
//        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_phone_bind, "&key=" + MyApplication.getInstance().getMykey() + "&mobile=" + bind_phone + "&vcode=" + etVcode.getText().toString(), new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("code") == 200) {
//                                Log.i("zjz", "msg=" + msg);
//                                showToast(object.optString("datas"));
//                                if (object.optString("datas").contains("成功")) {
//
//                                    Intent intent = new Intent();
//                                    intent.putExtra("bind_phone", bind_phone);
//                                    setResult(3, intent);
//                                    finish();
//                                }
//                                ProgressDlgUtil.stopProgressDlg();
//                            } else {
////                                Toast.makeText(getActivity(), "输入的邮箱格式不正确！请重新输入！", Toast.LENGTH_SHORT).show();
//                                ProgressDlgUtil.stopProgressDlg();
//                                Log.i("zjz", "goodsDetail:解析失败");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
////                            Toast.makeText(getActivity(), "输入的邮箱格式不正确！请重新输入！", Toast.LENGTH_SHORT).show();
//                            ProgressDlgUtil.stopProgressDlg();
//                        }
//                    }
//                });
//
//            }
//        });
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

    private void sendSMScode() {
//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_phone_bind_first, "act=security&op=send_modify_mobile" + "&mobile=" + etBindPhone.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("code") == 200) {
//                                Log.i("zjz", "msg=" + msg);
//                                showToast(object.optString("datas"));
////                                Toast.makeText(BindPhoneActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
//                                if (object.optString("datas").contains("成功")) {
//                                    startReset();
//                                    bind_phone = etBindPhone.getText().toString();
//                                }
//
//                            } else {
////                                Toast.makeText(getActivity(), "输入的邮箱格式不正确！请重新输入！", Toast.LENGTH_SHORT).show();
//                                Log.i("zjz", "goodsDetail:解析失败");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//            }
//        });
        String geturl = TLUrl.getInstance().URL_user + "bind?iou=1";
        String param = "token=" + Util.token + "&type=phone"
                + "&value=" + URLEncoder.encode(tv_quhao.getText().toString().trim()+etBindPhone.getText().toString().trim())+"&company=huaqiao";
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        System.err.println("param:" + param);
        HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                ProgressDlgUtil.stopProgressDlg();
                System.err.println("msg:" + msg);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (msg.length() > 0) {
                            Log.i("zjz","msg="+msg);
                            com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject
                                    .parseObject(msg);
                            try {
                                int code = obj.getIntValue("code");
                                if (code == 1) {

                                    startReset();
                                    orderId = obj.getString("result");
                                    showToast("短信验证已发送至" + etBindPhone.getText().toString().trim());
//                                    new EnterBindSecurityDialog(
//                                            activity, newPhone,
//                                            obj.getString("result"))
//                                            .show();
                                } else {
                                    if (code == -1016) {
                                        showToast("此手机已绑定其它帐号");
                                    } else if (code == -1024) {
                                        showToast("此类绑定暂不支持，请重试!");
                                    } else {
                                        showToast("此手机已绑定其它帐号");
                                    }
                                }
                            } catch (Exception e) {
                                showToast(obj
                                        .getString("code"));
                            }
                        } else {
                            showToast("绑定失败");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        isFinish = true;
        MyApplication.REGISTERACTIVITYS.remove(this);
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
