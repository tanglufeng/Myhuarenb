package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.DeliverAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeliverActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.t_deliver_company)
    TextView tDeliverCompany;
    @InjectView(R.id.linear_company)
    LinearLayout linearCompany;
    @InjectView(R.id.t_deliver_code)
    TextView tDeliverCode;
    @InjectView(R.id.linear_code)
    LinearLayout linearCode;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    public Handler handler = new Handler();
    String order_id;
    DeliverAdapter deliverAdapter;
    FullyLinearLayoutManager fullyLinearLayoutManager;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.t_address)
    TextView tAddress;
    private View view;
    String reciver_name;
    String address;
    String area;
    String mob_phone;
    String phone;
    String street;
    String tel_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_deliver, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        order_id = (String) getIntent().getSerializableExtra("order_id");
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        deliverAdapter = new DeliverAdapter();
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setAdapter(deliverAdapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        initView();
        setOnListener();
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
    }

    private void initView() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_deliver_seach, "&key=" + MyApplication.getInstance().getMykey() + "&order_id=" + order_id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "msg=" + msg);
                            if (object.getInt("code") == 200) {
                                deliverAdapter.getDatas().clear();
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.has("error")) {
//                                    showToast(object1.getString("error"));
                                    Map<String, String> map = fenGe(object.optString("reciver_info"));
                                    reciver_name = object.optString("reciver_name");
                                    if (tName != null)
                                        tName.setText("收货人："+reciver_name);
                                    address = map.get("address");
                                    if (tAddress != null)
                                        tAddress.setText(address);
                                    area = map.get("area");
                                    mob_phone = map.get("mob_phone");
                                    if (tPhone != null)
                                        tPhone.setText(mob_phone);
                                    phone = map.get("phone");
                                    street = map.get("street");
                                    tel_phone = map.get("tel_phone");
                                    if (tDeliverCompany != null)
                                        tDeliverCompany.setText("等待物流公司签收");
                                    showToast("订单待物流公司签收中...");
                                    return;
                                }
                                if (tDeliverCompany != null)
                                    tDeliverCompany.setText(object1.optString("express_name"));
                                if (tDeliverCode != null)
                                    tDeliverCode.setText(object1.optString("shipping_code"));
                                JSONArray jsonArray = object1.getJSONArray("deliver_info");
                                JSONArray ordermember = object1.getJSONArray("ordermember");
                                JSONObject orderjobj = ordermember.getJSONObject(0);
                                Map<String, String> map = fenGe(orderjobj.optString("reciver_info"));
                                reciver_name = orderjobj.optString("reciver_name");
                                if (tName != null)
                                    tName.setText("收货人："+reciver_name);
                                address = map.get("address");
                                if (tAddress != null)
                                    tAddress.setText(address);
                                area = map.get("area");
                                mob_phone = map.get("mob_phone");
                                if (tPhone != null)
                                    tPhone.setText(mob_phone);
                                phone = map.get("phone");
                                street = map.get("street");
                                tel_phone = map.get("tel_phone");
                                Log.i("zjz", "receiver_info=" + reciver_name + " " + address
                                        + " " + area + " " + mob_phone + " " + street + " " + tel_phone);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Goods goods = new Goods();
                                    Log.i("zjz", "title=" + jsonArray.get(i).toString());
                                    String[] split = jsonArray.get(i).toString().split("&nbsp;&nbsp;");
                                    if (split.length == 2) {
                                        goods.setTitle(split[0]);
                                        goods.setSubhead(split[1]);
                                    }
//                                    String s=jsonArray.get(i).toString();
//                                    s= s.replaceAll("&nbsp;&nbsp;", "\n");
//                                    goods.setTitle(s);
                                    deliverAdapter.getDatas().add(goods);
                                }
                                Log.i("zjz", "size=" + deliverAdapter.getDatas().size());
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
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }

    public Map<String, String> fenGe(String string) {
        String str = string;
        String[] split = str.split("\"");
        JSONObject jsonObject = new JSONObject();
        List<String> strings = new ArrayList<>();
        boolean first = false;
        for (String s : split) {

            if (s.indexOf(";") > -1 || s.indexOf(":") > -1) {
                if (s.indexOf("N") > -1)
                    strings.add("N");
                continue;
            }
            if (!"".equals(s.trim()) || first) {
                strings.add(s);
                first = true;
//                System.out.println(s.trim());
            }
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < strings.size(); i++) {
//            System.out.println(strings.get(i) + "=" + strings.get(i + 1));
            if (!strings.get(i).isEmpty())
                map.put(strings.get(i), strings.get(i + 1));
            i++;
        }
        return map;
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
