package com.abcs.haiwaigou.local.huohang.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.local.adapter.MyListPopAdapter;
import com.abcs.haiwaigou.local.adapter.MyListRightPopAdapter;
import com.abcs.haiwaigou.local.adapter.PeiSongTopAllTagAdapter;
import com.abcs.haiwaigou.local.adapter.PeiSongTopLineTagAdapter;
import com.abcs.haiwaigou.local.adapter.PoPGetStoreLeftAdapter;
import com.abcs.haiwaigou.local.adapter.PoPGetStoreRightAdapter;
import com.abcs.haiwaigou.local.beans.ActivityArr;
import com.abcs.haiwaigou.local.beans.HuoHangItem;
import com.abcs.haiwaigou.local.beans.SSXaun;
import com.abcs.haiwaigou.local.beans.TagArr;
import com.abcs.haiwaigou.local.beans.getStoreFirm;
import com.abcs.haiwaigou.local.beans.localPaiXu2;
import com.abcs.haiwaigou.local.evenbus.Notice;
import com.abcs.haiwaigou.local.huohang.adapter.PeiSongDetialAdapter3;
import com.abcs.haiwaigou.local.view.SideLetterBar;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class HuoHangFragment extends FragmentSociax implements View.OnClickListener, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, PeiSongDetialAdapter3.OnShopCartGoodsChangeListener {

    public RelativeLayout re_peisong_jie, re_qubu, re_paixu, re_xxaun;
    RelativeLayout re_che;
    public TextView tv_peisong_number, tv_peisong_qisong, tv_peisong_gfw;

    LinearLayout liner_mim;
    public ImageView img_peisong_che, iv_search;

    String title;
    ImageView img_paixu;
    EasyRecyclerView rvList;

    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_xiala)
    TextView tvXiala;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.iv_setting)
    ImageView ivSetting;
    @InjectView(R.id.tv_mess_num)
    TextView tv_mess_num;  // 未读消息数量
    @InjectView(R.id.iv_ps_xiaojiao1)
    ImageView iv_ps_xiaojiao1;
    @InjectView(R.id.tv_letter_overlay)
    TextView tvLetterOverlay;

    @InjectView(R.id.et_search)
    EditText textView2;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout reSearchTop;
    @InjectView(R.id.liner_title)
    LinearLayout liner_title;
    @InjectView(R.id.iv_guide)
    ImageView iv_guide;
    @InjectView(R.id.re_back)
    RelativeLayout re_back;
    @InjectView(R.id.side_letter_bar)
    SideLetterBar sideLetterBar;
    @InjectView(R.id.view_null)
    View view_null;

    @InjectView(R.id.recyclerview_top_line_tag)
    RecyclerView recyclerview_top_line_tag;
    @InjectView(R.id.iv_top_show_all_tag)
    ImageView iv_top_show_all_tag;
    @InjectView(R.id.ll_top_line)
    public LinearLayout ll_top_line;
    @InjectView(R.id.tv_ou_yuan)
    public TextView tv_ou_yuan;  // 欧元
    @InjectView(R.id.relat_tv)
    public RelativeLayout relat_tv;  // 返利弹窗
    @InjectView(R.id.relative_no_city)
    public RelativeLayout relative_no_city;
    @InjectView(R.id.relate_null)
    public RelativeLayout relate_null;
    @InjectView(R.id.t_select)
    public TextView t_select;
    @InjectView(R.id.lintive_setting)
    public LinearLayout lintive_setting;

    private boolean isRefresh = false;
    PeiSongDetialAdapter3 mAdapter;
    private Handler handler = new Handler();
    boolean isfirst=true;

    public List<TagArr> topTagArr = new ArrayList<>();//初始顶部标签行数据
    public List<TagArr> allTopTagArr = new ArrayList<>();//初始化所有顶部标签//所有顶部标签数据
    public PeiSongTopLineTagAdapter peiSongTopLineTagAdapter;//顶部标签适配器
    private PopupWindow pop_show_all_tag;//显示所有标签poppuwindow

    ActivityHome activity;
    private View view;
    String initTitle="仟惠仁（维也纳）";

    private ACache aCache;

    private MyBroadCastReceiver myBroadCastReceiver;
    private static HuoHangFragment instance;
    public static HuoHangFragment newInstance() {
        if (instance == null) {
            instance = new HuoHangFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_activity_bendi_peisong, null);
        }
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);

        Log.i(TAG, "onCreate: "+ MyApplication.getInstance().getIsFirstLocal());
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        img_paixu = (ImageView) view.findViewById(R.id.img_paixu);
        liner_mim = (LinearLayout) view.findViewById(R.id.liner_mim);
        re_che = (RelativeLayout) view.findViewById(R.id.re_che);
        rvList = (EasyRecyclerView) view.findViewById(R.id.listview_datas);
        tv_peisong_qisong = (TextView) view.findViewById(R.id.tv_peisong_qisong);
        re_peisong_jie = (RelativeLayout) view.findViewById(R.id.re_peisong_jie);
        re_qubu = (RelativeLayout) view.findViewById(R.id.re_qubu);
        re_paixu = (RelativeLayout) view.findViewById(R.id.re_paixu);
        re_xxaun = (RelativeLayout) view.findViewById(R.id.re_xxuan);
        tv_peisong_gfw = (TextView) view.findViewById(R.id.tv_peisong_gfw);
        tv_peisong_number = (TextView) view.findViewById(R.id.tv_peisong_number);
        img_peisong_che = (ImageView) view.findViewById(R.id.img_peisong_che);

        initListene();

        try {
            Typeface type= Typeface.createFromAsset(activity.getAssets(),"font/ltheijian.TTF");
            if(type!=null){
                tv_peisong_gfw.setTypeface(type) ;
                tv_peisong_qisong.setTypeface(type) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        aCache=ACache.get(activity);

        if(MyApplication.getInstance().getIsFirstLocal()){  //第一次进入

            relate_null.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth(), MyApplication.getWidth()*1920/1080);
            layoutParams.setMargins(0,0,0,0);
            iv_guide.setLayoutParams(layoutParams);
            iv_guide.setVisibility(View.VISIBLE);
            iv_guide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_guide.setVisibility(View.GONE);
                    if(ServerUtils.isConnect(activity)){
                        relative_no_city.setVisibility(View.GONE);

                        checkIsInto();/// 判断是否有店铺
                    }else {
                        relative_no_city.setVisibility(View.VISIBLE);
                        ToastUtil.showMessage("请检查您的网络！");
                    }

                    MyApplication.getInstance().saveIsFirstLocal(false);
                }
            });
        }else {
            if(ServerUtils.isConnect(activity)){
                relative_no_city.setVisibility(View.GONE);

                checkIsInto();/// 判断是否有店铺
            }else {
                relative_no_city.setVisibility(View.VISIBLE);
                ToastUtil.showMessage("请检查您的网络！");
            }
        }
        initTopLineTag();

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.local_activity_bendi_peisong;
    }


        private void checkIsInto() {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=verify_member_native&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=verify_member_native&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("state") == 1) {  // 去货行首页
                              /*  "id": "2132",
                                        "store_name": "天赋打呢",
                                        "address_id": "3624",
                                        "district_id": "1",
                                        "district_name": " 仟惠仁（维也纳）",
                                        "member_id": "21",
                                        "member_name": "17875505332",
                                        "add_time": "1491805590",
                                        "text": null,
                                        "is_default": "1"*/

                                JSONObject bean = object.optJSONObject("datas");

                                if (!TextUtils.isEmpty(bean.optString("district_id"))) {
                                    relate_null.setVisibility(View.GONE);
                                    district_id=bean.optString("district_id");
                                    cart_num=0;
                                    initCarNum();
                                }

                                if (!TextUtils.isEmpty(bean.optString("store_name"))) {
                                    tvName.setText(bean.optString("store_name"));
                                }
                                if (!TextUtils.isEmpty(bean.optString("address_id"))) {
                                    address_id=bean.optString("address_id");
                                }

                                if (!TextUtils.isEmpty(bean.optString("district_name"))) {
                                    if(bean.optString("district_name").length()>3){
                                        tvTitle.setText(bean.optString("district_name").substring(0,3));
                                    }else {
                                        tvTitle.setText(bean.optString("district_name"));
                                    }
                                }

                                initVPaiXu();
                                getUnReadMsg();  // 初始未读信息数

                            }else {  // 不是本地用户
                                /*原来的*/
//                                relate_null.setVisibility(View.VISIBLE);
//                                tvName.setText("我的店");
//                                tvTitle.setText(initTitle.substring(0,3));
//                                relate_null.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        //弹出广告的信息窗口
////                                        showGuanggaoWindow();
//                                        startActivity(new Intent(activity, LoacalKeFuActivity_HH.class));
//                                    }
//                                });
                                /*原来的*/

                                ///////////////////////////////////////////////////////////////////////////
                                is_LocalMember=1;
                                order="";
                                district_id="";
                                address_id="";
                                class_id="";
                                tvName.setText("");
                                tvTitle.setText(initTitle.substring(0,3));
                                initVPaiXu();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public   int  is_LocalMember= 0;  //  0 是货行的用户  1 不是货行的用户
    private void getUnReadMsg(){
//        http://www.huaqiaobang.com/mobile/index.php?act=native_message&op=get_message_state&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(com.thinksns.sociax.thinksnsbase.utils.TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native_message&op=get_message_state&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "getUnReadMsg: " + msg);

                  /*      {
                            "state": 1,
                                "datas": {
                            "msg": "弹窗内容",
                                    "message_num": 1,
                                    "message_state": 1
                        }
                        }*/

                        try {
                            JSONObject object=new JSONObject(msg);

                            if(object.optInt("state")==1){

                                JSONObject datas=object.optJSONObject("datas");

                                    if (datas.optInt("message_state")==1) {// 弹窗
                                        if(!isUpdata){
                                        View root_view=View.inflate(activity,R.layout.pop_notices_msg2,null);
                                        TextView img_close=(TextView) root_view.findViewById(R.id.imge_close);
                                        com.abcs.haiwaigou.view.AlignTextView tv_msg=(com.abcs.haiwaigou.view.AlignTextView) root_view.findViewById(R.id.tv_msg);

                                        tv_msg.setText(datas.optString("msg"));

                                        final PopupWindow popupWindow= new PopupWindow();
                                        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                                        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                                        popupWindow.setContentView(root_view);
                                        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                                        params.alpha = 0.5f;
                                        activity.getWindow().setAttributes(params);
                                        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                return false;
                                            }
                                        });
                                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                            @Override
                                            public void onDismiss() {
                                                WindowManager.LayoutParams params =activity.getWindow()
                                                        .getAttributes();
                                                params.alpha = 1f;
                                                activity.getWindow().setAttributes(params);
                                            }
                                        });
                                        popupWindow.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
                                        //触摸点击事件
                                        popupWindow.setTouchable(true);
                                        //聚集
                                        popupWindow.setFocusable(true);
                                        //设置允许在外点击消失
                                        popupWindow.setOutsideTouchable(false);
                                        //点击返回键popupwindown消失

                                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

                                        if(!TextUtils.isEmpty(datas.optString("msg"))){
                                            popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);    //消息弹窗
                                        }

                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                popupWindow.dismiss();
                                            }
                                        });
                                    }
                                }

                                if(datas!=null){   // 未读信息数

                                    if(datas.optInt("message_num")>0){
                                        tv_mess_num.setVisibility(View.VISIBLE);
                                        tv_mess_num.setText(datas.optInt("message_num")+"");
                                    }else {
                                        tv_mess_num.setVisibility(View.GONE);
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
    //显示所有顶部标签
    private void initShowAllTag() {
        View view = View.inflate(activity,R.layout.popup_bendi_show_all_tag,null);
        RecyclerView rv_all_tag = (RecyclerView) view.findViewById(R.id.rv_all_tag);
        rv_all_tag.setLayoutManager(new LinearLayoutManager(activity));
        PeiSongTopAllTagAdapter peiSongTopAllTagAdapter = new PeiSongTopAllTagAdapter(activity,allTopTagArr);
        rv_all_tag.setAdapter(peiSongTopAllTagAdapter);

        pop_show_all_tag = new PopupWindow(activity);
        pop_show_all_tag.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_show_all_tag.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        pop_show_all_tag.setContentView(view);

        pop_show_all_tag.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        pop_show_all_tag.setTouchable(true);
        //聚集
        pop_show_all_tag.setFocusable(true);
        //设置允许在外点击消失
        pop_show_all_tag.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        pop_show_all_tag.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1f;
        activity.getWindow().setAttributes(params);
        pop_show_all_tag.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        pop_show_all_tag.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        pop_show_all_tag.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        peiSongTopAllTagAdapter.setOnItemClickListener(new PeiSongTopAllTagAdapter.OnAllTagItemClickListener() {
            @Override
            public void allTagItemClick(TagArr tagArr) {
                addTopLineTag(tagArr);
                pop_show_all_tag.dismiss();
            }
        });
    }

    //初始化顶部标签栏
    private void initTopLineTag() {
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_top_line_tag.setLayoutManager(linearLayoutManager);//设置成横向列表
        peiSongTopLineTagAdapter = new PeiSongTopLineTagAdapter(activity, topTagArr);
        recyclerview_top_line_tag.setAdapter(peiSongTopLineTagAdapter);

        iv_top_show_all_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_show_all_tag.showAsDropDown(view);
            }
        });
    }
    //添加顶部标签
    public void addTopLineTag(TagArr tag){
        if(tag!=null){
            peiSongTopLineTagAdapter.addTag(tag);
        }
    }

    // 初始化 排序
    private void initVPaiXu(){

        idFromChangY=false;
        idFromNormal=false;
        idFromYouHui=false;
        isSearch=false;
        idFromPaiXu=true;
        loadingPaiXu();
        img_paixu.setImageResource(R.drawable.sort_button_down);
        tv1.setTextColor(getResources().getColor(R.color.local_ss));
        tv2.setTextColor(getResources().getColor(R.color.red));
        tv3.setTextColor(getResources().getColor(R.color.local_ss));

    }
    private boolean isSearch=false;
    private int currentPage = 1;

    private void initSlideBar() {
//        sideLetterBar.setOverlay(tvLetterOverlay);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = getLetterPosition(letter);
                Log.i("zds","slide_position="+position);
                rvList.scrollToPosition(position);
            }
        });
    }

    StringBuffer tag_ids;
    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {
        }

        @Override
        public void updateCarNum(Intent intent) {
            Log.i(TAG, "updateCarNum: ");
            cart_num=0;
            initCarNum();
        }

        @Override
        public void updateCollection(Intent intent) {
            Log.i(TAG, "updateCollection: ");
            if(intent!=null&&intent.getStringExtra("type").equals("tags")){

                if(peiSongTopLineTagAdapter.getItemCount()>0){
                    ll_top_line.setVisibility(View.VISIBLE);
                    if(peiSongTopLineTagAdapter.topTagArr.size()>0){
                        tag_ids=new StringBuffer();
                        for (int i=0;i<peiSongTopLineTagAdapter.topTagArr.size();i++){
                            if(i!=peiSongTopLineTagAdapter.topTagArr.size()-1){
                                tag_ids.append(peiSongTopLineTagAdapter.topTagArr.get(i).tagId);
                                tag_ids.append(",");
                            }else {
                                tag_ids.append(peiSongTopLineTagAdapter.topTagArr.get(i).tagId);
                            }
                        }
                    }

                    Log.i(TAG, "updateCarNum: tag_sizes"+peiSongTopLineTagAdapter.topTagArr.size());
                    Log.i(TAG, "updateCarNum: tag_ids"+tag_ids);
                    page=1;
                    isClickTag=true;
                    isRefresh=true;
                    idFromPaiXu=false;
                    idFromChangY=false;
                    idFromNormal=false;
                    idFromYouHui=false;
                }else {
                    ll_top_line.setVisibility(View.GONE);
                    page=1;
                    isClickTag=false;
                    isRefresh=true;
                    idFromPaiXu=false;
                    idFromChangY=false;
                    idFromNormal=true;
                    idFromYouHui=false;

                }

                loading();
            }
        }

        @Override
        public void update(Intent intent) {
        }
    };

    private String patamas;
    private String class_id="0";
    int page = 1;
    String order;
    private String district_id;
    private String address_id;
    private String district_name;
    private String store_name;
    private String order_key = "2";

    public int cart_num=0;
    public double cart_price;

    /****
     * 获取购物车数量
     */
    private void initCarNum() {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=find_cart_num&key=5ff36f75680c05703394eff5013f6a1a&district_id=1
        String car_patamas = "act=native&op=find_cart_num&district_id=" + district_id + "&key=" + MyApplication.getInstance().getMykey();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", car_patamas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(msg);

                            if (jsonObject.optInt("state") == 1) {

                                cart_num = jsonObject.optInt("cart_num");
                                cart_price = jsonObject.optDouble("cart_price");
                                double cart_taxprice = jsonObject.optDouble("cart_taxprice");
                                double rebate_money = jsonObject.optDouble("rebate_money");

                                tv_ou_yuan.setText("€"+NumberUtils.formatPriceNo(rebate_money));

                                Log.d(TAG, "run: cart_num"+cart_num+"====="+cart_price);

                                if (cart_num > 0) {
                                    tv_peisong_number.setText(cart_num + "");
//                                    tv_peisong_number.setVisibility(View.VISIBLE); // 暂时隐藏
                                    img_peisong_che.setImageResource(R.drawable.bg_bottom_psong_che2);
                                    tv_peisong_qisong.setText("选好了");
                                    tv_peisong_gfw.setText("€"+NumberUtils.formatPriceNo(cart_price));
                                    re_peisong_jie.setBackgroundResource(R.drawable.bg_bottom_psong_jie);
                                } else {
                                    tv_peisong_number.setText(cart_num + "");
//                                    tv_peisong_number.setVisibility(View.GONE);
                                    img_peisong_che.setImageResource(R.drawable.bg_bottom_psong_che2);
                                    tv_peisong_qisong.setText("€0.00");
                                    tv_peisong_gfw.setText("购物车为空");
                                    re_peisong_jie.setBackgroundResource(R.drawable.bg_bottom_psong_jie_no);
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

    /****
     * 加载排序数据
     */
    private boolean idFromChangY=false;
    private boolean idFromYouHui=false;
    public boolean idFromPaiXu=false;
    public boolean idFromNormal=false;
    public boolean isClickTag=false;

    private void loadingPaiXu() {

        ProgressDlgUtil.showProgressDlg("", activity);

//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=spell_goods_sort&key=25918f85df03d5ebfa076600519ecd1e&district_id=1&type=1&order=1+"&address_id="+address_id
        patamas="act=native&op=spell_goods_sort&key="+ MyApplication.getInstance().getMykey()+"&type=1&district_id="+district_id+"&order="+order+"&address_id="+address_id;

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", patamas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("zds", "paixu=" + msg);
//                        if (isRefresh) {
                        mAdapter.clear();
//                            isRefresh = false;
//                        }

                        if (!TextUtils.isEmpty(msg)) {

                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("state") == 1) {
                                    view_null.setVisibility(View.GONE);

                                    /**
                                     * 添加获取所有顶部标签
                                     */

                                    if(object.has("tag_array")){
                                        allTopTagArr.clear();
                                        JSONArray jsonArray_Tag = object.getJSONArray("tag_array");
                                        for(int f=0;f<jsonArray_Tag.length();f++){
                                            TagArr acty=new TagArr();
                                            JSONObject acty_object = jsonArray_Tag.getJSONObject(f);
                                            acty.tagId=acty_object.optString("tag_id");
                                            acty.img=acty_object.optString("img");
                                            acty.tagName=acty_object.optString("tag_name");
                                            if (!allTopTagArr.contains(acty)){
                                                allTopTagArr.add(acty);
                                            }
                                        }
                                        initShowAllTag();
                                    }

                                    JSONArray jsonArray = object.getJSONArray("datas");
                                    ArrayList<localPaiXu2.DatasEntry> ping_p=new ArrayList<localPaiXu2.DatasEntry>();
                                    ping_p.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        localPaiXu2.DatasEntry item=new localPaiXu2.DatasEntry();
                                        JSONObject localPaixu = jsonArray.getJSONObject(i);
                                        if (!TextUtils.isEmpty(localPaixu.optString("goods_letter"))) {
                                            item.setGoodsLetter(localPaixu.optString("goods_letter"));
                                        }

                                        JSONArray array = localPaixu.getJSONArray("goods_list");
                                        ArrayList<HuoHangItem> bean=new ArrayList<HuoHangItem>();
                                        bean.clear();
                                        for(int y=0;y<array.length();y++){
                                            HuoHangItem local= new HuoHangItem();
                                            JSONObject jsonobject = array.getJSONObject(y);
                                            local.goodsId=jsonobject.optString("goods_id");
                                            local.goodsName=jsonobject.optString("goods_name");
                                            local.goodsEnName=jsonobject.optString("goods_en_name");
                                            local.goodsSerial=jsonobject.optString("goods_serial");
                                            local.goodsImage=jsonobject.optString("goods_image");
                                            local.goodsPrice=jsonobject.optDouble("goods_price");
                                            local.goodsStorage=jsonobject.optString("goods_storage");
                                            local.tax_rate=jsonobject.optString("tax_rate");
                                            local.price_hide=jsonobject.optInt("price_hide");
                                            local.tax_includ=jsonobject.optString("tax_includ");

                                            if(jsonobject.has("activity_arr")){
                                                JSONArray activity_arr=jsonobject.optJSONArray("activity_arr");
                                                List<ActivityArr> localActivityList=new ArrayList<>();
                                                localActivityList.clear();
                                                for(int jj=0;jj<activity_arr.length();jj++){
                                                    ActivityArr acty=new ActivityArr();
                                                    JSONObject acty_object = activity_arr.getJSONObject(jj);
                                                    acty.types=acty_object.optString("types");
                                                    acty.img=acty_object.optString("img");
                                                    acty.activityName=acty_object.optString("activity_name");
                                                    localActivityList.add(acty);
                                                }

                                                local.activityArr.addAll(localActivityList);
                                            }

                                            if(jsonobject.has("tag_arr")){
                                                JSONArray tag_arr=jsonobject.optJSONArray("tag_arr");
                                                List<TagArr> localTagList=new ArrayList<>();
                                                localTagList.clear();
                                                for(int f=0;f<tag_arr.length();f++){
                                                    TagArr acty=new TagArr();
                                                    JSONObject acty_object = tag_arr.getJSONObject(f);
                                                    acty.tagId=acty_object.optString("tag_id");
                                                    acty.img=acty_object.optString("img");
                                                    acty.tagName=acty_object.optString("tag_name");
                                                    localTagList.add(acty);
                                                }
                                                local.tagArr.addAll(localTagList);
                                            }

                                            bean.add(local);
                                        }

                                        item.setGoodsList(bean);
                                        ping_p.add(item);
                                    }

                                    Log.i(TAG, "ping_p"+ping_p.size());

                                    if (ping_p.size() > 0) {
                                        letterIndexes = new HashMap<>();
                                        sections = new String[ping_p.size()];
                                        for (int index = 0; index < ping_p.size(); index++) {
                                            //当前城市拼音首字母
                                            String currentLetter = ping_p.get(index).goodsLetter;
                                            letterIndexes.put(currentLetter, index);
                                            sections[index] = currentLetter;
                                        }

                                        mAdapter.addAll(ping_p);
                                        mAdapter.notifyDataSetChanged();
                                        sideLetterBar.setVisibility(View.VISIBLE);
                                        initSlideBar();
                                        mAdapter.stopMore();
                                        view_null.setVisibility(View.VISIBLE);
                                    } else {
                                    }
                                }else {
                                    view_null.setVisibility(View.VISIBLE);
                                    ToastUtil.showMessage("已加载全部数据！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showMessage("请求失败！请重试！");
                        }
                        ProgressDlgUtil.stopProgressDlg();
                    }
                });
            }
        });
    }

    boolean isFirstSearch=true;
    // 本地搜索商品
    private void initSearch() {
        if(TextUtils.isEmpty(keyword)){
            ToastUtil.showMessage("请输入要搜索的内容");
            return;
        }

        sideLetterBar.setVisibility(View.GONE);
        if(isFirstSearch){
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }


        Log.i("zds", "currentPage=" + currentPage);
        Log.i("zds", "keyword=" + textView2.getText().toString().trim());

        HttpRequest.sendPost(TLUrl.getInstance().URL_bendi_goods, "&key=" + MyApplication.getInstance().getMykey() + "&keyword=" + keyword + "&page=" + currentPage + "&page_size=10&order_key=" + order_key, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isFirstSearch){
                            ProgressDlgUtil.stopProgressDlg();
                            isFirstSearch=false;
                        }

                        Log.i("zds", "msg=" + msg);

                        if (!TextUtils.isEmpty(msg)) {

                            if (isRefresh) {
                                mAdapter.clear();
                                isRefresh = false;
                            }

                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("state") == 1) {
                                    view_null.setVisibility(View.GONE);
                                    /**
                                     * 添加获取所有顶部标签
                                     */

                                    if(object.has("tag_array")){
                                        allTopTagArr.clear();
                                        JSONArray jsonArray_Tag = object.getJSONArray("tag_array");
                                        for(int f=0;f<jsonArray_Tag.length();f++){
                                            TagArr acty=new TagArr();
                                            JSONObject acty_object = jsonArray_Tag.getJSONObject(f);
                                            acty.tagId=acty_object.optString("tag_id");
                                            acty.img=acty_object.optString("img");
                                            acty.tagName=acty_object.optString("tag_name");
                                            if (!allTopTagArr.contains(acty)){
                                                allTopTagArr.add(acty);
                                            }
                                        }
                                        initShowAllTag();
                                    }

                                    JSONArray jsonArray = object.getJSONArray("datas");
                                    ArrayList<localPaiXu2.DatasEntry> ping_p=new ArrayList<localPaiXu2.DatasEntry>();
                                    ping_p.clear();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ArrayList<HuoHangItem> bean=new ArrayList<HuoHangItem>();
                                        bean.clear();
                                        localPaiXu2.DatasEntry item=new localPaiXu2.DatasEntry();
                                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                                        if (jsonobject.has("goods_letter")&&!TextUtils.isEmpty(jsonobject.optString("goods_letter"))) {
                                            item.setGoodsLetter(jsonobject.optString("goods_letter"));
                                        }

                                        HuoHangItem local= new HuoHangItem();
                                        local.goodsId=jsonobject.optString("goods_id");
                                        local.goodsName=jsonobject.optString("goods_name");
                                        local.goodsEnName=jsonobject.optString("goods_en_name");
                                        local.goodsSerial=jsonobject.optString("goods_serial");
                                        local.goodsImage=jsonobject.optString("goods_image");
                                        local.goodsPrice=jsonobject.optDouble("goods_price");
                                        local.goodsStorage=jsonobject.optString("goods_storage");
                                        local.tax_rate=jsonobject.optString("tax_rate");
                                        local.price_hide=jsonobject.optInt("price_hide");
                                        local.tax_includ=jsonobject.optString("tax_includ");

                                        if(jsonobject.has("activity_arr")){
                                            JSONArray activity_arr=jsonobject.optJSONArray("activity_arr");
                                            List<ActivityArr> localActivityList=new ArrayList<>();
                                            localActivityList.clear();
                                            for(int jj=0;jj<activity_arr.length();jj++){
                                                ActivityArr acty=new ActivityArr();
                                                JSONObject acty_object = activity_arr.getJSONObject(jj);
                                                acty.types=acty_object.optString("types");
                                                acty.img=acty_object.optString("img");
                                                acty.activityName=acty_object.optString("activity_name");
                                                localActivityList.add(acty);
                                            }

                                            local.activityArr.addAll(localActivityList);
                                        }

                                        if(jsonobject.has("tag_arr")){
                                            JSONArray tag_arr=jsonobject.optJSONArray("tag_arr");
                                            List<TagArr> localTagList=new ArrayList<>();
                                            localTagList.clear();
                                            for(int f=0;f<tag_arr.length();f++){
                                                TagArr acty=new TagArr();
                                                JSONObject acty_object = tag_arr.getJSONObject(f);
                                                acty.tagId=acty_object.optString("tag_id");
                                                acty.img=acty_object.optString("img");
                                                acty.tagName=acty_object.optString("tag_name");
                                                localTagList.add(acty);
                                            }
                                            local.tagArr.addAll(localTagList);
                                        }

                                        bean.add(local);

                                        item.setGoodsList(bean);
                                        ping_p.add(item);
                                    }

                                    Log.i(TAG, "ping_p"+ping_p.size());

                                    if (ping_p.size() > 0) {
                                        mAdapter.addAll(ping_p);
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        ToastUtil.showMessage("还没有数据，过会再来！");
                                    }
                                }else {
                                    if(serFirst&&currentPage==1){
                                        popTiShi("抱歉！没有找到相关的商品。");
                                        serFirst=false;
                                    }else {
                                        ToastUtil.showMessage("已加载全部数据！");
                                    }
                                    view_null.setVisibility(View.VISIBLE);
                                    mAdapter.stopMore();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showMessage("请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }

    private void popTiShi(String messge){
        View itemView = View.inflate(activity, R.layout.tljr_dialog_tishi, null);
        TextView msg = (TextView) itemView.findViewById(R.id.tljr_dialog_exit_msg);
        Button ok = (Button) itemView.findViewById(R.id.tljr_dialog_exit_ok);

        final PopupWindow popupWindow = new PopupWindow(itemView,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        msg.setText(messge);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onRefresh() {
        if(isSearch){
            currentPage = 1;
            isRefresh = true;
            initSearch();
        }else if(idFromPaiXu){
//            isRefresh = true;
            isfirst=false;
            loadingPaiXu();
        }else {
            page = 1;
            isRefresh = true;
            loading();
        }
    }


    @Override
    public void onLoadMore() {
        if(isSearch){
            if (currentPage == -1) {
                mAdapter.stopMore();
                view_null.setVisibility(View.VISIBLE);
                return;
            }
            currentPage=currentPage+1;
            initSearch();
        }else if (isClickTag) {
            page = page + 1;
            loading();
        }else {
            if (page == -1) {
                mAdapter.stopMore();
                view_null.setVisibility(View.VISIBLE);
                return;
            }
            page = page + 1;
            loading();
        }
    }


    private void initListene() {
        iv_search.setOnClickListener(this);
        re_peisong_jie.setOnClickListener(new NoDoubleClickListener() {  // 结算
            @Override
            public void onNoDoubleClick(View v) {
                if(is_LocalMember==1){  // 不是货行用户
//                    ToastUtil.showMessage("您还不是货行用户，请联系客户！");
                    return;
                }else {
                    Intent intent = null;
                    if (MyApplication.getInstance().self == null) {
                        intent = new Intent(activity, WXEntryActivity.class);
                        intent.putExtra("isthome", true);
                        startActivity(intent);
                        return;
                    } else {
                        if(Integer.valueOf(tv_peisong_number.getText().toString().trim())>0){
                            jieSuan();
                        }else {
                            ToastUtil.showMessage("您还没有选择商品呢！");
                            return;
                        }
                    }
                }
            }
        });

        re_che.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if(is_LocalMember==1){  // 不是货行用户
//                    ToastUtil.showMessage("您还不是货行用户，请联系客户！");
                    return;
                }else {
                    if (MyApplication.getInstance().self == null) {
                        Intent che = new Intent(activity, WXEntryActivity.class);
                        che.putExtra("isthome", true);
                        startActivity(che);
                        return;
                    } else {
                        addToCart();
                    }
                }
            }
        });
        re_qubu.setOnClickListener(this);
        re_paixu.setOnClickListener(this);
        re_xxaun.setOnClickListener(this);

//        添加自定义分割线：可自定义分割线高度和颜色
//        rvList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 0, getResources().getColor(R.color.transparent)));
        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rvList.setRefreshListener(this);
        rvList.setAdapter(mAdapter = new PeiSongDetialAdapter3(activity,tv_peisong_number));

        mAdapter.setmActivity(activity,this);
        mAdapter.setOnShopCartGoodsChangeListener(this);
        mAdapter.setNoMore(R.layout.view_nomore);
        mAdapter.setMore(R.layout.view_more, this);
        mAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.resumeMore();
            }
        });

    }

    @Override
    public void initData() {

    }

    ListView listview_pop_left;
    GridView gridview_datas;
    PopupWindow popupWindow;


    private void initPopQu() {

        View itemView = View.inflate(activity, R.layout.popup_sanbi_saixuan, null);
        listview_pop_left = (ListView) itemView.findViewById(R.id.listview);
        gridview_datas = (GridView) itemView.findViewById(R.id.gridview_datas);

        popupWindow = new PopupWindow(itemView, Util.WIDTH, Util.HEIGHT * 10 / 15);
        popupWindow.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        listview_pop_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SSXaun.DatasEntry ssXaun = (SSXaun.DatasEntry) parent.getItemAtPosition(position);
                listAdapter.setSelectedPosition(position);
                listAdapter.notifyDataSetChanged();

                if (!TextUtils.isEmpty(ssXaun.gcId)) {
                    gridview_datas.setAdapter(null);
                    initPopRight(ssXaun.gcId);
                   /* mAdapter.clear();
                    page = 1;
                    isRefresh = true;
                    idFromChangY=false;
                    idFromYouHui=false;
                    loading();*/
                }

//                popupWindow.dismiss();

            }
        });

        gridview_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SSXaun.DatasEntry ssXaun = (SSXaun.DatasEntry) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(ssXaun.gcId)) {
                    class_id = ssXaun.gcId;
                    page = 1;
                    mAdapter.clear();
                    ll_top_line.setVisibility(View.GONE);
                    isClickTag=false;
                    peiSongTopLineTagAdapter.clearAddDatas();// 清空顶部tag集合
                    idFromNormal=true;
                    idFromChangY=false;
                    idFromYouHui=false;
                    idFromPaiXu=false;
                    isSearch=false;
                    loading();
                }

                tv1.setText("返回");
                iv_ps_xiaojiao1.setVisibility(View.GONE);

                popupWindow.dismiss();
            }
        });
    }

    private void showGuanggaoWindow() {
//        https://japi.tuling.me/hrq/partner/getPartner

        if(!TextUtils.isEmpty(aCache.getAsString("nodressid"))){
            Log.i(TAG, "showGuanggaoWindow: local");
            initPetenr(aCache.getAsString("nodressid"));
        }else {
//            http://www.huaqiaobang.com/mobile/index.php?act=native&op=native_tc&key=25918f85df03d5ebfa076600519ecd1e
            HttpRequest.sendGet("http://www.huaqiaobang.com/mobile/index.php", "act=native&op=native_tc&key="+MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("getPartner", msg + "");

                            if (TextUtils.isEmpty(msg)) {
                                ToastUtil.showMessage("网络出错！");
                                return;
                            } else {
                                initPetenr(msg);
                                aCache.put("nodressid",msg);
                            }
                        }
                    });
                }
            });
        }
    }
    private void initPetenr(String msg_s){

        View itemView = View.inflate(activity, R.layout.local_guanggao_detail_item2, null);
        final TextView localDetailTvContain = (TextView) itemView.findViewById(R.id.local_item_detail_tv_contain);  //广告的内容
        final TextView localDetailTvName = (TextView) itemView.findViewById(R.id.local_detail_tv_name);          //标题
        final TextView tv_weixin_hao = (TextView) itemView.findViewById(R.id.tv_weixin_hao);          //微信号
        final ImageView localDetailIvWeiXin = (ImageView) itemView.findViewById(R.id.local_item_iv_weixin);  //微信二维码
        final ImageView localDetailClose = (ImageView) itemView.findViewById(R.id.local_detail_close);
        final com.abcs.huaqiaobang.view.CircleImageView local_detail_civ_head = (com.abcs.huaqiaobang.view.CircleImageView) itemView.findViewById(R.id.local_detail_civ_head);

        try {
            JSONObject jsonObject= new JSONObject(msg_s);
            localDetailTvContain.setText(jsonObject.optString("message"));
            tv_weixin_hao.setText(jsonObject.optString("phone"));

            if(!TextUtils.isEmpty(jsonObject.optString("img"))){
                MyApplication.imageLoader.displayImage(jsonObject.optString("img"),localDetailIvWeiXin,MyApplication.getListOptions());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        popupWindow = new PopupWindow(itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        popupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0);

        localDetailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    private boolean isLoFirst=true;
    private void loading() {

        sideLetterBar.setVisibility(View.GONE);

        if(isLoFirst){
            ProgressDlgUtil.showProgressDlg("", activity);
        }

        Log.i(TAG, "loading: page========="+page);

        if(idFromChangY){  // 来自我的常用
            patamas = "act=native&op=index&common_id=1&district_id=" + district_id +"&page=" + page + "&page_size=10" + "&key=" + MyApplication.getInstance().getMykey();
        }else if(idFromYouHui){ // 来自优惠
            patamas = "act=native&op=index&activity_id=1&district_id=" + district_id + "&page=" + page + "&page_size=10" + "&key=" + MyApplication.getInstance().getMykey();
        }else if(isClickTag){
            patamas = "act=native&op=index&key=" + MyApplication.getInstance().getMykey()+"&tag_id="+tag_ids+"&district_id="+district_id+"&page=" + page + "&page_size=10";
        }else if(idFromNormal){  // 来自普通列表
            patamas = "act=native&op=index&district_id=" + district_id +  "&page=" + page + "&page_size=10" + "&key=" + MyApplication.getInstance().getMykey()+"&class_id="+class_id+"&address_id="+address_id;

            order_key="";
            img_paixu.setImageResource(R.drawable.sort_button);
            tv1.setTextColor(getResources().getColor(R.color.red));
            tv2.setTextColor(getResources().getColor(R.color.local_ss));
            tv3.setTextColor(getResources().getColor(R.color.local_ss));
        }


        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", patamas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(isLoFirst){
                            ProgressDlgUtil.stopProgressDlg();
                            isLoFirst=false;
                        }

                        Log.i("zds", "putong=" + msg);
                        if (isRefresh) {
                            mAdapter.clear();
                            isRefresh = false;
                        }

                        if (!TextUtils.isEmpty(msg)) {

                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("state") == 1) {
                                    view_null.setVisibility(View.GONE);

                                    /**
                                     * 添加获取所有顶部标签
                                     */

                                    if(object.has("tag_array")){
                                        allTopTagArr.clear();
                                        JSONArray jsonArray_Tag = object.getJSONArray("tag_array");
                                        for(int f=0;f<jsonArray_Tag.length();f++){
                                            TagArr acty=new TagArr();
                                            JSONObject acty_object = jsonArray_Tag.getJSONObject(f);
                                            acty.tagId=acty_object.optString("tag_id");
                                            acty.img=acty_object.optString("img");
                                            acty.tagName=acty_object.optString("tag_name");
                                            if (!allTopTagArr.contains(acty)){
                                                allTopTagArr.add(acty);
                                            }
                                        }
                                        initShowAllTag();
                                    }

                                    JSONArray jsonArray = object.getJSONArray("datas");
                                    ArrayList<localPaiXu2.DatasEntry> ping_p=new ArrayList<localPaiXu2.DatasEntry>();
                                    ping_p.clear();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ArrayList<HuoHangItem> bean=new ArrayList<HuoHangItem>();
                                        bean.clear();
                                        localPaiXu2.DatasEntry item=new localPaiXu2.DatasEntry();
                                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                                        if (jsonobject.has("goods_letter")&&!TextUtils.isEmpty(jsonobject.optString("goods_letter"))) {
                                            item.setGoodsLetter(jsonobject.optString("goods_letter"));
                                        }

                                        HuoHangItem local= new HuoHangItem();
                                        local.goodsId=jsonobject.optString("goods_id");
                                        local.goodsName=jsonobject.optString("goods_name");
                                        local.goodsEnName=jsonobject.optString("goods_en_name");
                                        local.goodsSerial=jsonobject.optString("goods_serial");
                                        local.goodsImage=jsonobject.optString("goods_image");
                                        local.goodsPrice=jsonobject.optDouble("goods_price");
                                        local.goodsStorage=jsonobject.optString("goods_storage");
                                        local.tax_rate=jsonobject.optString("tax_rate");
                                        local.price_hide=jsonobject.optInt("price_hide");
                                        local.tax_includ=jsonobject.optString("tax_includ");

                                        if(jsonobject.has("activity_arr")){
                                            JSONArray activity_arr=jsonobject.optJSONArray("activity_arr");
                                            List<ActivityArr> localActivityList=new ArrayList<>();
                                            localActivityList.clear();
                                            for(int jj=0;jj<activity_arr.length();jj++){
                                                ActivityArr acty=new ActivityArr();
                                                JSONObject acty_object = activity_arr.getJSONObject(jj);
                                                acty.types=acty_object.optString("types");
                                                acty.img=acty_object.optString("img");
                                                acty.activityName=acty_object.optString("activity_name");
                                                localActivityList.add(acty);
                                            }

                                            local.activityArr.addAll(localActivityList);
                                        }

                                        if(jsonobject.has("tag_arr")){
                                            JSONArray tag_arr=jsonobject.optJSONArray("tag_arr");
                                            List<TagArr> localTagList=new ArrayList<>();
                                            localTagList.clear();
                                            for(int f=0;f<tag_arr.length();f++){
                                                TagArr acty=new TagArr();
                                                JSONObject acty_object = tag_arr.getJSONObject(f);
                                                acty.tagId=acty_object.optString("tag_id");
                                                acty.img=acty_object.optString("img");
                                                acty.tagName=acty_object.optString("tag_name");
                                                localTagList.add(acty);
                                            }
                                            local.tagArr.addAll(localTagList);
                                        }

                                        bean.add(local);

                                        item.setGoodsList(bean);
                                        ping_p.add(item);
                                    }

                                    Log.i(TAG, "ping_p"+ping_p.size());

                                    if (ping_p.size() > 0) {
                                        mAdapter.addAll(ping_p);
                                        mAdapter.notifyDataSetChanged();

                                    } else {
                                        ToastUtil.showMessage("还没有数据，过会再来！");
                                    }
                                }else {
                                    if(page==1){
                                        popTiShi("抱歉！没有找到相关的商品。");
                                    }else {
                                        ToastUtil.showMessage("已加载全部数据！");
                                    }
                                    view_null.setVisibility(View.VISIBLE);
                                    mAdapter.stopMore();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showMessage("请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }


    MyListPopAdapter listAdapter;
    MyListRightPopAdapter myRifhtAdapter;

    private boolean isFirstClick=true;
    // 分类
    private void getQuBuData() {
        initPopQu();
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=get_native_class&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=get_native_class&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        SSXaun peiSQuanBuLeft = new Gson().fromJson(msg, SSXaun.class);
                        if (peiSQuanBuLeft.state == 1) {

                            if (peiSQuanBuLeft.datas != null) {
                                if(peiSQuanBuLeft.datas.size() > 0){
                                    listAdapter = new MyListPopAdapter(activity, peiSQuanBuLeft.datas);
                                    listview_pop_left.setAdapter(listAdapter);
                                    listAdapter.setSelectedPosition(0);
                                    listAdapter.notifyDataSetChanged();
                                    try {
                                        SSXaun.DatasEntry dda = (SSXaun.DatasEntry) listAdapter.getItem(0);
                                        if (dda != null) {
                                            initPopRight(dda.gcId);
                                        }
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }else {

                                }
                            }
                        } else {
                            ToastUtil.showMessage("还没有数据哦！");
                        }
                    }
                });
            }
        });

        if (popupWindow != null) {
            popupWindow.showAsDropDown(liner_mim);
        }
    }

    /***
     * 获取店铺列表
     */

    private void getSoreFromN(final ListView listview_getstore,final ListView listview_right, final PopupWindow pop) {
//        http://newapi.tuling.me/huaqiaobang/mobile/index.php?act=native&op=find_member_store&key=2ad1ab26816eba2c28a8f263d1544a02&type=1
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=find_member_store&type=1&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        getStoreFirm item = new Gson().fromJson(msg, getStoreFirm.class);
                        if (item.state == 1) {

                            if (item.datas != null && item.datas.size() > 0) {
                                final PoPGetStoreLeftAdapter poPGetStoreAdapter = new PoPGetStoreLeftAdapter(activity, item.datas);
                                listview_getstore.setAdapter(poPGetStoreAdapter);

                                poPGetStoreAdapter.setSelectedPosition(0);
                                poPGetStoreAdapter.notifyDataSetChanged();

                                try {
                                    getStoreFirm.DatasEntry bean_store = (getStoreFirm.DatasEntry) poPGetStoreAdapter.getItem(0);
                                    if (bean_store != null&&bean_store.storeList!=null) {
                                        if(bean_store.storeList.size()>0){
                                            poPGetStoreRightAdapter.addDatas(bean_store.storeList);
                                        }else {
                                        }
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                listview_getstore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        getStoreFirm.DatasEntry bean = (getStoreFirm.DatasEntry) poPGetStoreAdapter.getItem(position);

                                        if(bean!=null&&bean.storeList!=null){

                                            if(bean.storeList.size()>0){
                                                poPGetStoreRightAdapter.addDatas(bean.storeList);
                                            }else {
                                            }
                                        }
                                    }
                                });

                                listview_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        getStoreFirm.DatasEntry.StoreListEntry item=(getStoreFirm.DatasEntry.StoreListEntry)parent.getItemAtPosition(position);
                                        if (!TextUtils.isEmpty(item.storeName)) {
                                            tvName.setText(item.storeName);
                                        }

                                        if (!TextUtils.isEmpty(item.districtName)) {
                                            tvTitle.setText(item.districtName.substring(0,3));
                                        }

                                        if (!TextUtils.isEmpty(item.districtId)) {
                                            district_id = item.districtId;
                                            address_id=item.addressId;
                                            cart_num=0;
                                            initCarNum();
                                            page = 1;
                                            mAdapter.clear();
                                            idFromChangY=false;
                                            idFromNormal=false;
                                            idFromYouHui=false;
                                            isSearch=false;
                                            idFromPaiXu=true;
                                            loadingPaiXu();
                                            img_paixu.setImageResource(R.drawable.sort_button_down);
                                            tv1.setTextColor(getResources().getColor(R.color.local_ss));
                                            tv2.setTextColor(getResources().getColor(R.color.red));
                                            tv3.setTextColor(getResources().getColor(R.color.local_ss));
                                        }
                                        pop.dismiss();
                                    }
                                });
                            }
                        } else {
                            ToastUtil.showMessage("还没有数据哦！");
                        }
                    }
                });
            }
        });
    }
