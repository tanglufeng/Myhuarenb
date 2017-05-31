package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.AllGoodsAdapter1;
import com.abcs.haiwaigou.adapter.AllGoodsRecyclerAapter;
import com.abcs.haiwaigou.adapter.viewholder.AllGoodsRecyclerViewHolder;
import com.abcs.haiwaigou.fragment.FilterMenuFragment;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllGoodsActivity extends BaseFragmentActivity implements View.OnClickListener,
        RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, AllGoodsRecyclerViewHolder.ItemOnClick {

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;
    @InjectView(R.id.textView2)
    EditText textView2;
    @InjectView(R.id.layout_search_bar)
    RelativeLayout layoutSearchBar;
    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.img_search)
    ImageView imgSearch;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.tv_global)
    TextView tvGlobal;
    @InjectView(R.id.btn_global)
    RelativeLayout btnGlobal;
    @InjectView(R.id.tv_salse_volume)
    TextView tvSalseVolume;
    @InjectView(R.id.img_volume)
    ImageView imgVolume;
    @InjectView(R.id.btn_salse_volume)
    RelativeLayout btnSalseVolume;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    @InjectView(R.id.img_price)
    ImageView imgPrice;
    @InjectView(R.id.btn_price)
    RelativeLayout btnPrice;
    @InjectView(R.id.tv_filter)
    TextView tvFilter;
    @InjectView(R.id.img_filter)
    ImageView imgFilter;
    @InjectView(R.id.btn_filter)
    RelativeLayout btnFilter;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.img_overlay)
    ImageView imgOverlay;
    @InjectView(R.id.tv_global_item_new)
    TextView tvGlobalItemNew;
    @InjectView(R.id.tv_global_item_comments)
    TextView tvGlobalItemComments;
    @InjectView(R.id.layout_global_menu_items)
    LinearLayout layoutGlobalMenuItems;
    @InjectView(R.id.layout_global_menu)
    RelativeLayout layoutGlobalMenu;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.tv_null2)
    TextView tvNull2;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    @InjectView(R.id.linear_type)
    LinearLayout linearType;
    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_no)
    RelativeLayout relativeNo;

    /**
     * 已经获取到多少条数据了
     */
    private int mCurrentCounter = 0;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    public final static String SORTUP_BRPRICE = "sort_up_by_price";
    public final static String SORTDOWN_BRPRICE = "sort_down_by_price";
    public final static String SORTDOWN_BRSALES = "sort_down_by_sales";
    public final static String SORTUP_BRSALES = "sort_up_by_sales";
    public final static String SORTDOWN_BRNEW = "sort_down_by_new";
    public final static String SORTDOWN_BRRENQI = "sort_down_by_renqi";
    public final static String SORTUP_BRRENQI = "sort_up_by_renqi";
    public final static String NOMAL = "sort_by_nomal";

    private boolean isGlobalMenuShow;
    private int mLastFirstPosition;

    private AllGoodsAdapter1 goodsAdapter;
    public Handler handler = new Handler();
    private int brandid;

    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private MyGridView mGridView;

    private boolean isGrid; // 是否为Grid列表
    private boolean isSortUp;    //是否为价格升序排列
    private boolean isVolume;    //是否为销量升序排列
    private boolean isFilter;    //是否为人气升序排列
    private int menuSize;

    private FilterMenuFragment filterMenuFragment;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    private View view;
    private String gc_id;
    private String store_id;

    private RequestQueue mRequestQueue;
    private int currentPage = 1;
    private int count;
    private int totalPage;
    boolean isLoadMore = false;
    boolean isSearch = false;
    boolean isStore = false;
    boolean isAd = false;
    boolean isWeek = false;
    boolean isUpdate = false;
    boolean first = false;
    private String type = NOMAL;
    private String keyword;
    private String title;

    boolean isResearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gc_id = (String) getIntent().getSerializableExtra("gc_id");
        isSearch = getIntent().getBooleanExtra("search", false);
        isStore = getIntent().getBooleanExtra("store", false);
        isAd = getIntent().getBooleanExtra("ad", false);
        isWeek = getIntent().getBooleanExtra("isWeek", false);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);

        Log.i("zjz", "isSearch=" + isSearch);
        mRequestQueue = Volley.newRequestQueue(this);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        initMenu();
        initView();
        setOnListener();
