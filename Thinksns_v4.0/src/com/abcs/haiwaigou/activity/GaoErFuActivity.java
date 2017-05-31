package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.HotGoodsRecyclerAapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.HotHeaderView;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GaoErFuActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, HWGFragmentViewHolder.ItemOnClick {

    public final static String SORTUP_BRPRICE = "sort_up_by_price";
    public final static String SORTDOWN_BRPRICE = "sort_down_by_price";
    public final static String SORTDOWN_BRSALES = "sort_down_by_sales";
    public final static String SORTUP_BRSALES = "sort_up_by_sales";
    public final static String SORTDOWN_BRNEW = "sort_down_by_new";
    public final static String SORTDOWN_BRRENQI = "sort_down_by_renqi";
    public final static String SORTUP_BRRENQI = "sort_up_by_renqi";
    public final static String NOMAL = "sort_by_nomal";
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.img_overlay)
    ImageView imgOverlay;
    @InjectView(R.id.img_all_buy)
    ImageView imgAllBuy;
    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;


    private View view;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    HotGoodsRecyclerAapter hotGoodsRecyclerAapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    public Handler handler = new Handler();
    private RequestQueue mRequestQueue;
    private int currentPage = 1;
    private int count;
    //    private int totalPage;
//    boolean isLoadMore = false;
    boolean first = false;

    boolean isSearch = false;
    boolean isStore = false;
    boolean isAd = false;
    //    private String type;
    private String keyword;
    private String picture;
    private String words = "";
    private static final int REQUEST_COUNT = 10;
    private int mCurrentCounter = 0;
    FullyGridLayoutManager fullyGridLayoutManager;

    HotHeaderView hotHeaderView;
    boolean isMain;
    boolean isSale;
    boolean isWeek;
    String special_id,city_id;
    MyBroadCastReceiver myBroadCastReceiver;
    private boolean isSortUp;    //是否为价格升序排列
    private boolean isVolume;    //是否为销量升序排列
    private boolean isFilter;    //是否为人气升序排列
    int text_position;

    private ArrayList<String> goodsIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_gaoerfu, null);
        }

        words = getIntent().getStringExtra("words");
        picture = getIntent().getStringExtra("picture");
        text_position = getIntent().getIntExtra("text_position", 0);
        special_id = getIntent().getStringExtra("special_id");
        city_id = getIntent().getStringExtra("city_id");
        isMain = getIntent().getBooleanExtra("isMain", false);
        isSale = getIntent().getBooleanExtra("isSale", false);
        isWeek = getIntent().getBooleanExtra("isWeek", false);
        hotHeaderView = new HotHeaderView(this);
        setContentView(view);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        if(getIntent().getStringExtra("title").equals("null")||getIntent().getStringExtra("title").equals("")){
            tTitle.setText("海外购");
        }else {
            tTitle.setText(getIntent().getStringExtra("title"));
        }
        setOnListener();
        initRecyclerView();
        initInCartNum();
//        RelativeLayout relative_title = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_title);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            seperate.setVisibility(View.VISIBLE);
////            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relative_title.getLayoutParams();
////            params.setMargins(0, getStatusBarHeight(), 0, 0);
////            relative_title.setLayoutParams(params);
////            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relativeSpinner.getLayoutParams();
////            layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
////            relativeSpinner.setLayoutParams(layoutParams);
//        }

    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
            initInCartNum();
        }

        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initInCartNum() {
        if (MyApplication.getInstance().getMykey() != null) {
            new InitCarNum(carNum, this,"");
        }
    }


    private void setOnListener() {
        imgOverlay.setOnClickListener(this);
        imgAllBuy.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeCart.setOnClickListener(this);
    }

    ArrayAdapter<CharSequence> companyAdapter = null;

    private void initRecyclerView() {
        hotGoodsRecyclerAapter = new HotGoodsRecyclerAapter(this, this);
//        recycleViewGridAdapter=new RecycleViewGridAdapter(this);
        initAllDates();
        hotGoodsRecyclerAapter.addHeadView(hotHeaderView);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        hotGoodsRecyclerAapter.setChangeGridLayoutManager(new HotGoodsRecyclerAapter.ChangeGridLayoutManagerSpance() {
            @Override
            public void change(final int size, final boolean isAddHead, final boolean isAddFoot) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int spanSzie = 1;
                        if (isAddHead) {
                            if (position == 0) {
                                spanSzie = gridLayoutManager.getSpanCount();
                            }
                        }

                        if (isAddFoot) {
                            if (position == size) {
                                spanSzie = gridLayoutManager.getSpanCount();
                            }
                        }
                        return spanSzie;
                    }
                });
            }
        });
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(hotGoodsRecyclerAapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, gridLayoutManager, false);
//        RecyclerViewUtils.setHeaderView(recyclerView, hotHeaderView);
        RelativeLayout relativeBack = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_back);
        RelativeLayout relative_cart = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_cart);
        TextView t_text = (TextView) hotHeaderView.findViewById(R.id.t_text);
