package com.abcs.haiwaigou.local.huohang.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.beans.HuoHangAddressList;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HuoHangAddressActivity extends BaseActivity implements View.OnClickListener{


    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;

    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    public Handler handler = new Handler();
    @InjectView(R.id.tv_add_address)
    TextView tvAddAddress;
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    MyBroadCastReceiver myBroadCastReceiver;

    boolean first = false;
    private View view;
    AddressListAdapter adapter;
    public boolean formSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_huohang_address, null);
        }
        setContentView(view);
        ButterKnife.inject(this);

        formSetting=getIntent().getBooleanExtra("formSetting",false);

        Log.i("zds", "onCreate: formSetting"+formSetting);

        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        adapter=new AddressListAdapter(HuoHangAddressActivity.this);
        listview.setAdapter(adapter);

        initAllDates();
        setOnListener();
    }


    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getSerializableExtra("type").equals(MyUpdateUI.ADDRESS)) {
                Log.i("zdszds", "更新收货地址");
                initAllDates();
            }
        }

        @Override
        public void update(Intent intent) {

        }

    };

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                first = true;
                initAllDates();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });
    }


    private void initAllDates() {
//        ProgressDlgUtil.showProgressDlg("Loading...", this);
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=native_address_list&key=5ff36f75680c05703394eff5013f6a1a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=native&op=native_address_list&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", msg);
//                        ProgressDlgUtil.stopProgressDlg();

                        if(TextUtils.isEmpty(msg)){
                            return;
                        }else {
                            HuoHangAddressList bean=new Gson().fromJson(msg, HuoHangAddressList.class);

                            if(bean!=null){
                                if(bean.state==1){
                                    if(bean.datas!=null){
                                        if(bean.datas.size()>0){
                                            adapter.addDatas(bean.datas);
                                        }else {
                                            if (layoutNull != null)
                                                layoutNull.setVisibility(View.GONE);
                                        }
                                    }
                                }else {
                                    if (layoutNull != null)
                                        layoutNull.setVisibility(View.GONE);
                                }
                            }else {
                                showToast("失败咯！");
                            }
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.tv_add_address:
                intent = new Intent(this, HuoHangEditAddressActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroy();
    }

    public class AddressListAdapter extends BaseAdapter {
        private Activity mcontext;
        private LayoutInflater inflater;
        private List<HuoHangAddressList.DatasEntry> cityList=new ArrayList<>();

        public AddressListAdapter(Activity mcontext) {
            this.mcontext = mcontext;
            inflater = LayoutInflater.from(mcontext);
        }

        public void addDatas(List<HuoHangAddressList.DatasEntry> List){
            cityList.clear();
            if(List!=null&&List.size()>0){
                cityList.addAll(List);
                notifyDataSetChanged();
            }
        }
        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public HuoHangAddressList.DatasEntry getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

             ViewHolder mHolder= null;
            if (convertView == null){
                convertView = View.inflate(mcontext,R.layout.huohang_item_address,null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            }else{
                mHolder = (ViewHolder) convertView.getTag();
            }

            final HuoHangAddressList.DatasEntry itemBean=getItem(position);
            if(itemBean!=null){

                mHolder.t_name.setText(itemBean.trueName.trim());
                String str = itemBean.mobPhone;
                String str_tel = itemBean.telPhone;


                if(str.length() > 0){
                    mHolder.t_phone.setVisibility(View.VISIBLE);
                    if (str.length() > 7) {
                        String first = str.substring(0, 3);
                        String last = str.substring(str.length() - 4, str.length());
                        mHolder.t_phone.setText(first + "****" + last);
                    } else {
                        mHolder.t_phone.setText(str);
                    }
                }else if(str_tel.length() > 0) {
                    mHolder.t_phone.setVisibility(View.VISIBLE);
                    if (str_tel.length() > 7) {
                        String first = str_tel.substring(0, 3);
                        String last = str_tel.substring(str_tel.length() - 4, str_tel.length());
                        mHolder.t_phone.setText(first + "****" + last);
                    } else {
                        mHolder.t_phone.setText(str_tel);
                    }
                }

                mHolder.t_address.setText(itemBean.address.trim());

//                mHolder.t_id_card.setText("身份证信息：" + itemBean.memberId);
//                mHolder.radio_check.setTag(new Integer(position));
                if (itemBean.isDefault.equals("1")){
                    mHolder.t_default.setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
                    mHolder.t_default.setText("默认地址");
                    mHolder.radio_check.setBackgroundResource(R.drawable.img_cart_select);
                }else {
                    mHolder.t_default.setTextColor(getResources().getColor(R.color.hwg_text2));
                    mHolder.t_default.setText("设为默认");
                    mHolder.radio_check.setBackgroundResource(R.drawable.img_cart_unselect);
                }


                mHolder.liner_seh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        http://newapi.tuling.me/huaqiaobang/mobile/index.php?act=native&op=edit_native_default_store&key=05dfd217ee5d0f1ef466bca5e84ff585&address_id=2874
//
                        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php","act=native&op=edit_native_default_store&key=" + MyApplication.getInstance().getMykey()+ "&address_id=" + itemBean.addressId, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject(msg);
                                            if (json != null && json.has("state")) {
                                                int state = json.getInt("state");
                                                if (state == 1) {
//                                                    showToast("成功！");
                                                    Log.d("shewei", "run: "+state);
                                                    MyUpdateUI.sendUpdateCollection(HuoHangAddressActivity.this, MyUpdateUI.ADDRESS);
                                                }else {
//                                                    showToast("失败！");
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
                });

                mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!formSetting){
                            Intent intent = new Intent();
                            intent.putExtra("address", itemBean.address);
                            intent.putExtra("name", itemBean.trueName);
                            intent.putExtra("phone", itemBean.telPhone);
                            intent.putExtra("tel_phone", itemBean.telPhone);
                            intent.putExtra("address_id", itemBean.addressId);
                            setResult(1, intent);
                            finish();
                        }
                    }
                });

                mHolder.linear_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(HuoHangAddressActivity.this, HuoHangEditAddressActivity.class);
                        intent.putExtra("isAdd", false);
                        intent.putExtra("address_id", itemBean.addressId);
                        intent.putExtra("trueName", itemBean.trueName);
                        intent.putExtra("memberId", itemBean.memberId);
                        intent.putExtra("telPhone", itemBean.telPhone);
                        intent.putExtra("mobPhone", itemBean.mobPhone);
                        intent.putExtra("address", itemBean.address);
                        intent.putExtra("isDefault", itemBean.isDefault);
                        startActivity(intent);


                    }
                });

                mHolder.linear_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog(HuoHangAddressActivity.this, "确定删除该地址？", new Complete() {
                            @Override
                            public void complete() {
                                ProgressDlgUtil.showProgressDlg("", HuoHangAddressActivity.this);
                                HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=native&op=del_address&address_id=" + itemBean.addressId + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                                    @Override
                                    public void revMsg(final String msg) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ProgressDlgUtil.stopProgressDlg();
                                                try {
                                                    JSONObject object = new JSONObject(msg);
                                                    if (object.getInt("state") == 1) {
                                                        showToast("成功！");
                                                        MyUpdateUI.sendUpdateCollection(HuoHangAddressActivity.this, MyUpdateUI.ADDRESS);
                                                    } else {
                                                        showToast("失败！");
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
                        }).show();
                    }
                });
            }

            return convertView;
        }

        public class  ViewHolder{
            public TextView t_name;
            public TextView t_phone;
            public TextView t_address;
            public ImageView img_delete;
            public ImageView img_edit;
            public ImageView img_default;
            public LinearLayout linear_root;
            public LinearLayout linear_edit;
            public LinearLayout linear_delete;
            public LinearLayout liner_seh;
            public RelativeLayout relative_root;
            public TextView radio_check;
            public TextView t_default;
            public TextView t_id_card;

            public ViewHolder(View itemView) {
                t_name = (TextView) itemView.findViewById(R.id.t_name);
                t_phone = (TextView) itemView.findViewById(R.id.t_phone);
                t_address = (TextView) itemView.findViewById(R.id.t_address);
                img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
                img_edit = (ImageView) itemView.findViewById(R.id.img_edit);
                img_default = (ImageView) itemView.findViewById(R.id.img_default);
                linear_root = (LinearLayout) itemView.findViewById(R.id.linear_root);
                linear_edit = (LinearLayout) itemView.findViewById(R.id.linear_edit);
                linear_delete = (LinearLayout) itemView.findViewById(R.id.linear_delete);
                liner_seh = (LinearLayout) itemView.findViewById(R.id.liner_seh);
                t_default = (TextView) itemView.findViewById(R.id.t_default);
                t_id_card = (TextView) itemView.findViewById(R.id.t_id_card);
                radio_check = (TextView) itemView.findViewById(R.id.radio_check);
            }

        }
    }
}
