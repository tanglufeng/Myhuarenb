package com.abcs.huaqiaobang.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xbw
 * @version 创建时间：2015年11月13日 上午11:22:09
 */
public class VipActivity extends BaseActivity {
    private LinearLayout grp;
    private ActivityHome activity;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_fragment_regular);
        findViewById(R.id.tljr_btn_lfanhui).setVisibility(View.VISIBLE);
        findViewById(R.id.tljr_btn_lfanhui).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        ((TextView) findViewById(R.id.tljr_textView2))
                .setText(getString(R.string.activity_vip_title));
        grp = (LinearLayout) findViewById(R.id.fragment_regular_grp);
        activity = MyApplication.getInstance().getMainActivity();
        getData();
    }

    private void getData() {
        if (activity.vipList.size() == 0) {
            getVipCurrent(new Complete() {

                @Override
                public void complete() {
                    showUI();
                }
            });
        } else {
            showUI();
        }
    }

    private void showUI() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (activity.vipList.size() == 0) {
                    return;
                }
                grp.removeAllViews();
                for (int i = 0; i < activity.vipList.size(); i++) {
                    Product product = activity.vipList.get(i);
                    View v = View.inflate(activity,
                            R.layout.occft_item_regular, null);
                    // 100起购-定期3个月-本金保障
                    String[] desc = product.getDesc().split("-");
                    String s = "";
                    for (int j = 0; j < desc.length; j++) {
                        s += ("·" + desc[j] + (j == desc.length - 1 ? "" : "\n"));
                    }
                    ((TextView) v.findViewById(R.id.item_regular_desc))
                            .setText(s);
                    TextView tv = (TextView) v
                            .findViewById(R.id.item_regular_gain);
                    tv.setText(Util.df.format(product.getEarnings() * 100)
                            + "%");
                    ((TextView) v.findViewById(R.id.item_regular_info))
                            .setText("已售出" + product.getSoldMoney() / 100
                                    + "元    已有" + product.getBuyNum() + "人购买");
                    if (product.isVip()) {
                        v.findViewById(R.id.item_regular_vip).setVisibility(
                                View.VISIBLE);
                        v.findViewById(R.id.item_regular_vip_value)
                                .setVisibility(View.VISIBLE);
                        ((TextView) v.findViewById(R.id.item_regular_vip_value))
                                .setText(Util.df.format(product
                                        .getEarnings() * 100) + "%+");
                        tv.setText(Util.df.format(product.getOverlayEarnings() * 100)
                                + "%");
                        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
                        tv.getPaint().setAntiAlias(true);// 抗锯齿
                    }
                    MyApplication.imageLoader.displayImage(
                            product.getIconUrl(),
                            (ImageView) v.findViewById(R.id.item_regular_icon),
                            MyApplication.options);
                    final int m = i;
//					v.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							activity.startVipActivity(activity.vipList.get(m));
//						}
//					});
                    v.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            super.onNoDoubleClick(v);
                            activity.startVipActivity(activity.vipList.get(m));
                        }
                    });
                    LayoutParams params = new LayoutParams(
                            LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
                    params.setMargins(15, 15, 15, 0);
                    v.setLayoutParams(params);
                    grp.addView(v);
                }
            }
        });
    }

    public void getVipCurrent(final Complete complete) {
        ProgressDlgUtil.showProgressDlg("", this);
        LogUtil.e("getVipCurrent",
                "method=getAllProductList&type=2&page=1&size=10&vip=true&uid="
                        + MyApplication.getInstance().self.getId());
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
                "method=getAllProductList&type=2&page=1&size=10&vip=true&uid="
                        + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getVipCurrent", msg);
                        if (msg.equals("")) {
                            getVipCurrent(complete);
                            return;
                        }
                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                activity.vipList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Product product = new Product();
                                    JSONObject object = jsonArray
                                            .getJSONObject(i);
                                    product.setBuyMoney(object.optInt("buyMoney"));
                                    product.setBuyNum(object.optInt("buyNum"));
                                    product.setBuyRate(object.optDouble("buyRate"));
                                    product.setConfirmBuyDate(object.optLong("confirmBuyDate"));
                                    product.setDesc(object.optString("desc"));
                                    product.setEarnings(object.optDouble("earnings"));
                                    product.setEndBuyDate(object.optLong("endBuyDate"));
                                    product.setIconUrl(object.optString("iconUrl"));
                                    product.setId(object.optString("id"));
                                    product.setName(object.optString("name"));
                                    product.setNumberOfDays(object.optInt("numberOfDays"));
                                    product.setOrder(object.optInt("order"));
                                    product.setProductType(object.optInt("productType"));
                                    product.setSoldMoney(object.optLong("soldMoney"));
                                    product.setUpdateDate(object.optLong("updateDate"));
                                    product.setVip(object.optBoolean("vip"));
                                    product.setOverlayEarnings(object.optDouble("overlayEarnings", 0));
                                    product.setItems(object.optString("items"));
                                    activity.vipList.add(product);
                                }
                                complete.complete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