//        initListView();
//        initGridView();

        if (isAd) {
            try {
                keyword = URLEncoder.encode((String) getIntent().getSerializableExtra("keyword"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            initRecyclerView();
        } else if (!isSearch) {
            title = getIntent().getStringExtra("title");
            tljrTxtNewsTitle.setText(title);
            initRecyclerView();
        }
        tvGlobal.setTextColor(Color.RED);
//        initTest();
    }

    private void initMenu() {
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_allgoods_recycler, null);
        }
        Intent intent = getIntent();
//        mDrawer = MenuDrawer.attach(AllGoodsActivity.this, MenuDrawer.Type.OVERLAY, Position.END);
//        mDrawer.setMenuView(R.layout.hwg_menudrawer);
//        mDrawer.setContentView(R.layout.hwg_activity_all_goods);
        setContentView(view);
        ButterKnife.inject(this);
        // 菜单无阴影
//        mDrawer.setDropShadowEnabled(false);
        filterMenuFragment = new FilterMenuFragment();
//        filterMenuFragment.setCategoryItem(categoryItem);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.menu_container, filterMenuFragment).commit();
    }

    AllGoodsRecyclerAapter allGoodsRecyclerAapter;
    FullyGridLayoutManager fullyGridLayoutManager;

    private void initRecyclerView() {
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(mOnScrollListener);
//        fullyGridLayoutManager = new FullyGridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(fullyGridLayoutManager);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size4);
//        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        allGoodsRecyclerAapter = new AllGoodsRecyclerAapter(this, this);
        initAllDates();
//        initTestAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(allGoodsRecyclerAapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, false);

    }


    /**
     * 设置菜单宽度
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 若不将menuSize设为成员变量，在从第二层菜单返回时，会造成菜单消失
        if (menuSize == 0) {
            int screenWidth = Util.getScreenWidth(this);
            menuSize = screenWidth / 7 * 6;
//            mDrawer.setMenuSize(menuSize);
        }
    }


    private void setOnListener() {
        btnGlobal.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        btnSalseVolume.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeSearch.setOnClickListener(this);
        layoutGlobalMenu.setOnClickListener(this);
        imgOverlay.setOnClickListener(this);
        layoutSearchBar.setOnClickListener(this);
        textView2.setOnClickListener(this);
    }

    private void initView() {
        if (isStore) {
            textView2.setFocusable(false);
            relativeSearch.setVisibility(View.GONE);
            layoutSearchBar.setVisibility(View.GONE);
            tljrTxtNewsTitle.setVisibility(View.VISIBLE);
            seperate.setVisibility(View.VISIBLE);
            linearType.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            tljrTxtNewsTitle.setText(getIntent().getStringExtra("store_name"));
            store_id = getIntent().getStringExtra("store_id");
        } else if (isSearch) {
            textView2.setFocusable(false);
            if (!isResearch)
                textView2.setText(getIntent().getStringExtra("search_keyword"));
            imgSearch.setVisibility(View.VISIBLE);
            seperate.setVisibility(View.INVISIBLE);
            layoutSearchBar.setVisibility(View.VISIBLE);
            tljrTxtNewsTitle.setVisibility(View.GONE);
            linearType.setVisibility(View.GONE);
            layoutNull.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
            goodsSearch();
        } else {
            textView2.setFocusable(false);
            relativeSearch.setVisibility(View.GONE);
            layoutSearchBar.setVisibility(View.GONE);
            tljrTxtNewsTitle.setVisibility(View.VISIBLE);
            seperate.setVisibility(View.VISIBLE);
            linearType.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }


    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        final ArrayList<Goods> dataList = new ArrayList<>();
        Log.i("zjz", "url_currentPage=" + currentPage);
        String param = null;
        if (isStore) {
            param = TLUrl.getInstance().URL_hwg_store_goods + "&key=4&page=10&curpage=" + currentPage + "&store_id=" + store_id;
        } else if (isSearch || isAd) {
            Log.i("zjz", "keyword=" + keyword);
            param = TLUrl.getInstance().URL_hwg_goods_search + "&curpage=" + currentPage + "&keyword=" + keyword;
        } else if (isWeek) {
            param = TLUrl.getInstance().URL_hwg_week + currentPage;
        } else if (isUpdate) {
            param = TLUrl.getInstance().URL_hwg_update + currentPage;
        } else {
            param = TLUrl.getInstance().URL_hwg_home_all_goods + "&key=4&page=10&curpage=" + currentPage + "&gc_id=" + gc_id;
        }
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        Log.i("zjz", "response=" + response);
                        type = NOMAL;
                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONObject goodsCount = jsonObject.optJSONObject("countgoods");
                        if (goodsCount != null && tljrTxtNewsTitle != null) {
                            tljrTxtNewsTitle.setText(title + "(" + goodsCount.optString("count(goods_id)") + ")");
                        }
                        if (!first) {
                            if (relativeNo != null)
                                if (jsonObject.optString("goods_no").equals("1"))
                                    relativeNo.setVisibility(View.VISIBLE);
                                else
                                    relativeNo.setVisibility(View.GONE);
                        }
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = allGoodsRecyclerAapter.getItemCount();
                            Log.i("zjz", "currentsize=" + currentSize);
                            //模拟组装10个数据

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                if (object1.has("xianshi_flag") && object1.optBoolean("xianshi_flag")) {
                                    g.setXiangou(1);
                                }
                                dataList.add(g);
                            }
                            addItems(dataList);
                        } else {
                            allGoodsRecyclerAapter.getList().clear();
                            dataList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                if (object1.has("xianshi_flag") && object1.optBoolean("xianshi_flag")) {
                                    g.setXiangou(1);
                                }
                                dataList.add(g);
                            }
                            mCurrentCounter = dataList.size();
                            allGoodsRecyclerAapter.addItems(dataList);
                            allGoodsRecyclerAapter.notifyDataSetChanged();
                        }
                        if (layoutNull != null)
                            layoutNull.setVisibility(dataList.size() == 0 ? View.VISIBLE : View.GONE);
//                        swipeRefreshLayout.setVisibility(allGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
//                        allGoodsRecyclerAdapter.notifyDataSetChanged();
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    ProgressDlgUtil.stopProgressDlg();
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
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        count = 0;
        currentPage = 1;
        Log.i("zjz", "type=" + type);
        switch (type) {
            case NOMAL:
                initAllDates();
//                initTestAllDates();
                break;
            case SORTDOWN_BRNEW:
                sortByDownNew();
                break;
            case SORTDOWN_BRPRICE:
                sortDown(SORTDOWN_BRPRICE);
                break;
            case SORTUP_BRPRICE:
                sortUp(SORTUP_BRPRICE);
                break;
            case SORTDOWN_BRRENQI:
                sortDown(SORTDOWN_BRRENQI);
                break;
            case SORTUP_BRRENQI:
                sortUp(SORTUP_BRRENQI);
                break;
            case SORTDOWN_BRSALES:
                sortDown(SORTDOWN_BRSALES);
                break;
            case SORTUP_BRSALES:
                sortUp(SORTUP_BRSALES);
                break;
        }
    }


    /**
     * 人气排序
     */
    private void sortByRenQi() {
        isLoadMore = false;
        currentPage = 1;
        isVolume = false;
        isSortUp = false;
        isFilter = !isFilter;
        if (isFilter) {
            sortUp(SORTUP_BRRENQI);
        } else {
            sortDown(SORTDOWN_BRRENQI);
        }
    }

    /**
     * 价格排序
     */
    private void sortByPrice() {
        isLoadMore = false;
        currentPage = 1;
        isFilter = false;
        isVolume = false;
        isSortUp = !isSortUp;
        if (isSortUp) {
            sortUp(SORTUP_BRPRICE);
        } else {
            sortDown(SORTDOWN_BRPRICE);
        }
    }

    /**
     * 销量排序
     */
    private void sortByVolume() {
        isLoadMore = false;
        currentPage = 1;
        isSortUp = false;
        isFilter = false;
        isVolume = !isVolume;
        if (isVolume) {
            sortUp(SORTUP_BRSALES);
        } else {
            sortDown(SORTDOWN_BRSALES);
        }
    }

    /**
     * 升序
     */
    private void sortUp(String types) {
        int darkgray = getResources().getColor(R.color.darkgray);
        String param = null;
        switch (types) {
            case SORTUP_BRPRICE:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_byprice_up + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_byprice_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_byprice_up + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_byprice_up + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_byprice_up + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTUP_BRPRICE;
                tvGlobal.setTextColor(darkgray);
                tvPrice.setTextColor(Color.RED);
                imgPrice.setImageResource(R.drawable.sort_button_price_up);
                tvSalseVolume.setTextColor(darkgray);
                imgVolume.setImageResource(R.drawable.sort_button_price);
                tvFilter.setTextColor(darkgray);
                imgFilter.setImageResource(R.drawable.sort_button_price);
                break;
            case SORTUP_BRSALES:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_bysale_up + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_bysale_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_bysale_up + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_bysale_up + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_bysale_up + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTUP_BRSALES;
                tvGlobal.setTextColor(darkgray);
                tvPrice.setTextColor(darkgray);
                imgPrice.setImageResource(R.drawable.sort_button_price);
                tvSalseVolume.setTextColor(Color.RED);
                imgVolume.setImageResource(R.drawable.sort_button_price_up);
                tvFilter.setTextColor(darkgray);
                imgFilter.setImageResource(R.drawable.sort_button_price);
                break;
            case SORTUP_BRRENQI:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_bylike_up + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_bylike_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_bylike_up + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_bylike_up + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_bylike_up + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTUP_BRRENQI;
                tvGlobal.setTextColor(darkgray);
                tvPrice.setTextColor(darkgray);
                imgPrice.setImageResource(R.drawable.sort_button_price);
                tvSalseVolume.setTextColor(darkgray);
                imgVolume.setImageResource(R.drawable.sort_button_price);
                tvFilter.setTextColor(Color.RED);
                imgFilter.setImageResource(R.drawable.sort_button_price_up);
                break;
        }

//        ProgressDlgUtil.showProgressDlg("", this);
        final ArrayList<Goods> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = allGoodsRecyclerAapter.getItemCount();
                            Log.i("zjz", "currentsize=" + currentSize);
                            //模拟组装10个数据

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            addItems(dataList);
                        } else {
                            allGoodsRecyclerAapter.getList().clear();
                            dataList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            mCurrentCounter = dataList.size();
                            allGoodsRecyclerAapter.addItems(dataList);
                            allGoodsRecyclerAapter.notifyDataSetChanged();
                        }
                        if (layoutNull != null)
                            layoutNull.setVisibility(dataList.size() == 0 ? View.VISIBLE : View.GONE);
