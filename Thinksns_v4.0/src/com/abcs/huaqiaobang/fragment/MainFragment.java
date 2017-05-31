package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.HWGFragment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.mCountDownTimer;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.DingQiActivity;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.chart.ChartActivity;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.MainFragmentLayout;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.NewsActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.tljr.news.store.DBHandler;
import com.abcs.huaqiaobang.tljr.news.store.DBManager;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.AnimRFGridLayoutManager;
import com.abcs.huaqiaobang.view.AnimRFRecyclerView;
import com.abcs.huaqiaobang.view.AutoScrollTextView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/11.
 */
public class MainFragment extends Fragment {


    private AnimRFRecyclerView recyclerView;
    public MainActivity activity;
    public View view;

    private ImageView avatar, loginBtn, help;
    private View login, info;
    private TextView name;
    private TextView threeMonthGain, sixMonthGain, oneYearGain;
    private AutoScrollTextView autoScrollTextView;
    private ArrayList<String> autoList = new ArrayList<String>();
    private int index = 0;
    private ViewPager viewpager = null;
    //    private List<ImageView> list = null;
    //    private List<Bitmap> list = null;
    private ImageView[] img = null;
    public String isReadNewsId = "";


    public static ArrayList<News> news;
    //    private String[] picUrl;
    private String[] picUrl = {
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/1.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/2_new.png"};
    private int pageChangeDelay = 0;
    private RelativeLayout titlebar;
    private ImageView img_shuoshuo;
    public TextView notify_circle;
    private DBManager dbManager;
    private boolean isLocal;
    private EditText t;
    private boolean isRefresh = false;
    private ArrayList<MainFragmentLayout> main_layout_list;
    private View banner_view;
    private View huilv_view;
    private View laba_view;
    private View news_view;
    private View vip_view;
    private View xinshou_view;
    private View licai_view;
    private View baozhang_view;
    private View slogan_view;
    private View hongbao_view, yugou_view;
    private String layout;
    //    private ArrayList<View> main_views;
    private Map<String, View> main_views;
    private String[] type_key = new String[]{"banner", "horn", "award", "ecr", "hotnews", "vip", "novice", "finance"
            , "insurance", "slogan"
    };
    private int[] type_position;
    private boolean error = false;
    private MainAdapter recycle_adapter;
    private View headerView;
    private ImageView iv_hander;
    private AnimRFGridLayoutManager manager;
    private ArrayList<View> news_views;
    private Handler handler = new Handler();
    private boolean isfrist = true;
    private Map<Integer, Bitmap> list;
    private String[] localnews;
    private View qg_view;
    private LinearLayout lineaLayoutYungou;
    private ArrayList<Goods> qiangImgs = new ArrayList<Goods>();
    private RequestQueue mRequestQueue;
    //    private AnimRFLinearLayoutManager manager;
    LoadPicture loadPicture = new LoadPicture();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (view == null) {
            view = getActivity().getLayoutInflater().inflate(
                    R.layout.fragment_main, null);
        }
        Log.i("MainFr", "onCreate");
        mRequestQueue = Volley.newRequestQueue(getContext());
        avatar = (ImageView) view.findViewById(R.id.tljr_tx);
        help = (ImageView) view.findViewById(R.id.occft_help);
        name = (TextView) view.findViewById(R.id.tljr_mingiz);
        help.setVisibility(View.INVISIBLE);
        titlebar = (RelativeLayout) view.findViewById(R.id.tljr_grp_sy_title);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
////            window.setFlags(
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titlebar.getLayoutParams();
//            params.setMargins(0, activity.getStatusBarHeight(), 0, 0);
//            titlebar.setLayoutParams(params);
//        }

        notify_circle = (TextView) view.findViewById(R.id.notify_circle);

        img_shuoshuo = (ImageView) view.findViewById(R.id.hq_shuoshuo);
        img_shuoshuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                } else {
                    intent = new Intent(getActivity(), ChartActivity.class);
                }
                startActivity(intent);

                notify_circle.setVisibility(View.GONE);


            }
        });

        recyclerView = (AnimRFRecyclerView) view.findViewById(R.id.main_recylerview);
        headerView = LayoutInflater.from(activity).inflate(R.layout.fragment_main_recycle_headr_view, null);
        recyclerView.addHeaderView(headerView);

        // 设置下拉时拉伸的图片，不设置就使用默认的
        iv_hander = (ImageView) headerView.findViewById(R.id.iv_hander);
        recyclerView.setHeaderImage(iv_hander);
        manager = new AnimRFGridLayoutManager(activity, 1);
//        manager = new AnimRFLinearLayoutManager(activity);
        manager.findFirstCompletelyVisibleItemPosition();
        recyclerView.setLayoutManager(manager);
        recyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                Log.i("MainShouY", "refresh");
                if (MyApplication.getInstance().checkNetWork()) {
                    loadLocalLayout();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefresh(false);
                    }
                }, 5000);
            }

            @Override
            public void onLoadMore() {
                // 开启线加载更多数据
                recyclerView.loadMoreComplate();
            }
        });
//        recyclerView.onSc
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                changeTitleColor(dy);
            }
        });
        dbManager = new DBManager(activity);
        imageLoader = ImageLoader.getInstance();
        news_views = new ArrayList<View>();
        //new AutoLogin(activity);
        loadLocalLayout();

    }

    private class MainAdapter extends AnimRFRecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View tempview = main_views.get(viewType + "");
