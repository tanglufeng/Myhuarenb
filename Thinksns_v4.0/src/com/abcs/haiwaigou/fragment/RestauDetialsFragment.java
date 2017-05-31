package com.abcs.haiwaigou.fragment;

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
import android.widget.TextView;

import com.abcs.haiwaigou.local.activity.RestauraActivity;
import com.abcs.haiwaigou.local.adapter.RestaurenAdapter;
import com.abcs.haiwaigou.local.beans.RestauDetisl;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.abcs.huaqiaobang.tljr.news.NewsActivity.adapter;


public class RestauDetialsFragment extends BaseFragment implements  RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {


    RestauraActivity activity;

    @InjectView(R.id.rv_restust)
     EasyRecyclerView rvList;

    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    private int pageNo=1;
    private boolean isRefresh = false;

    RestaurenAdapter mAdapter;
    View view;

    String categoryId;

    public static RestauDetialsFragment newInstance(String categoryId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryId", categoryId);
        RestauDetialsFragment hireJobFragment = new RestauDetialsFragment();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (RestauraActivity) getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_restaudetials_fragment, null);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getString("categoryId");
        }

        if(!TextUtils.isEmpty(categoryId)){
            lazyLoad();
        }

        return view;
    }

    Handler handler=new Handler();

    @Override
    protected void lazyLoad() {
        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rvList.setRefreshListener(this);
        rvList.setAdapter(mAdapter = new RestaurenAdapter(activity));
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

//                RestauransBean itemsEntity = adapter.getAllData().get(position);
//                Intent it = new Intent(BiChiActivity.this, BiChiDetialsActivity.class);
//                it.putExtra("chiId", itemsEntity.id);
//                it.putExtra("cityId", cityId);
//                it.putExtra("chiphoto", itemsEntity.photo);
//                startActivity(it);
            }
        });

//        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
        mAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.resumeMore();
            }
        });

        HttpRequest.sendGet(TLUrl.getInstance().URL_local_rest_fenqu_detisal, "categoryId=" + categoryId , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            Log.i("zds", "msg=" + msg);
                            ProgressDlgUtil.stopProgressDlg();
                            if (isRefresh) {
                                mAdapter.clear();
                                isRefresh = false;
                            }
                            if(!TextUtils.isEmpty(msg)){

                                RestauDetisl data= new Gson().fromJson(msg, RestauDetisl.class);

                                if(!TextUtils.isEmpty(data.status)){
                                    if(data.status.equals("1")){
                                        if( data.msg!=null){

                                            if( data.msg.size()>0){
                                                mAdapter.addAll(data.msg);
                                            }else {
                                                showToast(activity,"暂无数据！");
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
//                                        pageNo = response.body.next;
                                    }
                                }
                            }else {
                                showToast(activity,"请求失败！请重试！");
                            }
                    }
                });
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
