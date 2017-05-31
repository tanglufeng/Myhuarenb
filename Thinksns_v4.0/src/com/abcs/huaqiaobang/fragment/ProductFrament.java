package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.MyProductActivity;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.activity.OneMyProductActivity;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.ProductScrollView;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ProductFrament extends Fragment {

    private View view;

    LinearLayout layout;
    private Handler handler = new Handler();
    private int page = 1;
    private boolean loadMore = true;
    private ProductScrollView scrollview_product;
    private int scroll_Y = 0;
    private Map<String, String> temp_repeat = new HashMap<String, String>();
    private Map<String, View> temp_view = new HashMap<String, View>();

    private MyProductActivity activity;
    private int index;
    private boolean isShow = true;
    private View tempClickview;


    public static ProductFrament getInstance(int index) {
        ProductFrament frament = new ProductFrament();
        Bundle b = new Bundle();
        b.putInt("index", index);
        frament.setArguments(b);
        return frament;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_product, null);
        }

        activity = (MyProductActivity)getActivity();
        index = getArguments().getInt("index");

        layout = (LinearLayout) view.findViewById(R.id.myproduct_list);
        scrollview_product = (ProductScrollView) view.findViewById(R.id.scrollview_product);
        scrollview_product.setOnScrollToBottomLintener(new ProductScrollView.OnScrollToBottomListener() {


            @Override
            public void onScrollBottomListener(boolean isBottom,
                                               int scrollY) {
                // TODO Auto-generated method stub
                if (isBottom && scrollY >= scroll_Y) {

                    scroll_Y = scrollY;
                    if (loadMore) {
                        page++;
                        addMyProduct();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (getUserVisibleHint()) {
            if (isShow) {
                getMyProduct();
            }
            isShow = false;
        } else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void addMyProduct() {
        // TODO Auto-generated method stub
        if (MyApplication.getInstance().self == null) {
            return;
        }
//        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("getMyProduct",
                "method=getInvestProductList&page=1&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=getInvestProductList&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token + "&page=" + page + "&type=" + index, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
//                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    if (jsonObject.getInt("status") == 1) {
                                        JSONArray jsonArray = jsonObject
                                                .getJSONArray("msg");
                                        Log.i("MyP", msg);

                                        if (jsonArray.length() == 0) {
                                            showToast("暂无投资信息!");
                                            loadMore = false;
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


    public void getMyProduct() {
        if (MyApplication.getInstance().self == null) {
            return;
        }
//        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("getMyProduct",
                "method=getInvestProductList&page=1&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=getInvestProductList&size=10&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token + "&page=" + page + "&type=" + index, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
//                        ProgressDlgUtil.stopProgressDlg();
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

    private void showToast(String s) {

        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    private View getOneProduct(final JSONObject object, final String turnout_id)
            throws JSONException {
        final JSONObject obj = object.getJSONObject("product");
//        final boolean close= object.getBoolean("close");

        View v = null;
        LinearLayout.LayoutParams params = null;

        if (index == 3) {
            v = View.inflate(activity, R.layout.occft_item_producttixian, null);
            params = new LinearLayout.LayoutParams(
                    Util.WIDTH, Util.HEIGHT / 4);


        } else {
            v = View.inflate(activity, R.layout.occft_item_product, null);
            params = new LinearLayout.LayoutParams(
                    Util.WIDTH, Util.HEIGHT / 3);
        }
        params.topMargin = 10;
        v.setLayoutParams(params);

        refreshProduct(object, obj, v, turnout_id);
//        final ImageView jiesuan = (ImageView) v.findViewById(R.id.img_jiesuan);
//        jiesuan.setVisibility(close ? View.VISIBLE : View.GONE);


        return v;
    }

    private void refreshProduct(final JSONObject object, JSONObject obj, View v, final String turnout_id) throws JSONException {

//        Log.i("MyP", "refreshProduct" + v.findViewById(R.id.main_txt_current_sellandbuy));
        Log.i("MyP", "refreshProduct" + object.getBoolean("close"));
        final boolean close = object.getBoolean("close");
        final String repeat = object.getString("repeat");
        boolean isGuntou=object.getLong("endDate")-System.currentTimeMillis()>7*24*60*60*1000?true:false;
        Log.i("zjz","endData="+object.getLong("endDate"));
        Log.i("zjz","currentData="+System.currentTimeMillis());
        Log.i("zjz","isGuntou="+isGuntou);
        temp_repeat.put(turnout_id, repeat);


        Log.i("MyP", "getOneProduct==" + repeat);
        TextView tv = (TextView) v.findViewById(R.id.item_product_icon_gain);
        if (obj != null) {
            if (index != 3) {
                ((TextView) v.findViewById(R.id.main_txt_current_sellandbuy))
                        .setText("已售出" + obj.getLong("soldMoney") / 100 + "元    已有"
                                + obj.getInt("buyNum") + "人购买");
            }
            tv.setText(Util.df.format(obj.getDouble("earnings") * 100) + "%");
            MyApplication.imageLoader.displayImage(obj.getString("iconUrl"),
                    (ImageView) v.findViewById(R.id.item_product_icon),
                    MyApplication.options);
        }
        Resources resources = getResources();
        ImageView jiesuan = (ImageView) v.findViewById(R.id.img_jiesuan);
        jiesuan.setVisibility(close ? View.VISIBLE : View.GONE);
        TextView buyin = (TextView) v.findViewById(R.id.buyin);

        if (index == 3) {
            Log.i("LOG_DEBUG", object.optDouble("allmoney", 0) + "");
            buyin.setText(resources.getString(R.string.myproduct_buycost)
                    + Util.df.format(object.optDouble("allmoney", 0) == 0 ? object.getDouble("money") : object.optDouble("allmoney", 0) / 100) + "(元)");
            changeText(buyin);
        } else {
            buyin.setText(resources.getString(R.string.myproduct_buycost)
                    + Util.df.format(object.getDouble("money") / 100) + "(元)");
            changeText(buyin);
        }
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
            if (index == 3) {
                benxi.setText(resources.getString(R.string.myproduct_benxi)
                        + Util.df.format(object.optDouble("allmoney", 0) == 0 ? object.getDouble("money") : object.optDouble("allmoney", 0) / 100) + "(元)");
            } else {
                benxi.setText(resources.getString(R.string.myproduct_benxi)
                        + Util.df.format(object.getDouble("money") / 100) + "(元)");
            }
        } else {

            benxi.setText(resources.getString(R.string.myproduct_benxi)
                    + Util.df.format((object.getDouble("allEarnings") + object
                    .getDouble("money")) / 100) + "(元)");
        }
        changeText(benxi);
        TextView tag = (TextView) v.findViewById(R.id.tag);

        if (index != 3) {
            tag.setText(resources.getString(R.string.myproduct_tag) + object.getString("status"));
        } else {
            tag.setText(resources.getString(R.string.myproduct_tag) + "已提现 时间:" + Util.getDateOnlyDat(object.optLong("updateDate", 0)));
        }
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
        if (index != 3) {
            gunTouInit(object, v, turnout_id, close, repeat,isGuntou);
        }
        if (tempClickview != null && index == 2) {
            layout.removeView(tempClickview);
        }
//        v.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (MyApplication.getInstance().self == null) {
//                    login();
//                    return;
//                }
//                Intent intent;
//                if (object.optInt("productType") == 2) {
//                    intent = new Intent(MyProductActivity.this,
//                            CurrentActivity.class);
//                    try {
//                        intent.putExtra("info", Util
//                                .getStringByObject(getOneProduce(object
//                                        .getJSONObject("product"))));
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    intent = new Intent(MyProductActivity.this,
//                            OneMyProductActivity.class);
//
//                    intent.putExtra("info", object.toString());
//                    intent.putExtra("id", turnout_id);
////                    intent.putExtra("close", close);
//                }
//                startActivity(intent);
//            }
//        });
    }

    private void gunTouInit(final JSONObject object, final View v, final String turnout_id, final boolean close, String repeat, final boolean isGuntou) {
        TextView tv_turnout = (TextView) v.findViewById(R.id.tv_turnout);
//        GradientDrawable gd = new GradientDrawable();//创建drawable
//        int fillColor = 0;//内部填充颜色
//        gd.setCornerRadius(10);
        if (close) {
//            fillColor = Color.parseColor("#eb5041");
            tv_turnout.setBackground(getResources().getDrawable(R.drawable.textview_style));
        } else {
//            fillColor = Color.parseColor("#969696");
            tv_turnout.setBackground(getResources().getDrawable(R.drawable.textview_style3));
        }
//        gd.setColor(fillColor);

        tv_turnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View tv) {


                if (close) {

                    if (MyApplication.getInstance().self == null) {
//                        login();
                        return;
                    }
                    tempClickview = v;
                    Intent intent;
//                    if (object.optInt("productType") == 2) {
//                        intent = new Intent(activity,
//                                CurrentActivity.class);
//                        try {
//                            intent.putExtra("info", Util
//                                    .getStringByObject(getOneProduce(object
//                                            .getJSONObject("product"))));
//                        } catch (Throwable e) {
//                            e.printStackTrace();
//                        }
//                    } else {
                    intent = new Intent(activity,
                            OneMyProductActivity.class);

                    intent.putExtra("info", object.toString());
                    intent.putExtra("id", turnout_id);
//                    }
                    startActivityForResult(intent, 1);
                } else {

                }
                Log.i("MyP", "tv_turnout");

            }
        });
        TextView tv_guntou = (TextView) v.findViewById(R.id.tv_guntou);
        Drawable drawable = null;
//        GradientDrawable gd1 = new GradientDrawable();//创建drawable
//        int fillColor1 = 0;//内部填充颜色
//        gd1.setCornerRadius(10);
        if(!isGuntou){
            drawable = getResources().getDrawable(R.drawable.img_guntougouxuankuang2);
            tv_guntou.setBackground(getResources().getDrawable(R.drawable.textview_style3));
        } else if ("1".equals(repeat)) {
            drawable = getResources().getDrawable(R.drawable.img_guntougouxuankuang1);
            tv_guntou.setBackground(getResources().getDrawable(R.drawable.textview_style2));
//            fillColor1 = Color.parseColor("#50aa50");
        } else {
            drawable = getResources().getDrawable(R.drawable.img_guntougouxuankuang2);
            tv_guntou.setBackground(getResources().getDrawable(R.drawable.textview_style));
//            fillColor1 = Color.parseColor("#eb5041");
        }
//        gd1.setColor(fillColor1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Log.i("MyP", "tv_guntou" + drawable);
        tv_guntou.setCompoundDrawables(drawable, null, null, null);//设置TextView的drawableleft
//        tv_guntou.setBackground(gd1);
        temp_view.put(turnout_id, v);


        tv_guntou.findViewById(R.id.tv_guntou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View guntouv) {

                Drawable drawable = null;
//                GradientDrawable gd = new GradientDrawable();//创建drawable
//                int fillColor = 0;//内部填充颜色
//                gd.setCornerRadius(10);
                if(!isGuntou){
                    showToast("距离结束时间7天内无法进行滚投");
                }else {
                    if (!isGuntou(temp_repeat.get(turnout_id))) {


                        tempClickview = v;
                        OpengunTou(turnout_id);


//                    drawable = getResources().getDrawable(R.drawable.img_gouxuankuang1);
//                    guntouv.setBackground(getResources().getDrawable(R.drawable.textview_style2));
//                    fillColor = Color.parseColor("#50aa50");

                    } else {

                        closeGunTou(turnout_id);

//                    temp_repeat.put(turnout_id, "0");
//                    drawable = getResources().getDrawable(R.drawable.img_gouxuankuang2);
//                    guntouv.setBackground(getResources().getDrawable(R.drawable.textview_style));
//                    fillColor = Color.parseColor("#eb5041");
                    }
                }

//                gd.setColor(fillColor);

//                final String param = "method=repeatInvest&token=" + Util.token + "&id=" + turnout_id + "&uid=" + MyApplication.getInstance().self.getId() + "&repeat=" + temp_repeat.get(turnout_id);
//                HttpRequest.sendGet(TLUrl.URL_productServlet, param, new HttpRevMsg() {
//                    @Override
//                    public void revMsg(final String msg) {
//                        Log.i("MyP", param + "");
//                        Log.i("MyP", msg + "");
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//
//                                    JSONObject object1 = new JSONObject(msg);
//                                    if (object1.getInt("status") == 1) {
//                                        Log.i("MyP", object1.getJSONObject("msg").toString());
//                                        refreshProduct(object1.getJSONObject("msg"), null, temp_view.get(turnout_id), turnout_id);
//                                    } else {
//                                        showToast("网络异常,稍后重试");
//
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                    }
//                });
//                gd.setColor(fillColor);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                Log.i("MyP", "tv_guntou" + drawable);
//                ((TextView) guntouv).setCompoundDrawables(drawable, null, null, null);//设置TextView的drawableleft
//                ((TextView) v).setCompoundDrawablePadding(10);//设置图片和text之间的间距
//                v.setBackground();
            }
        });


        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                tempClickview = v;
                if (MyApplication.getInstance().self == null) {
//                    login();
                    return;
                }
                Intent intent;
//                if (object.optInt("productType") == 2) {
//                    intent = new Intent(activity,
//                            CurrentActivity.class);
//                    try {
//                        intent.putExtra("info", Util
//                                .getStringByObject(getOneProduce(object
//                                        .getJSONObject("product"))));
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                } else {
                intent = new Intent(activity,
                        OneMyProductActivity.class);

                intent.putExtra("info", object.toString());
                intent.putExtra("id", turnout_id);
//                    intent.putExtra("close", close);
//                }
                startActivityForResult(intent, 1);
            }
        });
    }

    private void closeGunTou(final String turnout_id) {


        new PromptDialog(getActivity(), "是否取消滚投", new Complete() {
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


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        new PromptDialog(getActivity(), "是否设置自动滚投，如果此投资为结算状态，将立即在次投资；否则将在投资结算后自动滚投！", new Complete() {
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

    private Product getOneProduce(JSONObject object) throws JSONException {
        Product product = new Product();
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
        return product;
    }


    private boolean isGuntou(String repeat) {


        if ("1".equals(repeat)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && tempClickview != null) {
            layout.removeView(tempClickview);
            Intent intent = new Intent(getActivity(), NoticeDialogActivity.class);
            intent.putExtra("msg", data.getStringExtra("msg"));
            startActivity(intent);


//            Snackbar snackbar = Snackbar.make(view, data.getStringExtra("msg"), Snackbar.LENGTH_LONG);
//            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) snackbar.getView();
////            ve.setBackgroundColor(0xeb5140);
//            TextView txt = ((TextView) ve.findViewById(R.id.snackbar_text));
//            txt.setTextColor(Color.parseColor("#FFFFFF"));
//            txt.setTextSize(15);
//            ViewGroup.LayoutParams vl = ve.getLayoutParams();
//            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(vl.width, vl.height);
//            ll.gravity = Gravity.TOP;
//            ve.setLayoutParams(ll);
//            ve.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_left_in_activity));
//            snackbar.show();
        }
    }
}