//        Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/fangzhenglantinghei.TTF");
//        t_text.setTypeface(fontFace);
        Util.setFZLTHFont(t_text);
        if (words.length() == 0 || words.equals("null")) {
            t_text.setVisibility(View.GONE);
//            initText(t_text);
        } else {
            t_text.setVisibility(View.VISIBLE);
            t_text.setText(Util.ReplaceSpecialSymbols(words));
        }
        ImageView img_banner = (ImageView) hotHeaderView.findViewById(R.id.img_banner);
        if (isMain) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(this, 245));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (isSale) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(this, 195));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (isWeek) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(this, 165));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        }
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(img_banner, picture);
        if(!TextUtils.isEmpty(picture)){
            ImageLoader.getInstance().displayImage(picture, img_banner, Options.getHDOptions());
        }else {
//            if(!TextUtils.isEmpty(words)){
//                //  根据word  判断类型
//
//                if(words.equals("1")){  /****高尔夫*****/
//                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_golfbanner, img_banner, Options.getHDOptions());
//                }else  if(words.equals("2")){/****政要通道*****/
//                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.vipchannel, img_banner, Options.getHDOptions());
//                }else  if(words.equals("3")){/****私人管家*****/
//                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.localguider, img_banner, Options.getHDOptions());
//                }else  if(words.equals("4")){/****贴身服务*****/
//                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.housekeeper, img_banner, Options.getHDOptions());
//                }
//            }
        }

//        car_num = (TextView) hotHeaderView.findViewById(R.id.car_num);
//        relativeBack.setOnClickListener(this);
//        relative_cart.setOnClickListener(this);

        LinearLayout linear_sort = (LinearLayout) hotHeaderView.findViewById(R.id.linear_sort);
        linear_sort.setVisibility(View.GONE);

//        recyclerView.addOnScrollListener(mOnScrollListener);

    }

    private void initText(final TextView textView) {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_hot_text, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "hot_text=" + msg);
                            if (object.optInt("status") == 1) {
                                JSONArray array = object.getJSONArray("msg");
                                JSONObject text = array.getJSONObject(text_position);
                                String string = text.optString("content");
                                textView.setText(string);

                            }
                        } catch (JSONException e) {
                            Log.i("zjz", "no_text");
                            textView.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("", this);
        }
        Log.i("zjz", "url_currentPage=" + currentPage);
        String param = null;
        param = TLUrl.getInstance().URL_hwg_special + "&special_id=" + special_id+ "&city_id=" + city_id;
        Log.i("zjz", "hot_special_url=" + TLUrl.getInstance().URL_hwg_special + "&special_id=" + special_id+ "&city_id=" + city_id);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "hot_special_msg=" + response);
                        JSONArray datas = response.getJSONArray("datas");
                        hotGoodsRecyclerAapter.getList().clear();
//                            recycleViewGridAdapter.getList().clear();
                        dataList.clear();
                        goodsIdList.clear();
                        int num = 0;
                        for (int i = 0; i < datas.length(); i++) {

                            JSONObject object = datas.getJSONObject(i);
                            JSONObject jsonObject1 = object.getJSONObject("goods");

                            JSONArray item = jsonObject1.getJSONArray("item");
                            for (int j = 0; j < item.length(); j++) {
                                Goods goods = new Goods();
                                JSONObject goodsObj = item.getJSONObject(j);
                                goods.setId(num);
                                num++;
                                goods.setGoods_id(goodsObj.optString("goods_id"));
                                goods.setTitle(goodsObj.optString("goods_name"));
                                if (goodsObj.has("zhuangtai")) {
                                    goods.setXiangou(1);
                                }
                                goods.setMoney(goodsObj.optDouble("goods_promotion_price"));
                                goods.setPicarr(goodsObj.optString("goods_image"));
                                goodsIdList.add(goodsObj.optString("goods_id"));
                                dataList.add(goods);
                            }

                        }
                        Log.i("zjz", "hot_special_size=" + dataList.size());
                        mCurrentCounter = dataList.size();
                        hotGoodsRecyclerAapter.addItems(dataList);
