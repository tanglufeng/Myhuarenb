package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.AllGoodsAdapter;
import com.abcs.haiwaigou.fragment.FilterMenuFragment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.haiwaigou.view.MyListView;
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
import com.nineoldandroids.animation.ObjectAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllGoodsActivity2 extends BaseFragmentActivity implements View.OnClickListener{


//    private MenuDrawer mDrawer;
    private boolean isGlobalMenuShow;
    private View mLayoutGlobalMenu;
    private ImageView mImgOverlay;
    private MyListView mListView;
//    private ZrcListView mListView
    private View mOverlayHeader;
    private int mLastFirstPosition;
    private ImageView mImgGlobal;
    private ImageView mImgGlobalList;

    public ArrayList<Goods>goodsList=new ArrayList<Goods>();
    private AllGoodsAdapter goodsAdapter;
    public Handler handler = new Handler();
    private int brandid;

    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    // private int gridCode = -1;
    // private int listCode = -1;
    private MyGridView mGridView;
//    private GoodsGridAdapter mGridAdapter;
    private ImageView mImgMenu;
    private ImageView mImgMenuList;

    private boolean isGrid; // 是否为Grid列表
    private boolean isSortUp;	//是否为价格升序排列
    private ImageView mImgMenuGrid;
    private ImageView mImgGlobalGrid;
    private int menuSize;

    private FilterMenuFragment filterMenuFragment;
    private TextView mTvSaleVolume;
    private TextView mTvSaleVolumeList;
    private TextView mTvSaleVolumeGrid;
    private TextView mTvPrice;
    private TextView mTvPriceList;
    private TextView mTvPriceGrid;
    private ImageView mImgPriceGrid;
    private ImageView mImgPriceList;
    private ImageView mImgPrice;
    private ProgressBar mProgressBar;
    private String gc_id;
    private RelativeLayout layout_null;

    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gc_id= (String) getIntent().getSerializableExtra("gc_id");
//        brandid= (int) getIntent().getSerializableExtra("brandid");
        mRequestQueue = Volley.newRequestQueue(this);
        initMenu();
        initView();
        setOnListener();
        initListView();
        initGridView();

    }

    private void initMenu() {
        Intent intent = getIntent();
//        mDrawer = MenuDrawer.attach(AllGoodsActivity.this, MenuDrawer.Type.OVERLAY, Position.END);
//        mDrawer.setMenuView(R.layout.hwg_menudrawer);
//        mDrawer.setContentView(R.layout.hwg_activity_all_goods);
        setContentView(R.layout.hwg_activity_all_goods);
        // 菜单无阴影
//        mDrawer.setDropShadowEnabled(false);
        filterMenuFragment = new FilterMenuFragment();
//        filterMenuFragment.setCategoryItem(categoryItem);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.menu_container, filterMenuFragment).commit();
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

    private void initGridView() {
        mGridView = (MyGridView) findViewById(R.id.gridView1);
        View head = getLayoutInflater().inflate(R.layout.hwg_head_goods_list, null);
        mGridView.addHeaderView(head, null, false);
//        mGridAdapter = new GoodsGridAdapter();
//        mGridView.setAdapter(mGridAdapter);
        mTvPriceGrid = (TextView) head.findViewById(R.id.tv_price);
        mTvSaleVolumeGrid = (TextView) head.findViewById(R.id.tv_salse_volume);
        mImgPriceGrid = (ImageView) head.findViewById(R.id.img_price);
        mImgGlobalGrid = (ImageView) head.findViewById(R.id.img_global);
//        mImgMenuGrid = (ImageView) head.findViewById(R.id.img_category_menu);
//        mImgMenuGrid.setOnClickListener(this);
        head.findViewById(R.id.btn_global).setOnClickListener(this);
        head.findViewById(R.id.btn_filter).setOnClickListener(this);
        head.findViewById(R.id.btn_price).setOnClickListener(this);
        head.findViewById(R.id.btn_salse_volume).setOnClickListener(this);
//        head.findViewById(R.id.layout_category_search_bar).setOnClickListener(this);
//        head.findViewById(R.id.img_back).setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                GoodsInfo info = goodsList.get(position - 2);
//                gotoDetail(info);
            }
        });
        // 滚动到最上方时隐藏mOverlayHeader
        mGridView.setOnGridScroll2TopListener(new MyGridView.OnGridScroll2TopListener() {

            public void scroll2Top() {
                mOverlayHeader.setVisibility(View.INVISIBLE);
            }
        });
        // 向上滚动后右下角出现回到顶部按钮
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < mLastFirstPosition) {
                    mOverlayHeader.setVisibility(View.VISIBLE);
                } else if (firstVisibleItem > mLastFirstPosition) {
                    mOverlayHeader.setVisibility(View.INVISIBLE);
                }
                mLastFirstPosition = firstVisibleItem;

                if (firstVisibleItem > 0) {
                    mImgOverlay.setVisibility(View.VISIBLE);
                } else {
                    mImgOverlay.setVisibility(View.INVISIBLE);
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

        });
    }

    private void setOnListener() {
//        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_global).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_price).setOnClickListener(this);
        findViewById(R.id.btn_salse_volume).setOnClickListener(this);
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
//        findViewById(R.id.layout_category_search_bar).setOnClickListener(this);
        mOverlayHeader.setOnClickListener(this);
        mLayoutGlobalMenu.setOnClickListener(this);
        mImgOverlay.setOnClickListener(this);
//        mImgMenu.setOnClickListener(this);

    }

    private void initView() {
        mImgPrice = (ImageView) findViewById(R.id.img_price);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvSaleVolume = (TextView) findViewById(R.id.tv_salse_volume);
        mLayoutGlobalMenu = findViewById(R.id.layout_global_menu);
        mOverlayHeader = findViewById(R.id.overlayHeader);
        mImgOverlay = (ImageView) findViewById(R.id.img_overlay);
        mImgGlobal = (ImageView) findViewById(R.id.img_global);
        layout_null= (RelativeLayout) findViewById(R.id.layout_null);
//        mImgMenu = (ImageView) findViewById(R.id.img_category_menu);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

    }


    private void initListView() {

        mListView = (MyListView) findViewById(R.id.listView1);
        View head = getLayoutInflater().inflate(R.layout.hwg_head_goods_list, null);
        mListView.addHeaderView(head, null, false);
//        mListAdapter = new GoodsListAdapter(goodsList);
//        mListView.setAdapter(mListAdapter);
        initAllDates();
        // 滚动到最上方时隐藏mOverlayHeader
        mListView.setOnScroll2TopListener(new MyListView.OnScroll2TopListener() {
            @Override
            public void scroll2Top() {
                mOverlayHeader.setVisibility(View.INVISIBLE);
            }
        });
        // 向上滚动后右下角出现回到顶部按钮
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < mLastFirstPosition) {
                    mOverlayHeader.setVisibility(View.VISIBLE);
                } else if (firstVisibleItem > mLastFirstPosition) {
                    mOverlayHeader.setVisibility(View.INVISIBLE);
                }
                mLastFirstPosition = firstVisibleItem;

                if (firstVisibleItem > 0) {
                    mImgOverlay.setVisibility(View.VISIBLE);
                } else {
                    mImgOverlay.setVisibility(View.INVISIBLE);
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

        });
        mImgPriceList = (ImageView) head.findViewById(R.id.img_price);
        mTvPriceList = (TextView) head.findViewById(R.id.tv_price);
        mTvSaleVolumeList = (TextView) head.findViewById(R.id.tv_salse_volume);
        mImgGlobalList = (ImageView) head.findViewById(R.id.img_global);
