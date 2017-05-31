package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * @author xbw
 * @version 创建时间：2015年10月21日 下午6:03:29
 */
public class OneMyProductActivity extends BaseActivity {
    private Product product;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_regular);
        if (isTaskRoot()) {
            finish();
            return;
        }
        findViewById(R.id.regular_btn_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        findViewById(R.id.turnin).setVisibility(View.GONE);
        LogUtil.e("OneMyProductActivity", getIntent().getStringExtra("info"));
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("info"));

            findViewById(R.id.current_grp).setVisibility(jsonObject.getBoolean("close") ? View.VISIBLE : View.GONE);

            product = new Product();
            JSONObject object = jsonObject.getJSONObject("product");
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
            product.setPayway(jsonObject.optString("payWay"));
            android.util.Log.i("One", jsonObject.optString("payWay") + jsonObject.optString("outWay"));
            product.setOutway(jsonObject.optString("outWay"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.tljr_textView2)).setText(product
                .getName());
        ((TextView) findViewById(R.id.qigoujine)).setText("起购金额(元)\n"
                + product.getBuyMoney() / 100);
        ((TextView) findViewById(R.id.touziqixian)).setText("投资期限(天)\n"
                + product.getNumberOfDays());
        changeText(((TextView) findViewById(R.id.qigoujine)));
        changeText(((TextView) findViewById(R.id.touziqixian)));
        try {
            if (jsonObject.getInt("money") <= 0) {
                findViewById(R.id.turnout).setBackgroundResource(
                        R.color.tljr_bjatitle);
                findViewById(R.id.turnout).setClickable(false);
            } else {
                findViewById(R.id.turnout).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (MyApplication.getInstance().self == null) {
                                    login();
                                    return;
                                }
                                try {
                                    MyApplication.getInstance().self
                                            .setInvest(jsonObject
                                                    .getInt("money"));
                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                try {

                                    startActivityForResult(new Intent(
                                            OneMyProductActivity.this,
                                            TurnOutActivity.class).putExtra(
                                            "info",
                                            Util.getStringByObject(product))
                                            .putExtra("id", getIntent().getStringExtra("id")), 2);
                                } catch (Throwable e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        findViewById(R.id.turnin).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    login();
                    return;
                }
                try {
                    startActivity(new Intent(OneMyProductActivity.this,
                            TurnInActivity.class).putExtra("info",
                            Util.getStringByObject(product)));
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        try {
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
            JSONArray array = new JSONArray(product.getItems());
            View vip = findViewById(R.id.vip);
            if (array.length() > 0) {
                layout.removeView(vip);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Iterator<String> iterator = object.keys();
                    while (iterator.hasNext()) {
                        final String key = iterator.next();
                        if (!key.equals("islink")) {
                            View view = View.inflate(this,
                                    R.layout.occft_item_info, null);
                            TextView name = ((TextView) view
                                    .findViewById(R.id.name));
                            name.setText(key);
                            name.setTextColor(getResources().getColor(
                                    R.color.tljr_text_shense));
                            name.setTextSize(18);
                            view.setLayoutParams(new LinearLayout.LayoutParams(
                                    Util.WIDTH, Util.HEIGHT / 12));
                            view.setBackgroundColor(getResources().getColor(
                                    R.color.tljr_white));
                            boolean islink = object.getBoolean("islink");
                            view.findViewById(R.id.arrow).setVisibility(
                                    islink ? View.VISIBLE : View.GONE);
                            view.findViewById(R.id.value).setVisibility(
                                    islink ? View.GONE : View.VISIBLE);
                            layout.addView(view);
                            if (islink) {
                                final String value = object.getString(key);
                                view.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(
                                                OneMyProductActivity.this,
                                                WebActivity.class);
                                        intent.putExtra("url", value);
                                        intent.putExtra("name", key);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                JSONArray jsonArray = object.getJSONArray(key);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject obj = jsonArray.getJSONObject(j);
                                    Iterator<String> iterator1 = obj.keys();
                                    while (iterator1.hasNext()) {
                                        final String key1 = iterator1.next();
                                        if (!key1.equals("islink")) {
                                            final String value1 = obj
                                                    .getString(key1);
                                            View view1 = View.inflate(this,
                                                    R.layout.occft_item_info,
                                                    null);
                                            view1.setLayoutParams(new LinearLayout.LayoutParams(
                                                    Util.WIDTH,
                                                    Util.HEIGHT / 15));
                                            ((TextView) view1
                                                    .findViewById(R.id.name))
                                                    .setText(key1);
                                            boolean islink1 = object
                                                    .getBoolean("islink");
                                            view1.findViewById(R.id.arrow)
                                                    .setVisibility(
                                                            islink1 ? View.VISIBLE
                                                                    : View.GONE);
                                            view1.findViewById(R.id.value)
                                                    .setVisibility(
                                                            islink1 ? View.GONE
                                                                    : View.VISIBLE);
                                            if (islink1) {
                                                view1.setOnClickListener(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(
                                                                OneMyProductActivity.this,
                                                                WebActivity.class);
                                                        intent.putExtra("url",
                                                                value1);
                                                        intent.putExtra("name",
                                                                key1);
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                ((TextView) view1
                                                        .findViewById(R.id.value))
                                                        .setText(obj
                                                                .getString(key1));
                                            }
                                            layout.addView(view1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                layout.addView(vip);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initInfo();
    }

    public void initInfo() {
        try {
            findViewById(R.id.info_grp).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.Investmoney)).setText(Util.df
                    .format(jsonObject.optDouble("earningsYesterday") / 100));
            ((TextView) findViewById(R.id.leiji)).setText("累计收益"
                    + Util.df.format(jsonObject.optDouble("allEarnings") / 100)
                    + "元");
            ((TextView) findViewById(R.id.yitougeshu)).setText("买入时间："
                    + Util.getDateOnlyDat(jsonObject.getLong("buyDate")));
            ((TextView) findViewById(R.id.yitoujinqian))
                    .setText("买入金额"
                            + Util.df.format(jsonObject.optDouble("money") / 100)
                            + "元");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void changeText(TextView view) {
        String str = view.getText().toString();
        int index = str.indexOf(")");
        if (index < 0) {
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new AbsoluteSizeSpan(20, true), index + 1, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
                index + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(style);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            setResult(4, data);
            finish();
        }

    }
}