//            View tempview = LayoutInflater.from(activity).inflate(R.layout.fragment_main_slogan_item, null);
            Log.i("ShouYe", viewType + "viewtype");
            ViewHolder holder = new ViewHolder(tempview);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return main_views.size();
        }

        @Override
        public int getItemViewType(int position) {

//            type_position
            Log.i("ShouYe", type_position[position] + "");
            return type_position[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public class ViewHolder extends AnimRFRecyclerView.ViewHolder {

        public TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void changeTitleColor(int y) {


//        if (y >= 0 && y <= 255) {
//            titlebar.setBackgroundColor(Color.argb(y, 235, 80, 65));
//        } else if (y >= 255) {
//            Log.i("tga", "不透明" + y);
//            titlebar.setBackgroundColor(Color.argb(255, 235, 80, 65));
//        }
//        if (y > 0) {
        Log.i("MainShouY", "不透明" + manager.findFirstCompletelyVisibleItemPosition());
        if (manager.findFirstCompletelyVisibleItemPosition() == 0 || manager.findFirstCompletelyVisibleItemPosition() == 1) {
            titlebar.setBackgroundColor(Color.argb(0, 235, 80, 65));
        } else {
            titlebar.setBackgroundColor(Color.argb(255, 235, 80, 65));
        }


    }


    //=====================zjz===========判断购物车数量
    public void initCarNum() {
        if (MyApplication.getInstance().self != null) {
//            new InitCarNum(HaiWaiGou.car_num,activity);
            MyUpdateUI.sendUpdateCarNum(activity);
//            new InitCarNum(GoodsDetailActivity.car_num,activity);
//            new InitCarNum(GoodsDetailCommentActivity.car_num,activity);
        } else {
            HWGFragment.car_num.setVisibility(View.GONE);
        }
    }
    //===============================zjz================END


    private void loadLocalLayout() {
        main_layout_list = new ArrayList<MainFragmentLayout>();
        main_views = new HashMap<String, View>();
        layout = dbManager.queryLayout("main");
//        if (layout != null) {
//            Log.i("MainFragment", layout);
//
//            getLayoutData(layout);
//
//            if (error) {
//                defaultLayout();
//            }
//            sortLayout();
//
//        } else {

        defaultLayout();
//        }
//        for (int i = 0; i < main_views.size(); i++) {
//            Log.i("MainFragment", main_views.size() + "" + main_views.get("0"));
//            main_layout.addView(main_views.get(i + ""));
//        }
        type_position = new int[main_views.size()];
        Iterator<Map.Entry<String, View>> iterator = main_views.entrySet().iterator();
        int k = 0;
        while (iterator.hasNext()) {
            try {
                type_position[k] = (Integer.valueOf(iterator.next().getKey()));
                k++;
            } catch (Exception e) {
                defaultLayout();
            }
        }
        int temp;
        int size = type_position.length; // 数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (type_position[i] > type_position[j]) { // 交换两数的位置
                    temp = type_position[i];
                    type_position[i] = type_position[j];
                    type_position[j] = temp;
                }
            }
        }
        for (int i : type_position) {
            Log.i("ShouYe", i + "typeposition sort");
        }
        recycle_adapter = new MainAdapter();
        recyclerView.setAdapter(recycle_adapter);
        initViewEvent();

        initData();
        String param = "own=home&platform=1";
        HttpRequest.sendGet(TLUrl.getInstance().URL_main_layout, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(msg);
                            if (object != null && object.getInt("status") == 1) {
                                JSONArray result = object.getJSONArray("result");
                                if (result.length() > 0) {
                                    if (layout == null) {
                                        dbManager.insertLayout(msg, "main");
                                    } else {
                                        dbManager.updateLayout(msg, "main");
                                        Log.i("MainFragment", msg);
                                    }
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

//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//
//            switch (msg.what) {
//                case 0:
////                    list.add((Bitmap) msg.obj);
////                    iv_hander.setImageBitmap((Bitmap) msg.obj);
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    private void initViewEvent() {
        t = (EditText) vip_view.findViewById(R.id.main_et_vip);

        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
//        viewpager = (ViewPager) headerView.findViewById(R.id.tljr_viewpager);


        login = view.findViewById(R.id.tljr_login);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.login();
            }
        });


        loginBtn = (ImageView) headerView.findViewById(R.id.main_img_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(activity, "Login");
                Intent intent = new Intent(activity, WXEntryActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(
                        R.anim.move_left_in_activity,
                        R.anim.move_right_out_activity);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Intent intent = new Intent(activity, HelpActivity.class);
//
//				activity.startActivity(intent);
            }
        });
        info = view.findViewById(R.id.tljr_grp_sy_login);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                MobclickAgent.onEvent(activity, "SelfInfo");
//                Intent intent = new Intent(activity, PersonalActivity.class);
//                startActivity(intent);
//                activity.overridePendingTransition(
//                        R.anim.move_left_in_activity,
//                        R.anim.move_right_out_activity);

                activity.trun2Fragment(4);
            }
        });
        autoScrollTextView = (AutoScrollTextView) laba_view
                .findViewById(R.id.main_txt_speaker);

        autoScrollTextView.setComplete(new Complete() {

            @Override
            public void complete() {
                index++;
                if (index > autoList.size() - 1)
                    index = 0;
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        autoScrollTextView.setText(autoList.get(index));
                        autoScrollTextView.init(activity.getWindowManager());
                        autoScrollTextView.startScroll();
                    }
                }, 3000);
            }
        });

        news_important = (LinearLayout) news_view
                .findViewById(R.id.tljr_news_important);

        news_view.findViewById(R.id.tljr_grp_symore).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
//                        for (MainFragmentLayout mf : activity.maintype_layout_list) {
//                            if (mf.getKey().equals(activity.type_str[2])) {
//                                try {
//                                    activity.setPage(Integer.valueOf(mf.getPosition()));
//                                } catch (Exception e) {
//                                    activity.setPage(2);
//                                }
//                                break;
//                            } else {
                        activity.trun2Fragment(2);