//                        swipeRefreshLayout.setVisibility(allGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
//                        allGoodsRecyclerAapter.notifyDataSetChanged();
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);

    }

    /**
     * 降序
     */
    private void sortDown(String types) {
        String param = null;
        switch (types) {
            case SORTDOWN_BRPRICE:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_byprice_down + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_byprice_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_byprice_down + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_byprice_down + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_byprice_down + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTDOWN_BRPRICE;
                imgPrice.setImageResource(R.drawable.sort_button_price_down);
                break;
            case SORTDOWN_BRSALES:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_bysale_down + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_bysale_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_bysale_down + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_bysale_down + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_bysale_down + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTDOWN_BRSALES;
                imgVolume.setImageResource(R.drawable.sort_button_price_down);
                break;
            case SORTDOWN_BRRENQI:
                if (isStore) {
                    param = TLUrl.getInstance().URL_hwg_store_bylike_down + "&curpage=" + currentPage + "&store_id=" + store_id;
                } else if (isSearch || isAd) {
                    param = TLUrl.getInstance().URL_hwg_sort_bylike_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                } else if (isWeek) {
                    param = TLUrl.getInstance().URL_hwg_week_bylike_down + currentPage;
                } else if (isUpdate) {
                    param = TLUrl.getInstance().URL_hwg_update_bylike_down + currentPage;
                } else {
                    param = TLUrl.getInstance().URL_hwg_sort_bylike_down + "&curpage=" + currentPage + "&gc_id=" + gc_id;
                }
                type = SORTDOWN_BRRENQI;
                imgFilter.setImageResource(R.drawable.sort_button_price_down);
                break;
        }
//        ProgressDlgUtil.showProgressDlg("", this);
        final ArrayList<Goods> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = allGoodsRecyclerAapter.getItemCount();
                            Log.i("zjz", "currentsize=" + currentSize);
                            //模拟组装10个数据

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            addItems(dataList);
                        } else {
                            allGoodsRecyclerAapter.getList().clear();
                            dataList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            mCurrentCounter = dataList.size();
                            allGoodsRecyclerAapter.addItems(dataList);
                            allGoodsRecyclerAapter.notifyDataSetChanged();
                        }
                        if (layoutNull != null)
                            layoutNull.setVisibility(dataList.size() == 0 ? View.VISIBLE : View.GONE);

