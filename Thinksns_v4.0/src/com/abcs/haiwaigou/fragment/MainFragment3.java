package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.adapter.HotGoodsRecyclerAapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.HotHeaderView;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
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

/**
 * Created by zjz on 2016/6/8 0008.
 * 海外购的其他fragment
 */
public class MainFragment3 extends BaseFragment implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, HWGFragmentViewHolder.ItemOnClick {

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
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;

    Activity activity;

    HotHeaderView hotHeaderView;
    private View view;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    HotGoodsRecyclerAapter hotGoodsRecyclerAapter;
    private PreviewHandler mHandler = new PreviewHandler(activity);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private RequestQueue mRequestQueue;
    private int currentPage = 1;
    boolean first = false;

    private static final int REQUEST_COUNT = 10;
    private int mCurrentCounter = 0;
    FullyGridLayoutManager fullyGridLayoutManager;


    boolean isMain;
    boolean isSale;
    boolean isWeek;
    String special_id,imgUrl,des;
    MyBroadCastReceiver myBroadCastReceiver;
    int text_position;

    private ArrayList<String> goodsIdList = new ArrayList<String>();


    private String objectName;
    private ACache aCache;


    int picWith;
    int picHeight;
    int picHeight2;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;




    public static MainFragment3 newInstance(String plateId, String objectName, String imgUrl, String des) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("plate_id", plateId);
        bundle.putSerializable("objectName", objectName);
        bundle.putSerializable("imgUrl", imgUrl);
        bundle.putSerializable("des", des);
        MainFragment3 fragment = new MainFragment3();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();
        aCache = ACache.get(activity);
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_activity_hotspecial2, null);
            ButterKnife.inject(this, view);
            Bundle bundle = getArguments();
            if (bundle != null) {
                special_id = bundle.getString("plate_id");
                objectName = bundle.getString("objectName");
                imgUrl = bundle.getString("imgUrl");
                des = bundle.getString("des");

            }
            picWith=Util.WIDTH;
            picHeight=picWith*33/72;
            picHeight2=picWith*39/72;
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        ButterKnife.inject(this, view);

        hotHeaderView = new HotHeaderView(activity);

        mRequestQueue = Volley.newRequestQueue(activity);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        setOnListener();
        initRecyclerView();
        initInCartNum();

        return view;
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
            new InitCarNum(carNum, activity,"");
        }
    }


    private void setOnListener() {
        imgOverlay.setOnClickListener(this);
        imgAllBuy.setOnClickListener(this);
        relativeCart.setOnClickListener(this);
    }

    ArrayAdapter<CharSequence> companyAdapter = null;

    private void initRecyclerView() {
        hotGoodsRecyclerAapter = new HotGoodsRecyclerAapter(activity, this);
//        recycleViewGridAdapter=new RecycleViewGridAdapter(activity);
        initAllDates();
        hotGoodsRecyclerAapter.addHeadView(hotHeaderView);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
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
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, gridLayoutManager, false);

        //        RecyclerViewUtils.setHeaderView(recyclerView, hotHeaderView);
        RelativeLayout relativeBack = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_back);
        RelativeLayout relative_cart = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_cart);
        TextView t_text = (TextView) hotHeaderView.findViewById(R.id.t_text);
//        Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/fangzhenglantinghei.TTF");
//        t_text.setTypeface(fontFace);
        Util.setFZLTHFont(t_text);
        if (des.length() == 0 || des.equals("null")) {
            t_text.setVisibility(View.GONE);
//            initText(t_text);
        } else {
            t_text.setVisibility(View.VISIBLE);
            Log.i("zjz", "hotspecial有数据");
            t_text.setText(Util.ReplaceSpecialSymbols(des));
        }
        ImageView img_banner = (ImageView) hotHeaderView.findViewById(R.id.img_banner);
        if (isMain) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 245));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (isSale) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 195));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (isWeek) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 165));
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        }
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(img_banner, picture);
        if(!TextUtils.isEmpty(imgUrl)){
            ImageLoader.getInstance().displayImage(imgUrl, img_banner, Options.getHDOptions());
        }else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_huaqiao_default, img_banner, Options.getHDOptions());
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
            ProgressDlgUtil.showProgressDlg("", activity);
        }
        Log.i("zjz", "url_currentPage=" + currentPage);
        String param = null;
        param = TLUrl.getInstance().URL_hwg_special + "&special_id=" + special_id;
        Log.i("zjz", "hot_special_url=" + TLUrl.getInstance().URL_hwg_special + "&special_id=" + special_id);
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

//                                private double money;  /****促销价*****/
//                                private double goods_markeprice; /****市场价*****/
//                                private double promote_money; /****原价*****/

                                goods.setMoney(goodsObj.optDouble("goods_promotion_price"));
                                goods.setGoods_markeprice(goodsObj.optDouble("goods_marketprice"));
                                goods.setPromote_money(goodsObj.optDouble("goods_price"));
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

    public Handler handler = new Handler();
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_overlay:
                recyclerView.scrollToPosition(0);
                break;
            case R.id.relative_cart:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
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
            showToast(activity,"请登录");
            Intent intent = new Intent(activity, WXEntryActivity.class);
            startActivity(intent);
            return;
        } else if (goodsIdList.size() == 0) {
            showToast(activity,"没有可以购买的商品");
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
        ProgressDlgUtil.showProgressDlg("Loading...", activity);

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
                                    showToast(activity,"添加到购物车成功！");
                                    MyUpdateUI.sendUpdateCarNum(activity);
                                    Intent intent = new Intent(activity, CartActivity2.class);
                                    intent.putExtra("store_id","");
                                    intent.putExtra("isCheck", true);
                                    intent.putExtra("goods", finalParam);
                                    startActivity(intent);
                                } else if (object.getJSONObject("datas").has("error")) {
                                    showToast(activity,"该商品已经下架或库存为0！");
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
                                    showToast(activity,text);
                                    MyUpdateUI.sendUpdateCarNum(activity);
                                    Intent intent = new Intent(activity, CartActivity2.class);
                                    intent.putExtra("isCheck", true);
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


    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(activity, GoodsDetailActivity.class);
        intent.putExtra("sid", hotGoodsRecyclerAapter.getList().get(position - 1).getGoods_id());
        intent.putExtra("pic", hotGoodsRecyclerAapter.getList().get(position - 1).getPicarr());
        startActivity(intent);
    }

    @Override
    protected void lazyLoad() {

    }

    private class PreviewHandler extends Handler {

        private WeakReference<Activity> ref;

        PreviewHandler(Activity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    initAllDates();
                    break;
                case -2:
                    notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(activity)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(activity);
        myBroadCastReceiver.unRegister();
    }
}
