package com.abcs.haiwaigou.local.huohang.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HuoHangEditAddressActivity extends BaseActivity implements View.OnClickListener {


    public Handler handler = new Handler();
    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_ok)
    TextView tOk;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    @InjectView(R.id.ed_store_name)
    EditText edStoreName;
    @InjectView(R.id.ed_address)
    EditText edAddress;
    @InjectView(R.id.ed_youbian)
    EditText edYoubian;
    @InjectView(R.id.ed_phone_beiyong)
    EditText edPhoneBeiyong;
    @InjectView(R.id.ed_my_phone)
    EditText edMyPhone;
    @InjectView(R.id.ed_my_phone_beiyong)
    EditText edMyPhoneBeiyong;
    @InjectView(R.id.btn_isdefault)
    TextView btnIsdefault;
    @InjectView(R.id.tv_xinxin)
    TextView tvXinxin;
    @InjectView(R.id.tv_jialing)
    TextView tvJialing;

    String param;
    String isDefault="0";

    String address_id,memberId,mobPhone,address,telPhone,trueName;

    boolean isAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_edit_address);
        ButterKnife.inject(this);

        isAdd=getIntent().getBooleanExtra("isAdd",false);
        address_id=getIntent().getStringExtra("address_id");
        int bstart=tvXinxin.getText().toString().trim().indexOf("*");
        int bend=bstart+"*".length();
        int fstart=tvJialing.getText().toString().trim().indexOf("‘0’");
        int fend=fstart+"‘0’".length();
        SpannableStringBuilder style=new SpannableStringBuilder(tvXinxin.getText().toString().trim());
        style.setSpan(new ForegroundColorSpan(Color.RED),bstart,bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder style2=new SpannableStringBuilder(tvJialing.getText().toString().trim());
        style2.setSpan(new ForegroundColorSpan(Color.RED),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvXinxin.setText(style);
        tvJialing.setText(style2);

        if(isAdd){  // 添加新地址

        }else {
            trueName=getIntent().getStringExtra("trueName");
            memberId=getIntent().getStringExtra("memberId");
            telPhone=getIntent().getStringExtra("telPhone");
            mobPhone=getIntent().getStringExtra("mobPhone");
            address=getIntent().getStringExtra("address");
            isDefault=getIntent().getStringExtra("isDefault");

            if(!TextUtils.isEmpty(telPhone)){
                edPhone.setText(telPhone);
            }
            if(!TextUtils.isEmpty(trueName)){
                edStoreName.setText(trueName);
            }

            if(!TextUtils.isEmpty(mobPhone)){
                edMyPhone.setText(mobPhone);
            }

            if(!TextUtils.isEmpty(address)){
                edAddress.setText(address);
            }

            if(!TextUtils.isEmpty(isDefault)&&isDefault.equals("1")){  // 默认
                btnIsdefault.setBackgroundResource(R.drawable.iv_sele_y);
            }else {
                btnIsdefault.setBackgroundResource(R.drawable.iv_sele_l);
            }
        }
    }

    // 修改地址
    private void save() {
        Log.i("zds", "address_id=" + address_id);
        param = "&key=" + MyApplication.getInstance().getMykey() + "&address="
                + e_Address + "&address_id=" + address_id + "&is_default=" + isDefault + "&tel_phone=" +
                phone + "&true_name=" + storeName;

        Log.i("zds", "param=" + param);

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=native&op=edit_native", param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("state")) {
                                int state = json.getInt("state");
                                Log.i("zds", "edit_msg=" + msg);
                                if (state == 1) {
                                    showToast("修改成功！");
                                    MyUpdateUI.sendUpdateCollection(HuoHangEditAddressActivity.this, MyUpdateUI.ADDRESS);
                                    finish();
                                } else {
                                    showToast("失败！");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    // 添加地址
    private void addAddress() {
        param = "&key=" + MyApplication.getInstance().getMykey() + "&address="
                + e_Address + "&is_default=" + isDefault + "&tel_phone=" +
                phone + "&true_name=" + storeName;

        Log.i("zds", "param=" + param);

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=native&op=add_native_address", param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("state")) {
                                int state = json.getInt("state");
                                Log.i("zds", "edit_msg=" + msg);
                                if (state == 1) {
                                    showToast("修改成功！");
                                    MyUpdateUI.sendUpdateCollection(HuoHangEditAddressActivity.this, MyUpdateUI.ADDRESS);
                                   /* Intent intent=new Intent(HuoHangEditAddressActivity.this, BenDiPeiSongActivity3.class);
                                    intent.putExtra("district_id","1");
                                    intent.putExtra("store_name","我的店");
                                    intent.putExtra("district_name","维也纳");
                                    startActivity(intent);*/
                                    finish();
                                } else {
                                    showToast("失败！");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.relative_back, R.id.t_ok,R.id.btn_isdefault})
    public void onClick(View view) {

        InputMethodManager imm;
        switch (view.getId()) {
            case R.id.relative_back:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                break;
            case R.id.t_ok:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                comfirm();
                break;
            case R.id.btn_isdefault:

                if(is_select){
                    btnIsdefault.setBackgroundResource(R.drawable.iv_sele_y);
                    isDefault="1"; // 1为设置为默认，0为取消默认
                    is_select=false;
                }else {
                    btnIsdefault.setBackgroundResource(R.drawable.iv_sele_l);
                    isDefault="0"; // 1为设置为默认，0为取消默认
                    is_select=true;
                }
                break;

        }
    }

    private boolean is_select=true;
    String phone,storeName,e_Address;
    private void comfirm() {
         phone = edPhone.getText().toString().trim();
         storeName = edStoreName.getText().toString().trim();
         e_Address = edAddress.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            showToast("电话不能为空！");
        } else if (TextUtils.isEmpty(storeName)) {
            showToast("店名不能为空！");
        } else if (TextUtils.isEmpty(e_Address)) {
            showToast("地址不能为空！");
        } else {
            if(isAdd){  // 添加新地址
                addAddress();
            }else {
                save();
            }
        }
    }
    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