//        mImgMenuList = (ImageView) head.findViewById(R.id.img_category_menu);
//        mImgMenuList.setOnClickListener(this);
        head.findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        head.findViewById(R.id.btn_global).setOnClickListener(this);
        head.findViewById(R.id.btn_filter).setOnClickListener(this);
        head.findViewById(R.id.btn_price).setOnClickListener(this);
        head.findViewById(R.id.btn_salse_volume).setOnClickListener(this);
//        head.findViewById(R.id.layout_category_search_bar).setOnClickListener(this);
//        head.findViewById(R.id.img_back).setOnClickListener(this);

    }


    private void initAllDates() {
        ProgressDlgUtil.showProgressDlg("", this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_all_goods+"&gc_id="+gc_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("zjz", response.toString());
                try {
//                    JSONObject object = new JSONObject(response.toString());
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        goodsList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();

                            g.setGoods_id(object1.optString("goods_id"));
                            g.setTitle(object1.optString("goods_name"));
                            g.setMoney(object1.optDouble("goods_price"));
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_salenum(object1.optString("goods_salenum"));
                            goodsList.add(g);
                        }
                        if(goodsList.size()==0){
                            layout_null.setVisibility(View.VISIBLE);
                        }
                        goodsAdapter = new AllGoodsAdapter(AllGoodsActivity2.this, goodsList, mListView);
                        mListView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("zjz", error.getMessage());
            }
        });
        mRequestQueue.add(jr);

