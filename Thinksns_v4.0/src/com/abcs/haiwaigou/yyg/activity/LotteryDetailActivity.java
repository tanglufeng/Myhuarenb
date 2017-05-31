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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.OrderDetailActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.yyg.adapter.YYGBuyRecordAdapter;
import com.abcs.haiwaigou.yyg.view.MyTextView;
import com.abcs.haiwaigou.yyg.view.ReadMoreTextView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.sociax.android.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LotteryDetailActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.img_lotterying)
    ImageView imgLotterying;
    @InjectView(R.id.linear_count_down)
    LinearLayout linearCountDown;
    @InjectView(R.id.relative_count_down)
    RelativeLayout relativeCountDown;
    @InjectView(R.id.relative_older)
    RelativeLayout relativeOlder;
    @InjectView(R.id.relative_share)
    RelativeLayout relativeShare;
    @InjectView(R.id.img_record_more)
    ImageView imgRecordMore;
    @InjectView(R.id.relative_record)
    RelativeLayout relativeRecord;
    @InjectView(R.id.t_count_down)
    MyTextView tCountDown;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.t_total_money)
    TextView tTotalMoney;
    @InjectView(R.id.img_isbuy)
    ImageView imgIsbuy;
    @InjectView(R.id.t_code)
    TextView tCode;
    @InjectView(R.id.t_my_code)
    ReadMoreTextView tMyCode;
    @InjectView(R.id.relative_my_code)
    RelativeLayout relativeMyCode;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.relative_detail)
    RelativeLayout relativeDetail;
    @InjectView(R.id.t_refund)
    TextView tRefund;
    @InjectView(R.id.t_deliver)
    TextView tDeliver;
    @InjectView(R.id.linear_bottom)
    LinearLayout linearBottom;
    @InjectView(R.id.relative_order_detail)
    RelativeLayout relativeOrderDetail;

    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间
    boolean isShow;
    boolean isLoadMore = false;
    boolean isLoad = false;
    boolean isLotterying = false;
    boolean isMore = true;
    int currentPage = 1;
    boolean first = false;
    boolean isBuy = false;
    private Handler handler = new Handler();
    YYGBuyRecordAdapter yygBuyRecordAdapter;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    String act_id;
    private MyBroadCastReceiver myBroadCastReceiver;
    private Handler mHandler = new Handler();
    private String goods_id;
    private String order_id;
    private String goods_pic;
    private String isDeliver;
    private double discount;
    private double goods_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_yyg_activity_lottery_detail);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
        }
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        act_id = getIntent().getStringExtra("act_id");

        isLotterying = getIntent().getBooleanExtra("isLottery", false);
        isBuy = getIntent().getBooleanExtra("isBuy", false);

        Log.i("zjz", "isLotterying=" + isLotterying);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                first = true;
                if (MyApplication.getInstance().self != null)
                    initMyCode();
                initTopViewDatas();
                setOnListener();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        if (isLotterying) {
            relativeCountDown.setVisibility(View.VISIBLE);
            imgLotterying.setVisibility(View.VISIBLE);
        } else {
            relativeCountDown.setVisibility(View.GONE);
            imgLotterying.setVisibility(View.GONE);
        }
        if (MyApplication.getInstance().self != null)
            initMyCode();
        initTopViewDatas();
        setOnListener();