//                        swipeRefreshLayout.setVisibility(allGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
//                        allGoodsRecyclerAapter.notifyDataSetChanged();
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
    }


    private void sortByDownNew() {
        type = SORTDOWN_BRNEW;
        int darkgray = getResources().getColor(R.color.darkgray);
        tvGlobal.setTextColor(Color.RED);
        tvPrice.setTextColor(darkgray);
        imgPrice.setImageResource(R.drawable.sort_button_price);
        tvSalseVolume.setTextColor(darkgray);
        imgVolume.setImageResource(R.drawable.sort_button_price);
        tvFilter.setTextColor(darkgray);
        imgFilter.setImageResource(R.drawable.sort_button_price);
//        ProgressDlgUtil.showProgressDlg("", this);
        final ArrayList<Goods> dataList = new ArrayList<>();
        String param = null;
        if (isStore) {
            param = TLUrl.getInstance().URL_hwg_store_bynew_down + "&curpage=" + currentPage + "&store_id=" + store_id;
        } else if (isSearch || isAd) {
            param = TLUrl.getInstance().URL_hwg_sort_bynew_down + "&curpage=" + currentPage + "&keyword=" + keyword;
        } else if (isWeek) {
            param = TLUrl.getInstance().URL_hwg_week_bynew_down + currentPage;
        } else if (isUpdate) {
            param = TLUrl.getInstance().URL_hwg_update_bynew_down + currentPage;
        } else {
            param = TLUrl.getInstance().URL_hwg_sort_bynew_down + "&curpage=" + currentPage + "&gc_id=" + gc_id;
        }
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");

                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = allGoodsRecyclerAapter.getItemCount();
                            Log.i("zjz", "currentsize=" + currentSize);
                            //模拟组装10个数据

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            addItems(dataList);
                        } else {
                            allGoodsRecyclerAapter.getList().clear();
                            dataList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
                            mCurrentCounter = dataList.size();
                            allGoodsRecyclerAapter.addItems(dataList);
                            allGoodsRecyclerAapter.notifyDataSetChanged();
                        }
                        if (layoutNull != null)
                            layoutNull.setVisibility(dataList.size() == 0 ? View.VISIBLE : View.GONE);

