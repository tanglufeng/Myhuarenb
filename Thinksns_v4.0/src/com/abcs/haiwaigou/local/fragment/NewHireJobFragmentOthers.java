package com.abcs.haiwaigou.local.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.GridVImgsAdapter;
import com.abcs.haiwaigou.local.activity.HireAndFindActivityOthers;
import com.abcs.haiwaigou.local.activity.OthersActivity;
import com.abcs.haiwaigou.local.adapter.NewHireJobAdapter;
import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.abcs.haiwaigou.model.GGAds;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.GuanggaoActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.weiba.ActivityWeibaCommon;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;


public class NewHireJobFragmentOthers extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    HireAndFindActivityOthers activity;

    EasyRecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefreshLayout;
    TextView tRefresh;
    RelativeLayout layoutNull;
    TextView tSend;
    private View view;
    String menuId,oLd_menuId;
    String cityId;
    String typeName,title;

    com.abcs.haiwaigou.view.MyGridView  gridView;
//    ImageView ivGgNew2;
//    ImageView ivGgNew3;
//    ImageView ivGgNew4;
//
//    ArrayList<ImageView> adsList=new ArrayList<>();


    private int pageNo=1;
    private boolean isRefresh = false;


    public static NewHireJobFragmentOthers newInstance(String cityId, String menuId, String typeName, String oLd_menuId, String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("menuId", menuId);
        bundle.putSerializable("typeName", typeName);
        bundle.putSerializable("oLd_menuId", oLd_menuId);
        bundle.putSerializable("title", title);
        NewHireJobFragmentOthers hireJobFragment = new NewHireJobFragmentOthers();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HireAndFindActivityOthers) getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_yyg_fragment_goods2, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = bundle.getString("cityId");
            menuId = bundle.getString("menuId");
            oLd_menuId = bundle.getString("oLd_menuId");
            typeName = bundle.getString("typeName");
            title = bundle.getString("title");
        }

        recyclerView=(EasyRecyclerView)view.findViewById(R.id.recyclerView);
        final View itemView = activity.getLayoutInflater().inflate(R.layout.local_gg_item_fourr, null);
        gridView=(com.abcs.haiwaigou.view.MyGridView)itemView.findViewById(R.id.gridView);
//        ivGgNew2=(ImageView)itemView.findViewById(R.id.iv_gg_new2);
//        ivGgNew3=(ImageView)itemView.findViewById(R.id.iv_gg_new3);
//        ivGgNew4=(ImageView)itemView.findViewById(R.id.iv_gg_new4);

//        adsList.clear();
//        adsList.add(ivGgNew1);
//        adsList.add(ivGgNew2);
//        adsList.add(ivGgNew3);
//        adsList.add(ivGgNew4);
        adapter = new NewHireJobAdapter(activity);
        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return itemView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
//        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        tRefresh=(TextView)view.findViewById(R.id.t_refresh);
        tSend=(TextView)view.findViewById(R.id.t_send);
        layoutNull=(RelativeLayout)view.findViewById(R.id.layout_null);
        initRecycler();
/*        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecycler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },200);
            }
        });*/
        tRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecycler();
                recyclerView.setRefreshing(false);
            }
        });
        tSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.go2Publish();
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void lazyLoad() {


    }

    private NewHireJobAdapter adapter;

    private void initRecycler() {

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter);

//        adapter.setNoMore(R.layout.view_nomore);this
//        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });


        initAllDates();

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                final NewHireFind.MsgBean mNewHire=(NewHireFind.MsgBean) adapter.getAllData().get(position);

                Intent intent = new Intent(activity, OthersActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("menuId",menuId);
                intent.putExtra("cityId",mNewHire.qyerCityId+"");
                activity.startActivity(intent);

            }
        });


    }

    Gson mGson=null;

    Handler mHandler=new Handler();


    private void initAllDates() {
        ProgressDlgUtil.showProgressDlg("Loading...", activity);

//        https://japi.tuling.me/hrq/conListDetail/getConListByConType.json?cityId=6561&menuId=8010&version=v1.0
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "cityId="+cityId+"&menuId="+menuId+"&version=v1.0", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        ProgressDlgUtil.stopProgressDlg();

                        Log.i("zds", "hire_msg=" + msg);

                        if (isRefresh) {
                            adapter.clear();
                            isRefresh = false;
                        }


                        NewHireFind mBean=new Gson().fromJson(msg,NewHireFind.class);
                        if(mBean!=null){

                            if(mBean.status.equals("1")){

                                if(mBean.msg!=null){

                                    if(mBean.msg.size()>0){
                                        layoutNull.setVisibility(View.GONE);
                                        adapter.addAll(mBean.msg);
                                    }else {
                                        adapter.stopMore();
                                        layoutNull.setVisibility(View.VISIBLE);
                                        if(tSend!=null)
                                            tSend.setVisibility(View.VISIBLE);
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                if(mBean.ads!=null){  // 广告
                                    if(mBean.ads.size()>0){
                                        gridView.setAdapter(new GridVImgsAdapter(activity,mBean.ads));
                                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                GGAds gg_bean=(GGAds) parent.getItemAtPosition(position);
                                                if(gg_bean!=null){

                                                    if(!TextUtils.isEmpty(gg_bean.url)){
                                                        if(gg_bean.is_jump==1){  // 跳微吧
                                                            Intent intent;
                                                            if (TextUtils.isEmpty(MyApplication.getInstance().getMykey())) {
                                                                intent = new Intent(activity, WXEntryActivity.class);
                                                                intent.putExtra("isthome",true);
                                                            } else {
                                                                intent = new Intent(activity, ActivityWeibaCommon.class);
                                                                intent.putExtra("name", "全部微吧");
                                                                intent.putExtra("type", ActivityWeibaCommon.FRAGMENT_WEIBA_ALL);
                                                            }
                                                            activity.startActivity(intent);
                                                        }else {
                                                            Intent intent = new Intent(activity, GuanggaoActivity.class);
                                                            intent.putExtra("url_local", gg_bean.url);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                  /*      for (int i = 0; i < mBean.ads.size(); i++) {
                                            if (i < 4) {
                                                adsList.get(i).setVisibility(View.VISIBLE);
                                                final GGAds bannersBean = mBean.ads.get(i);
                                                MyApplication.imageLoader.displayImage(bannersBean.img, adsList.get(i), MyApplication.getListOptions());
                                                adsList.get(i).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(activity, GuanggaoActivity.class);
                                                        intent.putExtra("url_local", bannersBean.url);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }*/
                                    }else {
                                      /*  for (int i = 0; i < adsList.size(); i++) {
                                            adsList.get(i).setVisibility(View.GONE);
                                        }*/
                                    }
                                }
                            }else {
                                showToast(activity,"数据出错！");
                            }
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onLoadMore() {
        pageNo=pageNo+1;
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        initAllDates();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