//                            }
//                        }

                    }
                });


        xinshou_view.findViewById(R.id.main_current).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (activity.regularList.size() > 0)
                            activity.startRegularActivity(activity.regularList
                                    .size() - 1);
                    }
                });
        licai_view.findViewById(R.id.main_regular).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        for (MainFragmentLayout mf : activity.maintype_layout_list) {
//                            if (mf.getKey().equals(activity.type_str[1])) {
//                                try {
//                                    activity.setPage(Integer.valueOf(mf.getPosition()));
//                                } catch (Exception e) {
//                                    activity.setPage(0);
//                                }
//                                break;
//                            } else {
//                        activity.trun2Fragment(3);
                        startActivity(new Intent(getContext(), DingQiActivity.class));
//                            }
//                        }
                    }
                });
        threeMonthGain = (TextView) licai_view
                .findViewById(R.id.main_txt_regular_threemonth_gain);
        sixMonthGain = (TextView) licai_view
                .findViewById(R.id.main_txt_regular_sixmonth_gain);
        oneYearGain = (TextView) licai_view
                .findViewById(R.id.main_txt_regular_oneyear_gain);
        vip_view.findViewById(R.id.main_img_enter_vip).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                        showEnterCode();
                    }
                });
        yugou_view.findViewById(R.id.toshopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.trun2Fragment(1);
            }
        });
    }

    private void defaultLayout() {
        inflateLayout();

//        main_views.put("0", banner_view);
        main_views.put("1", laba_view);
        main_views.put("2", huilv_view);
//        main_views.put("3", hongbao_view);
        main_views.put("4", news_view);
        main_views.put("5", yugou_view);
        main_views.put("6", qg_view);
//        main_views.put("5", vip_view);
//        main_views.put("6", xinshou_view);
//        main_views.put("7", licai_view);
//        main_views.put("8", baozhang_view);
        main_views.put("9", slogan_view);
    }

    private void inflateLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
        layoutParams.setMargins(10, 10, 10, 0);
        banner_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_banner_item, null);
        huilv_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_huilv_item, null);
        yugou_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_yugou_item, null);
        qg_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_qianggou_item, null);

//        qg_view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4));
        laba_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_laba_item, null);
        hongbao_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_hongbao_item, null);


        news_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_news_item, null);
        vip_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_vip_item, null);
        vip_view.setLayoutParams(layoutParams);

        xinshou_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_xinshou_item, null);
        xinshou_view.setLayoutParams(layoutParams);


        licai_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_licai_item, null);
        licai_view.setLayoutParams(layoutParams);
        baozhang_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_baozhang_item, null);
        slogan_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_slogan_item, null);
    }

    private void sortLayout() {


        for (int i = 0; i < main_layout_list.size(); i++) {
            Log.i("MainFragment", main_layout_list.get(1).isDisplay() + "");
            MainFragmentLayout mf = main_layout_list.get(i);
//            if (type_key[0].equals(mf.getKey())) {
//                banner_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_banner_item, null);
//
//                banner_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
//                if (mf.isDisplay()) {
//                    main_views.put(mf.getPosition(), banner_view);
//                }
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
//                banner_view.setLayoutParams(layoutParams);
//                continue;
//            }
            if (type_key[1].equals(mf.getKey())) {
                laba_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_laba_item, null);
                laba_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                laba_view.setLayoutParams(layoutParams);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), laba_view);
                }
                continue;
            }
            if (type_key[2].equals(mf.getKey())) {
                hongbao_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_hongbao_item, null);
                hongbao_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                hongbao_view.setLayoutParams(layoutParams);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), hongbao_view);
                }
            }
            if (type_key[3].equals(mf.getKey())) {
                huilv_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_huilv_item, null);
                huilv_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                huilv_view.setLayoutParams(layoutParams);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), huilv_view);
                }
                continue;
            }
            if (type_key[4].equals(mf.getKey())) {
                news_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_news_item, null);
                news_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                news_view.setLayoutParams(layoutParams);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), news_view);
                }
                continue;
            }
            if (type_key[5].equals(mf.getKey())) {
                vip_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_vip_item, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
                layoutParams.setMargins(10, Integer.valueOf(mf.getAnd_margin()), 10, 0);
                vip_view.setLayoutParams(layoutParams);
                vip_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), vip_view);
                }
                continue;
            }
            if (type_key[6].equals(mf.getKey())) {
                xinshou_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_xinshou_item, null);
                xinshou_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
                layoutParams1.setMargins(10, Integer.valueOf(mf.getAnd_margin()), 10, 0);
                xinshou_view.setLayoutParams(layoutParams1);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), xinshou_view);
                }
                continue;
            }
            if (type_key[7].equals(mf.getKey())) {
                licai_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_licai_item, null);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
                layoutParams2.setMargins(10, Integer.valueOf(mf.getAnd_margin()), 10, 0);
                licai_view.setLayoutParams(layoutParams2);
                licai_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                if (mf.isDisplay()) {

                    main_views.put(mf.getPosition(), licai_view);
                }
                continue;
            }
            if (type_key[8].equals(mf.getKey())) {
                baozhang_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_baozhang_item, null);
                baozhang_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, Util.HEIGHT / 4);
                layoutParams2.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                baozhang_view.setLayoutParams(layoutParams2);
                if (mf.isDisplay()) {

                    main_views.put(mf.getPosition(), baozhang_view);
                }
                continue;
            }
            if (type_key[9].equals(mf.getKey())) {
                slogan_view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_slogan_item, null);
                slogan_view.setVisibility(mf.isDisplay() ? View.VISIBLE : View.GONE);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(0, Integer.valueOf(mf.getAnd_margin()), 0, 0);
                slogan_view.setLayoutParams(layoutParams2);
                if (mf.isDisplay()) {
                    main_views.put(mf.getPosition(), slogan_view);
                }
                continue;
            }
        }
        Log.i("MainFragment", main_views.get("0") + "");

        //设置margin
//            if(mf.getAnd_margin()!="0") {
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.setMargins(Integer.valueOf(mf.getAnd_margin()), 0, 0, 0);
//                layout_view.setLayoutParams(lp);
//            }


