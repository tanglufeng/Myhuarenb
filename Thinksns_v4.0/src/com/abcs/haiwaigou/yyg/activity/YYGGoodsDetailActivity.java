package com.abcs.haiwaigou.yyg.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.adapter.YYGBuyRecordAdapter;
import com.abcs.haiwaigou.yyg.view.ReadMoreTextView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGGoodsDetailActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.img_icon)
    ImageView imgIcon;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.t_all_need)
    TextView tAllNeed;
    @InjectView(R.id.goods_prograss)
    ProgressBar goodsPrograss;
    @InjectView(R.id.t_canyu)
    TextView tCanyu;
    @InjectView(R.id.t_remain)
    TextView tRemain;
    @InjectView(R.id.t_buy)
    TextView tBuy;
    @InjectView(R.id.img_winner_head)
    CircleImageView imgWinnerHead;
    @InjectView(R.id.img_tip)
    ImageView imgTip;
    @InjectView(R.id.t_winner)
    TextView tWinner;
    @InjectView(R.id.t_joins)
    TextView tJoins;
    @InjectView(R.id.t_time)
    TextView tTime;
    @InjectView(R.id.img_last_lottery)
    ImageView imgLastLottery;
    @InjectView(R.id.t_code)
    TextView tCode;
    @InjectView(R.id.linear_count_down)
    LinearLayout linearCountDown;
    @InjectView(R.id.img_jisuan)
    ImageView imgJisuan;
    @InjectView(R.id.relative_calculate)
    RelativeLayout relativeCalculate;
    @InjectView(R.id.relative_older)
    RelativeLayout relativeOlder;
    @InjectView(R.id.relative_share)
    RelativeLayout relativeShare;
    @InjectView(R.id.img_record_more)
    ImageView imgRecordMore;
    @InjectView(R.id.relative_record)
    RelativeLayout relativeRecord;
    @InjectView(R.id.t_total_money)
    TextView tTotalMoney;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    String goods_title;
    String goods_img;
    String act_id;
    int all_need;
    int remain_need;
    int num = 1;
    double price_one;
    String total_money;
    @InjectView(R.id.relative_last)
    RelativeLayout relativeLast;
    @InjectView(R.id.img_isbuy)
    ImageView imgIsbuy;
    @InjectView(R.id.t_my_code)
    ReadMoreTextView tMyCode;
    @InjectView(R.id.relative_my_code)
    RelativeLayout relativeMyCode;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.relative_detail)
    RelativeLayout relativeDetail;

    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间
    boolean isShow;
    boolean isLoadMore = false;
    boolean isMore = true;
    int currentPage = 1;
    boolean first = false;
    boolean isBuy = false;
    private Handler handler = new Handler();
    YYGBuyRecordAdapter yygBuyRecordAdapter;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    MyBroadCastReceiver myBroadCastReceiver;
    private Handler mHandler = new Handler();
    private String goods_id;
    private String goods_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_lottery_goods_detail);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
        }
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
//        tMyCode.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras feugiat, nunc eget volutpat viverra, lacus ligula venenatis nulla, in accumsan eros eros nec ipsum. Fusce eget massa id lorem blandit lobortis. Donec lacus ligula, commodo quis eleifend ac, rhoncus sed mi. Curabitur luctus justo vel consequat fermentum. Donec facilisis elit in sem suscipit pharetra. Curabitur porta et dolor eget pretium. Mauris quam augue, interdum nec tempor vitae, consectetur at erat.");
//        tMyCode.setText("Lorem ipsum dolor");
        isBuy = getIntent().getBooleanExtra("isBuy", false);

        goods_title = getIntent().getStringExtra("title");
        goods_img = getIntent().getStringExtra("img_icon");
        act_id = getIntent().getStringExtra("act_id");
        all_need = getIntent().getIntExtra("all_need", 0);
        remain_need = getIntent().getIntExtra("remain_need", 0);
        price_one = getIntent().getDoubleExtra("price_one", 0);
        total_money = getIntent().getStringExtra("total_money");


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                first = true;
                initTopViewDatas();
                if (MyApplication.getInstance().self != null)
                    initMyCode();
                initRecycler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        if (MyApplication.getInstance().self != null)
            initMyCode();
