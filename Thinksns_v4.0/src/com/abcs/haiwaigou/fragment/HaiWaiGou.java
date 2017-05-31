package com.abcs.haiwaigou.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.activity.GoodsSortActivity;
import com.abcs.haiwaigou.activity.TestLogin;
import com.abcs.haiwaigou.adapter.GridAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.fragment.adapter.HWGFragmentAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Country;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.view.XScrollView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2016/1/11.
 */
@SuppressLint("HandlerLeak")
public class HaiWaiGou extends Fragment implements HWGFragmentViewHolder.ItemOnClick, View.OnClickListener {

    public static TextView car_num;
    public RelativeLayout view;
    public MainActivity activity;

    public Handler handler = new Handler();
    private XScrollView mScrollView;
    //banner
    private boolean isRefresh = false;
    private ViewPager viewpager = null;
    private List<ImageView> list = null;
    private List<ImageView> mList = null;
    private ImageView[] img = null;
    public static String[] picUrl = {"http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_dashujuguanggao.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_jichaguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png"};
    private int pageChangeDelay = 0;

    private ArrayList<Goods> goodsImgs = new ArrayList<Goods>();
    private Button[] btns;
    //	private LinearLayout vpTime;
    private int time = 8000;

    //gridview
    GridAdapter gridAdapter;
    private ArrayList<View> girds = new ArrayList<View>();
    private GridView gridView;
    private int[] icons = {R.drawable.logo_haiwaipaimai, R.drawable.logo_easten_europe, R.drawable.logo_africa,
            R.drawable.logo_spain, R.drawable.logo_austria, R.drawable.logo_switzerland,
            R.drawable.logo_australia, R.drawable.logo_germany, R.drawable.logo_italy};

    //recyclerview
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;


    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (view == null) {
            view = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.hwg_tljr_fragment_haiwaigou, null);
        }
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        car_num = (TextView) view.findViewById(R.id.car_num);
        view.findViewById(R.id.frame_shopcar).setOnClickListener(this);
        view.findViewById(R.id.img_yuyin).setOnClickListener(this);
        view.findViewById(R.id.tljr_grp_syupdate).setOnClickListener(this);

        initScrollView();
        initTitlePage();
        initGirdView();
        initRecyclerView();
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
//        new CartActivity();
//        activity.initListView();
//        activity.main.initCarNum();
        new InitCarNum(car_num, activity,"");