//                            recycleViewGridAdapter.addItems(dataList);
                        hotGoodsRecyclerAapter.notifyDataSetChanged();
//                            recycleViewGridAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.img_overlay:
                recyclerView.scrollToPosition(0);
                break;
            case R.id.relative_cart:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, CartActivity2.class);
                intent.putExtra("store_id","");
                startActivity(intent);
                break;
            case R.id.img_all_buy:
                buyAll();
                break;
        }
    }

    private void buyAll() {
        if (MyApplication.getInstance().getMykey() == null) {
            showToast("请登录");
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        } else if (goodsIdList.size() == 0) {
            showToast("没有可以购买的商品");
            return;
        }
        String param = null;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < goodsIdList.size(); i++) {
            buffer.append(goodsIdList.get(i) + "|1");
            if (i != goodsIdList.size() - 1) {
                buffer.append(",");
            }
        }
        param = buffer.toString();
        Log.i("zjz", "buy_all_param=" + param);
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        final String finalParam = param;
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_goods_buy_all_and_again, "&key=" + MyApplication.getInstance().getMykey() + "&goodsinfo=" + param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "buy_all_msg=" + msg);
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                if (object.optString("datas").equals("1")) {
                                    showToast("添加到购物车成功！");
                                    MyUpdateUI.sendUpdateCarNum(GaoErFuActivity.this);
                                    Intent intent = new Intent(GaoErFuActivity.this, CartActivity2.class);
                                    intent.putExtra("isCheck", true);
                                    intent.putExtra("store_id","");
                                    intent.putExtra("goods", finalParam);
                                    startActivity(intent);
                                } else if (object.getJSONObject("datas").has("error")) {
                                    showToast("该商品已经下架或库存为0！");
                                } else {
                                    JSONObject jsonObject = object.getJSONObject("datas");
                                    Iterator keys = jsonObject.keys();
                                    StringBuffer stringBuffer = new StringBuffer();
                                    while (keys.hasNext()) {
                                        String value = (String) keys.next();
                                        JSONObject goodsObject = jsonObject.getJSONObject(value);
                                        JSONObject goods = goodsObject.getJSONObject("kucun");
                                        if (goods.optString("goods_name").length() > 20) {
                                            stringBuffer.append("商品[" + goods.optString("goods_name").substring(0, 20) + "] ");
                                        } else {
                                            stringBuffer.append("商品[" + goods.optString("goods_name") + "] ");
                                        }

                                    }
                                    String text = stringBuffer + "库存为0或已经下架！";
                                    showToast(text);
                                    MyUpdateUI.sendUpdateCarNum(GaoErFuActivity.this);
                                    Intent intent = new Intent(GaoErFuActivity.this, CartActivity2.class);
                                    intent.putExtra("isCheck", true);
                                    intent.putExtra("store_id","");
                                    intent.putExtra("goods", finalParam);
                                    startActivity(intent);
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

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        initAllDates();
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        hotGoodsRecyclerAapter.addItems(list);
        mCurrentCounter += list.size();
    }

//    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
//
//        @Override
//        public void onLoadNextPage(View view) {
//            super.onLoadNextPage(view);
//
//            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
//            if (state == LoadingFooter.State.Loading) {
//                Log.d("@Cundong", "the state is Loading, just wait..");
//                return;
//            }
//
//            if (currentPage < totalPage) {
//                // loading more
//                RecyclerViewStateUtils.setFooterViewState(HotSpecialActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
//                requestData();
//            } else {
//                //the end
//                RecyclerViewStateUtils.setFooterViewState(HotSpecialActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
//            }
//        }
//    };

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", hotGoodsRecyclerAapter.getList().get(position - 1).getGoods_id());
        intent.putExtra("pic", hotGoodsRecyclerAapter.getList().get(position - 1).getPicarr());
        startActivity(intent);
    }

    private class PreviewHandler extends Handler {

        private WeakReference<GaoErFuActivity> ref;

        PreviewHandler(GaoErFuActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GaoErFuActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    initAllDates();
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.recyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(GaoErFuActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(GaoErFuActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