//        initTopView();
        initTopViewDatas();
        setOnListener();
        initRecycler();
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGBUY)) {
                Log.i("zjz", "YYG支付成功");
                initTopViewDatas();
                if (MyApplication.getInstance().self != null)
                    initMyCode();
                initRecycler();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initMyCode() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_buy_code, "activityId=" + act_id + "&page=1&pageSize=10000" + "&userId=" + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_my_code_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    showToast("请求失败,请重试");
                                    return;
                                }
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.optJSONArray("msg");
                                        if (jsonArray != null) {
                                            StringBuffer stringBuffer = new StringBuffer();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                stringBuffer.append(object.optString("buycode"));
                                                if (i != jsonArray.length() - 1) {
                                                    stringBuffer.append("，");
                                                }
                                            }
                                            if (relativeMyCode != null)
                                                relativeMyCode.setVisibility(View.VISIBLE);
                                            if (tMyCode != null)
                                                tMyCode.setText(stringBuffer.toString());
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

    FullyLinearLayoutManager fullyLinearLayoutManager;

    private void initRecycler() {
        yygBuyRecordAdapter = new YYGBuyRecordAdapter(this);
//        goodsList.clear();
//        for (int i = 0; i < 20; i++) {
//            Goods goods = new Goods();
//            goods.setTitle("华侨" + i);
//            goods.setId(i);
//            goodsList.add(goods);
//        }
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        initDatas();
        recyclerView.setAdapter(yygBuyRecordAdapter);
    }

    private void initDatas() {
        Log.i("zjz", "currentPage=" + currentPage);
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_record, "page=" + currentPage + "&pageSize=10" + "&activityId=" + act_id,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_record_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    showToast("请求失败,请重试");
                                    return;
                                }

                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.getJSONArray("msg");
                                        if (jsonArray != null) {
                                            if (isLoadMore) {
                                                int currentSize = yygBuyRecordAdapter.getItemCount();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(currentSize + i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject userObj = object.optJSONObject("user");
                                                    goods.setTitle(userObj.optString("nickname"));
                                                    goods.setQ_uid(userObj.optString("loginIp"));
                                                    goods.setPicarr(userObj.optString("avator"));
                                                    goods.setTime(object.optLong("payTime"));
                                                    goods.setGoods_num(object.optInt("buyNum"));
                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    addItems(goodsList);
                                                }
                                            } else {
                                                yygBuyRecordAdapter.getList().clear();
                                                goodsList.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject userObj = object.optJSONObject("user");
                                                    goods.setTitle(userObj.optString("nickname"));
                                                    goods.setQ_uid(userObj.optString("loginIp"));
                                                    goods.setPicarr(userObj.optString("avator"));
                                                    goods.setTime(object.optLong("payTime"));
                                                    goods.setGoods_num(object.optInt("buyNum"));
                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    yygBuyRecordAdapter.addItems(goodsList);
                                                    yygBuyRecordAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            View view = getLayoutInflater().inflate(R.layout.hwg_yyg_click_load_more, null);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(YYGGoodsDetailActivity.this, 30));
                                            LinearLayout linear_root = (LinearLayout) view.findViewById(R.id.linear_root);
                                            final TextView t_text = (TextView) view.findViewById(R.id.t_text);
                                            linear_root.setLayoutParams(params);
                                            linear_root.setGravity(Gravity.CENTER);
                                            linear_root.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (isMore) {
                                                        currentPage += 1;
                                                        isLoadMore = true;
                                                        initDatas();
                                                        t_text.setText("点击加载更多");
                                                    } else {
                                                        t_text.setText("没有更多了");
                                                    }
                                                }
                                            });
                                            yygBuyRecordAdapter.addFootView(view);
                                        } else {
                                            isMore = false;
                                        }
                                        Log.i("zjz", "yyg_record_isMore=" + isMore);