//        int num=CartActivity.mData.size();
//        Log.i("zjz","num="+num);
//        if (num > 0) {
//            car_num.setVisibility(View.VISIBLE);
//            car_num.setText(""+num);
//        } else {
//            car_num.setVisibility(View.INVISIBLE);
//        }
    }


    private void initScrollView() {
        mScrollView = (XScrollView) view.findViewById(R.id.tljr_sy_sc);
        mScrollView.initWithContext(activity);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(false);
        mScrollView.setRefreshTime(Util.getNowTime());

        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 2000);
                Log.i("zjz", "下拉刷新！！");
            }

            @Override
            public void onLoadMore() {
                System.out.println("加载更多");
            }
        });
    }

    private void onLoad() {
        initTitlePage();
        initGirdView();
        initRecyclerView();
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
    }

    private void initTitlePage() {
        // TODO Auto-generated method stub
//        	vpTime = (LinearLayout) view.findViewById(R.id.tljr_fragnment_showye_vptime);
//        	initTime();
        viewpager = (ViewPager) view.findViewById(R.id.tljr_viewpager);
        Util.setSize(viewpager, Util.IMAGEWIDTH, (int) (Util.IMAGEWIDTH / 2.1));
        initBanner();

        //	reflush();
    }

    private void initBanner() {

//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_banner, null, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("status") == 1) {
//                                goodsImgs.clear();
//                                Log.i("zjz", "banner:连接成功");
//                                JSONArray jsonArray = object.getJSONArray("msg");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    Goods g = new Goods();
//                                    g.setPicarr(object1.optString("img"));
//                                    goodsImgs.add(g);
//                                }
//                                initTitleDate();
//                                if (!isRefresh) {
//                                    handler.post(runnable);
//                                }
//                            } else {
//                                Log.i("zjz", "banner:解析失败");
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
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                goodsImgs.clear();
                                Log.i("zjz", "banner:连接成功");

                                JSONArray array = object.getJSONArray("datas");
                                JSONObject object1 = array.getJSONObject(0);
                                JSONObject object2 = object1.getJSONObject("adv_list");
                                JSONArray jsonArray = object2.getJSONArray("item");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object3 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setPicarr(object3.optString("image"));
//                                    Log.i("zjz", "image=" + object3.optString("image"));
                                    goodsImgs.add(g);

                                }
                                initTitleDate();
                                if (!isRefresh) {
                                    handler.post(runnable);
                                }
                            } else {
                                Log.i("zjz", "banner:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        }
                    }
                });

            }
        });
    }

    private void initTitleDate() {
        Log.i("zjz", "goodsize=" + goodsImgs.size());
        list = new ArrayList<ImageView>();
//        TypedArray array = activity.getResources().obtainTypedArray(R.array.banner_array);
        for (int i = 0; i < goodsImgs.size(); i++) {
            ImageView view = new ImageView(activity);
//            view.setBackgroundResource(array.getResourceId(i,R.drawable.img_morentupian));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
//             StartActivity.imageLoader.displayImage(picUrl[i], view);
            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                }
            });
        }
        img = new ImageView[list.size()];
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.tljr_viewGroup);
        layout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            img[i] = new ImageView(activity);
            if (0 == i) {
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.width = 10;
            params.height = 10;
            layout.addView(img[i], params);
        }
        adapter.notifyDataSetChanged();
        if (viewpager.getAdapter() == null) {
            viewpager.setAdapter(adapter);
            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int state) {
                }

                @Override
                public void onPageScrolled(int page, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int page) {
                    check(page);
                }
            });
        }
    }

    private void check(int page) {
        pageChangeDelay = 0;
        for (int i = 0; i < list.size(); i++) {
            if (page == i) {
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }
        }
    }

    PagerAdapter adapter = new PagerAdapter() {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (viewpager.getCurrentItem() >= list.size() - 1) {
                viewpager.setCurrentItem(0);
            } else {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
            handler.postDelayed(runnable, 5000);
        }
    };

    private void initGirdView() {
        gridView = (GridView) view.findViewById(R.id.tljr_zxjx);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        initGird();
    }

    public void initGird() {
        final ArrayList<Country> dataList = new ArrayList<Country>();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_country, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                Log.i("zjz", "grids:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Country c = new Country();
                                    c.setCid(object1.optString("cid"));
                                    c.setName(object1.optString("country"));
                                    c.setPicarr(object1.optString("img"));
                                    dataList.add(c);
                                }
                                gridAdapter = new GridAdapter(activity, dataList);
                                gridView.setAdapter(gridAdapter);
                                gridAdapter.notifyDataSetChanged();
//                                girds.clear();
//                                for (int i = 0; i < icons.length; i++) {
//                                    View v = View.inflate(activity, R.layout.gridvie_country,
//                                            null);
//                                    AbsListView.LayoutParams Params = new AbsListView.LayoutParams((int) (Util.WIDTH / 3), (int) (Util.HEIGHT * 210 / Util.IMAGEHEIGTH));
//                                    v.setLayoutParams(Params);
//                                    girds.add(v);
//                                }
//                                int num = (girds.size() / 3 + (girds.size() % 3 == 0 ? 0 : 1));
//                                int height = (int) (((float) 210 * num / Util.IMAGEHEIGTH) * Util.HEIGHT);
//                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.WIDTH, height);
//                                gridView.setLayoutParams(params);
                            } else {
                                Log.i("zjz", "grids解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        }
                    }
                });

            }
        });
        girds.clear();
        for (int i = 0; i < icons.length; i++) {
            View v = View.inflate(activity, R.layout.hwg_gridview_country,
                    null);
//            v.findViewById(R.id.tljr_view_sygird_icon).setBackgroundResource(
//                    icons[i]);

//            TextView tv = ((TextView) v.findViewById(R.id.tljr_view_sygird_name));
//            tv.setText("");
//            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
//            // 取控件aaa当前的布局参数
//            linearParams.width = (int) (Util.WIDTH / 3) - Util.dip2px(MyApplication.getInstance(),80);
//            tv.setLayoutParams(linearParams);

            AbsListView.LayoutParams Params = new AbsListView.LayoutParams((int) (Util.WIDTH / 3), (int) (Util.HEIGHT * 210 / Util.IMAGEHEIGTH));
            v.setLayoutParams(Params);
            girds.add(v);
        }
        int num = (girds.size() / 3 + (girds.size() % 3 == 0 ? 0 : 1));
        int height = (int) (((float) 210 * num / Util.IMAGEHEIGTH) * Util.HEIGHT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.WIDTH, height);
        gridView.setLayoutParams(params);
    }

    //==========================================测试===========================
    HWGFragmentAdapter hwgFragmentAdapter;