/*    *//***
     * 获取筛选列表
     *//*

    private void getShaiXaunFromN() {
//    private void getShaiXaunFromN(final MyGridView gridView_hd,final FlowTagLayout gridView_shuxing, final PopupWindow pop) {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=find_goods_filter&key=5dc143e73e4e261a6c801519c1bb0ee8
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=find_goods_filter&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        getShaiXuanFirm item = new Gson().fromJson(msg, getShaiXuanFirm.class);
                        if (item.state == 1) {

                            if (item.datas.activityList != null && item.datas.activityList.size() > 0) {
                                final GirdHDPopAdapter girdHDPopAdapter = new GirdHDPopAdapter(BenDiPeiSongActivity2.this, item.datas.activityList);
                                gridView_hd.setAdapter(girdHDPopAdapter);

                                girdHDPopAdapter.setSelectedPosition(0);
                                girdHDPopAdapter.notifyDataSetChanged();
                                gridView_hd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        girdHDPopAdapter.setSelectedPosition(position);
                                        girdHDPopAdapter.notifyDataSetChanged();

                                        getShaiXuanFirm.DatasBean.ActivityListBean bean = (getShaiXuanFirm.DatasBean.ActivityListBean) girdHDPopAdapter.getItem(position);
                                        if (!TextUtils.isEmpty(bean.activityId)) {

                                            Log.d(TAG, "onItemClick: "+bean.activityId);
                                        }
                                    }
                                });
                            }

                            if (item.datas.tagList != null && item.datas.tagList.size() > 0) {
                                //属性标签
                                TagAdapter mMobileTagAdapter = new TagAdapter<>(BenDiPeiSongActivity2.this);
                                gridView_shuxing.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
                                gridView_shuxing.setAdapter(mMobileTagAdapter);

                                gridView_shuxing.setOnTagSelectListener(new OnTagSelectListener() {
                                    @Override
                                    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                                        if (selectedList != null && selectedList.size() > 0) {


                                            StringBuilder sb = new StringBuilder();

                                            for (int i : selectedList) {
                                                sb.append(parent.getAdapter().getItem(i));
                                                sb.append(":");
                                            }
                                            Log.d(TAG, "onItemSelect: "+sb.toString());
                                        }
                                    }
                                });

                                mMobileTagAdapter.onlyAddAll(item.datas.tagList);

                            }
                        } else {
                            ToastUtil.showMessage("还没有数据哦！");
                        }
                    }
                });
            }
        });
    }*/

    private void initPopRight(String g_id) {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=get_native_class&key=25918f85df03d5ebfa076600519ecd1e&class_id=1161
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=get_native_class&key=" + MyApplication.getInstance().getMykey() + "&class_id=" + g_id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        SSXaun bean = new Gson().fromJson(msg, SSXaun.class);
                        if (bean.state == 1) {

                            if (bean.datas != null && bean.datas.size() > 0) {
                                if (myRifhtAdapter == null) {
                                    myRifhtAdapter = new MyListRightPopAdapter(activity);
                                }

                                gridview_datas.setAdapter(myRifhtAdapter);
                                myRifhtAdapter.addAllData(bean.datas);
                            }
                        }
                        else {
                            ToastUtil.showMessage("还没有数据哦！");
                        }
                    }
                });
            }
        });
    }

    private boolean isShengXu = true;
    @Override
    public void onNumChange(int buyNum, HuoHangItem data) {
        double total_price=0.00;
        if (mAdapter.goodsPri.size() > 0) {
            for (Map.Entry<Integer, Double> entry : mAdapter.goodsPri.entrySet()) {
                total_price+=entry.getValue();
            }
        }

        if (cart_price > 0) {
            tv_peisong_gfw.setText("€"+NumberUtils.formatPriceNo(total_price+cart_price));
        }else {
            tv_peisong_gfw.setText("€"+NumberUtils.formatPriceNo(total_price));
        }

    }

    private void popFanLi(){
        View root_view=View.inflate(activity,R.layout.pop_notices_msg2,null);
        TextView img_close=(TextView) root_view.findViewById(R.id.imge_close);
        com.abcs.haiwaigou.view.AlignTextView tv_msg=(com.abcs.haiwaigou.view.AlignTextView) root_view.findViewById(R.id.tv_msg);

        tv_msg.setText("此金额将在您下月的首单中抵扣，如下月首单未扣完，则在接下来的订单中继续扣除，扣完为止。如还未扣完则在下个月继续。有效被抵扣的订单将按抵扣前的总金额参加返现活动。");

        final PopupWindow popupWindow= new PopupWindow();
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(root_view);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params =activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        //点击返回键popupwindown消失

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
    @OnClick({R.id.relative_search,R.id.relative_back,R.id.tv_name, R.id.tv_xiala, R.id.tv_title, R.id.lintive_setting,R.id.iv_setting,R.id.relat_tv,R.id.relative_no_city,R.id.t_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t_select: // 重新加载网络
            case R.id.relative_no_city: // 重新加载网络
                if(ServerUtils.isConnect(activity)){
                    relative_no_city.setVisibility(View.GONE);

                    checkIsInto();/// 判断是否有店铺
                    getQuBuData();
                    getUnReadMsg();  // 初始未读信息数

                }else {
                    relative_no_city.setVisibility(View.VISIBLE);
                    ToastUtil.showMessage("请检查您的网络！");
                }
                break;
            case R.id.relative_search: //取消
                textView2.setText("");
                break;
            case R.id.relat_tv: //返利弹窗
                if(is_LocalMember==1){ /*不是货行用户*/
                    return;
                }else {
                    popFanLi();
                }

                break;
            case R.id.relative_back: //隐藏搜索框
                liner_title.setVisibility(View.VISIBLE);
                reSearchTop.setVisibility(View.GONE);
                break;
            case R.id.tv_name:
//                getStoreFirm();
                break;
            case R.id.tv_xiala:
                break;
            case R.id.tv_title:
                break;
            case R.id.lintive_setting:
            case R.id.iv_setting:
                if(is_LocalMember==1){ // 不是货行用户
//                    ToastUtil.showMessage("您还不是货行用户，请联系客户！");
                    return;
                }else {
                    settingPop();
                }
                break;
            case R.id.re_qubu: // 全部

                if(isFirstClick){
                    getQuBuData();
                    isFirstClick=false;
                }

                if (popupWindow != null) {
                    popupWindow.showAsDropDown(liner_mim);
                }

                order_key="";
                img_paixu.setImageResource(R.drawable.sort_button);
                tv1.setTextColor(getResources().getColor(R.color.red));
                tv2.setTextColor(getResources().getColor(R.color.local_ss));
                tv3.setTextColor(getResources().getColor(R.color.local_ss));

                break;
            case R.id.re_paixu: // 排序

                if (isShengXu) {
                    page = 1;
                    order = "2";
                    mAdapter.clear();
                    isShengXu = false;
                    img_paixu.setImageResource(R.drawable.sort_button_up);

                } else {
                    page = 1;
                    order = "1";
                    mAdapter.clear();
                    isShengXu = true;
                    img_paixu.setImageResource(R.drawable.sort_button_down);
                }
                ll_top_line.setVisibility(View.GONE);
                isClickTag=false;
                peiSongTopLineTagAdapter.clearAddDatas();// 清空顶部tag集合
                idFromChangY=false;
                idFromYouHui=false;
                idFromNormal=false;
                isSearch=false;
                idFromPaiXu=true;
                isfirst=true;
                loadingPaiXu();
                tv1.setTextColor(getResources().getColor(R.color.local_ss));
                tv2.setTextColor(getResources().getColor(R.color.red));
                tv3.setTextColor(getResources().getColor(R.color.local_ss));
                break;
            case R.id.re_xxuan: // 优惠

                ll_top_line.setVisibility(View.GONE);
                isClickTag=false;
                peiSongTopLineTagAdapter.clearAddDatas();// 清空顶部tag集合
                idFromYouHui=true;
                idFromNormal=false;
                idFromChangY=false;
                isSearch=false;
                idFromPaiXu=false;
                page=1;
                mAdapter.clear();
                order_key="";
                img_paixu.setImageResource(R.drawable.sort_button);
                tv1.setTextColor(getResources().getColor(R.color.local_ss));
                tv2.setTextColor(getResources().getColor(R.color.local_ss));
                tv3.setTextColor(getResources().getColor(R.color.red));
                loading();
                break;
            case R.id.iv_search: // 搜索
                Intent it=new Intent(activity, BenDiSearchActivity.class);
                startActivityForResult(it,900);  // 搜索返回

               /* mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                liner_title.setVisibility(View.GONE);
                reSearchTop.setVisibility(View.VISIBLE);
                isSearch=true;
                initSearch();*/
                break;
        }
    }

    String store_id = "11";

    private static final String TAG = "zds";


    /****
     * 获取商铺的列表
     */
    PoPGetStoreRightAdapter poPGetStoreRightAdapter;
    PopupWindow popupWindow_store;
    private void getStoreFirm() {
        View itemView = View.inflate(activity, R.layout.popup_bendi_getstore, null);
        ListView listview_getstore = (ListView) itemView.findViewById(R.id.listview);
        ListView listview_right = (ListView) itemView.findViewById(R.id.listview_right);

        popupWindow_store = new PopupWindow(itemView, Util.WIDTH, Util.HEIGHT * 3 / 5);
        popupWindow_store.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        popupWindow_store.setTouchable(true);
        //聚集
        popupWindow_store.setFocusable(true);
        //设置允许在外点击消失
        popupWindow_store.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow_store.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow_store.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow_store.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow_store.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        poPGetStoreRightAdapter = new PoPGetStoreRightAdapter(activity);
        listview_right.setAdapter(poPGetStoreRightAdapter);
        if(popupWindow_store!=null){
            popupWindow_store.showAsDropDown(tvName);
        }
        getSoreFromN(listview_getstore,listview_right, popupWindow_store);
    }

/*    *//****
     * 获取筛选的列表
     *//*
    PopupWindow pop;
    private void getShuaiXuanFirm() {

        View itemView = View.inflate(this, R.layout.popup_bendi_getshaixuan, null);
        MyGridView gridHuodong = (MyGridView) itemView.view.findViewById(R.id.grid_huodong);
        FlowTagLayout grid_shuxing = (FlowTagLayout) itemView.view.findViewById(R.id.grid_shuxing);

         pop = new PopupWindow(itemView, Util.WIDTH , Util.HEIGHT * 3 / 5);
        pop.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        pop.setTouchable(true);
        //聚集
        pop.setFocusable(true);
        //设置允许在外点击消失
        pop.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
//        params.alpha = 0.5f;
        params.alpha = 1f;
        activity.getWindow().setAttributes(params);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        getShaiXaunFromN(gridHuodong,grid_shuxing, pop);
    }*/
    /****
     * 获取设置的列表
     */
    private void settingPop() {

        View itemView = View.inflate(activity, R.layout.popup_bendi_setting, null);
        LinearLayout tv_mystore = (LinearLayout) itemView.findViewById(R.id.line_mystore);
        LinearLayout tv_myorder = (LinearLayout) itemView.findViewById(R.id.line_dingdan);
        LinearLayout tv_mynormal = (LinearLayout) itemView.findViewById(R.id.line_changyong);
        LinearLayout line_messge = (LinearLayout) itemView.findViewById(R.id.line_messge);

        final PopupWindow pop = new PopupWindow(activity);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setContentView(itemView);

        pop.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        pop.setTouchable(true);
        //聚集
        pop.setFocusable(true);
        //设置允许在外点击消失
        pop.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        pop.showAsDropDown(ivSetting);

        tv_mystore.setOnClickListener(new View.OnClickListener() {  // 我的店铺列表
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(activity,HuoHangAddressActivity.class);
                intent.putExtra("formSetting",true);
                startActivity(intent);
                pop.dismiss();
            }
        });
        tv_myorder.setOnClickListener(new View.OnClickListener() {  // 我的订单
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HuoHangOrderActivity.class);
//                intent.putExtra("position", 0);
                startActivity(intent);
                pop.dismiss();
            }
        });
        tv_mynormal.setOnClickListener(new View.OnClickListener() { //  我的常用
            @Override
            public void onClick(View v) {

                ll_top_line.setVisibility(View.GONE);
                isClickTag=false;
                peiSongTopLineTagAdapter.clearAddDatas();// 清空顶部tag集合
                idFromChangY=true;
                idFromNormal=false;
                idFromYouHui=false;
                isSearch=false;
                idFromPaiXu=false;
                page=1;
                mAdapter.clear();
                loading();
                pop.dismiss();
            }
        });
        line_messge.setOnClickListener(new View.OnClickListener() { //  我的消息
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NoticeActivity.class);
                startActivity(intent);
                pop.dismiss();
            }
        });

    }

    /***
     * 加入购物车
     */
    private void addToCart() {

        /*判断商品*/
        StringBuilder goods_ids = new StringBuilder();
        StringBuilder quantity_1 = new StringBuilder();

        if (mAdapter.datas.size() > 0) {
            for (Map.Entry<Integer, HuoHangItem> entry : mAdapter.datas.entrySet()) {

                goods_ids.append(entry.getValue().goodsId);
                goods_ids.append(",");
                quantity_1.append(entry.getValue().numbers);
                quantity_1.append(",");

            }
        }

        Log.d(TAG, "addToCart: " + goods_ids);
        Log.d(TAG, "addToCart: " + quantity_1);

//        if(!TextUtils.isEmpty(goods_ids)&&!TextUtils.isEmpty(quantity_1)){
        HttpRequest.sendPost(TLUrl.getInstance().URL_bendi_addche, "goods_id=" + goods_ids + "&quantity=" + quantity_1 + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("addche", msg + "");

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        } else {

                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 200) {
                                    initAdapterNoData();
                                    Intent intent = new Intent(activity, HuoHangCartActivity.class);
                                    intent.putExtra("store_id", store_id);
                                    startActivity(intent);
                                } else {
                                    Log.i("zds", "add:解析失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private void initAdapterNoData(){
        mAdapter.datas.clear();
        mAdapter.goodsNumMap.clear();
        mAdapter.goodsPri.clear();
        mAdapter.buyNum=0;
        cart_num=0;
        mAdapter.notifyDataSetChanged();
        initCarNum();
    }
    private String cart_id;

    private void jieSuan() {

            StringBuilder goods_ids_jie = new StringBuilder();
            StringBuilder quantity_1_jie = new StringBuilder();

        if (mAdapter.datas.size() > 0) {

            for (Map.Entry<Integer, HuoHangItem> entry : mAdapter.datas.entrySet()) {
                goods_ids_jie.append(entry.getValue().goodsId);
                goods_ids_jie.append(",");
                quantity_1_jie.append(entry.getValue().numbers);
                quantity_1_jie.append(",");
            }
        }

        Log.d(TAG, "addToCart_j: " + goods_ids_jie);
        Log.d(TAG, "addToCart_j: " + quantity_1_jie);

        HttpRequest.sendPost(TLUrl.getInstance().URL_bendi_jiesuan, "goods_id=" + goods_ids_jie + "&quantity=" + quantity_1_jie + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("jie", msg + "");

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        } else {

                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.optInt("code") == 200) {

                                    initAdapterNoData();
                                    JSONObject datas = object.optJSONObject("datas");
                                    if(datas.has("state")){

                                        if(datas.getBoolean("state")){  //正常的流程

                                            if(datas.has("data")){
                                                JSONObject data = datas.optJSONObject("data");
                                                if(data.has("store_cart_list")){
                                                    JSONObject store_cart_list = data.optJSONObject("store_cart_list");
                                                    if(store_cart_list.has("11")){
                                                        JSONArray jsonArray = store_cart_list.optJSONArray("11");

                                                /*判断商品是否可以买*/
                                                        StringBuffer srtbuffer = new StringBuffer();
                                                        String storeTemp;
                                                        ArrayList<String> strings = new ArrayList<String>();
                                                        HuoHangCheckOrderActivity.stores.clear();

                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                                            if (!object1.optBoolean("state")) {
                                                                Toast.makeText(activity, "所选商品中[" + object1.optString("goods_name").substring(0, 12) + "]已经下架，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                                return;
                                                            }
                                                            if (!object1.optBoolean("storage_state")) {
                                                                Toast.makeText(activity, "所选商品中[" + object1.optString("goods_name").substring(0, 12) + "]库存不足，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                                return;
                                                            }

                                                            StringBuffer buffer = new StringBuffer();
                                                            storeTemp=object1.optString("store_id");

                                                            if (!strings.contains(storeTemp)) {
                                                                HuoHangCheckOrderActivity.stores.add(storeTemp);
                                                            }

                                                            buffer.append("&cart_id[" + i + "]=");
                                                            buffer.append(object1.optString("cart_id") + "|" + object1.optString("goods_num"));
                                                            srtbuffer.append(buffer);

                                                            strings.add(object1.optString("store_id"));

                                                            cart_id = srtbuffer.toString();

                                                        }

                                                        Log.i("zds", "store_ids=" + HuoHangCheckOrderActivity.stores.size());
                                                        Log.i("zds", "cart_id=" + cart_id);
                                                        Intent intent=null;
                                                        intent = new Intent(activity, HuoHangCheckOrderActivity.class);
                                                        intent.putExtra("cart_id",cart_id);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        }else {  //有商品已经下架，提示用户先删除已经下架的商品

                                            ToastUtil.showMessage("购物车里的东西太久了，有商品已经下架，请删除购物车中下架的商品，再重新购买！");
                                        }

                                    }
                                } else {
                                    Log.i("zds", "add:解析失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private HashMap<String, Integer> letterIndexes;

    private String[] sections;

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    String keyword;
    private boolean serFirst=false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 900 && resultCode == 1 && data != null) {

            boolean search=data.getBooleanExtra("search",false);
            keyword=data.getStringExtra("search_keyword");

            Log.i(TAG, "onActivityResult: search"+search);
            Log.i(TAG, "onActivityResult: keyword"+keyword);
            if(search){
                currentPage=1;
                mAdapter.clear();
                isSearch=true;
                idFromNormal=false;
                idFromChangY=false;
                idFromYouHui=false;
                idFromPaiXu=false;
                serFirst=true;// 第一次显示提示

                img_paixu.setImageResource(R.drawable.sort_button);
                tv1.setTextColor(getResources().getColor(R.color.local_ss));
                tv2.setTextColor(getResources().getColor(R.color.local_ss));
                tv3.setTextColor(getResources().getColor(R.color.local_ss));

                initSearch();
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBroadCastReceiver.unRegister();
        EventBus.getDefault().unregister(this);
        ButterKnife.reset(this);
    }

    private boolean isUpdata=false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDatas(String updata){

        LogUtil.i("zds",updata);
        if(updata.length()>0&&updata.equals("readmsg")){
            isUpdata=true;
            getUnReadMsg();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeChange(final Notice notice){
        Log.d("zds", "onNoticeChange:"+notice.googNum);
        Log.d("zds", "onNoticeChange:"+notice.googsToalPrice);

        if (notice.googNum > 0) {
            tv_peisong_number.setText(notice.googNum + "");
//            tv_peisong_number.setVisibility(View.VISIBLE);
            img_peisong_che.setImageResource(R.drawable.bg_bottom_psong_che2);
            tv_peisong_qisong.setText("选好了");
            tv_peisong_gfw.setText("€"+NumberUtils.formatPriceNo(notice.googsToalPrice));
            re_peisong_jie.setBackgroundResource(R.drawable.bg_bottom_psong_jie);
        } else {
            tv_peisong_number.setText(notice.googNum + "");
//            tv_peisong_number.setVisibility(View.GONE);
            img_peisong_che.setImageResource(R.drawable.bg_bottom_psong_che2);
            tv_peisong_qisong.setText("€0.00");
            tv_peisong_gfw.setText("购物车为空");
            re_peisong_jie.setBackgroundResource(R.drawable.bg_bottom_psong_jie_no);
        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void initIntentData() {
    }

    @Override
    public void initListener() {
    }
}
