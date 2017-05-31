package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.model.find.QiangGou;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.hqbtravel.adapter.HWGHdongAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 海外购 活动 限时抢购
 */
public class HWGHDQiangFragment extends BaseFragment implements View.OnClickListener{

    Activity activity;
    
    @InjectView(R.id.rv_hdong)
    EasyRecyclerView rvHdong;
    
    private View view;
    String type;


    public static HWGHDQiangFragment newInstance(String type, String time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("time", time);
        HWGHDQiangFragment hireJobFragment = new HWGHDQiangFragment();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.fragemt_h_dong, null);
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
            type = bundle.getString("type");
            time = bundle.getString("time");
        }
        initRecycler();
        return view;
    }

    HWGHdongAdapter adapter;
    ImageView img;
    TextView tv_tag;
    private void initRecycler() {

        rvHdong.setLayoutManager(new LinearLayoutManager(activity));
//        rvHdong.setRefreshListener(this);
        adapter = new HWGHdongAdapter(activity);

        final View top_view = View.inflate(activity, R.layout.item_hwg_hd_qiang, null);


        img=(ImageView) top_view.findViewById(R.id.img); // logo
        tv_tag=(TextView) top_view.findViewById(R.id.tv_tag); // logo

        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return top_view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        rvHdong.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                QiangGou.DatasEntry.GoodsListEntry itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(activity, GoodsDetailActivity.class);
                it.putExtra("sid", itemsEntity.goodsId);
                it.putExtra("pic", itemsEntity.goodsImage);
                it.putExtra("store_id", "");
                it.putExtra("isYun", false);
                getContext().startActivity(it);
            }
        });

//        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
//        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.resumeMore();
//            }
//        });

        getDatas();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private int pageNo=1;
    private boolean isRefresh=true;
//    @Override
//    public void onRefresh() {
//        pageNo = 1;
//        isRefresh = true;
//        getDatas();
//    }

    @Override
    public void onClick(View v) {

    }

    String time;
    Handler handler=new Handler();


    private void getDatas() {

//        http://www.huaqiaobang.com/mobile/index.php?act=activity&op=flash_sale_list&times=1491012000
        ProgressDlgUtil.showProgressDlg("",activity);
        Log.i("zds", "run: time"+time);

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=activity&op=flash_sale_list&times=" +time, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        if(TextUtils.isEmpty(msg)){
                            return;
                        }else {

                            Log.i("zds", "run: lazyLoad"+msg);
                            QiangGou bena=new Gson().fromJson(msg,QiangGou.class);

                            if(bena!=null){
                                if(bena.state==1){
                                    if(bena.datas!=null){

                                        if(!TextUtils.isEmpty(bena.datas.img)){  //  头部
                                            MyApplication.imageLoader.displayImage(bena.datas.img, img, MyApplication.getListOptions());
                                        }

                                        if(bena.datas.goodsList!=null){  // 列表

                                            if(isRefresh){
                                                adapter.clear();
                                                isRefresh=false;
                                            }

                                            if(bena.datas.goodsList.size()>0){
                                                adapter.addAll(bena.datas.goodsList);
                                            }else {
                                                adapter.stopMore();
                                            }
                                        }
                                    }
                                }else {
                                    adapter.stopMore();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

//    @Override
//    public void onLoadMore() {
//        if (pageNo == -1) {
//            adapter.stopMore();
//            return;
//        }
//    }
}
