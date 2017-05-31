package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.DingQiActivity;
import com.abcs.huaqiaobang.activity.RegularActivity;
import com.abcs.huaqiaobang.activity.VipActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author xbw 定期
 * @version 创建时间：2015年10月21日 下午3:47:42
 */
public class Regular extends Fragment {
    public DingQiActivity activity;
    public View view;
    private LinearLayout grp;
    private ScrollView scrollView;
    private TextView tv_refresh;
    private EditText t;
    private Handler handler = new Handler();
    private ArrayList<Product> products;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = (DingQiActivity)getActivity();

        if (view == null) {
            view = getActivity().getLayoutInflater().inflate(
                    R.layout.occft_fragment_regular, null);
        }
        grp = (LinearLayout) view.findViewById(R.id.fragment_regular_grp);
        scrollView = (ScrollView) view.findViewById(R.id.scrollview_regular);
        tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
        tv_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.tljr_btn_lfanhui).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.main_img_enter_vip).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showEnterCode();
                    }
                });
        t = (EditText) view.findViewById(R.id.main_et_vip);

        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        getRegular();
//        initView();
    }

    public void getRegular() {

        products = new ArrayList<>();

//        ProgressDlgUtil.showProgressDlg("", activity);
        String param;
        if(MyApplication.getInstance().self!=null){
            param="method=getAllProductList&type=1&page=1&size=10"+"&uid="+MyApplication.getInstance().self.getId();
        }else {
            param="method=getAllProductList&type=1&page=1&size=10";
        }
        LogUtil.e("getRegular", param);
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet, param,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getRegular", msg);
                        if (msg.equals("")) {
                            return;
                        }
//                        ProgressDlgUtil.stopProgressDlg();

                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Product product = new Product();
                                    JSONObject object = jsonArray
                                            .getJSONObject(i);
                                    product.setBuyMoney(object
                                            .optInt("buyMoney"));
                                    product.setBuyNum(object.optInt("buyNum"));
                                    product.setBuyRate(object
                                            .optDouble("buyRate"));
                                    product.setConfirmBuyDate(object
                                            .optLong("confirmBuyDate"));
                                    product.setDesc(object.optString("desc"));
                                    product.setEarnings(object
                                            .optDouble("earnings"));
                                    product.setEndBuyDate(object
                                            .optLong("endBuyDate"));
                                    product.setIconUrl(object
                                            .optString("iconUrl"));
                                    product.setId(object.optString("id"));
                                    product.setName(object.optString("name"));
                                    product.setNumberOfDays(object
                                            .optInt("numberOfDays"));
                                    product.setOrder(object.optInt("order"));
                                    product.setProductType(object
                                            .optInt("productType"));
                                    product.setSoldMoney(object
                                            .optLong("soldMoney"));
                                    product.setUpdateDate(object
                                            .optLong("updateDate"));
                                    product.setVip(object.optBoolean("vip"));
                                    product.setOverlayEarnings(object
                                            .optDouble("overlayEarnings", 0));
                                    product.setItems(object.optString("items"));
                                    products.add(product);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        initView();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showEnterCode() {
        if (MyApplication.getInstance().self == null) {

            Toast.makeText(activity,"请先登录",Toast.LENGTH_LONG).show();
            return;
        }

        if (!t.getText().toString().equals("")) {
            sendVipCode(t.getText().toString());
            return;
        } else {
            t.setFocusable(true);
        }
//        final EditText et = new EditText(activity);
//        new AlertDialog.Builder(activity).setTitle("请输入Vip码").setView(et)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        t.setText(et.getText().toString());
//                        sendVipCode(et.getText().toString());
//                    }
//                }).setNegativeButton("取消", null).show();
    }

    private void sendVipCode(final String code) {
        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("sendVipCode",
                "method=vip&uid=" + MyApplication.getInstance().self.getId()
                        + "&token=" + Util.token + "&code=" + code);
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet, "method=vip&uid="
                + MyApplication.getInstance().self.getId() + "&token="
                + Util.token + "&code=" + code, new HttpRevMsg() {

            @Override
            public void revMsg(final String msg) {
                LogUtil.e("sendVipCode", msg);
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                activity.startActivity(new Intent(activity,
                                        VipActivity.class));
                            } else {

                                Toast.makeText(activity,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void initView() {

        //home键问题

        if (products.size() == 0) {
//				scrollView.setVisibility(View.GONE);
//				tv_refresh.setVisibility(View.VISIBLE);
            return;
        }
//        grp.removeAllViews();
//        View vip_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_vip_item, null);

        for (int i = 0; i < products.size(); i++) {
            final Product product = products.get(i);
            View v = View.inflate(activity, R.layout.occft_item_regular, null);
            // 100起购-定期3个月-本金保障
            String[] desc = product.getDesc().split("-");
            String s = "";
            for (int j = 0; j < desc.length; j++) {
                s += ("·" + desc[j] + (j == desc.length - 1 ? "" : "\n"));
            }
            ((TextView) v.findViewById(R.id.item_regular_desc)).setText(s);
            ((TextView) v.findViewById(R.id.item_regular_gain)).setText(Util.df
                    .format(product.getEarnings() * 100) + "%");
            ((TextView) v.findViewById(R.id.item_regular_info)).setText("已售出"
                    + product.getSoldMoney() / 100 + "元    已有"
                    + product.getBuyNum() + "人购买");
            MyApplication.imageLoader.displayImage(product.getIconUrl(),
                    (ImageView) v.findViewById(R.id.item_regular_icon),
                    MyApplication.options);
            final int m = i;
//				v.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						activity.startRegularActivity(m);
//					}
//				});
            v.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    super.onNoDoubleClick(v);

                    Intent intent = new Intent(activity, RegularActivity.class);
                    try {
                        intent.putExtra("info",Util.getStringByObject(product));
//                        intent.putExtra("info",Util.getStringByObject(products.get(m)));
                    } catch (Throwable e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    activity.startActivity(intent);
                }
            });
            LayoutParams params = new LayoutParams(
                    LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
            params.setMargins(15, 15, 15, 0);
            v.setLayoutParams(params);
            grp.addView(v);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("Regular");
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("Regular");
    }
}