//        return main_views;
    }


    private void getLayoutData(String msg) {
        try {

            Log.i("tga", msg);
            JSONObject object = new JSONObject(msg);

            Log.i("tga", object.toString());
            if (object != null && object.getInt("status") == 1) {
                JSONArray result = object.getJSONArray("result");
                if (result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        MainFragmentLayout mLayout = new MainFragmentLayout();

                        mLayout.setAnd_margin(result.getJSONObject(i).getString("and_margin"));

                        Log.i("tga", result.getJSONObject(i).getBoolean("display") + "");
                        mLayout.setDisplay(result.getJSONObject(i).getBoolean("display"));
                        mLayout.setKey(result.getJSONObject(i).getString("key"));
                        mLayout.setName(result.getJSONObject(i).getString("name"));
                        mLayout.setPosition(result.getJSONObject(i).getString("position"));
                        main_layout_list.add(mLayout);
                    }

                }

            } else {


            }
        } catch (JSONException e) {
            e.printStackTrace();
            error = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return view;
    }

    private void initData() {
//        if (activity.checkNetWork()) {


        getCurrent();
        getRegular();
        getHotNews();
        initYunGouDate();

        initQiangGou();
//            getLocalNews();

//            loadLocalLayout();
//        } else {
////            getRegular();
//            getLocalNews();
//            activity.showToast("请检查网络");
//
//        }
////        initView();

        getUiData();
    }

    private void initQiangGou() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_good_qianggou, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "qianggou:连接成功");
                        qiangImgs.clear();
                        JSONArray jsonArray = response.getJSONArray("datas");
//                        JSONArray jsonArray = jsonObject.getJSONArray("store_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGoods_id(object1.optString("goods_id"));
                            g.setPicarr(TLUrl.getInstance().URL_hwg_qianggou + object1.optString("store_id") + "/" + object1.optString("groupbuy_image"));
                            g.setQ_end_time(object1.optLong("end_time"));
                            g.setTime(object1.optLong("count_down"));
                            g.setPromote_money(object1.optDouble("groupbuy_price"));
                            if (qiangImgs.size() < 4) {
                                qiangImgs.add(g);
                            }
                        }
                        initQiangGouDate();
                    } else {
                        Log.i("zjz", "storelist:解析失败");
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

            }
        });
        mRequestQueue.add(jr);
    }

    TextView countdownDay, countdownHour, countdownMinute, countdownSecond;
    ImageView imgQiang1, imgQiang2, imgQiang3, imgQiang4;
    TextView[] times;

    private void initQiangGouDate() {

        countdownDay = (TextView) qg_view.findViewById(R.id.countdown_day);
        countdownHour = (TextView) qg_view.findViewById(R.id.countdown_hour);
        countdownMinute = (TextView) qg_view.findViewById(R.id.countdown_minute);
        countdownSecond = (TextView) qg_view.findViewById(R.id.countdown_second);
        imgQiang1 = (ImageView) qg_view.findViewById(R.id.img_qiang1);
        imgQiang2 = (ImageView) qg_view.findViewById(R.id.img_qiang2);
        imgQiang3 = (ImageView) qg_view.findViewById(R.id.img_qiang3);
        imgQiang4 = (ImageView) qg_view.findViewById(R.id.img_qiang4);
        times = new TextView[4];
        times[0] = countdownDay;
        times[1] = countdownHour;
        times[2] = countdownMinute;
        times[3] = countdownSecond;
        for (int i = 0; i < qiangImgs.size(); i++) {
            switch (i) {
                case 0:
                    mCountDownTimer countDownTimer = new mCountDownTimer(qiangImgs.get(i).getTime() * 1000, 1000, times);
                    countDownTimer.start();
                    loadPicture.initPicture(imgQiang1, qiangImgs.get(i).getPicarr());
                    imgQiang1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                            intent.putExtra("sid", qiangImgs.get(0).getGoods_id());
                            intent.putExtra("pic", qiangImgs.get(0).getPicarr());
                            intent.putExtra("end_time", qiangImgs.get(0).getQ_end_time());
                            intent.putExtra("promote_money", qiangImgs.get(0).getPromote_money());
                            intent.putExtra("qiang", true);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    loadPicture.initPicture(imgQiang2, qiangImgs.get(i).getPicarr());
                    imgQiang2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                            intent.putExtra("sid", qiangImgs.get(1).getGoods_id());
                            intent.putExtra("pic", qiangImgs.get(1).getPicarr());
                            intent.putExtra("end_time", qiangImgs.get(1).getQ_end_time());
                            intent.putExtra("promote_money", qiangImgs.get(1).getPromote_money());
                            intent.putExtra("qiang", true);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    loadPicture.initPicture(imgQiang3, qiangImgs.get(i).getPicarr());
                    imgQiang3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                            intent.putExtra("sid", qiangImgs.get(2).getGoods_id());
                            intent.putExtra("pic", qiangImgs.get(2).getPicarr());
                            intent.putExtra("end_time", qiangImgs.get(2).getQ_end_time());
                            intent.putExtra("promote_money", qiangImgs.get(2).getPromote_money());
                            intent.putExtra("qiang", true);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    loadPicture.initPicture(imgQiang4, qiangImgs.get(i).getPicarr());
                    imgQiang4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                            intent.putExtra("sid", qiangImgs.get(3).getGoods_id());
                            intent.putExtra("pic", qiangImgs.get(3).getPicarr());
                            intent.putExtra("end_time", qiangImgs.get(3).getQ_end_time());
                            intent.putExtra("promote_money", qiangImgs.get(3).getPromote_money());
                            intent.putExtra("qiang", true);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
            }
        }
    }

    private void initYunGouDate() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_GOODS_ALL, "key=shopList&pages=1&pagelist=3", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                yungouList.clear();
                                Log.i("zjz", "allGoodsFragment:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setTitle(object1.getString("title"));
                                    g.setMoney(object1.getDouble("money"));
                                    g.setCanyurenshu(object1.getInt("canyurenshu"));
                                    g.setZongrenshu(object1.getInt("zongrenshu"));
                                    g.setShenyurenshu(g.getZongrenshu() - g.getCanyurenshu());
                                    g.setPicarr(object1.getString("picarr"));
                                    g.setId(object1.getInt("id"));
                                    g.setQishu(object1.getInt("qishu"));
                                    g.setLayoutType(1);
                                    if (yungouList.size() < 4) {
                                        yungouList.add(g);
                                    }
                                }