//        initRecycler();

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
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGLOTTERY)||intent.getStringExtra("type").equals(MyUpdateUI.YYGSETADDRESS)) {
                Log.i("zjz", "lottery_act更新揭晓");
                first = true;
                if (MyApplication.getInstance().self != null)
                    initMyCode();
                initTopViewDatas();
                setOnListener();
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


    private void initTopViewDatas() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_detail, "activityId=" + act_id,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_lottery_detail_msg=" + msg);
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

                                        JSONObject goodsObj = jsonObject.optJSONObject("goods");
                                        if (goodsObj != null) {
                                            if (imgIcon != null)
                                                ImageLoader.getInstance().displayImage(goodsObj.optString("picture"), imgIcon, Options.getHDOptions());
                                            goods_pic = goodsObj.optString("picture");
                                            goods_price = goodsObj.optDouble("price");
                                            if (tTotalMoney != null)
                                                tTotalMoney.setText("¥" + goodsObj.optString("price"));
                                            if (tTitle != null)
                                                tTitle.setText("第(" + jsonObject.optString("periodsNum") + ")期 " + goodsObj.optString("name"));
                                            if (relativeDetail != null) {
                                                relativeDetail.setVisibility(goodsObj.optString("isEntity").equals("1") ? View.VISIBLE : View.GONE);
                                                goods_id = goodsObj.optString("hqbGoodsId");
                                            }
                                            if(relativeOrderDetail!=null){
                                                relativeOrderDetail.setVisibility(jsonObject.has("hqbOrderId")&&!jsonObject.optString("hqbOrderId").equals("-1")? View.VISIBLE : View.GONE);
                                                order_id=jsonObject.optString("hqbOrderId");
                                            }
                                        }


                                        if (jsonObject.optString("substate").equals("0") && jsonObject.optLong("surplusTime") == -1) {
                                            //已揭晓
                                            if (relativeCountDown != null)
                                                relativeCountDown.setVisibility(View.GONE);
                                            if (imgLotterying != null)
                                                imgLotterying.setVisibility(View.GONE);
                                            JSONObject userObj = jsonObject.optJSONObject("user");
                                            if (userObj != null) {
                                                if (imgWinnerHead != null)
                                                    ImageLoader.getInstance().displayImage(userObj.optString("avator"), imgWinnerHead, Options.getUserHeadOptions());
                                                if (tWinner != null)
                                                    tWinner.setText(userObj.optString("nickname"));
                                                if (tTime != null)
                                                    tTime.setText(Util.format.format(jsonObject.optLong("accomplishTime")));
                                                if (tJoins != null)
                                                    tJoins.setText(jsonObject.optString("buyNum") + "次");
                                                if (tCode != null)
                                                    tCode.setText(jsonObject.optString("luckCode"));
                                                if (MyApplication.getInstance().self != null && linearBottom != null && goodsObj != null) {
                                                    if (MyApplication.getInstance().self.getId().equals(userObj.optString("userId")) && goodsObj.optString("isEntity").equals("1")) {
                                                        isDeliver = jsonObject.optString("isDeliver");
                                                        //isDeliver : 是否已做处理 [isDeliver = 0 (没有做处理)--isDeliver = 1 （已做发货处理）--isDeliver = 2（用户已收到货）--isDeliver =-1（已做云购币兑换处理）9待发货 ]
                                                        if (isDeliver.equals("0")) {
                                                            linearBottom.setVisibility(View.VISIBLE);
                                                            discount = jsonObject.optDouble("discount");
                                                        }


                                                    } else {
                                                        linearBottom.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        } else {
//                                        else if (jsonObject.optString("substate").equals("1")) {
                                            //正在揭晓中
                                            if (tCountDown != null && relativeCountDown != null & imgLotterying != null)
                                                if (jsonObject.optLong("surplusTime") != -1) {
//                                                    tCountDown.setTime((jsonObject.optLong("accomplishTime") + 3 * 60 * 1000) - System.currentTimeMillis());
                                                    tCountDown.setTime(jsonObject.optLong("surplusTime"));
                                                    relativeCountDown.setVisibility(View.VISIBLE);
                                                    imgLotterying.setVisibility(View.VISIBLE);
                                                } else {
                                                    relativeCountDown.setVisibility(View.GONE);
                                                    imgLotterying.setVisibility(View.GONE);
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

    FullyLinearLayoutManager fullyLinearLayoutManager;

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...",this);
        yygBuyRecordAdapter = new YYGBuyRecordAdapter(this);
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
                                                    if (jsonArray.length() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
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
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(LotteryDetailActivity.this, 30));
                                            LinearLayout linear_root = (LinearLayout) view.findViewById(R.id.linear_root);
                                            final TextView t_text = (TextView) view.findViewById(R.id.t_text);
                                            if (isMore) {
                                                t_text.setText("点击加载更多");
                                            } else {
                                                t_text.setText("没有更多了");
                                            }
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
                                        isLoad = true;

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }finally {
                                    ProgressDlgUtil.stopProgressDlg();
                                }


                            }
                        });
                    }
                });
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeRecord.setOnClickListener(this);
        relativeOlder.setOnClickListener(this);
        relativeDetail.setOnClickListener(this);
        tDeliver.setOnClickListener(this);
        tRefund.setOnClickListener(this);
        relativeOrderDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_record:
                if (!isLoad)
                    initRecycler();
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
                intent.putExtra("sid", goods_id);
                intent.putExtra("pic", goods_pic);
//                intent.putExtra("isYun", true);
                startActivity(intent);
                break;
            case R.id.t_deliver:
                intent = new Intent(this, YYGSetAddressActivity.class);
                intent.putExtra("act_id", act_id);
                intent.putExtra("goods_id", goods_id);
                startActivityForResult(intent, 1);
                break;
            case R.id.t_refund:
                new PromptDialog(this, "您确定将该商品折现？(商品价格" + discount + "折折现，折现后将有" + NumberUtils.formatPrice(goods_price * discount / 10) + "存入您的云购余额中)", new Complete() {
                    @Override
                    public void complete() {
                        ProgressDlgUtil.showProgressDlg("", LotteryDetailActivity.this);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_exchange, "&userId=" + MyApplication.getInstance().self.getId() + "&activityId=" + act_id, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            Log.i("zjz", "refund_msg=" + msg);
                                            if (object.getInt("status") == 1) {
                                                showToast("商品折现成功，已存入您的余额");
                                                MyUpdateUI.sendUpdateCollection(LotteryDetailActivity.this, MyUpdateUI.YYGREFUND);
                                                if (linearBottom != null)
                                                    linearBottom.setVisibility(View.GONE);
                                                ProgressDlgUtil.stopProgressDlg();
                                            } else {
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
                }).show();
                break;
            case R.id.relative_order_detail:
                intent=new Intent(this, OrderDetailActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            if (linearBottom != null)
                linearBottom.setVisibility(View.GONE);
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
