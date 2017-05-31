package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.Package1Fragment;
import com.abcs.haiwaigou.fragment.Package2Fragment;
import com.abcs.haiwaigou.fragment.Package3Fragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyDeliverActivity extends AppCompatActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.linear_name)
    LinearLayout linearName;
    @InjectView(R.id.t_address)
    TextView tAddress;
    @InjectView(R.id.package_tabs)
    PagerSlidingTabStrip packageTabs;
    @InjectView(R.id.package_pager)
    ViewPager packagePager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;

    private Handler handler = new Handler();
    String order_id;
    String reciver_name;
    String address;
    String area;
    String mob_phone;
    String phone;
    String street;
    String tel_phone;
    String express_name;
    String shipping_code;
    public ArrayList<String> deliver_info = new ArrayList<>();
    public ArrayList<ArrayList<String>> deliver_num = new ArrayList<ArrayList<String>>();
    public ArrayList<Goods> package_num = new ArrayList<>();
    private String message;
    Fragment currentFragment;
    Package1Fragment package1Fragment;
    Package2Fragment package2Fragment;
    Package3Fragment package3Fragment;
    CFViewPagerAdapter viewPagerAdapter;
    int currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_my_deliver);
        ButterKnife.inject(this);
        order_id = (String) getIntent().getSerializableExtra("order_id");
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
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
                                JSONObject object1 = object.optJSONObject("datas");
                                if (object1 != null) {
                                    if (object1.has("error")) {
//                                    showToast(object1.getString("error"));
                                        Map<String, String> map = fenGe(object.optString("reciver_info"));
                                        reciver_name = object.optString("reciver_name");
                                        if (tName != null)
                                            tName.setText("收货人：" + reciver_name);
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
                                        if (layoutNull != null)
                                            layoutNull.setVisibility(View.VISIBLE);
                                        if (linearRoot != null)
                                            linearRoot.setVisibility(View.INVISIBLE);
                                        return;
                                    }
                                    if (linearRoot != null)
                                        linearRoot.setVisibility(View.VISIBLE);
                                    Goods g = new Goods();
                                    g.setTitle(object1.optString("express_name"));
                                    g.setSubhead(object1.optString("shipping_code"));
                                    g.setTitle2(object1.optString("express_order_info"));
                                    package_num.add(g);
                                    express_name = object1.optString("express_name");
                                    shipping_code = object1.optString("shipping_code");
                                    JSONArray jsonArray = object1.getJSONArray("deliver_info");
                                    JSONArray ordermember = object1.getJSONArray("ordermember");
                                    JSONObject orderjobj = ordermember.getJSONObject(0);
                                    Map<String, String> map = fenGe(orderjobj.optString("reciver_info"));
                                    reciver_name = orderjobj.optString("reciver_name");
                                    if (tName != null)
                                        tName.setText("收货人：" + reciver_name);
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

                                    StringBuffer stringBuffer = new StringBuffer();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Goods goods = new Goods();
                                        Log.i("zjz", "title=" + jsonArray.get(i).toString());
//                                        deliver_info.add(jsonArray.get(i).toString());
                                        stringBuffer.append(jsonArray.get(i).toString());
                                        if (i != jsonArray.length() - 1) {
                                            stringBuffer.append("##");
                                        }
                                        String[] split = jsonArray.get(i).toString().split("&nbsp;&nbsp;");
                                        if (split.length == 2) {
                                            goods.setTitle(split[0]);
                                            goods.setSubhead(split[1]);
                                        }
                                    }
                                    message = stringBuffer.toString();
                                    deliver_info.add(message);