//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_all_goods+"&gc_id="+gc_id,null, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("code") == 200) {
//                                goodsList.clear();
//                                JSONObject jsonObject = object.getJSONObject("datas");
//                                JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
//                                Log.i("zjz", "goodsActivity:连接成功");
//                                Log.i("zjz","msg="+msg);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    Goods g = new Goods();
//
//                                    g.setGoods_id(object1.optString("goods_id"));
//                                    g.setTitle(object1.optString("goods_name"));
//                                    g.setMoney(object1.optDouble("goods_price"));
//                                    g.setPicarr(object1.optString("goods_image_url"));
//                                    g.setGoods_salenum(object1.optString("goods_salenum"));
//                                    goodsList.add(g);
//                                }
//                                goodsAdapter = new AllGoodsAdapter(AllGoodsActivity2.this, goodsList, mListView);
//                                mListView.setAdapter(goodsAdapter);
//                                goodsAdapter.notifyDataSetChanged();
//                                ProgressDlgUtil.stopProgressDlg();
//                            } else {
//                                Log.i("zjz", "goodsActivity解析失败");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
//
//                        }
//                    }
//                });
//
//            }
//        });
    }

    public void setSelectedResult(String result) {
        filterMenuFragment.setSelectedResult(result);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
                finish();
                break;
            case R.id.btn_global: // 综合
                showGlobalMenu();
                break;
            case R.id.layout_global_menu: // 综合下拉菜单
                showGlobalMenu();
                break;
            case R.id.img_overlay:
                mListView.setSelection(0);
                mGridView.setSelection(0);
                break;
            case R.id.btn_filter: // 筛选
                toggleFilterMenu();
                break;
            case R.id.btn_price: // 价格排序
                sortByPrice();
                break;
            case R.id.btn_salse_volume: // 销量排序
                sortByVolume();
                break;
            case R.id.overlayHeader:
                break;

            default:
                break;
        }
    }

    /**
     * 排序
     */
    private void sortByPrice() {
        isSortUp = !isSortUp;
        if (isSortUp) {
            sortUp();
        } else {
            sortDown();
        }
    }

    /**
     * 按价格升序
     */
    private void sortUp() {
        int darkgray = getResources().getColor(R.color.darkgray);
        mTvSaleVolume.setTextColor(darkgray);
        mTvSaleVolumeList.setTextColor(darkgray);
        mTvSaleVolumeGrid.setTextColor(darkgray);
        mTvPrice.setTextColor(Color.RED);
        mTvPriceList.setTextColor(Color.RED);
        mTvPriceGrid.setTextColor(Color.RED);
        mImgPrice.setImageResource(R.drawable.sort_button_price_up);
        mImgPriceList.setImageResource(R.drawable.sort_button_price_up);
        mImgPriceGrid.setImageResource(R.drawable.sort_button_price_up);
        ProgressDlgUtil.showProgressDlg("", this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_sort_byprice_up+"&gc_id="+gc_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        goodsList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();

                            g.setGoods_id(object1.optString("goods_id"));
                            g.setTitle(object1.optString("goods_name"));
                            g.setMoney(object1.optDouble("goods_price"));
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_salenum(object1.optString("goods_salenum"));
                            goodsList.add(g);
                        }
                        goodsAdapter = new AllGoodsAdapter(AllGoodsActivity2.this, goodsList, mListView);
                        mListView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        Log.i("zjz", "价格升序失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("zjz", error.getMessage());
            }
        });
        mRequestQueue.add(jr);

    }

    /**
     * 按价格降序
     */
    private void sortDown() {
        mImgPrice.setImageResource(R.drawable.sort_button_price_down);
        mImgPriceList.setImageResource(R.drawable.sort_button_price_down);
        mImgPriceGrid.setImageResource(R.drawable.sort_button_price_down);
        ProgressDlgUtil.showProgressDlg("", this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_sort_byprice_down+"&gc_id="+gc_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        goodsList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();

                            g.setGoods_id(object1.optString("goods_id"));
                            g.setTitle(object1.optString("goods_name"));
                            g.setMoney(object1.optDouble("goods_price"));
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_salenum(object1.optString("goods_salenum"));
                            goodsList.add(g);
                        }
                        goodsAdapter = new AllGoodsAdapter(AllGoodsActivity2.this, goodsList, mListView);
                        mListView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        Log.i("zjz", "价格降序失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("zjz", error.getMessage());
            }
        });
        mRequestQueue.add(jr);

    }

    /**
     * 销量排序
     */
    private void sortByVolume() {
        isSortUp = false;
        int darkgray = getResources().getColor(R.color.darkgray);
        mTvSaleVolume.setTextColor(Color.RED);
        mTvSaleVolumeList.setTextColor(Color.RED);
        mTvSaleVolumeGrid.setTextColor(Color.RED);
        mTvPrice.setTextColor(darkgray);
        mTvPriceList.setTextColor(darkgray);
        mTvPriceGrid.setTextColor(darkgray);
        mImgPrice.setImageResource(R.drawable.sort_button_price);
        mImgPriceList.setImageResource(R.drawable.sort_button_price);
        mImgPriceGrid.setImageResource(R.drawable.sort_button_price);
        goodsAdapter.notifyDataSetChanged();

        ProgressDlgUtil.showProgressDlg("", this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_sort_bysale_down+"&gc_id="+gc_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodsActivity:连接成功");
                        goodsList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();

                            g.setGoods_id(object1.optString("goods_id"));
                            g.setTitle(object1.optString("goods_name"));
                            g.setMoney(object1.optDouble("goods_price"));
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_salenum(object1.optString("goods_salenum"));
                            goodsList.add(g);
                        }
                        goodsAdapter = new AllGoodsAdapter(AllGoodsActivity2.this, goodsList, mListView);
                        mListView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        Log.i("zjz", "销量降序失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("zjz", error.getMessage());
            }
        });
        mRequestQueue.add(jr);
    }


    public void toggleFilterMenu() {
//        mDrawer.toggleMenu();
    }


    private void showGlobalMenu() {
        isGlobalMenuShow = !isGlobalMenuShow;
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(mImgGlobal, "rotation", 0, 180).setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(mImgGlobalList, "rotation", 0, 180).setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(mImgGlobalGrid, "rotation", 0, 180).setDuration(durationRotate).start();
            mLayoutGlobalMenu.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(mLayoutGlobalMenu, "alpha", 0, 1).setDuration(durationAlpha).start();
        } else {
            ObjectAnimator.ofFloat(mImgGlobal, "rotation", 180, 360).setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(mImgGlobalList, "rotation", 180, 360).setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(mImgGlobalGrid, "rotation", 180, 360).setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(mLayoutGlobalMenu, "alpha", 1, 0).setDuration(durationAlpha).start();
            mLayoutGlobalMenu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLayoutGlobalMenu.setVisibility(View.INVISIBLE);
                }
            }, durationAlpha);
        }

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

    class ViewHolder {
        ImageView imgIcon;
        ImageView imgVip;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvPercent;
        TextView tvNum;
        TextView t_goods_name;
        TextView t_goods_money;
        TextView t_goods_count;
        TextView t_goods_works;
        ImageView img_goods_icon;
        LinearLayout linear_root;
    }
}
