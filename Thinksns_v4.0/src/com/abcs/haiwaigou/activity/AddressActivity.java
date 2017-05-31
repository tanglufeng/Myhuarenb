package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.AddressRecyclerAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Addres;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddressActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {


    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    //    @InjectView(R.id.allGoods_content_fragment)   对对对
//    FrameLayout allGoodsContentFragment;
//    @InjectView(R.id.t_add)
//    TextView tAdd;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    public Handler handler = new Handler();
    @InjectView(R.id.tv_add_address)
    TextView tvAddAddress;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Addres> addressList = new ArrayList<Addres>();
    MyBroadCastReceiver myBroadCastReceiver;

    AddressRecyclerAdapter addressRecyclerAdapter;
    FullyLinearLayoutManager fullyLinearLayoutManager;
    boolean isBuy;
    boolean isYYG;
    boolean first = false;
    private View view;
    HashMap<Integer, Boolean> isSelect = new HashMap<Integer, Boolean>();
    HashMap<Integer, String> isMoren = new HashMap<Integer, String>();
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;

    public String store_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_address, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        store_id=getIntent().getStringExtra("store_id");
        isBuy = getIntent().getBooleanExtra("isBuy", false);
        isYYG = getIntent().getBooleanExtra("isYYG", false);
//        initListView();
        initRecyclerView();
        setOnListener();
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
            if (intent.getSerializableExtra("type").equals(MyUpdateUI.ADDRESS)) {
                Log.i("zjz", "更新收货地址");
//                initListView();
                initRecyclerView();
            }
        }

        @Override
        public void update(Intent intent) {

        }

    };


    private void initRecyclerView() {
        addressRecyclerAdapter = new AddressRecyclerAdapter(this, isBuy,isYYG);
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);

        recyclerView.setFocusable(false);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
//        recyclerView.setAdapter(addressRecyclerAdapter);
        initAllDates();
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, addressRecyclerAdapter, this, MyString.TYPE0);
        //添加分割线
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size4);
//        weekRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
//        tAdd.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
    }

//    private void initListView() {
//        initAllDates();
//
//        // // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
//        // float density = getResources().getDisplayMetrics().density;
//        // listView.setFirstTopOffset((int) (50 * density));
//
//        tljrZListView.setFooterDividersEnabled(false);
//        // listView.setSelector(R.drawable.tljr_listview_selector);
//        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
//        SimpleHeader header = new SimpleHeader(this);
//        header.setTextColor(0xffeb5041);
//        header.setCircleColor(0xffeb5041);
//        tljrZListView.setHeadable(header);
//
//        // 设置加载更多的样式（可选）
//        SimpleFooter footer = new SimpleFooter(this);
//        footer.setCircleColor(0xffeb5041);
//        tljrZListView.setFootable(footer);
//
//        tljrZListView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!MyApplication.getInstance().checkNetWork()) {
//                            tljrZListView.setRefreshFail("网络无连接，请检查网络");
//
//                        } else {
//                            tljrZListView.setRefreshSuccess("刷新成功");
//                            initAllDates();
//                        }
//                    }
//                }, 2 * 1000);
//
//            }
//        });
//
//        tljrZListView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!MyApplication.getInstance().checkNetWork()) {
//                            tljrZListView.setRefreshFail("网络无连接，请检查网络");
//
//                        } else {
//                            tljrZListView.setLoadMoreSuccess();
//                            initAllDates();
//                        }
//                    }
//                }, 2 * 1000);
//
//            }
//        });
//    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_address_list, "key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.i("zjz", "code=" + code);
                                Log.i("zjz", msg);
                                if (code == 200) {
                                    addressList.clear();
                                    addressRecyclerAdapter.getDatas().clear();
                                    JSONObject object = json.getJSONObject("datas");
                                    JSONArray jsonArray = object.getJSONArray("address_list");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        Addres a = new Addres();
                                        a.setAddress_id(object1.optString("address_id"));
                                        a.setTrue_name(object1.optString("true_name"));
                                        a.setPhone(object1.optString("mob_phone"));
                                        a.setT_phone(object1.optString("tel_phone"));
                                        a.setArea_info(object1.optString("area_info"));
                                        a.setDetail_address(object1.optString("address"));
                                        a.setArea_id(object1.optString("area_id"));
                                        a.setCity_id(object1.optString("city_id"));
                                        a.setIs_default(object1.optString("is_default"));
                                        a.setId_card(object1.optString("id_card"));
                                        addressList.add(a);
                                        addressRecyclerAdapter.getDatas().add(a);
//                                        Log.i("zjz", "isTrue=" + a.getAddress_id().equals(Util.preference.getString("address_id", "")));
//                                        if (Util.preference != null && a.getAddress_id().equals(Util.preference.getString("address_id", ""))) {
//                                            isSelect.put(i, true);
//                                            isMoren.put(i, "默认地址");
//                                        } else {
//                                            isSelect.put(i, false);
//                                            isMoren.put(i, "设置默认");
//                                        }
                                    }
                                    if (layoutNull != null)
                                        layoutNull.setVisibility(addressRecyclerAdapter.getDatas().size() == 0 ? View.VISIBLE : View.GONE);
//                                    addressAdapter = new AddressAdapter(AddressActivity.this, addressList, tljrZListView, isBuy, isSelect, isMoren);
//                                    tljrZListView.setAdapter(addressAdapter);

                                    addressRecyclerAdapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                } else {
                                    showToast("失败咯！");
                                    ProgressDlgUtil.stopProgressDlg();
                                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", "登录失败！");
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });
            }


        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
//            case R.id.t_add:
//                intent = new Intent(this, EditAddressActivity.class);
//                intent.putExtra("title", "新增收货地址");
//                intent.putExtra("isEdit", false);
//                startActivity(intent);
//                break;
            case R.id.tv_add_address:
                if (addressList.size() == 20) {
                    showToast("最多只能拥有20个有效地址！");
                } else {
                    intent = new Intent(this, EditAddressActivity.class);
                    intent.putExtra("title", "新增收货地址");
                    intent.putExtra("isEdit", false);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        initAllDates();
    }
}
