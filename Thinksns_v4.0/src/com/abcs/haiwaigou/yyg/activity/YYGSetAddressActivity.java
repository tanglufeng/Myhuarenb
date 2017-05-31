package com.abcs.haiwaigou.yyg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AddressActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGSetAddressActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_no_add)
    TextView tNoAdd;
    @InjectView(R.id.rl_choose)
    RelativeLayout rlChoose;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.linear_name)
    LinearLayout linearName;
    @InjectView(R.id.t_address)
    TextView tAddress;
    @InjectView(R.id.linear_add)
    LinearLayout linearAdd;
    @InjectView(R.id.t_id_card)
    TextView tIdCard;
    @InjectView(R.id.img_edit)
    ImageView imgEdit;
    @InjectView(R.id.linear_address)
    RelativeLayout linearAddress;
    @InjectView(R.id.linear_address_root)
    LinearLayout linearAddressRoot;
    @InjectView(R.id.relative_top)
    RelativeLayout relativeTop;
    @InjectView(R.id.img_location)
    ImageView imgLocation;
    @InjectView(R.id.img_more)
    ImageView imgMore;
    @InjectView(R.id.t_ok)
    TextView tOk;

    String address_info;
    String province;
    String city;
    String area;
    String address_detail;
    String address_id;
    String name;
    String phone;
    String act_id;
    String goods_id;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_yyg_activity_yygset_address);
        ButterKnife.inject(this);
        act_id=getIntent().getStringExtra("act_id");
        goods_id=getIntent().getStringExtra("goods_id");

        setOnListener();
    }

    private void setOnListener() {
        rlChoose.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        linearAddress.setOnClickListener(this);
        tOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.relative_back:
                finish();
                break;
            case R.id.rl_choose:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(this, AddressActivity.class);
                    intent.putExtra("isYYG", true);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.linear_address:
                intent = new Intent(this, AddressActivity.class);
                intent.putExtra("isYYG", true);
                startActivityForResult(intent, 1);
                break;
            case R.id.t_ok:
//                sendSuccess();
                tOk.setEnabled(false);
                confirm();
                break;
        }
    }

    private void confirm() {
        ProgressDlgUtil.showProgressDlg("Loading...", YYGSetAddressActivity.this);
        String param="&goods_id=" +goods_id+"&address_id="+address_id+"&key="+MyApplication.getInstance().getMykey()+"&actId="+act_id;
        String url=TLUrl.getInstance().URL_hwg_yyg_send_goods;
        HttpRequest.sendPost(url,param , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "send_msg=" + msg);
                            if (object.getInt("code") == 200) {
                                if(object.optString("datas").contains("success")){
                                    showToast("地址提交成功！我们会尽快为您发货");
                                    MyUpdateUI.sendUpdateCollection(YYGSetAddressActivity.this,MyUpdateUI.YYGSETADDRESS);
                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                    ProgressDlgUtil.stopProgressDlg();
                                }else {
                                    showToast("服务器访问失败，请重试");
                                    if(tOk!=null)
                                        tOk.setEnabled(true);
                                }
                            } else {
                                showToast("服务器访问失败，请重试");
                                ProgressDlgUtil.stopProgressDlg();
                                if(tOk!=null)
                                    tOk.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                            if(tOk!=null)
                                tOk.setEnabled(true);
                        }finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }


                });

            }
        });
    }

    private void sendSuccess() {
        ProgressDlgUtil.showProgressDlg("Loading...", YYGSetAddressActivity.this);
        String param="&userId=" + MyApplication.getInstance().self.getId() + "&contacts="+ name+"&contactsPhone="+phone+"&addressProvince="+province+
                "&addressCity="+city+ "&addressDistrict="+area+"&addressDetails="+address_detail+"&addressId="+address_id+"&activityId=" + act_id;
        String url=TLUrl.getInstance().URL_yyg_add_address;
        HttpRequest.sendPost(url,param , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "add_msg=" + msg);
                            if (object.getInt("status") == 1) {
                                confirm();
//                                    showToast("地址提交成功！我们会尽快为您发货");
//                                    MyUpdateUI.sendUpdateCollection(YYGSetAddressActivity.this,MyUpdateUI.YYGSETADDRESS);
//                                    Intent intent = new Intent();
//                                    setResult(1, intent);
//                                    finish();
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                showToast("服务器访问失败，请重试");
                                ProgressDlgUtil.stopProgressDlg();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            address_info = (String) data.getSerializableExtra("address_info");
            address_detail = (String) data.getSerializableExtra("address_detail");
            name = (String) data.getSerializableExtra("name");
            phone = (String) data.getSerializableExtra("phone");
            address_id = (String) data.getSerializableExtra("address_id");
            Log.i("zjz", "address_info=" + address_info);
            Log.i("zjz", "address_detail=" + address_detail);
            Log.i("zjz", "name=" + name);
            Log.i("zjz", "phone=" + phone);
            Log.i("zjz", "address_id=" + address_id);
            initAddress();
        }
    }

    private void initAddress() {
        String[] strings = address_info.split(" ");
        if(strings.length==3){
            province=strings[0];
            city=strings[1];
            area=strings[2];
            Log.i("zjz", "province=" + province);
            Log.i("zjz", "city=" + city);
            Log.i("zjz", "area=" + area);
        }
        tOk.setVisibility(View.VISIBLE);
        tName.setText(name);
        tPhone.setText(phone);
        tAddress.setText("收货地址：" + address_info+address_detail);
        rlChoose.setVisibility(View.GONE);
        linearAddress.setVisibility(View.VISIBLE);
    }
}