//                                yunGouAdapter=new YunGouAdapter(getActivity(),yungouList);
//                                yungouGridView.setAdapter(yunGouAdapter);
//
//                                yunGouAdapter.notifyDataSetChanged();
                                initYunGouView();
                            } else {
                                Log.i("zjz", "allGoodsFragment:解析失败");
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

    private ArrayList<Goods> yungouList = new ArrayList<Goods>();


    private void initYunGouView() {
        lineaLayoutYungou = (LinearLayout) yugou_view.findViewById(R.id.linea_layoutYungou);
        for (int i = 0; i < 3; i++) {
            View yungouView = LayoutInflater.from(activity).inflate(R.layout.fragment_shopping_yungou_item, null);
            View viewline = new View(getContext());
            viewline.setBackgroundColor(Color.parseColor("#ffcdcdcd"));
            viewline.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
            yungouView.setLayoutParams(new LinearLayout.LayoutParams(Util.WIDTH / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
            ImageView imageView = (ImageView) yungouView.findViewById(R.id.img_goods_icon);
            loadPicture.initPicture(imageView, yungouList.get(i).getPicarr());
            TextView tGoodsName = (TextView) yungouView.findViewById(R.id.t_goods_name);
            tGoodsName.setText(yungouList.get(i).getTitle());
            TextView tBuyNum = (TextView) yungouView.findViewById(R.id.t_buy_num);
            tBuyNum.setText(yungouList.get(i).getCanyurenshu() + "");
            TextView tTotalNum = (TextView) yungouView.findViewById(R.id.t_total_num);
            tTotalNum.setText(yungouList.get(i).getZongrenshu() + "");
            ProgressBar progressBar = (ProgressBar) yungouView.findViewById(R.id.processbar);
            int pro = (int) (Float.valueOf(yungouList.get(i).getCanyurenshu())
                    / Float.valueOf(yungouList.get(i).getZongrenshu()) * 100);
            progressBar.setProgress(pro);
            Button button = (Button) yungouView.findViewById(R.id.btn_buy);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApplication.getInstance().self == null) {
                        Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Log.i("zjz", "url=" + TLUrl.getInstance().URL_GOODS_SHOPCAR + "?" + "uid=" + MyApplication.getInstance().self.getId() + "&id="
                            + yungouList.get(finalI).getId() + "&num=1" + "&qishu=" + yungouList.get(finalI).getQishu());
                    HttpRequest.sendGet(TLUrl.getInstance().URL_GOODS_SHOPCAR, "uid=" + MyApplication.getInstance().self.getId() + "&id="
                            + yungouList.get(finalI).getId() + "&num=1" + "&qishu=" + yungouList.get(finalI).getQishu(), new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("status") == 1) {
                                            Log.i("zjz", "carFragment添加成功");
                                            Intent intent = new Intent(getActivity(), PayWayActivity.class);
                                            intent.putExtra("yungou", true);
                                            getActivity().startActivity(intent);
                                        } else {
                                            Log.i("zjz", "carFragment添加失败");
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        Log.i("zjz", e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });

                }
            });
            lineaLayoutYungou.addView(yungouView);
            lineaLayoutYungou.addView(viewline);
        }

    }

    private void getLocalNews() {

        localnews = dbManager.queryNew("50", DBHandler.TABLE_HOTNEWS);
        String queryNew;

        if (localnews != null && !isRefresh) {
            queryNew = localnews[0];

            if (queryNew != null) {

                isLocal = true;

                getData(queryNew, new Complete() {
                    @Override
                    public void complete() {
                        news_important.removeAllViews();
                        for (int i = 0; i < news.size(); i++) {
                            news_views.add(getOneXwByImp(news.get(i), i));
                            news_important.addView(getOneXwByImp(news.get(i), i));
                        }

                    }
                });


            }
//            if(isRefresh) {

//            }

        }
//        getHotNews();

    }

    private void getHotNews() {


        initNews(new Complete() {

            @Override
            public void complete() {
                // TODO Auto-generated method stub
                news_important.removeAllViews();
                for (int i = 0; i < news.size(); i++) {
                    news_views.add(getOneXwByImp(news.get(i), i));
                    news_important.addView(getOneXwByImp(news.get(i), i));
                }
//                if (localnews != null) {
//                    for (int i = 0; i < news.size(); i++) {
//                        setNewsView(news.get(i), i, news_views.get(i));
//                    }
//                } else {
//                    for (int i = 0; i < news.size(); i++) {
//                        news_views.add(getOneXwByImp(news.get(i), i));
//                        news_important.addView(getOneXwByImp(news.get(i), i));
//                    }
//                }

            }
        });

    }


    private void initNews(final Complete complete) {
        // TODO Auto-generated method stub
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());

        String id = Constent.preference.getString("news_p_id", "0");
        if (MyApplication.getInstance().self != null) {
            id = MyApplication.getInstance().self.getId();
            Constent.preference.edit().putString("news_p_id", id).commit();

        }

        Log.i("synews",TLUrl.getInstance().URL_new + "api/main/hn?platform=2&uid=" + id+"&digest=35");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                TLUrl.getInstance().URL_new + "api/main/hn?platform=2&uid=" + id+"&digest=35",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                Log.i("synews",response.toString());
                getData(response.toString(), complete);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLocal = false;
            }
        });

        newRequestQueue.add(jsonObjectRequest);

    }

    private void getData(String response, Complete complete) {

        news = new ArrayList<News>();


        //   Log.i("tga", "reCmd====" + cmdInfo.toString());



        try {
            JSONObject cmdInfo = new JSONObject(response);

            com.alibaba.fastjson.JSONObject ttt = com.alibaba.fastjson.JSONObject.parseObject(response);

            boolean insert = dbManager.insertOnceRequestNews(ttt, DBHandler.TABLE_HOTNEWS);
            if (cmdInfo.optString("status", "0").equals("0")) {
                return;
            }

            JSONObject obj = cmdInfo.getJSONObject("joData");
            JSONArray array = obj.getJSONArray("data");

            for (int i = 0; i < array.length(); i++) {
                News news_temp = new News();

                String title = array.getJSONObject(i)
                        .optString("title");
                news_temp.setTitle(title);
                String url = array.getJSONObject(i)
                        .optString("purl");
                news_temp.setpUrl(url);
                news_temp.setDigest(array.getJSONObject(i).optString("digest"));
                news_temp.setTime(array.getJSONObject(i)
                        .optLong("time"));
                String source = array.getJSONObject(i)
                        .optString("source");
                news_temp.setSource(source);
                String surl = array.getJSONObject(i)
                        .optString("surl");
                news_temp.setSurl(surl);
                news_temp.setId(array.getJSONObject(i)
                        .optString("id"));
                news_temp.setContent(array.getJSONObject(i)
                        .optString("content"));
                news_temp.setSpecial(array.getJSONObject(i).optString("species"));

                news_temp.setHaveCai(array.getJSONObject(i)
                        .optBoolean("hasOppose", false));
                news_temp.setHaveZan(array.getJSONObject(i)
                        .optBoolean("hasPraise", false));
                news_temp.setHaveSee(array.getJSONObject(i)
                        .optBoolean("read", false));

                news.add(news_temp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            complete.complete();
        }
    }


    private void getUiData() {
//        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("getindexinfo", "method=getindexinfo");
        HttpRequest.sendPost(TLUrl.getInstance().URL_indexPage, "method=getindexinfo",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getindexinfo", msg);
                        if (msg.equals("")) {
                            getUiData();
                            return;
                        }
//                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                initUi(jsonObject.getJSONObject("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initUi(final JSONObject jsonObject) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONArray array = jsonObject.getJSONArray("exchangerage");
                    int count = 0;
                    LinearLayout layout = (LinearLayout) view
                            .findViewById(R.id.main_grp_forex);
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        if (layout.getChildAt(i) instanceof TextView
                                && count < array.length()) {
                            JSONObject object = array.getJSONObject(count);
                            ((TextView) layout.getChildAt(i)).setText(object
                                    .getString("currencyName")
                                    + "\n"
                                    + object.getString("exchangeRate") + ":1");
                            changeText(((TextView) layout.getChildAt(i)));
                            count++;
                        }
                    }
//                    hongbao_view.findViewById(R.id.main_grp_award).setVisibility(
//                            jsonObject.getBoolean("showRed") ? View.VISIBLE
//                                    : View.GONE);
                    com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray
                            .parseArray(jsonObject.getString("broadcasts"));
                    LogUtil.e("getindexinfo", jsonArray.size() + "");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        autoList.add(jsonArray.getString(i));
                        LogUtil.e("getindexinfo", jsonArray.getString(i));
                    }
                    if (autoList.size() > 0) {
                        autoScrollTextView.setText(autoList.get(index));
                        autoScrollTextView.init(activity.getWindowManager());
                        autoScrollTextView.startScroll();
                    }
                    com.alibaba.fastjson.JSONArray urls = com.alibaba.fastjson.JSONArray
                            .parseArray(jsonObject.getString("urls"));
                    if (urls.size() > 0) {
                        picUrl = new String[urls.size()];
                        for (int i = 0; i < urls.size(); i++) {
                            System.err.println("urls:" + urls.getString(i));
                            picUrl[i] = urls.getString(i);
                        }
                        initView();
                        if (!isRefresh) {

                            handler.post(runnable);
                        } else {
                            recyclerView.setRefresh(false);
//                            titlebar.setBackgroundColor(Color.argb(0, 235, 80, 65));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
//        list = new ArrayList<ImageView>();
        list = new HashMap<Integer, Bitmap>();
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        com.android.volley.toolbox.ImageLoader loader = new com.android.volley.toolbox.ImageLoader(requestQueue, null);
//        for (int i = 0; i < picUrl.length; i++) {
////            ImageView view1 = new ImageView(activity);
////            view.setBackgroundResource(R.drawable.img_kaixuanmen);
////            view.setScaleType(ImageView.ScaleType.FIT_XY);
////
////            Util.setImage(picUrl[i], view, handler);
//            final int index = i;
//
//
//            loader.get(picUrl[i], new com.android.volley.toolbox.ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer, boolean b) {
//                    Bitmap bitmap = imageContainer.getBitmap();
//                    iv_hander.setImageBitmap(bitmap);
//                    list.put(index, bitmap);
//                }
//
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//
//                }
//            });
//            requestQueue.start();
//
//        }
        img = new ImageView[picUrl.length];
        LinearLayout layout = (LinearLayout) headerView
                .findViewById(R.id.tljr_viewGroup);
        layout.removeAllViews();
        for (int i = 0; i < picUrl.length; i++) {
            img[i] = new ImageView(activity);
            if (0 == i) {
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.width = 10;
            params.height = 10;
            layout.addView(img[i], params);
            final int index = i;
//            Util.setImage(picUrl[i], iv_hander, handler);

//            ImageView imageView = new ImageView(activity);
            imageLoader.loadImage(picUrl[i], Options.getListOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    if(0==index)
                    iv_hander.setImageBitmap(loadedImage);
                    list.put(index, loadedImage);
                }
            });

            Log.i("ShouYe", "come");

        }
//        imageLoader.d

    }

    private int flag = 0;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {

            if (flag >= picUrl.length) {
                flag = 0;
            }
            for (int i = 0; i < img.length; i++) {
                if (i == flag) {
                    img[i].setBackgroundResource(R.drawable.img_yuandian1);
                } else {
                    img[i].setBackgroundResource(R.drawable.img_yuandian2);
                }

            }
            if (list.get(flag) != null) {
                iv_hander.setImageBitmap(list.get(flag));
                flag++;
            }


            handler.postDelayed(runnable, 10000);
        }
    };

    private LinearLayout news_important, main_layout;
    private ImageLoader imageLoader;

    public void getCurrent() {
//        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("getAllProductList",
                "method=getAllProductList&type=2&page=1&size=10");
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
                "method=getAllProductList&type=2&page=1&size=10",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getCurrent", msg);
                        if (msg.equals("")) {
                            getCurrent();
                            return;
                        }
//                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                activity.currentList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    Product product = new Product();
                                    JSONObject object = jsonArray
                                            .getJSONObject(i);
                                    product.setBuyMoney(object
                                            .optInt("buyMoney"));
                                    product.setBuyNum(object.optInt("buyNum"));
                                    product.setBuyRate(object
                                            .optDouble("buyRate"));
                                    product.setConfirmBuyDate(object
                                            .optLong("confirmBuyDate"));
                                    product.setDesc(object.optString("desc"));
                                    product.setEarnings(object
                                            .optDouble("earnings"));
                                    product.setEndBuyDate(object
                                            .optLong("endBuyDate"));
                                    product.setIconUrl(object
                                            .optString("iconUrl"));
                                    product.setId(object.optString("id"));
                                    product.setName(object.optString("name"));
                                    product.setNumberOfDays(object
                                            .optInt("numberOfDays"));
                                    product.setOrder(object.optInt("order"));
                                    product.setProductType(object
                                            .optInt("productType"));
                                    product.setSoldMoney(object
                                            .optLong("soldMoney"));
                                    product.setUpdateDate(object
                                            .optLong("updateDate"));
                                    product.setVip(object.optBoolean("vip"));
                                    product.setOverlayEarnings(object
                                            .optDouble("overlayEarnings", 0));
                                    product.setItems(object.optString("items"));
                                    activity.currentList.add(product);
                                    // if (i == 0) {
                                    // activity.current
                                    // .initFormCurrent(object);
                                    // }
                                }
                                initCurrent();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("tga", "json解析出错");
                        }
                    }
                });
    }

    private void initCurrent() {

        Log.i("tga", "inicurrent");
        // if (activity.currentList.size() == 0) {
        //
        // Log.i("tga", "0=======");
        // return;
        // }
        // activity.current.initView();
        handler.post(new Runnable() {

            @Override
            public void run() {
                long soldMoney = 0, buyNum = 0;
                for (int i = 0; i < activity.currentList.size(); i++) {
                    Product product = activity.currentList.get(i);
                    soldMoney += product.getSoldMoney();
                    buyNum += product.getBuyNum();
                }
                Product product = activity.currentList.get(0);
                Log.i("tga", "post" + product.toString());
                ((TextView) xinshou_view.findViewById(R.id.main_txt_current_gain))
                        .setText(Util.df.format(product.getEarnings() * 100)
                                + "%");
                ((TextView) xinshou_view.findViewById(R.id.main_txt_current_sellandbuy))
                        .setText("已售出" + soldMoney + "  已有" + buyNum + "人购买");
            }
        });
    }

    public void getRegular() {


        LogUtil.e("getRegular",
                "method=getAllProductList&type=1&page=1&size=10");
//        ProgressDlgUtil.showProgressDlg("", activity);
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
                "method=getAllProductList&type=1&page=1&size=10",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getRegular", msg);
                        if (msg.equals("")) {
                            getRegular();
                            return;
                        }
//                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                activity.regularList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Product product = new Product();
                                    JSONObject object = jsonArray
                                            .getJSONObject(i);
                                    product.setBuyMoney(object
                                            .optInt("buyMoney"));
                                    product.setBuyNum(object.optInt("buyNum"));
                                    product.setBuyRate(object
                                            .optDouble("buyRate"));
                                    product.setConfirmBuyDate(object
                                            .optLong("confirmBuyDate"));
                                    product.setDesc(object.optString("desc"));
                                    product.setEarnings(object
                                            .optDouble("earnings"));
                                    product.setEndBuyDate(object
                                            .optLong("endBuyDate"));
                                    product.setIconUrl(object
                                            .optString("iconUrl"));
                                    product.setId(object.optString("id"));
                                    product.setName(object.optString("name"));
                                    product.setNumberOfDays(object
                                            .optInt("numberOfDays"));
                                    product.setOrder(object.optInt("order"));
                                    product.setProductType(object
                                            .optInt("productType"));
                                    product.setSoldMoney(object
                                            .optLong("soldMoney"));
                                    product.setUpdateDate(object
                                            .optLong("updateDate"));
                                    product.setVip(object.optBoolean("vip"));
                                    product.setOverlayEarnings(object
                                            .optDouble("overlayEarnings", 0));
                                    product.setItems(object.optString("items"));
                                    activity.regularList.add(product);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initRegular();
                    }
                });
    }

    private void initRegular() {
        if (activity.regularList.size() == 0) {
            return;
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                long soldMoney = 0, buyNum = 0;
                for (int i = 0; i < activity.regularList.size(); i++) {
                    Product product = activity.regularList.get(i);
                    soldMoney += product.getSoldMoney();
                    buyNum += product.getBuyNum();
                    TextView textView = null;
                    if (i == 0) {
                        textView = threeMonthGain;
                    } else if (i == 1) {
                        textView = sixMonthGain;
                    } else if (i == 2) {
                        textView = oneYearGain;
                    }
                    if (textView != null)
                        textView.setText(Util.df.format(product.getEarnings() * 100)
                                + "%");
                }
                ((TextView) licai_view.findViewById(R.id.textView4))
                        .setText(activity.regularList.get(0).getName());
                ((TextView) licai_view.findViewById(R.id.textView5))
                        .setText(activity.regularList.get(1).getName());
                ((TextView) licai_view.findViewById(R.id.textView6))
                        .setText(activity.regularList.get(2).getName());
                ((TextView) licai_view.findViewById(R.id.main_txt_regular_sellandbuy))
                        .setText("已售出" + soldMoney + "  已有" + buyNum + "人购买");
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        activity.regular.initView();
//                    }
//                }, 1000);

            }
        });
    }

    private void changeText(TextView view) {
        String str = view.getText().toString();
        int index = str.indexOf(":");
        if (index < 0) {
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new AbsoluteSizeSpan(10, true), index - 1, index,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#ed3535")),
                str.indexOf(")") + 1, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(style);

    }

    public void logout() {
        MyApplication.getInstance().self = null;
        avatar.setImageResource(R.drawable.img_avatar);
        info.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
        // ZiXuan.addStatus=true;

    }

    public void modifyNickName() {
        if (MyApplication.getInstance().self != null) {
            name.setText(Util.shortName(MyApplication.getInstance().self
                    .getNickName()));
        }
    }

    public void initUser() {

        if (MyApplication.getInstance().self != null) {
            User user = MyApplication.getInstance().self;
            avatar.setVisibility(View.VISIBLE);
            if ("".equals(Util.preference.getString("avatar", ""))) {
                MyApplication.imageLoader.displayImage(user.getAvatarUrl(), avatar,
                        Options.getCircleListOptions());
            } else {
                avatar.setImageResource(R.drawable.img_avatar);
                avatar.setImageDrawable(getResources().getDrawable(R.drawable.img_avatar));
            }
            name.setVisibility(View.VISIBLE);
            name.setText(Util.shortName(user.getNickName()));
            info.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
        } else {
            info.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private View getOneXwByImp(final News news, final int position)
            throws ParseException {
        View v = activity.getLayoutInflater().inflate(R.layout.tljr_item_syxw2,
                null);
        setNewsView(news, position, v);
        return v;
    }


    private void setNewsView(final News news, final int position, View v) {


        TextView title = (TextView) v.findViewById(R.id.imp_news_title2);
        TextView content = (TextView) v.findViewById(R.id.imp_news_content2);
        ImageView picture = (ImageView) v.findViewById(R.id.imp_news_picture2);

//         String imageUri = "drawable://" + localPicture[news.getSYXW_TYPE() %
        // 4];

        // picture.setImageResource(localPicture[news.getSYXW_TYPE()%4]);
        // StartActivity.imageLoader.displayImage(imageUri,picture,
        // StartActivity.options);
        // String rPURL = "";
        // if (!TextUtils.isEmpty(news.getpUrl()))
        // {

        if (isLocal) {


            final ImageAware imageAware = new ImageViewAware(picture, false);
            imageLoader.displayImage(news.getpUrl(), imageAware, com.abcs.huaqiaobang.tljr.news.Options.getListOptions(),
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        /*
                                         * 若联网时imageloader没有下载的图片，将使用从离线下载获取到的图片
										 */
                            System.out.println("onLoadingFailed");
                            news.setpUrl("" + news.getId() + ".png");
                            StartActivity.imageLoader.displayImage(news.getpUrl(), imageAware);
                        }

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
        } else {

            imageLoader
                    .displayImage(news.getpUrl(), picture, StartActivity.options);
        }

        // rPURL = news.getpUrl();
        // }

        title.setText(Util.StringFilter(news.getTitle().trim()));

        content.setText(Util.StringFilter(news.getDigest()));

        v.setPadding(0, 20, 0, 0);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!news.isHaveSee() && !HuanQiuShiShi.id.contains(news.getId())) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("time", news.getTime());
                        obj.put("id", news.getId());
                        obj.put("species", news.getSpecial());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (HuanQiuShiShi.readId == null) {
                        HuanQiuShiShi.readId = new JSONArray();
                    }
                    HuanQiuShiShi.readId.put(obj);
                    HuanQiuShiShi.id.add(news.getId());
                    HuanQiuShiShi.updataUserIsRead();
                    LogUtil.i("read", "isReadNewsId :" + HuanQiuShiShi.readId.toString());
                }

                Intent intent = new Intent(activity, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                bundle.putString("SYNews", "");


//                bundle.putSerializable("news1", MainFragment.this.news);
                intent.putExtras(bundle);


                startActivity(intent);
            }
        });
    }

    /*
     * 上传已阅数据
     */
    private void updataUserIsRead() {

        if (MyApplication.getInstance().self == null) {
            return;
        }

        if (isReadNewsId.equals("")) {
            return;
        }

        Log.i("read", "updataUserIsRead-isReadNewsId :" + isReadNewsId);
        Log.i("read", "上传已阅-:" + TLUrl.getInstance().URL_newsIsRead + "?" + "type=1"
                + "&pId=" + MyApplication.getInstance().self.getId()
                + "&data=[" + isReadNewsId + "]");
        HttpRequest.sendPost(TLUrl.getInstance().URL_newsIsRead, "type=1" + "&pId="
                + MyApplication.getInstance().self.getId() + "&data=["
                + isReadNewsId + "]", new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                // TODO Auto-generated method stub
                Log.i("read", "上传已阅+msg:" + msg);
                isReadNewsId = "";

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void refreshNickName() {
        name.setText(MyApplication.getInstance().self.getNickName());
    }

    public void refreshImgHeader() {
        imageLoader.displayImage(MyApplication.getInstance().self.getAvatarUrl(), avatar, Options.getCircleListOptions());
    }

    public void refreshImgHeader(int i) {
        avatar.setImageResource(R.drawable.img_avatar);
    }

}
