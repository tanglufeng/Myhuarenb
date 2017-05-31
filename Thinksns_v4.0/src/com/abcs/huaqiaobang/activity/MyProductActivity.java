package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.fragment.ProductFrament;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author xbw
 * @version 创建时间：2015年10月23日 下午6:00:12
 */
public class MyProductActivity extends FragmentActivity {
    LinearLayout layout;
    @InjectView(R.id.product_investment_in)
    TextView productInvestmentIn;
    @InjectView(R.id.product_can_withdraw_cash)
    TextView productCanWithdrawCash;
    @InjectView(R.id.product_already_mentioned)
    TextView productAlreadyMentioned;
    private Handler handler = new Handler();
    private int page = 1;
    private boolean loadMore = true;

    private int scroll_Y = 0;
    private Map<String, String> temp_repeat = new HashMap<String, String>();
    private Map<String, View> temp_view = new HashMap<String, View>();
    private ViewPager viewPager;
    private ImageView navagition;
    private int currentIndex;
    private ArrayList<TextView> tab_list;
    private View tempClickview;


    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_myproduct);
        ButterKnife.inject(this);
        layout = (LinearLayout) findViewById(R.id.myproduct_list);
        tab_list = new ArrayList<TextView>();
        productAlreadyMentioned.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        productCanWithdrawCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        productInvestmentIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        tab_list.add(productInvestmentIn);
        tab_list.add(productCanWithdrawCash);
        tab_list.add(productAlreadyMentioned);
        findViewById(R.id.turnout_btn_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });


        viewPager = (ViewPager) findViewById(R.id.product_viewpager);
        navagition = (ImageView) findViewById(R.id.navagition);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.WIDTH / 3, 3);
        navagition.setLayoutParams(params);

        CFViewPagerAdapter viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<3;i++){
            viewPagerAdapter.getDatas().add(ProductFrament.getInstance(i+1));
            viewPagerAdapter.getTitle().add(i, i+"");
        }

        //滑动的viewpager
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

       /* ProductFragmentAdapter adapter = new ProductFragmentAdapter(MyProductActivity.this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navagition
                        .getLayoutParams();
                currentIndex = position;

                for (int i = 0; i < tab_list.size(); i++) {
                    if (i != position) {
                        tab_list.get(i).setTextColor(Color.parseColor("#666666"));
                    } else {
                        tab_list.get(position).setTextColor(Color.parseColor("#fa5759"));
                    }
                }
                if (position == currentIndex) {
                    lp.leftMargin = (int) (positionOffset * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
                            * (Util.WIDTH / tab_list.size()));
                } else {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
                            * (Util.WIDTH / tab_list.size()));

                }
                navagition.setLayoutParams(lp);


            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initMyInfo();
    }

    public void initMyInfo() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                ((TextView) findViewById(R.id.Investmoney)).setText(Util.df
                        .format(MyApplication.getInstance().self
                                .getEarningsYesterday() / 100));
                ((TextView) findViewById(R.id.leiji)).setText("累计收益"
                        + Util.df.format(MyApplication.getInstance().self
                        .getAllEarnings() / 100) + "元");
                ((TextView) findViewById(R.id.yitougeshu)).setText("已投资"
                        + MyApplication.getInstance().self.getInvestCount()
                        + "个产品");
                ((TextView) findViewById(R.id.yitoujinqian)).setText("总投入"
                        + MyApplication.getInstance().self.getTotalInvest()
                        / 100 + "元");
            }
        });
    }

    public void getMyProduct() {
        if (MyApplication.getInstance().self == null) {
            return;
        }
        ProgressDlgUtil.showProgressDlg("", this);
        LogUtil.e("getMyProduct",
                "method=getInvestProductList&page=1&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=getInvestProductList&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token + "&page=" + page, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    if (jsonObject.getInt("status") == 1) {
                                        JSONArray jsonArray = jsonObject
                                                .getJSONArray("msg");

                                        layout.removeAllViews();
                                        if (jsonArray.length() == 0) {
                                            showToast("暂无投资信息!");
                                            return;
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray
                                                    .getJSONObject(i);
                                            String turnout_id = null;
                                            if (!object.isNull("id")) {
                                                turnout_id = object
                                                        .getString("id");
                                            }
                                            layout.addView(getOneProduct(
                                                    object, turnout_id));
                                        }
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    private View getOneProduct(final JSONObject object, final String turnout_id)
            throws JSONException {
        final JSONObject obj = object.getJSONObject("product");
        View v = View.inflate(this, R.layout.occft_item_product, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Util.WIDTH, Util.HEIGHT / 3);
        params.topMargin = 10;
        v.setLayoutParams(params);

        refreshProduct(object, obj, v, turnout_id);
        return v;
    }

    private void refreshProduct(final JSONObject object, JSONObject obj, View v, final String turnout_id) throws JSONException {

        final boolean close = object.getBoolean("close");
        final String repeat = object.getString("repeat");
        temp_repeat.put(turnout_id, repeat);

        TextView tv = (TextView) v.findViewById(R.id.item_product_icon_gain);
        if (obj != null) {
            ((TextView) v.findViewById(R.id.main_txt_current_sellandbuy))
                    .setText("已售出" + obj.getLong("soldMoney") / 100 + "元    已有"
                            + obj.getInt("buyNum") + "人购买");
            tv.setText(Util.df.format(obj.getDouble("earnings") * 100) + "%");
            MyApplication.imageLoader.displayImage(obj.getString("iconUrl"),
                    (ImageView) v.findViewById(R.id.item_product_icon),
                    MyApplication.options);
        }
        Resources resources = getResources();
        ImageView jiesuan = (ImageView) v.findViewById(R.id.img_jiesuan);
        jiesuan.setVisibility(close ? View.VISIBLE : View.GONE);
        TextView buyin = (TextView) v.findViewById(R.id.buyin);
        buyin.setText(resources.getString(R.string.myproduct_buycost)
                + Util.df.format(object.getDouble("money") / 100) + "(元)");
        changeText(buyin);
        TextView buytime = (TextView) v.findViewById(R.id.buytime);
        buytime.setText(resources.getString(R.string.myproduct_buytime)
                + Util.getDateOnlyDat(object.getLong("buyDate")));


        changeText(buytime);
        Log.i("MyP", buytime.getText().toString());
        TextView endtime = (TextView) v.findViewById(R.id.endtime);
        endtime.setText(resources.getString(R.string.myproduct_endtime)
                + (object.getLong("endDate") == 0 ? "永久" : Util
                .getDateOnlyDat(object.getLong("endDate"))));
        changeText(endtime);
        Log.i("MyP", endtime.getText().toString());
        TextView yergain = (TextView) v.findViewById(R.id.yergain);
        yergain.setText(resources.getString(R.string.myproduct_yegain)
                + Util.df.format(object.getDouble("earningsYesterday") / 100)
                + "(元)");
        changeText(yergain);
        TextView benxi = (TextView) v.findViewById(R.id.benxi);
        if (close) {
            benxi.setText(resources.getString(R.string.myproduct_benxi)
                    + Util.df.format(object.getDouble("money") / 100) + "(元)");
        } else {
            benxi.setText(resources.getString(R.string.myproduct_benxi)
                    + Util.df.format((object.getDouble("allEarnings") + object
                    .getDouble("money")) / 100) + "(元)");
        }
        changeText(benxi);
        TextView tag = (TextView) v.findViewById(R.id.tag);
        tag.setText(resources.getString(R.string.myproduct_tag) + object.getString("status"));
        changeText(tag, Color.parseColor("#ed3535"));
        if (obj != null && obj.getBoolean("vip")) {
            v.findViewById(R.id.item_product_bj).setBackgroundResource(
                    R.drawable.img_shuiyin1);
            v.findViewById(R.id.item_regular_vip_value).setVisibility(
                    View.VISIBLE);

            ((TextView) v.findViewById(R.id.item_regular_vip_value))
                    .setText(Util.df.format(obj.getDouble("earnings") * 100)
                            + "%");
            tv.setText(Util.df.format(object.optDouble("overlayEarnings", 0) * 100)
                    + "%");
            TextView vipcode = (TextView) v.findViewById(R.id.vipcode);
            vipcode.setVisibility(View.VISIBLE);
            vipcode.setText(resources.getString(R.string.myproduct_vip) + object.getString("vipCode"));

            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
            tv.getPaint().setAntiAlias(true);// 抗锯齿

        }
        TextView tv_turnout = (TextView) v.findViewById(R.id.tv_turnout);
        if (close) {
            tv_turnout.setBackground(getResources().getDrawable(R.drawable.textview_style));
        } else {
            tv_turnout.setBackground(getResources().getDrawable(R.drawable.textview_style3));
        }
        tv_turnout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (close) {

                    if (MyApplication.getInstance().self == null) {
                        login();
                        return;
                    }
                    Intent intent;
                    intent = new Intent(MyProductActivity.this,
                            OneMyProductActivity.class);

                    intent.putExtra("info", object.toString());
                    intent.putExtra("id", turnout_id);
                    startActivityForResult(intent, 2);
                } else {

                }
                Log.i("MyP", "tv_turnout");

            }
        });
        TextView tv_guntou = (TextView) v.findViewById(R.id.tv_guntou);
        Drawable drawable = null;
        if ("1".equals(repeat)) {
            drawable = getResources().getDrawable(R.drawable.img_guntougouxuankuang1);
            tv_guntou.setBackground(getResources().getDrawable(R.drawable.textview_style2));
        } else {
            drawable = getResources().getDrawable(R.drawable.img_guntougouxuankuang2);
            tv_guntou.setBackground(getResources().getDrawable(R.drawable.textview_style));
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Log.i("MyP", "tv_guntou" + drawable);
        tv_guntou.setCompoundDrawables(drawable, null, null, null);//设置TextView的drawableleft
        temp_view.put(turnout_id, v);


        tv_guntou.findViewById(R.id.tv_guntou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View guntouv) {

                Drawable drawable = null;
                if (!isGuntou(temp_repeat.get(turnout_id))) {
                    OpengunTou(turnout_id);
                } else {
                    closeGunTou(turnout_id);
                }
            }
        });

        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    login();
                    return;
                }
                Intent intent;
                tempClickview = v;
                intent = new Intent(MyProductActivity.this,
                        OneMyProductActivity.class);

                intent.putExtra("info", object.toString());
                intent.putExtra("id", turnout_id);
//                    intent.putExtra("close", close);
//                }
                startActivityForResult(intent, 2);
            }
        });
    }

    private void login(){
        Intent intent = new Intent(this,WXEntryActivity.class);
        intent.putExtra("isthome",true);
        startActivity(intent);
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
    }
    private void closeGunTou(final String turnout_id) {


        new PromptDialog(this, "是否取消滚投", new Complete() {
            @Override
            public void complete() {
                temp_repeat.put(turnout_id, "0");

                final String param = "method=repeatInvest&token=" + Util.token + "&id=" + turnout_id + "&uid=" + MyApplication.getInstance().self.getId() + "&repeat=" + temp_repeat.get(turnout_id);
                HttpRequest.sendGet(TLUrl.getInstance().URL_productServlet, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        Log.i("MyP", param + "");
                        Log.i("MyP", msg + "");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    JSONObject object1 = new JSONObject(msg);
                                    if (object1.getInt("status") == 1) {
                                        Log.i("MyP", object1.getJSONObject("msg").toString());
                                        refreshProduct(object1.getJSONObject("msg"), null, temp_view.get(turnout_id), turnout_id);
                                        showToast("取消了滚投");
                                    } else {
                                        showToast("网络异常,稍后重试");


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }
        }).show();

    }

    private void OpengunTou(final String turnout_id) {

        new PromptDialog(this, "是否设置自动滚投，如果此投资为结算状态，将立即在次投资；否则将在投资结算后自动滚投！", new Complete() {
            @Override
            public void complete() {
                temp_repeat.put(turnout_id, "1");

                final String param = "method=repeatInvest&token=" + Util.token + "&id=" + turnout_id + "&uid=" + MyApplication.getInstance().self.getId() + "&repeat=" + temp_repeat.get(turnout_id);
                HttpRequest.sendGet(TLUrl.getInstance().URL_productServlet, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        Log.i("MyP", param + "");
                        Log.i("MyP", msg + "");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    JSONObject object1 = new JSONObject(msg);
                                    if (object1.getInt("status") == 1) {
                                        Log.i("MyP", object1.getJSONObject("msg").toString());
                                        refreshProduct(object1.getJSONObject("msg"), null, temp_view.get(turnout_id), turnout_id);
                                        showToast("选择了滚投");
                                    } else {
                                        showToast("网络异常,稍后重试");


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }
        }).show();


    }

    private void changeText(TextView view, int... color) {
        String str = view.getText().toString();
        int index = str.indexOf(":");
        if (index < 0) {
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#555555")),
                index, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (color.length > 0)
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#ed3535")),
                    index, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(style);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                initMyInfo();
            }
        }, 2000);
        super.onResume();
    }

    private boolean isGuntou(String repeat) {


        if ("1".equals(repeat)) {
            return true;
        } else {
            return false;
        }
    }
}