//                                        yygBuyRecordAdapter.notifyDataSetChanged();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }
                });
    }

    private void initTopViewDatas() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_detail, "activityId=" + act_id,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_goods_detail_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    showToast("请求失败,请重试");
                                    ProgressDlgUtil.stopProgressDlg();
                                    return;
                                }
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONObject jsonObject = result.optJSONObject("msg");
                                        JSONObject preObj = jsonObject.optJSONObject("priorPeriod");
                                        if (preObj != null) {
                                            JSONObject userObj = preObj.optJSONObject("user");
                                            if (relativeLast != null)
                                                relativeLast.setVisibility(View.VISIBLE);
                                            if (relativeCalculate != null)
                                                relativeCalculate.setVisibility(View.VISIBLE);
                                            if (imgWinnerHead != null)
                                                ImageLoader.getInstance().displayImage(userObj.optString("avator"), imgWinnerHead, Options.getUserHeadOptions());
                                            if (tCode != null)
                                                tCode.setText(preObj.optString("luckCode"));
                                            if (tWinner != null)
                                                tWinner.setText(userObj.optString("nickname"));
                                            if (tTime != null)
                                                tTime.setText(Util.format.format(preObj.optLong("accomplishTime")));
                                            if (tJoins != null)
                                                tJoins.setText(preObj.optString("buyNum") + "次");
                                        }
                                        JSONObject goodsObj = jsonObject.optJSONObject("goods");
                                        if (goodsObj != null) {
                                            Goods goods = new Goods();
                                            if (tTotalMoney != null)
                                                tTotalMoney.setText("yen" + goodsObj.optString("price"));
                                            if (imgIcon != null)
                                                ImageLoader.getInstance().displayImage(goodsObj.optString("picture"), imgIcon, Options.getHDOptions());
                                            goods_pic=goodsObj.optString("picture");
                                            if (tTitle != null)
                                                tTitle.setText("第(" + jsonObject.optString("periodsNum") + ")期 " + goodsObj.optString("name"));
                                            goods_title = "第(" + jsonObject.optString("periodsNum") + ")期 " + goodsObj.optString("name");
                                            if (jsonObject.optInt("unitPrice") < 1) {
                                                goods.setZongrenshu(jsonObject.optInt("tatalPrice"));
                                                goods.setShenyurenshu(jsonObject.optInt("surplusValue"));
                                            } else {
                                                goods.setZongrenshu(jsonObject.optInt("tatalPrice") / jsonObject.optInt("unitPrice"));
                                                goods.setShenyurenshu(jsonObject.optInt("surplusValue") / jsonObject.optInt("unitPrice"));
                                            }
                                            goods.setCanyurenshu(goods.getZongrenshu() - goods.getShenyurenshu());
                                            if (tAllNeed != null)
                                                tAllNeed.setText("总需:" + goods.getZongrenshu());
                                            all_need = goods.getZongrenshu();
                                            int pro = (int) (Float.valueOf(goods.getCanyurenshu())
                                                    / Float.valueOf(goods.getZongrenshu()) * 100);
                                            if (goodsPrograss != null)
                                                goodsPrograss.setProgress(pro);
                                            if (tRemain != null)
                                                tRemain.setText("剩余" + goods.getShenyurenshu());
                                            remain_need = goods.getShenyurenshu();
                                            if (tCanyu != null)
                                                tCanyu.setText(goods.getCanyurenshu() + "参与");
                                            if(relativeDetail!=null){
                                                relativeDetail.setVisibility(goodsObj.optString("isEntity").equals("1")?View.VISIBLE:View.GONE);
                                                goods_id=goodsObj.optString("hqbGoodsId");
                                            }
                                        }

                                        if (imgIsbuy != null)
                                            imgIsbuy.setVisibility(isBuy ? View.GONE : View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    swipeRefreshLayout.setRefreshing(false);
                                    ProgressDlgUtil.stopProgressDlg();
                                }

                            }
                        });
                    }
                });
    }

    private void initTopView() {
        tTotalMoney.setText("yen" + total_money);
        ImageLoader.getInstance().displayImage(goods_img, imgIcon, Options.getHDOptions());
        tAllNeed.setText("总需:" + all_need);
        tTitle.setText(goods_title);
        int pro = (int) (Float.valueOf(all_need - remain_need)
                / Float.valueOf(all_need) * 100);
        goodsPrograss.setProgress(pro);
        tRemain.setText("剩余" + remain_need);
        tCanyu.setText((all_need - remain_need) + "参与");
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        tBuy.setOnClickListener(this);
        relativeRecord.setOnClickListener(this);
        relativeOlder.setOnClickListener(this);
        relativeDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.t_buy:
                if (MyApplication.getInstance().self == null) {
                    startActivity(new Intent(this, WXEntryActivity.class));
                    return;
                } else if (remain_need == 0) {
                    showToast("剩余人数为0，无法参与！");
                    return;
                }
                intent = new Intent(this, YYGBuyActivity.class);
                intent.putExtra("act_id", act_id);
                intent.putExtra("img_icon", goods_img);
                intent.putExtra("title", goods_title);
                intent.putExtra("all_need", all_need);
                intent.putExtra("remain_need", remain_need);
                intent.putExtra("price_one", price_one);
                startActivity(intent);
                break;
            case R.id.relative_record:
                isShow = !isShow;
                if (isShow) {
                    ObjectAnimator.ofFloat(imgRecordMore, "rotation", 0, 90).setDuration(durationRotate).start();
                    recyclerView.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(recyclerView, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgRecordMore, "rotation", 90, 0).setDuration(durationRotate).start();

                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                }
                break;
            case R.id.relative_older:
                intent = new Intent(this, YYGPreRecordActivity.class);
                intent.putExtra("act_id", act_id);
                startActivity(intent);
                break;
            case R.id.relative_detail:
                intent = new Intent(this, GoodsDetailActivity.class);
                intent.putExtra("sid",goods_id);
                intent.putExtra("pic", goods_pic);
                startActivity(intent);
                break;
        }
    }

    private void notifyDataSetChanged() {
        yygBuyRecordAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        yygBuyRecordAdapter.addItems(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBroadCastReceiver.unRegister();
    }
}
