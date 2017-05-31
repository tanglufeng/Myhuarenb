package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.local.adapter.HWGWanLeDetialAdapter;
import com.abcs.haiwaigou.local.beans.HWGWanLe;
import com.abcs.haiwaigou.local.beans.HWGWanLeDeTials;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainFragmentWanLe extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{
    @Override
    protected void lazyLoad() {

    }

    Activity activity;


    String objectName,plate_id;
    ListView listview;
    EasyRecyclerView rvList;

    private int pageNo=1;

    HWGWanLeDetialAdapter mAdapter;


    private View view;
    boolean isRefresh = false;

    int picWith;
    int picHeight;
    int picHeight2;

Handler handler=new Handler();

    private void getLeftDatas(String plate_id){
        //        http://www.huaqiaobang.com/mobile/index.php?act=travel&op=travel_city&country_id=215

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_wangles_left, "act=travel&op=travel_city&country_id=" + plate_id , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);

                        if(!TextUtils.isEmpty(msg)){

                            HWGWanLe data= new Gson().fromJson(msg, HWGWanLe.class);

                            if(data!=null){
                                if(data.status==1){
                                    if(data.cityList!=null){

                                        if( data.cityList.size()>0){
                                            adapter=new MyListAdapter(activity,data.cityList);
                                            listview.setAdapter(adapter);

                                            HWGWanLe.CityListBean rr=(HWGWanLe.CityListBean)adapter.getItem(0);
                                            if(rr!=null){
                                                if(!TextUtils.isEmpty(rr.cityId)){
                                                    category_id=rr.cityId;
                                                    loading();
                                                }
                                            }

                                            adapter.setSelectedPosition(0);
                                            adapter.notifyDataSetChanged();



                                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    HWGWanLe.CityListBean datasBean=(HWGWanLe.CityListBean)adapter.getItem(i);

                                                    if(!TextUtils.isEmpty(datasBean.cityId)){

                                                        Log.i("zds","datasBean.gcId=="+datasBean.cityId);
                                                        category_id=datasBean.cityId;
                                                        pageNo = 1;
                                                        isRefresh = true;
                                                        loading();
                                                        adapter.setSelectedPosition(i);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });

                                        }else {
                                            showToast(activity,"暂无数据！");
                                        }
                                    }
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


    private String category_id;
    private void loading() {

//       http://www.huaqiaobang.com/mobile/index.php?act=travel&op=travel_city_goods&city_id=55&page=5
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_wangles_left, "act=travel&op=travel_city_goods&city_id="+category_id+"&page="+pageNo+"&page_size=10" , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (isRefresh) {
                            mAdapter.clear();
                            isRefresh = false;
                        }

//                        {"state":0,"datas":"\u8d85\u51fa\u5f53\u524d\u9875\u6570\uff01"}
                        if(!TextUtils.isEmpty(msg)){

                            HWGWanLeDeTials data= new Gson().fromJson(msg, HWGWanLeDeTials.class);

                            if(data!=null){
                                if(data.status==1){
                                    if( data.goodsList!=null){

                                        if(data.goodsList.size()>0){
                                            mAdapter.addAll(data.goodsList);
                                            mAdapter.notifyDataSetChanged();
                                        }else {
                                            pageNo=-1;
                                            mAdapter.stopMore();
                                            return;
                                        }
                                        pageNo = pageNo+1;
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }else if(data.status==-1){
                                    mAdapter.stopMore();
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
    private MyListAdapter adapter;


    @Override
    public void onRefresh() {

        pageNo = 1;
        isRefresh = true;
        loading();

    }


    @Override
    public void onLoadMore() {
        loading();
    }


    public class MyListAdapter extends BaseAdapter {

        Context context;
        public MyListAdapter(Context context,List<HWGWanLe.CityListBean> list) {
            this.context = context;
            this.list = list;
        }

        List<HWGWanLe.CityListBean> list=new ArrayList<>();



        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MyListAdapter.VieeHolder vieeHolder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.item_text,viewGroup,false);
                vieeHolder=new MyListAdapter.VieeHolder(view);
                view.setTag(vieeHolder);
            }else {
                vieeHolder=(MyListAdapter.VieeHolder) view.getTag();
            }

            HWGWanLe.CityListBean bean=(HWGWanLe.CityListBean) getItem(i);

            vieeHolder.tv.setText(bean.cateName+" ("+bean.goods_count+")");

            if (selectedPosition == i) {
                vieeHolder.rela_bg.setSelected(true);
                vieeHolder.rela_bg.setPressed(true);
                vieeHolder.rela_bg.setBackgroundColor(getResources().getColor(R.color.hwg_bg));
            } else {
                vieeHolder.rela_bg.setSelected(false);
                vieeHolder.rela_bg.setPressed(false);
                vieeHolder.rela_bg.setBackgroundColor(Color.TRANSPARENT);

            }

            return view;


        }
        public class VieeHolder{
            TextView tv;
            RelativeLayout rela_bg;

            public VieeHolder(View view) {

                tv=(TextView) view.findViewById(R.id.tv);
                rela_bg=(RelativeLayout) view.findViewById(R.id.rela_bg);
            }
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        private int selectedPosition = -1;// 选中的位置

    }


    public static MainFragmentWanLe newInstance(String plateId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("plate_id", plateId);
        bundle.putSerializable("objectName", objectName);
        MainFragmentWanLe fragment = new MainFragmentWanLe();
        fragment.setArguments(bundle);
        return fragment;
    }

    ACache aCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();
        aCache = ACache.get(activity);
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.local_activity_wanle, null);
//            ButterKnife.inject(this, view);

            Bundle bundle = getArguments();
            if (bundle != null) {
                plate_id = bundle.getString("plate_id");
                objectName = bundle.getString("objectName");
            }
            picWith = Util.WIDTH;
            picHeight = picWith * 33 / 72;
            picHeight2 = picWith * 39 / 72;

            listview=(ListView)  view.findViewById(R.id.listview);
            rvList=(EasyRecyclerView)  view.findViewById(R.id.listview_datas);

            initListener();

            getLeftDatas(plate_id);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }


        return view;
    }


    private void initListener() {

        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rvList.setRefreshListener(this);
        rvList.setAdapter(mAdapter = new HWGWanLeDetialAdapter(activity));
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HWGWanLeDeTials.GoodsListBean bean=(HWGWanLeDeTials.GoodsListBean)mAdapter.getAllData().get(position);
                Intent it=new Intent(activity,GoodsDetailActivity.class);
                it.putExtra("sid",bean.goodsId);
                it.putExtra("pic", bean.goodsImage);
                it.putExtra("isYun",false);
                startActivity(it);
            }
        });

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