//                        swipeRefreshLayout.setVisibility(allGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
//                        allGoodsRecyclerAapter.notifyDataSetChanged();
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
    }


    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:

                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.btn_global: // 新品
                first = true;
                recyclerView.scrollToPosition(0);
                sortByDownNew();
                break;
            case R.id.layout_global_menu: // 综合下拉菜单
//                showGlobalMenu();
                break;
            case R.id.img_overlay:
                recyclerView.scrollToPosition(0);
//                mListView.setSelection(0);
//                mGridView.setSelection(0);
                break;
            case R.id.btn_filter: //人气排序
                first = true;
                recyclerView.scrollToPosition(0);
                sortByRenQi();
                break;
            case R.id.btn_price: // 价格排序
                first = true;
                recyclerView.scrollToPosition(0);
                sortByPrice();
                break;
            case R.id.btn_salse_volume: // 销量排序
                first = true;
                recyclerView.scrollToPosition(0);
                sortByVolume();
                break;
            case R.id.overlayHeader:
                break;
            case R.id.relative_search:
//                if (textView2.getText().toString().equals("")) {
//                    showToast("请输入商品信息");
//                } else {
//                    goodsSearch();
//                }
//
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                intent = new Intent(this, GoodsSearchActivity2.class);
                intent.putExtra("reSearch", true);
                intent.putExtra("text", "");
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_search_bar:
                intent = new Intent(this, GoodsSearchActivity2.class);
                intent.putExtra("reSearch", true);
                intent.putExtra("text", textView2.getText().toString());
                startActivityForResult(intent, 1);
