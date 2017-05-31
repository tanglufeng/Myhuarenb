package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.HotGoodsRecyclerAapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.HotHeaderView;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
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

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * 这是momo二级分类调到的界面，为了保险起见做了一个备份
 *
 * */
public class HotActivity2 extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, HWGFragmentViewHolder.ItemOnClick {

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
    @InjectView(R.id.linear_sort)
    LinearLayout linearSort;
    @InjectView(R.id.img_overlay)
    ImageView imgOverlay;
    @InjectView(R.id.spinner_select)
    Spinner spinnerSelect;
    @InjectView(R.id.relative_spinner)
    RelativeLayout relativeSpinner;
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.img_cart)
    ImageView imgCart;


    private View view;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    HotGoodsRecyclerAapter hotGoodsRecyclerAapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    public Handler handler = new Handler();
    private RequestQueue mRequestQueue;
    private int currentPage = 1;
    private int count;
    private int totalPage;
    boolean isLoadMore = false;
    boolean first = false;
    boolean isSearch = false;
    boolean isStore = false;
    boolean isAd = false;
    private String type;
    private String keyword;
    private String picture;
    private String words = "";
    private static final int REQUEST_COUNT = 10;
    private int mCurrentCounter = 0;
    FullyGridLayoutManager fullyGridLayoutManager;

    HotHeaderView hotHeaderView;
    boolean isSelect;
    boolean isRemai;
    boolean isMain;
    boolean isSale;
    boolean isWeek;
    String special_id;
    //    public static TextView car_num;
    MyBroadCastReceiver myBroadCastReceiver;
    private boolean isSortUp;    //是否为价格升序排列
    private boolean isVolume;    //是否为销量升序排列
    private boolean isFilter;    //是否为人气升序排列
    int text_position;

    public static ImageView img_cart;
    private boolean isSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_hot, null);
        }

        words = getIntent().getStringExtra("words");
        picture = getIntent().getStringExtra("picture");
        text_position = getIntent().getIntExtra("text_position", 0);
        isRemai = getIntent().getBooleanExtra("isRemai", false);
        isMain = getIntent().getBooleanExtra("isMain", false);
        isSale = getIntent().getBooleanExtra("isSale", false);
        isWeek = getIntent().getBooleanExtra("isWeek", false);
        isSecond=    getIntent().getBooleanExtra("isSecond",false);
        img_cart= (ImageView) view.findViewById(R.id.img_cart);
        if (isRemai) {
            special_id = getIntent().getStringExtra("special_id");
        } else {
            try {
                keyword = URLEncoder.encode((String) getIntent().getSerializableExtra("keyword"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
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
        initSpinner();
        initInCartNum();
//        RelativeLayout relative_title = (RelativeLayout) hotHeaderView.findViewById(R.id.relative_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relative_title.getLayoutParams();
//            params.setMargins(0, getStatusBarHeight(), 0, 0);
//            relative_title.setLayoutParams(params);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relativeSpinner.getLayoutParams();
//            layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
//            relativeSpinner.setLayoutParams(layoutParams);
        }

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

    private void initSpinner() {
        companyAdapter = ArrayAdapter.createFromResource(this, R.array.hot_select, R.layout.hwg_spinner_item);
        companyAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerSelect.setAdapter(companyAdapter);
        spinnerSelect.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                isLoadMore = false;
                currentPage = 1;
                recyclerView.scrollToPosition(0);
                switch (position) {
                    case 0:
                        sortByDownNew();
                        break;
                    case 1:
                        sortUp(SORTUP_BRSALES);
                        break;
                    case 2:
                        sortDown(SORTDOWN_BRSALES);
                        break;
                    case 3:
                        sortUp(SORTUP_BRPRICE);
                        break;
                    case 4:
                        sortDown(SORTDOWN_BRPRICE);
                        break;
                    case 5:
                        sortUp(SORTUP_BRRENQI);
                        break;
                    case 6:
                        sortDown(SORTDOWN_BRRENQI);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setOnListener() {
        imgOverlay.setOnClickListener(this);
        linearSort.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeCart.setOnClickListener(this);
    }

    ArrayAdapter<CharSequence> companyAdapter = null;
    Spinner spinner;

    private void initRecyclerView() {
        hotGoodsRecyclerAapter = new HotGoodsRecyclerAapter(this, this);
//        recycleViewGridAdapter=new RecycleViewGridAdapter(this);
        initAllDates();
        hotGoodsRecyclerAapter.addHeadView(hotHeaderView);
        if (isSecond){
            hotHeaderView.findViewById(R.id.img_banner).setVisibility(View.GONE);
            hotHeaderView.findViewById(R.id.t_text).setVisibility(View.GONE);
        }

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
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, gridLayoutManager, true);
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
            Log.i("zjz", "hot有数据");
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
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            img_banner.setLayoutParams(layoutParams);
            img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        }
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(img_banner, picture);
        ImageLoader.getInstance().displayImage(picture, img_banner, Options.getHDOptions());
//        car_num = (TextView) hotHeaderView.findViewById(R.id.car_num);
//        relativeBack.setOnClickListener(this);
//        relative_cart.setOnClickListener(this);
        spinner = (Spinner) hotHeaderView.findViewById(R.id.spinner_select);

        companyAdapter = ArrayAdapter.createFromResource(this, R.array.hot_select, R.layout.hwg_spinner_item);
        companyAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinner.setAdapter(companyAdapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isLoadMore = false;
                currentPage = 1;
                spinnerSelect.setSelection(position);
                switch (position) {
                    case 0:
                        sortByDownNew();
                        break;
                    case 1:
                        sortUp(SORTUP_BRSALES);
                        break;
                    case 2:
                        sortDown(SORTDOWN_BRSALES);
                        break;
                    case 3:
                        sortUp(SORTUP_BRPRICE);
                        break;
                    case 4:
                        sortDown(SORTDOWN_BRPRICE);
                        break;
                    case 5:
                        sortUp(SORTUP_BRRENQI);
                        break;
                    case 6:
                        sortDown(SORTDOWN_BRRENQI);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.addOnScrollListener(mOnScrollListener);

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
        if (isRemai) {
            param = TLUrl.getInstance().URL_hwg_special + "&special_id=" + special_id;
        } else {
            param = TLUrl.getInstance().URL_hwg_goods_search + "&curpage=" + currentPage + "&keyword=" + keyword;
        }
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "hot_act:连接成功");
                        type = NOMAL;
                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = hotGoodsRecyclerAapter.getItemCount();
//                            int currentSize = recycleViewGridAdapter.getItemCount();
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
                            hotGoodsRecyclerAapter.getList().clear();
//                            recycleViewGridAdapter.getList().clear();
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
                            hotGoodsRecyclerAapter.addItems(dataList);
//                            recycleViewGridAdapter.addItems(dataList);
                            hotGoodsRecyclerAapter.notifyDataSetChanged();
//                            recycleViewGridAdapter.notifyDataSetChanged();
                        }
//                        swipeRefreshLayout.setVisibility(allGoodsRecyclerAapter.getList().size() == 0 ? View.GONE : View.VISIBLE);
//                        allGoodsRecyclerAdapter.notifyDataSetChanged();
//                        ProgressDlgUtil.stopProgressDlg();
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
                param = TLUrl.getInstance().URL_hwg_sort_byprice_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTUP_BRPRICE;
                break;
            case SORTUP_BRSALES:
                param = TLUrl.getInstance().URL_hwg_sort_bysale_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTUP_BRSALES;
                break;
            case SORTUP_BRRENQI:
                param = TLUrl.getInstance().URL_hwg_sort_bylike_up + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTUP_BRRENQI;
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
                            int currentSize = hotGoodsRecyclerAapter.getItemCount();
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
                            hotGoodsRecyclerAapter.getList().clear();
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
                            hotGoodsRecyclerAapter.addItems(dataList);
                            hotGoodsRecyclerAapter.notifyDataSetChanged();

                        }
//                        swipeRefreshLayout.setVisibility(hotGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
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
                param = TLUrl.getInstance().URL_hwg_sort_byprice_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTDOWN_BRPRICE;
                break;
            case SORTDOWN_BRSALES:
                param = TLUrl.getInstance().URL_hwg_sort_bysale_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTDOWN_BRSALES;
                break;
            case SORTDOWN_BRRENQI:
                param = TLUrl.getInstance().URL_hwg_sort_bylike_down + "&curpage=" + currentPage + "&keyword=" + keyword;
                type = SORTDOWN_BRRENQI;
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
                            int currentSize = hotGoodsRecyclerAapter.getItemCount();
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
                            hotGoodsRecyclerAapter.getList().clear();
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
                            hotGoodsRecyclerAapter.addItems(dataList);
                            hotGoodsRecyclerAapter.notifyDataSetChanged();

                        }
//                        swipeRefreshLayout.setVisibility(hotGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
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
//        ProgressDlgUtil.showProgressDlg("", this);
        final ArrayList<Goods> dataList = new ArrayList<>();
        String param = null;
        param = TLUrl.getInstance().URL_hwg_sort_bynew_down + "&curpage=" + currentPage + "&keyword=" + keyword;
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
                            int currentSize = hotGoodsRecyclerAapter.getItemCount();
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
                            hotGoodsRecyclerAapter.getList().clear();
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
                            hotGoodsRecyclerAapter.addItems(dataList);
                            hotGoodsRecyclerAapter.notifyDataSetChanged();

                        }
//                        swipeRefreshLayout.setVisibility(hotGoodsRecyclerAapter.getList().size()==0?View.GONE:View.VISIBLE);
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
        }
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

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        hotGoodsRecyclerAapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(HotActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(HotActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", hotGoodsRecyclerAapter.getList().get(position - 1).getGoods_id());
        intent.putExtra("pic", hotGoodsRecyclerAapter.getList().get(position - 1).getPicarr());
        startActivity(intent);
    }


    private class PreviewHandler extends Handler {

        private WeakReference<HotActivity2> ref;

        PreviewHandler(HotActivity2 activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final HotActivity2 activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(HotActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(HotActivity2.this)) {
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