//                                    deliver_num.add(deliver_info);
                                    initPackage();
                                } else {
                                    if (linearRoot != null)
                                        linearRoot.setVisibility(View.VISIBLE);
                                    JSONArray array = object.optJSONArray("datas");
                                    JSONArray addObj = array.getJSONArray(array.length() - 1);
                                    JSONObject addObject = addObj.getJSONObject(0);
                                    Map<String, String> map = fenGe(addObject.optString("reciver_info"));
                                    reciver_name = addObject.optString("reciver_name");
                                    if (tName != null)
                                        tName.setText("收货人：" + reciver_name);
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
                                    deliver_num.clear();
                                    deliver_info.clear();
                                    for (int i = 0; i < array.length() - 1; i++) {
                                        JSONObject info = array.getJSONObject(i);
                                        Goods g = new Goods();
                                        g.setTitle(info.optString("express_name", ""));
                                        g.setSubhead(info.optString("shipping_code", ""));
                                        g.setTitle2(info.optString("express_order_info", ""));
                                        package_num.add(g);

                                        JSONArray jsonArray = info.getJSONArray("deliver_info");
                                        StringBuffer stringBuffer = new StringBuffer();
                                        for (int j = 0; j < jsonArray.length(); j++) {
//                                            deliver_info.add(jsonArray.get(j).toString());
                                            stringBuffer.append(jsonArray.get(j).toString());
                                            if (j != jsonArray.length() - 1) {
                                                stringBuffer.append("##");
                                            }
                                            Log.i("zjz", "title=" + jsonArray.get(j).toString());


                                        }
                                        message = stringBuffer.toString();
                                        deliver_info.add(message);
//                                        deliver_num.add(i,deliver_info);
//                                        Log.i("zjz","deliver_num="+i+deliver_num.get(i));
                                    }
                                    initPackage();
                                }

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


    private void initPackage() {
//        pager = (ViewPager) findViewById(R.id.comment_pager);
        //第三方Tab
//        tabs = (PagerSlidingTabStrip) findViewById(R.id.comment_tabs);
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        Log.i("zjz", "包裹数=" + package_num.size());
        Log.i("zjz", "快递数=" + deliver_num.size());
        Log.i("zjz", "快递数量=" + deliver_info.size());
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0, goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));
        if (package_num.size() <= 1) {
            linearTab.setVisibility(View.GONE);
        }else {
            linearTab.setVisibility(  View.VISIBLE);
        }
        for (int i = 0; i < package_num.size(); i++) {
            viewPagerAdapter.getDatas().add(Package1Fragment.newInstance(package_num.get(i).getTitle(), package_num.get(i).getSubhead(), deliver_info.get(i), package_num.get(i).getTitle2()));
            viewPagerAdapter.getTitle().add(i, "包裹" + (i + 1));
        }
//        switch (package_num.size()) {
//            case 1:
//                viewPagerAdapter.getDatas().add(Package1Fragment.newInstance(package_num.get(0).getTitle(), package_num.get(0).getSubhead(), deliver_info.get(0)));
//                viewPagerAdapter.getTitle().add(0, "包裹1");
//                break;
//            case 2:
//                viewPagerAdapter.getDatas().add(Package1Fragment.newInstance(package_num.get(0).getTitle(), package_num.get(0).getSubhead(), deliver_info.get(0)));
//                viewPagerAdapter.getDatas().add(Package2Fragment.newInstance(package_num.get(1).getTitle(), package_num.get(1).getSubhead(), deliver_info.get(1)));
//                viewPagerAdapter.getTitle().add(0, "包裹1");
//                viewPagerAdapter.getTitle().add(1, "包裹2");
//                break;
//            case 3:
//                viewPagerAdapter.getDatas().add(Package1Fragment.newInstance(package_num.get(0).getTitle(), package_num.get(0).getSubhead(), deliver_info.get(0)));
//                viewPagerAdapter.getDatas().add(Package2Fragment.newInstance(package_num.get(1).getTitle(), package_num.get(1).getSubhead(), deliver_info.get(1)));
//                viewPagerAdapter.getDatas().add(Package3Fragment.newInstance(package_num.get(2).getTitle(), package_num.get(2).getSubhead(), deliver_info.get(2)));
//                viewPagerAdapter.getTitle().add(0, "包裹1");
//                viewPagerAdapter.getTitle().add(1, "包裹2");
//                viewPagerAdapter.getTitle().add(2, "包裹3");
//                break;
//        }
        packagePager.setAdapter(viewPagerAdapter);
        packagePager.setOffscreenPageLimit(1);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        packageTabs.setViewPager(packagePager);
        packageTabs.setIndicatorHeight(Util.dip2px(this, 4));
        packageTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        packageTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
                currentType = position + 1;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                System.out.println("Change Posiont:" + position);

                // TODO Auto-generated method stub

            }
        });
        currentFragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 4; i++) {
            View view = packageTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {

                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
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
}