//                startActivity(intent);
                break;
            case R.id.textView2:
                intent = new Intent(this, GoodsSearchActivity2.class);
                intent.putExtra("text", textView2.getText().toString());
                intent.putExtra("reSearch", true);
                startActivityForResult(intent, 1);
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 1) {
            isSearch = data.getBooleanExtra("search", false);
            textView2.setText(data.getStringExtra("search_keyword"));
            isResearch = data.getBooleanExtra("research", false);
            isLoadMore = false;
            initMenu();
            initView();
            setOnListener();
        }
    }

    private void goodsSearch() {
        int darkgray = getResources().getColor(R.color.darkgray);
        tvGlobal.setTextColor(darkgray);
        tvPrice.setTextColor(darkgray);
        imgPrice.setImageResource(R.drawable.sort_button_price);
        tvSalseVolume.setTextColor(darkgray);
        imgVolume.setImageResource(R.drawable.sort_button_price);
        tvFilter.setTextColor(darkgray);
        imgFilter.setImageResource(R.drawable.sort_button_price);
        seperate.setVisibility(View.VISIBLE);
        linearType.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        try {
            keyword = URLEncoder.encode(textView2.getText().toString(), "utf-8");
            Log.i("zjz", "keyword=" + keyword);
//            keyword = URLEncoder.encode(getIntent().getStringExtra("search_keyword"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initRecyclerView();

    }

    /**
     * 点击返回时，先关闭菜单
     */
    @Override
    public void onBackPressed() {
        // TODO
        // 获取当前栈中的片段数
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count == 0) {
//            final int drawerState = mDrawer.getDrawerState();
//            if (drawerState == MenuDrawer.STATE_OPEN
//                    || drawerState == MenuDrawer.STATE_OPENING) {
//                mDrawer.closeMenu();
//                return;
//            }
        }
        super.onBackPressed();
    }


    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", allGoodsRecyclerAapter.getList().get(position).getGoods_id());
        intent.putExtra("pic", allGoodsRecyclerAapter.getList().get(position).getPicarr());
        startActivity(intent);
    }


    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        allGoodsRecyclerAapter.addItems(list);
        mCurrentCounter += list.size();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (currentPage < totalPage) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(AllGoodsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(AllGoodsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<AllGoodsActivity> ref;

        PreviewHandler(AllGoodsActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AllGoodsActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:

                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (currentPage < totalPage && isLoadMore) {
                        currentPage += 1;

                        Log.i("zjz", "current=" + currentPage);
                        RecyclerViewStateUtils.setFooterViewState(activity.recyclerView, LoadingFooter.State.Normal);
                        switch (type) {
                            case NOMAL:
                                initAllDates();
//                                initTestAllDates();
                                break;
                            case SORTDOWN_BRNEW:
                                sortByDownNew();
                                break;
                            case SORTDOWN_BRPRICE:
                                sortDown(SORTDOWN_BRPRICE);
                                break;
                            case SORTUP_BRPRICE:
                                sortUp(SORTUP_BRPRICE);
                                break;
                            case SORTDOWN_BRRENQI:
                                sortDown(SORTDOWN_BRRENQI);
                                break;
                            case SORTUP_BRRENQI:
                                sortUp(SORTUP_BRRENQI);
                                break;
                            case SORTDOWN_BRSALES:
                                sortDown(SORTDOWN_BRSALES);
                                break;
                            case SORTUP_BRSALES:
                                sortUp(SORTUP_BRSALES);
                                break;
                        }
                        return;
                    }


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
            RecyclerViewStateUtils.setFooterViewState(AllGoodsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(AllGoodsActivity.this)) {
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
        super.onDestroy();
    }
}