//    FullyLinearLayoutManager fullyLinearLayoutManager;
    FullyGridLayoutManager fullyGridLayoutManager;

    private void initRecyclerView() {
        hwgFragmentAdapter = new HWGFragmentAdapter(getActivity(),this,false);

//        fullyLinearLayoutManager = new FullyLinearLayoutManager(getContext());
        fullyGridLayoutManager=new FullyGridLayoutManager(getContext(),2);

//        fullyLinearLayoutManager = new FullyLinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setFocusable(false);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setLayoutManager(fullyGridLayoutManager);
        recyclerView.setAdapter(hwgFragmentAdapter);
        //添加分割线以及可以调整Item间距
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST,0));
        initDatas();
    }

//    private void initDatas() {
//        hwgFragmentAdapter.getDatas().clear();
//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_pinpai, null, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("status") == 1) {
//                                Log.i("zjz", "recycler:连接成功");
//                                JSONArray jsonArray = object.getJSONArray("msg");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    Brand b = new Brand();
//                                    b.setBimg(object1.optString("bimg"));
//                                    b.setBname(object1.optString("bname"));
//                                    b.setBrandid(object1.optInt("brandid"));
//                                    hwgFragmentAdapter.getDatas().add(b);
//                                }
//                                hwgFragmentAdapter.notifyDataSetChanged();
//                            } else {
//                                Log.i("zjz", "recycler解析失败");
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
//
//    }

    private void initDatas() {
        hwgFragmentAdapter.getDatas().clear();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                goodsImgs.clear();
                                Log.i("zjz", "recycler:连接成功");

                                JSONArray array = object.getJSONArray("datas");
                                JSONObject object1 = array.getJSONObject(7);
                                JSONObject object2 = object1.getJSONObject("goods");
                                JSONArray jsonArray = object2.getJSONArray("item");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object3 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setGoods_id(object3.optString("goods_id"));
                                    g.setPicarr(object3.optString("goods_image"));
                                    g.setTitle(object3.optString("goods_name"));
                                    g.setMoney(object3.optDouble("goods_promotion_price"));
                                    hwgFragmentAdapter.getDatas().add(g);
                                }
                                hwgFragmentAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("zjz", "recycler:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
//    @Override
//    public void onItemRootViewClick(int position) {
////        Toast.makeText(activity,"点击了"+position,Toast.LENGTH_SHORT).show();
////        Log.i("zjz", "点击了" + hwgFragmentAdapter.getDatas().get(position));
////        Intent intent = new Intent(activity, GoodsActivity.class);
//        Intent intent = new Intent(activity, AllGoodsActivity.class);
//        intent.putExtra("brandid", hwgFragmentAdapter.getDatas().get(position).getBrandid());
//        startActivity(intent);
//    }

    @Override
    public void onItemRootViewClick(int position) {
         Intent intent = new Intent(activity, GoodsDetailActivity2.class);
        intent.putExtra("sid", hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        Log.i("zjz","sid="+hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgFragmentAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.frame_shopcar:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
                intent.putExtra("store_id","");
                activity.startActivity(intent);
                break;
            case R.id.img_yuyin:
                intent=new Intent(activity, TestLogin.class);
                activity.startActivity(intent);
                break;
            case R.id.tljr_grp_syupdate:
                intent=new Intent(activity, GoodsSortActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
