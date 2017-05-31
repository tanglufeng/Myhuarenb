package com.abcs.haiwaigou.local.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.activity.HireAndFindActivity;
import com.abcs.haiwaigou.local.activity.RestauraActivity;
import com.abcs.haiwaigou.local.adapter.NewHireJobAdapter;
import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;


public class NewHireJobFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    HireAndFindActivity activity;

    EasyRecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefreshLayout;
    TextView tRefresh;
    RelativeLayout layoutNull;
    TextView tSend;
    private View view;
    String menuId,oLd_menuId;
    String cityId;
    String typeName,title;

    private int pageNo=1;
    private boolean isRefresh = false;


    public static NewHireJobFragment newInstance(String cityId, String menuId, String typeName, String oLd_menuId,String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("menuId", menuId);
        bundle.putSerializable("typeName", typeName);
        bundle.putSerializable("oLd_menuId", oLd_menuId);
        bundle.putSerializable("title", title);
        NewHireJobFragment hireJobFragment = new NewHireJobFragment();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HireAndFindActivity) getActivity();
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
//        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        tRefresh=(TextView)view.findViewById(R.id.t_refresh);
        tSend=(TextView)view.findViewById(R.id.t_send);
        layoutNull=(RelativeLayout)view.findViewById(R.id.layout_null);

        initRecycler();

 /*       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        recyclerView.setAdapter(adapter = new NewHireJobAdapter(activity));

//        adapter.setNoMore(R.layout.view_nomore);
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

                Log.e("zds_areasname", mNewHire.areas.size()+"");

                Intent intent = new Intent(activity, RestauraActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("cate_hire",mNewHire);
                intent.putExtra("menuId",menuId);
                activity.startActivity(intent);

            }
        });


    }

    Gson mGson=null;

    Handler mHandler=new Handler();


    private void initAllDates() {
        ProgressDlgUtil.showProgressDlg("Loading...", activity);

//        https://japi.tuling.me/hrq/conListDetail/getConListByConType.json?cityId=6561&menuId=8010&version=v1.0
        Log.i("zds","hire_url="+ TLUrl.getInstance().URL_local_get_menuList+"?"+"cityId="+cityId+"&page="+pageNo+"&pageSize=10"+"&menuId="+menuId+"&version=v1.0");
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
